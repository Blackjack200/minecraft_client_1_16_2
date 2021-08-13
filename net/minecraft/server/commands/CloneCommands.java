package net.minecraft.server.commands;

import javax.annotation.Nullable;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Deque;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.List;
import java.util.Collection;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import com.google.common.collect.Lists;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import java.util.function.Predicate;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CloneCommands {
    private static final SimpleCommandExceptionType ERROR_OVERLAP;
    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
    private static final SimpleCommandExceptionType ERROR_FAILED;
    public static final Predicate<BlockInWorld> FILTER_AIR;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("clone").requires(db -> db.hasPermission(2))).then(Commands.argument("begin", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(Commands.argument("end", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("destination", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPosArgument.blockPos()).executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), (Predicate<BlockInWorld>)(cei -> true), Mode.NORMAL))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("replace").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), (Predicate<BlockInWorld>)(cei -> true), Mode.NORMAL))).then(Commands.literal("force").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), (Predicate<BlockInWorld>)(cei -> true), Mode.FORCE)))).then(Commands.literal("move").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), (Predicate<BlockInWorld>)(cei -> true), Mode.MOVE)))).then(Commands.literal("normal").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), (Predicate<BlockInWorld>)(cei -> true), Mode.NORMAL))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("masked").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), CloneCommands.FILTER_AIR, Mode.NORMAL))).then(Commands.literal("force").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), CloneCommands.FILTER_AIR, Mode.FORCE)))).then(Commands.literal("move").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), CloneCommands.FILTER_AIR, Mode.MOVE)))).then(Commands.literal("normal").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), CloneCommands.FILTER_AIR, Mode.NORMAL))))).then(Commands.literal("filtered").then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("filter", (com.mojang.brigadier.arguments.ArgumentType<Object>)BlockPredicateArgument.blockPredicate()).executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), BlockPredicateArgument.getBlockPredicate((CommandContext<CommandSourceStack>)commandContext, "filter"), Mode.NORMAL))).then(Commands.literal("force").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), BlockPredicateArgument.getBlockPredicate((CommandContext<CommandSourceStack>)commandContext, "filter"), Mode.FORCE)))).then(Commands.literal("move").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), BlockPredicateArgument.getBlockPredicate((CommandContext<CommandSourceStack>)commandContext, "filter"), Mode.MOVE)))).then(Commands.literal("normal").executes(commandContext -> clone((CommandSourceStack)commandContext.getSource(), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "begin"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "end"), BlockPosArgument.getLoadedBlockPos((CommandContext<CommandSourceStack>)commandContext, "destination"), BlockPredicateArgument.getBlockPredicate((CommandContext<CommandSourceStack>)commandContext, "filter"), Mode.NORMAL)))))))));
    }
    
    private static int clone(final CommandSourceStack db, final BlockPos fx2, final BlockPos fx3, final BlockPos fx4, final Predicate<BlockInWorld> predicate, final Mode b) throws CommandSyntaxException {
        final BoundingBox cqx7 = new BoundingBox(fx2, fx3);
        final BlockPos fx5 = fx4.offset(cqx7.getLength());
        final BoundingBox cqx8 = new BoundingBox(fx4, fx5);
        if (!b.canOverlap() && cqx8.intersects(cqx7)) {
            throw CloneCommands.ERROR_OVERLAP.create();
        }
        final int integer10 = cqx7.getXSpan() * cqx7.getYSpan() * cqx7.getZSpan();
        if (integer10 > 32768) {
            throw CloneCommands.ERROR_AREA_TOO_LARGE.create(32768, integer10);
        }
        final ServerLevel aag11 = db.getLevel();
        if (!aag11.hasChunksAt(fx2, fx3) || !aag11.hasChunksAt(fx4, fx5)) {
            throw BlockPosArgument.ERROR_NOT_LOADED.create();
        }
        final List<CloneBlockInfo> list12 = (List<CloneBlockInfo>)Lists.newArrayList();
        final List<CloneBlockInfo> list13 = (List<CloneBlockInfo>)Lists.newArrayList();
        final List<CloneBlockInfo> list14 = (List<CloneBlockInfo>)Lists.newArrayList();
        final Deque<BlockPos> deque15 = (Deque<BlockPos>)Lists.newLinkedList();
        final BlockPos fx6 = new BlockPos(cqx8.x0 - cqx7.x0, cqx8.y0 - cqx7.y0, cqx8.z0 - cqx7.z0);
        for (int integer11 = cqx7.z0; integer11 <= cqx7.z1; ++integer11) {
            for (int integer12 = cqx7.y0; integer12 <= cqx7.y1; ++integer12) {
                for (int integer13 = cqx7.x0; integer13 <= cqx7.x1; ++integer13) {
                    final BlockPos fx7 = new BlockPos(integer13, integer12, integer11);
                    final BlockPos fx8 = fx7.offset(fx6);
                    final BlockInWorld cei22 = new BlockInWorld(aag11, fx7, false);
                    final BlockState cee23 = cei22.getState();
                    if (predicate.test(cei22)) {
                        final BlockEntity ccg24 = aag11.getBlockEntity(fx7);
                        if (ccg24 != null) {
                            final CompoundTag md25 = ccg24.save(new CompoundTag());
                            list13.add(new CloneBlockInfo(fx8, cee23, md25));
                            deque15.addLast(fx7);
                        }
                        else if (cee23.isSolidRender(aag11, fx7) || cee23.isCollisionShapeFullBlock(aag11, fx7)) {
                            list12.add(new CloneBlockInfo(fx8, cee23, null));
                            deque15.addLast(fx7);
                        }
                        else {
                            list14.add(new CloneBlockInfo(fx8, cee23, null));
                            deque15.addFirst(fx7);
                        }
                    }
                }
            }
        }
        if (b == Mode.MOVE) {
            for (final BlockPos fx9 : deque15) {
                final BlockEntity ccg25 = aag11.getBlockEntity(fx9);
                Clearable.tryClear(ccg25);
                aag11.setBlock(fx9, Blocks.BARRIER.defaultBlockState(), 2);
            }
            for (final BlockPos fx9 : deque15) {
                aag11.setBlock(fx9, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        final List<CloneBlockInfo> list15 = (List<CloneBlockInfo>)Lists.newArrayList();
        list15.addAll((Collection)list12);
        list15.addAll((Collection)list13);
        list15.addAll((Collection)list14);
        final List<CloneBlockInfo> list16 = (List<CloneBlockInfo>)Lists.reverse((List)list15);
        for (final CloneBlockInfo a20 : list16) {
            final BlockEntity ccg26 = aag11.getBlockEntity(a20.pos);
            Clearable.tryClear(ccg26);
            aag11.setBlock(a20.pos, Blocks.BARRIER.defaultBlockState(), 2);
        }
        int integer13 = 0;
        for (final CloneBlockInfo a21 : list15) {
            if (aag11.setBlock(a21.pos, a21.state, 2)) {
                ++integer13;
            }
        }
        for (final CloneBlockInfo a21 : list13) {
            final BlockEntity ccg27 = aag11.getBlockEntity(a21.pos);
            if (a21.tag != null && ccg27 != null) {
                a21.tag.putInt("x", a21.pos.getX());
                a21.tag.putInt("y", a21.pos.getY());
                a21.tag.putInt("z", a21.pos.getZ());
                ccg27.load(a21.state, a21.tag);
                ccg27.setChanged();
            }
            aag11.setBlock(a21.pos, a21.state, 2);
        }
        for (final CloneBlockInfo a21 : list16) {
            aag11.blockUpdated(a21.pos, a21.state.getBlock());
        }
        aag11.getBlockTicks().copy(cqx7, fx6);
        if (integer13 == 0) {
            throw CloneCommands.ERROR_FAILED.create();
        }
        db.sendSuccess(new TranslatableComponent("commands.clone.success", new Object[] { integer13 }), true);
        return integer13;
    }
    
    static {
        ERROR_OVERLAP = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.clone.overlap"));
        ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("commands.clone.toobig", new Object[] { object1, object2 }));
        ERROR_FAILED = new SimpleCommandExceptionType((Message)new TranslatableComponent("commands.clone.failed"));
        FILTER_AIR = (cei -> !cei.getState().isAir());
    }
    
    enum Mode {
        FORCE(true), 
        MOVE(true), 
        NORMAL(false);
        
        private final boolean canOverlap;
        
        private Mode(final boolean boolean3) {
            this.canOverlap = boolean3;
        }
        
        public boolean canOverlap() {
            return this.canOverlap;
        }
    }
    
    static class CloneBlockInfo {
        public final BlockPos pos;
        public final BlockState state;
        @Nullable
        public final CompoundTag tag;
        
        public CloneBlockInfo(final BlockPos fx, final BlockState cee, @Nullable final CompoundTag md) {
            this.pos = fx;
            this.state = cee;
            this.tag = md;
        }
    }
}
