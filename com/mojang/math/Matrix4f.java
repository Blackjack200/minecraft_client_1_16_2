package com.mojang.math;

import java.nio.FloatBuffer;

public final class Matrix4f {
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m03;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m13;
    protected float m20;
    protected float m21;
    protected float m22;
    protected float m23;
    protected float m30;
    protected float m31;
    protected float m32;
    protected float m33;
    
    public Matrix4f() {
    }
    
    public Matrix4f(final Matrix4f b) {
        this.m00 = b.m00;
        this.m01 = b.m01;
        this.m02 = b.m02;
        this.m03 = b.m03;
        this.m10 = b.m10;
        this.m11 = b.m11;
        this.m12 = b.m12;
        this.m13 = b.m13;
        this.m20 = b.m20;
        this.m21 = b.m21;
        this.m22 = b.m22;
        this.m23 = b.m23;
        this.m30 = b.m30;
        this.m31 = b.m31;
        this.m32 = b.m32;
        this.m33 = b.m33;
    }
    
    public Matrix4f(final Quaternion d) {
        final float float3 = d.i();
        final float float4 = d.j();
        final float float5 = d.k();
        final float float6 = d.r();
        final float float7 = 2.0f * float3 * float3;
        final float float8 = 2.0f * float4 * float4;
        final float float9 = 2.0f * float5 * float5;
        this.m00 = 1.0f - float8 - float9;
        this.m11 = 1.0f - float9 - float7;
        this.m22 = 1.0f - float7 - float8;
        this.m33 = 1.0f;
        final float float10 = float3 * float4;
        final float float11 = float4 * float5;
        final float float12 = float5 * float3;
        final float float13 = float3 * float6;
        final float float14 = float4 * float6;
        final float float15 = float5 * float6;
        this.m10 = 2.0f * (float10 + float15);
        this.m01 = 2.0f * (float10 - float15);
        this.m20 = 2.0f * (float12 - float14);
        this.m02 = 2.0f * (float12 + float14);
        this.m21 = 2.0f * (float11 + float13);
        this.m12 = 2.0f * (float11 - float13);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Matrix4f b3 = (Matrix4f)object;
        return Float.compare(b3.m00, this.m00) == 0 && Float.compare(b3.m01, this.m01) == 0 && Float.compare(b3.m02, this.m02) == 0 && Float.compare(b3.m03, this.m03) == 0 && Float.compare(b3.m10, this.m10) == 0 && Float.compare(b3.m11, this.m11) == 0 && Float.compare(b3.m12, this.m12) == 0 && Float.compare(b3.m13, this.m13) == 0 && Float.compare(b3.m20, this.m20) == 0 && Float.compare(b3.m21, this.m21) == 0 && Float.compare(b3.m22, this.m22) == 0 && Float.compare(b3.m23, this.m23) == 0 && Float.compare(b3.m30, this.m30) == 0 && Float.compare(b3.m31, this.m31) == 0 && Float.compare(b3.m32, this.m32) == 0 && Float.compare(b3.m33, this.m33) == 0;
    }
    
