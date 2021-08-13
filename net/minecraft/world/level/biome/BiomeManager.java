package net.minecraft.world.level.biome;

import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import com.google.common.hash.Hashing;

public class BiomeManager {
    private final NoiseBiomeSource noiseBiomeSource;
    private final long biomeZoomSeed;
    private final BiomeZoomer zoomer;
    
    public BiomeManager(final NoiseBiomeSource a, final long long2, final BiomeZoomer bsx) {
        this.noiseBiomeSource = a;
        this.biomeZoomSeed = long2;
        this.zoomer = bsx;
    }
    
    public static long obfuscateSeed(final long long1) {
        return Hashing.sha256().hashLong(long1).asLong();
    }
    
    public BiomeManager withDifferentSource(final BiomeSource bsv) {
        return new BiomeManager(bsv, this.biomeZoomSeed, this.zoomer);
    }
    
    public Biome getBiome(final BlockPos fx) {
        return this.zoomer.getBiome(this.biomeZoomSeed, fx.getX(), fx.getY(), fx.getZ(), this.noiseBiomeSource);
    }
    
    public Biome getNoiseBiomeAtPosition(final double double1, final double double2, final double double3) {
        final int integer8 = Mth.floor(double1) >> 2;
        final int integer9 = Mth.floor(double2) >> 2;
        final int integer10 = Mth.floor(double3) >> 2;
        return this.getNoiseBiomeAtQuart(integer8, integer9, integer10);
    }
    
    public Biome getNoiseBiomeAtPosition(final BlockPos fx) {
        final int integer3 = fx.getX() >> 2;
        final int integer4 = fx.getY() >> 2;
        final int integer5 = fx.getZ() >> 2;
        return this.getNoiseBiomeAtQuart(integer3, integer4, integer5);
    }
    
    public Biome getNoiseBiomeAtQuart(final int integer1, final int integer2, final int integer3) {
        return this.noiseBiomeSource.getNoiseBiome(integer1, integer2, integer3);
    }
    
    public interface NoiseBiomeSource {
        Biome getNoiseBiome(final int integer1, final int integer2, final int integer3);
    }
}
