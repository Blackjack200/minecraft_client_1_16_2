package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.BeeModel;
import net.minecraft.world.entity.animal.Bee;

public class BeeRenderer extends MobRenderer<Bee, BeeModel<Bee>> {
    private static final ResourceLocation ANGRY_BEE_TEXTURE;
    private static final ResourceLocation ANGRY_NECTAR_BEE_TEXTURE;
    private static final ResourceLocation BEE_TEXTURE;
    private static final ResourceLocation NECTAR_BEE_TEXTURE;
    
    public BeeRenderer(final EntityRenderDispatcher eel) {
        super(eel, new BeeModel(), 0.4f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Bee azx) {
        if (azx.isAngry()) {
            if (azx.hasNectar()) {
                return BeeRenderer.ANGRY_NECTAR_BEE_TEXTURE;
            }
            return BeeRenderer.ANGRY_BEE_TEXTURE;
        }
        else {
            if (azx.hasNectar()) {
                return BeeRenderer.NECTAR_BEE_TEXTURE;
            }
            return BeeRenderer.BEE_TEXTURE;
        }
    }
    
    static {
        ANGRY_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_angry.png");
        ANGRY_NECTAR_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_angry_nectar.png");
        BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee.png");
        NECTAR_BEE_TEXTURE = new ResourceLocation("textures/entity/bee/bee_nectar.png");
    }
}
