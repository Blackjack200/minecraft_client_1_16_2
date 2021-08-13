package net.minecraft.world.level.biome;

import java.util.function.Function;
import net.minecraft.core.Registry;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.Random;
import java.util.function.Predicate;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.Map;
import com.mojang.serialization.Codec;

public abstract class BiomeSource implements BiomeManager.NoiseBiomeSource {
    public static final Codec<BiomeSource> CODEC;
    protected final Map<StructureFeature<?>, Boolean> supportedStructures;
    protected final Set<BlockState> surfaceBlocks;
    protected final List<Biome> possibleBiomes;
    
    protected BiomeSource(final Stream<Supplier<Biome>> stream) {
        this((List<Biome>)stream.map(Supplier::get).collect(ImmutableList.toImmutableList()));
    }
    
    protected BiomeSource(final List<Biome> list) {
        this.supportedStructures = (Map<StructureFeature<?>, Boolean>)Maps.newHashMap();
        this.surfaceBlocks = (Set<BlockState>)Sets.newHashSet();
        this.possibleBiomes = list;
    }
    
    protected abstract Codec<? extends BiomeSource> codec();
    
    public abstract BiomeSource withSeed(final long long1);
    
    public List<Biome> possibleBiomes() {
        return this.possibleBiomes;
    }
    
    public Set<Biome> getBiomesWithin(final int integer1, final int integer2, final int integer3, final int integer4) {
        final int integer5 = integer1 - integer4 >> 2;
        final int integer6 = integer2 - integer4 >> 2;
        final int integer7 = integer3 - integer4 >> 2;
        final int integer8 = integer1 + integer4 >> 2;
        final int integer9 = integer2 + integer4 >> 2;
        final int integer10 = integer3 + integer4 >> 2;
        final int integer11 = integer8 - integer5 + 1;
        final int integer12 = integer9 - integer6 + 1;
        final int integer13 = integer10 - integer7 + 1;
        final Set<Biome> set15 = (Set<Biome>)Sets.newHashSet();
        for (int integer14 = 0; integer14 < integer13; ++integer14) {
            for (int integer15 = 0; integer15 < integer11; ++integer15) {
                for (int integer16 = 0; integer16 < integer12; ++integer16) {
                    final int integer17 = integer5 + integer15;
                    final int integer18 = integer6 + integer16;
                    final int integer19 = integer7 + integer14;
                    set15.add(this.getNoiseBiome(integer17, integer18, integer19));
                }
            }
        }
        return set15;
    }
    
    @Nullable
    public BlockPos findBiomeHorizontal(final int integer1, final int integer2, final int integer3, final int integer4, final Predicate<Biome> predicate, final Random random) {
        return this.findBiomeHorizontal(integer1, integer2, integer3, integer4, 1, predicate, random, false);
    }
    
    @Nullable
    public BlockPos findBiomeHorizontal(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final Predicate<Biome> predicate, final Random random, final boolean boolean8) {
        final int integer6 = integer1 >> 2;
        final int integer7 = integer3 >> 2;
        final int integer8 = integer4 >> 2;
        final int integer9 = integer2 >> 2;
        BlockPos fx14 = null;
        int integer10 = 0;
        int integer12;
        for (int integer11 = integer12 = (boolean8 ? 0 : integer8); integer12 <= integer8; integer12 += integer5) {
            for (int integer13 = -integer12; integer13 <= integer12; integer13 += integer5) {
                final boolean boolean9 = Math.abs(integer13) == integer12;
                for (int integer14 = -integer12; integer14 <= integer12; integer14 += integer5) {
                    if (boolean8) {
                        final boolean boolean10 = Math.abs(integer14) == integer12;
                        if (!boolean10 && !boolean9) {
                            continue;
                        }
                    }
                    final int integer15 = integer6 + integer14;
                    final int integer16 = integer7 + integer13;
                    if (predicate.test(this.getNoiseBiome(integer15, integer9, integer16))) {
                        if (fx14 == null || random.nextInt(integer10 + 1) == 0) {
                            fx14 = new BlockPos(integer15 << 2, integer2, integer16 << 2);
                            if (boolean8) {
                                return fx14;
                            }
                        }
                        ++integer10;
                    }
                }
            }
        }
        return fx14;
    }
    
    public boolean canGenerateStructure(final StructureFeature<?> ckx) {
        return (boolean)this.supportedStructures.computeIfAbsent(ckx, ckx -> this.possibleBiomes.stream().anyMatch(bss -> bss.getGenerationSettings().isValidStart(ckx)));
    }
    
    public Set<BlockState> getSurfaceBlocks() {
        if (this.surfaceBlocks.isEmpty()) {
            for (final Biome bss3 : this.possibleBiomes) {
                this.surfaceBlocks.add(bss3.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial());
            }
        }
        return this.surfaceBlocks;
    }
    
    static {
        Registry.<Codec<FixedBiomeSource>>register(Registry.BIOME_SOURCE, "fixed", FixedBiomeSource.CODEC);
        Registry.<Codec<MultiNoiseBiomeSource>>register(Registry.BIOME_SOURCE, "multi_noise", MultiNoiseBiomeSource.CODEC);
        Registry.<Codec<CheckerboardColumnBiomeSource>>register(Registry.BIOME_SOURCE, "checkerboard", CheckerboardColumnBiomeSource.CODEC);
        Registry.<Codec<OverworldBiomeSource>>register(Registry.BIOME_SOURCE, "vanilla_layered", OverworldBiomeSource.CODEC);
        Registry.<Codec<TheEndBiomeSource>>register(Registry.BIOME_SOURCE, "the_end", TheEndBiomeSource.CODEC);
        CODEC = Registry.BIOME_SOURCE.dispatchStable(BiomeSource::codec, Function.identity());
    }
}
