package net.minecraft.world.level.block.piston;

import java.util.Collection;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PistonStructureResolver {
    private final Level level;
    private final BlockPos pistonPos;
    private final boolean extending;
    private final BlockPos startPos;
    private final Direction pushDirection;
    private final List<BlockPos> toPush;
    private final List<BlockPos> toDestroy;
    private final Direction pistonDirection;
    
    public PistonStructureResolver(final Level bru, final BlockPos fx, final Direction gc, final boolean boolean4) {
        this.toPush = (List<BlockPos>)Lists.newArrayList();
        this.toDestroy = (List<BlockPos>)Lists.newArrayList();
        this.level = bru;
        this.pistonPos = fx;
        this.pistonDirection = gc;
        this.extending = boolean4;
        if (boolean4) {
            this.pushDirection = gc;
            this.startPos = fx.relative(gc);
        }
        else {
            this.pushDirection = gc.getOpposite();
            this.startPos = fx.relative(gc, 2);
        }
    }
    
    public boolean resolve() {
        this.toPush.clear();
        this.toDestroy.clear();
        final BlockState cee2 = this.level.getBlockState(this.startPos);
        if (!PistonBaseBlock.isPushable(cee2, this.level, this.startPos, this.pushDirection, false, this.pistonDirection)) {
            if (this.extending && cee2.getPistonPushReaction() == PushReaction.DESTROY) {
                this.toDestroy.add(this.startPos);
                return true;
            }
            return false;
        }
        else {
            if (!this.addBlockLine(this.startPos, this.pushDirection)) {
                return false;
            }
            for (int integer3 = 0; integer3 < this.toPush.size(); ++integer3) {
                final BlockPos fx4 = (BlockPos)this.toPush.get(integer3);
                if (isSticky(this.level.getBlockState(fx4).getBlock()) && !this.addBranchingBlocks(fx4)) {
                    return false;
                }
            }
            return true;
        }
    }
    
    private static boolean isSticky(final Block bul) {
        return bul == Blocks.SLIME_BLOCK || bul == Blocks.HONEY_BLOCK;
    }
    
    private static boolean canStickToEachOther(final Block bul1, final Block bul2) {
        return (bul1 != Blocks.HONEY_BLOCK || bul2 != Blocks.SLIME_BLOCK) && (bul1 != Blocks.SLIME_BLOCK || bul2 != Blocks.HONEY_BLOCK) && (isSticky(bul1) || isSticky(bul2));
    }
    
    private boolean addBlockLine(final BlockPos fx, final Direction gc) {
        BlockState cee4 = this.level.getBlockState(fx);
        Block bul5 = cee4.getBlock();
        if (cee4.isAir()) {
            return true;
        }
        if (!PistonBaseBlock.isPushable(cee4, this.level, fx, this.pushDirection, false, gc)) {
            return true;
        }
        if (fx.equals(this.pistonPos)) {
            return true;
        }
        if (this.toPush.contains(fx)) {
            return true;
        }
        int integer6 = 1;
        if (integer6 + this.toPush.size() > 12) {
            return false;
        }
        while (isSticky(bul5)) {
            final BlockPos fx2 = fx.relative(this.pushDirection.getOpposite(), integer6);
            final Block bul6 = bul5;
            cee4 = this.level.getBlockState(fx2);
            bul5 = cee4.getBlock();
            if (cee4.isAir() || !canStickToEachOther(bul6, bul5) || !PistonBaseBlock.isPushable(cee4, this.level, fx2, this.pushDirection, false, this.pushDirection.getOpposite())) {
                break;
            }
            if (fx2.equals(this.pistonPos)) {
                break;
            }
            if (++integer6 + this.toPush.size() > 12) {
                return false;
            }
        }
        int integer7 = 0;
        for (int integer8 = integer6 - 1; integer8 >= 0; --integer8) {
            this.toPush.add(fx.relative(this.pushDirection.getOpposite(), integer8));
            ++integer7;
        }
        int integer8 = 1;
        while (true) {
            final BlockPos fx3 = fx.relative(this.pushDirection, integer8);
            final int integer9 = this.toPush.indexOf(fx3);
            if (integer9 > -1) {
                this.reorderListAtCollision(integer7, integer9);
                for (int integer10 = 0; integer10 <= integer9 + integer7; ++integer10) {
                    final BlockPos fx4 = (BlockPos)this.toPush.get(integer10);
                    if (isSticky(this.level.getBlockState(fx4).getBlock()) && !this.addBranchingBlocks(fx4)) {
                        return false;
                    }
                }
                return true;
            }
            cee4 = this.level.getBlockState(fx3);
            if (cee4.isAir()) {
                return true;
            }
            if (!PistonBaseBlock.isPushable(cee4, this.level, fx3, this.pushDirection, true, this.pushDirection) || fx3.equals(this.pistonPos)) {
                return false;
            }
            if (cee4.getPistonPushReaction() == PushReaction.DESTROY) {
                this.toDestroy.add(fx3);
                return true;
            }
            if (this.toPush.size() >= 12) {
                return false;
            }
            this.toPush.add(fx3);
            ++integer7;
            ++integer8;
        }
    }
    
    private void reorderListAtCollision(final int integer1, final int integer2) {
        final List<BlockPos> list4 = (List<BlockPos>)Lists.newArrayList();
        final List<BlockPos> list5 = (List<BlockPos>)Lists.newArrayList();
        final List<BlockPos> list6 = (List<BlockPos>)Lists.newArrayList();
        list4.addAll((Collection)this.toPush.subList(0, integer2));
        list5.addAll((Collection)this.toPush.subList(this.toPush.size() - integer1, this.toPush.size()));
        list6.addAll((Collection)this.toPush.subList(integer2, this.toPush.size() - integer1));
        this.toPush.clear();
        this.toPush.addAll((Collection)list4);
        this.toPush.addAll((Collection)list5);
        this.toPush.addAll((Collection)list6);
    }
    
    private boolean addBranchingBlocks(final BlockPos fx) {
        final BlockState cee3 = this.level.getBlockState(fx);
        for (final Direction gc7 : Direction.values()) {
            if (gc7.getAxis() != this.pushDirection.getAxis()) {
                final BlockPos fx2 = fx.relative(gc7);
                final BlockState cee4 = this.level.getBlockState(fx2);
                if (canStickToEachOther(cee4.getBlock(), cee3.getBlock())) {
                    if (!this.addBlockLine(fx2, gc7)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public List<BlockPos> getToPush() {
        return this.toPush;
    }
    
    public List<BlockPos> getToDestroy() {
        return this.toDestroy;
    }
}
