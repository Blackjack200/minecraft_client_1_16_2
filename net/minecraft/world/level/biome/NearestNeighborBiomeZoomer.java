package net.minecraft.world.level.biome;

public enum NearestNeighborBiomeZoomer implements BiomeZoomer {
    INSTANCE;
    
    public Biome getBiome(final long long1, final int integer2, final int integer3, final int integer4, final BiomeManager.NoiseBiomeSource a) {
        return a.getNoiseBiome(integer2 >> 2, integer3 >> 2, integer4 >> 2);
    }
}
