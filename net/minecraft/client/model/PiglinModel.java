package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.util.Mth;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Mob;

public class PiglinModel<T extends Mob> extends PlayerModel<T> {
    public final ModelPart earRight;
    public final ModelPart earLeft;
    private final ModelPart bodyDefault;
    private final ModelPart headDefault;
    private final ModelPart leftArmDefault;
    private final ModelPart rightArmDefault;
    
    public PiglinModel(final float float1, final int integer2, final int integer3) {
        super(float1, false);
        this.texWidth = integer2;
        this.texHeight = integer3;
        (this.body = new ModelPart(this, 16, 16)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, float1);
        this.head = new ModelPart(this);
        this.head.texOffs(0, 0).addBox(-5.0f, -8.0f, -4.0f, 10.0f, 8.0f, 8.0f, float1);
        this.head.texOffs(31, 1).addBox(-2.0f, -4.0f, -5.0f, 4.0f, 4.0f, 1.0f, float1);
        this.head.texOffs(2, 4).addBox(2.0f, -2.0f, -5.0f, 1.0f, 2.0f, 1.0f, float1);
        this.head.texOffs(2, 0).addBox(-3.0f, -2.0f, -5.0f, 1.0f, 2.0f, 1.0f, float1);
        (this.earRight = new ModelPart(this)).setPos(4.5f, -6.0f, 0.0f);
        this.earRight.texOffs(51, 6).addBox(0.0f, 0.0f, -2.0f, 1.0f, 5.0f, 4.0f, float1);
        this.head.addChild(this.earRight);
        (this.earLeft = new ModelPart(this)).setPos(-4.5f, -6.0f, 0.0f);
        this.earLeft.texOffs(39, 6).addBox(-1.0f, 0.0f, -2.0f, 1.0f, 5.0f, 4.0f, float1);
        this.head.addChild(this.earLeft);
        this.hat = new ModelPart(this);
        this.bodyDefault = this.body.createShallowCopy();
        this.headDefault = this.head.createShallowCopy();
        this.leftArmDefault = this.leftArm.createShallowCopy();
        this.rightArmDefault = this.leftArm.createShallowCopy();
    }
    
    @Override
    public void setupAnim(final T aqk, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.body.copyFrom(this.bodyDefault);
        this.head.copyFrom(this.headDefault);
        this.leftArm.copyFrom(this.leftArmDefault);
        this.rightArm.copyFrom(this.rightArmDefault);
        super.setupAnim(aqk, float2, float3, float4, float5, float6);
        final float float7 = 0.5235988f;
        final float float8 = float4 * 0.1f + float2 * 0.5f;
        final float float9 = 0.08f + float3 * 0.4f;
        this.earRight.zRot = -0.5235988f - Mth.cos(float8 * 1.2f) * float9;
        this.earLeft.zRot = 0.5235988f + Mth.cos(float8) * float9;
        if (aqk instanceof AbstractPiglin) {
            final AbstractPiglin beo11 = (AbstractPiglin)aqk;
            final PiglinArmPose ber12 = beo11.getArmPose();
            if (ber12 == PiglinArmPose.DANCING) {
                final float float10 = float4 / 60.0f;
                this.earLeft.zRot = 0.5235988f + 0.017453292f * Mth.sin(float10 * 30.0f) * 10.0f;
                this.earRight.zRot = -0.5235988f - 0.017453292f * Mth.cos(float10 * 30.0f) * 10.0f;
                this.head.x = Mth.sin(float10 * 10.0f);
                this.head.y = Mth.sin(float10 * 40.0f) + 0.4f;
                this.rightArm.zRot = 0.017453292f * (70.0f + Mth.cos(float10 * 40.0f) * 10.0f);
                this.leftArm.zRot = this.rightArm.zRot * -1.0f;
                this.rightArm.y = Mth.sin(float10 * 40.0f) * 0.5f + 1.5f;
                this.leftArm.y = Mth.sin(float10 * 40.0f) * 0.5f + 1.5f;
                this.body.y = Mth.sin(float10 * 40.0f) * 0.35f;
            }
            else if (ber12 == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON && this.attackTime == 0.0f) {
                this.holdWeaponHigh(aqk);
            }
            else if (ber12 == PiglinArmPose.CROSSBOW_HOLD) {
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !aqk.isLeftHanded());
            }
            else if (ber12 == PiglinArmPose.CROSSBOW_CHARGE) {
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, aqk, !aqk.isLeftHanded());
            }
            else if (ber12 == PiglinArmPose.ADMIRING_ITEM) {
                this.head.xRot = 0.5f;
                this.head.yRot = 0.0f;
                if (aqk.isLeftHanded()) {
                    this.rightArm.yRot = -0.5f;
                    this.rightArm.xRot = -0.9f;
                }
                else {
                    this.leftArm.yRot = 0.5f;
                    this.leftArm.xRot = -0.9f;
                }
            }
        }
        else if (aqk.getType() == EntityType.ZOMBIFIED_PIGLIN) {
            AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, aqk.isAggressive(), this.attackTime, float4);
        }
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }
    
    @Override
    protected void setupAttackAnimation(final T aqk, final float float2) {
        if (this.attackTime > 0.0f && aqk instanceof Piglin && ((Piglin)aqk).getArmPose() == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON) {
            AnimationUtils.<T>swingWeaponDown(this.rightArm, this.leftArm, aqk, this.attackTime, float2);
            return;
        }
        super.setupAttackAnimation(aqk, float2);
    }
    
    private void holdWeaponHigh(final T aqk) {
        if (aqk.isLeftHanded()) {
            this.leftArm.xRot = -1.8f;
        }
        else {
            this.rightArm.xRot = -1.8f;
        }
    }
}
