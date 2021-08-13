package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Vector3f;
import net.minecraft.world.item.Items;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.WitchModel;
import net.minecraft.world.entity.LivingEntity;

public class WitchItemLayer<T extends LivingEntity> extends CrossedArmsItemLayer<T, WitchModel<T>> {
    public WitchItemLayer(final RenderLayerParent<T, WitchModel<T>> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final ItemStack bly12 = aqj.getMainHandItem();
        dfj.pushPose();
        if (bly12.getItem() == Items.POTION) {
            this.getParentModel().getHead().translateAndRotate(dfj);
            this.getParentModel().getNose().translateAndRotate(dfj);
            dfj.translate(0.0625, 0.25, 0.0);
            dfj.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
            dfj.mulPose(Vector3f.XP.rotationDegrees(140.0f));
            dfj.mulPose(Vector3f.ZP.rotationDegrees(10.0f));
            dfj.translate(0.0, -0.4000000059604645, 0.4000000059604645);
        }
        super.render(dfj, dzy, integer, aqj, float5, float6, float7, float8, float9, float10);
        dfj.popPose();
    }
}
