package net.minecraft.sounds;

import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;

public class SoundEvent {
    public static final Codec<SoundEvent> CODEC;
    private final ResourceLocation location;
    
    public SoundEvent(final ResourceLocation vk) {
        this.location = vk;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    static {
        CODEC = ResourceLocation.CODEC.xmap(SoundEvent::new, adn -> adn.location);
    }
}
