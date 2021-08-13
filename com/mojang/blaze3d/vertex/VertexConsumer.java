package com.mojang.blaze3d.vertex;

import org.apache.logging.log4j.LogManager;
import com.mojang.math.Matrix3f;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import com.mojang.math.Matrix4f;
import net.minecraft.core.Vec3i;
import com.mojang.math.Vector4f;
import org.lwjgl.system.MemoryStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.block.model.BakedQuad;
import org.apache.logging.log4j.Logger;

public interface VertexConsumer {
    public static final Logger LOGGER = LogManager.getLogger();
    
    VertexConsumer vertex(final double double1, final double double2, final double double3);
    
    VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4);
    
    VertexConsumer uv(final float float1, final float float2);
    
    VertexConsumer overlayCoords(final int integer1, final int integer2);
    
    VertexConsumer uv2(final int integer1, final int integer2);
    
    VertexConsumer normal(final float float1, final float float2, final float float3);
    
    void endVertex();
    
    default void vertex(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final int integer10, final int integer11, final float float12, final float float13, final float float14) {
        this.vertex(float1, float2, float3);
        this.color(float4, float5, float6, float7);
        this.uv(float8, float9);
        this.overlayCoords(integer10);
        this.uv2(integer11);
        this.normal(float12, float13, float14);
        this.endVertex();
    }
    
    default VertexConsumer color(final float float1, final float float2, final float float3, final float float4) {
        return this.color((int)(float1 * 255.0f), (int)(float2 * 255.0f), (int)(float3 * 255.0f), (int)(float4 * 255.0f));
    }
    
    default VertexConsumer uv2(final int integer) {
        return this.uv2(integer & 0xFFFF, integer >> 16 & 0xFFFF);
    }
    
    default VertexConsumer overlayCoords(final int integer) {
        return this.overlayCoords(integer & 0xFFFF, integer >> 16 & 0xFFFF);
    }
    
    default void putBulkData(final PoseStack.Pose a, final BakedQuad eas, final float float3, final float float4, final float float5, final int integer6, final int integer7) {
        this.putBulkData(a, eas, new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, float3, float4, float5, new int[] { integer6, integer6, integer6, integer6 }, integer7, false);
    }
    
    default void putBulkData(final PoseStack.Pose a, final BakedQuad eas, final float[] arr, final float float4, final float float5, final float float6, final int[] arr, final int integer, final boolean boolean9) {
        final int[] arr2 = eas.getVertices();
        final Vec3i gr12 = eas.getDirection().getNormal();
        final Vector3f g13 = new Vector3f((float)gr12.getX(), (float)gr12.getY(), (float)gr12.getZ());
        final Matrix4f b14 = a.pose();
        g13.transform(a.normal());
        final int integer2 = 8;
        final int integer3 = arr2.length / 8;
        try (final MemoryStack memoryStack17 = MemoryStack.stackPush()) {
            final ByteBuffer byteBuffer19 = memoryStack17.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
            final IntBuffer intBuffer20 = byteBuffer19.asIntBuffer();
            for (int integer4 = 0; integer4 < integer3; ++integer4) {
                intBuffer20.clear();
                intBuffer20.put(arr2, integer4 * 8, 8);
                final float float7 = byteBuffer19.getFloat(0);
                final float float8 = byteBuffer19.getFloat(4);
                final float float9 = byteBuffer19.getFloat(8);
                float float13;
                float float14;
                float float15;
                if (boolean9) {
                    final float float10 = (byteBuffer19.get(12) & 0xFF) / 255.0f;
                    final float float11 = (byteBuffer19.get(13) & 0xFF) / 255.0f;
                    final float float12 = (byteBuffer19.get(14) & 0xFF) / 255.0f;
                    float13 = float10 * arr[integer4] * float4;
                    float14 = float11 * arr[integer4] * float5;
                    float15 = float12 * arr[integer4] * float6;
                }
                else {
                    float13 = arr[integer4] * float4;
                    float14 = arr[integer4] * float5;
                    float15 = arr[integer4] * float6;
                }
                final int integer5 = arr[integer4];
                final float float11 = byteBuffer19.getFloat(16);
                final float float12 = byteBuffer19.getFloat(20);
                final Vector4f h31 = new Vector4f(float7, float8, float9, 1.0f);
                h31.transform(b14);
                this.vertex(h31.x(), h31.y(), h31.z(), float13, float14, float15, 1.0f, float11, float12, integer, integer5, g13.x(), g13.y(), g13.z());
            }
        }
    }
    
    default VertexConsumer vertex(final Matrix4f b, final float float2, final float float3, final float float4) {
        final Vector4f h6 = new Vector4f(float2, float3, float4, 1.0f);
        h6.transform(b);
        return this.vertex(h6.x(), h6.y(), h6.z());
    }
    
    default VertexConsumer normal(final Matrix3f a, final float float2, final float float3, final float float4) {
        final Vector3f g6 = new Vector3f(float2, float3, float4);
        g6.transform(a);
        return this.normal(g6.x(), g6.y(), g6.z());
    }
}
