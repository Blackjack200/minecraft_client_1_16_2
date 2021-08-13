package net.minecraft.world.level.levelgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class ChanceDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<ChanceDecoratorConfiguration> CODEC;
    public final int chance;
    
    public ChanceDecoratorConfiguration(final int integer) {
        this.chance = integer;
    }
    
    static {
        CODEC = Codec.INT.fieldOf("chance").xmap(ChanceDecoratorConfiguration::new, cpk -> cpk.chance).codec();
    }
}
