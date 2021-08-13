package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import java.util.function.Supplier;
import com.google.common.annotations.VisibleForTesting;
import java.util.Objects;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import com.google.common.math.DoubleMath;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import java.util.Iterator;
import java.util.stream.Stream;
import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;
import net.minecraft.Util;
import java.util.Arrays;
import com.google.common.math.IntMath;
import net.minecraft.world.phys.AABB;

public final class Shapes {
    private static final VoxelShape BLOCK;
    public static final VoxelShape INFINITY;
    private static final VoxelShape EMPTY;
    
    public static VoxelShape empty() {
        return Shapes.EMPTY;
    }
    
    public static VoxelShape block() {
        return Shapes.BLOCK;
    }
    
    public static VoxelShape box(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        return create(new AABB(double1, double2, double3, double4, double5, double6));
    }
    
    public static VoxelShape create(final AABB dcf) {
        final int integer2 = findBits(dcf.minX, dcf.maxX);
        final int integer3 = findBits(dcf.minY, dcf.maxY);
        final int integer4 = findBits(dcf.minZ, dcf.maxZ);
        if (integer2 < 0 || integer3 < 0 || integer4 < 0) {
            return new ArrayVoxelShape(Shapes.BLOCK.shape, new double[] { dcf.minX, dcf.maxX }, new double[] { dcf.minY, dcf.maxY }, new double[] { dcf.minZ, dcf.maxZ });
        }
        if (integer2 == 0 && integer3 == 0 && integer4 == 0) {
            return dcf.contains(0.5, 0.5, 0.5) ? block() : empty();
        }
        final int integer5 = 1 << integer2;
        final int integer6 = 1 << integer3;
        final int integer7 = 1 << integer4;
        final int integer8 = (int)Math.round(dcf.minX * integer5);
        final int integer9 = (int)Math.round(dcf.maxX * integer5);
        final int integer10 = (int)Math.round(dcf.minY * integer6);
        final int integer11 = (int)Math.round(dcf.maxY * integer6);
        final int integer12 = (int)Math.round(dcf.minZ * integer7);
        final int integer13 = (int)Math.round(dcf.maxZ * integer7);
        final BitSetDiscreteVoxelShape dcn14 = new BitSetDiscreteVoxelShape(integer5, integer6, integer7, integer8, integer10, integer12, integer9, integer11, integer13);
        for (long long15 = integer8; long15 < integer9; ++long15) {
            for (long long16 = integer10; long16 < integer11; ++long16) {
                for (long long17 = integer12; long17 < integer13; ++long17) {
                    dcn14.setFull((int)long15, (int)long16, (int)long17, false, true);
                }
            }
        }
        return new CubeVoxelShape(dcn14);
    }
    
    private static int findBits(final double double1, final double double2) {
        if (double1 < -1.0E-7 || double2 > 1.0000001) {
            return -1;
        }
        for (int integer5 = 0; integer5 <= 3; ++integer5) {
            final double double3 = double1 * (1 << integer5);
            final double double4 = double2 * (1 << integer5);
            final boolean boolean10 = Math.abs(double3 - Math.floor(double3)) < 1.0E-7;
            final boolean boolean11 = Math.abs(double4 - Math.floor(double4)) < 1.0E-7;
            if (boolean10 && boolean11) {
                return integer5;
            }
        }
        return -1;
    }
    
    protected static long lcm(final int integer1, final int integer2) {
        return integer1 * (long)(integer2 / IntMath.gcd(integer1, integer2));
    }
    
    public static VoxelShape or(final VoxelShape dde1, final VoxelShape dde2) {
        return join(dde1, dde2, BooleanOp.OR);
    }
    
    public static VoxelShape or(final VoxelShape dde, final VoxelShape... arr) {
        return (VoxelShape)Arrays.stream((Object[])arr).reduce(dde, Shapes::or);
    }
    
    public static VoxelShape join(final VoxelShape dde1, final VoxelShape dde2, final BooleanOp dco) {
        return joinUnoptimized(dde1, dde2, dco).optimize();
    }
    
