package net.minecraft.client.model;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;

public class HoglinModel<T extends Mob> extends AgeableListModel<T> {
    private final ModelPart head;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart body;
    private final ModelPart frontRightLeg;
    private final ModelPart frontLeftLeg;
    private final ModelPart backRightLeg;
    private final ModelPart backLeftLeg;
    private final ModelPart mane;
    
    public HoglinModel() {
        super(true, 8.0f, 6.0f, 1.9f, 2.0f, 24.0f);
        this.texWidth = 128;
        this.texHeight = 64;
        (this.body = new ModelPart(this)).setPos(0.0f, 7.0f, 0.0f);
        this.body.texOffs(1, 1).addBox(-8.0f, -7.0f, -13.0f, 16.0f, 14.0f, 26.0f);
        (this.mane = new ModelPart(this)).setPos(0.0f, -14.0f, -5.0f);
        this.mane.texOffs(90, 33).addBox(0.0f, 0.0f, -9.0f, 0.0f, 10.0f, 19.0f, 0.001f);
        this.body.addChild(this.mane);
        (this.head = new ModelPart(this)).setPos(0.0f, 2.0f, -12.0f);
        this.head.texOffs(61, 1).addBox(-7.0f, -3.0f, -19.0f, 14.0f, 6.0f, 19.0f);
        (this.rightEar = new ModelPart(this)).setPos(-6.0f, -2.0f, -3.0f);
        this.rightEar.texOffs(1, 1).addBox(-6.0f, -1.0f, -2.0f, 6.0f, 1.0f, 4.0f);
        this.rightEar.zRot = -0.6981317f;
        this.head.addChild(this.rightEar);
        (this.leftEar = new ModelPart(this)).setPos(6.0f, -2.0f, -3.0f);
        this.leftEar.texOffs(1, 6).addBox(0.0f, -1.0f, -2.0f, 6.0f, 1.0f, 4.0f);
        this.leftEar.zRot = 0.6981317f;
        this.head.addChild(this.leftEar);
        final ModelPart dwf2 = new ModelPart(this);
        dwf2.setPos(-7.0f, 2.0f, -12.0f);
        dwf2.texOffs(10, 13).addBox(-1.0f, -11.0f, -1.0f, 2.0f, 11.0f, 2.0f);
        this.head.addChild(dwf2);
        final ModelPart dwf3 = new ModelPart(this);
        dwf3.setPos(7.0f, 2.0f, -12.0f);
        dwf3.texOffs(1, 13).addBox(-1.0f, -11.0f, -1.0f, 2.0f, 11.0f, 2.0f);
        this.head.addChild(dwf3);
        this.head.xRot = 0.87266463f;
        final int integer4 = 14;
        final int integer5 = 11;
        (this.frontRightLeg = new ModelPart(this)).setPos(-4.0f, 10.0f, -8.5f);
        this.frontRightLeg.texOffs(66, 42).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 14.0f, 6.0f);
        (this.frontLeftLeg = new ModelPart(this)).setPos(4.0f, 10.0f, -8.5f);
        this.frontLeftLeg.texOffs(41, 42).addBox(-3.0f, 0.0f, -3.0f, 6.0f, 14.0f, 6.0f);
        (this.backRightLeg = new ModelPart(this)).setPos(-5.0f, 13.0f, 10.0f);
        this.backRightLeg.texOffs(21, 45).addBox(-2.5f, 0.0f, -2.5f, 5.0f, 11.0f, 5.0f);
        (this.backLeftLeg = new ModelPart(this)).setPos(5.0f, 13.0f, 10.0f);
        this.backLeftLeg.texOffs(0, 45).addBox(-2.5f, 0.0f, -2.5f, 5.0f, 11.0f, 5.0f);
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.frontRightLeg, this.frontLeftLeg, this.backRightLeg, this.backLeftLeg);
    }
    
    @Override
    public void setupAnim(final T aqk, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.rightEar.zRot = -0.6981317f - float3 * Mth.sin(float2);
        this.leftEar.zRot = 0.6981317f + float3 * Mth.sin(float2);
        this.head.yRot = float5 * 0.017453292f;
        final int integer8 = ((HoglinBase)aqk).getAttackAnimationRemainingTicks();
        final float float7 = 1.0f - Mth.abs(10 - 2 * integer8) / 10.0f;
        this.head.xRot = Mth.lerp(float7, 0.87266463f, -0.34906584f);
        if (((LivingEntity)aqk).isBaby()) {
            this.head.y = Mth.lerp(float7, 2.0f, 5.0f);
            this.mane.z = -3.0f;
        }
        else {
            this.head.y = 2.0f;
            this.mane.z = -7.0f;
        }
        final float float8 = 1.2f;
        this.frontRightLeg.xRot = Mth.cos(float2) * 1.2f * float3;
        this.frontLeftLeg.xRot = Mth.cos(float2 + 3.1415927f) * 1.2f * float3;
        this.backRightLeg.xRot = this.frontLeftLeg.xRot;
        this.backLeftLeg.xRot = this.frontRightLeg.xRot;
    }
}
