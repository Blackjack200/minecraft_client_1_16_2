package com.mojang.math;

import org.apache.commons.lang3.tuple.Triple;
import net.minecraft.util.Mth;
import com.mojang.datafixers.util.Pair;

public final class Matrix3f {
    private static final float G;
    private static final float CS;
    private static final float SS;
    private static final float SQ2;
    protected float m00;
    protected float m01;
    protected float m02;
    protected float m10;
    protected float m11;
    protected float m12;
    protected float m20;
    protected float m21;
    protected float m22;
    
    public Matrix3f() {
    }
    
    public Matrix3f(final Quaternion d) {
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
    
    public static Matrix3f createScaleMatrix(final float float1, final float float2, final float float3) {
        final Matrix3f a4 = new Matrix3f();
        a4.m00 = float1;
        a4.m11 = float2;
        a4.m22 = float3;
        return a4;
    }
    
    public Matrix3f(final Matrix4f b) {
        this.m00 = b.m00;
        this.m01 = b.m01;
        this.m02 = b.m02;
        this.m10 = b.m10;
        this.m11 = b.m11;
        this.m12 = b.m12;
        this.m20 = b.m20;
        this.m21 = b.m21;
        this.m22 = b.m22;
    }
    
    public Matrix3f(final Matrix3f a) {
        this.m00 = a.m00;
        this.m01 = a.m01;
        this.m02 = a.m02;
        this.m10 = a.m10;
        this.m11 = a.m11;
        this.m12 = a.m12;
        this.m20 = a.m20;
        this.m21 = a.m21;
        this.m22 = a.m22;
    }
    
    private static Pair<Float, Float> approxGivensQuat(final float float1, final float float2, final float float3) {
        final float float4 = 2.0f * (float1 - float3);
        final float float5 = float2;
        if (Matrix3f.G * float5 * float5 < float4 * float4) {
            final float float6 = Mth.fastInvSqrt(float5 * float5 + float4 * float4);
            return (Pair<Float, Float>)Pair.of((float6 * float5), (float6 * float4));
        }
        return (Pair<Float, Float>)Pair.of(Matrix3f.SS, Matrix3f.CS);
    }
    
    private static Pair<Float, Float> qrGivensQuat(final float float1, final float float2) {
        final float float3 = (float)Math.hypot((double)float1, (double)float2);
        float float4 = (float3 > 1.0E-6f) ? float2 : 0.0f;
        float float5 = Math.abs(float1) + Math.max(float3, 1.0E-6f);
        if (float1 < 0.0f) {
            final float float6 = float4;
            float4 = float5;
            float5 = float6;
        }
        final float float6 = Mth.fastInvSqrt(float5 * float5 + float4 * float4);
        float5 *= float6;
        float4 *= float6;
        return (Pair<Float, Float>)Pair.of(float4, float5);
    }
    
    private static Quaternion stepJacobi(final Matrix3f a) {
        final Matrix3f a2 = new Matrix3f();
        final Quaternion d3 = Quaternion.ONE.copy();
        if (a.m01 * a.m01 + a.m10 * a.m10 > 1.0E-6f) {
            final Pair<Float, Float> pair4 = approxGivensQuat(a.m00, 0.5f * (a.m01 + a.m10), a.m11);
            final Float float5 = (Float)pair4.getFirst();
            final Float float6 = (Float)pair4.getSecond();
            final Quaternion d4 = new Quaternion(0.0f, 0.0f, float5, float6);
            final float float7 = float6 * float6 - float5 * float5;
            final float float8 = -2.0f * float5 * float6;
            final float float9 = float6 * float6 + float5 * float5;
            d3.mul(d4);
            a2.setIdentity();
            a2.m00 = float7;
            a2.m11 = float7;
            a2.m10 = -float8;
            a2.m01 = float8;
            a2.m22 = float9;
            a.mul(a2);
            a2.transpose();
            a2.mul(a);
            a.load(a2);
        }
        if (a.m02 * a.m02 + a.m20 * a.m20 > 1.0E-6f) {
            final Pair<Float, Float> pair4 = approxGivensQuat(a.m00, 0.5f * (a.m02 + a.m20), a.m22);
            final float float10 = -(float)pair4.getFirst();
            final Float float6 = (Float)pair4.getSecond();
            final Quaternion d4 = new Quaternion(0.0f, float10, 0.0f, float6);
            final float float7 = float6 * float6 - float10 * float10;
            final float float8 = -2.0f * float10 * float6;
            final float float9 = float6 * float6 + float10 * float10;
            d3.mul(d4);
            a2.setIdentity();
            a2.m00 = float7;
            a2.m22 = float7;
            a2.m20 = float8;
            a2.m02 = -float8;
            a2.m11 = float9;
            a.mul(a2);
            a2.transpose();
            a2.mul(a);
            a.load(a2);
        }
        if (a.m12 * a.m12 + a.m21 * a.m21 > 1.0E-6f) {
            final Pair<Float, Float> pair4 = approxGivensQuat(a.m11, 0.5f * (a.m12 + a.m21), a.m22);
            final Float float5 = (Float)pair4.getFirst();
            final Float float6 = (Float)pair4.getSecond();
            final Quaternion d4 = new Quaternion(float5, 0.0f, 0.0f, float6);
            final float float7 = float6 * float6 - float5 * float5;
            final float float8 = -2.0f * float5 * float6;
            final float float9 = float6 * float6 + float5 * float5;
            d3.mul(d4);
            a2.setIdentity();
            a2.m11 = float7;
            a2.m22 = float7;
            a2.m21 = -float8;
            a2.m12 = float8;
            a2.m00 = float9;
            a.mul(a2);
            a2.transpose();
            a2.mul(a);
            a.load(a2);
        }
        return d3;
    }
    
    public void transpose() {
        float float2 = this.m01;
        this.m01 = this.m10;
        this.m10 = float2;
        float2 = this.m02;
        this.m02 = this.m20;
        this.m20 = float2;
        float2 = this.m12;
        this.m12 = this.m21;
        this.m21 = float2;
    }
    
    public Triple<Quaternion, Vector3f, Quaternion> svdDecompose() {
        final Quaternion d2 = Quaternion.ONE.copy();
        final Quaternion d3 = Quaternion.ONE.copy();
        final Matrix3f a4 = this.copy();
        a4.transpose();
        a4.mul(this);
        for (int integer5 = 0; integer5 < 5; ++integer5) {
            d3.mul(stepJacobi(a4));
        }
        d3.normalize();
        final Matrix3f a5 = new Matrix3f(this);
        a5.mul(new Matrix3f(d3));
        float float7 = 1.0f;
        Pair<Float, Float> pair6 = qrGivensQuat(a5.m00, a5.m10);
        final Float float8 = (Float)pair6.getFirst();
        final Float float9 = (Float)pair6.getSecond();
        final float float10 = float9 * float9 - float8 * float8;
        final float float11 = -2.0f * float8 * float9;
        final float float12 = float9 * float9 + float8 * float8;
        final Quaternion d4 = new Quaternion(0.0f, 0.0f, float8, float9);
        d2.mul(d4);
        final Matrix3f a6 = new Matrix3f();
        a6.setIdentity();
        a6.m00 = float10;
        a6.m11 = float10;
        a6.m10 = float11;
        a6.m01 = -float11;
        a6.m22 = float12;
        float7 *= float12;
        a6.mul(a5);
        pair6 = qrGivensQuat(a6.m00, a6.m20);
        final float float13 = -(float)pair6.getFirst();
        final Float float14 = (Float)pair6.getSecond();
        final float float15 = float14 * float14 - float13 * float13;
        final float float16 = -2.0f * float13 * float14;
        final float float17 = float14 * float14 + float13 * float13;
        final Quaternion d5 = new Quaternion(0.0f, float13, 0.0f, float14);
        d2.mul(d5);
        final Matrix3f a7 = new Matrix3f();
        a7.setIdentity();
        a7.m00 = float15;
        a7.m22 = float15;
        a7.m20 = -float16;
        a7.m02 = float16;
        a7.m11 = float17;
        float7 *= float17;
        a7.mul(a6);
        pair6 = qrGivensQuat(a7.m11, a7.m21);
        final Float float18 = (Float)pair6.getFirst();
        final Float float19 = (Float)pair6.getSecond();
        final float float20 = float19 * float19 - float18 * float18;
        final float float21 = -2.0f * float18 * float19;
        final float float22 = float19 * float19 + float18 * float18;
        final Quaternion d6 = new Quaternion(float18, 0.0f, 0.0f, float19);
        d2.mul(d6);
        final Matrix3f a8 = new Matrix3f();
        a8.setIdentity();
        a8.m11 = float20;
        a8.m22 = float20;
        a8.m21 = float21;
        a8.m12 = -float21;
        a8.m00 = float22;
        float7 *= float22;
        a8.mul(a7);
        float7 = 1.0f / float7;
        d2.mul((float)Math.sqrt((double)float7));
        final Vector3f g29 = new Vector3f(a8.m00 * float7, a8.m11 * float7, a8.m22 * float7);
        return (Triple<Quaternion, Vector3f, Quaternion>)Triple.of(d2, g29, d3);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final Matrix3f a3 = (Matrix3f)object;
        return Float.compare(a3.m00, this.m00) == 0 && Float.compare(a3.m01, this.m01) == 0 && Float.compare(a3.m02, this.m02) == 0 && Float.compare(a3.m10, this.m10) == 0 && Float.compare(a3.m11, this.m11) == 0 && Float.compare(a3.m12, this.m12) == 0 && Float.compare(a3.m20, this.m20) == 0 && Float.compare(a3.m21, this.m21) == 0 && Float.compare(a3.m22, this.m22) == 0;
    }
    
    public int hashCode() {
        int integer2 = (this.m00 != 0.0f) ? Float.floatToIntBits(this.m00) : 0;
        integer2 = 31 * integer2 + ((this.m01 != 0.0f) ? Float.floatToIntBits(this.m01) : 0);
        integer2 = 31 * integer2 + ((this.m02 != 0.0f) ? Float.floatToIntBits(this.m02) : 0);
        integer2 = 31 * integer2 + ((this.m10 != 0.0f) ? Float.floatToIntBits(this.m10) : 0);
        integer2 = 31 * integer2 + ((this.m11 != 0.0f) ? Float.floatToIntBits(this.m11) : 0);
        integer2 = 31 * integer2 + ((this.m12 != 0.0f) ? Float.floatToIntBits(this.m12) : 0);
        integer2 = 31 * integer2 + ((this.m20 != 0.0f) ? Float.floatToIntBits(this.m20) : 0);
        integer2 = 31 * integer2 + ((this.m21 != 0.0f) ? Float.floatToIntBits(this.m21) : 0);
        integer2 = 31 * integer2 + ((this.m22 != 0.0f) ? Float.floatToIntBits(this.m22) : 0);
        return integer2;
    }
    
    public void load(final Matrix3f a) {
        this.m00 = a.m00;
        this.m01 = a.m01;
        this.m02 = a.m02;
        this.m10 = a.m10;
        this.m11 = a.m11;
        this.m12 = a.m12;
        this.m20 = a.m20;
        this.m21 = a.m21;
        this.m22 = a.m22;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Matrix3f:\n");
        stringBuilder2.append(this.m00);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m01);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m02);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m10);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m11);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m12);
        stringBuilder2.append("\n");
        stringBuilder2.append(this.m20);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m21);
        stringBuilder2.append(" ");
        stringBuilder2.append(this.m22);
        stringBuilder2.append("\n");
        return stringBuilder2.toString();
    }
    
    public void setIdentity() {
        this.m00 = 1.0f;
        this.m01 = 0.0f;
        this.m02 = 0.0f;
        this.m10 = 0.0f;
        this.m11 = 1.0f;
        this.m12 = 0.0f;
        this.m20 = 0.0f;
        this.m21 = 0.0f;
        this.m22 = 1.0f;
    }
    
    public float adjugateAndDet() {
        final float float2 = this.m11 * this.m22 - this.m12 * this.m21;
        final float float3 = -(this.m10 * this.m22 - this.m12 * this.m20);
        final float float4 = this.m10 * this.m21 - this.m11 * this.m20;
        final float float5 = -(this.m01 * this.m22 - this.m02 * this.m21);
        final float float6 = this.m00 * this.m22 - this.m02 * this.m20;
        final float float7 = -(this.m00 * this.m21 - this.m01 * this.m20);
        final float float8 = this.m01 * this.m12 - this.m02 * this.m11;
        final float float9 = -(this.m00 * this.m12 - this.m02 * this.m10);
        final float float10 = this.m00 * this.m11 - this.m01 * this.m10;
        final float float11 = this.m00 * float2 + this.m01 * float3 + this.m02 * float4;
        this.m00 = float2;
        this.m10 = float3;
        this.m20 = float4;
        this.m01 = float5;
        this.m11 = float6;
        this.m21 = float7;
        this.m02 = float8;
        this.m12 = float9;
        this.m22 = float10;
        return float11;
    }
    
    public boolean invert() {
        final float float2 = this.adjugateAndDet();
        if (Math.abs(float2) > 1.0E-6f) {
            this.mul(float2);
            return true;
        }
        return false;
    }
    
    public void set(final int integer1, final int integer2, final float float3) {
        if (integer1 == 0) {
            if (integer2 == 0) {
                this.m00 = float3;
            }
            else if (integer2 == 1) {
                this.m01 = float3;
            }
            else {
                this.m02 = float3;
            }
        }
        else if (integer1 == 1) {
            if (integer2 == 0) {
                this.m10 = float3;
            }
            else if (integer2 == 1) {
                this.m11 = float3;
            }
            else {
                this.m12 = float3;
            }
        }
        else if (integer2 == 0) {
            this.m20 = float3;
        }
        else if (integer2 == 1) {
            this.m21 = float3;
        }
        else {
            this.m22 = float3;
        }
    }
    
    public void mul(final Matrix3f a) {
        final float float3 = this.m00 * a.m00 + this.m01 * a.m10 + this.m02 * a.m20;
        final float float4 = this.m00 * a.m01 + this.m01 * a.m11 + this.m02 * a.m21;
        final float float5 = this.m00 * a.m02 + this.m01 * a.m12 + this.m02 * a.m22;
        final float float6 = this.m10 * a.m00 + this.m11 * a.m10 + this.m12 * a.m20;
        final float float7 = this.m10 * a.m01 + this.m11 * a.m11 + this.m12 * a.m21;
        final float float8 = this.m10 * a.m02 + this.m11 * a.m12 + this.m12 * a.m22;
        final float float9 = this.m20 * a.m00 + this.m21 * a.m10 + this.m22 * a.m20;
        final float float10 = this.m20 * a.m01 + this.m21 * a.m11 + this.m22 * a.m21;
        final float float11 = this.m20 * a.m02 + this.m21 * a.m12 + this.m22 * a.m22;
        this.m00 = float3;
        this.m01 = float4;
        this.m02 = float5;
        this.m10 = float6;
        this.m11 = float7;
        this.m12 = float8;
        this.m20 = float9;
        this.m21 = float10;
        this.m22 = float11;
    }
    
    public void mul(final Quaternion d) {
        this.mul(new Matrix3f(d));
    }
    
    public void mul(final float float1) {
        this.m00 *= float1;
        this.m01 *= float1;
        this.m02 *= float1;
        this.m10 *= float1;
        this.m11 *= float1;
        this.m12 *= float1;
        this.m20 *= float1;
        this.m21 *= float1;
        this.m22 *= float1;
    }
    
    public Matrix3f copy() {
        return new Matrix3f(this);
    }
    
    static {
        G = 3.0f + 2.0f * (float)Math.sqrt(2.0);
        CS = (float)Math.cos(0.39269908169872414);
        SS = (float)Math.sin(0.39269908169872414);
        SQ2 = 1.0f / (float)Math.sqrt(2.0);
    }
}
