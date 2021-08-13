package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SurfaceBuilder<C extends SurfaceBuilderConfiguration> {
    private static final BlockState DIRT;
    private static final BlockState GRASS_BLOCK;
    private static final BlockState PODZOL;
    private static final BlockState GRAVEL;
    private static final BlockState STONE;
    private static final BlockState COARSE_DIRT;
    private static final BlockState SAND;
    private static final BlockState RED_SAND;
    private static final BlockState WHITE_TERRACOTTA;
    private static final BlockState MYCELIUM;
    private static final BlockState SOUL_SAND;
    private static final BlockState NETHERRACK;
    private static final BlockState ENDSTONE;
    private static final BlockState CRIMSON_NYLIUM;
    private static final BlockState WARPED_NYLIUM;
    private static final BlockState NETHER_WART_BLOCK;
    private static final BlockState WARPED_WART_BLOCK;
    private static final BlockState BLACKSTONE;
    private static final BlockState BASALT;
    private static final BlockState MAGMA;
    public static final SurfaceBuilderBaseConfiguration CONFIG_PODZOL;
    public static final SurfaceBuilderBaseConfiguration CONFIG_GRAVEL;
    public static final SurfaceBuilderBaseConfiguration CONFIG_GRASS;
    public static final SurfaceBuilderBaseConfiguration CONFIG_STONE;
    public static final SurfaceBuilderBaseConfiguration CONFIG_COARSE_DIRT;
    public static final SurfaceBuilderBaseConfiguration CONFIG_DESERT;
    public static final SurfaceBuilderBaseConfiguration CONFIG_OCEAN_SAND;
    public static final SurfaceBuilderBaseConfiguration CONFIG_FULL_SAND;
    public static final SurfaceBuilderBaseConfiguration CONFIG_BADLANDS;
    public static final SurfaceBuilderBaseConfiguration CONFIG_MYCELIUM;
    public static final SurfaceBuilderBaseConfiguration CONFIG_HELL;
    public static final SurfaceBuilderBaseConfiguration CONFIG_SOUL_SAND_VALLEY;
    public static final SurfaceBuilderBaseConfiguration CONFIG_THEEND;
    public static final SurfaceBuilderBaseConfiguration CONFIG_CRIMSON_FOREST;
    public static final SurfaceBuilderBaseConfiguration CONFIG_WARPED_FOREST;
    public static final SurfaceBuilderBaseConfiguration CONFIG_BASALT_DELTAS;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> DEFAULT;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> MOUNTAIN;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SHATTERED_SAVANNA;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> GRAVELLY_MOUNTAIN;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> GIANT_TREE_TAIGA;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SWAMP;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> BADLANDS;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> WOODED_BADLANDS;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> ERODED_BADLANDS;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> FROZEN_OCEAN;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> NETHER;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> NETHER_FOREST;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> SOUL_SAND_VALLEY;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> BASALT_DELTAS;
    public static final SurfaceBuilder<SurfaceBuilderBaseConfiguration> NOPE;
    private final Codec<ConfiguredSurfaceBuilder<C>> configuredCodec;
    
    private static <C extends SurfaceBuilderConfiguration, F extends SurfaceBuilder<C>> F register(final String string, final F ctq) {
        return Registry.<F>register(Registry.SURFACE_BUILDER, string, ctq);
    }
    
    public SurfaceBuilder(final Codec<C> codec) {
        this.configuredCodec = (Codec<ConfiguredSurfaceBuilder<C>>)codec.fieldOf("config").xmap(this::configured, ConfiguredSurfaceBuilder::config).codec();
    }
    
    public Codec<ConfiguredSurfaceBuilder<C>> configuredCodec() {
        return this.configuredCodec;
    }
    
    public ConfiguredSurfaceBuilder<C> configured(final C cts) {
        return new ConfiguredSurfaceBuilder<C>(this, cts);
    }
    
    public abstract void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11, final C cts);
    
    public void initNoise(final long long1) {
    }
    
    static {
        DIRT = Blocks.DIRT.defaultBlockState();
        GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
        PODZOL = Blocks.PODZOL.defaultBlockState();
        GRAVEL = Blocks.GRAVEL.defaultBlockState();
        STONE = Blocks.STONE.defaultBlockState();
        COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
        SAND = Blocks.SAND.defaultBlockState();
        RED_SAND = Blocks.RED_SAND.defaultBlockState();
        WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();
        MYCELIUM = Blocks.MYCELIUM.defaultBlockState();
        SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
        NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
        ENDSTONE = Blocks.END_STONE.defaultBlockState();
        CRIMSON_NYLIUM = Blocks.CRIMSON_NYLIUM.defaultBlockState();
        WARPED_NYLIUM = Blocks.WARPED_NYLIUM.defaultBlockState();
        NETHER_WART_BLOCK = Blocks.NETHER_WART_BLOCK.defaultBlockState();
        WARPED_WART_BLOCK = Blocks.WARPED_WART_BLOCK.defaultBlockState();
        BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
        BASALT = Blocks.BASALT.defaultBlockState();
        MAGMA = Blocks.MAGMA_BLOCK.defaultBlockState();
        CONFIG_PODZOL = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.PODZOL, SurfaceBuilder.DIRT, SurfaceBuilder.GRAVEL);
        CONFIG_GRAVEL = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.GRAVEL, SurfaceBuilder.GRAVEL, SurfaceBuilder.GRAVEL);
        CONFIG_GRASS = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.GRASS_BLOCK, SurfaceBuilder.DIRT, SurfaceBuilder.GRAVEL);
        CONFIG_STONE = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.STONE, SurfaceBuilder.STONE, SurfaceBuilder.GRAVEL);
        CONFIG_COARSE_DIRT = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.COARSE_DIRT, SurfaceBuilder.DIRT, SurfaceBuilder.GRAVEL);
        CONFIG_DESERT = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.SAND, SurfaceBuilder.SAND, SurfaceBuilder.GRAVEL);
        CONFIG_OCEAN_SAND = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.GRASS_BLOCK, SurfaceBuilder.DIRT, SurfaceBuilder.SAND);
        CONFIG_FULL_SAND = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.SAND, SurfaceBuilder.SAND, SurfaceBuilder.SAND);
        CONFIG_BADLANDS = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.RED_SAND, SurfaceBuilder.WHITE_TERRACOTTA, SurfaceBuilder.GRAVEL);
        CONFIG_MYCELIUM = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.MYCELIUM, SurfaceBuilder.DIRT, SurfaceBuilder.GRAVEL);
        CONFIG_HELL = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.NETHERRACK, SurfaceBuilder.NETHERRACK, SurfaceBuilder.NETHERRACK);
        CONFIG_SOUL_SAND_VALLEY = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.SOUL_SAND, SurfaceBuilder.SOUL_SAND, SurfaceBuilder.SOUL_SAND);
        CONFIG_THEEND = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.ENDSTONE, SurfaceBuilder.ENDSTONE, SurfaceBuilder.ENDSTONE);
        CONFIG_CRIMSON_FOREST = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.CRIMSON_NYLIUM, SurfaceBuilder.NETHERRACK, SurfaceBuilder.NETHER_WART_BLOCK);
        CONFIG_WARPED_FOREST = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.WARPED_NYLIUM, SurfaceBuilder.NETHERRACK, SurfaceBuilder.WARPED_WART_BLOCK);
        CONFIG_BASALT_DELTAS = new SurfaceBuilderBaseConfiguration(SurfaceBuilder.BLACKSTONE, SurfaceBuilder.BASALT, SurfaceBuilder.MAGMA);
        DEFAULT = SurfaceBuilder.<SurfaceBuilderConfiguration, DefaultSurfaceBuilder>register("default", new DefaultSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        MOUNTAIN = SurfaceBuilder.<SurfaceBuilderConfiguration, MountainSurfaceBuilder>register("mountain", new MountainSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        SHATTERED_SAVANNA = SurfaceBuilder.<SurfaceBuilderConfiguration, ShatteredSavanaSurfaceBuilder>register("shattered_savanna", new ShatteredSavanaSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        GRAVELLY_MOUNTAIN = SurfaceBuilder.<SurfaceBuilderConfiguration, GravellyMountainSurfaceBuilder>register("gravelly_mountain", new GravellyMountainSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        GIANT_TREE_TAIGA = SurfaceBuilder.<SurfaceBuilderConfiguration, GiantTreeTaigaSurfaceBuilder>register("giant_tree_taiga", new GiantTreeTaigaSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        SWAMP = SurfaceBuilder.<SurfaceBuilderConfiguration, SwampSurfaceBuilder>register("swamp", new SwampSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        BADLANDS = SurfaceBuilder.<SurfaceBuilderConfiguration, BadlandsSurfaceBuilder>register("badlands", new BadlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        WOODED_BADLANDS = SurfaceBuilder.<SurfaceBuilderConfiguration, WoodedBadlandsSurfaceBuilder>register("wooded_badlands", new WoodedBadlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        ERODED_BADLANDS = SurfaceBuilder.<SurfaceBuilderConfiguration, ErodedBadlandsSurfaceBuilder>register("eroded_badlands", new ErodedBadlandsSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        FROZEN_OCEAN = SurfaceBuilder.<SurfaceBuilderConfiguration, FrozenOceanSurfaceBuilder>register("frozen_ocean", new FrozenOceanSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        NETHER = SurfaceBuilder.<SurfaceBuilderConfiguration, NetherSurfaceBuilder>register("nether", new NetherSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        NETHER_FOREST = SurfaceBuilder.<SurfaceBuilderConfiguration, NetherForestSurfaceBuilder>register("nether_forest", new NetherForestSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        SOUL_SAND_VALLEY = SurfaceBuilder.<SurfaceBuilderConfiguration, SoulSandValleySurfaceBuilder>register("soul_sand_valley", new SoulSandValleySurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        BASALT_DELTAS = SurfaceBuilder.<SurfaceBuilderConfiguration, BasaltDeltasSurfaceBuilder>register("basalt_deltas", new BasaltDeltasSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
        NOPE = SurfaceBuilder.<SurfaceBuilderConfiguration, NopeSurfaceBuilder>register("nope", new NopeSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
    }
}
