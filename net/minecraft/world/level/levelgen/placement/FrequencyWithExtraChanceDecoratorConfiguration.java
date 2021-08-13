package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class FrequencyWithExtraChanceDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<FrequencyWithExtraChanceDecoratorConfiguration> CODEC;
    public final int count;
    public final float extraChance;
    public final int extraCount;
    
    public FrequencyWithExtraChanceDecoratorConfiguration(final int integer1, final float float2, final int integer3) {
        this.count = integer1;
        this.extraChance = float2;
        this.extraCount = integer3;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("count").forGetter(cqa -> cqa.count), (App)Codec.FLOAT.fieldOf("extra_chance").forGetter(cqa -> cqa.extraChance), (App)Codec.INT.fieldOf("extra_count").forGetter(cqa -> cqa.extraCount)).apply((Applicative)instance, FrequencyWithExtraChanceDecoratorConfiguration::new));
    }
}
