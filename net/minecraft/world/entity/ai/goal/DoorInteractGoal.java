package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

public abstract class DoorInteractGoal extends Goal {
    protected Mob mob;
    protected BlockPos doorPos;
    protected boolean hasDoor;
    private boolean passed;
    private float doorOpenDirX;
    private float doorOpenDirZ;
    
    public DoorInteractGoal(final Mob aqk) {
        this.doorPos = BlockPos.ZERO;
        this.mob = aqk;
        if (!GoalUtils.hasGroundPathNavigation(aqk)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }
    
    protected boolean isOpen() {
        if (!this.hasDoor) {
            return false;
        }
        final BlockState cee2 = this.mob.level.getBlockState(this.doorPos);
        if (!(cee2.getBlock() instanceof DoorBlock)) {
            return this.hasDoor = false;
        }
        return cee2.<Boolean>getValue((Property<Boolean>)DoorBlock.OPEN);
    }
    
    protected void setOpen(final boolean boolean1) {
        if (this.hasDoor) {
            final BlockState cee3 = this.mob.level.getBlockState(this.doorPos);
            if (cee3.getBlock() instanceof DoorBlock) {
                ((DoorBlock)cee3.getBlock()).setOpen(this.mob.level, cee3, this.doorPos, boolean1);
            }
        }
    }
    
    @Override
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        }
        if (!this.mob.horizontalCollision) {
            return false;
        }
        final GroundPathNavigation ayf2 = (GroundPathNavigation)this.mob.getNavigation();
        final Path cxa3 = ayf2.getPath();
        if (cxa3 == null || cxa3.isDone() || !ayf2.canOpenDoors()) {
            return false;
        }
        for (int integer4 = 0; integer4 < Math.min(cxa3.getNextNodeIndex() + 2, cxa3.getNodeCount()); ++integer4) {
            final Node cwy5 = cxa3.getNode(integer4);
            this.doorPos = new BlockPos(cwy5.x, cwy5.y + 1, cwy5.z);
            if (this.mob.distanceToSqr(this.doorPos.getX(), this.mob.getY(), this.doorPos.getZ()) <= 2.25) {
                this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level, this.doorPos);
                if (this.hasDoor) {
                    return true;
                }
            }
        }
        this.doorPos = this.mob.blockPosition().above();
        return this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level, this.doorPos);
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.passed;
    }
    
    @Override
    public void start() {
        this.passed = false;
        this.doorOpenDirX = (float)(this.doorPos.getX() + 0.5 - this.mob.getX());
        this.doorOpenDirZ = (float)(this.doorPos.getZ() + 0.5 - this.mob.getZ());
    }
    
    @Override
    public void tick() {
        final float float2 = (float)(this.doorPos.getX() + 0.5 - this.mob.getX());
        final float float3 = (float)(this.doorPos.getZ() + 0.5 - this.mob.getZ());
        final float float4 = this.doorOpenDirX * float2 + this.doorOpenDirZ * float3;
        if (float4 < 0.0f) {
            this.passed = true;
        }
    }
}
