package net.minecraft.world.entity.ai.goal.target;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.EntityType;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class HurtByTargetGoal extends TargetGoal {
    private static final TargetingConditions HURT_BY_TARGETING;
    private boolean alertSameType;
    private int timestamp;
    private final Class<?>[] toIgnoreDamage;
    private Class<?>[] toIgnoreAlert;
    
    public HurtByTargetGoal(final PathfinderMob aqr, final Class<?>... arr) {
        super(aqr, true);
        this.toIgnoreDamage = arr;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.TARGET));
    }
    
    @Override
    public boolean canUse() {
        final int integer2 = this.mob.getLastHurtByMobTimestamp();
        final LivingEntity aqj3 = this.mob.getLastHurtByMob();
        if (integer2 == this.timestamp || aqj3 == null) {
            return false;
        }
        if (aqj3.getType() == EntityType.PLAYER && this.mob.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
            return false;
        }
        for (final Class<?> class7 : this.toIgnoreDamage) {
            if (class7.isAssignableFrom(aqj3.getClass())) {
                return false;
            }
        }
        return this.canAttack(aqj3, HurtByTargetGoal.HURT_BY_TARGETING);
    }
    
    public HurtByTargetGoal setAlertOthers(final Class<?>... arr) {
        this.alertSameType = true;
        this.toIgnoreAlert = arr;
        return this;
    }
    
    @Override
    public void start() {
        this.mob.setTarget(this.mob.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.timestamp = this.mob.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;
        if (this.alertSameType) {
            this.alertOthers();
        }
        super.start();
    }
    
    protected void alertOthers() {
        final double double2 = this.getFollowDistance();
        final AABB dcf4 = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(double2, 10.0, double2);
        final List<Mob> list5 = this.mob.level.<Mob>getLoadedEntitiesOfClass((java.lang.Class<? extends Mob>)this.mob.getClass(), dcf4);
        for (final Mob aqk7 : list5) {
            if (this.mob == aqk7) {
                continue;
            }
            if (aqk7.getTarget() != null) {
                continue;
            }
            if (this.mob instanceof TamableAnimal && ((TamableAnimal)this.mob).getOwner() != ((TamableAnimal)aqk7).getOwner()) {
                continue;
            }
            if (aqk7.isAlliedTo(this.mob.getLastHurtByMob())) {
                continue;
            }
            if (this.toIgnoreAlert != null) {
                boolean boolean8 = false;
                for (final Class<?> class12 : this.toIgnoreAlert) {
                    if (aqk7.getClass() == class12) {
                        boolean8 = true;
                        break;
                    }
                }
                if (boolean8) {
                    continue;
                }
            }
            this.alertOther(aqk7, this.mob.getLastHurtByMob());
        }
    }
    
    protected void alertOther(final Mob aqk, final LivingEntity aqj) {
        aqk.setTarget(aqj);
    }
    
    static {
        HURT_BY_TARGETING = new TargetingConditions().allowUnseeable().ignoreInvisibilityTesting();
    }
}
