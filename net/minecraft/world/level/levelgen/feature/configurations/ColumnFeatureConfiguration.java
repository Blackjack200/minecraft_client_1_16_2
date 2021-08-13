package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public class ColumnFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<ColumnFeatureConfiguration> CODEC;
    private final UniformInt reach;
    private final UniformInt height;
    
    public ColumnFeatureConfiguration(final UniformInt aft1, final UniformInt aft2) {
        this.reach = aft1;
        this.height = aft2;
    }
    
    public UniformInt reach() {
        return this.reach;
    }
    
    public UniformInt height() {
        return this.height;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)UniformInt.codec(0, 2, 1).fieldOf("reach").forGetter(clq -> clq.reach), (App)UniformInt.codec(1, 5, 5).fieldOf("height").forGetter(clq -> clq.height)).apply((Applicative)instance, ColumnFeatureConfiguration::new));
    }
}
