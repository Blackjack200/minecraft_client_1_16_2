package net.minecraft.world.entity.ai.goal.target;

import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

public class OwnerHurtTargetGoal extends TargetGoal {
    private final TamableAnimal tameAnimal;
    private LivingEntity ownerLastHurt;
    private int timestamp;
    
    public OwnerHurtTargetGoal(final TamableAnimal arb) {
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
        this.ownerLastHurt = aqj2.getLastHurtMob();
        final int integer3 = aqj2.getLastHurtMobTimestamp();
        return integer3 != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurt, aqj2);
    }
    
    @Override
    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        final LivingEntity aqj2 = this.tameAnimal.getOwner();
        if (aqj2 != null) {
            this.timestamp = aqj2.getLastHurtMobTimestamp();
        }
        super.start();
    }
}
