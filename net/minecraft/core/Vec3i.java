package net.minecraft.core;

import net.minecraft.Util;
import com.mojang.serialization.DataResult;
import java.util.stream.IntStream;
import com.google.common.base.MoreObjects;
import net.minecraft.util.Mth;
import com.mojang.serialization.Codec;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Vec3i implements Comparable<Vec3i> {
    public static final Codec<Vec3i> CODEC;
    public static final Vec3i ZERO;
    private int x;
    private int y;
    private int z;
    
    public Vec3i(final int integer1, final int integer2, final int integer3) {
        this.x = integer1;
        this.y = integer2;
        this.z = integer3;
    }
    
    public Vec3i(final double double1, final double double2, final double double3) {
        this(Mth.floor(double1), Mth.floor(double2), Mth.floor(double3));
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Vec3i)) {
            return false;
        }
        final Vec3i gr3 = (Vec3i)object;
        return this.getX() == gr3.getX() && this.getY() == gr3.getY() && this.getZ() == gr3.getZ();
    }
    
    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }
    
    public int compareTo(final Vec3i gr) {
        if (this.getY() != gr.getY()) {
            return this.getY() - gr.getY();
        }
        if (this.getZ() == gr.getZ()) {
            return this.getX() - gr.getX();
        }
        return this.getZ() - gr.getZ();
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    protected void setX(final int integer) {
        this.x = integer;
    }
    
    protected void setY(final int integer) {
        this.y = integer;
    }
    
    protected void setZ(final int integer) {
        this.z = integer;
    }
    
    public Vec3i above() {
        return this.above(1);
    }
    
    public Vec3i above(final int integer) {
        return this.relative(Direction.UP, integer);
    }
    
    public Vec3i below() {
        return this.below(1);
    }
    
    public Vec3i below(final int integer) {
        return this.relative(Direction.DOWN, integer);
    }
    
    public Vec3i relative(final Direction gc, final int integer) {
        if (integer == 0) {
            return this;
        }
        return new Vec3i(this.getX() + gc.getStepX() * integer, this.getY() + gc.getStepY() * integer, this.getZ() + gc.getStepZ() * integer);
    }
    
    public Vec3i cross(final Vec3i gr) {
        return new Vec3i(this.getY() * gr.getZ() - this.getZ() * gr.getY(), this.getZ() * gr.getX() - this.getX() * gr.getZ(), this.getX() * gr.getY() - this.getY() * gr.getX());
    }
    
    public boolean closerThan(final Vec3i gr, final double double2) {
        return this.distSqr(gr.getX(), gr.getY(), gr.getZ(), false) < double2 * double2;
    }
    
    public boolean closerThan(final Position gk, final double double2) {
        return this.distSqr(gk.x(), gk.y(), gk.z(), true) < double2 * double2;
    }
    
    public double distSqr(final Vec3i gr) {
        return this.distSqr(gr.getX(), gr.getY(), gr.getZ(), true);
    }
    
    public double distSqr(final Position gk, final boolean boolean2) {
        return this.distSqr(gk.x(), gk.y(), gk.z(), boolean2);
    }
    
    public double distSqr(final double double1, final double double2, final double double3, final boolean boolean4) {
        final double double4 = boolean4 ? 0.5 : 0.0;
        final double double5 = this.getX() + double4 - double1;
        final double double6 = this.getY() + double4 - double2;
        final double double7 = this.getZ() + double4 - double3;
        return double5 * double5 + double6 * double6 + double7 * double7;
    }
    
    public int distManhattan(final Vec3i gr) {
        final float float3 = (float)Math.abs(gr.getX() - this.getX());
        final float float4 = (float)Math.abs(gr.getY() - this.getY());
        final float float5 = (float)Math.abs(gr.getZ() - this.getZ());
        return (int)(float3 + float4 + float5);
    }
    
    public int get(final Direction.Axis a) {
        return a.choose(this.x, this.y, this.z);
    }
    
    public String toString() {
        return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }
    
    public String toShortString() {
        return new StringBuilder().append("").append(this.getX()).append(", ").append(this.getY()).append(", ").append(this.getZ()).toString();
    }
    
    static {
        CODEC = Codec.INT_STREAM.comapFlatMap(intStream -> Util.fixedSize(intStream, 3).map(arr -> new Vec3i(arr[0], arr[1], arr[2])), gr -> IntStream.of(new int[] { gr.getX(), gr.getY(), gr.getZ() }));
        ZERO = new Vec3i(0, 0, 0);
    }
}
