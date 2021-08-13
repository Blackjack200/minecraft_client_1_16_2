package net.minecraft.world.level.levelgen.surfacebuilders;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import java.util.Random;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class ConfiguredSurfaceBuilder<SC extends SurfaceBuilderConfiguration> {
    public static final Codec<ConfiguredSurfaceBuilder<?>> DIRECT_CODEC;
    public static final Codec<Supplier<ConfiguredSurfaceBuilder<?>>> CODEC;
    public final SurfaceBuilder<SC> surfaceBuilder;
    public final SC config;
    
    public ConfiguredSurfaceBuilder(final SurfaceBuilder<SC> ctq, final SC cts) {
        this.surfaceBuilder = ctq;
        this.config = cts;
    }
    
    public void apply(final Random random, final ChunkAccess cft, final Biome bss, final int integer4, final int integer5, final int integer6, final double double7, final BlockState cee8, final BlockState cee9, final int integer10, final long long11) {
        this.surfaceBuilder.apply(random, cft, bss, integer4, integer5, integer6, double7, cee8, cee9, integer10, long11, this.config);
    }
    
    public void initNoise(final long long1) {
        this.surfaceBuilder.initNoise(long1);
    }
    
    public SC config() {
        return this.config;
    }
    
    static {
        DIRECT_CODEC = Registry.SURFACE_BUILDER.dispatch(ctd -> ctd.surfaceBuilder, SurfaceBuilder::configuredCodec);
        CODEC = (Codec)RegistryFileCodec.<ConfiguredSurfaceBuilder<?>>create(Registry.CONFIGURED_SURFACE_BUILDER_REGISTRY, ConfiguredSurfaceBuilder.DIRECT_CODEC);
    }
}
