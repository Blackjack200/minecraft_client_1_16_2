package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.levelgen.structure.NetherFossilFeature;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Maps;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.FeatureAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.core.Registry;
import java.util.Locale;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RuinedPortalConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.levelgen.GenerationStep;
import java.util.Map;
import com.google.common.collect.BiMap;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class StructureFeature<C extends FeatureConfiguration> {
    public static final BiMap<String, StructureFeature<?>> STRUCTURES_REGISTRY;
    private static final Map<StructureFeature<?>, GenerationStep.Decoration> STEP;
    private static final Logger LOGGER;
    public static final StructureFeature<JigsawConfiguration> PILLAGER_OUTPOST;
    public static final StructureFeature<MineshaftConfiguration> MINESHAFT;
    public static final StructureFeature<NoneFeatureConfiguration> WOODLAND_MANSION;
    public static final StructureFeature<NoneFeatureConfiguration> JUNGLE_TEMPLE;
    public static final StructureFeature<NoneFeatureConfiguration> DESERT_PYRAMID;
    public static final StructureFeature<NoneFeatureConfiguration> IGLOO;
    public static final StructureFeature<RuinedPortalConfiguration> RUINED_PORTAL;
    public static final StructureFeature<ShipwreckConfiguration> SHIPWRECK;
    public static final SwamplandHutFeature SWAMP_HUT;
    public static final StructureFeature<NoneFeatureConfiguration> STRONGHOLD;
    public static final StructureFeature<NoneFeatureConfiguration> OCEAN_MONUMENT;
    public static final StructureFeature<OceanRuinConfiguration> OCEAN_RUIN;
    public static final StructureFeature<NoneFeatureConfiguration> NETHER_BRIDGE;
    public static final StructureFeature<NoneFeatureConfiguration> END_CITY;
    public static final StructureFeature<ProbabilityFeatureConfiguration> BURIED_TREASURE;
    public static final StructureFeature<JigsawConfiguration> VILLAGE;
    public static final StructureFeature<NoneFeatureConfiguration> NETHER_FOSSIL;
    public static final StructureFeature<JigsawConfiguration> BASTION_REMNANT;
    public static final List<StructureFeature<?>> NOISE_AFFECTING_FEATURES;
    private static final ResourceLocation JIGSAW_RENAME;
    private static final Map<ResourceLocation, ResourceLocation> RENAMES;
    private final Codec<ConfiguredStructureFeature<C, StructureFeature<C>>> configuredStructureCodec;
    
    private static <F extends StructureFeature<?>> F register(final String string, final F ckx, final GenerationStep.Decoration b) {
        StructureFeature.STRUCTURES_REGISTRY.put(string.toLowerCase(Locale.ROOT), ckx);
        StructureFeature.STEP.put(ckx, b);
        return Registry.<F>register(Registry.STRUCTURE_FEATURE, string.toLowerCase(Locale.ROOT), ckx);
    }
    
    public StructureFeature(final Codec<C> codec) {
        this.configuredStructureCodec = (Codec<ConfiguredStructureFeature<C, StructureFeature<C>>>)codec.fieldOf("config").xmap(clx -> new ConfiguredStructureFeature((F)this, (FC)clx), cit -> cit.config).codec();
    }
    
    public GenerationStep.Decoration step() {
        return (GenerationStep.Decoration)StructureFeature.STEP.get(this);
    }
    
    public static void bootstrap() {
    }
    
    @Nullable
    public static StructureStart<?> loadStaticStart(final StructureManager cst, final CompoundTag md, final long long3) {
        final String string5 = md.getString("id");
        if ("INVALID".equals(string5)) {
            return StructureStart.INVALID_START;
        }
        final StructureFeature<?> ckx6 = Registry.STRUCTURE_FEATURE.get(new ResourceLocation(string5.toLowerCase(Locale.ROOT)));
        if (ckx6 == null) {
            StructureFeature.LOGGER.error("Unknown feature id: {}", string5);
            return null;
        }
        final int integer7 = md.getInt("ChunkX");
        final int integer8 = md.getInt("ChunkZ");
        final int integer9 = md.getInt("references");
        final BoundingBox cqx10 = md.contains("BB") ? new BoundingBox(md.getIntArray("BB")) : BoundingBox.getUnknownBox();
        final ListTag mj11 = md.getList("Children", 10);
        try {
            final StructureStart<?> crs12 = ckx6.createStart(integer7, integer8, cqx10, integer9, long3);
            for (int integer10 = 0; integer10 < mj11.size(); ++integer10) {
                final CompoundTag md2 = mj11.getCompound(integer10);
                final String string6 = md2.getString("id").toLowerCase(Locale.ROOT);
                final ResourceLocation vk16 = new ResourceLocation(string6);
                final ResourceLocation vk17 = (ResourceLocation)StructureFeature.RENAMES.getOrDefault(vk16, vk16);
                final StructurePieceType cky18 = Registry.STRUCTURE_PIECE.get(vk17);
                if (cky18 == null) {
                    StructureFeature.LOGGER.error("Unknown structure piece id: {}", vk17);
                }
                else {
                    try {
                        final StructurePiece crr19 = cky18.load(cst, md2);
                        crs12.getPieces().add(crr19);
                    }
                    catch (Exception exception19) {
                        StructureFeature.LOGGER.error("Exception loading structure piece with id {}", vk17, exception19);
                    }
                }
            }
            return crs12;
        }
        catch (Exception exception20) {
            StructureFeature.LOGGER.error("Failed Start with id {}", string5, exception20);
            return null;
        }
    }
    
    public Codec<ConfiguredStructureFeature<C, StructureFeature<C>>> configuredStructureCodec() {
        return this.configuredStructureCodec;
    }
    
    public ConfiguredStructureFeature<C, ? extends StructureFeature<C>> configured(final C clx) {
        return new ConfiguredStructureFeature<C, StructureFeature<C>>((StructureFeature<C>)this, clx);
    }
    
    @Nullable
    public BlockPos getNearestGeneratedFeature(final LevelReader brw, final StructureFeatureManager bsk, final BlockPos fx, final int integer, final boolean boolean5, final long long6, final StructureFeatureConfiguration cmv) {
        final int integer2 = cmv.spacing();
        final int integer3 = fx.getX() >> 4;
        final int integer4 = fx.getZ() >> 4;
        int integer5 = 0;
        final WorldgenRandom chu14 = new WorldgenRandom();
        while (integer5 <= integer) {
            for (int integer6 = -integer5; integer6 <= integer5; ++integer6) {
                final boolean boolean6 = integer6 == -integer5 || integer6 == integer5;
                for (int integer7 = -integer5; integer7 <= integer5; ++integer7) {
                    final boolean boolean7 = integer7 == -integer5 || integer7 == integer5;
                    if (boolean6 || boolean7) {
                        final int integer8 = integer3 + integer2 * integer6;
                        final int integer9 = integer4 + integer2 * integer7;
                        final ChunkPos bra21 = this.getPotentialFeatureChunk(cmv, long6, chu14, integer8, integer9);
                        final ChunkAccess cft22 = brw.getChunk(bra21.x, bra21.z, ChunkStatus.STRUCTURE_STARTS);
                        final StructureStart<?> crs23 = bsk.getStartForFeature(SectionPos.of(cft22.getPos(), 0), this, cft22);
                        if (crs23 != null && crs23.isValid()) {
                            if (boolean5 && crs23.canBeReferenced()) {
                                crs23.addReference();
                                return crs23.getLocatePos();
                            }
                            if (!boolean5) {
                                return crs23.getLocatePos();
                            }
                        }
                        if (integer5 == 0) {
                            break;
                        }
                    }
                }
                if (integer5 == 0) {
                    break;
                }
            }
            ++integer5;
        }
        return null;
    }
    
    protected boolean linearSeparation() {
        return true;
    }
    
    public final ChunkPos getPotentialFeatureChunk(final StructureFeatureConfiguration cmv, final long long2, final WorldgenRandom chu, final int integer4, final int integer5) {
        final int integer6 = cmv.spacing();
        final int integer7 = cmv.separation();
        final int integer8 = Math.floorDiv(integer4, integer6);
        final int integer9 = Math.floorDiv(integer5, integer6);
        chu.setLargeFeatureWithSalt(long2, integer8, integer9, cmv.salt());
        int integer10;
        int integer11;
        if (this.linearSeparation()) {
            integer10 = chu.nextInt(integer6 - integer7);
            integer11 = chu.nextInt(integer6 - integer7);
        }
        else {
            integer10 = (chu.nextInt(integer6 - integer7) + chu.nextInt(integer6 - integer7)) / 2;
            integer11 = (chu.nextInt(integer6 - integer7) + chu.nextInt(integer6 - integer7)) / 2;
        }
        return new ChunkPos(integer8 * integer6 + integer10, integer9 * integer6 + integer11);
    }
    
    protected boolean isFeatureChunk(final ChunkGenerator cfv, final BiomeSource bsv, final long long3, final WorldgenRandom chu, final int integer5, final int integer6, final Biome bss, final ChunkPos bra, final C clx) {
        return true;
    }
    
    private StructureStart<C> createStart(final int integer1, final int integer2, final BoundingBox cqx, final int integer4, final long long5) {
        return this.getStartFactory().create(this, integer1, integer2, cqx, integer4, long5);
    }
    
    public StructureStart<?> generate(final RegistryAccess gn, final ChunkGenerator cfv, final BiomeSource bsv, final StructureManager cst, final long long5, final ChunkPos bra, final Biome bss, final int integer, final WorldgenRandom chu, final StructureFeatureConfiguration cmv, final C clx) {
        final ChunkPos bra2 = this.getPotentialFeatureChunk(cmv, long5, chu, bra.x, bra.z);
        if (bra.x == bra2.x && bra.z == bra2.z && this.isFeatureChunk(cfv, bsv, long5, chu, bra.x, bra.z, bss, bra2, clx)) {
            final StructureStart<C> crs15 = this.createStart(bra.x, bra.z, BoundingBox.getUnknownBox(), integer, long5);
            crs15.generatePieces(gn, cfv, cst, bra.x, bra.z, bss, clx);
            if (crs15.isValid()) {
                return crs15;
            }
        }
        return StructureStart.INVALID_START;
    }
    
    public abstract StructureStartFactory<C> getStartFactory();
    
    public String getFeatureName() {
        return (String)StructureFeature.STRUCTURES_REGISTRY.inverse().get(this);
    }
    
    public List<MobSpawnSettings.SpawnerData> getSpecialEnemies() {
        return (List<MobSpawnSettings.SpawnerData>)ImmutableList.of();
    }
    
    public List<MobSpawnSettings.SpawnerData> getSpecialAnimals() {
        return (List<MobSpawnSettings.SpawnerData>)ImmutableList.of();
    }
    
    static {
        STRUCTURES_REGISTRY = (BiMap)HashBiMap.create();
        STEP = (Map)Maps.newHashMap();
        LOGGER = LogManager.getLogger();
        PILLAGER_OUTPOST = StructureFeature.<PillagerOutpostFeature>register("Pillager_Outpost", new PillagerOutpostFeature(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        MINESHAFT = StructureFeature.<MineshaftFeature>register("Mineshaft", new MineshaftFeature(MineshaftConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
        WOODLAND_MANSION = StructureFeature.<WoodlandMansionFeature>register("Mansion", new WoodlandMansionFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        JUNGLE_TEMPLE = StructureFeature.<JunglePyramidFeature>register("Jungle_Pyramid", new JunglePyramidFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        DESERT_PYRAMID = StructureFeature.<DesertPyramidFeature>register("Desert_Pyramid", new DesertPyramidFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        IGLOO = StructureFeature.<IglooFeature>register("Igloo", new IglooFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        RUINED_PORTAL = StructureFeature.<RuinedPortalFeature>register("Ruined_Portal", new RuinedPortalFeature(RuinedPortalConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        SHIPWRECK = StructureFeature.<ShipwreckFeature>register("Shipwreck", new ShipwreckFeature(ShipwreckConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        SWAMP_HUT = StructureFeature.<SwamplandHutFeature>register("Swamp_Hut", new SwamplandHutFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        STRONGHOLD = StructureFeature.<StrongholdFeature>register("Stronghold", new StrongholdFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.STRONGHOLDS);
        OCEAN_MONUMENT = StructureFeature.<OceanMonumentFeature>register("Monument", new OceanMonumentFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        OCEAN_RUIN = StructureFeature.<OceanRuinFeature>register("Ocean_Ruin", new OceanRuinFeature(OceanRuinConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        NETHER_BRIDGE = StructureFeature.<NetherFortressFeature>register("Fortress", new NetherFortressFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_DECORATION);
        END_CITY = StructureFeature.<EndCityFeature>register("EndCity", new EndCityFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        BURIED_TREASURE = StructureFeature.<BuriedTreasureFeature>register("Buried_Treasure", new BuriedTreasureFeature(ProbabilityFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES);
        VILLAGE = StructureFeature.<VillageFeature>register("Village", new VillageFeature(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        NETHER_FOSSIL = StructureFeature.<NetherFossilFeature>register("Nether_Fossil", new NetherFossilFeature(NoneFeatureConfiguration.CODEC), GenerationStep.Decoration.UNDERGROUND_DECORATION);
        BASTION_REMNANT = StructureFeature.<BastionFeature>register("Bastion_Remnant", new BastionFeature(JigsawConfiguration.CODEC), GenerationStep.Decoration.SURFACE_STRUCTURES);
        NOISE_AFFECTING_FEATURES = (List)ImmutableList.of(StructureFeature.PILLAGER_OUTPOST, StructureFeature.VILLAGE, StructureFeature.NETHER_FOSSIL);
        JIGSAW_RENAME = new ResourceLocation("jigsaw");
        RENAMES = (Map)ImmutableMap.builder().put(new ResourceLocation("nvi"), StructureFeature.JIGSAW_RENAME).put(new ResourceLocation("pcp"), StructureFeature.JIGSAW_RENAME).put(new ResourceLocation("bastionremnant"), StructureFeature.JIGSAW_RENAME).put(new ResourceLocation("runtime"), StructureFeature.JIGSAW_RENAME).build();
    }
    
    public interface StructureStartFactory<C extends FeatureConfiguration> {
        StructureStart<C> create(final StructureFeature<C> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6);
    }
}
