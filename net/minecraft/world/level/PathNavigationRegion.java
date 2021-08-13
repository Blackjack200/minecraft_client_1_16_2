package net.minecraft.world.level;

import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;

public class PathNavigationRegion implements BlockGetter, CollisionGetter {
    protected final int centerX;
    protected final int centerZ;
    protected final ChunkAccess[][] chunks;
    protected boolean allEmpty;
    protected final Level level;
    
    public PathNavigationRegion(final Level bru, final BlockPos fx2, final BlockPos fx3) {
        this.level = bru;
        this.centerX = fx2.getX() >> 4;
        this.centerZ = fx2.getZ() >> 4;
        final int integer5 = fx3.getX() >> 4;
        final int integer6 = fx3.getZ() >> 4;
        this.chunks = new ChunkAccess[integer5 - this.centerX + 1][integer6 - this.centerZ + 1];
        final ChunkSource cfw7 = bru.getChunkSource();
        this.allEmpty = true;
        for (int integer7 = this.centerX; integer7 <= integer5; ++integer7) {
            for (int integer8 = this.centerZ; integer8 <= integer6; ++integer8) {
                this.chunks[integer7 - this.centerX][integer8 - this.centerZ] = cfw7.getChunkNow(integer7, integer8);
            }
        }
        for (int integer7 = fx2.getX() >> 4; integer7 <= fx3.getX() >> 4; ++integer7) {
            for (int integer8 = fx2.getZ() >> 4; integer8 <= fx3.getZ() >> 4; ++integer8) {
                final ChunkAccess cft10 = this.chunks[integer7 - this.centerX][integer8 - this.centerZ];
                if (cft10 != null && !cft10.isYSpaceEmpty(fx2.getY(), fx3.getY())) {
                    this.allEmpty = false;
                    return;
                }
            }
        }
    }
    
    private ChunkAccess getChunk(final BlockPos fx) {
        return this.getChunk(fx.getX() >> 4, fx.getZ() >> 4);
    }
    
    private ChunkAccess getChunk(final int integer1, final int integer2) {
        final int integer3 = integer1 - this.centerX;
        final int integer4 = integer2 - this.centerZ;
        if (integer3 < 0 || integer3 >= this.chunks.length || integer4 < 0 || integer4 >= this.chunks[integer3].length) {
            return new EmptyLevelChunk(this.level, new ChunkPos(integer1, integer2));
        }
        final ChunkAccess cft6 = this.chunks[integer3][integer4];
        return (cft6 != null) ? cft6 : new EmptyLevelChunk(this.level, new ChunkPos(integer1, integer2));
    }
    
    public WorldBorder getWorldBorder() {
        return this.level.getWorldBorder();
    }
    
    public BlockGetter getChunkForCollisions(final int integer1, final int integer2) {
        return this.getChunk(integer1, integer2);
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        final ChunkAccess cft3 = this.getChunk(fx);
        return cft3.getBlockEntity(fx);
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        if (Level.isOutsideBuildHeight(fx)) {
            return Blocks.AIR.defaultBlockState();
        }
        final ChunkAccess cft3 = this.getChunk(fx);
        return cft3.getBlockState(fx);
    }
    
    public Stream<VoxelShape> getEntityCollisions(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate) {
        return (Stream<VoxelShape>)Stream.empty();
    }
    
    public Stream<VoxelShape> getCollisions(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate) {
        return this.getBlockCollisions(apx, dcf);
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        if (Level.isOutsideBuildHeight(fx)) {
            return Fluids.EMPTY.defaultFluidState();
        }
        final ChunkAccess cft3 = this.getChunk(fx);
        return cft3.getFluidState(fx);
    }
}
