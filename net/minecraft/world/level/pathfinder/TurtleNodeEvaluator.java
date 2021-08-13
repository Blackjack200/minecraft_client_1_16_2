package net.minecraft.world.level.pathfinder;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BaseRailBlock;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;

public class TurtleNodeEvaluator extends WalkNodeEvaluator {
    private float oldWalkableCost;
    private float oldWaterBorderCost;
    
    @Override
    public void prepare(final PathNavigationRegion bsf, final Mob aqk) {
        super.prepare(bsf, aqk);
        aqk.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.oldWalkableCost = aqk.getPathfindingMalus(BlockPathTypes.WALKABLE);
        aqk.setPathfindingMalus(BlockPathTypes.WALKABLE, 6.0f);
        this.oldWaterBorderCost = aqk.getPathfindingMalus(BlockPathTypes.WATER_BORDER);
        aqk.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 4.0f);
    }
    
    @Override
    public void done() {
        this.mob.setPathfindingMalus(BlockPathTypes.WALKABLE, this.oldWalkableCost);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER_BORDER, this.oldWaterBorderCost);
        super.done();
    }
    
    @Override
    public Node getStart() {
        return this.getNode(Mth.floor(this.mob.getBoundingBox().minX), Mth.floor(this.mob.getBoundingBox().minY + 0.5), Mth.floor(this.mob.getBoundingBox().minZ));
    }
    
    @Override
    public Target getGoal(final double double1, final double double2, final double double3) {
        return new Target(this.getNode(Mth.floor(double1), Mth.floor(double2 + 0.5), Mth.floor(double3)));
    }
    
    @Override
    public int getNeighbors(final Node[] arr, final Node cwy) {
        int integer4 = 0;
        final int integer5 = 1;
        final BlockPos fx6 = new BlockPos(cwy.x, cwy.y, cwy.z);
        final double double7 = this.inWaterDependentPosHeight(fx6);
        final Node cwy2 = this.getAcceptedNode(cwy.x, cwy.y, cwy.z + 1, 1, double7);
        final Node cwy3 = this.getAcceptedNode(cwy.x - 1, cwy.y, cwy.z, 1, double7);
        final Node cwy4 = this.getAcceptedNode(cwy.x + 1, cwy.y, cwy.z, 1, double7);
        final Node cwy5 = this.getAcceptedNode(cwy.x, cwy.y, cwy.z - 1, 1, double7);
        final Node cwy6 = this.getAcceptedNode(cwy.x, cwy.y + 1, cwy.z, 0, double7);
        final Node cwy7 = this.getAcceptedNode(cwy.x, cwy.y - 1, cwy.z, 1, double7);
        if (cwy2 != null && !cwy2.closed) {
            arr[integer4++] = cwy2;
        }
        if (cwy3 != null && !cwy3.closed) {
            arr[integer4++] = cwy3;
        }
        if (cwy4 != null && !cwy4.closed) {
            arr[integer4++] = cwy4;
        }
        if (cwy5 != null && !cwy5.closed) {
            arr[integer4++] = cwy5;
        }
        if (cwy6 != null && !cwy6.closed) {
            arr[integer4++] = cwy6;
        }
        if (cwy7 != null && !cwy7.closed) {
            arr[integer4++] = cwy7;
        }
        final boolean boolean15 = cwy5 == null || cwy5.type == BlockPathTypes.OPEN || cwy5.costMalus != 0.0f;
        final boolean boolean16 = cwy2 == null || cwy2.type == BlockPathTypes.OPEN || cwy2.costMalus != 0.0f;
        final boolean boolean17 = cwy4 == null || cwy4.type == BlockPathTypes.OPEN || cwy4.costMalus != 0.0f;
        final boolean boolean18 = cwy3 == null || cwy3.type == BlockPathTypes.OPEN || cwy3.costMalus != 0.0f;
        if (boolean15 && boolean18) {
            final Node cwy8 = this.getAcceptedNode(cwy.x - 1, cwy.y, cwy.z - 1, 1, double7);
            if (cwy8 != null && !cwy8.closed) {
                arr[integer4++] = cwy8;
            }
        }
        if (boolean15 && boolean17) {
            final Node cwy8 = this.getAcceptedNode(cwy.x + 1, cwy.y, cwy.z - 1, 1, double7);
            if (cwy8 != null && !cwy8.closed) {
                arr[integer4++] = cwy8;
            }
        }
        if (boolean16 && boolean18) {
            final Node cwy8 = this.getAcceptedNode(cwy.x - 1, cwy.y, cwy.z + 1, 1, double7);
            if (cwy8 != null && !cwy8.closed) {
                arr[integer4++] = cwy8;
            }
        }
        if (boolean16 && boolean17) {
            final Node cwy8 = this.getAcceptedNode(cwy.x + 1, cwy.y, cwy.z + 1, 1, double7);
            if (cwy8 != null && !cwy8.closed) {
                arr[integer4++] = cwy8;
            }
        }
        return integer4;
    }
    
    private double inWaterDependentPosHeight(final BlockPos fx) {
        if (!this.mob.isInWater()) {
            final BlockPos fx2 = fx.below();
            final VoxelShape dde4 = this.level.getBlockState(fx2).getCollisionShape(this.level, fx2);
            return fx2.getY() + (dde4.isEmpty() ? 0.0 : dde4.max(Direction.Axis.Y));
        }
        return fx.getY() + 0.5;
    }
    
    @Nullable
    private Node getAcceptedNode(final int integer1, int integer2, final int integer3, final int integer4, final double double5) {
        Node cwy8 = null;
        final BlockPos fx9 = new BlockPos(integer1, integer2, integer3);
        final double double6 = this.inWaterDependentPosHeight(fx9);
        if (double6 - double5 > 1.125) {
            return null;
        }
        BlockPathTypes cww12 = this.getBlockPathType(this.level, integer1, integer2, integer3, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, false, false);
        float float13 = this.mob.getPathfindingMalus(cww12);
        final double double7 = this.mob.getBbWidth() / 2.0;
        if (float13 >= 0.0f) {
            cwy8 = this.getNode(integer1, integer2, integer3);
            cwy8.type = cww12;
            cwy8.costMalus = Math.max(cwy8.costMalus, float13);
        }
        if (cww12 == BlockPathTypes.WATER || cww12 == BlockPathTypes.WALKABLE) {
            if (integer2 < this.mob.level.getSeaLevel() - 10 && cwy8 != null) {
                final Node node = cwy8;
                ++node.costMalus;
            }
            return cwy8;
        }
        if (cwy8 == null && integer4 > 0 && cww12 != BlockPathTypes.FENCE && cww12 != BlockPathTypes.UNPASSABLE_RAIL && cww12 != BlockPathTypes.TRAPDOOR) {
            cwy8 = this.getAcceptedNode(integer1, integer2 + 1, integer3, integer4 - 1, double5);
        }
        if (cww12 == BlockPathTypes.OPEN) {
            final AABB dcf16 = new AABB(integer1 - double7 + 0.5, integer2 + 0.001, integer3 - double7 + 0.5, integer1 + double7 + 0.5, integer2 + this.mob.getBbHeight(), integer3 + double7 + 0.5);
            if (!this.mob.level.noCollision(this.mob, dcf16)) {
                return null;
            }
            final BlockPathTypes cww13 = this.getBlockPathType(this.level, integer1, integer2 - 1, integer3, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, false, false);
            if (cww13 == BlockPathTypes.BLOCKED) {
                cwy8 = this.getNode(integer1, integer2, integer3);
                cwy8.type = BlockPathTypes.WALKABLE;
                cwy8.costMalus = Math.max(cwy8.costMalus, float13);
                return cwy8;
            }
            if (cww13 == BlockPathTypes.WATER) {
                cwy8 = this.getNode(integer1, integer2, integer3);
                cwy8.type = BlockPathTypes.WATER;
                cwy8.costMalus = Math.max(cwy8.costMalus, float13);
                return cwy8;
            }
            int integer5 = 0;
            while (integer2 > 0 && cww12 == BlockPathTypes.OPEN) {
                --integer2;
                if (integer5++ >= this.mob.getMaxFallDistance()) {
                    return null;
                }
                cww12 = this.getBlockPathType(this.level, integer1, integer2, integer3, this.mob, this.entityWidth, this.entityHeight, this.entityDepth, false, false);
                float13 = this.mob.getPathfindingMalus(cww12);
                if (cww12 != BlockPathTypes.OPEN && float13 >= 0.0f) {
                    cwy8 = this.getNode(integer1, integer2, integer3);
                    cwy8.type = cww12;
                    cwy8.costMalus = Math.max(cwy8.costMalus, float13);
                    break;
                }
                if (float13 < 0.0f) {
                    return null;
                }
            }
        }
        return cwy8;
    }
    
    @Override
    protected BlockPathTypes evaluateBlockPathType(final BlockGetter bqz, final boolean boolean2, final boolean boolean3, final BlockPos fx, BlockPathTypes cww) {
        if (cww == BlockPathTypes.RAIL && !(bqz.getBlockState(fx).getBlock() instanceof BaseRailBlock) && !(bqz.getBlockState(fx.below()).getBlock() instanceof BaseRailBlock)) {
            cww = BlockPathTypes.UNPASSABLE_RAIL;
        }
        if (cww == BlockPathTypes.DOOR_OPEN || cww == BlockPathTypes.DOOR_WOOD_CLOSED || cww == BlockPathTypes.DOOR_IRON_CLOSED) {
            cww = BlockPathTypes.BLOCKED;
        }
        if (cww == BlockPathTypes.LEAVES) {
            cww = BlockPathTypes.BLOCKED;
        }
        return cww;
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4) {
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        BlockPathTypes cww7 = WalkNodeEvaluator.getBlockPathTypeRaw(bqz, a6.set(integer2, integer3, integer4));
        if (cww7 == BlockPathTypes.WATER) {
            for (final Direction gc11 : Direction.values()) {
                final BlockPathTypes cww8 = WalkNodeEvaluator.getBlockPathTypeRaw(bqz, a6.set(integer2, integer3, integer4).move(gc11));
                if (cww8 == BlockPathTypes.BLOCKED) {
                    return BlockPathTypes.WATER_BORDER;
                }
            }
            return BlockPathTypes.WATER;
        }
        if (cww7 == BlockPathTypes.OPEN && integer3 >= 1) {
            final BlockState cee8 = bqz.getBlockState(new BlockPos(integer2, integer3 - 1, integer4));
            final BlockPathTypes cww9 = WalkNodeEvaluator.getBlockPathTypeRaw(bqz, a6.set(integer2, integer3 - 1, integer4));
            if (cww9 == BlockPathTypes.WALKABLE || cww9 == BlockPathTypes.OPEN || cww9 == BlockPathTypes.LAVA) {
                cww7 = BlockPathTypes.OPEN;
            }
            else {
                cww7 = BlockPathTypes.WALKABLE;
            }
            if (cww9 == BlockPathTypes.DAMAGE_FIRE || cee8.is(Blocks.MAGMA_BLOCK) || cee8.is(BlockTags.CAMPFIRES)) {
                cww7 = BlockPathTypes.DAMAGE_FIRE;
            }
            if (cww9 == BlockPathTypes.DAMAGE_CACTUS) {
                cww7 = BlockPathTypes.DAMAGE_CACTUS;
            }
            if (cww9 == BlockPathTypes.DAMAGE_OTHER) {
                cww7 = BlockPathTypes.DAMAGE_OTHER;
            }
        }
        if (cww7 == BlockPathTypes.WALKABLE) {
            cww7 = WalkNodeEvaluator.checkNeighbourBlocks(bqz, a6.set(integer2, integer3, integer4), cww7);
        }
        return cww7;
    }
}
