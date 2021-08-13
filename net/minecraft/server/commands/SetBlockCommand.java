package net.minecraft.server.commands;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import java.util.function.Predicate;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class SetBlockCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("setblock").requires(db -> db.hasPermission(2))).then(Commands.argument("pos", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("block", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockStateArgument.block()).executes(commandContext -> setBlock((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, null))).then(Commands.literal("destroy").executes(commandContext -> setBlock((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.DESTROY, null)))).then(Commands.literal("keep").executes(commandContext -> setBlock((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, (Predicate<BlockInWorld>)(cei -> cei.getLevel().isEmptyBlock(cei.getPos())))))).then(Commands.literal("replace").executes(commandContext -> setBlock((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "pos"), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, null))))));
    }
    
    private static int setBlock(final CommandSourceStack db, final BlockPos fx, final BlockInput ef, final Mode b, @Nullable final Predicate<BlockInWorld> predicate) throws CommandSyntaxException {
        final ServerLevel aag6 = db.getLevel();
        if (predicate != null && !predicate.test(new BlockInWorld(aag6, fx, true))) {
            throw SetBlockCommand.ERROR_FAILED.create();
        }
        boolean boolean7;
        if (b == Mode.DESTROY) {
            aag6.destroyBlock(fx, true);
            boolean7 = (!ef.getState().isAir() || !aag6.getBlockState(fx).isAir());
        }
        else {
            final BlockEntity ccg8 = aag6.getBlockEntity(fx);
            Clearable.tryClear(ccg8);
            boolean7 = true;
        }
        if (boolean7 && !ef.place(aag6, fx, 2)) {
            throw SetBlockCommand.ERROR_FAILED.create();
        }
        aag6.blockUpdated(fx, ef.getState().getBlock());
        db.sendSuccess(new TranslatableComponent("commands.setblock.success", new Object[] { fx.getX(), fx.getY(), fx.getZ() }), true);
        return 1;
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.setblock.failed"));
    }
    
    public enum Mode {
        REPLACE, 
        DESTROY;
    }
    
    public interface Filter {
        @Nullable
        BlockInput filter(final BoundingBox cqx, final BlockPos fx, final BlockInput ef, final ServerLevel aag);
    }
}
