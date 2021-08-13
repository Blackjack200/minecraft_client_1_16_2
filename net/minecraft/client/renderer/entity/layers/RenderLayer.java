package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;

public abstract class RenderLayer<T extends Entity, M extends EntityModel<T>> {
    private final RenderLayerParent<T, M> renderer;
    
    public RenderLayer(final RenderLayerParent<T, M> egc) {
        this.renderer = egc;
    }
    
    protected static <T extends LivingEntity> void coloredCutoutModelCopyLayerRender(final EntityModel<T> dtu1, final EntityModel<T> dtu2, final ResourceLocation vk, final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float8, final float float9, final float float10, final float float11, final float float12, final float float13, final float float14, final float float15, final float float16) {
        if (!aqj.isInvisible()) {
            dtu1.copyPropertiesTo(dtu2);
            dtu2.prepareMobModel(aqj, float8, float9, float13);
            dtu2.setupAnim(aqj, float8, float9, float10, float11, float12);
            RenderLayer.<T>renderColoredCutoutModel(dtu2, vk, dfj, dzy, integer, aqj, float14, float15, float16);
        }
    }
    
    protected static <T extends LivingEntity> void renderColoredCutoutModel(final EntityModel<T> dtu, final ResourceLocation vk, final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float7, final float float8, final float float9) {
        final VertexConsumer dfn10 = dzy.getBuffer(RenderType.entityCutoutNoCull(vk));
        dtu.renderToBuffer(dfj, dfn10, integer, LivingEntityRenderer.getOverlayCoords(aqj, 0.0f), float7, float8, float9, 1.0f);
    }
    
    public M getParentModel() {
        return this.renderer.getModel();
    }
    
    protected ResourceLocation getTextureLocation(final T apx) {
        return this.renderer.getTextureLocation(apx);
    }
    
    public abstract void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T apx, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10);
}
