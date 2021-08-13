package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionHand;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class HumanoidModel<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    public ModelPart head;
    public ModelPart hat;
    public ModelPart body;
    public ModelPart rightArm;
    public ModelPart leftArm;
    public ModelPart rightLeg;
    public ModelPart leftLeg;
    public ArmPose leftArmPose;
    public ArmPose rightArmPose;
    public boolean crouching;
    public float swimAmount;
    
    public HumanoidModel(final float float1) {
        this(RenderType::entityCutoutNoCull, float1, 0.0f, 64, 32);
    }
    
    protected HumanoidModel(final float float1, final float float2, final int integer3, final int integer4) {
        this(RenderType::entityCutoutNoCull, float1, float2, integer3, integer4);
    }
    
    public HumanoidModel(final Function<ResourceLocation, RenderType> function, final float float2, final float float3, final int integer4, final int integer5) {
        super(function, true, 16.0f, 0.0f, 2.0f, 2.0f, 24.0f);
        this.leftArmPose = ArmPose.EMPTY;
        this.rightArmPose = ArmPose.EMPTY;
        this.texWidth = integer4;
        this.texHeight = integer5;
        (this.head = new ModelPart(this, 0, 0)).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, float2);
        this.head.setPos(0.0f, 0.0f + float3, 0.0f);
        (this.hat = new ModelPart(this, 32, 0)).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, float2 + 0.5f);
        this.hat.setPos(0.0f, 0.0f + float3, 0.0f);
        (this.body = new ModelPart(this, 16, 16)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, float2);
        this.body.setPos(0.0f, 0.0f + float3, 0.0f);
        (this.rightArm = new ModelPart(this, 40, 16)).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float2);
        this.rightArm.setPos(-5.0f, 2.0f + float3, 0.0f);
        this.leftArm = new ModelPart(this, 40, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float2);
        this.leftArm.setPos(5.0f, 2.0f + float3, 0.0f);
        (this.rightLeg = new ModelPart(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float2);
        this.rightLeg.setPos(-1.9f, 12.0f + float3, 0.0f);
        this.leftLeg = new ModelPart(this, 0, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float2);
        this.leftLeg.setPos(1.9f, 12.0f + float3, 0.0f);
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.hat);
    }
    
    @Override
    public void prepareMobModel(final T aqj, final float float2, final float float3, final float float4) {
        this.swimAmount = aqj.getSwimAmount(float4);
        super.prepareMobModel(aqj, float2, float3, float4);
    }
    
    @Override
    public void setupAnim(final T aqj, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final boolean boolean8 = aqj.getFallFlyingTicks() > 4;
        final boolean boolean9 = aqj.isVisuallySwimming();
        this.head.yRot = float5 * 0.017453292f;
        if (boolean8) {
            this.head.xRot = -0.7853982f;
        }
        else if (this.swimAmount > 0.0f) {
            if (boolean9) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, -0.7853982f);
            }
            else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, float6 * 0.017453292f);
            }
        }
        else {
            this.head.xRot = float6 * 0.017453292f;
        }
        this.body.yRot = 0.0f;
        this.rightArm.z = 0.0f;
        this.rightArm.x = -5.0f;
        this.leftArm.z = 0.0f;
        this.leftArm.x = 5.0f;
        float float7 = 1.0f;
        if (boolean8) {
            float7 = (float)aqj.getDeltaMovement().lengthSqr();
            float7 /= 0.2f;
            float7 *= float7 * float7;
        }
        if (float7 < 1.0f) {
            float7 = 1.0f;
        }
        this.rightArm.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 2.0f * float3 * 0.5f / float7;
        this.leftArm.xRot = Mth.cos(float2 * 0.6662f) * 2.0f * float3 * 0.5f / float7;
        this.rightArm.zRot = 0.0f;
        this.leftArm.zRot = 0.0f;
        this.rightLeg.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3 / float7;
        this.leftLeg.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3 / float7;
        this.rightLeg.yRot = 0.0f;
        this.leftLeg.yRot = 0.0f;
        this.rightLeg.zRot = 0.0f;
        this.leftLeg.zRot = 0.0f;
        if (this.riding) {
            final ModelPart rightArm = this.rightArm;
            rightArm.xRot -= 0.62831855f;
            final ModelPart leftArm = this.leftArm;
            leftArm.xRot -= 0.62831855f;
            this.rightLeg.xRot = -1.4137167f;
            this.rightLeg.yRot = 0.31415927f;
            this.rightLeg.zRot = 0.07853982f;
            this.leftLeg.xRot = -1.4137167f;
            this.leftLeg.yRot = -0.31415927f;
            this.leftLeg.zRot = -0.07853982f;
        }
        this.rightArm.yRot = 0.0f;
        this.leftArm.yRot = 0.0f;
        final boolean boolean10 = aqj.getMainArm() == HumanoidArm.RIGHT;
        final boolean boolean11 = boolean10 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
        if (boolean10 != boolean11) {
            this.poseLeftArm(aqj);
            this.poseRightArm(aqj);
        }
        else {
            this.poseRightArm(aqj);
            this.poseLeftArm(aqj);
        }
        this.setupAttackAnimation(aqj, float4);
        if (this.crouching) {
            this.body.xRot = 0.5f;
            final ModelPart rightArm2 = this.rightArm;
            rightArm2.xRot += 0.4f;
            final ModelPart leftArm2 = this.leftArm;
            leftArm2.xRot += 0.4f;
            this.rightLeg.z = 4.0f;
            this.leftLeg.z = 4.0f;
            this.rightLeg.y = 12.2f;
            this.leftLeg.y = 12.2f;
            this.head.y = 4.2f;
            this.body.y = 3.2f;
            this.leftArm.y = 5.2f;
            this.rightArm.y = 5.2f;
        }
        else {
            this.body.xRot = 0.0f;
            this.rightLeg.z = 0.1f;
            this.leftLeg.z = 0.1f;
            this.rightLeg.y = 12.0f;
            this.leftLeg.y = 12.0f;
            this.head.y = 0.0f;
            this.body.y = 0.0f;
            this.leftArm.y = 2.0f;
            this.rightArm.y = 2.0f;
        }
        AnimationUtils.bobArms(this.rightArm, this.leftArm, float4);
        if (this.swimAmount > 0.0f) {
            final float float8 = float2 % 26.0f;
            final HumanoidArm aqf14 = this.getAttackArm(aqj);
            final float float9 = (aqf14 == HumanoidArm.RIGHT && this.attackTime > 0.0f) ? 0.0f : this.swimAmount;
            final float float10 = (aqf14 == HumanoidArm.LEFT && this.attackTime > 0.0f) ? 0.0f : this.swimAmount;
            if (float8 < 14.0f) {
                this.leftArm.xRot = this.rotlerpRad(float10, this.leftArm.xRot, 0.0f);
                this.rightArm.xRot = Mth.lerp(float9, this.rightArm.xRot, 0.0f);
                this.leftArm.yRot = this.rotlerpRad(float10, this.leftArm.yRot, 3.1415927f);
                this.rightArm.yRot = Mth.lerp(float9, this.rightArm.yRot, 3.1415927f);
                this.leftArm.zRot = this.rotlerpRad(float10, this.leftArm.zRot, 3.1415927f + 1.8707964f * this.quadraticArmUpdate(float8) / this.quadraticArmUpdate(14.0f));
                this.rightArm.zRot = Mth.lerp(float9, this.rightArm.zRot, 3.1415927f - 1.8707964f * this.quadraticArmUpdate(float8) / this.quadraticArmUpdate(14.0f));
            }
            else if (float8 >= 14.0f && float8 < 22.0f) {
                final float float11 = (float8 - 14.0f) / 8.0f;
                this.leftArm.xRot = this.rotlerpRad(float10, this.leftArm.xRot, 1.5707964f * float11);
                this.rightArm.xRot = Mth.lerp(float9, this.rightArm.xRot, 1.5707964f * float11);
                this.leftArm.yRot = this.rotlerpRad(float10, this.leftArm.yRot, 3.1415927f);
                this.rightArm.yRot = Mth.lerp(float9, this.rightArm.yRot, 3.1415927f);
                this.leftArm.zRot = this.rotlerpRad(float10, this.leftArm.zRot, 5.012389f - 1.8707964f * float11);
                this.rightArm.zRot = Mth.lerp(float9, this.rightArm.zRot, 1.2707963f + 1.8707964f * float11);
            }
            else if (float8 >= 22.0f && float8 < 26.0f) {
                final float float11 = (float8 - 22.0f) / 4.0f;
                this.leftArm.xRot = this.rotlerpRad(float10, this.leftArm.xRot, 1.5707964f - 1.5707964f * float11);
                this.rightArm.xRot = Mth.lerp(float9, this.rightArm.xRot, 1.5707964f - 1.5707964f * float11);
                this.leftArm.yRot = this.rotlerpRad(float10, this.leftArm.yRot, 3.1415927f);
                this.rightArm.yRot = Mth.lerp(float9, this.rightArm.yRot, 3.1415927f);
                this.leftArm.zRot = this.rotlerpRad(float10, this.leftArm.zRot, 3.1415927f);
                this.rightArm.zRot = Mth.lerp(float9, this.rightArm.zRot, 3.1415927f);
            }
            final float float11 = 0.3f;
            final float float12 = 0.33333334f;
            this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot, 0.3f * Mth.cos(float2 * 0.33333334f + 3.1415927f));
            this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3f * Mth.cos(float2 * 0.33333334f));
        }
        this.hat.copyFrom(this.head);
    }
    
    private void poseRightArm(final T aqj) {
        switch (this.rightArmPose) {
            case EMPTY: {
                this.rightArm.yRot = 0.0f;
                break;
            }
            case BLOCK: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.9424779f;
                this.rightArm.yRot = -0.5235988f;
                break;
            }
            case ITEM: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - 0.31415927f;
                this.rightArm.yRot = 0.0f;
                break;
            }
            case THROW_SPEAR: {
                this.rightArm.xRot = this.rightArm.xRot * 0.5f - 3.1415927f;
                this.rightArm.yRot = 0.0f;
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yRot = -0.1f + this.head.yRot;
                this.leftArm.yRot = 0.1f + this.head.yRot + 0.4f;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                break;
            }
            case CROSSBOW_CHARGE: {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, aqj, true);
                break;
            }
            case CROSSBOW_HOLD: {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            }
        }
    }
    
    private void poseLeftArm(final T aqj) {
        switch (this.leftArmPose) {
            case EMPTY: {
                this.leftArm.yRot = 0.0f;
                break;
            }
            case BLOCK: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - 0.9424779f;
                this.leftArm.yRot = 0.5235988f;
                break;
            }
            case ITEM: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - 0.31415927f;
                this.leftArm.yRot = 0.0f;
                break;
            }
            case THROW_SPEAR: {
                this.leftArm.xRot = this.leftArm.xRot * 0.5f - 3.1415927f;
                this.leftArm.yRot = 0.0f;
                break;
            }
            case BOW_AND_ARROW: {
                this.rightArm.yRot = -0.1f + this.head.yRot - 0.4f;
                this.leftArm.yRot = 0.1f + this.head.yRot;
                this.rightArm.xRot = -1.5707964f + this.head.xRot;
                this.leftArm.xRot = -1.5707964f + this.head.xRot;
                break;
            }
            case CROSSBOW_CHARGE: {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, aqj, false);
                break;
            }
            case CROSSBOW_HOLD: {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
                break;
            }
        }
    }
    
    protected void setupAttackAnimation(final T aqj, final float float2) {
        if (this.attackTime <= 0.0f) {
            return;
        }
        final HumanoidArm aqf4 = this.getAttackArm(aqj);
        final ModelPart dwf5 = this.getArm(aqf4);
        float float3 = this.attackTime;
        this.body.yRot = Mth.sin(Mth.sqrt(float3) * 6.2831855f) * 0.2f;
        if (aqf4 == HumanoidArm.LEFT) {
            final ModelPart body = this.body;
            body.yRot *= -1.0f;
        }
        this.rightArm.z = Mth.sin(this.body.yRot) * 5.0f;
        this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0f;
        this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0f;
        this.leftArm.x = Mth.cos(this.body.yRot) * 5.0f;
        final ModelPart rightArm = this.rightArm;
        rightArm.yRot += this.body.yRot;
        final ModelPart leftArm = this.leftArm;
        leftArm.yRot += this.body.yRot;
        final ModelPart leftArm2 = this.leftArm;
        leftArm2.xRot += this.body.yRot;
        float3 = 1.0f - this.attackTime;
        float3 *= float3;
        float3 *= float3;
        float3 = 1.0f - float3;
        final float float4 = Mth.sin(float3 * 3.1415927f);
        final float float5 = Mth.sin(this.attackTime * 3.1415927f) * -(this.head.xRot - 0.7f) * 0.75f;
        final ModelPart modelPart = dwf5;
        modelPart.xRot -= (float)(float4 * 1.2 + float5);
        final ModelPart modelPart2 = dwf5;
        modelPart2.yRot += this.body.yRot * 2.0f;
        final ModelPart modelPart3 = dwf5;
        modelPart3.zRot += Mth.sin(this.attackTime * 3.1415927f) * -0.4f;
    }
    
    protected float rotlerpRad(final float float1, final float float2, final float float3) {
        float float4 = (float3 - float2) % 6.2831855f;
        if (float4 < -3.1415927f) {
            float4 += 6.2831855f;
        }
        if (float4 >= 3.1415927f) {
            float4 -= 6.2831855f;
        }
        return float2 + float1 * float4;
    }
    
    private float quadraticArmUpdate(final float float1) {
        return -65.0f * float1 + float1 * float1;
    }
    
    public void copyPropertiesTo(final HumanoidModel<T> due) {
        super.copyPropertiesTo(due);
        due.leftArmPose = this.leftArmPose;
        due.rightArmPose = this.rightArmPose;
        due.crouching = this.crouching;
        due.head.copyFrom(this.head);
        due.hat.copyFrom(this.hat);
        due.body.copyFrom(this.body);
        due.rightArm.copyFrom(this.rightArm);
        due.leftArm.copyFrom(this.leftArm);
        due.rightLeg.copyFrom(this.rightLeg);
        due.leftLeg.copyFrom(this.leftLeg);
    }
    
    public void setAllVisible(final boolean boolean1) {
        this.head.visible = boolean1;
        this.hat.visible = boolean1;
        this.body.visible = boolean1;
        this.rightArm.visible = boolean1;
        this.leftArm.visible = boolean1;
        this.rightLeg.visible = boolean1;
        this.leftLeg.visible = boolean1;
    }
    
    @Override
    public void translateToHand(final HumanoidArm aqf, final PoseStack dfj) {
        this.getArm(aqf).translateAndRotate(dfj);
    }
    
    protected ModelPart getArm(final HumanoidArm aqf) {
        if (aqf == HumanoidArm.LEFT) {
            return this.leftArm;
        }
        return this.rightArm;
    }
    
    @Override
    public ModelPart getHead() {
        return this.head;
    }
    
    protected HumanoidArm getAttackArm(final T aqj) {
        final HumanoidArm aqf3 = aqj.getMainArm();
        return (aqj.swingingArm == InteractionHand.MAIN_HAND) ? aqf3 : aqf3.getOpposite();
    }
    
    public enum ArmPose {
        EMPTY(false), 
        ITEM(false), 
        BLOCK(false), 
        BOW_AND_ARROW(true), 
        THROW_SPEAR(false), 
        CROSSBOW_CHARGE(true), 
        CROSSBOW_HOLD(true);
        
        private final boolean twoHanded;
        
        private ArmPose(final boolean boolean3) {
            this.twoHanded = boolean3;
        }
        
        public boolean isTwoHanded() {
            return this.twoHanded;
        }
    }
}
