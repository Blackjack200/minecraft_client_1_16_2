package net.minecraft.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.world.entity.animal.Rabbit;

public class RabbitRenderer extends MobRenderer<Rabbit, RabbitModel<Rabbit>> {
    private static final ResourceLocation RABBIT_BROWN_LOCATION;
    private static final ResourceLocation RABBIT_WHITE_LOCATION;
    private static final ResourceLocation RABBIT_BLACK_LOCATION;
    private static final ResourceLocation RABBIT_GOLD_LOCATION;
    private static final ResourceLocation RABBIT_SALT_LOCATION;
    private static final ResourceLocation RABBIT_WHITE_SPLOTCHED_LOCATION;
    private static final ResourceLocation RABBIT_TOAST_LOCATION;
    private static final ResourceLocation RABBIT_EVIL_LOCATION;
    
    public RabbitRenderer(final EntityRenderDispatcher eel) {
        super(eel, new RabbitModel(), 0.3f);
    }
    
    @Override
    public ResourceLocation getTextureLocation(final Rabbit ban) {
        final String string3 = ChatFormatting.stripFormatting(ban.getName().getString());
        if (string3 != null && "Toast".equals(string3)) {
            return RabbitRenderer.RABBIT_TOAST_LOCATION;
        }
        switch (ban.getRabbitType()) {
            default: {
                return RabbitRenderer.RABBIT_BROWN_LOCATION;
            }
            case 1: {
                return RabbitRenderer.RABBIT_WHITE_LOCATION;
            }
            case 2: {
                return RabbitRenderer.RABBIT_BLACK_LOCATION;
            }
            case 4: {
                return RabbitRenderer.RABBIT_GOLD_LOCATION;
            }
            case 5: {
                return RabbitRenderer.RABBIT_SALT_LOCATION;
            }
            case 3: {
                return RabbitRenderer.RABBIT_WHITE_SPLOTCHED_LOCATION;
            }
            case 99: {
                return RabbitRenderer.RABBIT_EVIL_LOCATION;
            }
        }
    }
    
    static {
        RABBIT_BROWN_LOCATION = new ResourceLocation("textures/entity/rabbit/brown.png");
        RABBIT_WHITE_LOCATION = new ResourceLocation("textures/entity/rabbit/white.png");
        RABBIT_BLACK_LOCATION = new ResourceLocation("textures/entity/rabbit/black.png");
        RABBIT_GOLD_LOCATION = new ResourceLocation("textures/entity/rabbit/gold.png");
        RABBIT_SALT_LOCATION = new ResourceLocation("textures/entity/rabbit/salt.png");
        RABBIT_WHITE_SPLOTCHED_LOCATION = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
        RABBIT_TOAST_LOCATION = new ResourceLocation("textures/entity/rabbit/toast.png");
        RABBIT_EVIL_LOCATION = new ResourceLocation("textures/entity/rabbit/caerbannog.png");
    }
}
