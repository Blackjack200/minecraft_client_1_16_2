package net.minecraft.world.level.dimension;

import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.Map;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.chunk.ChunkGenerator;
import java.util.function.Supplier;
import java.util.LinkedHashSet;
import net.minecraft.resources.ResourceKey;
import com.mojang.serialization.Codec;

public final class LevelStem {
    public static final Codec<LevelStem> CODEC;
    public static final ResourceKey<LevelStem> OVERWORLD;
    public static final ResourceKey<LevelStem> NETHER;
    public static final ResourceKey<LevelStem> END;
    private static final LinkedHashSet<ResourceKey<LevelStem>> BUILTIN_ORDER;
    private final Supplier<DimensionType> type;
    private final ChunkGenerator generator;
    
    public LevelStem(final Supplier<DimensionType> supplier, final ChunkGenerator cfv) {
        this.type = supplier;
        this.generator = cfv;
    }
    
    public Supplier<DimensionType> typeSupplier() {
        return this.type;
    }
    
    public DimensionType type() {
        return (DimensionType)this.type.get();
    }
    
    public ChunkGenerator generator() {
        return this.generator;
    }
    
    public static MappedRegistry<LevelStem> sortMap(final MappedRegistry<LevelStem> gi) {
        final MappedRegistry<LevelStem> gi2 = new MappedRegistry<LevelStem>(Registry.LEVEL_STEM_REGISTRY, Lifecycle.experimental());
        for (final ResourceKey<LevelStem> vj4 : LevelStem.BUILTIN_ORDER) {
            final LevelStem chb5 = gi.get(vj4);
            if (chb5 != null) {
                gi2.<LevelStem>register(vj4, chb5, gi.lifecycle(chb5));
            }
        }
        for (final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry4 : gi.entrySet()) {
            final ResourceKey<LevelStem> vj5 = (ResourceKey<LevelStem>)entry4.getKey();
            if (LevelStem.BUILTIN_ORDER.contains(vj5)) {
                continue;
            }
            gi2.register(vj5, entry4.getValue(), gi.lifecycle((LevelStem)entry4.getValue()));
        }
        return gi2;
    }
    
    public static boolean stable(final long long1, final MappedRegistry<LevelStem> gi) {
        final List<Map.Entry<ResourceKey<LevelStem>, LevelStem>> list4 = (List<Map.Entry<ResourceKey<LevelStem>, LevelStem>>)Lists.newArrayList((Iterable)gi.entrySet());
        if (list4.size() != LevelStem.BUILTIN_ORDER.size()) {
            return false;
        }
        final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry5 = (Map.Entry<ResourceKey<LevelStem>, LevelStem>)list4.get(0);
        final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry6 = (Map.Entry<ResourceKey<LevelStem>, LevelStem>)list4.get(1);
        final Map.Entry<ResourceKey<LevelStem>, LevelStem> entry7 = (Map.Entry<ResourceKey<LevelStem>, LevelStem>)list4.get(2);
        if (entry5.getKey() != LevelStem.OVERWORLD || entry6.getKey() != LevelStem.NETHER || entry7.getKey() != LevelStem.END) {
            return false;
        }
        if (!((LevelStem)entry5.getValue()).type().equalTo(DimensionType.DEFAULT_OVERWORLD) && ((LevelStem)entry5.getValue()).type() != DimensionType.DEFAULT_OVERWORLD_CAVES) {
            return false;
        }
        if (!((LevelStem)entry6.getValue()).type().equalTo(DimensionType.DEFAULT_NETHER)) {
            return false;
        }
        if (!((LevelStem)entry7.getValue()).type().equalTo(DimensionType.DEFAULT_END)) {
            return false;
        }
        if (!(((LevelStem)entry6.getValue()).generator() instanceof NoiseBasedChunkGenerator) || !(((LevelStem)entry7.getValue()).generator() instanceof NoiseBasedChunkGenerator)) {
            return false;
        }
        final NoiseBasedChunkGenerator chl8 = (NoiseBasedChunkGenerator)((LevelStem)entry6.getValue()).generator();
        final NoiseBasedChunkGenerator chl9 = (NoiseBasedChunkGenerator)((LevelStem)entry7.getValue()).generator();
        if (!chl8.stable(long1, NoiseGeneratorSettings.NETHER)) {
            return false;
        }
        if (!chl9.stable(long1, NoiseGeneratorSettings.END)) {
            return false;
        }
        if (!(chl8.getBiomeSource() instanceof MultiNoiseBiomeSource)) {
            return false;
        }
        final MultiNoiseBiomeSource bte10 = (MultiNoiseBiomeSource)chl8.getBiomeSource();
        if (!bte10.stable(long1)) {
            return false;
        }
        if (!(chl9.getBiomeSource() instanceof TheEndBiomeSource)) {
            return false;
        }
        final TheEndBiomeSource bth11 = (TheEndBiomeSource)chl9.getBiomeSource();
        return bth11.stable(long1);
    }
    
    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)DimensionType.CODEC.fieldOf("type").forGetter(LevelStem::typeSupplier), (App)ChunkGenerator.CODEC.fieldOf("generator").forGetter(LevelStem::generator)).apply((Applicative)instance, instance.stable(LevelStem::new)));
        OVERWORLD = ResourceKey.<LevelStem>create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation("overworld"));
        NETHER = ResourceKey.<LevelStem>create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation("the_nether"));
        END = ResourceKey.<LevelStem>create(Registry.LEVEL_STEM_REGISTRY, new ResourceLocation("the_end"));
        BUILTIN_ORDER = Sets.newLinkedHashSet((Iterable)ImmutableList.of(LevelStem.OVERWORLD, LevelStem.NETHER, LevelStem.END));
    }
}
