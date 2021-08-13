package net.minecraft.core;

import org.apache.logging.log4j.LogManager;
import net.minecraft.Util;
import com.mojang.serialization.DataResult;
import java.util.stream.IntStream;
import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import org.apache.commons.lang3.Validate;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.stream.StreamSupport;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.Random;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Logger;
import com.mojang.serialization.Codec;
import javax.annotation.concurrent.Immutable;

@Immutable
public class BlockPos extends Vec3i {
    public static final Codec<BlockPos> CODEC;
    private static final Logger LOGGER;
    public static final BlockPos ZERO;
    private static final int PACKED_X_LENGTH;
    private static final int PACKED_Z_LENGTH;
    private static final int PACKED_Y_LENGTH;
    private static final long PACKED_X_MASK;
    private static final long PACKED_Y_MASK;
    private static final long PACKED_Z_MASK;
    private static final int Z_OFFSET;
    private static final int X_OFFSET;
    
    public BlockPos(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
    }
    
    public BlockPos(final double double1, final double double2, final double double3) {
        super(double1, double2, double3);
    }
    
    public BlockPos(final Vec3 dck) {
        this(dck.x, dck.y, dck.z);
    }
    
    public BlockPos(final Position gk) {
        this(gk.x(), gk.y(), gk.z());
    }
    
    public BlockPos(final Vec3i gr) {
        this(gr.getX(), gr.getY(), gr.getZ());
    }
    
    public static long offset(final long long1, final Direction gc) {
        return offset(long1, gc.getStepX(), gc.getStepY(), gc.getStepZ());
    }
    
    public static long offset(final long long1, final int integer2, final int integer3, final int integer4) {
        return asLong(getX(long1) + integer2, getY(long1) + integer3, getZ(long1) + integer4);
    }
    
    public static int getX(final long long1) {
        return (int)(long1 << 64 - BlockPos.X_OFFSET - BlockPos.PACKED_X_LENGTH >> 64 - BlockPos.PACKED_X_LENGTH);
    }
    
    public static int getY(final long long1) {
        return (int)(long1 << 64 - BlockPos.PACKED_Y_LENGTH >> 64 - BlockPos.PACKED_Y_LENGTH);
    }
    
    public static int getZ(final long long1) {
        return (int)(long1 << 64 - BlockPos.Z_OFFSET - BlockPos.PACKED_Z_LENGTH >> 64 - BlockPos.PACKED_Z_LENGTH);
    }
    
    public static BlockPos of(final long long1) {
        return new BlockPos(getX(long1), getY(long1), getZ(long1));
    }
    
    public long asLong() {
        return asLong(this.getX(), this.getY(), this.getZ());
    }
    
    public static long asLong(final int integer1, final int integer2, final int integer3) {
        long long4 = 0L;
        long4 |= ((long)integer1 & BlockPos.PACKED_X_MASK) << BlockPos.X_OFFSET;
        long4 |= ((long)integer2 & BlockPos.PACKED_Y_MASK) << 0;
        long4 |= ((long)integer3 & BlockPos.PACKED_Z_MASK) << BlockPos.Z_OFFSET;
        return long4;
    }
    
    public static long getFlatIndex(final long long1) {
        return long1 & 0xFFFFFFFFFFFFFFF0L;
    }
    
    public BlockPos offset(final double double1, final double double2, final double double3) {
        if (double1 == 0.0 && double2 == 0.0 && double3 == 0.0) {
            return this;
        }
        return new BlockPos(this.getX() + double1, this.getY() + double2, this.getZ() + double3);
    }
    
