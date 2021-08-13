package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.world.entity.animal.Ocelot;

public class OcelotRenderer extends MobRenderer<Ocelot, OcelotModel<Ocelot>> {
    private static final ResourceLocation CAT_OCELOT_LOCATION;
    
    public OcelotRenderer(final EntityRenderDispatcher eel) {
        super(eel, new OcelotModel(0.0f), 0.4f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Ocelot bah) {
        return OcelotRenderer.CAT_OCELOT_LOCATION;
    }
    
    static {
        CAT_OCELOT_LOCATION = new ResourceLocation("textures/entity/cat/ocelot.png");
    }
}
