package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import com.mojang.math.Vector3f;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;

public class CapeLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public CapeLayer(final RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final AbstractClientPlayer dzb, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!dzb.isCapeLoaded() || dzb.isInvisible() || !dzb.isModelPartShown(PlayerModelPart.CAPE) || dzb.getCloakTextureLocation() == null) {
            return;
        }
        final ItemStack bly12 = dzb.getItemBySlot(EquipmentSlot.CHEST);
        if (bly12.getItem() == Items.ELYTRA) {
            return;
        }
        dfj.pushPose();
        dfj.translate(0.0, 0.0, 0.125);
        final double double13 = Mth.lerp(float7, dzb.xCloakO, dzb.xCloak) - Mth.lerp(float7, dzb.xo, dzb.getX());
        final double double14 = Mth.lerp(float7, dzb.yCloakO, dzb.yCloak) - Mth.lerp(float7, dzb.yo, dzb.getY());
        final double double15 = Mth.lerp(float7, dzb.zCloakO, dzb.zCloak) - Mth.lerp(float7, dzb.zo, dzb.getZ());
        final float float11 = dzb.yBodyRotO + (dzb.yBodyRot - dzb.yBodyRotO);
        final double double16 = Mth.sin(float11 * 0.017453292f);
        final double double17 = -Mth.cos(float11 * 0.017453292f);
        float float12 = (float)double14 * 10.0f;
        float12 = Mth.clamp(float12, -6.0f, 32.0f);
        float float13 = (float)(double13 * double16 + double15 * double17) * 100.0f;
        float13 = Mth.clamp(float13, 0.0f, 150.0f);
        float float14 = (float)(double13 * double17 - double15 * double16) * 100.0f;
        float14 = Mth.clamp(float14, -20.0f, 20.0f);
        if (float13 < 0.0f) {
            float13 = 0.0f;
        }
        final float float15 = Mth.lerp(float7, dzb.oBob, dzb.bob);
        float12 += Mth.sin(Mth.lerp(float7, dzb.walkDistO, dzb.walkDist) * 6.0f) * 32.0f * float15;
        if (dzb.isCrouching()) {
            float12 += 25.0f;
        }
        dfj.mulPose(Vector3f.XP.rotationDegrees(6.0f + float13 / 2.0f + float12));
        dfj.mulPose(Vector3f.ZP.rotationDegrees(float14 / 2.0f));
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f - float14 / 2.0f));
        final VertexConsumer dfn28 = dzy.getBuffer(RenderType.entitySolid(dzb.getCloakTextureLocation()));
        ((RenderLayer<T, PlayerModel>)this).getParentModel().renderCloak(dfj, dfn28, integer, OverlayTexture.NO_OVERLAY);
        dfj.popPose();
    }
}
