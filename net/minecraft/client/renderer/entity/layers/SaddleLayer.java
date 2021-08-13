package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EntityModel;

public class SaddleLayer<T extends Entity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation textureLocation;
    private final M model;
    
    public SaddleLayer(final RenderLayerParent<T, M> egc, final M dtu, final ResourceLocation vk) {
        super(egc);
        this.model = dtu;
        this.textureLocation = vk;
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T apx, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (!((Saddleable)apx).isSaddled()) {
            return;
        }
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(apx, float5, float6, float7);
        this.model.setupAnim(apx, float5, float6, float8, float9, float10);
        final VertexConsumer dfn12 = dzy.getBuffer(RenderType.entityCutoutNoCull(this.textureLocation));
        this.model.renderToBuffer(dfj, dfn12, integer, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
    }
}
