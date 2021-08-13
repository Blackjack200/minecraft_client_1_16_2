package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.DolphinCarryingItemLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.DolphinModel;
import net.minecraft.world.entity.animal.Dolphin;

public class DolphinRenderer extends MobRenderer<Dolphin, DolphinModel<Dolphin>> {
    private static final ResourceLocation DOLPHIN_LOCATION;
    
    public DolphinRenderer(final EntityRenderDispatcher eel) {
        super(eel, new DolphinModel(), 0.7f);
        this.addLayer(new DolphinCarryingItemLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Dolphin bac) {
        return DolphinRenderer.DOLPHIN_LOCATION;
    }
    
    static {
        DOLPHIN_LOCATION = new ResourceLocation("textures/entity/dolphin.png");
    }
}
