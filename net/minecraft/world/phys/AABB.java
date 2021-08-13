package net.minecraft.world.phys;

import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;

public class AABB {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    
    public AABB(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        this.minX = Math.min(double1, double4);
        this.minY = Math.min(double2, double5);
        this.minZ = Math.min(double3, double6);
        this.maxX = Math.max(double1, double4);
        this.maxY = Math.max(double2, double5);
        this.maxZ = Math.max(double3, double6);
    }
    
    public AABB(final BlockPos fx) {
        this(fx.getX(), fx.getY(), fx.getZ(), fx.getX() + 1, fx.getY() + 1, fx.getZ() + 1);
    }
    
    public AABB(final BlockPos fx1, final BlockPos fx2) {
        this(fx1.getX(), fx1.getY(), fx1.getZ(), fx2.getX(), fx2.getY(), fx2.getZ());
    }
    
    public AABB(final Vec3 dck1, final Vec3 dck2) {
        this(dck1.x, dck1.y, dck1.z, dck2.x, dck2.y, dck2.z);
    }
    
    public static AABB of(final BoundingBox cqx) {
        return new AABB(cqx.x0, cqx.y0, cqx.z0, cqx.x1 + 1, cqx.y1 + 1, cqx.z1 + 1);
    }
    
    public static AABB unitCubeFromLowerCorner(final Vec3 dck) {
        return new AABB(dck.x, dck.y, dck.z, dck.x + 1.0, dck.y + 1.0, dck.z + 1.0);
    }
    
    public double min(final Direction.Axis a) {
        return a.choose(this.minX, this.minY, this.minZ);
    }
    
