package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.world.entity.animal.IronGolem;

public class IronGolemFlowerLayer extends RenderLayer<IronGolem, IronGolemModel<IronGolem>> {
    public IronGolemFlowerLayer(final RenderLayerParent<IronGolem, IronGolemModel<IronGolem>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final IronGolem baf, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (baf.getOfferFlowerTick() == 0) {
            return;
        }
        dfj.pushPose();
        final ModelPart dwf12 = ((RenderLayer<T, IronGolemModel>)this).getParentModel().getFlowerHoldingArm();
        dwf12.translateAndRotate(dfj);
        dfj.translate(-1.1875, 1.0625, -0.9375);
        dfj.translate(0.5, 0.5, 0.5);
        final float float11 = 0.5f;
        dfj.scale(0.5f, 0.5f, 0.5f);
        dfj.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
        dfj.translate(-0.5, -0.5, -0.5);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Blocks.POPPY.defaultBlockState(), dfj, dzy, integer, OverlayTexture.NO_OVERLAY);
        dfj.popPose();
    }
}
