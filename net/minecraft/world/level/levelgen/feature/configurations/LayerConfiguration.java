package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class LayerConfiguration implements FeatureConfiguration {
    public static final Codec<LayerConfiguration> CODEC;
    public final int height;
    public final BlockState state;
    
    public LayerConfiguration(final int integer, final BlockState cee) {
        this.height = integer;
        this.state = cee;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.intRange(0, 255).fieldOf("height").forGetter(cma -> cma.height), (App)BlockState.CODEC.fieldOf("state").forGetter(cma -> cma.state)).apply((Applicative)instance, LayerConfiguration::new));
    }
}
