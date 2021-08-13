package net.minecraft.world.level.biome;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryLookupCodec;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import java.util.Random;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import com.mojang.serialization.Codec;

public class TheEndBiomeSource extends BiomeSource {
    public static final Codec<TheEndBiomeSource> CODEC;
    private final SimplexNoise islandNoise;
    private final Registry<Biome> biomes;
    private final long seed;
    private final Biome end;
    private final Biome highlands;
    private final Biome midlands;
    private final Biome islands;
    private final Biome barrens;
    
    public TheEndBiomeSource(final Registry<Biome> gm, final long long2) {
        this(gm, long2, gm.getOrThrow(Biomes.THE_END), gm.getOrThrow(Biomes.END_HIGHLANDS), gm.getOrThrow(Biomes.END_MIDLANDS), gm.getOrThrow(Biomes.SMALL_END_ISLANDS), gm.getOrThrow(Biomes.END_BARRENS));
    }
    
    private TheEndBiomeSource(final Registry<Biome> gm, final long long2, final Biome bss3, final Biome bss4, final Biome bss5, final Biome bss6, final Biome bss7) {
        super((List<Biome>)ImmutableList.of(bss3, bss4, bss5, bss6, bss7));
        this.biomes = gm;
        this.seed = long2;
        this.end = bss3;
        this.highlands = bss4;
        this.midlands = bss5;
        this.islands = bss6;
        this.barrens = bss7;
        final WorldgenRandom chu10 = new WorldgenRandom(long2);
        chu10.consumeCount(17292);
        this.islandNoise = new SimplexNoise(chu10);
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return TheEndBiomeSource.CODEC;
    }
    
    @Override
    public BiomeSource withSeed(final long long1) {
        return new TheEndBiomeSource(this.biomes, long1, this.end, this.highlands, this.midlands, this.islands, this.barrens);
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        final int integer4 = integer1 >> 2;
        final int integer5 = integer3 >> 2;
        if (integer4 * (long)integer4 + integer5 * (long)integer5 <= 4096L) {
            return this.end;
        }
        final float float7 = getHeightValue(this.islandNoise, integer4 * 2 + 1, integer5 * 2 + 1);
        if (float7 > 40.0f) {
            return this.highlands;
        }
        if (float7 >= 0.0f) {
            return this.midlands;
        }
        if (float7 < -20.0f) {
            return this.islands;
        }
        return this.barrens;
    }
    
    public boolean stable(final long long1) {
        return this.seed == long1;
    }
    
    public static float getHeightValue(final SimplexNoise cua, final int integer2, final int integer3) {
        final int integer4 = integer2 / 2;
        final int integer5 = integer3 / 2;
        final int integer6 = integer2 % 2;
        final int integer7 = integer3 % 2;
        float float8 = 100.0f - Mth.sqrt((float)(integer2 * integer2 + integer3 * integer3)) * 8.0f;
        float8 = Mth.clamp(float8, -100.0f, 80.0f);
        for (int integer8 = -12; integer8 <= 12; ++integer8) {
            for (int integer9 = -12; integer9 <= 12; ++integer9) {
                final long long11 = integer4 + integer8;
                final long long12 = integer5 + integer9;
                if (long11 * long11 + long12 * long12 > 4096L && cua.getValue((double)long11, (double)long12) < -0.8999999761581421) {
                    final float float9 = (Mth.abs((float)long11) * 3439.0f + Mth.abs((float)long12) * 147.0f) % 13.0f + 9.0f;
                    final float float10 = (float)(integer6 - integer8 * 2);
                    final float float11 = (float)(integer7 - integer9 * 2);
                    float float12 = 100.0f - Mth.sqrt(float10 * float10 + float11 * float11) * float9;
                    float12 = Mth.clamp(float12, -100.0f, 80.0f);
                    float8 = Math.max(float8, float12);
                }
            }
        }
        return float8;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(bth -> bth.biomes), (App)Codec.LONG.fieldOf("seed").stable().forGetter(bth -> bth.seed)).apply((Applicative)instance, instance.stable(TheEndBiomeSource::new)));
    }
}
