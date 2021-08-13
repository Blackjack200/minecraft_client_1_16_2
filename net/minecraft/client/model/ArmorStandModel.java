package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import com.google.common.collect.Iterables;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.client.model.geom.ModelPart;

public class ArmorStandModel extends ArmorStandArmorModel {
    private final ModelPart bodyStick1;
    private final ModelPart bodyStick2;
    private final ModelPart shoulderStick;
    private final ModelPart basePlate;
    
    public ArmorStandModel() {
        this(0.0f);
    }
    
    public ArmorStandModel(final float float1) {
        super(float1, 64, 64);
        (this.head = new ModelPart(this, 0, 0)).addBox(-1.0f, -7.0f, -1.0f, 2.0f, 7.0f, 2.0f, float1);
        this.head.setPos(0.0f, 0.0f, 0.0f);
        (this.body = new ModelPart(this, 0, 26)).addBox(-6.0f, 0.0f, -1.5f, 12.0f, 3.0f, 3.0f, float1);
        this.body.setPos(0.0f, 0.0f, 0.0f);
        (this.rightArm = new ModelPart(this, 24, 0)).addBox(-2.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f, float1);
        this.rightArm.setPos(-5.0f, 2.0f, 0.0f);
        this.leftArm = new ModelPart(this, 32, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(0.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f, float1);
        this.leftArm.setPos(5.0f, 2.0f, 0.0f);
        (this.rightLeg = new ModelPart(this, 8, 0)).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 11.0f, 2.0f, float1);
        this.rightLeg.setPos(-1.9f, 12.0f, 0.0f);
        this.leftLeg = new ModelPart(this, 40, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-1.0f, 0.0f, -1.0f, 2.0f, 11.0f, 2.0f, float1);
        this.leftLeg.setPos(1.9f, 12.0f, 0.0f);
        (this.bodyStick1 = new ModelPart(this, 16, 0)).addBox(-3.0f, 3.0f, -1.0f, 2.0f, 7.0f, 2.0f, float1);
        this.bodyStick1.setPos(0.0f, 0.0f, 0.0f);
        this.bodyStick1.visible = true;
        (this.bodyStick2 = new ModelPart(this, 48, 16)).addBox(1.0f, 3.0f, -1.0f, 2.0f, 7.0f, 2.0f, float1);
        this.bodyStick2.setPos(0.0f, 0.0f, 0.0f);
        (this.shoulderStick = new ModelPart(this, 0, 48)).addBox(-4.0f, 10.0f, -1.0f, 8.0f, 2.0f, 2.0f, float1);
        this.shoulderStick.setPos(0.0f, 0.0f, 0.0f);
        (this.basePlate = new ModelPart(this, 0, 32)).addBox(-6.0f, 11.0f, -6.0f, 12.0f, 1.0f, 12.0f, float1);
        this.basePlate.setPos(0.0f, 12.0f, 0.0f);
        this.hat.visible = false;
    }
    
    @Override
    public void prepareMobModel(final ArmorStand bck, final float float2, final float float3, final float float4) {
        this.basePlate.xRot = 0.0f;
        this.basePlate.yRot = 0.017453292f * -Mth.rotLerp(float4, bck.yRotO, bck.yRot);
        this.basePlate.zRot = 0.0f;
    }
    
    @Override
    public void setupAnim(final ArmorStand bck, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bck, float2, float3, float4, float5, float6);
        this.leftArm.visible = bck.isShowArms();
        this.rightArm.visible = bck.isShowArms();
        this.basePlate.visible = !bck.isNoBasePlate();
        this.leftLeg.setPos(1.9f, 12.0f, 0.0f);
        this.rightLeg.setPos(-1.9f, 12.0f, 0.0f);
        this.bodyStick1.xRot = 0.017453292f * bck.getBodyPose().getX();
        this.bodyStick1.yRot = 0.017453292f * bck.getBodyPose().getY();
        this.bodyStick1.zRot = 0.017453292f * bck.getBodyPose().getZ();
        this.bodyStick2.xRot = 0.017453292f * bck.getBodyPose().getX();
        this.bodyStick2.yRot = 0.017453292f * bck.getBodyPose().getY();
        this.bodyStick2.zRot = 0.017453292f * bck.getBodyPose().getZ();
        this.shoulderStick.xRot = 0.017453292f * bck.getBodyPose().getX();
        this.shoulderStick.yRot = 0.017453292f * bck.getBodyPose().getY();
        this.shoulderStick.zRot = 0.017453292f * bck.getBodyPose().getZ();
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)Iterables.concat((Iterable)super.bodyParts(), (Iterable)ImmutableList.of(this.bodyStick1, this.bodyStick2, this.shoulderStick, this.basePlate));
    }
    
    @Override
    public void translateToHand(final HumanoidArm aqf, final PoseStack dfj) {
        final ModelPart dwf4 = this.getArm(aqf);
        final boolean boolean5 = dwf4.visible;
        dwf4.visible = true;
        super.translateToHand(aqf, dfj);
        dwf4.visible = boolean5;
    }
}
