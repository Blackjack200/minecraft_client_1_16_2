package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Zombie;

public class DrownedModel<T extends Zombie> extends ZombieModel<T> {
    public DrownedModel(final float float1, final float float2, final int integer3, final int integer4) {
        super(float1, float2, integer3, integer4);
        (this.rightArm = new ModelPart(this, 32, 48)).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.rightArm.setPos(-5.0f, 2.0f + float2, 0.0f);
        (this.rightLeg = new ModelPart(this, 16, 48)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        this.rightLeg.setPos(-1.9f, 12.0f + float2, 0.0f);
    }
    
    public DrownedModel(final float float1, final boolean boolean2) {
        super(float1, 0.0f, 64, boolean2 ? 32 : 64);
    }
    
    @Override
    public void prepareMobModel(final T beg, final float float2, final float float3, final float float4) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        final ItemStack bly6 = beg.getItemInHand(InteractionHand.MAIN_HAND);
        if (bly6.getItem() == Items.TRIDENT && beg.isAggressive()) {
            if (beg.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.THROW_SPEAR;
            }
            else {
                this.leftArmPose = ArmPose.THROW_SPEAR;
            }
        }
        super.prepareMobModel(beg, float2, float3, float4);
    }
    
    @Override
    public void setupAnim(final T beg, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(beg, float2, float3, float4, float5, float6);
        if (this.leftArmPose == ArmPose.THROW_SPEAR) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5f - 3.1415927f;
            this.leftArm.yRot = 0.0f;
        }
        if (this.rightArmPose == ArmPose.THROW_SPEAR) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5f - 3.1415927f;
            this.rightArm.yRot = 0.0f;
        }
        if (this.swimAmount > 0.0f) {
            this.rightArm.xRot = this.rotlerpRad(this.swimAmount, this.rightArm.xRot, -2.5132742f) + this.swimAmount * 0.35f * Mth.sin(0.1f * float4);
            this.leftArm.xRot = this.rotlerpRad(this.swimAmount, this.leftArm.xRot, -2.5132742f) - this.swimAmount * 0.35f * Mth.sin(0.1f * float4);
            this.rightArm.zRot = this.rotlerpRad(this.swimAmount, this.rightArm.zRot, -0.15f);
            this.leftArm.zRot = this.rotlerpRad(this.swimAmount, this.leftArm.zRot, 0.15f);
            final ModelPart leftLeg = this.leftLeg;
            leftLeg.xRot -= this.swimAmount * 0.55f * Mth.sin(0.1f * float4);
            final ModelPart rightLeg = this.rightLeg;
            rightLeg.xRot += this.swimAmount * 0.55f * Mth.sin(0.1f * float4);
            this.head.xRot = 0.0f;
        }
    }
}
