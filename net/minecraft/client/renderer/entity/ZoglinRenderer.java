package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.HoglinModel;
import net.minecraft.world.entity.monster.Zoglin;

public class ZoglinRenderer extends MobRenderer<Zoglin, HoglinModel<Zoglin>> {
    private static final ResourceLocation ZOGLIN_LOCATION;
    
    public ZoglinRenderer(final EntityRenderDispatcher eel) {
        super(eel, new HoglinModel(), 0.7f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Zoglin bef) {
        return ZoglinRenderer.ZOGLIN_LOCATION;
    }
    
    static {
        ZOGLIN_LOCATION = new ResourceLocation("textures/entity/hoglin/zoglin.png");
    }
}
