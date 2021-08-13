package net.minecraft.world.entity.ai.goal;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;

public class LandOnOwnersShoulderGoal extends Goal {
    private final ShoulderRidingEntity entity;
    private ServerPlayer owner;
    private boolean isSittingOnShoulder;
    
    public LandOnOwnersShoulderGoal(final ShoulderRidingEntity baq) {
        this.entity = baq;
    }
    
    @Override
    public boolean canUse() {
        final ServerPlayer aah2 = (ServerPlayer)this.entity.getOwner();
        final boolean boolean3 = aah2 != null && !aah2.isSpectator() && !aah2.abilities.flying && !aah2.isInWater();
        return !this.entity.isOrderedToSit() && boolean3 && this.entity.canSitOnShoulder();
    }
    
    @Override
    public boolean isInterruptable() {
        return !this.isSittingOnShoulder;
    }
    
    @Override
    public void start() {
        this.owner = (ServerPlayer)this.entity.getOwner();
        this.isSittingOnShoulder = false;
    }
    
    @Override
    public void tick() {
        if (this.isSittingOnShoulder || this.entity.isInSittingPose() || this.entity.isLeashed()) {
            return;
        }
        if (this.entity.getBoundingBox().intersects(this.owner.getBoundingBox())) {
            this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
        }
    }
}
