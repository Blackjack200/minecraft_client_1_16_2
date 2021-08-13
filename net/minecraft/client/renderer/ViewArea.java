package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.world.level.Level;

public class ViewArea {
    protected final LevelRenderer levelRenderer;
    protected final Level level;
    protected int chunkGridSizeY;
    protected int chunkGridSizeX;
    protected int chunkGridSizeZ;
    public ChunkRenderDispatcher.RenderChunk[] chunks;
    
    public ViewArea(final ChunkRenderDispatcher ecm, final Level bru, final int integer, final LevelRenderer dzw) {
        this.levelRenderer = dzw;
        this.level = bru;
        this.setViewDistance(integer);
        this.createChunks(ecm);
    }
    
    protected void createChunks(final ChunkRenderDispatcher ecm) {
        final int integer3 = this.chunkGridSizeX * this.chunkGridSizeY * this.chunkGridSizeZ;
        this.chunks = new ChunkRenderDispatcher.RenderChunk[integer3];
        for (int integer4 = 0; integer4 < this.chunkGridSizeX; ++integer4) {
            for (int integer5 = 0; integer5 < this.chunkGridSizeY; ++integer5) {
                for (int integer6 = 0; integer6 < this.chunkGridSizeZ; ++integer6) {
                    final int integer7 = this.getChunkIndex(integer4, integer5, integer6);
                    (this.chunks[integer7] = ecm.new RenderChunk()).setOrigin(integer4 * 16, integer5 * 16, integer6 * 16);
                }
            }
        }
    }
    
    public void releaseAllBuffers() {
        for (final ChunkRenderDispatcher.RenderChunk c5 : this.chunks) {
            c5.releaseBuffers();
        }
    }
    
    private int getChunkIndex(final int integer1, final int integer2, final int integer3) {
        return (integer3 * this.chunkGridSizeY + integer2) * this.chunkGridSizeX + integer1;
    }
    
    protected void setViewDistance(final int integer) {
        final int integer2 = integer * 2 + 1;
        this.chunkGridSizeX = integer2;
        this.chunkGridSizeY = 16;
        this.chunkGridSizeZ = integer2;
    }
    
    public void repositionCamera(final double double1, final double double2) {
        final int integer6 = Mth.floor(double1);
        final int integer7 = Mth.floor(double2);
        for (int integer8 = 0; integer8 < this.chunkGridSizeX; ++integer8) {
            final int integer9 = this.chunkGridSizeX * 16;
            final int integer10 = integer6 - 8 - integer9 / 2;
            final int integer11 = integer10 + Math.floorMod(integer8 * 16 - integer10, integer9);
            for (int integer12 = 0; integer12 < this.chunkGridSizeZ; ++integer12) {
                final int integer13 = this.chunkGridSizeZ * 16;
                final int integer14 = integer7 - 8 - integer13 / 2;
                final int integer15 = integer14 + Math.floorMod(integer12 * 16 - integer14, integer13);
                for (int integer16 = 0; integer16 < this.chunkGridSizeY; ++integer16) {
                    final int integer17 = integer16 * 16;
                    final ChunkRenderDispatcher.RenderChunk c18 = this.chunks[this.getChunkIndex(integer8, integer16, integer12)];
                    c18.setOrigin(integer11, integer17, integer15);
                }
            }
        }
    }
    
    public void setDirty(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
        final int integer4 = Math.floorMod(integer1, this.chunkGridSizeX);
        final int integer5 = Math.floorMod(integer2, this.chunkGridSizeY);
        final int integer6 = Math.floorMod(integer3, this.chunkGridSizeZ);
        final ChunkRenderDispatcher.RenderChunk c9 = this.chunks[this.getChunkIndex(integer4, integer5, integer6)];
        c9.setDirty(boolean4);
    }
    
    @Nullable
    protected ChunkRenderDispatcher.RenderChunk getRenderChunkAt(final BlockPos fx) {
        int integer3 = Mth.intFloorDiv(fx.getX(), 16);
        final int integer4 = Mth.intFloorDiv(fx.getY(), 16);
        int integer5 = Mth.intFloorDiv(fx.getZ(), 16);
        if (integer4 < 0 || integer4 >= this.chunkGridSizeY) {
            return null;
        }
        integer3 = Mth.positiveModulo(integer3, this.chunkGridSizeX);
        integer5 = Mth.positiveModulo(integer5, this.chunkGridSizeZ);
        return this.chunks[this.getChunkIndex(integer3, integer4, integer5)];
    }
}
