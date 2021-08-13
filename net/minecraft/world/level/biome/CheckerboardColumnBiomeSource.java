package net.minecraft.world.level.biome;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import java.util.function.Supplier;
import java.util.List;
import com.mojang.serialization.Codec;

public class CheckerboardColumnBiomeSource extends BiomeSource {
    public static final Codec<CheckerboardColumnBiomeSource> CODEC;
    private final List<Supplier<Biome>> allowedBiomes;
    private final int bitShift;
    private final int size;
    
    public CheckerboardColumnBiomeSource(final List<Supplier<Biome>> list, final int integer) {
        super((Stream<Supplier<Biome>>)list.stream());
        this.allowedBiomes = list;
        this.bitShift = integer + 2;
        this.size = integer;
    }
    
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CheckerboardColumnBiomeSource.CODEC;
    }
    
    @Override
    public BiomeSource withSeed(final long long1) {
        return this;
    }
    
    public Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        return (Biome)((Supplier)this.allowedBiomes.get(Math.floorMod((integer1 >> this.bitShift) + (integer3 >> this.bitShift), this.allowedBiomes.size()))).get();
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Biome.LIST_CODEC.fieldOf("biomes").forGetter(bsz -> bsz.allowedBiomes), (App)Codec.intRange(0, 62).fieldOf("scale").orElse(2).forGetter(bsz -> bsz.size)).apply((Applicative)instance, CheckerboardColumnBiomeSource::new));
    }
}
