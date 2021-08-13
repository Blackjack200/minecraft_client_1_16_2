package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.DragonFireball;

public class DragonFireballRenderer extends EntityRenderer<DragonFireball> {
    private static final ResourceLocation TEXTURE_LOCATION;
    private static final RenderType RENDER_TYPE;
    
    public DragonFireballRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    protected int getBlockLightLevel(final DragonFireball bga, final BlockPos fx) {
        return 15;
    }
    
    @Override
    public void render(final DragonFireball bga, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.scale(2.0f, 2.0f, 2.0f);
        dfj.mulPose(this.entityRenderDispatcher.cameraOrientation());
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        final PoseStack.Pose a8 = dfj.last();
        final Matrix4f b9 = a8.pose();
        final Matrix3f a9 = a8.normal();
        final VertexConsumer dfn11 = dzy.getBuffer(DragonFireballRenderer.RENDER_TYPE);
        vertex(dfn11, b9, a9, integer, 0.0f, 0, 0, 1);
        vertex(dfn11, b9, a9, integer, 1.0f, 0, 1, 1);
        vertex(dfn11, b9, a9, integer, 1.0f, 1, 1, 0);
        vertex(dfn11, b9, a9, integer, 0.0f, 1, 0, 0);
        dfj.popPose();
        super.render(bga, float2, float3, dfj, dzy, integer);
    }
    
    private static void vertex(final VertexConsumer dfn, final Matrix4f b, final Matrix3f a, final int integer4, final float float5, final int integer6, final int integer7, final int integer8) {
        dfn.vertex(b, float5 - 0.5f, integer6 - 0.25f, 0.0f).color(255, 255, 255, 255).uv((float)integer7, (float)integer8).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer4).normal(a, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    @Override
    public ResourceLocation getTextureLocation(final DragonFireball bga) {
        return DragonFireballRenderer.TEXTURE_LOCATION;
    }
    
    static {
        TEXTURE_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");
        RENDER_TYPE = RenderType.entityCutoutNoCull(DragonFireballRenderer.TEXTURE_LOCATION);
    }
}
