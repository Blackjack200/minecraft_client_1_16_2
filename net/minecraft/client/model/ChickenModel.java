package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class ChickenModel<T extends Entity> extends AgeableListModel<T> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart wing0;
    private final ModelPart wing1;
    private final ModelPart beak;
    private final ModelPart redThing;
    
    public ChickenModel() {
        final int integer2 = 16;
        (this.head = new ModelPart(this, 0, 0)).addBox(-2.0f, -6.0f, -2.0f, 4.0f, 6.0f, 3.0f, 0.0f);
        this.head.setPos(0.0f, 15.0f, -4.0f);
        (this.beak = new ModelPart(this, 14, 0)).addBox(-2.0f, -4.0f, -4.0f, 4.0f, 2.0f, 2.0f, 0.0f);
        this.beak.setPos(0.0f, 15.0f, -4.0f);
        (this.redThing = new ModelPart(this, 14, 4)).addBox(-1.0f, -2.0f, -3.0f, 2.0f, 2.0f, 2.0f, 0.0f);
        this.redThing.setPos(0.0f, 15.0f, -4.0f);
        (this.body = new ModelPart(this, 0, 9)).addBox(-3.0f, -4.0f, -3.0f, 6.0f, 8.0f, 6.0f, 0.0f);
        this.body.setPos(0.0f, 16.0f, 0.0f);
        (this.leg0 = new ModelPart(this, 26, 0)).addBox(-1.0f, 0.0f, -3.0f, 3.0f, 5.0f, 3.0f);
        this.leg0.setPos(-2.0f, 19.0f, 1.0f);
        (this.leg1 = new ModelPart(this, 26, 0)).addBox(-1.0f, 0.0f, -3.0f, 3.0f, 5.0f, 3.0f);
        this.leg1.setPos(1.0f, 19.0f, 1.0f);
        (this.wing0 = new ModelPart(this, 24, 13)).addBox(0.0f, 0.0f, -3.0f, 1.0f, 4.0f, 6.0f);
        this.wing0.setPos(-4.0f, 13.0f, 0.0f);
        (this.wing1 = new ModelPart(this, 24, 13)).addBox(-1.0f, 0.0f, -3.0f, 1.0f, 4.0f, 6.0f);
        this.wing1.setPos(4.0f, 13.0f, 0.0f);
    }
    
    @Override
    protected Iterable<ModelPart> headParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.head, this.beak, this.redThing);
    }
    
    @Override
    protected Iterable<ModelPart> bodyParts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.leg0, this.leg1, this.wing0, this.wing1);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.head.xRot = float6 * 0.017453292f;
        this.head.yRot = float5 * 0.017453292f;
        this.beak.xRot = this.head.xRot;
        this.beak.yRot = this.head.yRot;
        this.redThing.xRot = this.head.xRot;
        this.redThing.yRot = this.head.yRot;
        this.body.xRot = 1.5707964f;
        this.leg0.xRot = Mth.cos(float2 * 0.6662f) * 1.4f * float3;
        this.leg1.xRot = Mth.cos(float2 * 0.6662f + 3.1415927f) * 1.4f * float3;
        this.wing0.zRot = float4;
        this.wing1.zRot = -float4;
    }
}
