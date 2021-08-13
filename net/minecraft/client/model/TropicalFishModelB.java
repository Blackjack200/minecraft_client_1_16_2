package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class TropicalFishModelB<T extends Entity> extends ColorableListModel<T> {
    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart topFin;
    private final ModelPart bottomFin;
    
    public TropicalFishModelB(final float float1) {
        this.texWidth = 32;
        this.texHeight = 32;
        final int integer3 = 19;
        (this.body = new ModelPart(this, 0, 20)).addBox(-1.0f, -3.0f, -3.0f, 2.0f, 6.0f, 6.0f, float1);
        this.body.setPos(0.0f, 19.0f, 0.0f);
        (this.tail = new ModelPart(this, 21, 16)).addBox(0.0f, -3.0f, 0.0f, 0.0f, 6.0f, 5.0f, float1);
        this.tail.setPos(0.0f, 19.0f, 3.0f);
        (this.leftFin = new ModelPart(this, 2, 16)).addBox(-2.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, float1);
        this.leftFin.setPos(-1.0f, 20.0f, 0.0f);
        this.leftFin.yRot = 0.7853982f;
        (this.rightFin = new ModelPart(this, 2, 12)).addBox(0.0f, 0.0f, 0.0f, 2.0f, 2.0f, 0.0f, float1);
        this.rightFin.setPos(1.0f, 20.0f, 0.0f);
        this.rightFin.yRot = -0.7853982f;
        (this.topFin = new ModelPart(this, 20, 11)).addBox(0.0f, -4.0f, 0.0f, 0.0f, 4.0f, 6.0f, float1);
        this.topFin.setPos(0.0f, 16.0f, -3.0f);
        (this.bottomFin = new ModelPart(this, 20, 21)).addBox(0.0f, 0.0f, 0.0f, 0.0f, 4.0f, 6.0f, float1);
        this.bottomFin.setPos(0.0f, 22.0f, -3.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.body, this.tail, this.leftFin, this.rightFin, this.topFin, this.bottomFin);
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
