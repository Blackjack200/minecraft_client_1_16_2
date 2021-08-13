package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

public class OwnerHurtByTargetGoal extends TargetGoal {
    private final TamableAnimal tameAnimal;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;
    
    public OwnerHurtByTargetGoal(final TamableAnimal arb) {
        super(arb, false);
        this.tameAnimal = arb;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.TARGET));
    }
    
    @Override
    public boolean canUse() {
        if (!this.tameAnimal.isTame() || this.tameAnimal.isOrderedToSit()) {
            return false;
        }
        final LivingEntity aqj2 = this.tameAnimal.getOwner();
        if (aqj2 == null) {
            return false;
        }
        this.ownerLastHurtBy = aqj2.getLastHurtByMob();
        final int integer3 = aqj2.getLastHurtByMobTimestamp();
        return integer3 != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurtBy, aqj2);
    }
    
    @Override
    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        final LivingEntity aqj2 = this.tameAnimal.getOwner();
        if (aqj2 != null) {
            this.timestamp = aqj2.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}
