package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Arrow;

public class TippableArrowRenderer extends ArrowRenderer<Arrow> {
    public static final ResourceLocation NORMAL_ARROW_LOCATION;
    public static final ResourceLocation TIPPED_ARROW_LOCATION;
    
    public TippableArrowRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Arrow bfz) {
        return (bfz.getColor() > 0) ? TippableArrowRenderer.TIPPED_ARROW_LOCATION : TippableArrowRenderer.NORMAL_ARROW_LOCATION;
    }
    
    static {
        NORMAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/arrow.png");
        TIPPED_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/tipped_arrow.png");
    }
}
