package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LightningBolt;

public class LightningBoltRenderer extends EntityRenderer<LightningBolt> {
    public LightningBoltRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public void render(final LightningBolt aqi, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        final float[] arr8 = new float[8];
        final float[] arr9 = new float[8];
        float float4 = 0.0f;
        float float5 = 0.0f;
        final Random random12 = new Random(aqi.seed);
        for (int integer2 = 7; integer2 >= 0; --integer2) {
            arr8[integer2] = float4;
            arr9[integer2] = float5;
            float4 += random12.nextInt(11) - 5;
            float5 += random12.nextInt(11) - 5;
        }
        final VertexConsumer dfn12 = dzy.getBuffer(RenderType.lightning());
        final Matrix4f b13 = dfj.last().pose();
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            final Random random13 = new Random(aqi.seed);
            for (int integer4 = 0; integer4 < 3; ++integer4) {
                int integer5 = 7;
                int integer6 = 0;
                if (integer4 > 0) {
                    integer5 = 7 - integer4;
                }
                if (integer4 > 0) {
                    integer6 = integer5 - 2;
                }
                float float6 = arr8[integer5] - float4;
                float float7 = arr9[integer5] - float5;
                for (int integer7 = integer5; integer7 >= integer6; --integer7) {
                    final float float8 = float6;
                    final float float9 = float7;
                    if (integer4 == 0) {
                        float6 += random13.nextInt(11) - 5;
                        float7 += random13.nextInt(11) - 5;
                    }
                    else {
                        float6 += random13.nextInt(31) - 15;
                        float7 += random13.nextInt(31) - 15;
                    }
                    final float float10 = 0.5f;
                    final float float11 = 0.45f;
                    final float float12 = 0.45f;
                    final float float13 = 0.5f;
                    float float14 = 0.1f + integer3 * 0.2f;
                    if (integer4 == 0) {
                        float14 *= (float)(integer7 * 0.1 + 1.0);
                    }
                    float float15 = 0.1f + integer3 * 0.2f;
                    if (integer4 == 0) {
                        float15 *= (integer7 - 1) * 0.1f + 1.0f;
                    }
                    quad(b13, dfn12, float6, float7, integer7, float8, float9, 0.45f, 0.45f, 0.5f, float14, float15, false, false, true, false);
                    quad(b13, dfn12, float6, float7, integer7, float8, float9, 0.45f, 0.45f, 0.5f, float14, float15, true, false, true, true);
                    quad(b13, dfn12, float6, float7, integer7, float8, float9, 0.45f, 0.45f, 0.5f, float14, float15, true, true, false, true);
                    quad(b13, dfn12, float6, float7, integer7, float8, float9, 0.45f, 0.45f, 0.5f, float14, float15, false, true, false, false);
                }
            }
        }
    }
    
    private static void quad(final Matrix4f b, final VertexConsumer dfn, final float float3, final float float4, final int integer, final float float6, final float float7, final float float8, final float float9, final float float10, final float float11, final float float12, final boolean boolean13, final boolean boolean14, final boolean boolean15, final boolean boolean16) {
        dfn.vertex(b, float3 + (boolean13 ? float12 : (-float12)), (float)(integer * 16), float4 + (boolean14 ? float12 : (-float12))).color(float8, float9, float10, 0.3f).endVertex();
        dfn.vertex(b, float6 + (boolean13 ? float11 : (-float11)), (float)((integer + 1) * 16), float7 + (boolean14 ? float11 : (-float11))).color(float8, float9, float10, 0.3f).endVertex();
        dfn.vertex(b, float6 + (boolean15 ? float11 : (-float11)), (float)((integer + 1) * 16), float7 + (boolean16 ? float11 : (-float11))).color(float8, float9, float10, 0.3f).endVertex();
        dfn.vertex(b, float3 + (boolean15 ? float12 : (-float12)), (float)(integer * 16), float4 + (boolean16 ? float12 : (-float12))).color(float8, float9, float10, 0.3f).endVertex();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final LightningBolt aqi) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
