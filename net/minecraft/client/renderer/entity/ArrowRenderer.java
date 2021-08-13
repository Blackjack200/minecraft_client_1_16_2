package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.projectile.AbstractArrow;

public abstract class ArrowRenderer<T extends AbstractArrow> extends EntityRenderer<T> {
    public ArrowRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public void render(final T bfx, final float float2, final float float3, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        dfj.pushPose();
        dfj.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(float3, bfx.yRotO, bfx.yRot) - 90.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(float3, bfx.xRotO, bfx.xRot)));
        final int integer2 = 0;
        final float float4 = 0.0f;
        final float float5 = 0.5f;
        final float float6 = 0.0f;
        final float float7 = 0.15625f;
        final float float8 = 0.0f;
        final float float9 = 0.15625f;
        final float float10 = 0.15625f;
        final float float11 = 0.3125f;
        final float float12 = 0.05625f;
        final float float13 = bfx.shakeTime - float3;
        if (float13 > 0.0f) {
            final float float14 = -Mth.sin(float13 * 3.0f) * float13;
            dfj.mulPose(Vector3f.ZP.rotationDegrees(float14));
        }
        dfj.mulPose(Vector3f.XP.rotationDegrees(45.0f));
        dfj.scale(0.05625f, 0.05625f, 0.05625f);
        dfj.translate(-4.0, 0.0, 0.0);
        final VertexConsumer dfn19 = dzy.getBuffer(RenderType.entityCutout(this.getTextureLocation(bfx)));
        final PoseStack.Pose a20 = dfj.last();
        final Matrix4f b21 = a20.pose();
        final Matrix3f a21 = a20.normal();
        this.vertex(b21, a21, dfn19, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, integer);
        this.vertex(b21, a21, dfn19, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, integer);
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            this.vertex(b21, a21, dfn19, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, integer);
            this.vertex(b21, a21, dfn19, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, integer);
            this.vertex(b21, a21, dfn19, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, integer);
            this.vertex(b21, a21, dfn19, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, integer);
        }
        dfj.popPose();
        super.render(bfx, float2, float3, dfj, dzy, integer);
    }
    
    public void vertex(final Matrix4f b, final Matrix3f a, final VertexConsumer dfn, final int integer4, final int integer5, final int integer6, final float float7, final float float8, final int integer9, final int integer10, final int integer11, final int integer12) {
        dfn.vertex(b, (float)integer4, (float)integer5, (float)integer6).color(255, 255, 255, 255).uv(float7, float8).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer12).normal(a, (float)integer9, (float)integer11, (float)integer10).endVertex();
    }
}
