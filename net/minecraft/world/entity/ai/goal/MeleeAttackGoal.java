package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.PathfinderMob;

public class MeleeAttackGoal extends Goal {
    protected final PathfinderMob mob;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private final int attackInterval = 20;
    private long lastCanUseCheck;
    
    public MeleeAttackGoal(final PathfinderMob aqr, final double double2, final boolean boolean3) {
        this.mob = aqr;
        this.speedModifier = double2;
        this.followingTargetEvenIfNotSeen = boolean3;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        final long long2 = this.mob.level.getGameTime();
        if (long2 - this.lastCanUseCheck < 20L) {
            return false;
        }
        this.lastCanUseCheck = long2;
        final LivingEntity aqj4 = this.mob.getTarget();
        if (aqj4 == null) {
            return false;
        }
        if (!aqj4.isAlive()) {
            return false;
        }
        this.path = this.mob.getNavigation().createPath(aqj4, 0);
        return this.path != null || this.getAttackReachSqr(aqj4) >= this.mob.distanceToSqr(aqj4.getX(), aqj4.getY(), aqj4.getZ());
    }
    
    @Override
    public boolean canContinueToUse() {
        final LivingEntity aqj2 = this.mob.getTarget();
        if (aqj2 == null) {
            return false;
        }
        if (!aqj2.isAlive()) {
            return false;
        }
        if (!this.followingTargetEvenIfNotSeen) {
            return !this.mob.getNavigation().isDone();
        }
        return this.mob.isWithinRestriction(aqj2.blockPosition()) && (!(aqj2 instanceof Player) || (!aqj2.isSpectator() && !((Player)aqj2).isCreative()));
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speedModifier);
        this.mob.setAggressive(true);
        this.ticksUntilNextPathRecalculation = 0;
        this.ticksUntilNextAttack = 0;
    }
    
    @Override
    public void stop() {
        final LivingEntity aqj2 = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(aqj2)) {
            this.mob.setTarget(null);
        }
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }
    
    @Override
    public void tick() {
        final LivingEntity aqj2 = this.mob.getTarget();
        this.mob.getLookControl().setLookAt(aqj2, 30.0f, 30.0f);
        final double double3 = this.mob.distanceToSqr(aqj2.getX(), aqj2.getY(), aqj2.getZ());
        this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
        if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().canSee(aqj2)) && this.ticksUntilNextPathRecalculation <= 0 && ((this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0) || aqj2.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
            this.pathedTargetX = aqj2.getX();
            this.pathedTargetY = aqj2.getY();
            this.pathedTargetZ = aqj2.getZ();
            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
            if (double3 > 1024.0) {
                this.ticksUntilNextPathRecalculation += 10;
            }
            else if (double3 > 256.0) {
                this.ticksUntilNextPathRecalculation += 5;
            }
            if (!this.mob.getNavigation().moveTo(aqj2, this.speedModifier)) {
                this.ticksUntilNextPathRecalculation += 15;
            }
        }
        this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        this.checkAndPerformAttack(aqj2, double3);
    }
    
    protected void checkAndPerformAttack(final LivingEntity aqj, final double double2) {
        final double double3 = this.getAttackReachSqr(aqj);
        if (double2 <= double3 && this.ticksUntilNextAttack <= 0) {
            this.resetAttackCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(aqj);
        }
    }
    
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 20;
    }
    
    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }
    
    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }
    
    protected int getAttackInterval() {
        return 20;
    }
    
    protected double getAttackReachSqr(final LivingEntity aqj) {
        return this.mob.getBbWidth() * 2.0f * (this.mob.getBbWidth() * 2.0f) + aqj.getBbWidth();
    }
}
