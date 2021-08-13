package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;

public abstract class EyesLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    public EyesLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T apx, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        final VertexConsumer dfn12 = dzy.getBuffer(this.renderType());
        this.getParentModel().renderToBuffer(dfj, dfn12, 15728640, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public abstract RenderType renderType();
}
