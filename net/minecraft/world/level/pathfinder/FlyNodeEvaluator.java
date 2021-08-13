package net.minecraft.world.level.pathfinder;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.EnumSet;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;

public class FlyNodeEvaluator extends WalkNodeEvaluator {
    @Override
    public void prepare(final PathNavigationRegion bsf, final Mob aqk) {
        super.prepare(bsf, aqk);
        this.oldWaterCost = aqk.getPathfindingMalus(BlockPathTypes.WATER);
    }
    
    @Override
    public void done() {
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
        super.done();
    }
    
    @Override
    public Node getStart() {
        int integer2;
        if (this.canFloat() && this.mob.isInWater()) {
            integer2 = Mth.floor(this.mob.getY());
            final BlockPos.MutableBlockPos a3 = new BlockPos.MutableBlockPos(this.mob.getX(), integer2, this.mob.getZ());
            for (Block bul4 = this.level.getBlockState(a3).getBlock(); bul4 == Blocks.WATER; bul4 = this.level.getBlockState(a3).getBlock()) {
                ++integer2;
                a3.set(this.mob.getX(), integer2, this.mob.getZ());
            }
        }
        else {
            integer2 = Mth.floor(this.mob.getY() + 0.5);
        }
        final BlockPos fx3 = this.mob.blockPosition();
        final BlockPathTypes cww4 = this.getBlockPathType(this.mob, fx3.getX(), integer2, fx3.getZ());
        if (this.mob.getPathfindingMalus(cww4) < 0.0f) {
            final Set<BlockPos> set5 = (Set<BlockPos>)Sets.newHashSet();
            set5.add(new BlockPos(this.mob.getBoundingBox().minX, integer2, this.mob.getBoundingBox().minZ));
            set5.add(new BlockPos(this.mob.getBoundingBox().minX, integer2, this.mob.getBoundingBox().maxZ));
            set5.add(new BlockPos(this.mob.getBoundingBox().maxX, integer2, this.mob.getBoundingBox().minZ));
            set5.add(new BlockPos(this.mob.getBoundingBox().maxX, integer2, this.mob.getBoundingBox().maxZ));
            for (final BlockPos fx4 : set5) {
                final BlockPathTypes cww5 = this.getBlockPathType(this.mob, fx4);
                if (this.mob.getPathfindingMalus(cww5) >= 0.0f) {
                    return super.getNode(fx4.getX(), fx4.getY(), fx4.getZ());
                }
            }
        }
        return super.getNode(fx3.getX(), integer2, fx3.getZ());
    }
    
    @Override
    public Target getGoal(final double double1, final double double2, final double double3) {
        return new Target(super.getNode(Mth.floor(double1), Mth.floor(double2), Mth.floor(double3)));
    }
    
