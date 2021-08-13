package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Panda;

public class PandaModel<T extends Panda> extends QuadrupedModel<T> {
    private float sitAmount;
    private float lieOnBackAmount;
    private float rollAmount;
    
    public PandaModel(final int integer, final float float2) {
        super(integer, float2, true, 23.0f, 4.8f, 2.7f, 3.0f, 49);
        this.texWidth = 64;
        this.texHeight = 64;
        (this.head = new ModelPart(this, 0, 6)).addBox(-6.5f, -5.0f, -4.0f, 13.0f, 10.0f, 9.0f);
        this.head.setPos(0.0f, 11.5f, -17.0f);
        this.head.texOffs(45, 16).addBox(-3.5f, 0.0f, -6.0f, 7.0f, 5.0f, 2.0f);
        this.head.texOffs(52, 25).addBox(-8.5f, -8.0f, -1.0f, 5.0f, 4.0f, 1.0f);
        this.head.texOffs(52, 25).addBox(3.5f, -8.0f, -1.0f, 5.0f, 4.0f, 1.0f);
        (this.body = new ModelPart(this, 0, 25)).addBox(-9.5f, -13.0f, -6.5f, 19.0f, 26.0f, 13.0f);
        this.body.setPos(0.0f, 10.0f, 0.0f);
        final int integer2 = 9;
        final int integer3 = 6;
        (this.leg0 = new ModelPart(this, 40, 0)).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 9.0f, 6.0f);
        this.leg0.setPos(-5.5f, 15.0f, 9.0f);
        (this.leg1 = new ModelPart(this, 40, 0)).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 9.0f, 6.0f);
        this.leg1.setPos(5.5f, 15.0f, 9.0f);
        (this.leg2 = new ModelPart(this, 40, 0)).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 9.0f, 6.0f);
        this.leg2.setPos(-5.5f, 15.0f, -9.0f);
        (this.leg3 = new ModelPart(this, 40, 0)).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 9.0f, 6.0f);
        this.leg3.setPos(5.5f, 15.0f, -9.0f);
    }
    
    @Override
    public void prepareMobModel(final T bai, final float float2, final float float3, final float float4) {
        super.prepareMobModel(bai, float2, float3, float4);
        this.sitAmount = bai.getSitAmount(float4);
        this.lieOnBackAmount = bai.getLieOnBackAmount(float4);
        this.rollAmount = (bai.isBaby() ? 0.0f : bai.getRollAmount(float4));
    }
    
    @Override
    public void setupAnim(final T bai, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bai, float2, float3, float4, float5, float6);
        final boolean boolean8 = bai.getUnhappyCounter() > 0;
        final boolean boolean9 = bai.isSneezing();
        final int integer10 = bai.getSneezeCounter();
        final boolean boolean10 = bai.isEating();
        final boolean boolean11 = bai.isScared();
        if (boolean8) {
            this.head.yRot = 0.35f * Mth.sin(0.6f * float4);
            this.head.zRot = 0.35f * Mth.sin(0.6f * float4);
            this.leg2.xRot = -0.75f * Mth.sin(0.3f * float4);
            this.leg3.xRot = 0.75f * Mth.sin(0.3f * float4);
        }
        else {
            this.head.zRot = 0.0f;
        }
        if (boolean9) {
            if (integer10 < 15) {
                this.head.xRot = -0.7853982f * integer10 / 14.0f;
            }
            else if (integer10 < 20) {
                final float float7 = (float)((integer10 - 15) / 5);
                this.head.xRot = -0.7853982f + 0.7853982f * float7;
            }
        }
        if (this.sitAmount > 0.0f) {
            this.body.xRot = ModelUtils.rotlerpRad(this.body.xRot, 1.7407963f, this.sitAmount);
            this.head.xRot = ModelUtils.rotlerpRad(this.head.xRot, 1.5707964f, this.sitAmount);
            this.leg2.zRot = -0.27079642f;
            this.leg3.zRot = 0.27079642f;
            this.leg0.zRot = 0.5707964f;
            this.leg1.zRot = -0.5707964f;
            if (boolean10) {
                this.head.xRot = 1.5707964f + 0.2f * Mth.sin(float4 * 0.6f);
                this.leg2.xRot = -0.4f - 0.2f * Mth.sin(float4 * 0.6f);
                this.leg3.xRot = -0.4f - 0.2f * Mth.sin(float4 * 0.6f);
            }
            if (boolean11) {
                this.head.xRot = 2.1707964f;
                this.leg2.xRot = -0.9f;
                this.leg3.xRot = -0.9f;
            }
        }
        else {
            this.leg0.zRot = 0.0f;
            this.leg1.zRot = 0.0f;
            this.leg2.zRot = 0.0f;
            this.leg3.zRot = 0.0f;
        }
        if (this.lieOnBackAmount > 0.0f) {
            this.leg0.xRot = -0.6f * Mth.sin(float4 * 0.15f);
            this.leg1.xRot = 0.6f * Mth.sin(float4 * 0.15f);
            this.leg2.xRot = 0.3f * Mth.sin(float4 * 0.25f);
            this.leg3.xRot = -0.3f * Mth.sin(float4 * 0.25f);
            this.head.xRot = ModelUtils.rotlerpRad(this.head.xRot, 1.5707964f, this.lieOnBackAmount);
        }
        if (this.rollAmount > 0.0f) {
            this.head.xRot = ModelUtils.rotlerpRad(this.head.xRot, 2.0561945f, this.rollAmount);
            this.leg0.xRot = -0.5f * Mth.sin(float4 * 0.5f);
            this.leg1.xRot = 0.5f * Mth.sin(float4 * 0.5f);
            this.leg2.xRot = 0.5f * Mth.sin(float4 * 0.5f);
            this.leg3.xRot = -0.5f * Mth.sin(float4 * 0.5f);
        }
    }
}
