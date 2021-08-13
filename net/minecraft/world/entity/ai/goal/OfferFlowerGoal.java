package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class OfferFlowerGoal extends Goal {
    private static final TargetingConditions OFFER_TARGER_CONTEXT;
    private final IronGolem golem;
    private Villager villager;
    private int tick;
    
    public OfferFlowerGoal(final IronGolem baf) {
        this.golem = baf;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
    }
    
    @Override
    public boolean canUse() {
        if (!this.golem.level.isDay()) {
            return false;
        }
        if (this.golem.getRandom().nextInt(8000) != 0) {
            return false;
        }
        this.villager = this.golem.level.<Villager>getNearestEntity((java.lang.Class<? extends Villager>)Villager.class, OfferFlowerGoal.OFFER_TARGER_CONTEXT, (LivingEntity)this.golem, this.golem.getX(), this.golem.getY(), this.golem.getZ(), this.golem.getBoundingBox().inflate(6.0, 2.0, 6.0));
        return this.villager != null;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.tick > 0;
    }
    
    @Override
    public void start() {
        this.tick = 400;
        this.golem.offerFlower(true);
    }
    
    @Override
    public void stop() {
        this.golem.offerFlower(false);
        this.villager = null;
    }
    
    @Override
    public void tick() {
        this.golem.getLookControl().setLookAt(this.villager, 30.0f, 30.0f);
        --this.tick;
    }
    
    static {
        OFFER_TARGER_CONTEXT = new TargetingConditions().range(6.0).allowSameTeam().allowInvulnerable();
    }
}
