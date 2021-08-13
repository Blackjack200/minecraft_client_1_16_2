package net.minecraft.world.phys;

import net.minecraft.core.Direction;
import java.util.EnumSet;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Position;

public class Vec3 implements Position {
    public static final Vec3 ZERO;
    public final double x;
    public final double y;
    public final double z;
    
    public static Vec3 fromRGB24(final int integer) {
        final double double2 = (integer >> 16 & 0xFF) / 255.0;
        final double double3 = (integer >> 8 & 0xFF) / 255.0;
        final double double4 = (integer & 0xFF) / 255.0;
        return new Vec3(double2, double3, double4);
    }
    
    public static Vec3 atCenterOf(final Vec3i gr) {
        return new Vec3(gr.getX() + 0.5, gr.getY() + 0.5, gr.getZ() + 0.5);
    }
    
    public static Vec3 atLowerCornerOf(final Vec3i gr) {
        return new Vec3(gr.getX(), gr.getY(), gr.getZ());
    }
    
    public static Vec3 atBottomCenterOf(final Vec3i gr) {
        return new Vec3(gr.getX() + 0.5, gr.getY(), gr.getZ() + 0.5);
    }
    
    public static Vec3 upFromBottomCenterOf(final Vec3i gr, final double double2) {
        return new Vec3(gr.getX() + 0.5, gr.getY() + double2, gr.getZ() + 0.5);
    }
    
    public Vec3(final double double1, final double double2, final double double3) {
        this.x = double1;
        this.y = double2;
        this.z = double3;
    }
    
    public Vec3(final Vector3f g) {
        this(g.x(), g.y(), g.z());
    }
    
    public Vec3 vectorTo(final Vec3 dck) {
        return new Vec3(dck.x - this.x, dck.y - this.y, dck.z - this.z);
    }
    
    public Vec3 normalize() {
        final double double2 = Mth.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        if (double2 < 1.0E-4) {
            return Vec3.ZERO;
        }
        return new Vec3(this.x / double2, this.y / double2, this.z / double2);
    }
    
    public double dot(final Vec3 dck) {
        return this.x * dck.x + this.y * dck.y + this.z * dck.z;
    }
    
    public Vec3 cross(final Vec3 dck) {
        return new Vec3(this.y * dck.z - this.z * dck.y, this.z * dck.x - this.x * dck.z, this.x * dck.y - this.y * dck.x);
    }
    
    public Vec3 subtract(final Vec3 dck) {
        return this.subtract(dck.x, dck.y, dck.z);
    }
    
    public Vec3 subtract(final double double1, final double double2, final double double3) {
        return this.add(-double1, -double2, -double3);
    }
    
    public Vec3 add(final Vec3 dck) {
        return this.add(dck.x, dck.y, dck.z);
    }
    
    public Vec3 add(final double double1, final double double2, final double double3) {
        return new Vec3(this.x + double1, this.y + double2, this.z + double3);
    }
    
    public boolean closerThan(final Position gk, final double double2) {
        return this.distanceToSqr(gk.x(), gk.y(), gk.z()) < double2 * double2;
    }
    
    public double distanceTo(final Vec3 dck) {
        final double double3 = dck.x - this.x;
        final double double4 = dck.y - this.y;
        final double double5 = dck.z - this.z;
        return Mth.sqrt(double3 * double3 + double4 * double4 + double5 * double5);
    }
    
    public double distanceToSqr(final Vec3 dck) {
        final double double3 = dck.x - this.x;
        final double double4 = dck.y - this.y;
        final double double5 = dck.z - this.z;
        return double3 * double3 + double4 * double4 + double5 * double5;
    }
    
    public double distanceToSqr(final double double1, final double double2, final double double3) {
        final double double4 = double1 - this.x;
        final double double5 = double2 - this.y;
        final double double6 = double3 - this.z;
        return double4 * double4 + double5 * double5 + double6 * double6;
    }
    
