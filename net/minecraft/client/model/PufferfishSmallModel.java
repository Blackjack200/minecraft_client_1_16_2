package net.minecraft.client.model;

import net.minecraft.util.Mth;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class PufferfishSmallModel<T extends Entity> extends ListModel<T> {
    private final ModelPart cube;
    private final ModelPart eye0;
    private final ModelPart eye1;
    private final ModelPart fin0;
    private final ModelPart fin1;
    private final ModelPart finBack;
    
    public PufferfishSmallModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        final int integer2 = 23;
        (this.cube = new ModelPart(this, 0, 27)).addBox(-1.5f, -2.0f, -1.5f, 3.0f, 2.0f, 3.0f);
        this.cube.setPos(0.0f, 23.0f, 0.0f);
        (this.eye0 = new ModelPart(this, 24, 6)).addBox(-1.5f, 0.0f, -1.5f, 1.0f, 1.0f, 1.0f);
        this.eye0.setPos(0.0f, 20.0f, 0.0f);
        (this.eye1 = new ModelPart(this, 28, 6)).addBox(0.5f, 0.0f, -1.5f, 1.0f, 1.0f, 1.0f);
        this.eye1.setPos(0.0f, 20.0f, 0.0f);
        (this.finBack = new ModelPart(this, -3, 0)).addBox(-1.5f, 0.0f, 0.0f, 3.0f, 0.0f, 3.0f);
        this.finBack.setPos(0.0f, 22.0f, 1.5f);
        (this.fin0 = new ModelPart(this, 25, 0)).addBox(-1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 2.0f);
        this.fin0.setPos(-1.5f, 22.0f, -1.5f);
        (this.fin1 = new ModelPart(this, 25, 0)).addBox(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 2.0f);
        this.fin1.setPos(1.5f, 22.0f, -1.5f);
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)ImmutableList.of(this.cube, this.eye0, this.eye1, this.finBack, this.fin0, this.fin1);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        this.fin0.zRot = -0.2f + 0.4f * Mth.sin(float4 * 0.2f);
        this.fin1.zRot = 0.2f - 0.4f * Mth.sin(float4 * 0.2f);
    }
}
