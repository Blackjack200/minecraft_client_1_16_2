package net.minecraft.world.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.Mob;

public class RangedAttackGoal extends Goal {
    private final Mob mob;
    private final RangedAttackMob rangedAttackMob;
    private LivingEntity target;
    private int attackTime;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
    
    public RangedAttackGoal(final RangedAttackMob bdr, final double double2, final int integer, final float float4) {
        this(bdr, double2, integer, integer, float4);
    }
    
    public RangedAttackGoal(final RangedAttackMob bdr, final double double2, final int integer3, final int integer4, final float float5) {
        this.attackTime = -1;
        if (!(bdr instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.rangedAttackMob = bdr;
        this.mob = (Mob)bdr;
        this.speedModifier = double2;
        this.attackIntervalMin = integer3;
        this.attackIntervalMax = integer4;
        this.attackRadius = float5;
        this.attackRadiusSqr = float5 * float5;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        final LivingEntity aqj2 = this.mob.getTarget();
        if (aqj2 == null || !aqj2.isAlive()) {
            return false;
        }
        this.target = aqj2;
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.canUse() || !this.mob.getNavigation().isDone();
    }
    
    @Override
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }
    
    @Override
    public void tick() {
        final double double2 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        final boolean boolean4 = this.mob.getSensing().canSee(this.target);
        if (boolean4) {
            ++this.seeTime;
        }
        else {
            this.seeTime = 0;
        }
        if (double2 > this.attackRadiusSqr || this.seeTime < 5) {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }
        else {
            this.mob.getNavigation().stop();
        }
        this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
        final int attackTime = this.attackTime - 1;
        this.attackTime = attackTime;
        if (attackTime == 0) {
            if (!boolean4) {
                return;
            }
            float float6;
            final float float5 = float6 = Mth.sqrt(double2) / this.attackRadius;
            float6 = Mth.clamp(float6, 0.1f, 1.0f);
            this.rangedAttackMob.performRangedAttack(this.target, float6);
            this.attackTime = Mth.floor(float5 * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
        }
        else if (this.attackTime < 0) {
            final float float5 = Mth.sqrt(double2) / this.attackRadius;
            this.attackTime = Mth.floor(float5 * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
        }
    }
}
