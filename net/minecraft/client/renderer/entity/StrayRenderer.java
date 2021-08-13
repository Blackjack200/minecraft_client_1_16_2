package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.client.renderer.entity.layers.StrayClothingLayer;
import net.minecraft.resources.ResourceLocation;

public class StrayRenderer extends SkeletonRenderer {
    private static final ResourceLocation STRAY_SKELETON_LOCATION;
    
    public StrayRenderer(final EntityRenderDispatcher eel) {
        super(eel);
        this.addLayer(new StrayClothingLayer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>>(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final AbstractSkeleton bcw) {
        return StrayRenderer.STRAY_SKELETON_LOCATION;
    }
    
    static {
        STRAY_SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/stray.png");
    }
}
