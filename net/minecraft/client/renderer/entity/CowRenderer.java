package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.CowModel;
import net.minecraft.world.entity.animal.Cow;

public class CowRenderer extends MobRenderer<Cow, CowModel<Cow>> {
    private static final ResourceLocation COW_LOCATION;
    
    public CowRenderer(final EntityRenderDispatcher eel) {
        super(eel, new CowModel(), 0.7f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Cow bab) {
        return CowRenderer.COW_LOCATION;
    }
    
    static {
        COW_LOCATION = new ResourceLocation("textures/entity/cow/cow.png");
    }
}