    public int hashCode() {
        int integer2 = (this.m00 != 0.0f) ? Float.floatToIntBits(this.m00) : 0;
        integer2 = 31 * integer2 + ((this.m01 != 0.0f) ? Float.floatToIntBits(this.m01) : 0);
        integer2 = 31 * integer2 + ((this.m02 != 0.0f) ? Float.floatToIntBits(this.m02) : 0);
        integer2 = 31 * integer2 + ((this.m03 != 0.0f) ? Float.floatToIntBits(this.m03) : 0);
        integer2 = 31 * integer2 + ((this.m10 != 0.0f) ? Float.floatToIntBits(this.m10) : 0);
        integer2 = 31 * integer2 + ((this.m11 != 0.0f) ? Float.floatToIntBits(this.m11) : 0);
        integer2 = 31 * integer2 + ((this.m12 != 0.0f) ? Float.floatToIntBits(this.m12) : 0);
        integer2 = 31 * integer2 + ((this.m13 != 0.0f) ? Float.floatToIntBits(this.m13) : 0);
        integer2 = 31 * integer2 + ((this.m20 != 0.0f) ? Float.floatToIntBits(this.m20) : 0);
        integer2 = 31 * integer2 + ((this.m21 != 0.0f) ? Float.floatToIntBits(this.m21) : 0);
        integer2 = 31 * integer2 + ((this.m22 != 0.0f) ? Float.floatToIntBits(this.m22) : 0);
        integer2 = 31 * integer2 + ((this.m23 != 0.0f) ? Float.floatToIntBits(this.m23) : 0);
        integer2 = 31 * integer2 + ((this.m30 != 0.0f) ? Float.floatToIntBits(this.m30) : 0);
        integer2 = 31 * integer2 + ((this.m31 != 0.0f) ? Float.floatToIntBits(this.m31) : 0);
        integer2 = 31 * integer2 + ((this.m32 != 0.0f) ? Float.floatToIntBits(this.m32) : 0);
        integer2 = 31 * integer2 + ((this.m33 != 0.0f) ? Float.floatToIntBits(this.m33) : 0);
        return integer2;
    }
    
