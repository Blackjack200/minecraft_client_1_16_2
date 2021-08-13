package net.minecraft.world.level.biome;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.Random;
import java.util.function.Predicate;
import java.util.List;
import com.google.common.collect.ImmutableList;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class FixedBiomeSource extends BiomeSource {
    public static final Codec<FixedBiomeSource> CODEC;
    private final Supplier<Biome> biome;
    
    public FixedBiomeSource(final Biome bss) {
        this((Supplier<Biome>)(() -> bss));
    }
    
    public FixedBiomeSource(final Supplier<Biome> supplier) {
        super((List<Biome>)ImmutableList.of(supplier.get()));
        this.biome = supplier;
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return FixedBiomeSource.CODEC;
    }
    
    @Override
    public BiomeSource withSeed(final long long1) {
        return this;
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        return (Biome)this.biome.get();
    }
    
    @Nullable
    @Override
    public BlockPos findBiomeHorizontal(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final Predicate<Biome> predicate, final Random random, final boolean boolean8) {
        if (!predicate.test(this.biome.get())) {
            return null;
        }
        if (boolean8) {
            return new BlockPos(integer1, integer2, integer3);
        }
        return new BlockPos(integer1 - integer4 + random.nextInt(integer4 * 2 + 1), integer2, integer3 - integer4 + random.nextInt(integer4 * 2 + 1));
    }
    
    @Override
    public Set<Biome> getBiomesWithin(final int integer1, final int integer2, final int integer3, final int integer4) {
        return (Set<Biome>)Sets.newHashSet((Object[])new Biome[] { (Biome)this.biome.get() });
    }
    
    static {
        CODEC = Biome.CODEC.fieldOf("biome").xmap(FixedBiomeSource::new, bta -> bta.biome).stable().codec();
    }
}
