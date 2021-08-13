package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class DepthAverageConfigation implements DecoratorConfiguration {
    public static final Codec<DepthAverageConfigation> CODEC;
    public final int baseline;
    public final int spread;
    
    public DepthAverageConfigation(final int integer1, final int integer2) {
        this.baseline = integer1;
        this.spread = integer2;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.INT.fieldOf("baseline").forGetter(cpt -> cpt.baseline), (App)Codec.INT.fieldOf("spread").forGetter(cpt -> cpt.spread)).apply((Applicative)instance, DepthAverageConfigation::new));
    }
}