    private static int bufferIndex(final int integer1, final int integer2) {
        return integer2 * 4 + integer1;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Matrix4f:\n");
        stringBuilder2.append(this.m00);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m01);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m02);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m03);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m10);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m11);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m12);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m13);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m20);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m21);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m22);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m23);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m30);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m31);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m32);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m33);
        stringBuilder2.append("\n");
        return stringBuilder2.toString();
    }
    
    public void store(final FloatBuffer floatBuffer) {
        floatBuffer.put(bufferIndex(0, 0), this.m00);
        floatBuffer.put(bufferIndex(0, 1), this.m01);
        floatBuffer.put(bufferIndex(0, 2), this.m02);
        floatBuffer.put(bufferIndex(0, 3), this.m03);
        floatBuffer.put(bufferIndex(1, 0), this.m10);
        floatBuffer.put(bufferIndex(1, 1), this.m11);
        floatBuffer.put(bufferIndex(1, 2), this.m12);
        floatBuffer.put(bufferIndex(1, 3), this.m13);
        floatBuffer.put(bufferIndex(2, 0), this.m20);
        floatBuffer.put(bufferIndex(2, 1), this.m21);
        floatBuffer.put(bufferIndex(2, 2), this.m22);
        floatBuffer.put(bufferIndex(2, 3), this.m23);
        floatBuffer.put(bufferIndex(3, 0), this.m30);
        floatBuffer.put(bufferIndex(3, 1), this.m31);
        floatBuffer.put(bufferIndex(3, 2), this.m32);
        floatBuffer.put(bufferIndex(3, 3), this.m33);
    }
    
    public void setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m03 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m13 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
        this.m23 = 0.0f;
        this.m30 = 0.0f;
        this.m31 = 0.0f;
        this.m32 = 0.0f;
        this.m33 = 1.0f;
    }
    
    public float adjugateAndDet() {
        final float float2 = this.m00 * this.m11 - this.m01 * this.m10;
        final float float3 = this.m00 * this.m12 - this.m02 * this.m10;
        final float float4 = this.m00 * this.m13 - this.m03 * this.m10;
        final float float5 = this.m01 * this.m12 - this.m02 * this.m11;
        final float float6 = this.m01 * this.m13 - this.m03 * this.m11;
        final float float7 = this.m02 * this.m13 - this.m03 * this.m12;
        final float float8 = this.m20 * this.m31 - this.m21 * this.m30;
        final float float9 = this.m20 * this.m32 - this.m22 * this.m30;
        final float float10 = this.m20 * this.m33 - this.m23 * this.m30;
        final float float11 = this.m21 * this.m32 - this.m22 * this.m31;
        final float float12 = this.m21 * this.m33 - this.m23 * this.m31;
        final float float13 = this.m22 * this.m33 - this.m23 * this.m32;
        final float float14 = this.m11 * float13 - this.m12 * float12 + this.m13 * float11;
        final float float15 = -this.m10 * float13 + this.m12 * float10 - this.m13 * float9;
        final float float16 = this.m10 * float12 - this.m11 * float10 + this.m13 * float8;
        final float float17 = -this.m10 * float11 + this.m11 * float9 - this.m12 * float8;
        final float float18 = -this.m01 * float13 + this.m02 * float12 - this.m03 * float11;
        final float float19 = this.m00 * float13 - this.m02 * float10 + this.m03 * float9;
        final float float20 = -this.m00 * float12 + this.m01 * float10 - this.m03 * float8;
        final float float21 = this.m00 * float11 - this.m01 * float9 + this.m02 * float8;
        final float float22 = this.m31 * float7 - this.m32 * float6 + this.m33 * float5;
        final float float23 = -this.m30 * float7 + this.m32 * float4 - this.m33 * float3;
        final float float24 = this.m30 * float6 - this.m31 * float4 + this.m33 * float2;
        final float float25 = -this.m30 * float5 + this.m31 * float3 - this.m32 * float2;
        final float float26 = -this.m21 * float7 + this.m22 * float6 - this.m23 * float5;
        final float float27 = this.m20 * float7 - this.m22 * float4 + this.m23 * float3;
        final float float28 = -this.m20 * float6 + this.m21 * float4 - this.m23 * float2;
        final float float29 = this.m20 * float5 - this.m21 * float3 + this.m22 * float2;
        this.m00 = float14;
        this.m10 = float15;
        this.m20 = float16;
        this.m30 = float17;
        this.m01 = float18;
        this.m11 = float19;
        this.m21 = float20;
        this.m31 = float21;
        this.m02 = float22;
        this.m12 = float23;
        this.m22 = float24;
        this.m32 = float25;
        this.m03 = float26;
        this.m13 = float27;
        this.m23 = float28;
        this.m33 = float29;
        return float2 * float13 - float3 * float12 + float4 * float11 + float5 * float10 - float6 * float9 + float7 * float8;
    }
    
    public void transpose() {
        float float2 = this.m10;
        this.m10 = this.m01;
        this.m01 = float2;
        float2 = this.m20;
        this.m20 = this.m02;
        this.m02 = float2;
        float2 = this.m21;
        this.m21 = this.m12;
        this.m12 = float2;
        float2 = this.m30;
        this.m30 = this.m03;
        this.m03 = float2;
        float2 = this.m31;
        this.m31 = this.m13;
        this.m13 = float2;
        float2 = this.m32;
        this.m32 = this.m23;
        this.m23 = float2;
    }
    
    public boolean invert() {
        final float float2 = this.adjugateAndDet();
        if (Math.abs(float2) > 1.0E-6f) {
            this.multiply(float2);
            return true;
        }
        return false;
    }
    
    public void multiply(final Matrix4f b) {
        final float float3 = this.m00 * b.m00 + this.m01 * b.m10 + this.m02 * b.m20 + this.m03 * b.m30;
        final float float4 = this.m00 * b.m01 + this.m01 * b.m11 + this.m02 * b.m21 + this.m03 * b.m31;
        final float float5 = this.m00 * b.m02 + this.m01 * b.m12 + this.m02 * b.m22 + this.m03 * b.m32;
        final float float6 = this.m00 * b.m03 + this.m01 * b.m13 + this.m02 * b.m23 + this.m03 * b.m33;
        final float float7 = this.m10 * b.m00 + this.m11 * b.m10 + this.m12 * b.m20 + this.m13 * b.m30;
        final float float8 = this.m10 * b.m01 + this.m11 * b.m11 + this.m12 * b.m21 + this.m13 * b.m31;
        final float float9 = this.m10 * b.m02 + this.m11 * b.m12 + this.m12 * b.m22 + this.m13 * b.m32;
        final float float10 = this.m10 * b.m03 + this.m11 * b.m13 + this.m12 * b.m23 + this.m13 * b.m33;
        final float float11 = this.m20 * b.m00 + this.m21 * b.m10 + this.m22 * b.m20 + this.m23 * b.m30;
        final float float12 = this.m20 * b.m01 + this.m21 * b.m11 + this.m22 * b.m21 + this.m23 * b.m31;
        final float float13 = this.m20 * b.m02 + this.m21 * b.m12 + this.m22 * b.m22 + this.m23 * b.m32;
        final float float14 = this.m20 * b.m03 + this.m21 * b.m13 + this.m22 * b.m23 + this.m23 * b.m33;
        final float float15 = this.m30 * b.m00 + this.m31 * b.m10 + this.m32 * b.m20 + this.m33 * b.m30;
        final float float16 = this.m30 * b.m01 + this.m31 * b.m11 + this.m32 * b.m21 + this.m33 * b.m31;
        final float float17 = this.m30 * b.m02 + this.m31 * b.m12 + this.m32 * b.m22 + this.m33 * b.m32;
        final float float18 = this.m30 * b.m03 + this.m31 * b.m13 + this.m32 * b.m23 + this.m33 * b.m33;
        this.m00 = float3;
        this.m01 = float4;
        this.m02 = float5;
        this.m03 = float6;
        this.m10 = float7;
        this.m11 = float8;
        this.m12 = float9;
        this.m13 = float10;
        this.m20 = float11;
        this.m21 = float12;
        this.m22 = float13;
        this.m23 = float14;
        this.m30 = float15;
        this.m31 = float16;
        this.m32 = float17;
        this.m33 = float18;
    }
    
    public void multiply(final Quaternion d) {
        this.multiply(new Matrix4f(d));
    }
    
    public void multiply(final float float1) {
        this.m00 *= float1;
        this.m01 *= float1;
        this.m02 *= float1;
        this.m03 *= float1;
        this.m10 *= float1;
        this.m11 *= float1;
        this.m12 *= float1;
        this.m13 *= float1;
        this.m20 *= float1;
        this.m21 *= float1;
        this.m22 *= float1;
        this.m23 *= float1;
        this.m30 *= float1;
        this.m31 *= float1;
        this.m32 *= float1;
        this.m33 *= float1;
    }
    
    public static Matrix4f perspective(final double double1, final float float2, final float float3, final float float4) {
        final float float5 = (float)(1.0 / Math.tan(double1 * 0.01745329238474369 / 2.0));
        final Matrix4f b7 = new Matrix4f();
        b7.m00 = float5 / float2;
        b7.m11 = float5;
        b7.m22 = (float4 + float3) / (float3 - float4);
        b7.m32 = -1.0f;
        b7.m23 = 2.0f * float4 * float3 / (float3 - float4);
        return b7;
    }
    
    public static Matrix4f orthographic(final float float1, final float float2, final float float3, final float float4) {
        final Matrix4f b5 = new Matrix4f();
        b5.m00 = 2.0f / float1;
        b5.m11 = 2.0f / float2;
        final float float5 = float4 - float3;
        b5.m22 = -2.0f / float5;
        b5.m33 = 1.0f;
        b5.m03 = -1.0f;
        b5.m13 = -1.0f;
        b5.m23 = -(float4 + float3) / float5;
        return b5;
    }
    
    public void translate(final Vector3f g) {
        this.m03 += g.x();
        this.m13 += g.y();
        this.m23 += g.z();
    }
    
    public Matrix4f copy() {
        return new Matrix4f(this);
    }
    
    public static Matrix4f createScaleMatrix(final float float1, final float float2, final float float3) {
        final Matrix4f b4 = new Matrix4f();
        b4.m00 = float1;
        b4.m11 = float2;
        b4.m22 = float3;
        b4.m33 = 1.0f;
        return b4;
    }
    
    public static Matrix4f createTranslateMatrix(final float float1, final float float2, final float float3) {
        final Matrix4f b4 = new Matrix4f();
        b4.m00 = 1.0f;
        b4.m11 = 1.0f;
        b4.m22 = 1.0f;
        b4.m33 = 1.0f;
        b4.m03 = float1;
        b4.m13 = float2;
        b4.m23 = float3;
        return b4;
    }
}
