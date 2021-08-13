package net.minecraft.client.renderer.entity.layers;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EntityModel;

public class StrayClothingLayer<T extends Mob, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation STRAY_CLOTHES_LOCATION;
    private final SkeletonModel<T> layerModel;
    
    public StrayClothingLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
        this.layerModel = new SkeletonModel<T>(0.25f, true);
    }
    
    @Override
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final int integer, final T aqk, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        RenderLayer.<T>coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, StrayClothingLayer.STRAY_CLOTHES_LOCATION, dfj, dzy, integer, aqk, float5, float6, float8, float9, float10, float7, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        STRAY_CLOTHES_LOCATION = new ResourceLocation("textures/entity/skeleton/stray_overlay.png");
    }
}
