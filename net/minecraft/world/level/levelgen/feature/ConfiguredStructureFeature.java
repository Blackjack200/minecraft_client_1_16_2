package net.minecraft.world.level.levelgen.feature;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ConfiguredStructureFeature<FC extends FeatureConfiguration, F extends StructureFeature<FC>> {
    public static final Codec<ConfiguredStructureFeature<?, ?>> DIRECT_CODEC;
    public static final Codec<Supplier<ConfiguredStructureFeature<?, ?>>> CODEC;
    public static final Codec<List<Supplier<ConfiguredStructureFeature<?, ?>>>> LIST_CODEC;
    public final F feature;
    public final FC config;
    
    public ConfiguredStructureFeature(final F ckx, final FC clx) {
        this.feature = ckx;
        this.config = clx;
    }
    
    public StructureStart<?> generate(final RegistryAccess gn, final ChunkGenerator cfv, final BiomeSource bsv, final StructureManager cst, final long long5, final ChunkPos bra, final Biome bss, final int integer, final StructureFeatureConfiguration cmv) {
        return this.feature.generate(gn, cfv, bsv, cst, long5, bra, bss, integer, new WorldgenRandom(), cmv, this.config);
    }
    
    static {
        DIRECT_CODEC = Registry.STRUCTURE_FEATURE.dispatch(cit -> cit.feature, StructureFeature::configuredStructureCodec);
        CODEC = (Codec)RegistryFileCodec.<ConfiguredStructureFeature<?, ?>>create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ConfiguredStructureFeature.DIRECT_CODEC);
        LIST_CODEC = RegistryFileCodec.<ConfiguredStructureFeature<?, ?>>homogeneousList(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, ConfiguredStructureFeature.DIRECT_CODEC);
    }
}
