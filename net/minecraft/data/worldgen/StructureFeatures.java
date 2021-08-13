package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.RuinedPortalFeature;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RuinedPortalConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ShipwreckConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

public class StructureFeatures {
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> PILLAGER_OUTPOST;
    public static final ConfiguredStructureFeature<MineshaftConfiguration, ? extends StructureFeature<MineshaftConfiguration>> MINESHAFT;
    public static final ConfiguredStructureFeature<MineshaftConfiguration, ? extends StructureFeature<MineshaftConfiguration>> MINESHAFT_MESA;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> WOODLAND_MANSION;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> JUNGLE_TEMPLE;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> DESERT_PYRAMID;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> IGLOO;
    public static final ConfiguredStructureFeature<ShipwreckConfiguration, ? extends StructureFeature<ShipwreckConfiguration>> SHIPWRECK;
    public static final ConfiguredStructureFeature<ShipwreckConfiguration, ? extends StructureFeature<ShipwreckConfiguration>> SHIPWRECH_BEACHED;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> SWAMP_HUT;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> STRONGHOLD;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> OCEAN_MONUMENT;
    public static final ConfiguredStructureFeature<OceanRuinConfiguration, ? extends StructureFeature<OceanRuinConfiguration>> OCEAN_RUIN_COLD;
    public static final ConfiguredStructureFeature<OceanRuinConfiguration, ? extends StructureFeature<OceanRuinConfiguration>> OCEAN_RUIN_WARM;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> NETHER_BRIDGE;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> NETHER_FOSSIL;
    public static final ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> END_CITY;
    public static final ConfiguredStructureFeature<ProbabilityFeatureConfiguration, ? extends StructureFeature<ProbabilityFeatureConfiguration>> BURIED_TREASURE;
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> BASTION_REMNANT;
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> VILLAGE_PLAINS;
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> VILLAGE_DESERT;
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> VILLAGE_SAVANNA;
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> VILLAGE_SNOWY;
    public static final ConfiguredStructureFeature<JigsawConfiguration, ? extends StructureFeature<JigsawConfiguration>> VILLAGE_TAIGA;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_STANDARD;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_DESERT;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_JUNGLE;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_SWAMP;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_MOUNTAIN;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_OCEAN;
    public static final ConfiguredStructureFeature<RuinedPortalConfiguration, ? extends StructureFeature<RuinedPortalConfiguration>> RUINED_PORTAL_NETHER;
    
