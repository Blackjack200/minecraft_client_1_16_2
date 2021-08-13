package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.texture.OverlayTexture;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;

public class BeeStingerLayer<T extends LivingEntity, M extends PlayerModel<T>> extends StuckInBodyLayer<T, M> {
    private static final ResourceLocation BEE_STINGER_LOCATION;
    
    public BeeStingerLayer(final LivingEntityRenderer<T, M> efj) {
        super(efj);
    }
    
    @Override
    protected int numStuck(final T aqj) {
        return aqj.getStingerCount();
    }
    
    @Override
    protected void renderStuckItem(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Entity apx, final float float5, final float float6, final float float7, final float float8) {
        final float float9 = Mth.sqrt(float5 * float5 + float7 * float7);
        final float float10 = (float)(Math.atan2((double)float5, (double)float7) * 57.2957763671875);
        final float float11 = (float)(Math.atan2((double)float6, (double)float9) * 57.2957763671875);
        dfj.translate(0.0, 0.0, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(float10 - 90.0f));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(float11));
        final float float12 = 0.0f;
        final float float13 = 0.125f;
        final float float14 = 0.0f;
        final float float15 = 0.0625f;
        final float float16 = 0.03125f;
        dfj.mulPose(Vector3f.XP.rotationDegrees(45.0f));
        dfj.scale(0.03125f, 0.03125f, 0.03125f);
        dfj.translate(2.5, 0.0, 0.0);
        final VertexConsumer dfn18 = dzy.getBuffer(RenderType.entityCutoutNoCull(BeeStingerLayer.BEE_STINGER_LOCATION));
        for (int integer2 = 0; integer2 < 4; ++integer2) {
            dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            final PoseStack.Pose a20 = dfj.last();
            final Matrix4f b21 = a20.pose();
            final Matrix3f a21 = a20.normal();
            vertex(dfn18, b21, a21, -4.5f, -1, 0.0f, 0.0f, integer);
            vertex(dfn18, b21, a21, 4.5f, -1, 0.125f, 0.0f, integer);
            vertex(dfn18, b21, a21, 4.5f, 1, 0.125f, 0.0625f, integer);
            vertex(dfn18, b21, a21, -4.5f, 1, 0.0f, 0.0625f, integer);
        }
    }
    
    private static void vertex(final VertexConsumer dfn, final Matrix4f b, final Matrix3f a, final float float4, final int integer5, final float float6, final float float7, final int integer8) {
        dfn.vertex(b, float4, (float)integer5, 0.0f).color(255, 255, 255, 255).uv(float6, float7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(integer8).normal(a, 0.0f, 1.0f, 0.0f).endVertex();
    }
    
    static {
        BEE_STINGER_LOCATION = new ResourceLocation("textures/entity/bee/bee_stinger.png");
    }
}
