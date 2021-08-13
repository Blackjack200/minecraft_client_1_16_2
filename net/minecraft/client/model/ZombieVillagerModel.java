package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.Zombie;

public class ZombieVillagerModel<T extends Zombie> extends HumanoidModel<T> implements VillagerHeadModel {
    private ModelPart hatRim;
    
    public ZombieVillagerModel(final float float1, final boolean boolean2) {
        super(float1, 0.0f, 64, boolean2 ? 32 : 64);
        if (boolean2) {
            (this.head = new ModelPart(this, 0, 0)).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 8.0f, 8.0f, float1);
            (this.body = new ModelPart(this, 16, 16)).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, float1 + 0.1f);
            (this.rightLeg = new ModelPart(this, 0, 16)).setPos(-2.0f, 12.0f, 0.0f);
            this.rightLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1 + 0.1f);
            this.leftLeg = new ModelPart(this, 0, 16);
            this.leftLeg.mirror = true;
            this.leftLeg.setPos(2.0f, 12.0f, 0.0f);
            this.leftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1 + 0.1f);
        }
        else {
            this.head = new ModelPart(this, 0, 0);
            this.head.texOffs(0, 0).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f, float1);
            this.head.texOffs(24, 0).addBox(-1.0f, -3.0f, -6.0f, 2.0f, 4.0f, 2.0f, float1);
            (this.hat = new ModelPart(this, 32, 0)).addBox(-4.0f, -10.0f, -4.0f, 8.0f, 10.0f, 8.0f, float1 + 0.5f);
            this.hatRim = new ModelPart(this);
            this.hatRim.texOffs(30, 47).addBox(-8.0f, -8.0f, -6.0f, 16.0f, 16.0f, 1.0f, float1);
            this.hatRim.xRot = -1.5707964f;
            this.hat.addChild(this.hatRim);
            (this.body = new ModelPart(this, 16, 20)).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 12.0f, 6.0f, float1);
            this.body.texOffs(0, 38).addBox(-4.0f, 0.0f, -3.0f, 8.0f, 18.0f, 6.0f, float1 + 0.05f);
            (this.rightArm = new ModelPart(this, 44, 22)).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
            this.rightArm.setPos(-5.0f, 2.0f, 0.0f);
            this.leftArm = new ModelPart(this, 44, 22);
            this.leftArm.mirror = true;
            this.leftArm.addBox(-1.0f, -2.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
            this.leftArm.setPos(5.0f, 2.0f, 0.0f);
            (this.rightLeg = new ModelPart(this, 0, 22)).setPos(-2.0f, 12.0f, 0.0f);
            this.rightLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
            this.leftLeg = new ModelPart(this, 0, 22);
            this.leftLeg.mirror = true;
            this.leftLeg.setPos(2.0f, 12.0f, 0.0f);
            this.leftLeg.addBox(-2.0f, 0.0f, -2.0f, 4.0f, 12.0f, 4.0f, float1);
        }
    }
    
    @Override
    public void setupAnim(final T beg, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(beg, float2, float3, float4, float5, float6);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, beg.isAggressive(), this.attackTime, float4);
    }
    
    @Override
    public void hatVisible(final boolean boolean1) {
        this.head.visible = boolean1;
        this.hat.visible = boolean1;
        this.hatRim.visible = boolean1;
    }
}
