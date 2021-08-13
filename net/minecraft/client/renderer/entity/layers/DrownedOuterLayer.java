package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.DrownedModel;
import net.minecraft.world.entity.monster.Drowned;

public class DrownedOuterLayer<T extends Drowned> extends RenderLayer<T, DrownedModel<T>> {
    private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION;
    private final DrownedModel<T> model;
    
    public DrownedOuterLayer(final RenderLayerParent<T, DrownedModel<T>> egc) {
        super(egc);
        this.model = new DrownedModel<T>(0.25f, 0.0f, 64, 64);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T bdb, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        RenderLayer.<T>coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, DrownedOuterLayer.DROWNED_OUTER_LAYER_LOCATION, dfj, dzy, integer, bdb, float5, float6, float8, float9, float10, float7, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        DROWNED_OUTER_LAYER_LOCATION = new ResourceLocation("textures/entity/zombie/drowned_outer_layer.png");
    }
}
