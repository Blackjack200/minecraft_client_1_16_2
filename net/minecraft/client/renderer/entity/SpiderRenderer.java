package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.world.entity.monster.Spider;

public class SpiderRenderer<T extends Spider> extends MobRenderer<T, SpiderModel<T>> {
    private static final ResourceLocation SPIDER_LOCATION;
    
    public SpiderRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SpiderModel(), 0.8f);
        this.addLayer(new SpiderEyesLayer<T, SpiderModel<T>>(this));
    }
    
    @Override
    protected float getFlipDegrees(final T bdy) {
        return 180.0f;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final T bdy) {
        return SpiderRenderer.SPIDER_LOCATION;
    }
    
    static {
        SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/spider.png");
    }
}
