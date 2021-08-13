package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepFurLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SheepModel;
import net.minecraft.world.entity.animal.Sheep;

public class SheepRenderer extends MobRenderer<Sheep, SheepModel<Sheep>> {
    private static final ResourceLocation SHEEP_LOCATION;
    
    public SheepRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SheepModel(), 0.7f);
        this.addLayer(new SheepFurLayer(this));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Sheep bap) {
        return SheepRenderer.SHEEP_LOCATION;
    }
    
    static {
        SHEEP_LOCATION = new ResourceLocation("textures/entity/sheep/sheep.png");
    }
}
