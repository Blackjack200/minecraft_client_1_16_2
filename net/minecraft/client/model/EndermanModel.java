package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public class EndermanModel<T extends LivingEntity> extends HumanoidModel<T> {
    public boolean carrying;
    public boolean creepy;
    
    public EndermanModel(final float float1) {
        super(0.0f, -14.0f, 64, 32);
        final float float2 = -14.0f;
        (this.hat = new ModelPart(this, 0, 16)).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, float1 - 0.5f);
        this.hat.setPos(0.0f, -14.0f, 0.0f);
        (this.body = new ModelPart(this, 32, 16)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, float1);
        this.body.setPos(0.0f, -14.0f, 0.0f);
        (this.rightArm = new ModelPart(this, 56, 0)).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 30.0f, 2.0f, float1);
        this.rightArm.setPos(-3.0f, -12.0f, 0.0f);
        this.leftArm = new ModelPart(this, 56, 0);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0f, -2.0f, -1.0f, 2.0f, 30.0f, 2.0f, float1);
        this.leftArm.setPos(5.0f, -12.0f, 0.0f);
        (this.rightLeg = new ModelPart(this, 56, 0)).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 30.0f, 2.0f, float1);
        this.rightLeg.setPos(-2.0f, -2.0f, 0.0f);
        this.leftLeg = new ModelPart(this, 56, 0);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-1.0f, 0.0f, -1.0f, 2.0f, 30.0f, 2.0f, float1);
        this.leftLeg.setPos(2.0f, -2.0f, 0.0f);
    }
    
    @Override
    public void setupAnim(final T aqj, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(aqj, float2, float3, float4, float5, float6);
        this.head.visible = true;
        final float float7 = -14.0f;
        this.body.xRot = 0.0f;
        this.body.y = -14.0f;
        this.body.z = -0.0f;
        final ModelPart rightLeg = this.rightLeg;
        rightLeg.xRot -= 0.0f;
        final ModelPart leftLeg = this.leftLeg;
        leftLeg.xRot -= 0.0f;
        final ModelPart rightArm = this.rightArm;
        rightArm.xRot *= 0.5;
        final ModelPart leftArm = this.leftArm;
        leftArm.xRot *= 0.5;
        final ModelPart rightLeg2 = this.rightLeg;
        rightLeg2.xRot *= 0.5;
        final ModelPart leftLeg2 = this.leftLeg;
        leftLeg2.xRot *= 0.5;
        final float float8 = 0.4f;
        if (this.rightArm.xRot > 0.4f) {
            this.rightArm.xRot = 0.4f;
        }
        if (this.leftArm.xRot > 0.4f) {
            this.leftArm.xRot = 0.4f;
        }
        if (this.rightArm.xRot < -0.4f) {
            this.rightArm.xRot = -0.4f;
        }
        if (this.leftArm.xRot < -0.4f) {
            this.leftArm.xRot = -0.4f;
        }
        if (this.rightLeg.xRot > 0.4f) {
            this.rightLeg.xRot = 0.4f;
        }
        if (this.leftLeg.xRot > 0.4f) {
            this.leftLeg.xRot = 0.4f;
        }
        if (this.rightLeg.xRot < -0.4f) {
            this.rightLeg.xRot = -0.4f;
        }
        if (this.leftLeg.xRot < -0.4f) {
            this.leftLeg.xRot = -0.4f;
        }
        if (this.carrying) {
            this.rightArm.xRot = -0.5f;
            this.leftArm.xRot = -0.5f;
            this.rightArm.zRot = 0.05f;
            this.leftArm.zRot = -0.05f;
        }
        this.rightArm.z = 0.0f;
        this.leftArm.z = 0.0f;
        this.rightLeg.z = 0.0f;
        this.leftLeg.z = 0.0f;
        this.rightLeg.y = -5.0f;
        this.leftLeg.y = -5.0f;
        this.head.z = -0.0f;
        this.head.y = -13.0f;
        this.hat.x = this.head.x;
        this.hat.y = this.head.y;
        this.hat.z = this.head.z;
        this.hat.xRot = this.head.xRot;
        this.hat.yRot = this.head.yRot;
        this.hat.zRot = this.head.zRot;
        if (this.creepy) {
            final float float9 = 1.0f;
            final ModelPart head = this.head;
            head.y -= 5.0f;
        }
        final float float9 = -14.0f;
        this.rightArm.setPos(-5.0f, -12.0f, 0.0f);
        this.leftArm.setPos(5.0f, -12.0f, 0.0f);
    }
}
