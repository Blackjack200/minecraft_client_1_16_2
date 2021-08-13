package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.scores.Team;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class TargetGoal extends Goal {
    protected final Mob mob;
    protected final boolean mustSee;
    private final boolean mustReach;
    private int reachCache;
    private int reachCacheTime;
    private int unseenTicks;
    protected LivingEntity targetMob;
    protected int unseenMemoryTicks;
    
    public TargetGoal(final Mob aqk, final boolean boolean2) {
        this(aqk, boolean2, false);
    }
    
    public TargetGoal(final Mob aqk, final boolean boolean2, final boolean boolean3) {
        this.unseenMemoryTicks = 60;
        this.mob = aqk;
        this.mustSee = boolean2;
        this.mustReach = boolean3;
    }
    
    @Override
    public boolean canContinueToUse() {
        LivingEntity aqj2 = this.mob.getTarget();
        if (aqj2 == null) {
            aqj2 = this.targetMob;
        }
        if (aqj2 == null) {
            return false;
        }
        if (!aqj2.isAlive()) {
            return false;
        }
        final Team ddm3 = this.mob.getTeam();
        final Team ddm4 = aqj2.getTeam();
        if (ddm3 != null && ddm4 == ddm3) {
            return false;
        }
        final double double5 = this.getFollowDistance();
        if (this.mob.distanceToSqr(aqj2) > double5 * double5) {
            return false;
        }
        if (this.mustSee) {
            if (this.mob.getSensing().canSee(aqj2)) {
                this.unseenTicks = 0;
            }
            else if (++this.unseenTicks > this.unseenMemoryTicks) {
                return false;
            }
        }
        if (aqj2 instanceof Player && ((Player)aqj2).abilities.invulnerable) {
            return false;
        }
        this.mob.setTarget(aqj2);
        return true;
    }
    
    protected double getFollowDistance() {
        return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
    
    @Override
    public void start() {
        this.reachCache = 0;
        this.reachCacheTime = 0;
        this.unseenTicks = 0;
    }
    
    @Override
    public void stop() {
        this.mob.setTarget(null);
        this.targetMob = null;
    }
    
    protected boolean canAttack(@Nullable final LivingEntity aqj, final TargetingConditions azd) {
        if (aqj == null) {
            return false;
        }
        if (!azd.test(this.mob, aqj)) {
            return false;
        }
        if (!this.mob.isWithinRestriction(aqj.blockPosition())) {
            return false;
        }
        if (this.mustReach) {
            if (--this.reachCacheTime <= 0) {
                this.reachCache = 0;
            }
            if (this.reachCache == 0) {
                this.reachCache = (this.canReach(aqj) ? 1 : 2);
            }
            if (this.reachCache == 2) {
                return false;
            }
        }
        return true;
    }
    
    private boolean canReach(final LivingEntity aqj) {
        this.reachCacheTime = 10 + this.mob.getRandom().nextInt(5);
        final Path cxa3 = this.mob.getNavigation().createPath(aqj, 0);
        if (cxa3 == null) {
            return false;
        }
        final Node cwy4 = cxa3.getEndNode();
        if (cwy4 == null) {
            return false;
        }
        final int integer5 = cwy4.x - Mth.floor(aqj.getX());
        final int integer6 = cwy4.z - Mth.floor(aqj.getZ());
        return integer5 * integer5 + integer6 * integer6 <= 2.25;
    }
    
    public TargetGoal setUnseenMemoryTicks(final int integer) {
        this.unseenMemoryTicks = integer;
        return this;
    }
}
