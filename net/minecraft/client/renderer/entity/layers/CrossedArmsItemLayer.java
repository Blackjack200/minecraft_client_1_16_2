package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.LivingEntity;

public class CrossedArmsItemLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public CrossedArmsItemLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        dfj.pushPose();
        dfj.translate(0.0, 0.4000000059604645, -0.4000000059604645);
        dfj.mulPose(Vector3f.XP.rotationDegrees(180.0f));
        final ItemStack bly12 = aqj.getItemBySlot(EquipmentSlot.MAINHAND);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(aqj, bly12, ItemTransforms.TransformType.GROUND, false, dfj, dzy, integer);
        dfj.popPose();
    }
}
