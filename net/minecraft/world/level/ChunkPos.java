package net.minecraft.world.level;

import java.util.Spliterator;
import java.util.stream.StreamSupport;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import java.util.Spliterators;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;

public class ChunkPos {
    public static final long INVALID_CHUNK_POS;
    public final int x;
    public final int z;
    
    public ChunkPos(final int integer1, final int integer2) {
        this.x = integer1;
        this.z = integer2;
    }
    
    public ChunkPos(final BlockPos fx) {
        this.x = fx.getX() >> 4;
        this.z = fx.getZ() >> 4;
    }
    
    public ChunkPos(final long long1) {
        this.x = (int)long1;
        this.z = (int)(long1 >> 32);
    }
    
    public long toLong() {
        return asLong(this.x, this.z);
    }
    
    public static long asLong(final int integer1, final int integer2) {
        return ((long)integer1 & 0xFFFFFFFFL) | ((long)integer2 & 0xFFFFFFFFL) << 32;
    }
    
    public static int getX(final long long1) {
        return (int)(long1 & 0xFFFFFFFFL);
    }
    
    public static int getZ(final long long1) {
        return (int)(long1 >>> 32 & 0xFFFFFFFFL);
    }
    
    public int hashCode() {
        final int integer2 = 1664525 * this.x + 1013904223;
        final int integer3 = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
        return integer2 ^ integer3;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ChunkPos) {
            final ChunkPos bra3 = (ChunkPos)object;
            return this.x == bra3.x && this.z == bra3.z;
        }
        return false;
    }
    
    public int getMinBlockX() {
        return this.x << 4;
    }
    
    public int getMinBlockZ() {
        return this.z << 4;
    }
    
    public int getMaxBlockX() {
        return (this.x << 4) + 15;
    }
    
    public int getMaxBlockZ() {
        return (this.z << 4) + 15;
    }
    
    public int getRegionX() {
        return this.x >> 5;
    }
    
    public int getRegionZ() {
        return this.z >> 5;
    }
    
    public int getRegionLocalX() {
        return this.x & 0x1F;
    }
    
    public int getRegionLocalZ() {
        return this.z & 0x1F;
    }
    
    public String toString() {
        return new StringBuilder().append("[").append(this.x).append(", ").append(this.z).append("]").toString();
    }
    
    public BlockPos getWorldPosition() {
        return new BlockPos(this.getMinBlockX(), 0, this.getMinBlockZ());
    }
    
    public int getChessboardDistance(final ChunkPos bra) {
        return Math.max(Math.abs(this.x - bra.x), Math.abs(this.z - bra.z));
    }
    
    public static Stream<ChunkPos> rangeClosed(final ChunkPos bra, final int integer) {
        return rangeClosed(new ChunkPos(bra.x - integer, bra.z - integer), new ChunkPos(bra.x + integer, bra.z + integer));
    }
    
    public static Stream<ChunkPos> rangeClosed(final ChunkPos bra1, final ChunkPos bra2) {
        final int integer3 = Math.abs(bra1.x - bra2.x) + 1;
        final int integer4 = Math.abs(bra1.z - bra2.z) + 1;
        final int integer5 = (bra1.x < bra2.x) ? 1 : -1;
        final int integer6 = (bra1.z < bra2.z) ? 1 : -1;
        return (Stream<ChunkPos>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<ChunkPos>((long)(integer3 * integer4), 64) {
            @Nullable
            private ChunkPos pos;
            
            public boolean tryAdvance(final Consumer<? super ChunkPos> consumer) {
                if (this.pos == null) {
                    this.pos = bra1;
                }
                else {
                    final int integer3 = this.pos.x;
                    final int integer4 = this.pos.z;
                    if (integer3 == bra2.x) {
                        if (integer4 == bra2.z) {
                            return false;
                        }
                        this.pos = new ChunkPos(bra1.x, integer4 + integer6);
                    }
                    else {
                        this.pos = new ChunkPos(integer3 + integer5, integer4);
                    }
                }
                consumer.accept(this.pos);
                return true;
            }
        }, false);
    }
    
    static {
        INVALID_CHUNK_POS = asLong(1875016, 1875016);
    }
}
