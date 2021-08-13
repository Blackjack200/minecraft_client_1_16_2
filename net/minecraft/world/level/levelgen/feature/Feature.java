package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.BlockBehaviour;
import java.util.function.Predicate;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.core.Registry;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratedFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomBooleanFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceSphereConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class Feature<FC extends FeatureConfiguration> {
    public static final Feature<NoneFeatureConfiguration> NO_OP;
    public static final Feature<TreeConfiguration> TREE;
    public static final AbstractFlowerFeature<RandomPatchConfiguration> FLOWER;
    public static final AbstractFlowerFeature<RandomPatchConfiguration> NO_BONEMEAL_FLOWER;
    public static final Feature<RandomPatchConfiguration> RANDOM_PATCH;
    public static final Feature<BlockPileConfiguration> BLOCK_PILE;
    public static final Feature<SpringConfiguration> SPRING;
    public static final Feature<NoneFeatureConfiguration> CHORUS_PLANT;
    public static final Feature<ReplaceBlockConfiguration> EMERALD_ORE;
    public static final Feature<NoneFeatureConfiguration> VOID_START_PLATFORM;
    public static final Feature<NoneFeatureConfiguration> DESERT_WELL;
    public static final Feature<NoneFeatureConfiguration> FOSSIL;
    public static final Feature<HugeMushroomFeatureConfiguration> HUGE_RED_MUSHROOM;
    public static final Feature<HugeMushroomFeatureConfiguration> HUGE_BROWN_MUSHROOM;
    public static final Feature<NoneFeatureConfiguration> ICE_SPIKE;
    public static final Feature<NoneFeatureConfiguration> GLOWSTONE_BLOB;
    public static final Feature<NoneFeatureConfiguration> FREEZE_TOP_LAYER;
    public static final Feature<NoneFeatureConfiguration> VINES;
    public static final Feature<NoneFeatureConfiguration> MONSTER_ROOM;
    public static final Feature<NoneFeatureConfiguration> BLUE_ICE;
    public static final Feature<BlockStateConfiguration> ICEBERG;
    public static final Feature<BlockStateConfiguration> FOREST_ROCK;
    public static final Feature<DiskConfiguration> DISK;
    public static final Feature<DiskConfiguration> ICE_PATCH;
    public static final Feature<BlockStateConfiguration> LAKE;
    public static final Feature<OreConfiguration> ORE;
    public static final Feature<SpikeConfiguration> END_SPIKE;
    public static final Feature<NoneFeatureConfiguration> END_ISLAND;
    public static final Feature<EndGatewayConfiguration> END_GATEWAY;
    public static final SeagrassFeature SEAGRASS;
    public static final Feature<NoneFeatureConfiguration> KELP;
    public static final Feature<NoneFeatureConfiguration> CORAL_TREE;
    public static final Feature<NoneFeatureConfiguration> CORAL_MUSHROOM;
    public static final Feature<NoneFeatureConfiguration> CORAL_CLAW;
    public static final Feature<CountConfiguration> SEA_PICKLE;
    public static final Feature<SimpleBlockConfiguration> SIMPLE_BLOCK;
    public static final Feature<ProbabilityFeatureConfiguration> BAMBOO;
    public static final Feature<HugeFungusConfiguration> HUGE_FUNGUS;
    public static final Feature<BlockPileConfiguration> NETHER_FOREST_VEGETATION;
    public static final Feature<NoneFeatureConfiguration> WEEPING_VINES;
    public static final Feature<NoneFeatureConfiguration> TWISTING_VINES;
    public static final Feature<ColumnFeatureConfiguration> BASALT_COLUMNS;
    public static final Feature<DeltaFeatureConfiguration> DELTA_FEATURE;
    public static final Feature<ReplaceSphereConfiguration> REPLACE_BLOBS;
    public static final Feature<LayerConfiguration> FILL_LAYER;
    public static final BonusChestFeature BONUS_CHEST;
    public static final Feature<NoneFeatureConfiguration> BASALT_PILLAR;
    public static final Feature<OreConfiguration> NO_SURFACE_ORE;
    public static final Feature<RandomFeatureConfiguration> RANDOM_SELECTOR;
    public static final Feature<SimpleRandomFeatureConfiguration> SIMPLE_RANDOM_SELECTOR;
    public static final Feature<RandomBooleanFeatureConfiguration> RANDOM_BOOLEAN_SELECTOR;
    public static final Feature<DecoratedFeatureConfiguration> DECORATED;
    private final Codec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec;
    
    private static <C extends FeatureConfiguration, F extends Feature<C>> F register(final String string, final F cji) {
        return Registry.<F>register(Registry.FEATURE, string, cji);
    }
    
    public Feature(final Codec<FC> codec) {
        this.configuredCodec = (Codec<ConfiguredFeature<FC, Feature<FC>>>)codec.fieldOf("config").xmap(clx -> new ConfiguredFeature((F)this, (FC)clx), cis -> cis.config).codec();
    }
    
    public Codec<ConfiguredFeature<FC, Feature<FC>>> configuredCodec() {
        return this.configuredCodec;
    }
    
    public ConfiguredFeature<FC, ?> configured(final FC clx) {
        return new ConfiguredFeature<FC, Object>(this, clx);
    }
    
    protected void setBlock(final LevelWriter bsb, final BlockPos fx, final BlockState cee) {
        bsb.setBlock(fx, cee, 3);
    }
    
    public abstract boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final FC clx);
    
    protected static boolean isStone(final Block bul) {
        return bul == Blocks.STONE || bul == Blocks.GRANITE || bul == Blocks.DIORITE || bul == Blocks.ANDESITE;
    }
    
    public static boolean isDirt(final Block bul) {
        return bul == Blocks.DIRT || bul == Blocks.GRASS_BLOCK || bul == Blocks.PODZOL || bul == Blocks.COARSE_DIRT || bul == Blocks.MYCELIUM;
    }
    
    public static boolean isGrassOrDirt(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)(cee -> isDirt(cee.getBlock())));
    }
    
    public static boolean isAir(final LevelSimulatedReader brz, final BlockPos fx) {
        return brz.isStateAtPosition(fx, (Predicate<BlockState>)BlockBehaviour.BlockStateBase::isAir);
    }
    
    static {
        NO_OP = Feature.<FeatureConfiguration, NoOpFeature>register("no_op", new NoOpFeature(NoneFeatureConfiguration.CODEC));
        TREE = Feature.<FeatureConfiguration, TreeFeature>register("tree", new TreeFeature(TreeConfiguration.CODEC));
        FLOWER = Feature.<FeatureConfiguration, DefaultFlowerFeature>register("flower", new DefaultFlowerFeature(RandomPatchConfiguration.CODEC));
        NO_BONEMEAL_FLOWER = Feature.<FeatureConfiguration, DefaultFlowerFeature>register("no_bonemeal_flower", new DefaultFlowerFeature(RandomPatchConfiguration.CODEC));
        RANDOM_PATCH = Feature.<FeatureConfiguration, RandomPatchFeature>register("random_patch", new RandomPatchFeature(RandomPatchConfiguration.CODEC));
        BLOCK_PILE = Feature.<FeatureConfiguration, BlockPileFeature>register("block_pile", new BlockPileFeature(BlockPileConfiguration.CODEC));
        SPRING = Feature.<FeatureConfiguration, SpringFeature>register("spring_feature", new SpringFeature(SpringConfiguration.CODEC));
        CHORUS_PLANT = Feature.<FeatureConfiguration, ChorusPlantFeature>register("chorus_plant", new ChorusPlantFeature(NoneFeatureConfiguration.CODEC));
        EMERALD_ORE = Feature.<FeatureConfiguration, ReplaceBlockFeature>register("emerald_ore", new ReplaceBlockFeature(ReplaceBlockConfiguration.CODEC));
        VOID_START_PLATFORM = Feature.<FeatureConfiguration, VoidStartPlatformFeature>register("void_start_platform", new VoidStartPlatformFeature(NoneFeatureConfiguration.CODEC));
        DESERT_WELL = Feature.<FeatureConfiguration, DesertWellFeature>register("desert_well", new DesertWellFeature(NoneFeatureConfiguration.CODEC));
        FOSSIL = Feature.<FeatureConfiguration, FossilFeature>register("fossil", new FossilFeature(NoneFeatureConfiguration.CODEC));
        HUGE_RED_MUSHROOM = Feature.<FeatureConfiguration, HugeRedMushroomFeature>register("huge_red_mushroom", new HugeRedMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
        HUGE_BROWN_MUSHROOM = Feature.<FeatureConfiguration, HugeBrownMushroomFeature>register("huge_brown_mushroom", new HugeBrownMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
        ICE_SPIKE = Feature.<FeatureConfiguration, IceSpikeFeature>register("ice_spike", new IceSpikeFeature(NoneFeatureConfiguration.CODEC));
        GLOWSTONE_BLOB = Feature.<FeatureConfiguration, GlowstoneFeature>register("glowstone_blob", new GlowstoneFeature(NoneFeatureConfiguration.CODEC));
        FREEZE_TOP_LAYER = Feature.<FeatureConfiguration, SnowAndFreezeFeature>register("freeze_top_layer", new SnowAndFreezeFeature(NoneFeatureConfiguration.CODEC));
        VINES = Feature.<FeatureConfiguration, VinesFeature>register("vines", new VinesFeature(NoneFeatureConfiguration.CODEC));
        MONSTER_ROOM = Feature.<FeatureConfiguration, MonsterRoomFeature>register("monster_room", new MonsterRoomFeature(NoneFeatureConfiguration.CODEC));
        BLUE_ICE = Feature.<FeatureConfiguration, BlueIceFeature>register("blue_ice", new BlueIceFeature(NoneFeatureConfiguration.CODEC));
        ICEBERG = Feature.<FeatureConfiguration, IcebergFeature>register("iceberg", new IcebergFeature(BlockStateConfiguration.CODEC));
        FOREST_ROCK = Feature.<FeatureConfiguration, BlockBlobFeature>register("forest_rock", new BlockBlobFeature(BlockStateConfiguration.CODEC));
        DISK = Feature.<FeatureConfiguration, DiskReplaceFeature>register("disk", new DiskReplaceFeature(DiskConfiguration.CODEC));
        ICE_PATCH = Feature.<FeatureConfiguration, IcePatchFeature>register("ice_patch", new IcePatchFeature(DiskConfiguration.CODEC));
        LAKE = Feature.<FeatureConfiguration, LakeFeature>register("lake", new LakeFeature(BlockStateConfiguration.CODEC));
        ORE = Feature.<FeatureConfiguration, OreFeature>register("ore", new OreFeature(OreConfiguration.CODEC));
        END_SPIKE = Feature.<FeatureConfiguration, SpikeFeature>register("end_spike", new SpikeFeature(SpikeConfiguration.CODEC));
        END_ISLAND = Feature.<FeatureConfiguration, EndIslandFeature>register("end_island", new EndIslandFeature(NoneFeatureConfiguration.CODEC));
        END_GATEWAY = Feature.<FeatureConfiguration, EndGatewayFeature>register("end_gateway", new EndGatewayFeature(EndGatewayConfiguration.CODEC));
        SEAGRASS = Feature.<FeatureConfiguration, SeagrassFeature>register("seagrass", new SeagrassFeature(ProbabilityFeatureConfiguration.CODEC));
        KELP = Feature.<FeatureConfiguration, KelpFeature>register("kelp", new KelpFeature(NoneFeatureConfiguration.CODEC));
        CORAL_TREE = Feature.<FeatureConfiguration, CoralTreeFeature>register("coral_tree", new CoralTreeFeature(NoneFeatureConfiguration.CODEC));
        CORAL_MUSHROOM = Feature.<FeatureConfiguration, CoralMushroomFeature>register("coral_mushroom", new CoralMushroomFeature(NoneFeatureConfiguration.CODEC));
        CORAL_CLAW = Feature.<FeatureConfiguration, CoralClawFeature>register("coral_claw", new CoralClawFeature(NoneFeatureConfiguration.CODEC));
        SEA_PICKLE = Feature.<FeatureConfiguration, SeaPickleFeature>register("sea_pickle", new SeaPickleFeature(CountConfiguration.CODEC));
        SIMPLE_BLOCK = Feature.<FeatureConfiguration, SimpleBlockFeature>register("simple_block", new SimpleBlockFeature(SimpleBlockConfiguration.CODEC));
        BAMBOO = Feature.<FeatureConfiguration, BambooFeature>register("bamboo", new BambooFeature(ProbabilityFeatureConfiguration.CODEC));
        HUGE_FUNGUS = Feature.<FeatureConfiguration, HugeFungusFeature>register("huge_fungus", new HugeFungusFeature(HugeFungusConfiguration.CODEC));
        NETHER_FOREST_VEGETATION = Feature.<FeatureConfiguration, NetherForestVegetationFeature>register("nether_forest_vegetation", new NetherForestVegetationFeature(BlockPileConfiguration.CODEC));
        WEEPING_VINES = Feature.<FeatureConfiguration, WeepingVinesFeature>register("weeping_vines", new WeepingVinesFeature(NoneFeatureConfiguration.CODEC));
        TWISTING_VINES = Feature.<FeatureConfiguration, TwistingVinesFeature>register("twisting_vines", new TwistingVinesFeature(NoneFeatureConfiguration.CODEC));
        BASALT_COLUMNS = Feature.<FeatureConfiguration, BasaltColumnsFeature>register("basalt_columns", new BasaltColumnsFeature(ColumnFeatureConfiguration.CODEC));
        DELTA_FEATURE = Feature.<FeatureConfiguration, DeltaFeature>register("delta_feature", new DeltaFeature(DeltaFeatureConfiguration.CODEC));
        REPLACE_BLOBS = Feature.<FeatureConfiguration, ReplaceBlobsFeature>register("netherrack_replace_blobs", new ReplaceBlobsFeature(ReplaceSphereConfiguration.CODEC));
        FILL_LAYER = Feature.<FeatureConfiguration, FillLayerFeature>register("fill_layer", new FillLayerFeature(LayerConfiguration.CODEC));
        BONUS_CHEST = Feature.<FeatureConfiguration, BonusChestFeature>register("bonus_chest", new BonusChestFeature(NoneFeatureConfiguration.CODEC));
        BASALT_PILLAR = Feature.<FeatureConfiguration, BasaltPillarFeature>register("basalt_pillar", new BasaltPillarFeature(NoneFeatureConfiguration.CODEC));
        NO_SURFACE_ORE = Feature.<FeatureConfiguration, NoSurfaceOreFeature>register("no_surface_ore", new NoSurfaceOreFeature(OreConfiguration.CODEC));
        RANDOM_SELECTOR = Feature.<FeatureConfiguration, RandomSelectorFeature>register("random_selector", new RandomSelectorFeature(RandomFeatureConfiguration.CODEC));
        SIMPLE_RANDOM_SELECTOR = Feature.<FeatureConfiguration, SimpleRandomSelectorFeature>register("simple_random_selector", new SimpleRandomSelectorFeature(SimpleRandomFeatureConfiguration.CODEC));
        RANDOM_BOOLEAN_SELECTOR = Feature.<FeatureConfiguration, RandomBooleanSelectorFeature>register("random_boolean_selector", new RandomBooleanSelectorFeature(RandomBooleanFeatureConfiguration.CODEC));
        DECORATED = Feature.<FeatureConfiguration, DecoratedFeature>register("decorated", new DecoratedFeature(DecoratedFeatureConfiguration.CODEC));
    }
}