    public static VoxelShape joinUnoptimized(final VoxelShape dde1, final VoxelShape dde2, final BooleanOp dco) {
        if (dco.apply(false, false)) {
            throw Util.<IllegalArgumentException>pauseInIde(new IllegalArgumentException());
        }
        if (dde1 == dde2) {
            return dco.apply(true, true) ? dde1 : empty();
        }
        final boolean boolean4 = dco.apply(true, false);
        final boolean boolean5 = dco.apply(false, true);
        if (dde1.isEmpty()) {
            return boolean5 ? dde2 : empty();
        }
        if (dde2.isEmpty()) {
            return boolean4 ? dde1 : empty();
        }
        final IndexMerger dcw6 = createIndexMerger(1, dde1.getCoords(Direction.Axis.X), dde2.getCoords(Direction.Axis.X), boolean4, boolean5);
        final IndexMerger dcw7 = createIndexMerger(dcw6.getList().size() - 1, dde1.getCoords(Direction.Axis.Y), dde2.getCoords(Direction.Axis.Y), boolean4, boolean5);
        final IndexMerger dcw8 = createIndexMerger((dcw6.getList().size() - 1) * (dcw7.getList().size() - 1), dde1.getCoords(Direction.Axis.Z), dde2.getCoords(Direction.Axis.Z), boolean4, boolean5);
        final BitSetDiscreteVoxelShape dcn9 = BitSetDiscreteVoxelShape.join(dde1.shape, dde2.shape, dcw6, dcw7, dcw8, dco);
        if (dcw6 instanceof DiscreteCubeMerger && dcw7 instanceof DiscreteCubeMerger && dcw8 instanceof DiscreteCubeMerger) {
            return new CubeVoxelShape(dcn9);
        }
        return new ArrayVoxelShape(dcn9, dcw6.getList(), dcw7.getList(), dcw8.getList());
    }
    
    public static boolean joinIsNotEmpty(final VoxelShape dde1, final VoxelShape dde2, final BooleanOp dco) {
        if (dco.apply(false, false)) {
            throw Util.<IllegalArgumentException>pauseInIde(new IllegalArgumentException());
        }
        if (dde1 == dde2) {
            return dco.apply(true, true);
        }
        if (dde1.isEmpty()) {
            return dco.apply(false, !dde2.isEmpty());
        }
        if (dde2.isEmpty()) {
            return dco.apply(!dde1.isEmpty(), false);
        }
        final boolean boolean4 = dco.apply(true, false);
        final boolean boolean5 = dco.apply(false, true);
        for (final Direction.Axis a9 : AxisCycle.AXIS_VALUES) {
            if (dde1.max(a9) < dde2.min(a9) - 1.0E-7) {
                return boolean4 || boolean5;
            }
            if (dde2.max(a9) < dde1.min(a9) - 1.0E-7) {
                return boolean4 || boolean5;
            }
        }
        final IndexMerger dcw6 = createIndexMerger(1, dde1.getCoords(Direction.Axis.X), dde2.getCoords(Direction.Axis.X), boolean4, boolean5);
        final IndexMerger dcw7 = createIndexMerger(dcw6.getList().size() - 1, dde1.getCoords(Direction.Axis.Y), dde2.getCoords(Direction.Axis.Y), boolean4, boolean5);
        final IndexMerger dcw8 = createIndexMerger((dcw6.getList().size() - 1) * (dcw7.getList().size() - 1), dde1.getCoords(Direction.Axis.Z), dde2.getCoords(Direction.Axis.Z), boolean4, boolean5);
        return joinIsNotEmpty(dcw6, dcw7, dcw8, dde1.shape, dde2.shape, dco);
    }
    
    private static boolean joinIsNotEmpty(final IndexMerger dcw1, final IndexMerger dcw2, final IndexMerger dcw3, final DiscreteVoxelShape dct4, final DiscreteVoxelShape dct5, final BooleanOp dco) {
        return !dcw1.forMergedIndexes((integer6, integer7, integer8) -> dcw2.forMergedIndexes((integer7, integer8, integer9) -> dcw3.forMergedIndexes((integer8, integer9, integer10) -> !dco.apply(dct4.isFullWide(integer6, integer7, integer8), dct5.isFullWide(integer7, integer8, integer9)))));
    }
    
    public static double collide(final Direction.Axis a, final AABB dcf, final Stream<VoxelShape> stream, double double4) {
        final Iterator<VoxelShape> iterator6 = (Iterator<VoxelShape>)stream.iterator();
        while (iterator6.hasNext()) {
            if (Math.abs(double4) < 1.0E-7) {
                return 0.0;
            }
            double4 = ((VoxelShape)iterator6.next()).collide(a, dcf, double4);
        }
        return double4;
    }
    
    public static double collide(final Direction.Axis a, final AABB dcf, final LevelReader brw, final double double4, final CollisionContext dcp, final Stream<VoxelShape> stream) {
        return collide(dcf, brw, double4, dcp, AxisCycle.between(a, Direction.Axis.Z), stream);
    }
    
