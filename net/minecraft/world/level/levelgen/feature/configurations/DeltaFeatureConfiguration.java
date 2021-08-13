package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class DeltaFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<DeltaFeatureConfiguration> CODEC;
    private final BlockState contents;
    private final BlockState rim;
    private final UniformInt size;
    private final UniformInt rimSize;
    
    public DeltaFeatureConfiguration(final BlockState cee1, final BlockState cee2, final UniformInt aft3, final UniformInt aft4) {
        this.contents = cee1;
        this.rim = cee2;
        this.size = aft3;
        this.rimSize = aft4;
    }
    
    public BlockState contents() {
        return this.contents;
    }
    
    public BlockState rim() {
        return this.rim;
    }
    
    public UniformInt size() {
        return this.size;
    }
    
    public UniformInt rimSize() {
        return this.rimSize;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)BlockState.CODEC.fieldOf("contents").forGetter(clu -> clu.contents), (App)BlockState.CODEC.fieldOf("rim").forGetter(clu -> clu.rim), (App)UniformInt.codec(0, 8, 8).fieldOf("size").forGetter(clu -> clu.size), (App)UniformInt.codec(0, 8, 8).fieldOf("rim_size").forGetter(clu -> clu.rimSize)).apply((Applicative)instance, DeltaFeatureConfiguration::new));
    }
}
