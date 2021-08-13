package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class OcelotModel<T extends Entity> extends AgeableListModel<T> {
    protected final ModelPart backLegL;
    protected final ModelPart backLegR;
    protected final ModelPart frontLegL;
    protected final ModelPart frontLegR;
    protected final ModelPart tail1;
    protected final ModelPart tail2;
    protected final ModelPart head;
    protected final ModelPart body;
    protected int state;
    
    public OcelotModel(final float float1) {
        super(true, 10.0f, 4.0f);
        this.state = 1;
        (this.head = new ModelPart(this)).addBox("main", -2.5f, -2.0f, -3.0f, 5, 4, 5, float1, 0, 0);
        this.head.addBox("nose", -1.5f, 0.0f, -4.0f, 3, 2, 2, float1, 0, 24);
        this.head.addBox("ear1", -2.0f, -3.0f, 0.0f, 1, 1, 2, float1, 0, 10);
        this.head.addBox("ear2", 1.0f, -3.0f, 0.0f, 1, 1, 2, float1, 6, 10);
        this.head.setPos(0.0f, 15.0f, -9.0f);
        (this.body = new ModelPart(this, 20, 0)).addBox(-2.0f, 3.0f, -8.0f, 4.0f, 16.0f, 6.0f, float1);
        this.body.setPos(0.0f, 12.0f, -10.0f);
        (this.tail1 = new ModelPart(this, 0, 15)).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 8.0f, 1.0f, float1);
        this.tail1.xRot = 0.9f;
        this.tail1.setPos(0.0f, 15.0f, 8.0f);
        (this.tail2 = new ModelPart(this, 4, 15)).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 8.0f, 1.0f, float1);
        this.tail2.setPos(0.0f, 20.0f, 14.0f);
        (this.backLegL = new ModelPart(this, 8, 13)).addBox(-1.0f, 0.0f, 1.0f, 2.0f, 6.0f, 2.0f, float1);
        this.backLegL.setPos(1.1f, 18.0f, 5.0f);
        (this.backLegR = new ModelPart(this, 8, 13)).addBox(-1.0f, 0.0f, 1.0f, 2.0f, 6.0f, 2.0f, float1);
        this.backLegR.setPos(-1.1f, 18.0f, 5.0f);
        (this.frontLegL = new ModelPart(this, 40, 0)).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 10.0f, 2.0f, float1);
        this.frontLegL.setPos(1.2f, 14.1f, -5.0f);
        (this.frontLegR = new ModelPart(this, 40, 0)).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 10.0f, 2.0f, float1);
        this.frontLegR.setPos(-1.2f, 14.1f, -5.0f);
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.backLegL, this.backLegR, this.frontLegL, this.frontLegR, this.tail1, this.tail2);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.xRot = float6 * 0.017453292f;
        this.head.yRot = float5 * 0.017453292f;
        if (this.state != 3) {
            this.body.xRot = 1.5707964f;
            if (this.state == 2) {
                this.backLegL.xRot = Mth.cos(float2 * 0.6662f) * float3;
                this.backLegR.xRot = Mth.cos(float2 * 0.6662f + 0.3f) * float3;
                this.frontLegL.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f + 0.3f) * float3;
                this.frontLegR.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * float3;
                this.tail2.xRot = 1.7278761f + 0.31415927f * Mth.cos(float2) * float3;
            }
            else {
                this.backLegL.xRot = Mth.cos(float2 * 0.6662f) * float3;
                this.backLegR.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * float3;
                this.frontLegL.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * float3;
                this.frontLegR.xRot = Mth.cos(float2 * 0.6662f) * float3;
                if (this.state == 1) {
                    this.tail2.xRot = 1.7278761f + 0.7853982f * Mth.cos(float2) * float3;
                }
                else {
                    this.tail2.xRot = 1.7278761f + 0.47123894f * Mth.cos(float2) * float3;
                }
            }
        }
    }
    
    @Override
    public void prepareMobModel(final T apx, final float float2, final float float3, final float float4) {
        this.body.y = 12.0f;
        this.body.z = -10.0f;
        this.head.y = 15.0f;
        this.head.z = -9.0f;
        this.tail1.y = 15.0f;
        this.tail1.z = 8.0f;
        this.tail2.y = 20.0f;
        this.tail2.z = 14.0f;
        this.frontLegL.y = 14.1f;
        this.frontLegL.z = -5.0f;
        this.frontLegR.y = 14.1f;
        this.frontLegR.z = -5.0f;
        this.backLegL.y = 18.0f;
        this.backLegL.z = 5.0f;
        this.backLegR.y = 18.0f;
        this.backLegR.z = 5.0f;
        this.tail1.xRot = 0.9f;
        if (apx.isCrouching()) {
            final ModelPart body = this.body;
            ++body.y;
            final ModelPart head = this.head;
            head.y += 2.0f;
            final ModelPart tail1 = this.tail1;
            ++tail1.y;
            final ModelPart tail2 = this.tail2;
            tail2.y -= 4.0f;
            final ModelPart tail3 = this.tail2;
            tail3.z += 2.0f;
            this.tail1.xRot = 1.5707964f;
            this.tail2.xRot = 1.5707964f;
            this.state = 0;
        }
        else if (apx.isSprinting()) {
            this.tail2.y = this.tail1.y;
            final ModelPart tail4 = this.tail2;
            tail4.z += 2.0f;
            this.tail1.xRot = 1.5707964f;
            this.tail2.xRot = 1.5707964f;
            this.state = 2;
        }
        else {
            this.state = 1;
        }
    }
}
