package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.PandaModel;
import net.minecraft.world.entity.animal.Panda;

public class PandaHoldsItemLayer extends RenderLayer<Panda, PandaModel<Panda>> {
    public PandaHoldsItemLayer(final RenderLayerParent<Panda, PandaModel<Panda>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Panda bai, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final ItemStack bly12 = bai.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!bai.isSitting() || bai.isScared()) {
            return;
        }
        float float11 = -0.6f;
        float float12 = 1.4f;
        if (bai.isEating()) {
            float11 -= 0.2f * Mth.sin(float8 * 0.6f) + 0.2f;
            float12 -= 0.09f * Mth.sin(float8 * 0.6f);
        }
        dfj.pushPose();
        dfj.translate(0.10000000149011612, float12, float11);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(bai, bly12, ItemTransforms.TransformType.GROUND, false, dfj, dzy, integer);
        dfj.popPose();
    }
}
