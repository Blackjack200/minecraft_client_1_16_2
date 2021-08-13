package net.minecraft.world.level.chunk;

import org.apache.logging.log4j.LogManager;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.IdMap;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.biome.BiomeManager;

public class ChunkBiomeContainer implements BiomeManager.NoiseBiomeSource {
    private static final Logger LOGGER;
    private static final int WIDTH_BITS;
    private static final int HEIGHT_BITS;
    public static final int BIOMES_SIZE;
    public static final int HORIZONTAL_MASK;
    public static final int VERTICAL_MASK;
    private final IdMap<Biome> biomeRegistry;
    private final Biome[] biomes;
    
    public ChunkBiomeContainer(final IdMap<Biome> gg, final Biome[] arr) {
        this.biomeRegistry = gg;
        this.biomes = arr;
    }
    
    private ChunkBiomeContainer(final IdMap<Biome> gg) {
        this(gg, new Biome[ChunkBiomeContainer.BIOMES_SIZE]);
    }
    
    public ChunkBiomeContainer(final IdMap<Biome> gg, final int[] arr) {
        this(gg);
        for (int integer4 = 0; integer4 < this.biomes.length; ++integer4) {
            final int integer5 = arr[integer4];
            final Biome bss6 = gg.byId(integer5);
            if (bss6 == null) {
                ChunkBiomeContainer.LOGGER.warn(new StringBuilder().append("Received invalid biome id: ").append(integer5).toString());
                this.biomes[integer4] = gg.byId(0);
            }
            else {
                this.biomes[integer4] = bss6;
            }
        }
    }
    
    public ChunkBiomeContainer(final IdMap<Biome> gg, final ChunkPos bra, final BiomeSource bsv) {
        this(gg);
        final int integer5 = bra.getMinBlockX() >> 2;
        final int integer6 = bra.getMinBlockZ() >> 2;
        for (int integer7 = 0; integer7 < this.biomes.length; ++integer7) {
            final int integer8 = integer7 & ChunkBiomeContainer.HORIZONTAL_MASK;
            final int integer9 = integer7 >> ChunkBiomeContainer.WIDTH_BITS + ChunkBiomeContainer.WIDTH_BITS & ChunkBiomeContainer.VERTICAL_MASK;
            final int integer10 = integer7 >> ChunkBiomeContainer.WIDTH_BITS & ChunkBiomeContainer.HORIZONTAL_MASK;
            this.biomes[integer7] = bsv.getNoiseBiome(integer5 + integer8, integer9, integer6 + integer10);
        }
    }
    
    public ChunkBiomeContainer(final IdMap<Biome> gg, final ChunkPos bra, final BiomeSource bsv, @Nullable final int[] arr) {
        this(gg);
        final int integer6 = bra.getMinBlockX() >> 2;
        final int integer7 = bra.getMinBlockZ() >> 2;
        if (arr != null) {
            for (int integer8 = 0; integer8 < arr.length; ++integer8) {
                this.biomes[integer8] = gg.byId(arr[integer8]);
                if (this.biomes[integer8] == null) {
                    final int integer9 = integer8 & ChunkBiomeContainer.HORIZONTAL_MASK;
                    final int integer10 = integer8 >> ChunkBiomeContainer.WIDTH_BITS + ChunkBiomeContainer.WIDTH_BITS & ChunkBiomeContainer.VERTICAL_MASK;
                    final int integer11 = integer8 >> ChunkBiomeContainer.WIDTH_BITS & ChunkBiomeContainer.HORIZONTAL_MASK;
                    this.biomes[integer8] = bsv.getNoiseBiome(integer6 + integer9, integer10, integer7 + integer11);
                }
            }
        }
        else {
            for (int integer8 = 0; integer8 < this.biomes.length; ++integer8) {
                final int integer9 = integer8 & ChunkBiomeContainer.HORIZONTAL_MASK;
                final int integer10 = integer8 >> ChunkBiomeContainer.WIDTH_BITS + ChunkBiomeContainer.WIDTH_BITS & ChunkBiomeContainer.VERTICAL_MASK;
                final int integer11 = integer8 >> ChunkBiomeContainer.WIDTH_BITS & ChunkBiomeContainer.HORIZONTAL_MASK;
                this.biomes[integer8] = bsv.getNoiseBiome(integer6 + integer9, integer10, integer7 + integer11);
            }
        }
    }
    
    public int[] writeBiomes() {
        final int[] arr2 = new int[this.biomes.length];
        for (int integer3 = 0; integer3 < this.biomes.length; ++integer3) {
            arr2[integer3] = this.biomeRegistry.getId(this.biomes[integer3]);
        }
        return arr2;
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        final int integer4 = integer1 & ChunkBiomeContainer.HORIZONTAL_MASK;
        final int integer5 = Mth.clamp(integer2, 0, ChunkBiomeContainer.VERTICAL_MASK);
        final int integer6 = integer3 & ChunkBiomeContainer.HORIZONTAL_MASK;
        return this.biomes[integer5 << ChunkBiomeContainer.WIDTH_BITS + ChunkBiomeContainer.WIDTH_BITS | integer6 << ChunkBiomeContainer.WIDTH_BITS | integer4];
    }
    
    static {
        LOGGER = LogManager.getLogger();
        WIDTH_BITS = (int)Math.round(Math.log(16.0) / Math.log(2.0)) - 2;
        HEIGHT_BITS = (int)Math.round(Math.log(256.0) / Math.log(2.0)) - 2;
        BIOMES_SIZE = 1 << ChunkBiomeContainer.WIDTH_BITS + ChunkBiomeContainer.WIDTH_BITS + ChunkBiomeContainer.HEIGHT_BITS;
        HORIZONTAL_MASK = (1 << ChunkBiomeContainer.WIDTH_BITS) - 1;
        VERTICAL_MASK = (1 << ChunkBiomeContainer.HEIGHT_BITS) - 1;
    }
}
