package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.SilverfishModel;
import net.minecraft.world.entity.monster.Silverfish;

public class SilverfishRenderer extends MobRenderer<Silverfish, SilverfishModel<Silverfish>> {
    private static final ResourceLocation SILVERFISH_LOCATION;
    
    public SilverfishRenderer(final EntityRenderDispatcher eel) {
        super(eel, new SilverfishModel(), 0.3f);
    }
    
    @Override
    protected float getFlipDegrees(final Silverfish bdu) {
        return 180.0f;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Silverfish bdu) {
        return SilverfishRenderer.SILVERFISH_LOCATION;
    }
    
    static {
        SILVERFISH_LOCATION = new ResourceLocation("textures/entity/silverfish.png");
    }
}
