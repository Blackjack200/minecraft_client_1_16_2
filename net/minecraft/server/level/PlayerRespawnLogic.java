package net.minecraft.server.level;

import net.minecraft.world.level.ChunkPos;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;

public class PlayerRespawnLogic {
    @Nullable
    protected static BlockPos getOverworldRespawnPos(final ServerLevel aag, final int integer2, final int integer3, final boolean boolean4) {
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos(integer2, 0, integer3);
        final Biome bss6 = aag.getBiome(a5);
        final boolean boolean5 = aag.dimensionType().hasCeiling();
        final BlockState cee8 = bss6.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
        if (boolean4 && !cee8.getBlock().is(BlockTags.VALID_SPAWN)) {
            return null;
        }
        final LevelChunk cge9 = aag.getChunk(integer2 >> 4, integer3 >> 4);
        final int integer4 = boolean5 ? aag.getChunkSource().getGenerator().getSpawnHeight() : cge9.getHeight(Heightmap.Types.MOTION_BLOCKING, integer2 & 0xF, integer3 & 0xF);
        if (integer4 < 0) {
            return null;
        }
        final int integer5 = cge9.getHeight(Heightmap.Types.WORLD_SURFACE, integer2 & 0xF, integer3 & 0xF);
        if (integer5 <= integer4 && integer5 > cge9.getHeight(Heightmap.Types.OCEAN_FLOOR, integer2 & 0xF, integer3 & 0xF)) {
            return null;
        }
        for (int integer6 = integer4 + 1; integer6 >= 0; --integer6) {
            a5.set(integer2, integer6, integer3);
            final BlockState cee9 = aag.getBlockState(a5);
            if (!cee9.getFluidState().isEmpty()) {
                break;
            }
            if (cee9.equals(cee8)) {
                return a5.above().immutable();
            }
        }
        return null;
    }
    
    @Nullable
    public static BlockPos getSpawnPosInChunk(final ServerLevel aag, final ChunkPos bra, final boolean boolean3) {
        for (int integer4 = bra.getMinBlockX(); integer4 <= bra.getMaxBlockX(); ++integer4) {
            for (int integer5 = bra.getMinBlockZ(); integer5 <= bra.getMaxBlockZ(); ++integer5) {
                final BlockPos fx6 = getOverworldRespawnPos(aag, integer4, integer5, boolean3);
                if (fx6 != null) {
                    return fx6;
                }
            }
        }
        return null;
    }
}
