package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class ReplaceSphereConfiguration implements FeatureConfiguration {
    public static final Codec<ReplaceSphereConfiguration> CODEC;
    public final BlockState targetState;
    public final BlockState replaceState;
    private final UniformInt radius;
    
    public ReplaceSphereConfiguration(final BlockState cee1, final BlockState cee2, final UniformInt aft) {
        this.targetState = cee1;
        this.replaceState = cee2;
        this.radius = aft;
    }
    
    public UniformInt radius() {
        return this.radius;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("target").forGetter(cmn -> cmn.targetState), (App)BlockState.CODEC.fieldOf("state").forGetter(cmn -> cmn.replaceState), (App)UniformInt.CODEC.fieldOf("radius").forGetter(cmn -> cmn.radius)).apply((Applicative)instance, ReplaceSphereConfiguration::new));
    }
}
