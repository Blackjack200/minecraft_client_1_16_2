package net.minecraft.world.entity;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public abstract class PathfinderMob extends Mob {
    protected PathfinderMob(final EntityType<? extends PathfinderMob> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public float getWalkTargetValue(final BlockPos fx) {
        return this.getWalkTargetValue(fx, this.level);
    }
    
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        return 0.0f;
    }
    
    @Override
    public boolean checkSpawnRules(final LevelAccessor brv, final MobSpawnType aqm) {
        return this.getWalkTargetValue(this.blockPosition(), brv) >= 0.0f;
    }
    
    public boolean isPathFinding() {
        return !this.getNavigation().isDone();
    }
    
    @Override
    protected void tickLeash() {
        super.tickLeash();
        final Entity apx2 = this.getLeashHolder();
        if (apx2 != null && apx2.level == this.level) {
            this.restrictTo(apx2.blockPosition(), 5);
            final float float3 = this.distanceTo(apx2);
            if (this instanceof TamableAnimal && ((TamableAnimal)this).isInSittingPose()) {
                if (float3 > 10.0f) {
                    this.dropLeash(true, true);
                }
                return;
            }
            this.onLeashDistance(float3);
            if (float3 > 10.0f) {
                this.dropLeash(true, true);
                this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
            }
            else if (float3 > 6.0f) {
                final double double4 = (apx2.getX() - this.getX()) / float3;
                final double double5 = (apx2.getY() - this.getY()) / float3;
                final double double6 = (apx2.getZ() - this.getZ()) / float3;
                this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(double4 * double4 * 0.4, double4), Math.copySign(double5 * double5 * 0.4, double5), Math.copySign(double6 * double6 * 0.4, double6)));
            }
            else {
                this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                final float float4 = 2.0f;
                final Vec3 dck5 = new Vec3(apx2.getX() - this.getX(), apx2.getY() - this.getY(), apx2.getZ() - this.getZ()).normalize().scale(Math.max(float3 - 2.0f, 0.0f));
                this.getNavigation().moveTo(this.getX() + dck5.x, this.getY() + dck5.y, this.getZ() + dck5.z, this.followLeashSpeed());
            }
        }
    }
    
    protected double followLeashSpeed() {
        return 1.0;
    }
    
    protected void onLeashDistance(final float float1) {
    }
}
