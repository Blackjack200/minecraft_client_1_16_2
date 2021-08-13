package net.minecraft.world.level.levelgen.feature.featuresize;

import net.minecraft.core.Registry;
import java.util.Optional;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.OptionalInt;
import com.mojang.serialization.Codec;

public abstract class FeatureSize {
    public static final Codec<FeatureSize> CODEC;
    protected final OptionalInt minClippedHeight;
    
    protected static <S extends FeatureSize> RecordCodecBuilder<S, OptionalInt> minClippedHeightCodec() {
        return (RecordCodecBuilder<S, OptionalInt>)Codec.intRange(0, 80).optionalFieldOf("min_clipped_height").xmap(optional -> (OptionalInt)optional.map(OptionalInt::of).orElse(OptionalInt.empty()), optionalInt -> optionalInt.isPresent() ? Optional.of(optionalInt.getAsInt()) : Optional.empty()).forGetter(cmy -> cmy.minClippedHeight);
    }
    
    public FeatureSize(final OptionalInt optionalInt) {
        this.minClippedHeight = optionalInt;
    }
    
    protected abstract FeatureSizeType<?> type();
    
    public abstract int getSizeAtHeight(final int integer1, final int integer2);
    
    public OptionalInt minClippedHeight() {
        return this.minClippedHeight;
    }
    
    static {
        CODEC = Registry.FEATURE_SIZE_TYPES.dispatch(FeatureSize::type, FeatureSizeType::codec);
    }
}
