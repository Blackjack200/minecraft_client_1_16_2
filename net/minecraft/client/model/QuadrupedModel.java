package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class QuadrupedModel<T extends Entity> extends AgeableListModel<T> {
    protected ModelPart head;
    protected ModelPart body;
    protected ModelPart leg0;
    protected ModelPart leg1;
    protected ModelPart leg2;
    protected ModelPart leg3;
    
    public QuadrupedModel(final int integer1, final float float2, final boolean boolean3, final float float4, final float float5, final float float6, final float float7, final int integer8) {
        super(boolean3, float4, float5, float6, float7, (float)integer8);
        (this.head = new ModelPart(this, 0, 0)).addBox(-4.0f, -4.0f, -8.0f, 8.0f, 8.0f, 8.0f, float2);
        this.head.setPos(0.0f, (float)(18 - integer1), -6.0f);
        (this.body = new ModelPart(this, 28, 8)).addBox(-5.0f, -10.0f, -7.0f, 10.0f, 16.0f, 8.0f, float2);
        this.body.setPos(0.0f, (float)(17 - integer1), 2.0f);
        (this.leg0 = new ModelPart(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, (float)integer1, 4.0f, float2);
        this.leg0.setPos(-3.0f, (float)(24 - integer1), 7.0f);
        (this.leg1 = new ModelPart(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, (float)integer1, 4.0f, float2);
        this.leg1.setPos(3.0f, (float)(24 - integer1), 7.0f);
        (this.leg2 = new ModelPart(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, (float)integer1, 4.0f, float2);
        this.leg2.setPos(-3.0f, (float)(24 - integer1), -5.0f);
        (this.leg3 = new ModelPart(this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4.0f, (float)integer1, 4.0f, float2);
        this.leg3.setPos(3.0f, (float)(24 - integer1), -5.0f);
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.leg0, this.leg1, this.leg2, this.leg3);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.xRot = float6 * 0.017453292f;
        this.head.yRot = float5 * 0.017453292f;
        this.body.xRot = 1.5707964f;
        this.leg0.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3;
        this.leg1.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3;
        this.leg2.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3;
        this.leg3.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3;
    }
}
