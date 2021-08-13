package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemModel<T extends IronGolem> extends ListModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart arm0;
    private final ModelPart arm1;
    private final ModelPart leg0;
    private final ModelPart leg1;
    
    public IronGolemModel() {
        final int integer2 = 128;
        final int integer3 = 128;
        (this.head = new ModelPart(this).setTexSize(128, 128)).setPos(0.0f, -7.0f, -2.0f);
        this.head.texOffs(0, 0).addBox(-4.0f, -12.0f, -5.5f, 8.0f, 10.0f, 8.0f, 0.0f);
        this.head.texOffs(24, 0).addBox(-1.0f, -5.0f, -7.5f, 2.0f, 4.0f, 2.0f, 0.0f);
        (this.body = new ModelPart(this).setTexSize(128, 128)).setPos(0.0f, -7.0f, 0.0f);
        this.body.texOffs(0, 40).addBox(-9.0f, -2.0f, -6.0f, 18.0f, 12.0f, 11.0f, 0.0f);
        this.body.texOffs(0, 70).addBox(-4.5f, 10.0f, -3.0f, 9.0f, 5.0f, 6.0f, 0.5f);
        (this.arm0 = new ModelPart(this).setTexSize(128, 128)).setPos(0.0f, -7.0f, 0.0f);
        this.arm0.texOffs(60, 21).addBox(-13.0f, -2.5f, -3.0f, 4.0f, 30.0f, 6.0f, 0.0f);
        (this.arm1 = new ModelPart(this).setTexSize(128, 128)).setPos(0.0f, -7.0f, 0.0f);
        this.arm1.texOffs(60, 58).addBox(9.0f, -2.5f, -3.0f, 4.0f, 30.0f, 6.0f, 0.0f);
        (this.leg0 = new ModelPart(this, 0, 22).setTexSize(128, 128)).setPos(-4.0f, 11.0f, 0.0f);
        this.leg0.texOffs(37, 0).addBox(-3.5f, -3.0f, -3.0f, 6.0f, 16.0f, 5.0f, 0.0f);
        this.leg1 = new ModelPart(this, 0, 22).setTexSize(128, 128);
        this.leg1.mirror = true;
        this.leg1.texOffs(60, 0).setPos(5.0f, 11.0f, 0.0f);
        this.leg1.addBox(-3.5f, -3.0f, -3.0f, 6.0f, 16.0f, 5.0f, 0.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.arm0, this.arm1);
    }
    
    @Override
    public void setupAnim(final T baf, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.yRot = float5 * 0.017453292f;
        this.head.xRot = float6 * 0.017453292f;
        this.leg0.xRot = -1.5f * Mth.triangleWave(float2, 13.0f) * float3;
        this.leg1.xRot = 1.5f * Mth.triangleWave(float2, 13.0f) * float3;
        this.leg0.yRot = 0.0f;
        this.leg1.yRot = 0.0f;
    }
    
    @Override
    public void prepareMobModel(final T baf, final float float2, final float float3, final float float4) {
        final int integer6 = baf.getAttackAnimationTick();
        if (integer6 > 0) {
            this.arm0.xRot = -2.0f + 1.5f * Mth.triangleWave(integer6 - float4, 10.0f);
            this.arm1.xRot = -2.0f + 1.5f * Mth.triangleWave(integer6 - float4, 10.0f);
        }
        else {
            final int integer7 = baf.getOfferFlowerTick();
            if (integer7 > 0) {
                this.arm0.xRot = -0.8f + 0.025f * Mth.triangleWave((float)integer7, 70.0f);
                this.arm1.xRot = 0.0f;
            }
            else {
                this.arm0.xRot = (-0.2f + 1.5f * Mth.triangleWave(float2, 13.0f)) * float3;
                this.arm1.xRot = (-0.2f - 1.5f * Mth.triangleWave(float2, 13.0f)) * float3;
            }
        }
    }
    
    public ModelPart getFlowerHoldingArm() {
        return this.arm0;
    }
}