    public Vec3 scale(final double double1) {
        return this.multiply(double1, double1, double1);
    }
    
    public Vec3 reverse() {
        return this.scale(-1.0);
    }
    
    public Vec3 multiply(final Vec3 dck) {
        return this.multiply(dck.x, dck.y, dck.z);
    }
    
    public Vec3 multiply(final double double1, final double double2, final double double3) {
        return new Vec3(this.x * double1, this.y * double2, this.z * double3);
    }
    
    public double length() {
        return Mth.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }
    
    public double lengthSqr() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Vec3)) {
            return false;
        }
        final Vec3 dck3 = (Vec3)object;
        return Double.compare(dck3.x, this.x) == 0 && Double.compare(dck3.y, this.y) == 0 && Double.compare(dck3.z, this.z) == 0;
    }
    
    public int hashCode() {
        long long3 = Double.doubleToLongBits(this.x);
        int integer2 = (int)(long3 ^ long3 >>> 32);
        long3 = Double.doubleToLongBits(this.y);
        integer2 = 31 * integer2 + (int)(long3 ^ long3 >>> 32);
        long3 = Double.doubleToLongBits(this.z);
        integer2 = 31 * integer2 + (int)(long3 ^ long3 >>> 32);
        return integer2;
    }
    
    public String toString() {
        return new StringBuilder().append("(").append(this.x).append(", ").append(this.y).append(", ").append(this.z).append(")").toString();
    }
    
    public Vec3 xRot(final float float1) {
        final float float2 = Mth.cos(float1);
        final float float3 = Mth.sin(float1);
        final double double5 = this.x;
        final double double6 = this.y * float2 + this.z * float3;
        final double double7 = this.z * float2 - this.y * float3;
        return new Vec3(double5, double6, double7);
    }
    
    public Vec3 yRot(final float float1) {
        final float float2 = Mth.cos(float1);
        final float float3 = Mth.sin(float1);
        final double double5 = this.x * float2 + this.z * float3;
        final double double6 = this.y;
        final double double7 = this.z * float2 - this.x * float3;
        return new Vec3(double5, double6, double7);
    }
    
    public Vec3 zRot(final float float1) {
        final float float2 = Mth.cos(float1);
        final float float3 = Mth.sin(float1);
        final double double5 = this.x * float2 + this.y * float3;
        final double double6 = this.y * float2 - this.x * float3;
        final double double7 = this.z;
        return new Vec3(double5, double6, double7);
    }
    
    public static Vec3 directionFromRotation(final Vec2 dcj) {
        return directionFromRotation(dcj.x, dcj.y);
    }
    
    public static Vec3 directionFromRotation(final float float1, final float float2) {
        final float float3 = Mth.cos(-float2 * 0.017453292f - 3.1415927f);
        final float float4 = Mth.sin(-float2 * 0.017453292f - 3.1415927f);
        final float float5 = -Mth.cos(-float1 * 0.017453292f);
        final float float6 = Mth.sin(-float1 * 0.017453292f);
        return new Vec3(float4 * float5, float6, float3 * float5);
    }
    
    public Vec3 align(final EnumSet<Direction.Axis> enumSet) {
        final double double3 = enumSet.contains(Direction.Axis.X) ? Mth.floor(this.x) : this.x;
        final double double4 = enumSet.contains(Direction.Axis.Y) ? Mth.floor(this.y) : this.y;
        final double double5 = enumSet.contains(Direction.Axis.Z) ? Mth.floor(this.z) : this.z;
        return new Vec3(double3, double4, double5);
    }
    
    public double get(final Direction.Axis a) {
        return a.choose(this.x, this.y, this.z);
    }
    
    public final double x() {
        return this.x;
    }
    
    public final double y() {
        return this.y;
    }
    
    public final double z() {
        return this.z;
    }
    
    static {
        ZERO = new Vec3(0.0, 0.0, 0.0);
    }
}
