package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.model.geom.ModelPart;

public class SkeletonModel<T extends Mob> extends HumanoidModel<T> {
    public SkeletonModel() {
        this(0.0f, false);
    }
    
    public SkeletonModel(final float float1, final boolean boolean2) {
        super(float1);
        if (!boolean2) {
            (this.rightArm = new ModelPart(this, 40, 16)).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f, float1);
            this.rightArm.setPos(-5.0f, 2.0f, 0.0f);
            this.leftArm = new ModelPart(this, 40, 16);
            this.leftArm.mirror = true;
            this.leftArm.addBox(-1.0f, -2.0f, -1.0f, 2.0f, 12.0f, 2.0f, float1);
            this.leftArm.setPos(5.0f, 2.0f, 0.0f);
            (this.rightLeg = new ModelPart(this, 0, 16)).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f, float1);
            this.rightLeg.setPos(-2.0f, 12.0f, 0.0f);
            this.leftLeg = new ModelPart(this, 0, 16);
            this.leftLeg.mirror = true;
            this.leftLeg.addBox(-1.0f, 0.0f, -1.0f, 2.0f, 12.0f, 2.0f, float1);
            this.leftLeg.setPos(2.0f, 12.0f, 0.0f);
        }
    }
    
    @Override
    public void prepareMobModel(final T aqk, final float float2, final float float3, final float float4) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        final ItemStack bly6 = ((LivingEntity)aqk).getItemInHand(InteractionHand.MAIN_HAND);
        if (bly6.getItem() == Items.BOW && ((Mob)aqk).isAggressive()) {
            if (((Mob)aqk).getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            }
            else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }
        super.prepareMobModel(aqk, float2, float3, float4);
    }
    
    @Override
    public void setupAnim(final T aqk, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(aqk, float2, float3, float4, float5, float6);
        final ItemStack bly8 = ((LivingEntity)aqk).getMainHandItem();
        if (((Mob)aqk).isAggressive() && (bly8.isEmpty() || bly8.getItem() != Items.BOW)) {
            final float float7 = Mth.sin(this.attackTime * 3.1415927f);
            final float float8 = Mth.sin((1.0f - (1.0f - this.attackTime) * (1.0f - this.attackTime)) * 3.1415927f);
            this.rightArm.zRot = 0.0f;
            this.leftArm.zRot = 0.0f;
            this.rightArm.yRot = -(0.1f - float7 * 0.6f);
            this.leftArm.yRot = 0.1f - float7 * 0.6f;
            this.rightArm.xRot = -1.5707964f;
            this.leftArm.xRot = -1.5707964f;
            final ModelPart rightArm = this.rightArm;
            rightArm.xRot -= float7 * 1.2f - float8 * 0.4f;
            final ModelPart leftArm = this.leftArm;
            leftArm.xRot -= float7 * 1.2f - float8 * 0.4f;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, float4);
        }
    }
    
    @Override
    public void translateToHand(final HumanoidArm aqf, final PoseStack dfj) {
        final float float4 = (aqf == HumanoidArm.RIGHT) ? 1.0f : -1.0f;
        final ModelPart arm;
        final ModelPart dwf5 = arm = this.getArm(aqf);
        arm.x += float4;
        dwf5.translateAndRotate(dfj);
        final ModelPart modelPart = dwf5;
        modelPart.x -= float4;
    }
}
