package net.minecraft.client.model;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Sheep;

public class SheepModel<T extends Sheep> extends QuadrupedModel<T> {
    private float headXRot;
    
    public SheepModel() {
        super(12, 0.0f, false, 8.0f, 4.0f, 2.0f, 2.0f, 24);
        (this.head = new ModelPart(this, 0, 0)).addBox(-3.0f, -4.0f, -6.0f, 6.0f, 6.0f, 8.0f, 0.0f);
        this.head.setPos(0.0f, 6.0f, -8.0f);
        (this.body = new ModelPart(this, 28, 8)).addBox(-4.0f, -10.0f, -7.0f, 8.0f, 16.0f, 6.0f, 0.0f);
        this.body.setPos(0.0f, 5.0f, 2.0f);
    }
    
    @Override
    public void prepareMobModel(final T bap, final float float2, final float float3, final float float4) {
        super.prepareMobModel(bap, float2, float3, float4);
        this.head.y = 6.0f + bap.getHeadEatPositionScale(float4) * 9.0f;
        this.headXRot = bap.getHeadEatAngleScale(float4);
    }
    
    @Override
    public void setupAnim(final T bap, final float float2, final float float3, final float float4, final float float5, final float float6) {
        super.setupAnim(bap, float2, float3, float4, float5, float6);
        this.head.xRot = this.headXRot;
    }
}
