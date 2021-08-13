package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.DolphinModel;
import net.minecraft.world.entity.animal.Dolphin;

public class DolphinCarryingItemLayer extends RenderLayer<Dolphin, DolphinModel<Dolphin>> {
    public DolphinCarryingItemLayer(final RenderLayerParent<Dolphin, DolphinModel<Dolphin>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Dolphin bac, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final boolean boolean12 = bac.getMainArm() == HumanoidArm.RIGHT;
        dfj.pushPose();
        final float float11 = 1.0f;
        final float float12 = -1.0f;
        final float float13 = Mth.abs(bac.xRot) / 60.0f;
        if (bac.xRot < 0.0f) {
            dfj.translate(0.0, 1.0f - float13 * 0.5f, -1.0f + float13 * 0.5f);
        }
        else {
            dfj.translate(0.0, 1.0f + float13 * 0.8f, -1.0f + float13 * 0.2f);
        }
        final ItemStack bly16 = boolean12 ? bac.getMainHandItem() : bac.getOffhandItem();
        Minecraft.getInstance().getItemInHandRenderer().renderItem(bac, bly16, ItemTransforms.TransformType.GROUND, false, dfj, dzy, integer);
        dfj.popPose();
    }
}
