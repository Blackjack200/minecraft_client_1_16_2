package net.minecraft.client.resources.sounds;

import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.sounds.Weighted;

public class Sound implements Weighted<Sound> {
    private final ResourceLocation location;
    private final float volume;
    private final float pitch;
    private final int weight;
    private final Type type;
    private final boolean stream;
    private final boolean preload;
    private final int attenuationDistance;
    
    public Sound(final String string, final float float2, final float float3, final int integer4, final Type a, final boolean boolean6, final boolean boolean7, final int integer8) {
        this.location = new ResourceLocation(string);
        this.volume = float2;
        this.pitch = float3;
        this.weight = integer4;
        this.type = a;
        this.stream = boolean6;
        this.preload = boolean7;
        this.attenuationDistance = integer8;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    public ResourceLocation getPath() {
        return new ResourceLocation(this.location.getNamespace(), "sounds/" + this.location.getPath() + ".ogg");
    }
    
    public float getVolume() {
        return this.volume;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public Sound getSound() {
        return this;
    }
    
    public void preloadIfRequired(final SoundEngine enj) {
        if (this.preload) {
            enj.requestPreload(this);
        }
    }
    
    public Type getType() {
        return this.type;
    }
    
    public boolean shouldStream() {
        return this.stream;
    }
    
    public boolean shouldPreload() {
        return this.preload;
    }
    
    public int getAttenuationDistance() {
        return this.attenuationDistance;
    }
    
    public String toString() {
        return new StringBuilder().append("Sound[").append(this.location).append("]").toString();
    }
    
    public enum Type {
        FILE("file"), 
        SOUND_EVENT("event");
        
        private final String name;
        
        private Type(final String string3) {
            this.name = string3;
        }
        
        public static Type getByName(final String string) {
            for (final Type a5 : values()) {
                if (a5.name.equals(string)) {
                    return a5;
                }
            }
            return null;
        }
    }
}
