package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;

public class BiomeInitLayer implements C0Transformer {
    private static final int[] LEGACY_WARM_BIOMES;
    private static final int[] WARM_BIOMES;
    private static final int[] MEDIUM_BIOMES;
    private static final int[] COLD_BIOMES;
    private static final int[] ICE_BIOMES;
    private int[] warmBiomes;
    
    public BiomeInitLayer(final boolean boolean1) {
        this.warmBiomes = BiomeInitLayer.WARM_BIOMES;
        if (boolean1) {
            this.warmBiomes = BiomeInitLayer.LEGACY_WARM_BIOMES;
        }
    }
    
    public int apply(final Context cvh, int integer) {
        final int integer2 = (integer & 0xF00) >> 8;
        integer &= 0xFFFFF0FF;
        if (Layers.isOcean(integer) || integer == 14) {
            return integer;
        }
        switch (integer) {
            case 1: {
                if (integer2 > 0) {
                    return (cvh.nextRandom(3) == 0) ? 39 : 38;
                }
                return this.warmBiomes[cvh.nextRandom(this.warmBiomes.length)];
            }
            case 2: {
                if (integer2 > 0) {
                    return 21;
                }
                return BiomeInitLayer.MEDIUM_BIOMES[cvh.nextRandom(BiomeInitLayer.MEDIUM_BIOMES.length)];
            }
            case 3: {
                if (integer2 > 0) {
                    return 32;
                }
                return BiomeInitLayer.COLD_BIOMES[cvh.nextRandom(BiomeInitLayer.COLD_BIOMES.length)];
            }
            case 4: {
                return BiomeInitLayer.ICE_BIOMES[cvh.nextRandom(BiomeInitLayer.ICE_BIOMES.length)];
            }
            default: {
                return 14;
            }
        }
    }
    
    static {
        LEGACY_WARM_BIOMES = new int[] { 2, 4, 3, 6, 1, 5 };
        WARM_BIOMES = new int[] { 2, 2, 2, 35, 35, 1 };
        MEDIUM_BIOMES = new int[] { 4, 29, 3, 1, 27, 6 };
        COLD_BIOMES = new int[] { 4, 3, 5, 1 };
        ICE_BIOMES = new int[] { 12, 12, 12, 30 };
    }
}
