package net.minecraft.client.renderer.culling;

import net.minecraft.world.phys.AABB;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;

public class Frustum {
    private final Vector4f[] frustumData;
    private double camX;
    private double camY;
    private double camZ;
    
    public Frustum(final Matrix4f b1, final Matrix4f b2) {
        this.frustumData = new Vector4f[6];
        this.calculateFrustum(b1, b2);
    }
    
    public void prepare(final double double1, final double double2, final double double3) {
        this.camX = double1;
        this.camY = double2;
        this.camZ = double3;
    }
    
    private void calculateFrustum(final Matrix4f b1, final Matrix4f b2) {
        final Matrix4f b3 = b2.copy();
        b3.multiply(b1);
        b3.transpose();
        this.getPlane(b3, -1, 0, 0, 0);
        this.getPlane(b3, 1, 0, 0, 1);
        this.getPlane(b3, 0, -1, 0, 2);
        this.getPlane(b3, 0, 1, 0, 3);
        this.getPlane(b3, 0, 0, -1, 4);
        this.getPlane(b3, 0, 0, 1, 5);
    }
    
    private void getPlane(final Matrix4f b, final int integer2, final int integer3, final int integer4, final int integer5) {
        final Vector4f h7 = new Vector4f((float)integer2, (float)integer3, (float)integer4, 1.0f);
        h7.transform(b);
        h7.normalize();
        this.frustumData[integer5] = h7;
    }
    
    public boolean isVisible(final AABB dcf) {
        return this.cubeInFrustum(dcf.minX, dcf.minY, dcf.minZ, dcf.maxX, dcf.maxY, dcf.maxZ);
    }
    
    private boolean cubeInFrustum(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        final float float14 = (float)(double1 - this.camX);
        final float float15 = (float)(double2 - this.camY);
        final float float16 = (float)(double3 - this.camZ);
        final float float17 = (float)(double4 - this.camX);
        final float float18 = (float)(double5 - this.camY);
        final float float19 = (float)(double6 - this.camZ);
        return this.cubeInFrustum(float14, float15, float16, float17, float18, float19);
    }
    
    private boolean cubeInFrustum(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6) {
        for (int integer8 = 0; integer8 < 6; ++integer8) {
            final Vector4f h9 = this.frustumData[integer8];
            if (h9.dot(new Vector4f(float1, float2, float3, 1.0f)) <= 0.0f) {
                if (h9.dot(new Vector4f(float4, float2, float3, 1.0f)) <= 0.0f) {
                    if (h9.dot(new Vector4f(float1, float5, float3, 1.0f)) <= 0.0f) {
                        if (h9.dot(new Vector4f(float4, float5, float3, 1.0f)) <= 0.0f) {
                            if (h9.dot(new Vector4f(float1, float2, float6, 1.0f)) <= 0.0f) {
                                if (h9.dot(new Vector4f(float4, float2, float6, 1.0f)) <= 0.0f) {
                                    if (h9.dot(new Vector4f(float1, float5, float6, 1.0f)) <= 0.0f) {
                                        if (h9.dot(new Vector4f(float4, float5, float6, 1.0f)) <= 0.0f) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
