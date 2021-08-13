package net.minecraft.world.level.levelgen.carver;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Registry;
import java.util.BitSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import java.util.function.Function;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import java.util.List;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class ConfiguredWorldCarver<WC extends CarverConfiguration> {
    public static final Codec<ConfiguredWorldCarver<?>> DIRECT_CODEC;
    public static final Codec<Supplier<ConfiguredWorldCarver<?>>> CODEC;
    public static final Codec<List<Supplier<ConfiguredWorldCarver<?>>>> LIST_CODEC;
    private final WorldCarver<WC> worldCarver;
    private final WC config;
    
    public ConfiguredWorldCarver(final WorldCarver<WC> cid, final WC chw) {
        this.worldCarver = cid;
        this.config = chw;
    }
    
    public WC config() {
        return this.config;
    }
    
    public boolean isStartChunk(final Random random, final int integer2, final int integer3) {
        return this.worldCarver.isStartChunk(random, integer2, integer3, this.config);
    }
    
    public boolean carve(final ChunkAccess cft, final Function<BlockPos, Biome> function, final Random random, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final BitSet bitSet) {
        return this.worldCarver.carve(cft, function, random, integer4, integer5, integer6, integer7, integer8, bitSet, this.config);
    }
    
    static {
        DIRECT_CODEC = Registry.CARVER.dispatch(chy -> chy.worldCarver, WorldCarver::configuredCodec);
        CODEC = (Codec)RegistryFileCodec.<ConfiguredWorldCarver<?>>create(Registry.CONFIGURED_CARVER_REGISTRY, ConfiguredWorldCarver.DIRECT_CODEC);
        LIST_CODEC = RegistryFileCodec.<ConfiguredWorldCarver<?>>homogeneousList(Registry.CONFIGURED_CARVER_REGISTRY, ConfiguredWorldCarver.DIRECT_CODEC);
    }
}
