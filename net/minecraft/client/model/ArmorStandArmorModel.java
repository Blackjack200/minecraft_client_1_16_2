package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;

public class ArmorStandArmorModel extends HumanoidModel<ArmorStand> {
    public ArmorStandArmorModel(final float float1) {
        this(float1, 64, 32);
    }
    
    protected ArmorStandArmorModel(final float float1, final int integer2, final int integer3) {
        super(float1, 0.0f, integer2, integer3);
    }
    
    @Override
    public void setupAnim(final ArmorStand bck, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.xRot = 0.017453292f * bck.getHeadPose().getX();
        this.head.yRot = 0.017453292f * bck.getHeadPose().getY();
        this.head.zRot = 0.017453292f * bck.getHeadPose().getZ();
        this.head.setPos(0.0f, 1.0f, 0.0f);
        this.body.xRot = 0.017453292f * bck.getBodyPose().getX();
        this.body.yRot = 0.017453292f * bck.getBodyPose().getY();
        this.body.zRot = 0.017453292f * bck.getBodyPose().getZ();
        this.leftArm.xRot = 0.017453292f * bck.getLeftArmPose().getX();
        this.leftArm.yRot = 0.017453292f * bck.getLeftArmPose().getY();
        this.leftArm.zRot = 0.017453292f * bck.getLeftArmPose().getZ();
        this.rightArm.xRot = 0.017453292f * bck.getRightArmPose().getX();
        this.rightArm.yRot = 0.017453292f * bck.getRightArmPose().getY();
        this.rightArm.zRot = 0.017453292f * bck.getRightArmPose().getZ();
        this.leftLeg.xRot = 0.017453292f * bck.getLeftLegPose().getX();
        this.leftLeg.yRot = 0.017453292f * bck.getLeftLegPose().getY();
        this.leftLeg.zRot = 0.017453292f * bck.getLeftLegPose().getZ();
        this.leftLeg.setPos(1.9f, 11.0f, 0.0f);
        this.rightLeg.xRot = 0.017453292f * bck.getRightLegPose().getX();
        this.rightLeg.yRot = 0.017453292f * bck.getRightLegPose().getY();
        this.rightLeg.zRot = 0.017453292f * bck.getRightLegPose().getZ();
        this.rightLeg.setPos(-1.9f, 11.0f, 0.0f);
        this.hat.copyFrom(this.head);
    }
}
