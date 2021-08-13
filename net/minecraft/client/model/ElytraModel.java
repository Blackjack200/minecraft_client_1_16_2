package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.player.AbstractClientPlayer;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class ElytraModel<T extends LivingEntity> extends AgeableListModel<T> {
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    
    public ElytraModel() {
        (this.leftWing = new ModelPart(this, 22, 0)).addBox(-10.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, 1.0f);
        this.rightWing = new ModelPart(this, 22, 0);
        this.rightWing.mirror = true;
        this.rightWing.addBox(0.0f, 0.0f, 0.0f, 10.0f, 20.0f, 2.0f, 1.0f);
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of();
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.leftWing, this.rightWing);
    }
    
    @Override
    public void setupAnim(final T aqj, final float float2, final float float3, final float float4, final float float5, final float float6) {
        float float7 = 0.2617994f;
        float float8 = -0.2617994f;
        float float9 = 0.0f;
        float float10 = 0.0f;
        if (aqj.isFallFlying()) {
            float float11 = 1.0f;
            final Vec3 dck13 = aqj.getDeltaMovement();
            if (dck13.y < 0.0) {
                final Vec3 dck14 = dck13.normalize();
                float11 = 1.0f - (float)Math.pow(-dck14.y, 1.5);
            }
            float7 = float11 * 0.34906584f + (1.0f - float11) * float7;
            float8 = float11 * -1.5707964f + (1.0f - float11) * float8;
        }
        else if (aqj.isCrouching()) {
            float7 = 0.6981317f;
            float8 = -0.7853982f;
            float9 = 3.0f;
            float10 = 0.08726646f;
        }
        this.leftWing.x = 5.0f;
        this.leftWing.y = float9;
        if (aqj instanceof AbstractClientPlayer) {
            final AbstractClientPlayer abstractClientPlayer;
            final AbstractClientPlayer dzb12 = abstractClientPlayer = (AbstractClientPlayer)aqj;
            abstractClientPlayer.elytraRotX += (float)((float7 - dzb12.elytraRotX) * 0.1);
            final AbstractClientPlayer abstractClientPlayer2 = dzb12;
            abstractClientPlayer2.elytraRotY += (float)((float10 - dzb12.elytraRotY) * 0.1);
            final AbstractClientPlayer abstractClientPlayer3 = dzb12;
            abstractClientPlayer3.elytraRotZ += (float)((float8 - dzb12.elytraRotZ) * 0.1);
            this.leftWing.xRot = dzb12.elytraRotX;
            this.leftWing.yRot = dzb12.elytraRotY;
            this.leftWing.zRot = dzb12.elytraRotZ;
        }
        else {
            this.leftWing.xRot = float7;
            this.leftWing.zRot = float8;
            this.leftWing.yRot = float10;
        }
        this.rightWing.x = -this.leftWing.x;
        this.rightWing.yRot = -this.leftWing.yRot;
        this.rightWing.y = this.leftWing.y;
        this.rightWing.xRot = this.leftWing.xRot;
        this.rightWing.zRot = -this.leftWing.zRot;
    }
}