    @Override
    public int getNeighbors(final Node[] arr, final Node cwy) {
        int integer4 = 0;
        final Node cwy2 = this.getNode(cwy.x, cwy.y, cwy.z + 1);
        if (this.isOpen(cwy2)) {
            arr[integer4++] = cwy2;
        }
        final Node cwy3 = this.getNode(cwy.x - 1, cwy.y, cwy.z);
        if (this.isOpen(cwy3)) {
            arr[integer4++] = cwy3;
        }
        final Node cwy4 = this.getNode(cwy.x + 1, cwy.y, cwy.z);
        if (this.isOpen(cwy4)) {
            arr[integer4++] = cwy4;
        }
        final Node cwy5 = this.getNode(cwy.x, cwy.y, cwy.z - 1);
        if (this.isOpen(cwy5)) {
            arr[integer4++] = cwy5;
        }
        final Node cwy6 = this.getNode(cwy.x, cwy.y + 1, cwy.z);
        if (this.isOpen(cwy6)) {
            arr[integer4++] = cwy6;
        }
        final Node cwy7 = this.getNode(cwy.x, cwy.y - 1, cwy.z);
        if (this.isOpen(cwy7)) {
            arr[integer4++] = cwy7;
        }
        final Node cwy8 = this.getNode(cwy.x, cwy.y + 1, cwy.z + 1);
        if (this.isOpen(cwy8) && this.hasMalus(cwy2) && this.hasMalus(cwy6)) {
            arr[integer4++] = cwy8;
        }
        final Node cwy9 = this.getNode(cwy.x - 1, cwy.y + 1, cwy.z);
        if (this.isOpen(cwy9) && this.hasMalus(cwy3) && this.hasMalus(cwy6)) {
            arr[integer4++] = cwy9;
        }
        final Node cwy10 = this.getNode(cwy.x + 1, cwy.y + 1, cwy.z);
        if (this.isOpen(cwy10) && this.hasMalus(cwy4) && this.hasMalus(cwy6)) {
            arr[integer4++] = cwy10;
        }
        final Node cwy11 = this.getNode(cwy.x, cwy.y + 1, cwy.z - 1);
        if (this.isOpen(cwy11) && this.hasMalus(cwy5) && this.hasMalus(cwy6)) {
            arr[integer4++] = cwy11;
        }
        final Node cwy12 = this.getNode(cwy.x, cwy.y - 1, cwy.z + 1);
        if (this.isOpen(cwy12) && this.hasMalus(cwy2) && this.hasMalus(cwy7)) {
            arr[integer4++] = cwy12;
        }
        final Node cwy13 = this.getNode(cwy.x - 1, cwy.y - 1, cwy.z);
        if (this.isOpen(cwy13) && this.hasMalus(cwy3) && this.hasMalus(cwy7)) {
            arr[integer4++] = cwy13;
        }
        final Node cwy14 = this.getNode(cwy.x + 1, cwy.y - 1, cwy.z);
        if (this.isOpen(cwy14) && this.hasMalus(cwy4) && this.hasMalus(cwy7)) {
            arr[integer4++] = cwy14;
        }
        final Node cwy15 = this.getNode(cwy.x, cwy.y - 1, cwy.z - 1);
        if (this.isOpen(cwy15) && this.hasMalus(cwy5) && this.hasMalus(cwy7)) {
            arr[integer4++] = cwy15;
        }
        final Node cwy16 = this.getNode(cwy.x + 1, cwy.y, cwy.z - 1);
        if (this.isOpen(cwy16) && this.hasMalus(cwy5) && this.hasMalus(cwy4)) {
            arr[integer4++] = cwy16;
        }
        final Node cwy17 = this.getNode(cwy.x + 1, cwy.y, cwy.z + 1);
        if (this.isOpen(cwy17) && this.hasMalus(cwy2) && this.hasMalus(cwy4)) {
            arr[integer4++] = cwy17;
        }
        final Node cwy18 = this.getNode(cwy.x - 1, cwy.y, cwy.z - 1);
        if (this.isOpen(cwy18) && this.hasMalus(cwy5) && this.hasMalus(cwy3)) {
            arr[integer4++] = cwy18;
        }
        final Node cwy19 = this.getNode(cwy.x - 1, cwy.y, cwy.z + 1);
        if (this.isOpen(cwy19) && this.hasMalus(cwy2) && this.hasMalus(cwy3)) {
            arr[integer4++] = cwy19;
        }
        final Node cwy20 = this.getNode(cwy.x + 1, cwy.y + 1, cwy.z - 1);
        if (this.isOpen(cwy20) && this.hasMalus(cwy16) && this.hasMalus(cwy5) && this.hasMalus(cwy4) && this.hasMalus(cwy6) && this.hasMalus(cwy11) && this.hasMalus(cwy10)) {
            arr[integer4++] = cwy20;
        }
        final Node cwy21 = this.getNode(cwy.x + 1, cwy.y + 1, cwy.z + 1);
        if (this.isOpen(cwy21) && this.hasMalus(cwy17) && this.hasMalus(cwy2) && this.hasMalus(cwy4) && this.hasMalus(cwy6) && this.hasMalus(cwy8) && this.hasMalus(cwy10)) {
            arr[integer4++] = cwy21;
        }
        final Node cwy22 = this.getNode(cwy.x - 1, cwy.y + 1, cwy.z - 1);
        if (this.isOpen(cwy22) && this.hasMalus(cwy18) && this.hasMalus(cwy5) && (this.hasMalus(cwy3) & this.hasMalus(cwy6)) && this.hasMalus(cwy11) && this.hasMalus(cwy9)) {
            arr[integer4++] = cwy22;
        }
        final Node cwy23 = this.getNode(cwy.x - 1, cwy.y + 1, cwy.z + 1);
        if (this.isOpen(cwy23) && this.hasMalus(cwy19) && this.hasMalus(cwy2) && (this.hasMalus(cwy3) & this.hasMalus(cwy6)) && this.hasMalus(cwy8) && this.hasMalus(cwy9)) {
            arr[integer4++] = cwy23;
        }
        final Node cwy24 = this.getNode(cwy.x + 1, cwy.y - 1, cwy.z - 1);
        if (this.isOpen(cwy24) && this.hasMalus(cwy16) && this.hasMalus(cwy5) && this.hasMalus(cwy4) && this.hasMalus(cwy7) && this.hasMalus(cwy15) && this.hasMalus(cwy14)) {
            arr[integer4++] = cwy24;
        }
        final Node cwy25 = this.getNode(cwy.x + 1, cwy.y - 1, cwy.z + 1);
        if (this.isOpen(cwy25) && this.hasMalus(cwy17) && this.hasMalus(cwy2) && this.hasMalus(cwy4) && this.hasMalus(cwy7) && this.hasMalus(cwy12) && this.hasMalus(cwy14)) {
            arr[integer4++] = cwy25;
        }
        final Node cwy26 = this.getNode(cwy.x - 1, cwy.y - 1, cwy.z - 1);
        if (this.isOpen(cwy26) && this.hasMalus(cwy18) && this.hasMalus(cwy5) && this.hasMalus(cwy3) && this.hasMalus(cwy7) && this.hasMalus(cwy15) && this.hasMalus(cwy13)) {
            arr[integer4++] = cwy26;
        }
        final Node cwy27 = this.getNode(cwy.x - 1, cwy.y - 1, cwy.z + 1);
        if (this.isOpen(cwy27) && this.hasMalus(cwy19) && this.hasMalus(cwy2) && this.hasMalus(cwy3) && this.hasMalus(cwy7) && this.hasMalus(cwy12) && this.hasMalus(cwy13)) {
            arr[integer4++] = cwy27;
        }
        return integer4;
    }
    
