package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
import com.mojang.serialization.Codec;

public class OceanRuinConfiguration implements FeatureConfiguration {
    public static final Codec<OceanRuinConfiguration> CODEC;
    public final OceanRuinFeature.Type biomeTemp;
    public final float largeProbability;
    public final float clusterProbability;
    
    public OceanRuinConfiguration(final OceanRuinFeature.Type b, final float float2, final float float3) {
        this.biomeTemp = b;
        this.largeProbability = float2;
        this.clusterProbability = float3;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)OceanRuinFeature.Type.CODEC.fieldOf("biome_temp").forGetter(cmf -> cmf.biomeTemp), (App)Codec.floatRange(0.0f, 1.0f).fieldOf("large_probability").forGetter(cmf -> cmf.largeProbability), (App)Codec.floatRange(0.0f, 1.0f).fieldOf("cluster_probability").forGetter(cmf -> cmf.clusterProbability)).apply((Applicative)instance, OceanRuinConfiguration::new));
    }
}
