package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.world.entity.monster.EnderMan;

public class CarriedBlockLayer extends RenderLayer<EnderMan, EndermanModel<EnderMan>> {
    public CarriedBlockLayer(final RenderLayerParent<EnderMan, EndermanModel<EnderMan>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final EnderMan bdd, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final BlockState cee12 = bdd.getCarriedBlock();
        if (cee12 == null) {
            return;
        }
        dfj.pushPose();
        dfj.translate(0.0, 0.6875, -0.75);
        dfj.mulPose(Vector3f.XP.rotationDegrees(20.0f));
        dfj.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        dfj.translate(0.25, 0.1875, 0.25);
        final float float11 = 0.5f;
        dfj.scale(-0.5f, -0.5f, 0.5f);
        dfj.mulPose(Vector3f.YP.rotationDegrees(90.0f));
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(cee12, dfj, dzy, integer, OverlayTexture.NO_OVERLAY);
        dfj.popPose();
    }
}
