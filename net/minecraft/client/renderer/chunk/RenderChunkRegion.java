package net.minecraft.client.renderer.chunk;

import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.core.Direction;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;

public class RenderChunkRegion implements BlockAndTintGetter {
    protected final int centerX;
    protected final int centerZ;
    protected final BlockPos start;
    protected final int xLength;
    protected final int yLength;
    protected final int zLength;
    protected final LevelChunk[][] chunks;
    protected final BlockState[] blockStates;
    protected final FluidState[] fluidStates;
    protected final Level level;
    
    @Nullable
    public static RenderChunkRegion createIfNotEmpty(final Level bru, final BlockPos fx2, final BlockPos fx3, final int integer) {
        final int integer2 = fx2.getX() - integer >> 4;
        final int integer3 = fx2.getZ() - integer >> 4;
        final int integer4 = fx3.getX() + integer >> 4;
        final int integer5 = fx3.getZ() + integer >> 4;
        final LevelChunk[][] arr9 = new LevelChunk[integer4 - integer2 + 1][integer5 - integer3 + 1];
        for (int integer6 = integer2; integer6 <= integer4; ++integer6) {
            for (int integer7 = integer3; integer7 <= integer5; ++integer7) {
                arr9[integer6 - integer2][integer7 - integer3] = bru.getChunk(integer6, integer7);
            }
        }
        if (isAllEmpty(fx2, fx3, integer2, integer3, arr9)) {
            return null;
        }
        int integer6 = 1;
        final BlockPos fx4 = fx2.offset(-1, -1, -1);
        final BlockPos fx5 = fx3.offset(1, 1, 1);
        return new RenderChunkRegion(bru, integer2, integer3, arr9, fx4, fx5);
    }
    
    public static boolean isAllEmpty(final BlockPos fx1, final BlockPos fx2, final int integer3, final int integer4, final LevelChunk[][] arr) {
        for (int integer5 = fx1.getX() >> 4; integer5 <= fx2.getX() >> 4; ++integer5) {
            for (int integer6 = fx1.getZ() >> 4; integer6 <= fx2.getZ() >> 4; ++integer6) {
                final LevelChunk cge8 = arr[integer5 - integer3][integer6 - integer4];
                if (!cge8.isYSpaceEmpty(fx1.getY(), fx2.getY())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public RenderChunkRegion(final Level bru, final int integer2, final int integer3, final LevelChunk[][] arr, final BlockPos fx5, final BlockPos fx6) {
        this.level = bru;
        this.centerX = integer2;
        this.centerZ = integer3;
        this.chunks = arr;
        this.start = fx5;
        this.xLength = fx6.getX() - fx5.getX() + 1;
        this.yLength = fx6.getY() - fx5.getY() + 1;
        this.zLength = fx6.getZ() - fx5.getZ() + 1;
        this.blockStates = new BlockState[this.xLength * this.yLength * this.zLength];
        this.fluidStates = new FluidState[this.xLength * this.yLength * this.zLength];
        for (final BlockPos fx7 : BlockPos.betweenClosed(fx5, fx6)) {
            final int integer4 = (fx7.getX() >> 4) - integer2;
            final int integer5 = (fx7.getZ() >> 4) - integer3;
            final LevelChunk cge12 = arr[integer4][integer5];
            final int integer6 = this.index(fx7);
            this.blockStates[integer6] = cge12.getBlockState(fx7);
            this.fluidStates[integer6] = cge12.getFluidState(fx7);
        }
    }
    
    protected final int index(final BlockPos fx) {
        return this.index(fx.getX(), fx.getY(), fx.getZ());
    }
    
    protected int index(final int integer1, final int integer2, final int integer3) {
        final int integer4 = integer1 - this.start.getX();
        final int integer5 = integer2 - this.start.getY();
        final int integer6 = integer3 - this.start.getZ();
        return integer6 * this.xLength * this.yLength + integer5 * this.xLength + integer4;
    }
    
    public BlockState getBlockState(final BlockPos fx) {
        return this.blockStates[this.index(fx)];
    }
    
    public FluidState getFluidState(final BlockPos fx) {
        return this.fluidStates[this.index(fx)];
    }
    
    public float getShade(final Direction gc, final boolean boolean2) {
        return this.level.getShade(gc, boolean2);
    }
    
    public LevelLightEngine getLightEngine() {
        return this.level.getLightEngine();
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx) {
        return this.getBlockEntity(fx, LevelChunk.EntityCreationType.IMMEDIATE);
    }
    
    @Nullable
    public BlockEntity getBlockEntity(final BlockPos fx, final LevelChunk.EntityCreationType a) {
        final int integer4 = (fx.getX() >> 4) - this.centerX;
        final int integer5 = (fx.getZ() >> 4) - this.centerZ;
        return this.chunks[integer4][integer5].getBlockEntity(fx, a);
    }
    
    public int getBlockTint(final BlockPos fx, final ColorResolver colorResolver) {
        return this.level.getBlockTint(fx, colorResolver);
    }
}
