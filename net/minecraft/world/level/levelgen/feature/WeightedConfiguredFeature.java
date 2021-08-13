package net.minecraft.world.level.levelgen.feature;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class WeightedConfiguredFeature {
    public static final Codec<WeightedConfiguredFeature> CODEC;
    public final Supplier<ConfiguredFeature<?, ?>> feature;
    public final float chance;
    
    public WeightedConfiguredFeature(final ConfiguredFeature<?, ?> cis, final float float2) {
        this((Supplier<ConfiguredFeature<?, ?>>)(() -> cis), float2);
    }
    
    private WeightedConfiguredFeature(final Supplier<ConfiguredFeature<?, ?>> supplier, final float float2) {
        this.feature = supplier;
        this.chance = float2;
    }
    
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx) {
        return ((ConfiguredFeature)this.feature.get()).place(bso, cfv, random, fx);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)ConfiguredFeature.CODEC.fieldOf("feature").forGetter(clg -> clg.feature), (App)Codec.floatRange(0.0f, 1.0f).fieldOf("chance").forGetter(clg -> clg.chance)).apply((Applicative)instance, WeightedConfiguredFeature::new));
    }
}
