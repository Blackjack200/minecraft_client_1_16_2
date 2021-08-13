package net.minecraft.core;

import java.util.Spliterator;
import java.util.stream.StreamSupport;
import java.util.function.Consumer;
import java.util.Spliterators;
import java.util.stream.Stream;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public class SectionPos extends Vec3i {
    private SectionPos(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    public static SectionPos of(final int integer1, final int integer2, final int integer3) {
        return new SectionPos(integer1, integer2, integer3);
    }
    
    public static SectionPos of(final BlockPos fx) {
        return new SectionPos(blockToSectionCoord(fx.getX()), blockToSectionCoord(fx.getY()), blockToSectionCoord(fx.getZ()));
    }
    
    public static SectionPos of(final ChunkPos bra, final int integer) {
        return new SectionPos(bra.x, integer, bra.z);
    }
    
    public static SectionPos of(final Entity apx) {
        return new SectionPos(blockToSectionCoord(Mth.floor(apx.getX())), blockToSectionCoord(Mth.floor(apx.getY())), blockToSectionCoord(Mth.floor(apx.getZ())));
    }
    
    public static SectionPos of(final long long1) {
        return new SectionPos(x(long1), y(long1), z(long1));
    }
    
    public static long offset(final long long1, final Direction gc) {
        return offset(long1, gc.getStepX(), gc.getStepY(), gc.getStepZ());
    }
    
    public static long offset(final long long1, final int integer2, final int integer3, final int integer4) {
        return asLong(x(long1) + integer2, y(long1) + integer3, z(long1) + integer4);
    }
    
    public static int blockToSectionCoord(final int integer) {
        return integer >> 4;
    }
    
    public static int sectionRelative(final int integer) {
        return integer & 0xF;
    }
    
    public static short sectionRelativePos(final BlockPos fx) {
        final int integer2 = sectionRelative(fx.getX());
        final int integer3 = sectionRelative(fx.getY());
        final int integer4 = sectionRelative(fx.getZ());
        return (short)(integer2 << 8 | integer4 << 4 | integer3 << 0);
    }
    
    public static int sectionRelativeX(final short short1) {
        return short1 >>> 8 & 0xF;
    }
    
    public static int sectionRelativeY(final short short1) {
        return short1 >>> 0 & 0xF;
    }
    
    public static int sectionRelativeZ(final short short1) {
        return short1 >>> 4 & 0xF;
    }
    
    public int relativeToBlockX(final short short1) {
        return this.minBlockX() + sectionRelativeX(short1);
    }
    
    public int relativeToBlockY(final short short1) {
        return this.minBlockY() + sectionRelativeY(short1);
    }
    
    public int relativeToBlockZ(final short short1) {
        return this.minBlockZ() + sectionRelativeZ(short1);
    }
    
    public BlockPos relativeToBlockPos(final short short1) {
        return new BlockPos(this.relativeToBlockX(short1), this.relativeToBlockY(short1), this.relativeToBlockZ(short1));
    }
    
    public static int sectionToBlockCoord(final int integer) {
        return integer << 4;
    }
    
    public static int x(final long long1) {
        return (int)(long1 << 0 >> 42);
    }
    
    public static int y(final long long1) {
        return (int)(long1 << 44 >> 44);
    }
    
    public static int z(final long long1) {
        return (int)(long1 << 22 >> 42);
    }
    
    public int x() {
        return this.getX();
    }
    
    public int y() {
        return this.getY();
    }
    
    public int z() {
        return this.getZ();
    }
    
    public int minBlockX() {
        return this.x() << 4;
    }
    
    public int minBlockY() {
        return this.y() << 4;
    }
    
    public int minBlockZ() {
        return this.z() << 4;
    }
    
    public int maxBlockX() {
        return (this.x() << 4) + 15;
    }
    
    public int maxBlockY() {
        return (this.y() << 4) + 15;
    }
    
    public int maxBlockZ() {
        return (this.z() << 4) + 15;
    }
    
    public static long blockToSection(final long long1) {
        return asLong(blockToSectionCoord(BlockPos.getX(long1)), blockToSectionCoord(BlockPos.getY(long1)), blockToSectionCoord(BlockPos.getZ(long1)));
    }
    
    public static long getZeroNode(final long long1) {
        return long1 & 0xFFFFFFFFFFF00000L;
    }
    
    public BlockPos origin() {
        return new BlockPos(sectionToBlockCoord(this.x()), sectionToBlockCoord(this.y()), sectionToBlockCoord(this.z()));
    }
    
    public BlockPos center() {
        final int integer2 = 8;
        return this.origin().offset(8, 8, 8);
    }
    
    public ChunkPos chunk() {
        return new ChunkPos(this.x(), this.z());
    }
    
    public static long asLong(final int integer1, final int integer2, final int integer3) {
        long long4 = 0L;
        long4 |= ((long)integer1 & 0x3FFFFFL) << 42;
        long4 |= ((long)integer2 & 0xFFFFFL) << 0;
        long4 |= ((long)integer3 & 0x3FFFFFL) << 20;
        return long4;
    }
    
    public long asLong() {
        return asLong(this.x(), this.y(), this.z());
    }
    
    public Stream<BlockPos> blocksInside() {
        return BlockPos.betweenClosedStream(this.minBlockX(), this.minBlockY(), this.minBlockZ(), this.maxBlockX(), this.maxBlockY(), this.maxBlockZ());
    }
    
    public static Stream<SectionPos> cube(final SectionPos gp, final int integer) {
        final int integer2 = gp.x();
        final int integer3 = gp.y();
        final int integer4 = gp.z();
        return betweenClosedStream(integer2 - integer, integer3 - integer, integer4 - integer, integer2 + integer, integer3 + integer, integer4 + integer);
    }
    
    public static Stream<SectionPos> aroundChunk(final ChunkPos bra, final int integer) {
        final int integer2 = bra.x;
        final int integer3 = bra.z;
        return betweenClosedStream(integer2 - integer, 0, integer3 - integer, integer2 + integer, 15, integer3 + integer);
    }
    
    public static Stream<SectionPos> betweenClosedStream(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        return (Stream<SectionPos>)StreamSupport.stream((Spliterator)new Spliterators.AbstractSpliterator<SectionPos>((long)((integer4 - integer1 + 1) * (integer5 - integer2 + 1) * (integer6 - integer3 + 1)), 64) {
            final Cursor3D cursor = new Cursor3D(integer1, integer2, integer3, integer4, integer5, integer6);
            
            public boolean tryAdvance(final Consumer<? super SectionPos> consumer) {
                if (this.cursor.advance()) {
                    consumer.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ(), null));
                    return true;
                }
                return false;
            }
        }, false);
    }
}
