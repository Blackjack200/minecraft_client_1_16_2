package net.minecraft.data.worldgen;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;

public class SurfaceBuilders {
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> BADLANDS;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> BASALT_DELTAS;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> CRIMSON_FOREST;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> DESERT;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> END;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ERODED_BADLANDS;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> FROZEN_OCEAN;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> FULL_SAND;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GIANT_TREE_TAIGA;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GRASS;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> GRAVELLY_MOUNTAIN;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> ICE_SPIKES;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> MOUNTAIN;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> MYCELIUM;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> NETHER;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> NOPE;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> OCEAN_SAND;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SHATTERED_SAVANNA;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SOUL_SAND_VALLEY;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> STONE;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> SWAMP;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> WARPED_FOREST;
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> WOODED_BADLANDS;
    
    private static <SC extends SurfaceBuilderConfiguration> ConfiguredSurfaceBuilder<SC> register(final String string, final ConfiguredSurfaceBuilder<SC> ctd) {
        return BuiltinRegistries.<ConfiguredSurfaceBuilder<SC>>register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, string, ctd);
    }
    
    static {
        BADLANDS = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("badlands", SurfaceBuilder.BADLANDS.configured(SurfaceBuilder.CONFIG_BADLANDS));
        BASALT_DELTAS = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("basalt_deltas", SurfaceBuilder.BASALT_DELTAS.configured(SurfaceBuilder.CONFIG_BASALT_DELTAS));
        CRIMSON_FOREST = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("crimson_forest", SurfaceBuilder.NETHER_FOREST.configured(SurfaceBuilder.CONFIG_CRIMSON_FOREST));
        DESERT = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("desert", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_DESERT));
        END = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("end", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_THEEND));
        ERODED_BADLANDS = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("eroded_badlands", SurfaceBuilder.ERODED_BADLANDS.configured(SurfaceBuilder.CONFIG_BADLANDS));
        FROZEN_OCEAN = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("frozen_ocean", SurfaceBuilder.FROZEN_OCEAN.configured(SurfaceBuilder.CONFIG_GRASS));
        FULL_SAND = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("full_sand", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_FULL_SAND));
        GIANT_TREE_TAIGA = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("giant_tree_taiga", SurfaceBuilder.GIANT_TREE_TAIGA.configured(SurfaceBuilder.CONFIG_GRASS));
        GRASS = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("grass", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_GRASS));
        GRAVELLY_MOUNTAIN = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("gravelly_mountain", SurfaceBuilder.GRAVELLY_MOUNTAIN.configured(SurfaceBuilder.CONFIG_GRASS));
        ICE_SPIKES = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("ice_spikes", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderBaseConfiguration(Blocks.SNOW_BLOCK.defaultBlockState(), Blocks.DIRT.defaultBlockState(), Blocks.GRAVEL.defaultBlockState())));
        MOUNTAIN = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("mountain", SurfaceBuilder.MOUNTAIN.configured(SurfaceBuilder.CONFIG_GRASS));
        MYCELIUM = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("mycelium", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_MYCELIUM));
        NETHER = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("nether", SurfaceBuilder.NETHER.configured(SurfaceBuilder.CONFIG_HELL));
        NOPE = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("nope", SurfaceBuilder.NOPE.configured(SurfaceBuilder.CONFIG_STONE));
        OCEAN_SAND = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("ocean_sand", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_OCEAN_SAND));
        SHATTERED_SAVANNA = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("shattered_savanna", SurfaceBuilder.SHATTERED_SAVANNA.configured(SurfaceBuilder.CONFIG_GRASS));
        SOUL_SAND_VALLEY = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("soul_sand_valley", SurfaceBuilder.SOUL_SAND_VALLEY.configured(SurfaceBuilder.CONFIG_SOUL_SAND_VALLEY));
        STONE = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("stone", SurfaceBuilder.DEFAULT.configured(SurfaceBuilder.CONFIG_STONE));
        SWAMP = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("swamp", SurfaceBuilder.SWAMP.configured(SurfaceBuilder.CONFIG_GRASS));
        WARPED_FOREST = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("warped_forest", SurfaceBuilder.NETHER_FOREST.configured(SurfaceBuilder.CONFIG_WARPED_FOREST));
        WOODED_BADLANDS = SurfaceBuilders.<SurfaceBuilderBaseConfiguration>register("wooded_badlands", SurfaceBuilder.WOODED_BADLANDS.configured(SurfaceBuilder.CONFIG_BADLANDS));
    }
}
