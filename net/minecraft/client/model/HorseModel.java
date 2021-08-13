package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class HorseModel<T extends AbstractHorse> extends AgeableListModel<T> {
    protected final ModelPart body;
    protected final ModelPart headParts;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart babyLeg1;
    private final ModelPart babyLeg2;
    private final ModelPart babyLeg3;
    private final ModelPart babyLeg4;
    private final ModelPart tail;
    private final ModelPart[] saddleParts;
    private final ModelPart[] ridingParts;
    
    public HorseModel(final float float1) {
        super(true, 16.2f, 1.36f, 2.7272f, 2.0f, 20.0f);
        this.texWidth = 64;
        this.texHeight = 64;
        (this.body = new ModelPart(this, 0, 32)).addBox(-5.0f, -8.0f, -17.0f, 10.0f, 10.0f, 22.0f, 0.05f);
        this.body.setPos(0.0f, 11.0f, 5.0f);
        (this.headParts = new ModelPart(this, 0, 35)).addBox(-2.05f, -6.0f, -2.0f, 4.0f, 12.0f, 7.0f);
        this.headParts.xRot = 0.5235988f;
        final ModelPart dwf3 = new ModelPart(this, 0, 13);
        dwf3.addBox(-3.0f, -11.0f, -2.0f, 6.0f, 5.0f, 7.0f, float1);
        final ModelPart dwf4 = new ModelPart(this, 56, 36);
        dwf4.addBox(-1.0f, -11.0f, 5.01f, 2.0f, 16.0f, 2.0f, float1);
        final ModelPart dwf5 = new ModelPart(this, 0, 25);
        dwf5.addBox(-2.0f, -11.0f, -7.0f, 4.0f, 5.0f, 5.0f, float1);
        this.headParts.addChild(dwf3);
        this.headParts.addChild(dwf4);
        this.headParts.addChild(dwf5);
        this.addEarModels(this.headParts);
        this.leg1 = new ModelPart(this, 48, 21);
        this.leg1.mirror = true;
        this.leg1.addBox(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, float1);
        this.leg1.setPos(4.0f, 14.0f, 7.0f);
        (this.leg2 = new ModelPart(this, 48, 21)).addBox(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, float1);
        this.leg2.setPos(-4.0f, 14.0f, 7.0f);
        this.leg3 = new ModelPart(this, 48, 21);
        this.leg3.mirror = true;
        this.leg3.addBox(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, float1);
        this.leg3.setPos(4.0f, 6.0f, -12.0f);
        (this.leg4 = new ModelPart(this, 48, 21)).addBox(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, float1);
        this.leg4.setPos(-4.0f, 6.0f, -12.0f);
        final float float2 = 5.5f;
        this.babyLeg1 = new ModelPart(this, 48, 21);
        this.babyLeg1.mirror = true;
        this.babyLeg1.addBox(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, float1, float1 + 5.5f, float1);
        this.babyLeg1.setPos(4.0f, 14.0f, 7.0f);
        (this.babyLeg2 = new ModelPart(this, 48, 21)).addBox(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, float1, float1 + 5.5f, float1);
        this.babyLeg2.setPos(-4.0f, 14.0f, 7.0f);
        this.babyLeg3 = new ModelPart(this, 48, 21);
        this.babyLeg3.mirror = true;
        this.babyLeg3.addBox(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, float1, float1 + 5.5f, float1);
        this.babyLeg3.setPos(4.0f, 6.0f, -12.0f);
        (this.babyLeg4 = new ModelPart(this, 48, 21)).addBox(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, float1, float1 + 5.5f, float1);
        this.babyLeg4.setPos(-4.0f, 6.0f, -12.0f);
        (this.tail = new ModelPart(this, 42, 36)).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 14.0f, 4.0f, float1);
        this.tail.setPos(0.0f, -5.0f, 2.0f);
        this.tail.xRot = 0.5235988f;
        this.body.addChild(this.tail);
        final ModelPart dwf6 = new ModelPart(this, 26, 0);
        dwf6.addBox(-5.0f, -8.0f, -9.0f, 10.0f, 9.0f, 9.0f, 0.5f);
        this.body.addChild(dwf6);
        final ModelPart dwf7 = new ModelPart(this, 29, 5);
        dwf7.addBox(2.0f, -9.0f, -6.0f, 1.0f, 2.0f, 2.0f, float1);
        this.headParts.addChild(dwf7);
        final ModelPart dwf8 = new ModelPart(this, 29, 5);
        dwf8.addBox(-3.0f, -9.0f, -6.0f, 1.0f, 2.0f, 2.0f, float1);
        this.headParts.addChild(dwf8);
        final ModelPart dwf9 = new ModelPart(this, 32, 2);
        dwf9.addBox(3.1f, -6.0f, -8.0f, 0.0f, 3.0f, 16.0f, float1);
        dwf9.xRot = -0.5235988f;
        this.headParts.addChild(dwf9);
        final ModelPart dwf10 = new ModelPart(this, 32, 2);
        dwf10.addBox(-3.1f, -6.0f, -8.0f, 0.0f, 3.0f, 16.0f, float1);
        dwf10.xRot = -0.5235988f;
        this.headParts.addChild(dwf10);
        final ModelPart dwf11 = new ModelPart(this, 1, 1);
        dwf11.addBox(-3.0f, -11.0f, -1.9f, 6.0f, 5.0f, 6.0f, 0.2f);
        this.headParts.addChild(dwf11);
        final ModelPart dwf12 = new ModelPart(this, 19, 0);
        dwf12.addBox(-2.0f, -11.0f, -4.0f, 4.0f, 5.0f, 2.0f, 0.2f);
        this.headParts.addChild(dwf12);
        this.saddleParts = new ModelPart[] { dwf6, dwf7, dwf8, dwf11, dwf12 };
        this.ridingParts = new ModelPart[] { dwf9, dwf10 };
    }
    
    protected void addEarModels(final ModelPart dwf) {
        final ModelPart dwf2 = new ModelPart(this, 19, 16);
        dwf2.addBox(0.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, -0.001f);
        final ModelPart dwf3 = new ModelPart(this, 19, 16);
        dwf3.addBox(-2.55f, -13.0f, 4.0f, 2.0f, 3.0f, 1.0f, -0.001f);
        dwf.addChild(dwf2);
        dwf.addChild(dwf3);
    }
    
    @Override
    public void setupAnim(final T bay, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final boolean boolean8 = bay.isSaddled();
        final boolean boolean9 = bay.isVehicle();
        for (final ModelPart dwf13 : this.saddleParts) {
            dwf13.visible = boolean8;
        }
        for (final ModelPart dwf13 : this.ridingParts) {
            dwf13.visible = (boolean9 && boolean8);
        }
        this.body.y = 11.0f;
    }
    
    public Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.headParts);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.leg1, this.leg2, this.leg3, this.leg4, this.babyLeg1, this.babyLeg2, this.babyLeg3, this.babyLeg4);
    }
    
    @Override
    public void prepareMobModel(final T bay, final float float2, final float float3, final float float4) {
        super.prepareMobModel(bay, float2, float3, float4);
        final float float5 = Mth.rotlerp(bay.yBodyRotO, bay.yBodyRot, float4);
        final float float6 = Mth.rotlerp(bay.yHeadRotO, bay.yHeadRot, float4);
        final float float7 = Mth.lerp(float4, bay.xRotO, bay.xRot);
        float float8 = float6 - float5;
        float float9 = float7 * 0.017453292f;
        if (float8 > 20.0f) {
            float8 = 20.0f;
        }
        if (float8 < -20.0f) {
            float8 = -20.0f;
        }
        if (float3 > 0.2f) {
            float9 += Mth.cos(float2 * 0.4f) * 0.15f * float3;
        }
        final float float10 = bay.getEatAnim(float4);
        final float float11 = bay.getStandAnim(float4);
        final float float12 = 1.0f - float11;
        final float float13 = bay.getMouthAnim(float4);
        final boolean boolean15 = bay.tailCounter != 0;
        final float float14 = bay.tickCount + float4;
        this.headParts.y = 4.0f;
        this.headParts.z = -12.0f;
        this.body.xRot = 0.0f;
        this.headParts.xRot = 0.5235988f + float9;
        this.headParts.yRot = float8 * 0.017453292f;
        final float float15 = bay.isInWater() ? 0.2f : 1.0f;
        final float float16 = Mth.cos(float15 * float2 * 0.6662f + 3.1415927f);
        final float float17 = float16 * 0.8f * float3;
        final float float18 = (1.0f - Math.max(float11, float10)) * (0.5235988f + float9 + float13 * Mth.sin(float14) * 0.05f);
        this.headParts.xRot = float11 * (0.2617994f + float9) + float10 * (2.1816616f + Mth.sin(float14) * 0.05f) + float18;
        this.headParts.yRot = float11 * float8 * 0.017453292f + (1.0f - Math.max(float11, float10)) * this.headParts.yRot;
        this.headParts.y = float11 * -4.0f + float10 * 11.0f + (1.0f - Math.max(float11, float10)) * this.headParts.y;
        this.headParts.z = float11 * -4.0f + float10 * -12.0f + (1.0f - Math.max(float11, float10)) * this.headParts.z;
        this.body.xRot = float11 * -0.7853982f + float12 * this.body.xRot;
        final float float19 = 0.2617994f * float11;
        final float float20 = Mth.cos(float14 * 0.6f + 3.1415927f);
        this.leg3.y = 2.0f * float11 + 14.0f * float12;
        this.leg3.z = -6.0f * float11 - 10.0f * float12;
        this.leg4.y = this.leg3.y;
        this.leg4.z = this.leg3.z;
        final float float21 = (-1.0471976f + float20) * float11 + float17 * float12;
        final float float22 = (-1.0471976f - float20) * float11 - float17 * float12;
        this.leg1.xRot = float19 - float16 * 0.5f * float3 * float12;
        this.leg2.xRot = float19 + float16 * 0.5f * float3 * float12;
        this.leg3.xRot = float21;
        this.leg4.xRot = float22;
        this.tail.xRot = 0.5235988f + float3 * 0.75f;
        this.tail.y = -5.0f + float3;
        this.tail.z = 2.0f + float3 * 2.0f;
        if (boolean15) {
            this.tail.yRot = Mth.cos(float14 * 0.7f);
        }
        else {
            this.tail.yRot = 0.0f;
        }
        this.babyLeg1.y = this.leg1.y;
        this.babyLeg1.z = this.leg1.z;
        this.babyLeg1.xRot = this.leg1.xRot;
        this.babyLeg2.y = this.leg2.y;
        this.babyLeg2.z = this.leg2.z;
        this.babyLeg2.xRot = this.leg2.xRot;
        this.babyLeg3.y = this.leg3.y;
        this.babyLeg3.z = this.leg3.z;
        this.babyLeg3.xRot = this.leg3.xRot;
        this.babyLeg4.y = this.leg4.y;
        this.babyLeg4.z = this.leg4.z;
        this.babyLeg4.xRot = this.leg4.xRot;
        final boolean boolean16 = bay.isBaby();
        this.leg1.visible = !boolean16;
        this.leg2.visible = !boolean16;
        this.leg3.visible = !boolean16;
        this.leg4.visible = !boolean16;
        this.babyLeg1.visible = boolean16;
        this.babyLeg2.visible = boolean16;
        this.babyLeg3.visible = boolean16;
        this.babyLeg4.visible = boolean16;
        this.body.y = (boolean16 ? 10.8f : 0.0f);
    }
}
