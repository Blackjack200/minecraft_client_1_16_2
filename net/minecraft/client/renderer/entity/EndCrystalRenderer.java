package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public class EndCrystalRenderer extends EntityRenderer<EndCrystal> {
    private static final ResourceLocation END_CRYSTAL_LOCATION;
    private static final RenderType RENDER_TYPE;
    private static final float SIN_45;
    private final ModelPart cube;
    private final ModelPart glass;
    private final ModelPart base;
    
    public EndCrystalRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.shadowRadius = 0.5f;
        (this.glass = new ModelPart(64, 32, 0, 0)).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f);
        (this.cube = new ModelPart(64, 32, 32, 0)).addBox(-4.0f, -4.0f, -4.0f, 8.0f, 8.0f, 8.0f);
        (this.base = new ModelPart(64, 32, 0, 16)).addBox(-6.0f, 0.0f, -6.0f, 12.0f, 4.0f, 12.0f);
    }
    
    @Override
    public void render(final EndCrystal bbn, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        final float float4 = getY(bbn, float3);
        final float float5 = (bbn.time + float3) * 3.0f;
        final VertexConsumer dfn10 = dzy.getBuffer(EndCrystalRenderer.RENDER_TYPE);
        dfj.pushPose();
        dfj.scale(2.0f, 2.0f, 2.0f);
        dfj.translate(0.0, -0.5, 0.0);
        final int integer2 = OverlayTexture.NO_OVERLAY;
        if (bbn.showsBottom()) {
            this.base.render(dfj, dfn10, integer, integer2);
        }
        dfj.mulPose(Vector3f.YP.rotationDegrees(float5));
        dfj.translate(0.0, 1.5f + float4 / 2.0f, 0.0);
        dfj.mulPose(new Quaternion(new Vector3f(EndCrystalRenderer.SIN_45, 0.0f, EndCrystalRenderer.SIN_45), 60.0f, true));
        this.glass.render(dfj, dfn10, integer, integer2);
        final float float6 = 0.875f;
        dfj.scale(0.875f, 0.875f, 0.875f);
        dfj.mulPose(new Quaternion(new Vector3f(EndCrystalRenderer.SIN_45, 0.0f, EndCrystalRenderer.SIN_45), 60.0f, true));
        dfj.mulPose(Vector3f.YP.rotationDegrees(float5));
        this.glass.render(dfj, dfn10, integer, integer2);
        dfj.scale(0.875f, 0.875f, 0.875f);
        dfj.mulPose(new Quaternion(new Vector3f(EndCrystalRenderer.SIN_45, 0.0f, EndCrystalRenderer.SIN_45), 60.0f, true));
        dfj.mulPose(Vector3f.YP.rotationDegrees(float5));
        this.cube.render(dfj, dfn10, integer, integer2);
        dfj.popPose();
        dfj.popPose();
        final BlockPos fx13 = bbn.getBeamTarget();
        if (fx13 != null) {
            final float float7 = fx13.getX() + 0.5f;
            final float float8 = fx13.getY() + 0.5f;
            final float float9 = fx13.getZ() + 0.5f;
            final float float10 = (float)(float7 - bbn.getX());
            final float float11 = (float)(float8 - bbn.getY());
            final float float12 = (float)(float9 - bbn.getZ());
            dfj.translate(float10, float11, float12);
            EnderDragonRenderer.renderCrystalBeams(-float10, -float11 + float4, -float12, float3, bbn.time, dfj, dzy, integer);
        }
        super.render(bbn, float2, float3, dfj, dzy, integer);
    }
    
    public static float getY(final EndCrystal bbn, final float float2) {
        final float float3 = bbn.time + float2;
        float float4 = Mth.sin(float3 * 0.2f) / 2.0f + 0.5f;
        float4 = (float4 * float4 + float4) * 0.4f;
        return float4 - 1.4f;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final EndCrystal bbn) {
        return EndCrystalRenderer.END_CRYSTAL_LOCATION;
    }
    
    @Override
    public boolean shouldRender(final EndCrystal bbn, final Frustum ecr, final double double3, final double double4, final double double5) {
        return super.shouldRender(bbn, ecr, double3, double4, double5) || bbn.getBeamTarget() != null;
    }
    
    static {
        END_CRYSTAL_LOCATION = new ResourceLocation("textures/entity/end_crystal/end_crystal.png");
        RENDER_TYPE = RenderType.entityCutoutNoCull(EndCrystalRenderer.END_CRYSTAL_LOCATION);
        SIN_45 = (float)Math.sin(0.7853981633974483);
    }
}
