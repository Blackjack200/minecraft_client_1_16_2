package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Bee;

public class BeeModel<T extends Bee> extends AgeableListModel<T> {
    private final ModelPart bone;
    private final ModelPart body;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;
    private float rollAmount;
    
    public BeeModel() {
        super(false, 24.0f, 0.0f);
        this.texWidth = 64;
        this.texHeight = 64;
        (this.bone = new ModelPart(this)).setPos(0.0f, 19.0f, 0.0f);
        (this.body = new ModelPart(this, 0, 0)).setPos(0.0f, 0.0f, 0.0f);
        this.bone.addChild(this.body);
        this.body.addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f, 0.0f);
        (this.stinger = new ModelPart(this, 26, 7)).addBox(0.0f, -1.0f, 5.0f, 0.0f, 1.0f, 2.0f, 0.0f);
        this.body.addChild(this.stinger);
        (this.leftAntenna = new ModelPart(this, 2, 0)).setPos(0.0f, -2.0f, -5.0f);
        this.leftAntenna.addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f, 0.0f);
        (this.rightAntenna = new ModelPart(this, 2, 3)).setPos(0.0f, -2.0f, -5.0f);
        this.rightAntenna.addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f, 0.0f);
        this.body.addChild(this.leftAntenna);
        this.body.addChild(this.rightAntenna);
        (this.rightWing = new ModelPart(this, 0, 18)).setPos(-1.5f, -4.0f, -3.0f);
        this.rightWing.xRot = 0.0f;
        this.rightWing.yRot = -0.2618f;
        this.rightWing.zRot = 0.0f;
        this.bone.addChild(this.rightWing);
        this.rightWing.addBox(-9.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, 0.001f);
        (this.leftWing = new ModelPart(this, 0, 18)).setPos(1.5f, -4.0f, -3.0f);
        this.leftWing.xRot = 0.0f;
        this.leftWing.yRot = 0.2618f;
        this.leftWing.zRot = 0.0f;
        this.leftWing.mirror = true;
        this.bone.addChild(this.leftWing);
        this.leftWing.addBox(0.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, 0.001f);
        (this.frontLeg = new ModelPart(this)).setPos(1.5f, 3.0f, -2.0f);
        this.bone.addChild(this.frontLeg);
        this.frontLeg.addBox("frontLegBox", -5.0f, 0.0f, 0.0f, 7, 2, 0, 0.0f, 26, 1);
        (this.midLeg = new ModelPart(this)).setPos(1.5f, 3.0f, 0.0f);
        this.bone.addChild(this.midLeg);
        this.midLeg.addBox("midLegBox", -5.0f, 0.0f, 0.0f, 7, 2, 0, 0.0f, 26, 3);
        (this.backLeg = new ModelPart(this)).setPos(1.5f, 3.0f, 2.0f);
        this.bone.addChild(this.backLeg);
        this.backLeg.addBox("backLegBox", -5.0f, 0.0f, 0.0f, 7, 2, 0, 0.0f, 26, 5);
    }
    
    @Override
    public void prepareMobModel(final T azx, final float float2, final float float3, final float float4) {
        super.prepareMobModel(azx, float2, float3, float4);
        this.rollAmount = azx.getRollAmount(float4);
        this.stinger.visible = !azx.hasStung();
    }
    
    @Override
    public void setupAnim(final T azx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.rightWing.xRot = 0.0f;
        this.leftAntenna.xRot = 0.0f;
        this.rightAntenna.xRot = 0.0f;
        this.bone.xRot = 0.0f;
        this.bone.y = 19.0f;
        final boolean boolean8 = azx.isOnGround() && azx.getDeltaMovement().lengthSqr() < 1.0E-7;
        if (boolean8) {
            this.rightWing.yRot = -0.2618f;
            this.rightWing.zRot = 0.0f;
            this.leftWing.xRot = 0.0f;
            this.leftWing.yRot = 0.2618f;
            this.leftWing.zRot = 0.0f;
            this.frontLeg.xRot = 0.0f;
            this.midLeg.xRot = 0.0f;
            this.backLeg.xRot = 0.0f;
        }
        else {
            final float float7 = float4 * 2.1f;
            this.rightWing.yRot = 0.0f;
            this.rightWing.zRot = Mth.cos(float7) * 3.1415927f * 0.15f;
            this.leftWing.xRot = this.rightWing.xRot;
            this.leftWing.yRot = this.rightWing.yRot;
            this.leftWing.zRot = -this.rightWing.zRot;
            this.frontLeg.xRot = 0.7853982f;
            this.midLeg.xRot = 0.7853982f;
            this.backLeg.xRot = 0.7853982f;
            this.bone.xRot = 0.0f;
            this.bone.yRot = 0.0f;
            this.bone.zRot = 0.0f;
        }
        if (!azx.isAngry()) {
            this.bone.xRot = 0.0f;
            this.bone.yRot = 0.0f;
            this.bone.zRot = 0.0f;
            if (!boolean8) {
                final float float7 = Mth.cos(float4 * 0.18f);
                this.bone.xRot = 0.1f + float7 * 3.1415927f * 0.025f;
                this.leftAntenna.xRot = float7 * 3.1415927f * 0.03f;
                this.rightAntenna.xRot = float7 * 3.1415927f * 0.03f;
                this.frontLeg.xRot = -float7 * 3.1415927f * 0.1f + 0.3926991f;
                this.backLeg.xRot = -float7 * 3.1415927f * 0.05f + 0.7853982f;
                this.bone.y = 19.0f - Mth.cos(float4 * 0.18f) * 0.9f;
            }
        }
        if (this.rollAmount > 0.0f) {
            this.bone.xRot = ModelUtils.rotlerpRad(this.bone.xRot, 3.0915928f, this.rollAmount);
        }
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of();
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.bone);
    }
}
