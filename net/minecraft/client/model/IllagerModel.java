package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.AbstractIllager;

public class IllagerModel<T extends AbstractIllager> extends ListModel<T> implements ArmedModel, HeadedModel {
    private final ModelPart head;
    private final ModelPart hat;
    private final ModelPart body;
    private final ModelPart arms;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    
    public IllagerModel(final float float1, final float float2, final int integer3, final int integer4) {
        (this.head = new ModelPart(this).setTexSize(integer3, integer4)).setPos(0.0f, 0.0f + float2, 0.0f);
        this.head.texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f, float1);
        (this.hat = new ModelPart(this, 32, 0).setTexSize(integer3, integer4)).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 12.0f, 8.0f, float1 + 0.45f);
        this.head.addChild(this.hat);
        this.hat.visible = false;
        final ModelPart dwf6 = new ModelPart(this).setTexSize(integer3, integer4);
        dwf6.setPos(0.0f, float2 - 2.0f, 0.0f);
        dwf6.texOffs(24, 0).addBox(-1.0f, -1.0f, -6.0f, 2.0f, 4.0f, 2.0f, float1);
        this.head.addChild(dwf6);
        (this.body = new ModelPart(this).setTexSize(integer3, integer4)).setPos(0.0f, 0.0f + float2, 0.0f);
        this.body.texOffs(16, 20).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f, float1);
        this.body.texOffs(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 18.0f, 6.0f, float1 + 0.5f);
        (this.arms = new ModelPart(this).setTexSize(integer3, integer4)).setPos(0.0f, 0.0f + float2 + 2.0f, 0.0f);
        this.arms.texOffs(44, 22).addBox(-8.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f, float1);
        final ModelPart dwf7 = new ModelPart(this, 44, 22).setTexSize(integer3, integer4);
        dwf7.mirror = true;
        dwf7.addBox(4.0f, -2.0f, -2.0f, 4.0f, 8.0f, 4.0f, float1);
        this.arms.addChild(dwf7);
        this.arms.texOffs(40, 38).addBox(-4.0f, 2.0f, -2.0f, 8.0f, 4.0f, 4.0f, float1);
        (this.leftLeg = new ModelPart(this, 0, 22).setTexSize(integer3, integer4)).setPos(-2.0f, 12.0f + float2, 0.0f);
        this.leftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.rightLeg = new ModelPart(this, 0, 22).setTexSize(integer3, integer4);
        this.rightLeg.mirror = true;
        this.rightLeg.setPos(2.0f, 12.0f + float2, 0.0f);
        this.rightLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        (this.rightArm = new ModelPart(this, 40, 46).setTexSize(integer3, integer4)).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.rightArm.setPos(-5.0f, 2.0f + float2, 0.0f);
        this.leftArm = new ModelPart(this, 40, 46).setTexSize(integer3, integer4);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.leftArm.setPos(5.0f, 2.0f + float2, 0.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head, this.body, this.leftLeg, this.rightLeg, this.arms, this.rightArm, this.leftArm);
    }
    
    @Override
    public void setupAnim(final T bcv, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.yRot = float5 * 0.017453292f;
        this.head.xRot = float6 * 0.017453292f;
        this.arms.y = 3.0f;
        this.arms.z = -1.0f;
        this.arms.xRot = -0.75f;
        if (this.riding) {
            this.rightArm.xRot = -0.62831855f;
            this.rightArm.yRot = 0.0f;
            this.rightArm.zRot = 0.0f;
            this.leftArm.xRot = -0.62831855f;
            this.leftArm.yRot = 0.0f;
            this.leftArm.zRot = 0.0f;
            this.leftLeg.xRot = -1.4137167f;
            this.leftLeg.yRot = 0.31415927f;
            this.leftLeg.zRot = 0.07853982f;
            this.rightLeg.xRot = -1.4137167f;
            this.rightLeg.yRot = -0.31415927f;
            this.rightLeg.zRot = -0.07853982f;
        }
        else {
            this.rightArm.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 2.0f * float3 * 0.5f;
            this.rightArm.yRot = 0.0f;
            this.rightArm.zRot = 0.0f;
            this.leftArm.xRot = Mth.cos(float2 * 0.6662f) * 2.0f * float3 * 0.5f;
            this.leftArm.yRot = 0.0f;
            this.leftArm.zRot = 0.0f;
            this.leftLeg.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3 * 0.5f;
            this.leftLeg.yRot = 0.0f;
            this.leftLeg.zRot = 0.0f;
            this.rightLeg.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3 * 0.5f;
            this.rightLeg.yRot = 0.0f;
            this.rightLeg.zRot = 0.0f;
        }
        final AbstractIllager.IllagerArmPose a8 = bcv.getArmPose();
        if (a8 == AbstractIllager.IllagerArmPose.ATTACKING) {
            if (bcv.getMainHandItem().isEmpty()) {
                AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, true, this.attackTime, float4);
            }
            else {
                AnimationUtils.<T>swingWeaponDown(this.rightArm, this.leftArm, bcv, this.attackTime, float4);
            }
        }
        else if (a8 == AbstractIllager.IllagerArmPose.SPELLCASTING) {
            this.rightArm.z = 0.0f;
            this.rightArm.x = -5.0f;
            this.leftArm.z = 0.0f;
            this.leftArm.x = 5.0f;
            this.rightArm.xRot = Mth.cos(float4 * 0.6662f) * 0.25f;
            this.leftArm.xRot = Mth.cos(float4 * 0.6662f) * 0.25f;
            this.rightArm.zRot = 2.3561945f;
            this.leftArm.zRot = -2.3561945f;
            this.rightArm.yRot = 0.0f;
            this.leftArm.yRot = 0.0f;
        }
        else if (a8 == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
            this.rightArm.yRot = -0.1f + this.head.yRot;
            this.rightArm.xRot = -1.5707964f + this.head.xRot;
            this.leftArm.xRot = -0.9424779f + this.head.xRot;
            this.leftArm.yRot = this.head.yRot - 0.4f;
            this.leftArm.zRot = 1.5707964f;
        }
        else if (a8 == AbstractIllager.IllagerArmPose.CROSSBOW_HOLD) {
            AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
        }
        else if (a8 == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE) {
            AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, bcv, true);
        }
        else if (a8 == AbstractIllager.IllagerArmPose.CELEBRATING) {
            this.rightArm.z = 0.0f;
            this.rightArm.x = -5.0f;
            this.rightArm.xRot = Mth.cos(float4 * 0.6662f) * 0.05f;
            this.rightArm.zRot = 2.670354f;
            this.rightArm.yRot = 0.0f;
            this.leftArm.z = 0.0f;
            this.leftArm.x = 5.0f;
            this.leftArm.xRot = Mth.cos(float4 * 0.6662f) * 0.05f;
            this.leftArm.zRot = -2.3561945f;
            this.leftArm.yRot = 0.0f;
        }
        final boolean boolean9 = a8 == AbstractIllager.IllagerArmPose.CROSSED;
        this.arms.visible = boolean9;
        this.leftArm.visible = !boolean9;
        this.rightArm.visible = !boolean9;
    }
    
    private ModelPart getArm(final HumanoidArm aqf) {
        if (aqf == HumanoidArm.LEFT) {
            return this.leftArm;
        }
        return this.rightArm;
    }
    
    public ModelPart getHat() {
        return this.hat;
    }
    
    @Override
    public ModelPart getHead() {
        return this.head;
    }
    
    @Override
    public void translateToHand(final HumanoidArm aqf, final PoseStack dfj) {
        this.getArm(aqf).translateAndRotate(dfj);
    }
}
