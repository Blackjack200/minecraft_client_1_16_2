package com.mojang.math;

import net.minecraft.util.Mth;

public class Vector4f {
    private float x;
    private float y;
    private float z;
    private float w;
    
    public Vector4f() {
    }
    
    public Vector4f(final float float1, final float float2, final float float3, final float float4) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
        this.w = float4;
    }
    
    public Vector4f(final Vector3f g) {
        this(g.x(), g.y(), g.z(), 1.0f);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Vector4f h3 = (Vector4f)object;
        return Float.compare(h3.x, this.x) == 0 && Float.compare(h3.y, this.y) == 0 && Float.compare(h3.z, this.z) == 0 && Float.compare(h3.w, this.w) == 0;
    }
    
    public int hashCode() {
        int integer2 = Float.floatToIntBits(this.x);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.y);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.z);
        integer2 = 31 * integer2 + Float.floatToIntBits(this.w);
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
    
    public float w() {
        return this.w;
    }
    
    public void mul(final Vector3f g) {
        this.x *= g.x();
        this.y *= g.y();
        this.z *= g.z();
    }
    
    public void set(final float float1, final float float2, final float float3, final float float4) {
        this.x = float1;
        this.y = float2;
        this.z = float3;
        this.w = float4;
    }
    
    public float dot(final Vector4f h) {
        return this.x * h.x + this.y * h.y + this.z * h.z + this.w * h.w;
    }
    
    public boolean normalize() {
        final float float2 = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if (float2 < 1.0E-5) {
            return false;
        }
        final float float3 = Mth.fastInvSqrt(float2);
        this.x *= float3;
        this.y *= float3;
        this.z *= float3;
        this.w *= float3;
        return true;
    }
    
    public void transform(final Matrix4f b) {
        final float float3 = this.x;
        final float float4 = this.y;
        final float float5 = this.z;
        final float float6 = this.w;
        this.x = b.m00 * float3 + b.m01 * float4 + b.m02 * float5 + b.m03 * float6;
        this.y = b.m10 * float3 + b.m11 * float4 + b.m12 * float5 + b.m13 * float6;
        this.z = b.m20 * float3 + b.m21 * float4 + b.m22 * float5 + b.m23 * float6;
        this.w = b.m30 * float3 + b.m31 * float4 + b.m32 * float5 + b.m33 * float6;
    }
    
    public void transform(final Quaternion d) {
        final Quaternion d2 = new Quaternion(d);
        d2.mul(new Quaternion(this.x(), this.y(), this.z(), 0.0f));
        final Quaternion d3 = new Quaternion(d);
        d3.conj();
        d2.mul(d3);
        this.set(d2.i(), d2.j(), d2.k(), this.w());
    }
    
    public void perspectiveDivide() {
        this.x /= this.w;
        this.y /= this.w;
        this.z /= this.w;
        this.w = 1.0f;
    }
    
    public String toString() {
        return new StringBuilder().append("[").append(this.x).append(", ").append(this.y).append(", ").append(this.z).append(", ").append(this.w).append("]").toString();
    }
}
