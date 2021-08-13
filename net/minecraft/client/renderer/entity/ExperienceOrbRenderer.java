package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ExperienceOrb;

public class ExperienceOrbRenderer extends EntityRenderer<ExperienceOrb> {
    private static final ResourceLocation EXPERIENCE_ORB_LOCATION;
    private static final RenderType RENDER_TYPE;
    
    public ExperienceOrbRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.shadowRadius = 0.15f;
        this.shadowStrength = 0.75f;
    }
    
    @Override
    protected int getBlockLightLevel(final ExperienceOrb aqd, final BlockPos fx) {
        return Mth.clamp(super.getBlockLightLevel(aqd, fx) + 7, 0, 15);
    }
    
    @Override
    public void render(final ExperienceOrb aqd, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        final int integer2 = aqd.getIcon();
        final float float4 = (integer2 % 4 * 16 + 0) / 64.0f;
        final float float5 = (integer2 % 4 * 16 + 16) / 64.0f;
        final float float6 = (integer2 / 4 * 16 + 0) / 64.0f;
        final float float7 = (integer2 / 4 * 16 + 16) / 64.0f;
        final float float8 = 1.0f;
        final float float9 = 0.5f;
        final float float10 = 0.25f;
        final float float11 = 255.0f;
        final float float12 = (aqd.tickCount + float3) / 2.0f;
        final int integer3 = (int)((Mth.sin(float12 + 0.0f) + 1.0f) * 0.5f * 255.0f);
        final int integer4 = 255;
        final int integer5 = (int)((Mth.sin(float12 + 4.1887903f) + 1.0f) * 0.1f * 255.0f);
        dfj.translate(0.0, 0.10000000149011612, 0.0);
        dfj.mulPose(this.entityRenderDispatcher.cameraOrientation());
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        final float float13 = 0.3f;
        dfj.scale(0.3f, 0.3f, 0.3f);
        final VertexConsumer dfn22 = dzy.getBuffer(ExperienceOrbRenderer.RENDER_TYPE);
        final PoseStack.Pose a23 = dfj.last();
        final Matrix4f b24 = a23.pose();
        final Matrix3f a24 = a23.normal();
        vertex(dfn22, b24, a24, -0.5f, -0.25f, integer3, 255, integer5, float4, float7, integer);
        vertex(dfn22, b24, a24, 0.5f, -0.25f, integer3, 255, integer5, float5, float7, integer);
        vertex(dfn22, b24, a24, 0.5f, 0.75f, integer3, 255, integer5, float5, float6, integer);
        vertex(dfn22, b24, a24, -0.5f, 0.75f, integer3, 255, integer5, float4, float6, integer);
        dfj.popPose();
        super.render(aqd, float2, float3, dfj, dzy, integer);
    }
    
    private static void vertex(final VertexConsumer dfn, final Matrix4f b, final Matrix3f a, final float float4, final float float5, final int integer6, final int integer7, final int integer8, final float float9, final float float10, final int integer11) {
        dfn.vertex(b, float4, float5, 0.0f).color(integer6, integer7, integer8, 128).uv(float9, float10).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer11).normal(a, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ExperienceOrb aqd) {
        return ExperienceOrbRenderer.EXPERIENCE_ORB_LOCATION;
    }
    
    static {
        EXPERIENCE_ORB_LOCATION = new ResourceLocation("textures/entity/experience_orb.png");
        RENDER_TYPE = RenderType.itemEntityTranslucentCull(ExperienceOrbRenderer.EXPERIENCE_ORB_LOCATION);
    }
}
