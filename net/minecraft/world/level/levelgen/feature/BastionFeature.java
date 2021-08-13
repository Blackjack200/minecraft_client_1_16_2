package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import com.mojang.serialization.Codec;

public class BastionFeature extends JigsawFeature {
    public BastionFeature(final Codec<JigsawConfiguration> codec) {
        super(codec, 33, false, false);
    }
    
    @Override
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final JigsawConfiguration clz) {
        return chu.nextInt(5) >= 2;
    }
}
