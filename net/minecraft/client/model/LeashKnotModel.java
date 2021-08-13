package net.minecraft.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class LeashKnotModel<T extends Entity> extends ListModel<T> {
    private final ModelPart knot;
    
    public LeashKnotModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        (this.knot = new ModelPart(this, 0, 0)).addBox(-3.0f, -6.0f, -3.0f, 6.0f, 8.0f, 6.0f, 0.0f);
        this.knot.setPos(0.0f, 0.0f, 0.0f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.knot);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.knot.yRot = float5 * 0.017453292f;
        this.knot.xRot = float6 * 0.017453292f;
    }
}
