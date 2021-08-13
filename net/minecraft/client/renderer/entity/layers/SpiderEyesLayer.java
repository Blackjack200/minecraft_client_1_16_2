package net.minecraft.client.renderer.entity.layers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.world.entity.Entity;

public class SpiderEyesLayer<T extends Entity, M extends SpiderModel<T>> extends EyesLayer<T, M> {
    private static final RenderType SPIDER_EYES;
    
    public SpiderEyesLayer(final RenderLayerParent<T, M> egc) {
        super(egc);
    }
    
    @Override
    public RenderType renderType() {
        return SpiderEyesLayer.SPIDER_EYES;
    }
    
    static {
        SPIDER_EYES = RenderType.eyes(new ResourceLocation("textures/entity/spider_eyes.png"));
    }
}