    private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> ConfiguredStructureFeature<FC, F> register(final String string, final ConfiguredStructureFeature<FC, F> cit) {
        return BuiltinRegistries.<ConfiguredStructureFeature<FC, F>>register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, string, cit);
    }
    
    static {
        PILLAGER_OUTPOST = StructureFeatures.register("pillager_outpost", StructureFeature.PILLAGER_OUTPOST.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> PillagerOutpostPools.START), 7)));
        MINESHAFT = StructureFeatures.register("mineshaft", StructureFeature.MINESHAFT.configured(new MineshaftConfiguration(0.004f, MineshaftFeature.Type.NORMAL)));
        MINESHAFT_MESA = StructureFeatures.register("mineshaft_mesa", StructureFeature.MINESHAFT.configured(new MineshaftConfiguration(0.004f, MineshaftFeature.Type.MESA)));
        WOODLAND_MANSION = StructureFeatures.register("mansion", StructureFeature.WOODLAND_MANSION.configured(NoneFeatureConfiguration.INSTANCE));
        JUNGLE_TEMPLE = StructureFeatures.register("jungle_pyramid", StructureFeature.JUNGLE_TEMPLE.configured(NoneFeatureConfiguration.INSTANCE));
        DESERT_PYRAMID = StructureFeatures.register("desert_pyramid", StructureFeature.DESERT_PYRAMID.configured(NoneFeatureConfiguration.INSTANCE));
        IGLOO = StructureFeatures.register("igloo", StructureFeature.IGLOO.configured(NoneFeatureConfiguration.INSTANCE));
        SHIPWRECK = StructureFeatures.register("shipwreck", StructureFeature.SHIPWRECK.configured(new ShipwreckConfiguration(false)));
        SHIPWRECH_BEACHED = StructureFeatures.register("shipwreck_beached", StructureFeature.SHIPWRECK.configured(new ShipwreckConfiguration(true)));
        SWAMP_HUT = StructureFeatures.register("swamp_hut", StructureFeature.SWAMP_HUT.configured(NoneFeatureConfiguration.INSTANCE));
        STRONGHOLD = StructureFeatures.register("stronghold", StructureFeature.STRONGHOLD.configured(NoneFeatureConfiguration.INSTANCE));
        OCEAN_MONUMENT = StructureFeatures.register("monument", StructureFeature.OCEAN_MONUMENT.configured(NoneFeatureConfiguration.INSTANCE));
        OCEAN_RUIN_COLD = StructureFeatures.register("ocean_ruin_cold", StructureFeature.OCEAN_RUIN.configured(new OceanRuinConfiguration(OceanRuinFeature.Type.COLD, 0.3f, 0.9f)));
        OCEAN_RUIN_WARM = StructureFeatures.register("ocean_ruin_warm", StructureFeature.OCEAN_RUIN.configured(new OceanRuinConfiguration(OceanRuinFeature.Type.WARM, 0.3f, 0.9f)));
        NETHER_BRIDGE = StructureFeatures.register("fortress", StructureFeature.NETHER_BRIDGE.configured(NoneFeatureConfiguration.INSTANCE));
        NETHER_FOSSIL = StructureFeatures.register("nether_fossil", StructureFeature.NETHER_FOSSIL.configured(NoneFeatureConfiguration.INSTANCE));
        END_CITY = StructureFeatures.register("end_city", StructureFeature.END_CITY.configured(NoneFeatureConfiguration.INSTANCE));
        BURIED_TREASURE = StructureFeatures.register("buried_treasure", StructureFeature.BURIED_TREASURE.configured(new ProbabilityFeatureConfiguration(0.01f)));
        BASTION_REMNANT = StructureFeatures.register("bastion_remnant", StructureFeature.BASTION_REMNANT.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> BastionPieces.START), 6)));
        VILLAGE_PLAINS = StructureFeatures.register("village_plains", StructureFeature.VILLAGE.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> PlainVillagePools.START), 6)));
        VILLAGE_DESERT = StructureFeatures.register("village_desert", StructureFeature.VILLAGE.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> DesertVillagePools.START), 6)));
        VILLAGE_SAVANNA = StructureFeatures.register("village_savanna", StructureFeature.VILLAGE.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> SavannaVillagePools.START), 6)));
        VILLAGE_SNOWY = StructureFeatures.register("village_snowy", StructureFeature.VILLAGE.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> SnowyVillagePools.START), 6)));
        VILLAGE_TAIGA = StructureFeatures.register("village_taiga", StructureFeature.VILLAGE.configured(new JigsawConfiguration((Supplier<StructureTemplatePool>)(() -> TaigaVillagePools.START), 6)));
        RUINED_PORTAL_STANDARD = StructureFeatures.register("ruined_portal", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.STANDARD)));
        RUINED_PORTAL_DESERT = StructureFeatures.register("ruined_portal_desert", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.DESERT)));
        RUINED_PORTAL_JUNGLE = StructureFeatures.register("ruined_portal_jungle", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.JUNGLE)));
        RUINED_PORTAL_SWAMP = StructureFeatures.register("ruined_portal_swamp", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.SWAMP)));
        RUINED_PORTAL_MOUNTAIN = StructureFeatures.register("ruined_portal_mountain", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.MOUNTAIN)));
        RUINED_PORTAL_OCEAN = StructureFeatures.register("ruined_portal_ocean", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.OCEAN)));
        RUINED_PORTAL_NETHER = StructureFeatures.register("ruined_portal_nether", StructureFeature.RUINED_PORTAL.configured(new RuinedPortalConfiguration(RuinedPortalFeature.Type.NETHER)));
    }
}
