package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.ShulkerBullet;

public class ShulkerBulletRenderer extends EntityRenderer<ShulkerBullet> {
    private static final ResourceLocation TEXTURE_LOCATION;
    private static final RenderType RENDER_TYPE;
    private final ShulkerBulletModel<ShulkerBullet> model;
    
    public ShulkerBulletRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.model = new ShulkerBulletModel<ShulkerBullet>();
    }
    
    @Override
    protected int getBlockLightLevel(final ShulkerBullet bgl, final BlockPos fx) {
        return 15;
    }
    
    @Override
    public void render(final ShulkerBullet bgl, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        final float float4 = Mth.rotlerp(bgl.yRotO, bgl.yRot, float3);
        final float float5 = Mth.lerp(float3, bgl.xRotO, bgl.xRot);
        final float float6 = bgl.tickCount + float3;
        dfj.translate(0.0, 0.15000000596046448, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(Mth.sin(float6 * 0.1f) * 180.0f));
        dfj.mulPose(Vector3f.XP.rotationDegrees(Mth.cos(float6 * 0.1f) * 180.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(Mth.sin(float6 * 0.15f) * 360.0f));
        dfj.scale(-0.5f, -0.5f, 0.5f);
        this.model.setupAnim(bgl, 0.0f, 0.0f, 0.0f, float4, float5);
        final VertexConsumer dfn11 = dzy.getBuffer(this.model.renderType(ShulkerBulletRenderer.TEXTURE_LOCATION));
        this.model.renderToBuffer(dfj, dfn11, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        dfj.scale(1.5f, 1.5f, 1.5f);
        final VertexConsumer dfn12 = dzy.getBuffer(ShulkerBulletRenderer.RENDER_TYPE);
        this.model.renderToBuffer(dfj, dfn12, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 0.15f);
        dfj.popPose();
        super.render(bgl, float2, float3, dfj, dzy, integer);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final ShulkerBullet bgl) {
        return ShulkerBulletRenderer.TEXTURE_LOCATION;
    }
    
    static {
        TEXTURE_LOCATION = new ResourceLocation("textures/entity/shulker/spark.png");
        RENDER_TYPE = RenderType.entityTranslucent(ShulkerBulletRenderer.TEXTURE_LOCATION);
    }
}
