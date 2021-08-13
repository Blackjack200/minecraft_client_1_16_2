package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

public abstract class AbstractZombieModel<T extends Monster> extends HumanoidModel<T> {
    protected AbstractZombieModel(final float float1, final float float2, final int integer3, final int integer4) {
        super(float1, float2, integer3, integer4);
    }
    
    @Override
    public void setupAnim(final T bdn, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bdn, float2, float3, float4, float5, float6);
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(bdn), this.attackTime, float4);
    }
    
    public abstract boolean isAggressive(final T bdn);
}
