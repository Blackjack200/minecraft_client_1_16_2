package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class ReplaceBlockConfiguration implements FeatureConfiguration {
    public static final Codec<ReplaceBlockConfiguration> CODEC;
    public final BlockState target;
    public final BlockState state;
    
    public ReplaceBlockConfiguration(final BlockState cee1, final BlockState cee2) {
        this.target = cee1;
        this.state = cee2;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("target").forGetter(cmm -> cmm.target), (App)BlockState.CODEC.fieldOf("state").forGetter(cmm -> cmm.state)).apply((Applicative)instance, ReplaceBlockConfiguration::new));
    }
}
