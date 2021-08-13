package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.FoxModel;
import net.minecraft.world.entity.animal.Fox;

public class FoxHeldItemLayer extends RenderLayer<Fox, FoxModel<Fox>> {
    public FoxHeldItemLayer(final RenderLayerParent<Fox, FoxModel<Fox>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final Fox bae, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final boolean boolean12 = bae.isSleeping();
        final boolean boolean13 = bae.isBaby();
        dfj.pushPose();
        if (boolean13) {
            final float float11 = 0.75f;
            dfj.scale(0.75f, 0.75f, 0.75f);
            dfj.translate(0.0, 0.5, 0.20937499403953552);
        }
        dfj.translate(((RenderLayer<T, FoxModel>)this).getParentModel().head.x / 16.0f, ((RenderLayer<T, FoxModel>)this).getParentModel().head.y / 16.0f, ((RenderLayer<T, FoxModel>)this).getParentModel().head.z / 16.0f);
        final float float11 = bae.getHeadRollAngle(float7);
        dfj.mulPose(Vector3f.ZP.rotation(float11));
        dfj.mulPose(Vector3f.YP.rotationDegrees(float9));
        dfj.mulPose(Vector3f.XP.rotationDegrees(float10));
        if (bae.isBaby()) {
            if (boolean12) {
                dfj.translate(0.4000000059604645, 0.25999999046325684, 0.15000000596046448);
            }
            else {
                dfj.translate(0.05999999865889549, 0.25999999046325684, -0.5);
            }
        }
        else if (boolean12) {
            dfj.translate(0.46000000834465027, 0.25999999046325684, 0.2199999988079071);
        }
        else {
            dfj.translate(0.05999999865889549, 0.27000001072883606, -0.5);
        }
        dfj.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        if (boolean12) {
            dfj.mulPose(Vector3f.ZP.rotationDegrees(90.0f));
        }
        final ItemStack bly15 = bae.getItemBySlot(EquipmentSlot.MAINHAND);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(bae, bly15, ItemTransforms.TransformType.GROUND, false, dfj, dzy, integer);
        dfj.popPose();
    }
}
