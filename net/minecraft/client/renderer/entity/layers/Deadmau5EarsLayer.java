package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public class Deadmau5EarsLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public Deadmau5EarsLayer(final RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final AbstractClientPlayer dzb, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!"deadmau5".equals(dzb.getName().getString()) || !dzb.isSkinLoaded() || dzb.isInvisible()) {
            return;
        }
        final VertexConsumer dfn12 = dzy.getBuffer(RenderType.entitySolid(dzb.getSkinTextureLocation()));
        final int integer2 = LivingEntityRenderer.getOverlayCoords(dzb, 0.0f);
        for (int integer3 = 0; integer3 < 2; ++integer3) {
            final float float11 = Mth.lerp(float7, dzb.yRotO, dzb.yRot) - Mth.lerp(float7, dzb.yBodyRotO, dzb.yBodyRot);
            final float float12 = Mth.lerp(float7, dzb.xRotO, dzb.xRot);
            dfj.pushPose();
            dfj.mulPose(Vector3f.YP.rotationDegrees(float11));
            dfj.mulPose(Vector3f.XP.rotationDegrees(float12));
            dfj.translate(0.375f * (integer3 * 2 - 1), 0.0, 0.0);
            dfj.translate(0.0, -0.375, 0.0);
            dfj.mulPose(Vector3f.XP.rotationDegrees(-float12));
            dfj.mulPose(Vector3f.YP.rotationDegrees(-float11));
            final float float13 = 1.3333334f;
            dfj.scale(1.3333334f, 1.3333334f, 1.3333334f);
            ((RenderLayer<T, PlayerModel>)this).getParentModel().renderEars(dfj, dfn12, integer, integer2);
            dfj.popPose();
        }
    }
}
