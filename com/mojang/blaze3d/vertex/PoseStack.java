package com.mojang.blaze3d.vertex;

import java.util.ArrayDeque;
import com.mojang.math.Quaternion;
import com.mojang.math.Matrix3f;
import net.minecraft.util.Mth;
import com.mojang.math.Matrix4f;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Queues;
import java.util.Deque;

public class PoseStack {
    private final Deque<Pose> poseStack;
    
    public PoseStack() {
        this.poseStack = Util.make((Deque)Queues.newArrayDeque(), (java.util.function.Consumer<Deque>)(arrayDeque -> {
            final Matrix4f b2 = new Matrix4f();
            b2.setIdentity();
            final Matrix3f a3 = new Matrix3f();
            a3.setIdentity();
            arrayDeque.add(new Pose(b2, a3));
        }));
    }
    
    public void translate(final double double1, final double double2, final double double3) {
        final Pose a8 = (Pose)this.poseStack.getLast();
        a8.pose.multiply(Matrix4f.createTranslateMatrix((float)double1, (float)double2, (float)double3));
    }
    
    public void scale(final float float1, final float float2, final float float3) {
        final Pose a5 = (Pose)this.poseStack.getLast();
        a5.pose.multiply(Matrix4f.createScaleMatrix(float1, float2, float3));
        if (float1 == float2 && float2 == float3) {
            if (float1 > 0.0f) {
                return;
            }
            a5.normal.mul(-1.0f);
        }
        final float float4 = 1.0f / float1;
        final float float5 = 1.0f / float2;
        final float float6 = 1.0f / float3;
        final float float7 = Mth.fastInvCubeRoot(float4 * float5 * float6);
        a5.normal.mul(Matrix3f.createScaleMatrix(float7 * float4, float7 * float5, float7 * float6));
    }
    
    public void mulPose(final Quaternion d) {
        final Pose a3 = (Pose)this.poseStack.getLast();
        a3.pose.multiply(d);
        a3.normal.mul(d);
    }
    
    public void pushPose() {
        final Pose a2 = (Pose)this.poseStack.getLast();
        this.poseStack.addLast(new Pose(a2.pose.copy(), a2.normal.copy()));
    }
    
    public void popPose() {
        this.poseStack.removeLast();
    }
    
    public Pose last() {
        return (Pose)this.poseStack.getLast();
    }
    
    public boolean clear() {
        return this.poseStack.size() == 1;
    }
    
    public static final class Pose {
        private final Matrix4f pose;
        private final Matrix3f normal;
        
        private Pose(final Matrix4f b, final Matrix3f a) {
            this.pose = b;
            this.normal = a;
        }
        
        public Matrix4f pose() {
            return this.pose;
        }
        
        public Matrix3f normal() {
            return this.normal;
        }
    }
}
