package net.minecraft.client.resources.sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;

public class SimpleSoundInstance extends AbstractSoundInstance {
    public SimpleSoundInstance(final SoundEvent adn, final SoundSource adp, final float float3, final float float4, final BlockPos fx) {
        this(adn, adp, float3, float4, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5);
    }
    
    public static SimpleSoundInstance forUI(final SoundEvent adn, final float float2) {
        return forUI(adn, float2, 0.25f);
    }
    
    public static SimpleSoundInstance forUI(final SoundEvent adn, final float float2, final float float3) {
        return new SimpleSoundInstance(adn.getLocation(), SoundSource.MASTER, float3, float2, false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
    }
    
    public static SimpleSoundInstance forMusic(final SoundEvent adn) {
        return new SimpleSoundInstance(adn.getLocation(), SoundSource.MUSIC, 1.0f, 1.0f, false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
    }
    
    public static SimpleSoundInstance forRecord(final SoundEvent adn, final double double2, final double double3, final double double4) {
        return new SimpleSoundInstance(adn, SoundSource.RECORDS, 4.0f, 1.0f, false, 0, SoundInstance.Attenuation.LINEAR, double2, double3, double4);
    }
    
    public static SimpleSoundInstance forLocalAmbience(final SoundEvent adn, final float float2, final float float3) {
        return new SimpleSoundInstance(adn.getLocation(), SoundSource.AMBIENT, float3, float2, false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
    }
    
    public static SimpleSoundInstance forAmbientAddition(final SoundEvent adn) {
        return forLocalAmbience(adn, 1.0f, 1.0f);
    }
    
    public static SimpleSoundInstance forAmbientMood(final SoundEvent adn, final double double2, final double double3, final double double4) {
        return new SimpleSoundInstance(adn, SoundSource.AMBIENT, 1.0f, 1.0f, false, 0, SoundInstance.Attenuation.LINEAR, double2, double3, double4);
    }
    
    public SimpleSoundInstance(final SoundEvent adn, final SoundSource adp, final float float3, final float float4, final double double5, final double double6, final double double7) {
        this(adn, adp, float3, float4, false, 0, SoundInstance.Attenuation.LINEAR, double5, double6, double7);
    }
    
    private SimpleSoundInstance(final SoundEvent adn, final SoundSource adp, final float float3, final float float4, final boolean boolean5, final int integer, final SoundInstance.Attenuation a, final double double8, final double double9, final double double10) {
        this(adn.getLocation(), adp, float3, float4, boolean5, integer, a, double8, double9, double10, false);
    }
    
    public SimpleSoundInstance(final ResourceLocation vk, final SoundSource adp, final float float3, final float float4, final boolean boolean5, final int integer, final SoundInstance.Attenuation a, final double double8, final double double9, final double double10, final boolean boolean11) {
        super(vk, adp);
        this.volume = float3;
        this.pitch = float4;
        this.x = double8;
        this.y = double9;
        this.z = double10;
        this.looping = boolean5;
        this.delay = integer;
        this.attenuation = a;
        this.relative = boolean11;
    }
}
