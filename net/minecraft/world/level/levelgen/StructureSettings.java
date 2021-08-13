package net.minecraft.world.level.levelgen;

import java.util.Iterator;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Keyable;
import net.minecraft.core.Registry;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.google.common.collect.Maps;
import java.util.Optional;
import javax.annotation.Nullable;
import java.util.Map;
import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;

public class StructureSettings {
    public static final Codec<StructureSettings> CODEC;
    public static final ImmutableMap<StructureFeature<?>, StructureFeatureConfiguration> DEFAULTS;
    public static final StrongholdConfiguration DEFAULT_STRONGHOLD;
    private final Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig;
    @Nullable
    private final StrongholdConfiguration stronghold;
    
    public StructureSettings(final Optional<StrongholdConfiguration> optional, final Map<StructureFeature<?>, StructureFeatureConfiguration> map) {
        this.stronghold = (StrongholdConfiguration)optional.orElse(null);
        this.structureConfig = map;
    }
    
    public StructureSettings(final boolean boolean1) {
        this.structureConfig = (Map<StructureFeature<?>, StructureFeatureConfiguration>)Maps.newHashMap((Map)StructureSettings.DEFAULTS);
        this.stronghold = (boolean1 ? StructureSettings.DEFAULT_STRONGHOLD : null);
    }
    
    public Map<StructureFeature<?>, StructureFeatureConfiguration> structureConfig() {
        return this.structureConfig;
    }
    
    @Nullable
    public StructureFeatureConfiguration getConfig(final StructureFeature<?> ckx) {
        return (StructureFeatureConfiguration)this.structureConfig.get(ckx);
    }
    
    @Nullable
    public StrongholdConfiguration stronghold() {
        return this.stronghold;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)StrongholdConfiguration.CODEC.optionalFieldOf("stronghold").forGetter(chs -> Optional.ofNullable(chs.stronghold)), (App)Codec.simpleMap((Codec)Registry.STRUCTURE_FEATURE, (Codec)StructureFeatureConfiguration.CODEC, (Keyable)Registry.STRUCTURE_FEATURE).fieldOf("structures").forGetter(chs -> chs.structureConfig)).apply((Applicative)instance, StructureSettings::new));
        DEFAULTS = ImmutableMap.builder().put(StructureFeature.VILLAGE, new StructureFeatureConfiguration(32, 8, 10387312)).put(StructureFeature.DESERT_PYRAMID, new StructureFeatureConfiguration(32, 8, 14357617)).put(StructureFeature.IGLOO, new StructureFeatureConfiguration(32, 8, 14357618)).put(StructureFeature.JUNGLE_TEMPLE, new StructureFeatureConfiguration(32, 8, 14357619)).put(StructureFeature.SWAMP_HUT, new StructureFeatureConfiguration(32, 8, 14357620)).put(StructureFeature.PILLAGER_OUTPOST, new StructureFeatureConfiguration(32, 8, 165745296)).put(StructureFeature.STRONGHOLD, new StructureFeatureConfiguration(1, 0, 0)).put(StructureFeature.OCEAN_MONUMENT, new StructureFeatureConfiguration(32, 5, 10387313)).put(StructureFeature.END_CITY, new StructureFeatureConfiguration(20, 11, 10387313)).put(StructureFeature.WOODLAND_MANSION, new StructureFeatureConfiguration(80, 20, 10387319)).put(StructureFeature.BURIED_TREASURE, new StructureFeatureConfiguration(1, 0, 0)).put(StructureFeature.MINESHAFT, new StructureFeatureConfiguration(1, 0, 0)).put(StructureFeature.RUINED_PORTAL, new StructureFeatureConfiguration(40, 15, 34222645)).put(StructureFeature.SHIPWRECK, new StructureFeatureConfiguration(24, 4, 165745295)).put(StructureFeature.OCEAN_RUIN, new StructureFeatureConfiguration(20, 8, 14357621)).put(StructureFeature.BASTION_REMNANT, new StructureFeatureConfiguration(27, 4, 30084232)).put(StructureFeature.NETHER_BRIDGE, new StructureFeatureConfiguration(27, 4, 30084232)).put(StructureFeature.NETHER_FOSSIL, new StructureFeatureConfiguration(2, 1, 14357921)).build();
        for (final StructureFeature<?> ckx2 : Registry.STRUCTURE_FEATURE) {
            if (!StructureSettings.DEFAULTS.containsKey(ckx2)) {
                throw new IllegalStateException(new StringBuilder().append("Structure feature without default settings: ").append(Registry.STRUCTURE_FEATURE.getKey(ckx2)).toString());
            }
        }
        DEFAULT_STRONGHOLD = new StrongholdConfiguration(32, 3, 128);
    }
}
