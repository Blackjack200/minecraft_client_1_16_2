package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import com.mojang.serialization.Codec;

public class MineshaftConfiguration implements FeatureConfiguration {
    public static final Codec<MineshaftConfiguration> CODEC;
    public final float probability;
    public final MineshaftFeature.Type type;
    
    public MineshaftConfiguration(final float float1, final MineshaftFeature.Type b) {
        this.probability = float1;
        this.type = b;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.floatRange(0.0f, 1.0f).fieldOf("probability").forGetter(cmb -> cmb.probability), (App)MineshaftFeature.Type.CODEC.fieldOf("type").forGetter(cmb -> cmb.type)).apply((Applicative)instance, MineshaftConfiguration::new));
    }
}
