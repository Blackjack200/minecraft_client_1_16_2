package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.entity.TamableAnimal;

public class SitWhenOrderedToGoal extends Goal {
    private final TamableAnimal mob;
    
    public SitWhenOrderedToGoal(final TamableAnimal arb) {
        this.mob = arb;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.mob.isOrderedToSit();
    }
    
    @Override
    public boolean canUse() {
        if (!this.mob.isTame()) {
            return false;
        }
        if (this.mob.isInWaterOrBubble()) {
            return false;
        }
        if (!this.mob.isOnGround()) {
            return false;
        }
        final LivingEntity aqj2 = this.mob.getOwner();
        return aqj2 == null || ((this.mob.distanceToSqr(aqj2) >= 144.0 || aqj2.getLastHurtByMob() == null) && this.mob.isOrderedToSit());
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setInSittingPose(true);
    }
    
    @Override
    public void stop() {
        this.mob.setInSittingPose(false);
    }
}