    public double max(final Direction.Axis a) {
        return a.choose(this.maxX, this.maxY, this.maxZ);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof AABB)) {
            return false;
        }
        final AABB dcf3 = (AABB)object;
        return Double.compare(dcf3.minX, this.minX) == 0 && Double.compare(dcf3.minY, this.minY) == 0 && Double.compare(dcf3.minZ, this.minZ) == 0 && Double.compare(dcf3.maxX, this.maxX) == 0 && Double.compare(dcf3.maxY, this.maxY) == 0 && Double.compare(dcf3.maxZ, this.maxZ) == 0;
    }
    
    public int hashCode() {
        long long2 = Double.doubleToLongBits(this.minX);
        int integer4 = (int)(long2 ^ long2 >>> 32);
        long2 = Double.doubleToLongBits(this.minY);
        integer4 = 31 * integer4 + (int)(long2 ^ long2 >>> 32);
        long2 = Double.doubleToLongBits(this.minZ);
        integer4 = 31 * integer4 + (int)(long2 ^ long2 >>> 32);
        long2 = Double.doubleToLongBits(this.maxX);
        integer4 = 31 * integer4 + (int)(long2 ^ long2 >>> 32);
        long2 = Double.doubleToLongBits(this.maxY);
        integer4 = 31 * integer4 + (int)(long2 ^ long2 >>> 32);
        long2 = Double.doubleToLongBits(this.maxZ);
        integer4 = 31 * integer4 + (int)(long2 ^ long2 >>> 32);
        return integer4;
    }
    
    public AABB contract(final double double1, final double double2, final double double3) {
        double double4 = this.minX;
        double double5 = this.minY;
        double double6 = this.minZ;
        double double7 = this.maxX;
        double double8 = this.maxY;
        double double9 = this.maxZ;
        if (double1 < 0.0) {
            double4 -= double1;
        }
        else if (double1 > 0.0) {
            double7 -= double1;
        }
        if (double2 < 0.0) {
            double5 -= double2;
        }
        else if (double2 > 0.0) {
            double8 -= double2;
        }
        if (double3 < 0.0) {
            double6 -= double3;
        }
        else if (double3 > 0.0) {
            double9 -= double3;
        }
        return new AABB(double4, double5, double6, double7, double8, double9);
    }
    
    public AABB expandTowards(final Vec3 dck) {
        return this.expandTowards(dck.x, dck.y, dck.z);
    }
    
    public AABB expandTowards(final double double1, final double double2, final double double3) {
        double double4 = this.minX;
        double double5 = this.minY;
        double double6 = this.minZ;
        double double7 = this.maxX;
        double double8 = this.maxY;
        double double9 = this.maxZ;
        if (double1 < 0.0) {
            double4 += double1;
        }
        else if (double1 > 0.0) {
            double7 += double1;
        }
        if (double2 < 0.0) {
            double5 += double2;
        }
        else if (double2 > 0.0) {
            double8 += double2;
        }
        if (double3 < 0.0) {
            double6 += double3;
        }
        else if (double3 > 0.0) {
            double9 += double3;
        }
        return new AABB(double4, double5, double6, double7, double8, double9);
    }
    
    public AABB inflate(final double double1, final double double2, final double double3) {
        final double double4 = this.minX - double1;
        final double double5 = this.minY - double2;
        final double double6 = this.minZ - double3;
        final double double7 = this.maxX + double1;
        final double double8 = this.maxY + double2;
        final double double9 = this.maxZ + double3;
        return new AABB(double4, double5, double6, double7, double8, double9);
    }
    
    public AABB inflate(final double double1) {
        return this.inflate(double1, double1, double1);
    }
    
    public AABB intersect(final AABB dcf) {
        final double double3 = Math.max(this.minX, dcf.minX);
        final double double4 = Math.max(this.minY, dcf.minY);
        final double double5 = Math.max(this.minZ, dcf.minZ);
        final double double6 = Math.min(this.maxX, dcf.maxX);
        final double double7 = Math.min(this.maxY, dcf.maxY);
        final double double8 = Math.min(this.maxZ, dcf.maxZ);
        return new AABB(double3, double4, double5, double6, double7, double8);
    }
    
    public AABB minmax(final AABB dcf) {
        final double double3 = Math.min(this.minX, dcf.minX);
        final double double4 = Math.min(this.minY, dcf.minY);
        final double double5 = Math.min(this.minZ, dcf.minZ);
        final double double6 = Math.max(this.maxX, dcf.maxX);
        final double double7 = Math.max(this.maxY, dcf.maxY);
        final double double8 = Math.max(this.maxZ, dcf.maxZ);
        return new AABB(double3, double4, double5, double6, double7, double8);
    }
    
    public AABB move(final double double1, final double double2, final double double3) {
        return new AABB(this.minX + double1, this.minY + double2, this.minZ + double3, this.maxX + double1, this.maxY + double2, this.maxZ + double3);
    }
    
    public AABB move(final BlockPos fx) {
        return new AABB(this.minX + fx.getX(), this.minY + fx.getY(), this.minZ + fx.getZ(), this.maxX + fx.getX(), this.maxY + fx.getY(), this.maxZ + fx.getZ());
    }
    
    public AABB move(final Vec3 dck) {
        return this.move(dck.x, dck.y, dck.z);
    }
    
    public boolean intersects(final AABB dcf) {
        return this.intersects(dcf.minX, dcf.minY, dcf.minZ, dcf.maxX, dcf.maxY, dcf.maxZ);
    }
    
    public boolean intersects(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        return this.minX < double4 && this.maxX > double1 && this.minY < double5 && this.maxY > double2 && this.minZ < double6 && this.maxZ > double3;
    }
    
    public boolean intersects(final Vec3 dck1, final Vec3 dck2) {
        return this.intersects(Math.min(dck1.x, dck2.x), Math.min(dck1.y, dck2.y), Math.min(dck1.z, dck2.z), Math.max(dck1.x, dck2.x), Math.max(dck1.y, dck2.y), Math.max(dck1.z, dck2.z));
    }
    
    public boolean contains(final Vec3 dck) {
        return this.contains(dck.x, dck.y, dck.z);
    }
    
    public boolean contains(final double double1, final double double2, final double double3) {
        return double1 >= this.minX && double1 < this.maxX && double2 >= this.minY && double2 < this.maxY && double3 >= this.minZ && double3 < this.maxZ;
    }
    
    public double getSize() {
        final double double2 = this.getXsize();
        final double double3 = this.getYsize();
        final double double4 = this.getZsize();
        return (double2 + double3 + double4) / 3.0;
    }
    
    public double getXsize() {
        return this.maxX - this.minX;
    }
    
    public double getYsize() {
        return this.maxY - this.minY;
    }
    
    public double getZsize() {
        return this.maxZ - this.minZ;
    }
    
    public AABB deflate(final double double1) {
        return this.inflate(-double1);
    }
    
    public Optional<Vec3> clip(final Vec3 dck1, final Vec3 dck2) {
        final double[] arr4 = { 1.0 };
        final double double5 = dck2.x - dck1.x;
        final double double6 = dck2.y - dck1.y;
        final double double7 = dck2.z - dck1.z;
        final Direction gc11 = getDirection(this, dck1, arr4, null, double5, double6, double7);
        if (gc11 == null) {
            return (Optional<Vec3>)Optional.empty();
        }
        final double double8 = arr4[0];
        return (Optional<Vec3>)Optional.of(dck1.add(double8 * double5, double8 * double6, double8 * double7));
    }
    
    @Nullable
    public static BlockHitResult clip(final Iterable<AABB> iterable, final Vec3 dck2, final Vec3 dck3, final BlockPos fx) {
        final double[] arr5 = { 1.0 };
        Direction gc6 = null;
        final double double7 = dck3.x - dck2.x;
        final double double8 = dck3.y - dck2.y;
        final double double9 = dck3.z - dck2.z;
        for (final AABB dcf14 : iterable) {
            gc6 = getDirection(dcf14.move(fx), dck2, arr5, gc6, double7, double8, double9);
        }
        if (gc6 == null) {
            return null;
        }
        final double double10 = arr5[0];
        return new BlockHitResult(dck2.add(double10 * double7, double10 * double8, double10 * double9), gc6, fx, false);
    }
    
    @Nullable
    private static Direction getDirection(final AABB dcf, final Vec3 dck, final double[] arr, @Nullable Direction gc, final double double5, final double double6, final double double7) {
        if (double5 > 1.0E-7) {
            gc = clipPoint(arr, gc, double5, double6, double7, dcf.minX, dcf.minY, dcf.maxY, dcf.minZ, dcf.maxZ, Direction.WEST, dck.x, dck.y, dck.z);
        }
        else if (double5 < -1.0E-7) {
            gc = clipPoint(arr, gc, double5, double6, double7, dcf.maxX, dcf.minY, dcf.maxY, dcf.minZ, dcf.maxZ, Direction.EAST, dck.x, dck.y, dck.z);
        }
        if (double6 > 1.0E-7) {
            gc = clipPoint(arr, gc, double6, double7, double5, dcf.minY, dcf.minZ, dcf.maxZ, dcf.minX, dcf.maxX, Direction.DOWN, dck.y, dck.z, dck.x);
        }
        else if (double6 < -1.0E-7) {
            gc = clipPoint(arr, gc, double6, double7, double5, dcf.maxY, dcf.minZ, dcf.maxZ, dcf.minX, dcf.maxX, Direction.UP, dck.y, dck.z, dck.x);
        }
        if (double7 > 1.0E-7) {
            gc = clipPoint(arr, gc, double7, double5, double6, dcf.minZ, dcf.minX, dcf.maxX, dcf.minY, dcf.maxY, Direction.NORTH, dck.z, dck.x, dck.y);
        }
        else if (double7 < -1.0E-7) {
            gc = clipPoint(arr, gc, double7, double5, double6, dcf.maxZ, dcf.minX, dcf.maxX, dcf.minY, dcf.maxY, Direction.SOUTH, dck.z, dck.x, dck.y);
        }
        return gc;
    }
    
    @Nullable
    private static Direction clipPoint(final double[] arr, @Nullable final Direction gc2, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8, final double double9, final double double10, final Direction gc11, final double double12, final double double13, final double double14) {
        final double double15 = (double6 - double12) / double3;
        final double double16 = double13 + double15 * double4;
        final double double17 = double14 + double15 * double5;
        if (0.0 < double15 && double15 < arr[0] && double7 - 1.0E-7 < double16 && double16 < double8 + 1.0E-7 && double9 - 1.0E-7 < double17 && double17 < double10 + 1.0E-7) {
            arr[0] = double15;
            return gc11;
        }
        return gc2;
    }
    
    public String toString() {
        return new StringBuilder().append("AABB[").append(this.minX).append(", ").append(this.minY).append(", ").append(this.minZ).append("] -> [").append(this.maxX).append(", ").append(this.maxY).append(", ").append(this.maxZ).append("]").toString();
    }
    
    public boolean hasNaN() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }
    
    public Vec3 getCenter() {
        return new Vec3(Mth.lerp(0.5, this.minX, this.maxX), Mth.lerp(0.5, this.minY, this.maxY), Mth.lerp(0.5, this.minZ, this.maxZ));
    }
    
    public static AABB ofSize(final double double1, final double double2, final double double3) {
        return new AABB(-double1 / 2.0, -double2 / 2.0, -double3 / 2.0, double1 / 2.0, double2 / 2.0, double3 / 2.0);
    }
}
