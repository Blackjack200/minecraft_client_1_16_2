package net.minecraft.client.model;

import net.minecraft.util.Mth;
import java.util.Arrays;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;

public class EndermiteModel<T extends Entity> extends ListModel<T> {
    private static final int[][] BODY_SIZES;
    private static final int[][] BODY_TEXS;
    private static final int BODY_COUNT;
    private final ModelPart[] bodyParts;
    
    public EndermiteModel() {
        this.bodyParts = new ModelPart[EndermiteModel.BODY_COUNT];
        float float2 = -3.5f;
        for (int integer3 = 0; integer3 < this.bodyParts.length; ++integer3) {
            (this.bodyParts[integer3] = new ModelPart(this, EndermiteModel.BODY_TEXS[integer3][0], EndermiteModel.BODY_TEXS[integer3][1])).addBox(EndermiteModel.BODY_SIZES[integer3][0] * -0.5f, 0.0f, EndermiteModel.BODY_SIZES[integer3][2] * -0.5f, (float)EndermiteModel.BODY_SIZES[integer3][0], (float)EndermiteModel.BODY_SIZES[integer3][1], (float)EndermiteModel.BODY_SIZES[integer3][2]);
            this.bodyParts[integer3].setPos(0.0f, (float)(24 - EndermiteModel.BODY_SIZES[integer3][1]), float2);
            if (integer3 < this.bodyParts.length - 1) {
                float2 += (EndermiteModel.BODY_SIZES[integer3][2] + EndermiteModel.BODY_SIZES[integer3 + 1][2]) * 0.5f;
            }
        }
    }
    
    @Override
    public Iterable<ModelPart> parts() {
        return (Iterable<ModelPart>)Arrays.asList((Object[])this.bodyParts);
    }
    
    @Override
    public void setupAnim(final T apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        for (int integer8 = 0; integer8 < this.bodyParts.length; ++integer8) {
            this.bodyParts[integer8].yRot = Mth.cos(float4 * 0.9f + integer8 * 0.15f * 3.1415927f) * 3.1415927f * 0.01f * (1 + Math.abs(integer8 - 2));
            this.bodyParts[integer8].x = Mth.sin(float4 * 0.9f + integer8 * 0.15f * 3.1415927f) * 3.1415927f * 0.1f * Math.abs(integer8 - 2);
        }
    }
    
    static {
        BODY_SIZES = new int[][] { { 4, 3, 2 }, { 6, 4, 5 }, { 3, 3, 1 }, { 1, 2, 1 } };
        BODY_TEXS = new int[][] { { 0, 0 }, { 0, 5 }, { 0, 14 }, { 0, 18 } };
        BODY_COUNT = EndermiteModel.BODY_SIZES.length;
    }
}
