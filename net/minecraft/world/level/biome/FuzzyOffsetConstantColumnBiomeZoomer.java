package net.minecraft.world.level.biome;

public enum FuzzyOffsetConstantColumnBiomeZoomer implements BiomeZoomer {
    INSTANCE;
    
    public Biome getBiome(final long long1, final int integer2, final int integer3, final int integer4, final BiomeManager.NoiseBiomeSource a) {
        return FuzzyOffsetBiomeZoomer.INSTANCE.getBiome(long1, integer2, 0, integer4, a);
    }
}
