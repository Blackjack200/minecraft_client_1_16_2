package net.minecraft.world.level;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import java.util.stream.Stream;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.biome.BiomeManager;

public interface LevelReader extends BlockAndTintGetter, CollisionGetter, BiomeManager.NoiseBiomeSource {
    @Nullable
    ChunkAccess getChunk(final int integer1, final int integer2, final ChunkStatus cfx, final boolean boolean4);
    
    @Deprecated
    boolean hasChunk(final int integer1, final int integer2);
    
    int getHeight(final Heightmap.Types a, final int integer2, final int integer3);
    
    int getSkyDarken();
    
    BiomeManager getBiomeManager();
    
    default Biome getBiome(final BlockPos fx) {
        return this.getBiomeManager().getBiome(fx);
    }
    
    default Stream<BlockState> getBlockStatesIfLoaded(final AABB dcf) {
        final int integer3 = Mth.floor(dcf.minX);
        final int integer4 = Mth.floor(dcf.maxX);
        final int integer5 = Mth.floor(dcf.minY);
        final int integer6 = Mth.floor(dcf.maxY);
        final int integer7 = Mth.floor(dcf.minZ);
        final int integer8 = Mth.floor(dcf.maxZ);
        if (this.hasChunksAt(integer3, integer5, integer7, integer4, integer6, integer8)) {
            return this.getBlockStates(dcf);
        }
        return (Stream<BlockState>)Stream.empty();
    }
    
    default int getBlockTint(final BlockPos fx, final ColorResolver colorResolver) {
        return colorResolver.getColor(this.getBiome(fx), fx.getX(), fx.getZ());
    }
    
    default Biome getNoiseBiome(final int integer1, final int integer2, final int integer3) {
        final ChunkAccess cft5 = this.getChunk(integer1 >> 2, integer3 >> 2, ChunkStatus.BIOMES, false);
        if (cft5 != null && cft5.getBiomes() != null) {
            return cft5.getBiomes().getNoiseBiome(integer1, integer2, integer3);
        }
        return this.getUncachedNoiseBiome(integer1, integer2, integer3);
    }
    
    Biome getUncachedNoiseBiome(final int integer1, final int integer2, final int integer3);
    
    boolean isClientSide();
    
    @Deprecated
    int getSeaLevel();
    
    DimensionType dimensionType();
    
    default BlockPos getHeightmapPos(final Heightmap.Types a, final BlockPos fx) {
        return new BlockPos(fx.getX(), this.getHeight(a, fx.getX(), fx.getZ()), fx.getZ());
    }
    
    default boolean isEmptyBlock(final BlockPos fx) {
        return this.getBlockState(fx).isAir();
    }
    
    default boolean canSeeSkyFromBelowWater(final BlockPos fx) {
        if (fx.getY() >= this.getSeaLevel()) {
            return this.canSeeSky(fx);
        }
        BlockPos fx2 = new BlockPos(fx.getX(), this.getSeaLevel(), fx.getZ());
        if (!this.canSeeSky(fx2)) {
            return false;
        }
        for (fx2 = fx2.below(); fx2.getY() > fx.getY(); fx2 = fx2.below()) {
            final BlockState cee4 = this.getBlockState(fx2);
            if (cee4.getLightBlock(this, fx2) > 0 && !cee4.getMaterial().isLiquid()) {
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    default float getBrightness(final BlockPos fx) {
        return this.dimensionType().brightness(this.getMaxLocalRawBrightness(fx));
    }
    
    default int getDirectSignal(final BlockPos fx, final Direction gc) {
        return this.getBlockState(fx).getDirectSignal(this, fx, gc);
    }
    
    default ChunkAccess getChunk(final BlockPos fx) {
        return this.getChunk(fx.getX() >> 4, fx.getZ() >> 4);
    }
    
    default ChunkAccess getChunk(final int integer1, final int integer2) {
        return this.getChunk(integer1, integer2, ChunkStatus.FULL, true);
    }
    
    default ChunkAccess getChunk(final int integer1, final int integer2, final ChunkStatus cfx) {
        return this.getChunk(integer1, integer2, cfx, true);
    }
    
    @Nullable
    default BlockGetter getChunkForCollisions(final int integer1, final int integer2) {
        return this.getChunk(integer1, integer2, ChunkStatus.EMPTY, false);
    }
    
    default boolean isWaterAt(final BlockPos fx) {
        return this.getFluidState(fx).is(FluidTags.WATER);
    }
    
    default boolean containsAnyLiquid(final AABB dcf) {
        final int integer3 = Mth.floor(dcf.minX);
        final int integer4 = Mth.ceil(dcf.maxX);
        final int integer5 = Mth.floor(dcf.minY);
        final int integer6 = Mth.ceil(dcf.maxY);
        final int integer7 = Mth.floor(dcf.minZ);
        final int integer8 = Mth.ceil(dcf.maxZ);
        final BlockPos.MutableBlockPos a9 = new BlockPos.MutableBlockPos();
        for (int integer9 = integer3; integer9 < integer4; ++integer9) {
            for (int integer10 = integer5; integer10 < integer6; ++integer10) {
                for (int integer11 = integer7; integer11 < integer8; ++integer11) {
                    final BlockState cee13 = this.getBlockState(a9.set(integer9, integer10, integer11));
                    if (!cee13.getFluidState().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    default int getMaxLocalRawBrightness(final BlockPos fx) {
        return this.getMaxLocalRawBrightness(fx, this.getSkyDarken());
    }
    
    default int getMaxLocalRawBrightness(final BlockPos fx, final int integer) {
        if (fx.getX() < -30000000 || fx.getZ() < -30000000 || fx.getX() >= 30000000 || fx.getZ() >= 30000000) {
            return 15;
        }
        return this.getRawBrightness(fx, integer);
    }
    
    @Deprecated
    default boolean hasChunkAt(final BlockPos fx) {
        return this.hasChunk(fx.getX() >> 4, fx.getZ() >> 4);
    }
    
    @Deprecated
    default boolean hasChunksAt(final BlockPos fx1, final BlockPos fx2) {
        return this.hasChunksAt(fx1.getX(), fx1.getY(), fx1.getZ(), fx2.getX(), fx2.getY(), fx2.getZ());
    }
    
    @Deprecated
    default boolean hasChunksAt(int integer1, final int integer2, int integer3, int integer4, final int integer5, int integer6) {
        if (integer5 < 0 || integer2 >= 256) {
            return false;
        }
        integer1 >>= 4;
        integer3 >>= 4;
        integer4 >>= 4;
        integer6 >>= 4;
        for (int integer7 = integer1; integer7 <= integer4; ++integer7) {
            for (int integer8 = integer3; integer8 <= integer6; ++integer8) {
                if (!this.hasChunk(integer7, integer8)) {
                    return false;
                }
            }
        }
        return true;
    }
}
