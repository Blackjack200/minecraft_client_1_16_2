package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Codec;

public class StructureFeatureConfiguration {
    public static final Codec<StructureFeatureConfiguration> CODEC;
    private final int spacing;
    private final int separation;
    private final int salt;
    
    public StructureFeatureConfiguration(final int integer1, final int integer2, final int integer3) {
        this.spacing = integer1;
        this.separation = integer2;
        this.salt = integer3;
    }
    
    public int spacing() {
        return this.spacing;
    }
    
    public int separation() {
        return this.separation;
    }
    
    public int salt() {
        return this.salt;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.intRange(0, 4096).fieldOf("spacing").forGetter(cmv -> cmv.spacing), (App)Codec.intRange(0, 4096).fieldOf("separation").forGetter(cmv -> cmv.separation), (App)Codec.intRange(0, Integer.MAX_VALUE).fieldOf("salt").forGetter(cmv -> cmv.salt)).apply((Applicative)instance, StructureFeatureConfiguration::new)).comapFlatMap(cmv -> {
            if (cmv.spacing <= cmv.separation) {
                return DataResult.error("Spacing has to be smaller than separation");
            }
            return DataResult.success(cmv);
        }, Function.identity());
    }
}
