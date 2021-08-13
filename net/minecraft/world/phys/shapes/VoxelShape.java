package net.minecraft.world.phys.shapes;

import com.google.common.math.DoubleMath;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.AxisCycle;
import com.google.common.collect.Lists;
import java.util.List;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.Util;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;

public abstract class VoxelShape {
    protected final DiscreteVoxelShape shape;
    @Nullable
    private VoxelShape[] faces;
    
    VoxelShape(final DiscreteVoxelShape dct) {
        this.shape = dct;
    }
    
    public double min(final Direction.Axis a) {
        final int integer3 = this.shape.firstFull(a);
        if (integer3 >= this.shape.getSize(a)) {
            return Double.POSITIVE_INFINITY;
        }
        return this.get(a, integer3);
    }
    
    public double max(final Direction.Axis a) {
        final int integer3 = this.shape.lastFull(a);
        if (integer3 <= 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return this.get(a, integer3);
    }
    
    public AABB bounds() {
        if (this.isEmpty()) {
            throw Util.<UnsupportedOperationException>pauseInIde(new UnsupportedOperationException("No bounds for empty shape."));
        }
        return new AABB(this.min(Direction.Axis.X), this.min(Direction.Axis.Y), this.min(Direction.Axis.Z), this.max(Direction.Axis.X), this.max(Direction.Axis.Y), this.max(Direction.Axis.Z));
    }
    
    protected double get(final Direction.Axis a, final int integer) {
        return this.getCoords(a).getDouble(integer);
    }
    
    protected abstract DoubleList getCoords(final Direction.Axis a);
    
    public boolean isEmpty() {
        return this.shape.isEmpty();
    }
    
    public VoxelShape move(final double double1, final double double2, final double double3) {
        if (this.isEmpty()) {
            return Shapes.empty();
        }
        return new ArrayVoxelShape(this.shape, (DoubleList)new OffsetDoubleList(this.getCoords(Direction.Axis.X), double1), (DoubleList)new OffsetDoubleList(this.getCoords(Direction.Axis.Y), double2), (DoubleList)new OffsetDoubleList(this.getCoords(Direction.Axis.Z), double3));
    }
    
    public VoxelShape optimize() {
        final VoxelShape[] arr2 = { Shapes.empty() };
        final Object o;
        this.forAllBoxes((double2, double3, double4, double5, double6, double7) -> o[0] = Shapes.joinUnoptimized(o[0], Shapes.box(double2, double3, double4, double5, double6, double7), BooleanOp.OR));
        return arr2[0];
    }
    
    public void forAllEdges(final Shapes.DoubleLineConsumer a) {
        this.shape.forAllEdges((integer2, integer3, integer4, integer5, integer6, integer7) -> a.consume(this.get(Direction.Axis.X, integer2), this.get(Direction.Axis.Y, integer3), this.get(Direction.Axis.Z, integer4), this.get(Direction.Axis.X, integer5), this.get(Direction.Axis.Y, integer6), this.get(Direction.Axis.Z, integer7)), true);
    }
    
    public void forAllBoxes(final Shapes.DoubleLineConsumer a) {
        final DoubleList doubleList3 = this.getCoords(Direction.Axis.X);
        final DoubleList doubleList4 = this.getCoords(Direction.Axis.Y);
        final DoubleList doubleList5 = this.getCoords(Direction.Axis.Z);
        final DoubleList list;
        final DoubleList list2;
        final DoubleList list3;
        this.shape.forAllBoxes((integer5, integer6, integer7, integer8, integer9, integer10) -> a.consume(list.getDouble(integer5), list2.getDouble(integer6), list3.getDouble(integer7), list.getDouble(integer8), list2.getDouble(integer9), list3.getDouble(integer10)), true);
    }
    
    public List<AABB> toAabbs() {
        final List<AABB> list2 = (List<AABB>)Lists.newArrayList();
        this.forAllBoxes((double2, double3, double4, double5, double6, double7) -> list2.add(new AABB(double2, double3, double4, double5, double6, double7)));
        return list2;
    }
    
    public double max(final Direction.Axis a, final double double2, final double double3) {
        final Direction.Axis a2 = AxisCycle.FORWARD.cycle(a);
        final Direction.Axis a3 = AxisCycle.BACKWARD.cycle(a);
        final int integer9 = this.findIndex(a2, double2);
        final int integer10 = this.findIndex(a3, double3);
        final int integer11 = this.shape.lastFull(a, integer9, integer10);
        if (integer11 <= 0) {
            return Double.NEGATIVE_INFINITY;
        }
        return this.get(a, integer11);
    }
    
    protected int findIndex(final Direction.Axis a, final double double2) {
        return Mth.binarySearch(0, this.shape.getSize(a) + 1, integer -> integer >= 0 && (integer > this.shape.getSize(a) || double2 < this.get(a, integer))) - 1;
    }
    
    protected boolean isFullWide(final double double1, final double double2, final double double3) {
        return this.shape.isFullWide(this.findIndex(Direction.Axis.X, double1), this.findIndex(Direction.Axis.Y, double2), this.findIndex(Direction.Axis.Z, double3));
    }
    
    @Nullable
    public BlockHitResult clip(final Vec3 dck1, final Vec3 dck2, final BlockPos fx) {
        if (this.isEmpty()) {
            return null;
        }
        final Vec3 dck3 = dck2.subtract(dck1);
        if (dck3.lengthSqr() < 1.0E-7) {
            return null;
        }
        final Vec3 dck4 = dck1.add(dck3.scale(0.001));
        if (this.isFullWide(dck4.x - fx.getX(), dck4.y - fx.getY(), dck4.z - fx.getZ())) {
            return new BlockHitResult(dck4, Direction.getNearest(dck3.x, dck3.y, dck3.z).getOpposite(), fx, true);
        }
        return AABB.clip((Iterable<AABB>)this.toAabbs(), dck1, dck2, fx);
    }
    
    public VoxelShape getFaceShape(final Direction gc) {
        if (this.isEmpty() || this == Shapes.block()) {
            return this;
        }
        if (this.faces != null) {
            final VoxelShape dde3 = this.faces[gc.ordinal()];
            if (dde3 != null) {
                return dde3;
            }
        }
        else {
            this.faces = new VoxelShape[6];
        }
        final VoxelShape dde3 = this.calculateFace(gc);
        return this.faces[gc.ordinal()] = dde3;
    }
    
    private VoxelShape calculateFace(final Direction gc) {
        final Direction.Axis a3 = gc.getAxis();
        final Direction.AxisDirection b4 = gc.getAxisDirection();
        final DoubleList doubleList5 = this.getCoords(a3);
        if (doubleList5.size() == 2 && DoubleMath.fuzzyEquals(doubleList5.getDouble(0), 0.0, 1.0E-7) && DoubleMath.fuzzyEquals(doubleList5.getDouble(1), 1.0, 1.0E-7)) {
            return this;
        }
        final int integer6 = this.findIndex(a3, (b4 == Direction.AxisDirection.POSITIVE) ? 0.9999999 : 1.0E-7);
        return new SliceShape(this, a3, integer6);
    }
    
    public double collide(final Direction.Axis a, final AABB dcf, final double double3) {
        return this.collideX(AxisCycle.between(a, Direction.Axis.X), dcf, double3);
    }
    
    protected double collideX(final AxisCycle fv, final AABB dcf, double double3) {
        if (this.isEmpty()) {
            return double3;
        }
        if (Math.abs(double3) < 1.0E-7) {
            return 0.0;
        }
        final AxisCycle fv2 = fv.inverse();
        final Direction.Axis a7 = fv2.cycle(Direction.Axis.X);
        final Direction.Axis a8 = fv2.cycle(Direction.Axis.Y);
        final Direction.Axis a9 = fv2.cycle(Direction.Axis.Z);
        final double double4 = dcf.max(a7);
        final double double5 = dcf.min(a7);
        final int integer14 = this.findIndex(a7, double5 + 1.0E-7);
        final int integer15 = this.findIndex(a7, double4 - 1.0E-7);
        final int integer16 = Math.max(0, this.findIndex(a8, dcf.min(a8) + 1.0E-7));
        final int integer17 = Math.min(this.shape.getSize(a8), this.findIndex(a8, dcf.max(a8) - 1.0E-7) + 1);
        final int integer18 = Math.max(0, this.findIndex(a9, dcf.min(a9) + 1.0E-7));
        final int integer19 = Math.min(this.shape.getSize(a9), this.findIndex(a9, dcf.max(a9) - 1.0E-7) + 1);
        final int integer20 = this.shape.getSize(a7);
        if (double3 > 0.0) {
            for (int integer21 = integer15 + 1; integer21 < integer20; ++integer21) {
                for (int integer22 = integer16; integer22 < integer17; ++integer22) {
                    for (int integer23 = integer18; integer23 < integer19; ++integer23) {
                        if (this.shape.isFullWide(fv2, integer21, integer22, integer23)) {
                            final double double6 = this.get(a7, integer21) - double4;
                            if (double6 >= -1.0E-7) {
                                double3 = Math.min(double3, double6);
                            }
                            return double3;
                        }
                    }
                }
            }
        }
        else if (double3 < 0.0) {
            for (int integer21 = integer14 - 1; integer21 >= 0; --integer21) {
                for (int integer22 = integer16; integer22 < integer17; ++integer22) {
                    for (int integer23 = integer18; integer23 < integer19; ++integer23) {
                        if (this.shape.isFullWide(fv2, integer21, integer22, integer23)) {
                            final double double6 = this.get(a7, integer21 + 1) - double5;
                            if (double6 <= 1.0E-7) {
                                double3 = Math.max(double3, double6);
                            }
                            return double3;
                        }
                    }
                }
            }
        }
        return double3;
    }
    
    public String toString() {
        return this.isEmpty() ? "EMPTY" : new StringBuilder().append("VoxelShape[").append(this.bounds()).append("]").toString();
    }
}
