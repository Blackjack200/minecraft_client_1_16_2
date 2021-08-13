package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.EndermiteModel;
import net.minecraft.world.entity.monster.Endermite;

public class EndermiteRenderer extends MobRenderer<Endermite, EndermiteModel<Endermite>> {
    private static final ResourceLocation ENDERMITE_LOCATION;
    
    public EndermiteRenderer(final EntityRenderDispatcher eel) {
        super(eel, new EndermiteModel(), 0.3f);
    }
    
    @Override
    protected float getFlipDegrees(final Endermite bde) {
        return 180.0f;
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Endermite bde) {
        return EndermiteRenderer.ENDERMITE_LOCATION;
    }
    
    static {
        ENDERMITE_LOCATION = new ResourceLocation("textures/entity/endermite.png");
    }
}
