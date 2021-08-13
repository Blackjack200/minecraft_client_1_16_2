package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.world.entity.LivingEntity;

public class SlimeOuterLayer<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {
    private final EntityModel<T> model;
    
    public SlimeOuterLayer(final RenderLayerParent<T, SlimeModel<T>> egc) {
        super(egc);
        this.model = new SlimeModel<T>(0);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqj, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        if (aqj.isInvisible()) {
            return;
        }
        this.getParentModel().copyPropertiesTo(this.model);
        this.model.prepareMobModel(aqj, float5, float6, float7);
        this.model.setupAnim(aqj, float5, float6, float8, float9, float10);
        final VertexConsumer dfn12 = dzy.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(aqj)));
        this.model.renderToBuffer(dfj, dfn12, integer, LivingEntityRenderer.getOverlayCoords(aqj, 0.0f), 1.0f, 1.0f, 1.0f, 1.0f);
    }
}