    public BlockPos offset(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 0 && integer2 == 0 && integer3 == 0) {
            return this;
        }
        return new BlockPos(this.getX() + integer1, this.getY() + integer2, this.getZ() + integer3);
    }
    
    public BlockPos offset(final Vec3i gr) {
        return this.offset(gr.getX(), gr.getY(), gr.getZ());
    }
    
    public BlockPos subtract(final Vec3i gr) {
        return this.offset(-gr.getX(), -gr.getY(), -gr.getZ());
    }
    
    @Override
    public BlockPos above() {
        return this.relative(Direction.UP);
    }
    
    @Override
    public BlockPos above(final int integer) {
        return this.relative(Direction.UP, integer);
    }
    
    @Override
    public BlockPos below() {
        return this.relative(Direction.DOWN);
    }
    
    @Override
    public BlockPos below(final int integer) {
        return this.relative(Direction.DOWN, integer);
    }
    
    public BlockPos north() {
        return this.relative(Direction.NORTH);
    }
    
    public BlockPos north(final int integer) {
        return this.relative(Direction.NORTH, integer);
    }
    
    public BlockPos south() {
        return this.relative(Direction.SOUTH);
    }
    
    public BlockPos south(final int integer) {
        return this.relative(Direction.SOUTH, integer);
    }
    
    public BlockPos west() {
        return this.relative(Direction.WEST);
    }
    
    public BlockPos west(final int integer) {
        return this.relative(Direction.WEST, integer);
    }
    
    public BlockPos east() {
        return this.relative(Direction.EAST);
    }
    
    public BlockPos east(final int integer) {
        return this.relative(Direction.EAST, integer);
    }
    
    public BlockPos relative(final Direction gc) {
        return new BlockPos(this.getX() + gc.getStepX(), this.getY() + gc.getStepY(), this.getZ() + gc.getStepZ());
    }
    
    @Override
    public BlockPos relative(final Direction gc, final int integer) {
        if (integer == 0) {
            return this;
        }
        return new BlockPos(this.getX() + gc.getStepX() * integer, this.getY() + gc.getStepY() * integer, this.getZ() + gc.getStepZ() * integer);
    }
    
    public BlockPos relative(final Direction.Axis a, final int integer) {
        if (integer == 0) {
            return this;
        }
        final int integer2 = (a == Direction.Axis.X) ? integer : 0;
        final int integer3 = (a == Direction.Axis.Y) ? integer : 0;
        final int integer4 = (a == Direction.Axis.Z) ? integer : 0;
        return new BlockPos(this.getX() + integer2, this.getY() + integer3, this.getZ() + integer4);
    }
    
    public BlockPos rotate(final Rotation bzj) {
        switch (bzj) {
            default: {
                return this;
            }
            case CLOCKWISE_90: {
                return new BlockPos(-this.getZ(), this.getY(), this.getX());
            }
            case CLOCKWISE_180: {
                return new BlockPos(-this.getX(), this.getY(), -this.getZ());
            }
            case COUNTERCLOCKWISE_90: {
                return new BlockPos(this.getZ(), this.getY(), -this.getX());
            }
        }
    }
    
    public BlockPos d(final Vec3i gr) {
        return new BlockPos(this.getY() * gr.getZ() - this.getZ() * gr.getY(), this.getZ() * gr.getX() - this.getX() * gr.getZ(), this.getX() * gr.getY() - this.getY() * gr.getX());
    }
    
    public BlockPos immutable() {
        return this;
    }
    
    public MutableBlockPos mutable() {
        return new MutableBlockPos(this.getX(), this.getY(), this.getZ());
    }
    
    public static Iterable<BlockPos> randomBetweenClosed(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
        final int integer9 = integer6 - integer3 + 1;
        final int integer10 = integer7 - integer4 + 1;
        final int integer11 = integer8 - integer5 + 1;
        return (Iterable<BlockPos>)(() -> new AbstractIterator<BlockPos>() {
            final MutableBlockPos nextPos;
            int counter;
            final /* synthetic */ int val$limit;
            final /* synthetic */ int val$minX;
            final /* synthetic */ Random val$random;
            final /* synthetic */ int val$width;
            final /* synthetic */ int val$minY;
            final /* synthetic */ int val$height;
            final /* synthetic */ int val$minZ;
            final /* synthetic */ int val$depth;
            
            {
                this.nextPos = new MutableBlockPos();
                this.counter = this.val$limit;
            }
            
            protected BlockPos computeNext() {
                if (this.counter <= 0) {
                    return (BlockPos)this.endOfData();
                }
                final BlockPos fx2 = this.nextPos.set(this.val$minX + this.val$random.nextInt(this.val$width), this.val$minY + this.val$random.nextInt(this.val$height), this.val$minZ + this.val$random.nextInt(this.val$depth));
                --this.counter;
                return fx2;
            }
        });
    }
    
    public static Iterable<BlockPos> withinManhattan(final BlockPos fx, final int integer2, final int integer3, final int integer4) {
        final int integer5 = integer2 + integer3 + integer4;
        final int integer6 = fx.getX();
        final int integer7 = fx.getY();
        final int integer8 = fx.getZ();
        return (Iterable<BlockPos>)(() -> new AbstractIterator<BlockPos>() {
            private final MutableBlockPos cursor;
            private int currentDepth;
            private int maxX;
            private int maxY;
            private int x;
            private int y;
            private boolean zMirror;
            final /* synthetic */ int val$originZ;
            final /* synthetic */ int val$maxDepth;
            final /* synthetic */ int val$reachX;
            final /* synthetic */ int val$reachY;
            final /* synthetic */ int val$reachZ;
            final /* synthetic */ int val$originX;
            final /* synthetic */ int val$originY;
            
            {
                this.cursor = new MutableBlockPos();
            }
            
            protected BlockPos computeNext() {
                if (this.zMirror) {
                    this.zMirror = false;
                    this.cursor.setZ(this.val$originZ - (this.cursor.getZ() - this.val$originZ));
                    return this.cursor;
                }
                BlockPos fx2 = null;
                while (fx2 == null) {
                    if (this.y > this.maxY) {
                        ++this.x;
                        if (this.x > this.maxX) {
                            ++this.currentDepth;
                            if (this.currentDepth > this.val$maxDepth) {
                                return (BlockPos)this.endOfData();
                            }
                            this.maxX = Math.min(this.val$reachX, this.currentDepth);
                            this.x = -this.maxX;
                        }
                        this.maxY = Math.min(this.val$reachY, this.currentDepth - Math.abs(this.x));
                        this.y = -this.maxY;
                    }
                    final int integer2 = this.x;
                    final int integer3 = this.y;
                    final int integer4 = this.currentDepth - Math.abs(integer2) - Math.abs(integer3);
                    if (integer4 <= this.val$reachZ) {
                        this.zMirror = (integer4 != 0);
                        fx2 = this.cursor.set(this.val$originX + integer2, this.val$originY + integer3, this.val$originZ + integer4);
                    }
                    ++this.y;
                }
                return fx2;
            }
        });
    }
    
    public static Optional<BlockPos> findClosestMatch(final BlockPos fx, final int integer2, final int integer3, final Predicate<BlockPos> predicate) {
        return (Optional<BlockPos>)withinManhattanStream(fx, integer2, integer3, integer2).filter((Predicate)predicate).findFirst();
    }
    
    public static Stream<BlockPos> withinManhattanStream(final BlockPos fx, final int integer2, final int integer3, final int integer4) {
        return (Stream<BlockPos>)StreamSupport.stream(withinManhattan(fx, integer2, integer3, integer4).spliterator(), false);
    }
    
    public static Iterable<BlockPos> betweenClosed(final BlockPos fx1, final BlockPos fx2) {
        return betweenClosed(Math.min(fx1.getX(), fx2.getX()), Math.min(fx1.getY(), fx2.getY()), Math.min(fx1.getZ(), fx2.getZ()), Math.max(fx1.getX(), fx2.getX()), Math.max(fx1.getY(), fx2.getY()), Math.max(fx1.getZ(), fx2.getZ()));
    }
    
    public static Stream<BlockPos> betweenClosedStream(final BlockPos fx1, final BlockPos fx2) {
        return (Stream<BlockPos>)StreamSupport.stream(betweenClosed(fx1, fx2).spliterator(), false);
    }
    
    public static Stream<BlockPos> betweenClosedStream(final BoundingBox cqx) {
        return betweenClosedStream(Math.min(cqx.x0, cqx.x1), Math.min(cqx.y0, cqx.y1), Math.min(cqx.z0, cqx.z1), Math.max(cqx.x0, cqx.x1), Math.max(cqx.y0, cqx.y1), Math.max(cqx.z0, cqx.z1));
    }
    
    public static Stream<BlockPos> betweenClosedStream(final AABB dcf) {
        return betweenClosedStream(Mth.floor(dcf.minX), Mth.floor(dcf.minY), Mth.floor(dcf.minZ), Mth.floor(dcf.maxX), Mth.floor(dcf.maxY), Mth.floor(dcf.maxZ));
    }
    
    public static Stream<BlockPos> betweenClosedStream(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        return (Stream<BlockPos>)StreamSupport.stream(betweenClosed(integer1, integer2, integer3, integer4, integer5, integer6).spliterator(), false);
    }
    
    public static Iterable<BlockPos> betweenClosed(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        final int integer7 = integer4 - integer1 + 1;
        final int integer8 = integer5 - integer2 + 1;
        final int integer9 = integer6 - integer3 + 1;
        final int integer10 = integer7 * integer8 * integer9;
        return (Iterable<BlockPos>)(() -> new AbstractIterator<BlockPos>() {
            private final MutableBlockPos cursor;
            private int index;
            final /* synthetic */ int val$end;
            final /* synthetic */ int val$width;
            final /* synthetic */ int val$height;
            final /* synthetic */ int val$minX;
            final /* synthetic */ int val$minY;
            final /* synthetic */ int val$minZ;
            
            {
                this.cursor = new MutableBlockPos();
            }
            
            protected BlockPos computeNext() {
                if (this.index == this.val$end) {
                    return (BlockPos)this.endOfData();
                }
                final int integer7 = this.index % this.val$width;
                final int integer8 = this.index / this.val$width;
                final int integer1 = integer8 % this.val$height;
                final int integer2 = integer8 / this.val$height;
                ++this.index;
                return this.cursor.set(this.val$minX + integer7, this.val$minY + integer1, this.val$minZ + integer2);
            }
        });
    }
    
    public static Iterable<MutableBlockPos> spiralAround(final BlockPos fx, final int integer, final Direction gc3, final Direction gc4) {
        Validate.validState(gc3.getAxis() != gc4.getAxis(), "The two directions cannot be on the same axis", new Object[0]);
        return (Iterable<MutableBlockPos>)(() -> new AbstractIterator<MutableBlockPos>() {
            private final Direction[] directions;
            private final MutableBlockPos cursor;
            private final int legs;
            private int leg;
            private int legSize;
            private int legIndex;
            private int lastX;
            private int lastY;
            private int lastZ;
            final /* synthetic */ Direction val$firstDirection;
            final /* synthetic */ Direction val$secondDirection;
            final /* synthetic */ BlockPos val$center;
            final /* synthetic */ int val$radius;
            
            {
                this.directions = new Direction[] { this.val$firstDirection, this.val$secondDirection, this.val$firstDirection.getOpposite(), this.val$secondDirection.getOpposite() };
                this.cursor = this.val$center.mutable().move(this.val$secondDirection);
                this.legs = 4 * this.val$radius;
                this.leg = -1;
                this.lastX = this.cursor.getX();
                this.lastY = this.cursor.getY();
                this.lastZ = this.cursor.getZ();
            }
            
            protected MutableBlockPos computeNext() {
                this.cursor.set(this.lastX, this.lastY, this.lastZ).move(this.directions[(this.leg + 4) % 4]);
                this.lastX = this.cursor.getX();
                this.lastY = this.cursor.getY();
                this.lastZ = this.cursor.getZ();
                if (this.legIndex >= this.legSize) {
                    if (this.leg >= this.legs) {
                        return (MutableBlockPos)this.endOfData();
                    }
                    ++this.leg;
                    this.legIndex = 0;
                    this.legSize = this.leg / 2 + 1;
                }
                ++this.legIndex;
                return this.cursor;
            }
        });
    }
    
    static {
        CODEC = Codec.INT_STREAM.comapFlatMap(intStream -> Util.fixedSize(intStream, 3).map(arr -> new BlockPos(arr[0], arr[1], arr[2])), fx -> IntStream.of(new int[] { fx.getX(), fx.getY(), fx.getZ() })).stable();
        LOGGER = LogManager.getLogger();
        ZERO = new BlockPos(0, 0, 0);
        PACKED_X_LENGTH = 1 + Mth.log2(Mth.smallestEncompassingPowerOfTwo(30000000));
        PACKED_Z_LENGTH = BlockPos.PACKED_X_LENGTH;
        PACKED_Y_LENGTH = 64 - BlockPos.PACKED_X_LENGTH - BlockPos.PACKED_Z_LENGTH;
        PACKED_X_MASK = (1L << BlockPos.PACKED_X_LENGTH) - 1L;
        PACKED_Y_MASK = (1L << BlockPos.PACKED_Y_LENGTH) - 1L;
        PACKED_Z_MASK = (1L << BlockPos.PACKED_Z_LENGTH) - 1L;
        Z_OFFSET = BlockPos.PACKED_Y_LENGTH;
        X_OFFSET = BlockPos.PACKED_Y_LENGTH + BlockPos.PACKED_Z_LENGTH;
    }
    
    public static class MutableBlockPos extends BlockPos {
        public MutableBlockPos() {
            this(0, 0, 0);
        }
        
        public MutableBlockPos(final int integer1, final int integer2, final int integer3) {
            super(integer1, integer2, integer3);
        }
        
        public MutableBlockPos(final double double1, final double double2, final double double3) {
            this(Mth.floor(double1), Mth.floor(double2), Mth.floor(double3));
        }
        
        @Override
        public BlockPos offset(final double double1, final double double2, final double double3) {
            return super.offset(double1, double2, double3).immutable();
        }
        
        @Override
        public BlockPos offset(final int integer1, final int integer2, final int integer3) {
            return super.offset(integer1, integer2, integer3).immutable();
        }
        
        @Override
        public BlockPos relative(final Direction gc, final int integer) {
            return super.relative(gc, integer).immutable();
        }
        
        @Override
        public BlockPos relative(final Direction.Axis a, final int integer) {
            return super.relative(a, integer).immutable();
        }
        
        @Override
        public BlockPos rotate(final Rotation bzj) {
            return super.rotate(bzj).immutable();
        }
        
        public MutableBlockPos set(final int integer1, final int integer2, final int integer3) {
            this.setX(integer1);
            this.setY(integer2);
            this.setZ(integer3);
            return this;
        }
        
        public MutableBlockPos set(final double double1, final double double2, final double double3) {
            return this.set(Mth.floor(double1), Mth.floor(double2), Mth.floor(double3));
        }
        
        public MutableBlockPos set(final Vec3i gr) {
            return this.set(gr.getX(), gr.getY(), gr.getZ());
        }
        
        public MutableBlockPos set(final long long1) {
            return this.set(BlockPos.getX(long1), BlockPos.getY(long1), BlockPos.getZ(long1));
        }
        
        public MutableBlockPos set(final AxisCycle fv, final int integer2, final int integer3, final int integer4) {
            return this.set(fv.cycle(integer2, integer3, integer4, Direction.Axis.X), fv.cycle(integer2, integer3, integer4, Direction.Axis.Y), fv.cycle(integer2, integer3, integer4, Direction.Axis.Z));
        }
        
        public MutableBlockPos setWithOffset(final Vec3i gr, final Direction gc) {
            return this.set(gr.getX() + gc.getStepX(), gr.getY() + gc.getStepY(), gr.getZ() + gc.getStepZ());
        }
        
        public MutableBlockPos setWithOffset(final Vec3i gr, final int integer2, final int integer3, final int integer4) {
            return this.set(gr.getX() + integer2, gr.getY() + integer3, gr.getZ() + integer4);
        }
        
        public MutableBlockPos move(final Direction gc) {
            return this.move(gc, 1);
        }
        
        public MutableBlockPos move(final Direction gc, final int integer) {
            return this.set(this.getX() + gc.getStepX() * integer, this.getY() + gc.getStepY() * integer, this.getZ() + gc.getStepZ() * integer);
        }
        
        public MutableBlockPos move(final int integer1, final int integer2, final int integer3) {
            return this.set(this.getX() + integer1, this.getY() + integer2, this.getZ() + integer3);
        }
        
        public MutableBlockPos move(final Vec3i gr) {
            return this.set(this.getX() + gr.getX(), this.getY() + gr.getY(), this.getZ() + gr.getZ());
        }
        
        public MutableBlockPos clamp(final Direction.Axis a, final int integer2, final int integer3) {
            switch (a) {
                case X: {
                    return this.set(Mth.clamp(this.getX(), integer2, integer3), this.getY(), this.getZ());
                }
                case Y: {
                    return this.set(this.getX(), Mth.clamp(this.getY(), integer2, integer3), this.getZ());
                }
                case Z: {
                    return this.set(this.getX(), this.getY(), Mth.clamp(this.getZ(), integer2, integer3));
                }
                default: {
                    throw new IllegalStateException(new StringBuilder().append("Unable to clamp axis ").append(a).toString());
                }
            }
        }
        
        public void setX(final int integer) {
            super.setX(integer);
        }
        
        public void setY(final int integer) {
            super.setY(integer);
        }
        
        public void setZ(final int integer) {
            super.setZ(integer);
        }
        
        @Override
        public BlockPos immutable() {
            return new BlockPos(this);
        }
    }
}
