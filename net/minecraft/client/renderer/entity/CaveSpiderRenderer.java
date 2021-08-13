package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Spider;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.CaveSpider;

public class CaveSpiderRenderer extends SpiderRenderer<CaveSpider> {
    private static final ResourceLocation CAVE_SPIDER_LOCATION;
    
    public CaveSpiderRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.shadowRadius *= 0.7f;
    }
    
    protected void scale(final CaveSpider bcy, final PoseStack dfj, final float float3) {
        dfj.scale(0.7f, 0.7f, 0.7f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final CaveSpider bcy) {
        return CaveSpiderRenderer.CAVE_SPIDER_LOCATION;
    }
    
    static {
        CAVE_SPIDER_LOCATION = new ResourceLocation("textures/entity/spider/cave_spider.png");
    }
}