    private static double collide(final AABB dcf, final LevelReader brw, double double3, final CollisionContext dcp, final AxisCycle fv, final Stream<VoxelShape> stream) {
        if (dcf.getXsize() < 1.0E-6 || dcf.getYsize() < 1.0E-6 || dcf.getZsize() < 1.0E-6) {
            return double3;
        }
        if (Math.abs(double3) < 1.0E-7) {
            return 0.0;
        }
        final AxisCycle fv2 = fv.inverse();
        final Direction.Axis a9 = fv2.cycle(Direction.Axis.X);
        final Direction.Axis a10 = fv2.cycle(Direction.Axis.Y);
        final Direction.Axis a11 = fv2.cycle(Direction.Axis.Z);
        final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
        final int integer13 = Mth.floor(dcf.min(a9) - 1.0E-7) - 1;
        final int integer14 = Mth.floor(dcf.max(a9) + 1.0E-7) + 1;
        final int integer15 = Mth.floor(dcf.min(a10) - 1.0E-7) - 1;
        final int integer16 = Mth.floor(dcf.max(a10) + 1.0E-7) + 1;
        final double double4 = dcf.min(a11) - 1.0E-7;
        final double double5 = dcf.max(a11) + 1.0E-7;
        final boolean boolean21 = double3 > 0.0;
        final int integer17 = boolean21 ? (Mth.floor(dcf.max(a11) - 1.0E-7) - 1) : (Mth.floor(dcf.min(a11) + 1.0E-7) + 1);
        int integer18 = lastC(double3, double4, double5);
        final int integer19 = boolean21 ? 1 : -1;
        int integer20 = integer17;
        while (true) {
            if (boolean21) {
                if (integer20 > integer18) {
                    break;
                }
            }
            else if (integer20 < integer18) {
                break;
            }
            for (int integer21 = integer13; integer21 <= integer14; ++integer21) {
                for (int integer22 = integer15; integer22 <= integer16; ++integer22) {
                    int integer23 = 0;
                    if (integer21 == integer13 || integer21 == integer14) {
                        ++integer23;
                    }
                    if (integer22 == integer15 || integer22 == integer16) {
                        ++integer23;
                    }
                    if (integer20 == integer17 || integer20 == integer18) {
                        ++integer23;
                    }
                    if (integer23 < 3) {
                        a12.set(fv2, integer21, integer22, integer20);
                        final BlockState cee29 = brw.getBlockState(a12);
                        if (integer23 != 1 || cee29.hasLargeCollisionShape()) {
                            if (integer23 != 2 || cee29.is(Blocks.MOVING_PISTON)) {
                                double3 = cee29.getCollisionShape(brw, a12, dcp).collide(a11, dcf.move(-a12.getX(), -a12.getY(), -a12.getZ()), double3);
                                if (Math.abs(double3) < 1.0E-7) {
                                    return 0.0;
                                }
                                integer18 = lastC(double3, double4, double5);
                            }
                        }
                    }
                }
            }
            integer20 += integer19;
        }
        final double[] arr25 = { double3 };
        stream.forEach(dde -> arr25[0] = dde.collide(a11, dcf, arr25[0]));
        return arr25[0];
    }
    
    private static int lastC(final double double1, final double double2, final double double3) {
        return (double1 > 0.0) ? (Mth.floor(double3 + double1) + 1) : (Mth.floor(double2 + double1) - 1);
    }
    
    public static boolean blockOccudes(final VoxelShape dde1, final VoxelShape dde2, final Direction gc) {
        if (dde1 == block() && dde2 == block()) {
            return true;
        }
        if (dde2.isEmpty()) {
            return false;
        }
        final Direction.Axis a4 = gc.getAxis();
        final Direction.AxisDirection b5 = gc.getAxisDirection();
        final VoxelShape dde3 = (b5 == Direction.AxisDirection.POSITIVE) ? dde1 : dde2;
        final VoxelShape dde4 = (b5 == Direction.AxisDirection.POSITIVE) ? dde2 : dde1;
        final BooleanOp dco8 = (b5 == Direction.AxisDirection.POSITIVE) ? BooleanOp.ONLY_FIRST : BooleanOp.ONLY_SECOND;
        return DoubleMath.fuzzyEquals(dde3.max(a4), 1.0, 1.0E-7) && DoubleMath.fuzzyEquals(dde4.min(a4), 0.0, 1.0E-7) && !joinIsNotEmpty(new SliceShape(dde3, a4, dde3.shape.getSize(a4) - 1), new SliceShape(dde4, a4, 0), dco8);
    }
    
