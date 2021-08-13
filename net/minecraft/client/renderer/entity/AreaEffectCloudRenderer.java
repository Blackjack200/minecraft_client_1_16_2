package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AreaEffectCloud;

public class AreaEffectCloudRenderer extends EntityRenderer<AreaEffectCloud> {
    public AreaEffectCloudRenderer(final EntityRenderDispatcher eel) {
        super(eel);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final AreaEffectCloud apw) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
