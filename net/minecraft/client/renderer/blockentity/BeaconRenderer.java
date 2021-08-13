package net.minecraft.client.renderer.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import java.util.List;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

public class BeaconRenderer extends BlockEntityRenderer<BeaconBlockEntity> {
    public static final ResourceLocation BEAM_LOCATION;
    
    public BeaconRenderer(final BlockEntityRenderDispatcher ebv) {
        super(ebv);
    }
    
    @Override
    public void render(final BeaconBlockEntity ccb, final float float2, final PoseStack dfj, final MultiBufferSource dzy, final int integer5, final int integer6) {
        final long long8 = ccb.getLevel().getGameTime();
        final List<BeaconBlockEntity.BeaconBeamSection> list10 = ccb.getBeamSections();
        int integer7 = 0;
        for (int integer8 = 0; integer8 < list10.size(); ++integer8) {
            final BeaconBlockEntity.BeaconBeamSection a13 = (BeaconBlockEntity.BeaconBeamSection)list10.get(integer8);
            renderBeaconBeam(dfj, dzy, float2, long8, integer7, (integer8 == list10.size() - 1) ? 1024 : a13.getHeight(), a13.getColor());
            integer7 += a13.getHeight();
        }
    }
    
    private static void renderBeaconBeam(final PoseStack dfj, final MultiBufferSource dzy, final float float3, final long long4, final int integer5, final int integer6, final float[] arr) {
        renderBeaconBeam(dfj, dzy, BeaconRenderer.BEAM_LOCATION, float3, 1.0f, long4, integer5, integer6, arr, 0.2f, 0.25f);
    }
    
    public static void renderBeaconBeam(final PoseStack dfj, final MultiBufferSource dzy, final ResourceLocation vk, final float float4, final float float5, final long long6, final int integer7, final int integer8, final float[] arr, final float float10, final float float11) {
        final int integer9 = integer7 + integer8;
        dfj.pushPose();
        dfj.translate(0.5, 0.0, 0.5);
        final float float12 = Math.floorMod(long6, 40L) + float4;
        final float float13 = (integer8 < 0) ? float12 : (-float12);
        final float float14 = Mth.frac(float13 * 0.2f - Mth.floor(float13 * 0.1f));
        final float float15 = arr[0];
        final float float16 = arr[1];
        final float float17 = arr[2];
        dfj.pushPose();
        dfj.mulPose(Vector3f.YP.rotationDegrees(float12 * 2.25f - 45.0f));
        float float18 = 0.0f;
        float float19 = float10;
        float float20 = float10;
        float float21 = 0.0f;
        float float22 = -float10;
        float float23 = 0.0f;
        float float24 = 0.0f;
        float float25 = -float10;
        float float26 = 0.0f;
        float float27 = 1.0f;
        float float28 = -1.0f + float14;
        float float29 = integer8 * float5 * (0.5f / float10) + float28;
        renderPart(dfj, dzy.getBuffer(RenderType.beaconBeam(vk, false)), float15, float16, float17, 1.0f, integer7, integer9, 0.0f, float19, float20, 0.0f, float22, 0.0f, 0.0f, float25, 0.0f, 1.0f, float29, float28);
        dfj.popPose();
        float18 = -float11;
        float19 = -float11;
        float20 = float11;
        float21 = -float11;
        float22 = -float11;
        float23 = float11;
        float24 = float11;
        float25 = float11;
        float26 = 0.0f;
        float27 = 1.0f;
        float28 = -1.0f + float14;
        float29 = integer8 * float5 + float28;
        renderPart(dfj, dzy.getBuffer(RenderType.beaconBeam(vk, true)), float15, float16, float17, 0.125f, integer7, integer9, float18, float19, float20, float21, float22, float23, float24, float25, 0.0f, 1.0f, float29, float28);
        dfj.popPose();
    }
    
    private static void renderPart(final PoseStack dfj, final VertexConsumer dfn, final float float3, final float float4, final float float5, final float float6, final int integer7, final int integer8, final float float9, final float float10, final float float11, final float float12, final float float13, final float float14, final float float15, final float float16, final float float17, final float float18, final float float19, final float float20) {
        final PoseStack.Pose a21 = dfj.last();
        final Matrix4f b22 = a21.pose();
        final Matrix3f a22 = a21.normal();
        renderQuad(b22, a22, dfn, float3, float4, float5, float6, integer7, integer8, float9, float10, float11, float12, float17, float18, float19, float20);
        renderQuad(b22, a22, dfn, float3, float4, float5, float6, integer7, integer8, float15, float16, float13, float14, float17, float18, float19, float20);
        renderQuad(b22, a22, dfn, float3, float4, float5, float6, integer7, integer8, float11, float12, float15, float16, float17, float18, float19, float20);
        renderQuad(b22, a22, dfn, float3, float4, float5, float6, integer7, integer8, float13, float14, float9, float10, float17, float18, float19, float20);
    }
    
    private static void renderQuad(final Matrix4f b, final Matrix3f a, final VertexConsumer dfn, final float float4, final float float5, final float float6, final float float7, final int integer8, final int integer9, final float float10, final float float11, final float float12, final float float13, final float float14, final float float15, final float float16, final float float17) {
        addVertex(b, a, dfn, float4, float5, float6, float7, integer9, float10, float11, float15, float16);
        addVertex(b, a, dfn, float4, float5, float6, float7, integer8, float10, float11, float15, float17);
        addVertex(b, a, dfn, float4, float5, float6, float7, integer8, float12, float13, float14, float17);
        addVertex(b, a, dfn, float4, float5, float6, float7, integer9, float12, float13, float14, float16);
    }
    
    private static void addVertex(final Matrix4f b, final Matrix3f a, final VertexConsumer dfn, final float float4, final float float5, final float float6, final float float7, final int integer, final float float9, final float float10, final float float11, final float float12) {
        dfn.vertex(b, float9, (float)integer, float10).color(float4, float5, float6, float7).uv(float11, float12).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(a, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    @Override
    public boolean shouldRenderOffScreen(final BeaconBlockEntity ccb) {
        return true;
    }
    
    static {
        BEAM_LOCATION = new ResourceLocation("textures/entity/beacon_beam.png");
    }
}
