package net.minecraft.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.placement.nether.CountMultiLayerDecorator;
import net.minecraft.world.level.levelgen.placement.nether.GlowstoneDecorator;
import net.minecraft.world.level.levelgen.placement.nether.MagmaDecorator;
import net.minecraft.world.level.levelgen.placement.nether.FireDecorator;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoiseDependantDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public abstract class FeatureDecorator<DC extends DecoratorConfiguration> {
    public static final FeatureDecorator<NoneDecoratorConfiguration> NOPE;
    public static final FeatureDecorator<ChanceDecoratorConfiguration> CHANCE;
    public static final FeatureDecorator<CountConfiguration> COUNT;
    public static final FeatureDecorator<NoiseDependantDecoratorConfiguration> COUNT_NOISE;
    public static final FeatureDecorator<NoiseCountFactorDecoratorConfiguration> COUNT_NOISE_BIASED;
    public static final FeatureDecorator<FrequencyWithExtraChanceDecoratorConfiguration> COUNT_EXTRA;
    public static final FeatureDecorator<NoneDecoratorConfiguration> SQUARE;
    public static final FeatureDecorator<NoneDecoratorConfiguration> HEIGHTMAP;
    public static final FeatureDecorator<NoneDecoratorConfiguration> HEIGHTMAP_SPREAD_DOUBLE;
    public static final FeatureDecorator<NoneDecoratorConfiguration> TOP_SOLID_HEIGHTMAP;
    public static final FeatureDecorator<NoneDecoratorConfiguration> HEIGHTMAP_WORLD_SURFACE;
    public static final FeatureDecorator<RangeDecoratorConfiguration> RANGE;
    public static final FeatureDecorator<RangeDecoratorConfiguration> RANGE_BIASED;
    public static final FeatureDecorator<RangeDecoratorConfiguration> RANGE_VERY_BIASED;
    public static final FeatureDecorator<DepthAverageConfigation> DEPTH_AVERAGE;
    public static final FeatureDecorator<NoneDecoratorConfiguration> SPREAD_32_ABOVE;
    public static final FeatureDecorator<CarvingMaskDecoratorConfiguration> CARVING_MASK;
    public static final FeatureDecorator<CountConfiguration> FIRE;
    public static final FeatureDecorator<NoneDecoratorConfiguration> MAGMA;
    public static final FeatureDecorator<NoneDecoratorConfiguration> EMERALD_ORE;
    public static final FeatureDecorator<ChanceDecoratorConfiguration> LAVA_LAKE;
    public static final FeatureDecorator<ChanceDecoratorConfiguration> WATER_LAKE;
    public static final FeatureDecorator<CountConfiguration> GLOWSTONE;
    public static final FeatureDecorator<NoneDecoratorConfiguration> END_GATEWAY;
    public static final FeatureDecorator<NoneDecoratorConfiguration> DARK_OAK_TREE;
    public static final FeatureDecorator<NoneDecoratorConfiguration> ICEBERG;
    public static final FeatureDecorator<NoneDecoratorConfiguration> END_ISLAND;
    public static final FeatureDecorator<DecoratedDecoratorConfiguration> DECORATED;
    public static final FeatureDecorator<CountConfiguration> COUNT_MULTILAYER;
    private final Codec<ConfiguredDecorator<DC>> configuredCodec;
    
    private static <T extends DecoratorConfiguration, G extends FeatureDecorator<T>> G register(final String string, final G cpz) {
        return Registry.<G>register(Registry.DECORATOR, string, cpz);
    }
    
    public FeatureDecorator(final Codec<DC> codec) {
        this.configuredCodec = (Codec<ConfiguredDecorator<DC>>)codec.fieldOf("config").xmap(clt -> new ConfiguredDecorator((FeatureDecorator<DC>)this, (DC)clt), ConfiguredDecorator::config).codec();
    }
    
    public ConfiguredDecorator<DC> configured(final DC clt) {
        return new ConfiguredDecorator<DC>(this, clt);
    }
    
    public Codec<ConfiguredDecorator<DC>> configuredCodec() {
        return this.configuredCodec;
    }
    
    public abstract Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final DC clt, final BlockPos fx);
    
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(this.hashCode());
    }
    
    static {
        NOPE = FeatureDecorator.<DecoratorConfiguration, NopePlacementDecorator>register("nope", new NopePlacementDecorator(NoneDecoratorConfiguration.CODEC));
        CHANCE = FeatureDecorator.<DecoratorConfiguration, ChanceDecorator>register("chance", new ChanceDecorator(ChanceDecoratorConfiguration.CODEC));
        COUNT = FeatureDecorator.<DecoratorConfiguration, CountDecorator>register("count", new CountDecorator(CountConfiguration.CODEC));
        COUNT_NOISE = FeatureDecorator.<DecoratorConfiguration, CountNoiseDecorator>register("count_noise", new CountNoiseDecorator(NoiseDependantDecoratorConfiguration.CODEC));
        COUNT_NOISE_BIASED = FeatureDecorator.<DecoratorConfiguration, NoiseBasedDecorator>register("count_noise_biased", new NoiseBasedDecorator(NoiseCountFactorDecoratorConfiguration.CODEC));
        COUNT_EXTRA = FeatureDecorator.<DecoratorConfiguration, CountWithExtraChanceDecorator>register("count_extra", new CountWithExtraChanceDecorator(FrequencyWithExtraChanceDecoratorConfiguration.CODEC));
        SQUARE = FeatureDecorator.<DecoratorConfiguration, SquareDecorator>register("square", new SquareDecorator(NoneDecoratorConfiguration.CODEC));
        HEIGHTMAP = FeatureDecorator.<DecoratorConfiguration, HeightmapDecorator<NoneDecoratorConfiguration>>register("heightmap", new HeightmapDecorator<NoneDecoratorConfiguration>(NoneDecoratorConfiguration.CODEC));
        HEIGHTMAP_SPREAD_DOUBLE = FeatureDecorator.<DecoratorConfiguration, HeightmapDoubleDecorator<NoneDecoratorConfiguration>>register("heightmap_spread_double", new HeightmapDoubleDecorator<NoneDecoratorConfiguration>(NoneDecoratorConfiguration.CODEC));
        TOP_SOLID_HEIGHTMAP = FeatureDecorator.<DecoratorConfiguration, TopSolidHeightMapDecorator>register("top_solid_heightmap", new TopSolidHeightMapDecorator(NoneDecoratorConfiguration.CODEC));
        HEIGHTMAP_WORLD_SURFACE = FeatureDecorator.<DecoratorConfiguration, HeightMapWorldSurfaceDecorator>register("heightmap_world_surface", new HeightMapWorldSurfaceDecorator(NoneDecoratorConfiguration.CODEC));
        RANGE = FeatureDecorator.<DecoratorConfiguration, RangeDecorator>register("range", new RangeDecorator(RangeDecoratorConfiguration.CODEC));
        RANGE_BIASED = FeatureDecorator.<DecoratorConfiguration, BiasedRangeDecorator>register("range_biased", new BiasedRangeDecorator(RangeDecoratorConfiguration.CODEC));
        RANGE_VERY_BIASED = FeatureDecorator.<DecoratorConfiguration, VeryBiasedRangeDecorator>register("range_very_biased", new VeryBiasedRangeDecorator(RangeDecoratorConfiguration.CODEC));
        DEPTH_AVERAGE = FeatureDecorator.<DecoratorConfiguration, DepthAverageDecorator>register("depth_average", new DepthAverageDecorator(DepthAverageConfigation.CODEC));
        SPREAD_32_ABOVE = FeatureDecorator.<DecoratorConfiguration, Spread32Decorator>register("spread_32_above", new Spread32Decorator(NoneDecoratorConfiguration.CODEC));
        CARVING_MASK = FeatureDecorator.<DecoratorConfiguration, CarvingMaskDecorator>register("carving_mask", new CarvingMaskDecorator(CarvingMaskDecoratorConfiguration.CODEC));
        FIRE = FeatureDecorator.<DecoratorConfiguration, FireDecorator>register("fire", new FireDecorator(CountConfiguration.CODEC));
        MAGMA = FeatureDecorator.<DecoratorConfiguration, MagmaDecorator>register("magma", new MagmaDecorator(NoneDecoratorConfiguration.CODEC));
        EMERALD_ORE = FeatureDecorator.<DecoratorConfiguration, EmeraldPlacementDecorator>register("emerald_ore", new EmeraldPlacementDecorator(NoneDecoratorConfiguration.CODEC));
        LAVA_LAKE = FeatureDecorator.<DecoratorConfiguration, LakeLavaPlacementDecorator>register("lava_lake", new LakeLavaPlacementDecorator(ChanceDecoratorConfiguration.CODEC));
        WATER_LAKE = FeatureDecorator.<DecoratorConfiguration, LakeWaterPlacementDecorator>register("water_lake", new LakeWaterPlacementDecorator(ChanceDecoratorConfiguration.CODEC));
        GLOWSTONE = FeatureDecorator.<DecoratorConfiguration, GlowstoneDecorator>register("glowstone", new GlowstoneDecorator(CountConfiguration.CODEC));
        END_GATEWAY = FeatureDecorator.<DecoratorConfiguration, EndGatewayPlacementDecorator>register("end_gateway", new EndGatewayPlacementDecorator(NoneDecoratorConfiguration.CODEC));
        DARK_OAK_TREE = FeatureDecorator.<DecoratorConfiguration, DarkOakTreePlacementDecorator>register("dark_oak_tree", new DarkOakTreePlacementDecorator(NoneDecoratorConfiguration.CODEC));
        ICEBERG = FeatureDecorator.<DecoratorConfiguration, IcebergPlacementDecorator>register("iceberg", new IcebergPlacementDecorator(NoneDecoratorConfiguration.CODEC));
        END_ISLAND = FeatureDecorator.<DecoratorConfiguration, EndIslandPlacementDecorator>register("end_island", new EndIslandPlacementDecorator(NoneDecoratorConfiguration.CODEC));
        DECORATED = FeatureDecorator.<DecoratorConfiguration, DecoratedDecorator>register("decorated", new DecoratedDecorator(DecoratedDecoratorConfiguration.CODEC));
        COUNT_MULTILAYER = FeatureDecorator.<DecoratorConfiguration, CountMultiLayerDecorator>register("count_multilayer", new CountMultiLayerDecorator(CountConfiguration.CODEC));
    }
}
