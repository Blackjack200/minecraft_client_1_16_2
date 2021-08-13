package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import java.util.EnumSet;

public class RangedBowAttackGoal<T extends Monster> extends Goal {
    private final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime;
    
    public RangedBowAttackGoal(final T bdn, final double double2, final int integer, final float float4) {
        this.attackTime = -1;
        this.strafingTime = -1;
        this.mob = bdn;
        this.speedModifier = double2;
        this.attackIntervalMin = integer;
        this.attackRadiusSqr = float4 * float4;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    public void setMinAttackInterval(final int integer) {
        this.attackIntervalMin = integer;
    }
    
    @Override
    public boolean canUse() {
        return ((Mob)this.mob).getTarget() != null && this.isHoldingBow();
    }
    
    protected boolean isHoldingBow() {
        return ((LivingEntity)this.mob).isHolding(Items.BOW);
    }
    
    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !((Mob)this.mob).getNavigation().isDone()) && this.isHoldingBow();
    }
    
    @Override
    public void start() {
        super.start();
        ((Mob)this.mob).setAggressive(true);
    }
    
    @Override
    public void stop() {
        super.stop();
        ((Mob)this.mob).setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        ((LivingEntity)this.mob).stopUsingItem();
    }
    
    @Override
    public void tick() {
        final LivingEntity aqj2 = ((Mob)this.mob).getTarget();
        if (aqj2 == null) {
            return;
        }
        final double double3 = ((Entity)this.mob).distanceToSqr(aqj2.getX(), aqj2.getY(), aqj2.getZ());
        final boolean boolean5 = ((Mob)this.mob).getSensing().canSee(aqj2);
        final boolean boolean6 = this.seeTime > 0;
        if (boolean5 != boolean6) {
            this.seeTime = 0;
        }
        if (boolean5) {
            ++this.seeTime;
        }
        else {
            --this.seeTime;
        }
        if (double3 > this.attackRadiusSqr || this.seeTime < 20) {
            ((Mob)this.mob).getNavigation().moveTo(aqj2, this.speedModifier);
            this.strafingTime = -1;
        }
        else {
            ((Mob)this.mob).getNavigation().stop();
            ++this.strafingTime;
        }
        if (this.strafingTime >= 20) {
            if (((LivingEntity)this.mob).getRandom().nextFloat() < 0.3) {
                this.strafingClockwise = !this.strafingClockwise;
            }
            if (((LivingEntity)this.mob).getRandom().nextFloat() < 0.3) {
                this.strafingBackwards = !this.strafingBackwards;
            }
            this.strafingTime = 0;
        }
        if (this.strafingTime > -1) {
            if (double3 > this.attackRadiusSqr * 0.75f) {
                this.strafingBackwards = false;
            }
            else if (double3 < this.attackRadiusSqr * 0.25f) {
                this.strafingBackwards = true;
            }
            ((Mob)this.mob).getMoveControl().strafe(this.strafingBackwards ? -0.5f : 0.5f, this.strafingClockwise ? 0.5f : -0.5f);
            ((Mob)this.mob).lookAt(aqj2, 30.0f, 30.0f);
        }
        else {
            ((Mob)this.mob).getLookControl().setLookAt(aqj2, 30.0f, 30.0f);
        }
        if (((LivingEntity)this.mob).isUsingItem()) {
            if (!boolean5 && this.seeTime < -60) {
                ((LivingEntity)this.mob).stopUsingItem();
            }
            else if (boolean5) {
                final int integer7 = ((LivingEntity)this.mob).getTicksUsingItem();
                if (integer7 >= 20) {
                    ((LivingEntity)this.mob).stopUsingItem();
                    ((RangedAttackMob)this.mob).performRangedAttack(aqj2, BowItem.getPowerForTime(integer7));
                    this.attackTime = this.attackIntervalMin;
                }
            }
        }
        else {
            final int attackTime = this.attackTime - 1;
            this.attackTime = attackTime;
            if (attackTime <= 0 && this.seeTime >= -60) {
                ((LivingEntity)this.mob).startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this.mob, Items.BOW));
            }
        }
    }
}
