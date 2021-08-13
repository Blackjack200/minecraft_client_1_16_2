package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;

public class ItemInHandLayer<T extends LivingEntity, M extends EntityModel> extends RenderLayer<T, M> {
    public ItemInHandLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final boolean boolean12 = aqj.getMainArm() == HumanoidArm.RIGHT;
        final ItemStack bly13 = boolean12 ? aqj.getOffhandItem() : aqj.getMainHandItem();
        final ItemStack bly14 = boolean12 ? aqj.getMainHandItem() : aqj.getOffhandItem();
        if (bly13.isEmpty() && bly14.isEmpty()) {
            return;
        }
        dfj.pushPose();
        if (((net.minecraft.client.model.EntityModel)this.getParentModel()).young) {
            final float float11 = 0.5f;
            dfj.translate(0.0, 0.75, 0.0);
            dfj.scale(0.5f, 0.5f, 0.5f);
        }
        this.renderArmWithItem(aqj, bly14, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, dfj, dzy, integer);
        this.renderArmWithItem(aqj, bly13, ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, dfj, dzy, integer);
        dfj.popPose();
    }
    
    private void renderArmWithItem(final LivingEntity aqj, final ItemStack bly, final ItemTransforms.TransformType b, final HumanoidArm aqf, final PoseStack dfj, final MultiBufferSource dzy, final int integer) {
        if (bly.isEmpty()) {
            return;
        }
        dfj.pushPose();
        this.getParentModel().translateToHand(aqf, dfj);
        dfj.mulPose(Vector3f.XP.rotationDegrees(-90.0f));
        dfj.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        final boolean boolean9 = aqf == HumanoidArm.LEFT;
        dfj.translate((boolean9 ? -1 : 1) / 16.0f, 0.125, -0.625);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(aqj, bly, b, boolean9, dfj, dzy, integer);
        dfj.popPose();
    }
}
