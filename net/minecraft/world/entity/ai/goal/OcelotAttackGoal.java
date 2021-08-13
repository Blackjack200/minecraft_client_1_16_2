package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;

public class OcelotAttackGoal extends Goal {
    private final BlockGetter level;
    private final Mob mob;
    private LivingEntity target;
    private int attackTime;
    
    public OcelotAttackGoal(final Mob aqk) {
        this.mob = aqk;
        this.level = aqk.level;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        final LivingEntity aqj2 = this.mob.getTarget();
        if (aqj2 == null) {
            return false;
        }
        this.target = aqj2;
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.target.isAlive() && this.mob.distanceToSqr(this.target) <= 225.0 && (!this.mob.getNavigation().isDone() || this.canUse());
    }
    
    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }
    
    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
        final double double2 = this.mob.getBbWidth() * 2.0f * (this.mob.getBbWidth() * 2.0f);
        final double double3 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        double double4 = 0.8;
        if (double3 > double2 && double3 < 16.0) {
            double4 = 1.33;
        }
        else if (double3 < 225.0) {
            double4 = 0.6;
        }
        this.mob.getNavigation().moveTo(this.target, double4);
        this.attackTime = Math.max(this.attackTime - 1, 0);
        if (double3 > double2) {
            return;
        }
        if (this.attackTime > 0) {
            return;
        }
        this.attackTime = 20;
        this.mob.doHurtTarget(this.target);
    }
}
