package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

public class FollowOwnerGoal extends Goal {
    private final TamableAnimal tamable;
    private LivingEntity owner;
    private final LevelReader level;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private final float startDistance;
    private float oldWaterCost;
    private final boolean canFly;
    
    public FollowOwnerGoal(final TamableAnimal arb, final double double2, final float float3, final float float4, final boolean boolean5) {
        this.tamable = arb;
        this.level = arb.level;
        this.speedModifier = double2;
        this.navigation = arb.getNavigation();
        this.startDistance = float3;
        this.stopDistance = float4;
        this.canFly = boolean5;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        if (!(arb.getNavigation() instanceof GroundPathNavigation) && !(arb.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }
    
    @Override
    public boolean canUse() {
        final LivingEntity aqj2 = this.tamable.getOwner();
        if (aqj2 == null) {
            return false;
        }
        if (aqj2.isSpectator()) {
            return false;
        }
        if (this.tamable.isOrderedToSit()) {
            return false;
        }
        if (this.tamable.distanceToSqr(aqj2) < this.startDistance * this.startDistance) {
            return false;
        }
        this.owner = aqj2;
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.navigation.isDone() && !this.tamable.isOrderedToSit() && this.tamable.distanceToSqr(this.owner) > this.stopDistance * this.stopDistance;
    }
    
    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tamable.getPathfindingMalus(BlockPathTypes.WATER);
        this.tamable.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }
    
    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.tamable.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }
    
    @Override
    public void tick() {
        this.tamable.getLookControl().setLookAt(this.owner, 10.0f, (float)this.tamable.getMaxHeadXRot());
        final int timeToRecalcPath = this.timeToRecalcPath - 1;
        this.timeToRecalcPath = timeToRecalcPath;
        if (timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = 10;
        if (this.tamable.isLeashed() || this.tamable.isPassenger()) {
            return;
        }
        if (this.tamable.distanceToSqr(this.owner) >= 144.0) {
            this.teleportToOwner();
        }
        else {
            this.navigation.moveTo(this.owner, this.speedModifier);
        }
    }
    
    private void teleportToOwner() {
        final BlockPos fx2 = this.owner.blockPosition();
        for (int integer3 = 0; integer3 < 10; ++integer3) {
            final int integer4 = this.randomIntInclusive(-3, 3);
            final int integer5 = this.randomIntInclusive(-1, 1);
            final int integer6 = this.randomIntInclusive(-3, 3);
            final boolean boolean7 = this.maybeTeleportTo(fx2.getX() + integer4, fx2.getY() + integer5, fx2.getZ() + integer6);
            if (boolean7) {
                return;
            }
        }
    }
    
    private boolean maybeTeleportTo(final int integer1, final int integer2, final int integer3) {
        if (Math.abs(integer1 - this.owner.getX()) < 2.0 && Math.abs(integer3 - this.owner.getZ()) < 2.0) {
            return false;
        }
        if (!this.canTeleportTo(new BlockPos(integer1, integer2, integer3))) {
            return false;
        }
        this.tamable.moveTo(integer1 + 0.5, integer2, integer3 + 0.5, this.tamable.yRot, this.tamable.xRot);
        this.navigation.stop();
        return true;
    }
    
    private boolean canTeleportTo(final BlockPos fx) {
        final BlockPathTypes cww3 = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, fx.mutable());
        if (cww3 != BlockPathTypes.WALKABLE) {
            return false;
        }
        final BlockState cee4 = this.level.getBlockState(fx.below());
        if (!this.canFly && cee4.getBlock() instanceof LeavesBlock) {
            return false;
        }
        final BlockPos fx2 = fx.subtract(this.tamable.blockPosition());
        return this.level.noCollision(this.tamable, this.tamable.getBoundingBox().move(fx2));
    }
    
    private int randomIntInclusive(final int integer1, final int integer2) {
        return this.tamable.getRandom().nextInt(integer2 - integer1 + 1) + integer1;
    }
}
