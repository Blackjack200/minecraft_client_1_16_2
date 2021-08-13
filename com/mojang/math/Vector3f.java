package com.mojang.math;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class Vector3f {
    public static Vector3f XN;
    public static Vector3f XP;
    public static Vector3f YN;
    public static Vector3f YP;
    public static Vector3f ZN;
    public static Vector3f ZP;
    private float x;
    private float y;
    private float z;
    
    public Vector3f() {
    }
    
    public Vector3f(final float float1, final float float2, final float float3) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
    }
    
    public Vector3f(final Vec3 dck) {
        this((float)dck.x, (float)dck.y, (float)dck.z);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Vector3f g3 = (Vector3f)object;
        return Float.compare(g3.x, this.x) == 0 && Float.compare(g3.y, this.y) == 0 && Float.compare(g3.z, this.z) == 0;
    }
    
    public int hashCode() {
        int integer2 = Float.floatToIntBits(this.x);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.y);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.z);
        return integer2;
    }
    
    public float x() {
        return this.x;
    }
    
    public float y() {
        return this.y;
    }
    
    public float z() {
        return this.z;
    }
    
    public void mul(final float float1) {
        this.x *= float1;
        this.y *= float1;
        this.z *= float1;
    }
    
    public void mul(final float float1, final float float2, final float float3) {
        this.x *= float1;
        this.y *= float2;
        this.z *= float3;
    }
    
    public void clamp(final float float1, final float float2) {
        this.x = Mth.clamp(this.x, float1, float2);
        this.y = Mth.clamp(this.y, float1, float2);
        this.z = Mth.clamp(this.z, float1, float2);
    }
    
    public void set(final float float1, final float float2, final float float3) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
    }
    
    public void add(final float float1, final float float2, final float float3) {
        this.x += float1;
        this.y += float2;
        this.z += float3;
    }
    
    public void add(final Vector3f g) {
        this.x += g.x;
        this.y += g.y;
        this.z += g.z;
    }
    
    public void sub(final Vector3f g) {
        this.x -= g.x;
        this.y -= g.y;
        this.z -= g.z;
    }
    
    public float dot(final Vector3f g) {
        return this.x * g.x + this.y * g.y + this.z * g.z;
    }
    
    public boolean normalize() {
        final float float2 = this.x * this.x + this.y * this.y + this.z * this.z;
        if (float2 < 1.0E-5) {
            return false;
        }
        final float float3 = Mth.fastInvSqrt(float2);
        this.x *= float3;
        this.y *= float3;
        this.z *= float3;
        return true;
    }
    
    public void cross(final Vector3f g) {
        final float float3 = this.x;
        final float float4 = this.y;
        final float float5 = this.z;
        final float float6 = g.x();
        final float float7 = g.y();
        final float float8 = g.z();
        this.x = float4 * float8 - float5 * float7;
        this.y = float5 * float6 - float3 * float8;
        this.z = float3 * float7 - float4 * float6;
    }
    
    public void transform(final Matrix3f a) {
        final float float3 = this.x;
        final float float4 = this.y;
        final float float5 = this.z;
        this.x = a.m00 * float3 + a.m01 * float4 + a.m02 * float5;
        this.y = a.m10 * float3 + a.m11 * float4 + a.m12 * float5;
        this.z = a.m20 * float3 + a.m21 * float4 + a.m22 * float5;
    }
    
    public void transform(final Quaternion d) {
        final Quaternion d2 = new Quaternion(d);
        d2.mul(new Quaternion(this.x(), this.y(), this.z(), 0.0f));
        final Quaternion d3 = new Quaternion(d);
        d3.conj();
        d2.mul(d3);
        this.set(d2.i(), d2.j(), d2.k());
    }
    
    public void lerp(final Vector3f g, final float float2) {
        final float float3 = 1.0f - float2;
        this.x = this.x * float3 + g.x * float2;
        this.y = this.y * float3 + g.y * float2;
        this.z = this.z * float3 + g.z * float2;
    }
    
    public Quaternion rotation(final float float1) {
        return new Quaternion(this, float1, false);
    }
    
    public Quaternion rotationDegrees(final float float1) {
        return new Quaternion(this, float1, true);
    }
    
    public Vector3f copy() {
        return new Vector3f(this.x, this.y, this.z);
    }
    
    public void map(final Float2FloatFunction float2FloatFunction) {
        this.x = float2FloatFunction.get(this.x);
        this.y = float2FloatFunction.get(this.y);
        this.z = float2FloatFunction.get(this.z);
    }
    
    public String toString() {
        return new StringBuilder().append("[").append(this.x).append(", ").append(this.y).append(", ").append(this.z).append("]").toString();
    }
    
    static {
        Vector3f.XN = new Vector3f(-1.0f, 0.0f, 0.0f);
        Vector3f.XP = new Vector3f(1.0f, 0.0f, 0.0f);
        Vector3f.YN = new Vector3f(0.0f, -1.0f, 0.0f);
        Vector3f.YP = new Vector3f(0.0f, 1.0f, 0.0f);
        Vector3f.ZN = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f.ZP = new Vector3f(0.0f, 0.0f, 1.0f);
    }
}
