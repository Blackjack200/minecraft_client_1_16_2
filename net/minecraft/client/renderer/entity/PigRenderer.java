package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.PigModel;
import net.minecraft.world.entity.animal.Pig;

public class PigRenderer extends MobRenderer<Pig, PigModel<Pig>> {
    private static final ResourceLocation PIG_LOCATION;
    
    public PigRenderer(final EntityRenderDispatcher eel) {
        super(eel, new PigModel(), 0.7f);
        this.addLayer(new SaddleLayer<Pig, PigModel<Pig>>(this, new PigModel<Pig>(0.5f), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Pig bak) {
        return PigRenderer.PIG_LOCATION;
    }
    
    static {
        PIG_LOCATION = new ResourceLocation("textures/entity/pig/pig.png");
    }
}
