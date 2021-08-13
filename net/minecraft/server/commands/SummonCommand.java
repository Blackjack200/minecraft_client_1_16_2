package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntitySummonArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class SummonCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    private static final SimpleCommandExceptionType ERROR_DUPLICATE_UUID;
    private static final SimpleCommandExceptionType INVALID_POSITION;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("summon").requires(db -> db.hasPermission(2))).then(((RequiredArgumentBuilder)Commands.argument("entity", (com.mojang.brigadier.arguments.ArgumentType<Object>)EntitySummonArgument.id()).suggests((SuggestionProvider)SuggestionProviders.SUMMONABLE_ENTITIES).executes(commandContext -> spawnEntity((CommandSourceStack)commandContext.getSource(), EntitySummonArgument.getSummonableEntity((CommandContext<CommandSourceStack>)commandContext, "entity"), ((CommandSourceStack)commandContext.getSource()).getPosition(), new CompoundTag(), true))).then(((RequiredArgumentBuilder)Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)Vec3Argument.vec3()).executes(commandContext -> spawnEntity((CommandSourceStack)commandContext.getSource(), EntitySummonArgument.getSummonableEntity((CommandContext<CommandSourceStack>)commandContext, "entity"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), new CompoundTag(), true))).then(Commands.argument("nbt", (com.mojang.brigadier.arguments.ArgumentType<Object>)CompoundTagArgument.compoundTag()).executes(commandContext -> spawnEntity((CommandSourceStack)commandContext.getSource(), EntitySummonArgument.getSummonableEntity((CommandContext<CommandSourceStack>)commandContext, "entity"), Vec3Argument.getVec3((CommandContext<CommandSourceStack>)commandContext, "pos"), CompoundTagArgument.getCompoundTag((com.mojang.brigadier.context.CommandContext<Object>)commandContext, "nbt"), false))))));
    }
    
    private static int spawnEntity(final CommandSourceStack db, final ResourceLocation vk, final Vec3 dck, final CompoundTag md, final boolean boolean5) throws CommandSyntaxException {
        final BlockPos fx6 = new BlockPos(dck);
        if (!Level.isInSpawnableBounds(fx6)) {
            throw SummonCommand.INVALID_POSITION.create();
        }
        final CompoundTag md2 = md.copy();
        md2.putString("id", vk.toString());
        final ServerLevel aag8 = db.getLevel();
        final Entity apx9 = EntityType.loadEntityRecursive(md2, aag8, (Function<Entity, Entity>)(apx -> {
            apx.moveTo(dck.x, dck.y, dck.z, apx.yRot, apx.xRot);
            return apx;
        }));
        if (apx9 == null) {
            throw SummonCommand.ERROR_FAILED.create();
        }
        if (boolean5 && apx9 instanceof Mob) {
            ((Mob)apx9).finalizeSpawn(db.getLevel(), db.getLevel().getCurrentDifficultyAt(apx9.blockPosition()), MobSpawnType.COMMAND, null, null);
        }
        if (!aag8.tryAddFreshEntityWithPassengers(apx9)) {
            throw SummonCommand.ERROR_DUPLICATE_UUID.create();
        }
        db.sendSuccess(new TranslatableComponent("commands.summon.success", new Object[] { apx9.getDisplayName() }), true);
        return 1;
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.summon.failed"));
        ERROR_DUPLICATE_UUID = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.summon.failed.uuid"));
        INVALID_POSITION = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.summon.invalidPosition"));
    }
}
