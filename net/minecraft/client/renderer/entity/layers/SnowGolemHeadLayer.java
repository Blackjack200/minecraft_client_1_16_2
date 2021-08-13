package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.world.entity.animal.SnowGolem;

public class SnowGolemHeadLayer extends RenderLayer<SnowGolem, SnowGolemModel<SnowGolem>> {
    public SnowGolemHeadLayer(final RenderLayerParent<SnowGolem, SnowGolemModel<SnowGolem>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final SnowGolem bar, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (bar.isInvisible() || !bar.hasPumpkin()) {
            return;
        }
        dfj.pushPose();
        ((RenderLayer<T, SnowGolemModel>)this).getParentModel().getHead().translateAndRotate(dfj);
        final float float11 = 0.625f;
        dfj.translate(0.0, -0.34375, 0.0);
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        dfj.scale(0.625f, -0.625f, -0.625f);
        final ItemStack bly13 = new ItemStack(Blocks.CARVED_PUMPKIN);
        Minecraft.getInstance().getItemRenderer().renderStatic(bar, bly13, ItemTransforms.TransformType.HEAD, false, dfj, dzy, bar.level, integer, LivingEntityRenderer.getOverlayCoords(bar, 0.0f));
        dfj.popPose();
    }
}
