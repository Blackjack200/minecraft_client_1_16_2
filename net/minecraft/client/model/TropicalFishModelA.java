package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class TropicalFishModelA<T extends Entity> extends ColorableListModel<T> {
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart topFin;
    
    public TropicalFishModelA(final float float1) {
        this.texWidth = 32;
        this.texHeight = 32;
        final int integer3 = 22;
        (this.body = new ModelPart(this, 0, 0)).addBox(-1.0f, -1.5f, -3.0f, 2.0f, 3.0f, 6.0f, float1);
        this.body.setPos(0.0f, 22.0f, 0.0f);
        (this.tail = new ModelPart(this, 22, -6)).addBox(0.0f, -1.5f, 0.0f, 0.0f, 3.0f, 6.0f, float1);
        this.tail.setPos(0.0f, 22.0f, 3.0f);
        (this.leftFin = new ModelPart(this, 2, 16)).addBox(-2.0f, -1.0f, 0.0f, 2.0f, 2.0f, 0.0f, float1);
        this.leftFin.setPos(-1.0f, 22.5f, 0.0f);
        this.leftFin.yRot = 0.7853982f;
        (this.rightFin = new ModelPart(this, 2, 12)).addBox(0.0f, -1.0f, 0.0f, 2.0f, 2.0f, 0.0f, float1);
        this.rightFin.setPos(1.0f, 22.5f, 0.0f);
        this.rightFin.yRot = -0.7853982f;
        (this.topFin = new ModelPart(this, 10, -5)).addBox(0.0f, -3.0f, 0.0f, 0.0f, 3.0f, 6.0f, float1);
        this.topFin.setPos(0.0f, 20.5f, -3.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.tail, this.leftFin, this.rightFin, this.topFin);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        float float7 = 1.0f;
        if (!apx.isInWater()) {
            float7 = 1.5f;
        }
        this.tail.yRot = -float7 * 0.45f * Mth.sin(0.6f * float4);
    }
}
