package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

public class Carvers {
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> CAVE;
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> CANYON;
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> OCEAN_CAVE;
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CANYON;
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> UNDERWATER_CAVE;
    public static final ConfiguredWorldCarver<ProbabilityFeatureConfiguration> NETHER_CAVE;
    
    private static <WC extends CarverConfiguration> ConfiguredWorldCarver<WC> register(final String string, final ConfiguredWorldCarver<WC> chy) {
        return BuiltinRegistries.<ConfiguredWorldCarver<WC>>register(BuiltinRegistries.CONFIGURED_CARVER, string, chy);
    }
    
    static {
        CAVE = Carvers.<ProbabilityFeatureConfiguration>register("cave", WorldCarver.CAVE.configured(new ProbabilityFeatureConfiguration(0.14285715f)));
        CANYON = Carvers.<ProbabilityFeatureConfiguration>register("canyon", WorldCarver.CANYON.configured(new ProbabilityFeatureConfiguration(0.02f)));
        OCEAN_CAVE = Carvers.<ProbabilityFeatureConfiguration>register("ocean_cave", WorldCarver.CAVE.configured(new ProbabilityFeatureConfiguration(0.06666667f)));
        UNDERWATER_CANYON = Carvers.<ProbabilityFeatureConfiguration>register("underwater_canyon", WorldCarver.UNDERWATER_CANYON.configured(new ProbabilityFeatureConfiguration(0.02f)));
        UNDERWATER_CAVE = Carvers.<ProbabilityFeatureConfiguration>register("underwater_cave", WorldCarver.UNDERWATER_CAVE.configured(new ProbabilityFeatureConfiguration(0.06666667f)));
        NETHER_CAVE = Carvers.<ProbabilityFeatureConfiguration>register("nether_cave", WorldCarver.NETHER_CAVE.configured(new ProbabilityFeatureConfiguration(0.2f)));
    }
}