    public static VoxelShape getFaceShape(final VoxelShape dde, final Direction gc) {
        if (dde == block()) {
            return block();
        }
        final Direction.Axis a5 = gc.getAxis();
        boolean boolean3;
        int integer4;
        if (gc.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
            boolean3 = DoubleMath.fuzzyEquals(dde.max(a5), 1.0, 1.0E-7);
            integer4 = dde.shape.getSize(a5) - 1;
        }
        else {
            boolean3 = DoubleMath.fuzzyEquals(dde.min(a5), 0.0, 1.0E-7);
            integer4 = 0;
        }
        if (!boolean3) {
            return empty();
        }
        return new SliceShape(dde, a5, integer4);
    }
    
    public static boolean mergedFaceOccludes(final VoxelShape dde1, final VoxelShape dde2, final Direction gc) {
        if (dde1 == block() || dde2 == block()) {
            return true;
        }
        final Direction.Axis a4 = gc.getAxis();
        final Direction.AxisDirection b5 = gc.getAxisDirection();
        VoxelShape dde3 = (b5 == Direction.AxisDirection.POSITIVE) ? dde1 : dde2;
        VoxelShape dde4 = (b5 == Direction.AxisDirection.POSITIVE) ? dde2 : dde1;
        if (!DoubleMath.fuzzyEquals(dde3.max(a4), 1.0, 1.0E-7)) {
            dde3 = empty();
        }
        if (!DoubleMath.fuzzyEquals(dde4.min(a4), 0.0, 1.0E-7)) {
            dde4 = empty();
        }
        return !joinIsNotEmpty(block(), joinUnoptimized(new SliceShape(dde3, a4, dde3.shape.getSize(a4) - 1), new SliceShape(dde4, a4, 0), BooleanOp.OR), BooleanOp.ONLY_FIRST);
    }
    
    public static boolean faceShapeOccludes(final VoxelShape dde1, final VoxelShape dde2) {
        return dde1 == block() || dde2 == block() || ((!dde1.isEmpty() || !dde2.isEmpty()) && !joinIsNotEmpty(block(), joinUnoptimized(dde1, dde2, BooleanOp.OR), BooleanOp.ONLY_FIRST));
    }
    
    @VisibleForTesting
    protected static IndexMerger createIndexMerger(final int integer, final DoubleList doubleList2, final DoubleList doubleList3, final boolean boolean4, final boolean boolean5) {
        final int integer2 = doubleList2.size() - 1;
        final int integer3 = doubleList3.size() - 1;
        if (doubleList2 instanceof CubePointRange && doubleList3 instanceof CubePointRange) {
            final long long8 = lcm(integer2, integer3);
            if (integer * long8 <= 256L) {
                return new DiscreteCubeMerger(integer2, integer3);
            }
        }
        if (doubleList2.getDouble(integer2) < doubleList3.getDouble(0) - 1.0E-7) {
            return new NonOverlappingMerger(doubleList2, doubleList3, false);
        }
        if (doubleList3.getDouble(integer3) < doubleList2.getDouble(0) - 1.0E-7) {
            return new NonOverlappingMerger(doubleList3, doubleList2, true);
        }
        if (integer2 != integer3 || !Objects.equals(doubleList2, doubleList3)) {
            return new IndirectMerger(doubleList2, doubleList3, boolean4, boolean5);
        }
        if (doubleList2 instanceof IdenticalMerger) {
            return (IndexMerger)doubleList2;
        }
        if (doubleList3 instanceof IdenticalMerger) {
            return (IndexMerger)doubleList3;
        }
        return new IdenticalMerger(doubleList2);
    }
    
    static {
        BLOCK = Util.<VoxelShape>make((java.util.function.Supplier<VoxelShape>)(() -> {
            final DiscreteVoxelShape dct1 = new BitSetDiscreteVoxelShape(1, 1, 1);
            dct1.setFull(0, 0, 0, true, true);
            return new CubeVoxelShape(dct1);
        }));
        INFINITY = box(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        EMPTY = new ArrayVoxelShape(new BitSetDiscreteVoxelShape(0, 0, 0), (DoubleList)new DoubleArrayList(new double[] { 0.0 }), (DoubleList)new DoubleArrayList(new double[] { 0.0 }), (DoubleList)new DoubleArrayList(new double[] { 0.0 }));
    }
    
    public interface DoubleLineConsumer {
        void consume(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6);
    }
}
