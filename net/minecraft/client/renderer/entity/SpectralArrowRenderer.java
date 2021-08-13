package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.SpectralArrow;

public class SpectralArrowRenderer extends ArrowRenderer<SpectralArrow> {
    public static final ResourceLocation SPECTRAL_ARROW_LOCATION;
    
    public SpectralArrowRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final SpectralArrow bgo) {
        return SpectralArrowRenderer.SPECTRAL_ARROW_LOCATION;
    }
    
    static {
        SPECTRAL_ARROW_LOCATION = new ResourceLocation("textures/entity/projectiles/spectral_arrow.png");
    }
}
