package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.HoglinModel;
import net.minecraft.world.entity.monster.hoglin.Hoglin;

public class HoglinRenderer extends MobRenderer<Hoglin, HoglinModel<Hoglin>> {
    private static final ResourceLocation HOGLIN_LOCATION;
    
    public HoglinRenderer(final EntityRenderDispatcher eel) {
        super(eel, new HoglinModel(), 0.7f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Hoglin bej) {
        return HoglinRenderer.HOGLIN_LOCATION;
    }
    
    @Override
    protected boolean isShaking(final Hoglin bej) {
        return bej.isConverting();
    }
    
    static {
        HOGLIN_LOCATION = new ResourceLocation("textures/entity/hoglin/hoglin.png");
    }
}
