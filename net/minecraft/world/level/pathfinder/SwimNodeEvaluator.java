package net.minecraft.world.level.pathfinder;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class SwimNodeEvaluator extends NodeEvaluator {
    private final boolean allowBreaching;
    
    public SwimNodeEvaluator(final boolean boolean1) {
        this.allowBreaching = boolean1;
    }
    
    @Override
    public Node getStart() {
        return super.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5), Mth.floor(this.mob.getBoundingBox().minZ));
    }
    
    @Override
    public Target getGoal(final double double1, final double double2, final double double3) {
        return new Target(super.getNode(Mth.floor(double1 - this.mob.getBbWidth() / 2.0f), Mth.floor(double2 + 0.5), Mth.floor(double3 - this.mob.getBbWidth() / 2.0f)));
    }
    
    @Override
    public int getNeighbors(final Node[] arr, final Node cwy) {
        int integer4 = 0;
        for (final Direction gc8 : Direction.values()) {
            final Node cwy2 = this.getWaterNode(cwy.x + gc8.getStepX(), cwy.y + gc8.getStepY(), cwy.z + gc8.getStepZ());
            if (cwy2 != null && !cwy2.closed) {
                arr[integer4++] = cwy2;
            }
        }
        return integer4;
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4, final Mob aqk, final int integer6, final int integer7, final int integer8, final boolean boolean9, final boolean boolean10) {
        return this.getBlockPathType(bqz, integer2, integer3, integer4);
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4) {
        final BlockPos fx6 = new BlockPos(integer2, integer3, integer4);
        final FluidState cuu7 = bqz.getFluidState(fx6);
        final BlockState cee8 = bqz.getBlockState(fx6);
        if (cuu7.isEmpty() && cee8.isPathfindable(bqz, fx6.below(), PathComputationType.WATER) && cee8.isAir()) {
            return BlockPathTypes.BREACH;
        }
        if (!cuu7.is(FluidTags.WATER) || !cee8.isPathfindable(bqz, fx6, PathComputationType.WATER)) {
            return BlockPathTypes.BLOCKED;
        }
        return BlockPathTypes.WATER;
    }
    
    @Nullable
    private Node getWaterNode(final int integer1, final int integer2, final int integer3) {
        final BlockPathTypes cww5 = this.isFree(integer1, integer2, integer3);
        if ((this.allowBreaching && cww5 == BlockPathTypes.BREACH) || cww5 == BlockPathTypes.WATER) {
            return this.getNode(integer1, integer2, integer3);
        }
        return null;
    }
    
    @Nullable
    @Override
    protected Node getNode(final int integer1, final int integer2, final int integer3) {
        Node cwy5 = null;
        final BlockPathTypes cww6 = this.getBlockPathType(this.mob.level, integer1, integer2, integer3);
        final float float7 = this.mob.getPathfindingMalus(cww6);
        if (float7 >= 0.0f) {
            cwy5 = super.getNode(integer1, integer2, integer3);
            cwy5.type = cww6;
            cwy5.costMalus = Math.max(cwy5.costMalus, float7);
            if (this.level.getFluidState(new BlockPos(integer1, integer2, integer3)).isEmpty()) {
                final Node node = cwy5;
                node.costMalus += 8.0f;
            }
        }
        if (cww6 == BlockPathTypes.OPEN) {
            return cwy5;
        }
        return cwy5;
    }
    
    private BlockPathTypes isFree(final int integer1, final int integer2, final int integer3) {
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        for (int integer4 = integer1; integer4 < integer1 + this.entityWidth; ++integer4) {
            for (int integer5 = integer2; integer5 < integer2 + this.entityHeight; ++integer5) {
                for (int integer6 = integer3; integer6 < integer3 + this.entityDepth; ++integer6) {
                    final FluidState cuu9 = this.level.getFluidState(a5.set(integer4, integer5, integer6));
                    final BlockState cee10 = this.level.getBlockState(a5.set(integer4, integer5, integer6));
                    if (cuu9.isEmpty() && cee10.isPathfindable(this.level, a5.below(), PathComputationType.WATER) && cee10.isAir()) {
                        return BlockPathTypes.BREACH;
                    }
                    if (!cuu9.is(FluidTags.WATER)) {
                        return BlockPathTypes.BLOCKED;
                    }
                }
            }
        }
        final BlockState cee11 = this.level.getBlockState(a5);
        if (cee11.isPathfindable(this.level, a5, PathComputationType.WATER)) {
            return BlockPathTypes.WATER;
        }
        return BlockPathTypes.BLOCKED;
    }
}
