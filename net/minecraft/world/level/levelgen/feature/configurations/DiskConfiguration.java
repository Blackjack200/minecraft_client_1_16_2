package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class DiskConfiguration implements FeatureConfiguration {
    public static final Codec<DiskConfiguration> CODEC;
    public final BlockState state;
    public final UniformInt radius;
    public final int halfHeight;
    public final List<BlockState> targets;
    
    public DiskConfiguration(final BlockState cee, final UniformInt aft, final int integer, final List<BlockState> list) {
        this.state = cee;
        this.radius = aft;
        this.halfHeight = integer;
        this.targets = list;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("state").forGetter(clv -> clv.state), (App)UniformInt.codec(0, 4, 4).fieldOf("radius").forGetter(clv -> clv.radius), (App)Codec.intRange(0, 4).fieldOf("half_height").forGetter(clv -> clv.halfHeight), (App)BlockState.CODEC.listOf().fieldOf("targets").forGetter(clv -> clv.targets)).apply((Applicative)instance, DiskConfiguration::new));
    }
}
