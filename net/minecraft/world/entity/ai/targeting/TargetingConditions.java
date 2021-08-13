package net.minecraft.world.entity.ai.targeting;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public class TargetingConditions {
    public static final TargetingConditions DEFAULT;
    private double range;
    private boolean allowInvulnerable;
    private boolean allowSameTeam;
    private boolean allowUnseeable;
    private boolean allowNonAttackable;
    private boolean testInvisible;
    private Predicate<LivingEntity> selector;
    
    public TargetingConditions() {
        this.range = -1.0;
        this.testInvisible = true;
    }
    
    public TargetingConditions range(final double double1) {
        this.range = double1;
        return this;
    }
    
    public TargetingConditions allowInvulnerable() {
        this.allowInvulnerable = true;
        return this;
    }
    
    public TargetingConditions allowSameTeam() {
        this.allowSameTeam = true;
        return this;
    }
    
    public TargetingConditions allowUnseeable() {
        this.allowUnseeable = true;
        return this;
    }
    
    public TargetingConditions allowNonAttackable() {
        this.allowNonAttackable = true;
        return this;
    }
    
    public TargetingConditions ignoreInvisibilityTesting() {
        this.testInvisible = false;
        return this;
    }
    
    public TargetingConditions selector(@Nullable final Predicate<LivingEntity> predicate) {
        this.selector = predicate;
        return this;
    }
    
    public boolean test(@Nullable final LivingEntity aqj1, final LivingEntity aqj2) {
        if (aqj1 == aqj2) {
            return false;
        }
        if (aqj2.isSpectator()) {
            return false;
        }
        if (!aqj2.isAlive()) {
            return false;
        }
        if (!this.allowInvulnerable && aqj2.isInvulnerable()) {
            return false;
        }
        if (this.selector != null && !this.selector.test(aqj2)) {
            return false;
        }
        if (aqj1 != null) {
            if (!this.allowNonAttackable) {
                if (!aqj1.canAttack(aqj2)) {
                    return false;
                }
                if (!aqj1.canAttackType(aqj2.getType())) {
                    return false;
                }
            }
            if (!this.allowSameTeam && aqj1.isAlliedTo(aqj2)) {
                return false;
            }
            if (this.range > 0.0) {
                final double double4 = this.testInvisible ? aqj2.getVisibilityPercent(aqj1) : 1.0;
                final double double5 = Math.max(this.range * double4, 2.0);
                final double double6 = aqj1.distanceToSqr(aqj2.getX(), aqj2.getY(), aqj2.getZ());
                if (double6 > double5 * double5) {
                    return false;
                }
            }
            if (!this.allowUnseeable && aqj1 instanceof Mob && !((Mob)aqj1).getSensing().canSee(aqj2)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        DEFAULT = new TargetingConditions();
    }
}
