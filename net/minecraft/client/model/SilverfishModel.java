package net.minecraft.client.model;

import net.minecraft.util.Mth;
import java.util.Arrays;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class SilverfishModel<T extends Entity> extends ListModel<T> {
    private final ModelPart[] bodyParts;
    private final ModelPart[] bodyLayers;
    private final ImmutableList<ModelPart> parts;
    private final float[] zPlacement;
    private static final int[][] BODY_SIZES;
    private static final int[][] BODY_TEXS;
    
    public SilverfishModel() {
        this.zPlacement = new float[7];
        this.bodyParts = new ModelPart[7];
        float float2 = -3.5f;
        for (int integer3 = 0; integer3 < this.bodyParts.length; ++integer3) {
            (this.bodyParts[integer3] = new ModelPart(this, SilverfishModel.BODY_TEXS[integer3][0], SilverfishModel.BODY_TEXS[integer3][1])).addBox(SilverfishModel.BODY_SIZES[integer3][0] * -0.5f, 0.0f, SilverfishModel.BODY_SIZES[integer3][2] * -0.5f, (float)SilverfishModel.BODY_SIZES[integer3][0], (float)SilverfishModel.BODY_SIZES[integer3][1], (float)SilverfishModel.BODY_SIZES[integer3][2]);
            this.bodyParts[integer3].setPos(0.0f, (float)(24 - SilverfishModel.BODY_SIZES[integer3][1]), float2);
            this.zPlacement[integer3] = float2;
            if (integer3 < this.bodyParts.length - 1) {
                float2 += (SilverfishModel.BODY_SIZES[integer3][2] + SilverfishModel.BODY_SIZES[integer3 + 1][2]) * 0.5f;
            }
        }
        this.bodyLayers = new ModelPart[3];
        (this.bodyLayers[0] = new ModelPart(this, 20, 0)).addBox(-5.0f, 0.0f, SilverfishModel.BODY_SIZES[2][2] * -0.5f, 10.0f, 8.0f, (float)SilverfishModel.BODY_SIZES[2][2]);
        this.bodyLayers[0].setPos(0.0f, 16.0f, this.zPlacement[2]);
        (this.bodyLayers[1] = new ModelPart(this, 20, 11)).addBox(-3.0f, 0.0f, SilverfishModel.BODY_SIZES[4][2] * -0.5f, 6.0f, 4.0f, (float)SilverfishModel.BODY_SIZES[4][2]);
        this.bodyLayers[1].setPos(0.0f, 20.0f, this.zPlacement[4]);
        (this.bodyLayers[2] = new ModelPart(this, 20, 18)).addBox(-3.0f, 0.0f, SilverfishModel.BODY_SIZES[4][2] * -0.5f, 6.0f, 5.0f, (float)SilverfishModel.BODY_SIZES[1][2]);
        this.bodyLayers[2].setPos(0.0f, 19.0f, this.zPlacement[1]);
        final ImmutableList.Builder<ModelPart> builder3 = (ImmutableList.Builder<ModelPart>)ImmutableList.builder();
        builder3.addAll((Iterable)Arrays.asList((Object[])this.bodyParts));
        builder3.addAll((Iterable)Arrays.asList((Object[])this.bodyLayers));
        this.parts = (ImmutableList<ModelPart>)builder3.build();
    }
    
    public ImmutableList<ModelPart> parts() {
        return this.parts;
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        for (int integer8 = 0; integer8 < this.bodyParts.length; ++integer8) {
            this.bodyParts[integer8].yRot = Mth.cos(float4 * 0.9f + integer8 * 0.15f * 3.1415927f) * 3.1415927f * 0.05f * (1 + Math.abs(integer8 - 2));
            this.bodyParts[integer8].x = Mth.sin(float4 * 0.9f + integer8 * 0.15f * 3.1415927f) * 3.1415927f * 0.2f * Math.abs(integer8 - 2);
        }
        this.bodyLayers[0].yRot = this.bodyParts[2].yRot;
        this.bodyLayers[1].yRot = this.bodyParts[4].yRot;
        this.bodyLayers[1].x = this.bodyParts[4].x;
        this.bodyLayers[2].yRot = this.bodyParts[1].yRot;
        this.bodyLayers[2].x = this.bodyParts[1].x;
    }
    
    static {
        BODY_SIZES = new int[][] { { 3, 2, 2 }, { 4, 3, 2 }, { 6, 4, 3 }, { 3, 3, 3 }, { 2, 2, 3 }, { 2, 1, 2 }, { 1, 1, 2 } };
        BODY_TEXS = new int[][] { { 0, 0 }, { 0, 4 }, { 0, 9 }, { 0, 16 }, { 0, 22 }, { 11, 0 }, { 13, 4 } };
    }
}
