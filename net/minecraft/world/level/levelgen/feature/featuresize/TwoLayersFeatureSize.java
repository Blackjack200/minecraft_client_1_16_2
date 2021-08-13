package net.minecraft.world.level.levelgen.feature.featuresize;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import com.mojang.serialization.Codec;

public class TwoLayersFeatureSize extends FeatureSize {
    public static final Codec<TwoLayersFeatureSize> CODEC;
    private final int limit;
    private final int lowerSize;
    private final int upperSize;
    
    public TwoLayersFeatureSize(final int integer1, final int integer2, final int integer3) {
        this(integer1, integer2, integer3, OptionalInt.empty());
    }
    
    public TwoLayersFeatureSize(final int integer1, final int integer2, final int integer3, final OptionalInt optionalInt) {
        super(optionalInt);
        this.limit = integer1;
        this.lowerSize = integer2;
        this.upperSize = integer3;
    }
    
    @Override
    protected FeatureSizeType<?> type() {
        return FeatureSizeType.TWO_LAYERS_FEATURE_SIZE;
    }
    
    @Override
    public int getSizeAtHeight(final int integer1, final int integer2) {
        return (integer2 < this.limit) ? this.lowerSize : this.upperSize;
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Codec.intRange(0, 81).fieldOf("limit").orElse(1).forGetter(cnb -> cnb.limit), (App)Codec.intRange(0, 16).fieldOf("lower_size").orElse(0).forGetter(cnb -> cnb.lowerSize), (App)Codec.intRange(0, 16).fieldOf("upper_size").orElse(1).forGetter(cnb -> cnb.upperSize), (App)FeatureSize.<FeatureSize>minClippedHeightCodec()).apply((Applicative)instance, TwoLayersFeatureSize::new));
    }
}
