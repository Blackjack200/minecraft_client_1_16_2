package net.minecraft.client.resources;

import java.util.UUID;
import net.minecraft.resources.ResourceLocation;

public class DefaultPlayerSkin {
    private static final ResourceLocation STEVE_SKIN_LOCATION;
    private static final ResourceLocation ALEX_SKIN_LOCATION;
    
    public static ResourceLocation getDefaultSkin() {
        return DefaultPlayerSkin.STEVE_SKIN_LOCATION;
    }
    
    public static ResourceLocation getDefaultSkin(final UUID uUID) {
        if (isAlexDefault(uUID)) {
            return DefaultPlayerSkin.ALEX_SKIN_LOCATION;
        }
        return DefaultPlayerSkin.STEVE_SKIN_LOCATION;
    }
    
    public static String getSkinModelName(final UUID uUID) {
        if (isAlexDefault(uUID)) {
            return "slim";
        }
        return "default";
    }
    
    private static boolean isAlexDefault(final UUID uUID) {
        return (uUID.hashCode() & 0x1) == 0x1;
    }
    
    static {
        STEVE_SKIN_LOCATION = new ResourceLocation("textures/entity/steve.png");
        ALEX_SKIN_LOCATION = new ResourceLocation("textures/entity/alex.png");
    }
}
