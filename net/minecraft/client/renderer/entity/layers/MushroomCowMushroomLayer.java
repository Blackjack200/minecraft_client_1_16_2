package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.CowModel;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowMushroomLayer<T extends MushroomCow> extends RenderLayer<T, CowModel<T>> {
    public MushroomCowMushroomLayer(final RenderLayerParent<T, CowModel<T>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T bag, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (bag.isBaby() || bag.isInvisible()) {
            return;
        }
        final BlockRenderDispatcher eap12 = Minecraft.getInstance().getBlockRenderer();
        final BlockState cee13 = bag.getMushroomType().getBlockState();
        final int integer2 = LivingEntityRenderer.getOverlayCoords(bag, 0.0f);
        dfj.pushPose();
        dfj.translate(0.20000000298023224, -0.3499999940395355, 0.5);
        dfj.mulPose(Vector3f.YP.rotationDegrees(-48.0f));
        dfj.scale(-1.0f, -1.0f, 1.0f);
        dfj.translate(-0.5, -0.5, -0.5);
        eap12.renderSingleBlock(cee13, dfj, dzy, integer, integer2);
        dfj.popPose();
        dfj.pushPose();
        dfj.translate(0.20000000298023224, -0.3499999940395355, 0.5);
        dfj.mulPose(Vector3f.YP.rotationDegrees(42.0f));
        dfj.translate(0.10000000149011612, 0.0, -0.6000000238418579);
        dfj.mulPose(Vector3f.YP.rotationDegrees(-48.0f));
        dfj.scale(-1.0f, -1.0f, 1.0f);
        dfj.translate(-0.5, -0.5, -0.5);
        eap12.renderSingleBlock(cee13, dfj, dzy, integer, integer2);
        dfj.popPose();
        dfj.pushPose();
        this.getParentModel().getHead().translateAndRotate(dfj);
        dfj.translate(0.0, -0.699999988079071, -0.20000000298023224);
        dfj.mulPose(Vector3f.YP.rotationDegrees(-78.0f));
        dfj.scale(-1.0f, -1.0f, 1.0f);
        dfj.translate(-0.5, -0.5, -0.5);
        eap12.renderSingleBlock(cee13, dfj, dzy, integer, integer2);
        dfj.popPose();
    }
}
