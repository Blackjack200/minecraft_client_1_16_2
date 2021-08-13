package net.minecraft.client.renderer.entity.layers;

import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.EntityModel;

public abstract class EnergySwirlLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public EnergySwirlLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T apx, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!((PowerableMob)apx).isPowered()) {
            return;
        }
        final float float11 = ((Entity)apx).tickCount + float7;
        final EntityModel<T> dtu13 = this.model();
        dtu13.prepareMobModel(apx, float5, float6, float7);
        this.getParentModel().copyPropertiesTo(dtu13);
        final VertexConsumer dfn14 = dzy.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(float11), float11 * 0.01f));
        dtu13.setupAnim(apx, float5, float6, float8, float9, float10);
        dtu13.renderToBuffer(dfj, dfn14, integer, OverlayTexture.NO_OVERLAY, 0.5f, 0.5f, 0.5f, 1.0f);
    }
    
    protected abstract float xOffset(final float float1);
    
    protected abstract ResourceLocation getTextureLocation();
    
    protected abstract EntityModel<T> model();
}
