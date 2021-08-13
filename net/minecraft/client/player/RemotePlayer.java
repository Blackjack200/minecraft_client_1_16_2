package net.minecraft.client.player;

import net.minecraft.client.Minecraft;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;

public class RemotePlayer extends AbstractClientPlayer {
    public RemotePlayer(final ClientLevel dwl, final GameProfile gameProfile) {
        super(dwl, gameProfile);
        this.maxUpStep = 1.0f;
        this.noPhysics = true;
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        double double2 = this.getBoundingBox().getSize() * 10.0;
        if (Double.isNaN(double2)) {
            double2 = 1.0;
        }
        double2 *= 64.0 * getViewScale();
        return double1 < double2 * double2;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        return true;
    }
    
    @Override
    public void tick() {
        super.tick();
        this.calculateEntityAnimation(this, false);
    }
    
    @Override
    public void aiStep() {
        if (this.lerpSteps > 0) {
            final double double2 = this.getX() + (this.lerpX - this.getX()) / this.lerpSteps;
            final double double3 = this.getY() + (this.lerpY - this.getY()) / this.lerpSteps;
            final double double4 = this.getZ() + (this.lerpZ - this.getZ()) / this.lerpSteps;
            this.yRot += (float)(Mth.wrapDegrees(this.lerpYRot - this.yRot) / this.lerpSteps);
            this.xRot += (float)((this.lerpXRot - this.xRot) / this.lerpSteps);
            --this.lerpSteps;
            this.setPos(double2, double3, double4);
            this.setRot(this.yRot, this.xRot);
        }
        if (this.lerpHeadSteps > 0) {
            this.yHeadRot += (float)(Mth.wrapDegrees(this.lyHeadRot - this.yHeadRot) / this.lerpHeadSteps);
            --this.lerpHeadSteps;
        }
        this.oBob = this.bob;
        this.updateSwingTime();
        float float2;
        if (!this.onGround || this.isDeadOrDying()) {
            float2 = 0.0f;
        }
        else {
            float2 = Math.min(0.1f, Mth.sqrt(Entity.getHorizontalDistanceSqr(this.getDeltaMovement())));
        }
        if (this.onGround || this.isDeadOrDying()) {
            final float float3 = 0.0f;
        }
        else {
            final float float3 = (float)Math.atan(-this.getDeltaMovement().y * 0.20000000298023224) * 15.0f;
        }
        this.bob += (float2 - this.bob) * 0.4f;
        this.level.getProfiler().push("push");
        this.pushEntities();
        this.level.getProfiler().pop();
    }
    
    @Override
    protected void updatePlayerPose() {
    }
    
    @Override
    public void sendMessage(final Component nr, final UUID uUID) {
        final Minecraft djw4 = Minecraft.getInstance();
        if (!djw4.isBlocked(uUID)) {
            djw4.gui.getChat().addMessage(nr);
        }
    }
}
