package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.GenerationStep;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class CarvingMaskDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<CarvingMaskDecoratorConfiguration> CODEC;
    protected final GenerationStep.Carving step;
    protected final float probability;
    
    public CarvingMaskDecoratorConfiguration(final GenerationStep.Carving a, final float float2) {
        this.step = a;
        this.probability = float2;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)GenerationStep.Carving.CODEC.fieldOf("step").forGetter(cpi -> cpi.step), (App)Codec.FLOAT.fieldOf("probability").forGetter(cpi -> cpi.probability)).apply((Applicative)instance, CarvingMaskDecoratorConfiguration::new));
    }
}
