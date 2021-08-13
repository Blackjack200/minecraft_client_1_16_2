package net.minecraft.server.commands;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import java.util.Collections;
import net.minecraft.world.level.block.Blocks;
import com.mojang.brigadier.Message;
import net.minecraft.core.Vec3i;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Iterator;
import net.minecraft.server.level.ServerLevel;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import java.util.function.Predicate;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.arguments.blocks.BlockInput;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

public class FillCommand {
    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
    private static final BlockInput HOLLOW_CORE;
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("fill").requires(db -> db.hasPermission(2))).then(Commands.argument("from", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(Commands.argument("to", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("block", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockStateArgument.block()).executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, null))).then(((LiteralArgumentBuilder)Commands.literal("replace").executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, null))).then(Commands.argument("filter", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPredicateArgument.blockPredicate()).executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, BlockPredicateArgument.getBlockPredicate((CommandContext<CommandSourceStack>)commandContext, "filter")))))).then(Commands.literal("keep").executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.REPLACE, (Predicate<BlockInWorld>)(cei -> cei.getLevel().isEmptyBlock(cei.getPos())))))).then(Commands.literal("outline").executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.OUTLINE, null)))).then(Commands.literal("hollow").executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.HOLLOW, null)))).then(Commands.literal("destroy").executes(commandContext -> fillBlocks((CommandSourceStack)commandContext.getSource(), new BoundingBox(BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "from"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "to")), BlockStateArgument.getBlock((CommandContext<CommandSourceStack>)commandContext, "block"), Mode.DESTROY, null)))))));
    }
    
    private static int fillBlocks(final CommandSourceStack db, final BoundingBox cqx, final BlockInput ef, final Mode a, @Nullable final Predicate<BlockInWorld> predicate) throws CommandSyntaxException {
        final int integer6 = cqx.getXSpan() * cqx.getYSpan() * cqx.getZSpan();
        if (integer6 > 32768) {
            throw FillCommand.ERROR_AREA_TOO_LARGE.create(32768, integer6);
        }
        final List<BlockPos> list7 = (List<BlockPos>)Lists.newArrayList();
        final ServerLevel aag8 = db.getLevel();
        int integer7 = 0;
        for (final BlockPos fx11 : BlockPos.betweenClosed(cqx.x0, cqx.y0, cqx.z0, cqx.x1, cqx.y1, cqx.z1)) {
            if (predicate != null && !predicate.test(new BlockInWorld(aag8, fx11, true))) {
                continue;
            }
            final BlockInput ef2 = a.filter.filter(cqx, fx11, ef, aag8);
            if (ef2 == null) {
                continue;
            }
            final BlockEntity ccg13 = aag8.getBlockEntity(fx11);
            Clearable.tryClear(ccg13);
            if (!ef2.place(aag8, fx11, 2)) {
                continue;
            }
            list7.add(fx11.immutable());
            ++integer7;
        }
        for (final BlockPos fx11 : list7) {
            final Block bul12 = aag8.getBlockState(fx11).getBlock();
            aag8.blockUpdated(fx11, bul12);
        }
        if (integer7 == 0) {
            throw FillCommand.ERROR_FAILED.create();
        }
        db.sendSuccess(new TranslatableComponent("commands.fill.success", new Object[] { integer7 }), true);
        return integer7;
    }
    
    static {
        ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.fill.toobig", new Object[] { object1, object2 }));
        HOLLOW_CORE = new BlockInput(Blocks.AIR.defaultBlockState(), (Set<Property<?>>)Collections.emptySet(), null);
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.fill.failed"));
    }
    
    enum Mode {
        REPLACE((cqx, fx, ef, aag) -> ef), 
        OUTLINE((cqx, fx, ef, aag) -> {
            if (fx.getX() == cqx.x0 || fx.getX() == cqx.x1 || fx.getY() == cqx.y0 || fx.getY() == cqx.y1 || fx.getZ() == cqx.z0 || fx.getZ() == cqx.z1) {
                return ef;
            }
            else {
                return null;
            }
        }), 
        HOLLOW((cqx, fx, ef, aag) -> {
            if (fx.getX() == cqx.x0 || fx.getX() == cqx.x1 || fx.getY() == cqx.y0 || fx.getY() == cqx.y1 || fx.getZ() == cqx.z0 || fx.getZ() == cqx.z1) {
                return ef;
            }
            else {
                return FillCommand.HOLLOW_CORE;
            }
        }), 
        DESTROY((cqx, fx, ef, aag) -> {
            aag.destroyBlock(fx, true);
            return ef;
        });
        
        public final SetBlockCommand.Filter filter;
        
        private Mode(final SetBlockCommand.Filter a) {
            this.filter = a;
        }
    }
}
