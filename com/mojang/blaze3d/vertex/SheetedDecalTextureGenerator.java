package com.mojang.blaze3d.vertex;

import com.mojang.math.Vector4f;
import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;

public class SheetedDecalTextureGenerator extends DefaultedVertexConsumer {
    private final VertexConsumer delegate;
    private final Matrix4f cameraInversePose;
    private final Matrix3f normalInversePose;
    private float x;
    private float y;
    private float z;
    private int overlayU;
    private int overlayV;
    private int lightCoords;
    private float nx;
    private float ny;
    private float nz;
    
    public SheetedDecalTextureGenerator(final VertexConsumer dfn, final Matrix4f b, final Matrix3f a) {
        this.delegate = dfn;
        (this.cameraInversePose = b.copy()).invert();
        (this.normalInversePose = a.copy()).invert();
        this.resetState();
    }
    
    private void resetState() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.overlayU = 0;
        this.overlayV = 10;
        this.lightCoords = 15728880;
        this.nx = 0.0f;
        this.ny = 1.0f;
        this.nz = 0.0f;
    }
    
    public void endVertex() {
        final Vector3f g2 = new Vector3f(this.nx, this.ny, this.nz);
        g2.transform(this.normalInversePose);
        final Direction gc3 = Direction.getNearest(g2.x(), g2.y(), g2.z());
        final Vector4f h4 = new Vector4f(this.x, this.y, this.z, 1.0f);
        h4.transform(this.cameraInversePose);
        h4.transform(Vector3f.YP.rotationDegrees(180.0f));
        h4.transform(Vector3f.XP.rotationDegrees(-90.0f));
        h4.transform(gc3.getRotation());
        final float float5 = -h4.x();
        final float float6 = -h4.y();
        this.delegate.vertex(this.x, this.y, this.z).color(1.0f, 1.0f, 1.0f, 1.0f).uv(float5, float6).overlayCoords(this.overlayU, this.overlayV).uv2(this.lightCoords).normal(this.nx, this.ny, this.nz).endVertex();
        this.resetState();
    }
    
    public VertexConsumer vertex(final double double1, final double double2, final double double3) {
        this.x = (float)double1;
        this.y = (float)double2;
        this.z = (float)double3;
        return this;
    }
    
    public VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4) {
        return this;
    }
    
    public VertexConsumer uv(final float float1, final float float2) {
        return this;
    }
    
    public VertexConsumer overlayCoords(final int integer1, final int integer2) {
        this.overlayU = integer1;
        this.overlayV = integer2;
        return this;
    }
    
    public VertexConsumer uv2(final int integer1, final int integer2) {
        this.lightCoords = (integer1 | integer2 << 16);
        return this;
    }
    
    public VertexConsumer normal(final float float1, final float float2, final float float3) {
        this.nx = float1;
        this.ny = float2;
        this.nz = float3;
        return this;
    }
}