    private boolean hasMalus(@Nullable final Node cwy) {
        return cwy != null && cwy.costMalus >= 0.0f;
    }
    
    private boolean isOpen(@Nullable final Node cwy) {
        return cwy != null && !cwy.closed;
    }
    
    @Nullable
    @Override
    protected Node getNode(final int integer1, final int integer2, final int integer3) {
        Node cwy5 = null;
        final BlockPathTypes cww6 = this.getBlockPathType(this.mob, integer1, integer2, integer3);
        final float float7 = this.mob.getPathfindingMalus(cww6);
        if (float7 >= 0.0f) {
            cwy5 = super.getNode(integer1, integer2, integer3);
            cwy5.type = cww6;
            cwy5.costMalus = Math.max(cwy5.costMalus, float7);
            if (cww6 == BlockPathTypes.WALKABLE) {
                final Node node = cwy5;
                ++node.costMalus;
            }
        }
        if (cww6 == BlockPathTypes.OPEN || cww6 == BlockPathTypes.WALKABLE) {
            return cwy5;
        }
        return cwy5;
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4, final Mob aqk, final int integer6, final int integer7, final int integer8, final boolean boolean9, final boolean boolean10) {
        final EnumSet<BlockPathTypes> enumSet12 = (EnumSet<BlockPathTypes>)EnumSet.noneOf((Class)BlockPathTypes.class);
        BlockPathTypes cww13 = BlockPathTypes.BLOCKED;
        final BlockPos fx14 = aqk.blockPosition();
        cww13 = this.getBlockPathTypes(bqz, integer2, integer3, integer4, integer6, integer7, integer8, boolean9, boolean10, enumSet12, cww13, fx14);
        if (enumSet12.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        }
        BlockPathTypes cww14 = BlockPathTypes.BLOCKED;
        for (final BlockPathTypes cww15 : enumSet12) {
            if (aqk.getPathfindingMalus(cww15) < 0.0f) {
                return cww15;
            }
            if (aqk.getPathfindingMalus(cww15) < aqk.getPathfindingMalus(cww14)) {
                continue;
            }
            cww14 = cww15;
        }
        if (cww13 == BlockPathTypes.OPEN && aqk.getPathfindingMalus(cww14) == 0.0f) {
            return BlockPathTypes.OPEN;
        }
        return cww14;
    }
    
    @Override
    public BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4) {
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        BlockPathTypes cww7 = WalkNodeEvaluator.getBlockPathTypeRaw(bqz, a6.set(integer2, integer3, integer4));
        if (cww7 == BlockPathTypes.OPEN && integer3 >= 1) {
            final BlockState cee8 = bqz.getBlockState(a6.set(integer2, integer3 - 1, integer4));
            final BlockPathTypes cww8 = WalkNodeEvaluator.getBlockPathTypeRaw(bqz, a6.set(integer2, integer3 - 1, integer4));
            if (cww8 == BlockPathTypes.DAMAGE_FIRE || cee8.is(Blocks.MAGMA_BLOCK) || cww8 == BlockPathTypes.LAVA || cee8.is(BlockTags.CAMPFIRES)) {
                cww7 = BlockPathTypes.DAMAGE_FIRE;
            }
            else if (cww8 == BlockPathTypes.DAMAGE_CACTUS) {
                cww7 = BlockPathTypes.DAMAGE_CACTUS;
            }
            else if (cww8 == BlockPathTypes.DAMAGE_OTHER) {
                cww7 = BlockPathTypes.DAMAGE_OTHER;
            }
            else if (cww8 == BlockPathTypes.COCOA) {
                cww7 = BlockPathTypes.COCOA;
            }
            else if (cww8 == BlockPathTypes.FENCE) {
                cww7 = BlockPathTypes.FENCE;
            }
            else {
                cww7 = ((cww8 == BlockPathTypes.WALKABLE || cww8 == BlockPathTypes.OPEN || cww8 == BlockPathTypes.WATER) ? BlockPathTypes.OPEN : BlockPathTypes.WALKABLE);
            }
        }
        if (cww7 == BlockPathTypes.WALKABLE || cww7 == BlockPathTypes.OPEN) {
            cww7 = WalkNodeEvaluator.checkNeighbourBlocks(bqz, a6.set(integer2, integer3, integer4), cww7);
        }
        return cww7;
    }
    
    private BlockPathTypes getBlockPathType(final Mob aqk, final BlockPos fx) {
        return this.getBlockPathType(aqk, fx.getX(), fx.getY(), fx.getZ());
    }
    
    private BlockPathTypes getBlockPathType(final Mob aqk, final int integer2, final int integer3, final int integer4) {
        return this.getBlockPathType(this.level, integer2, integer3, integer4, aqk, this.entityWidth, this.entityHeight, this.entityDepth, this.canOpenDoors(), this.canPassDoors());
    }
}
