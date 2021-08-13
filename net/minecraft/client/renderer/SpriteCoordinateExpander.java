package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class SpriteCoordinateExpander implements VertexConsumer {
    private final VertexConsumer delegate;
    private final TextureAtlasSprite sprite;
    
    public SpriteCoordinateExpander(final VertexConsumer dfn, final TextureAtlasSprite eju) {
        this.delegate = dfn;
        this.sprite = eju;
    }
    
    public VertexConsumer vertex(final double double1, final double double2, final double double3) {
        return this.delegate.vertex(double1, double2, double3);
    }
    
    public VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4) {
        return this.delegate.color(integer1, integer2, integer3, integer4);
    }
    
    public VertexConsumer uv(final float float1, final float float2) {
        return this.delegate.uv(this.sprite.getU(float1 * 16.0f), this.sprite.getV(float2 * 16.0f));
    }
    
    public VertexConsumer overlayCoords(final int integer1, final int integer2) {
        return this.delegate.overlayCoords(integer1, integer2);
    }
    
    public VertexConsumer uv2(final int integer1, final int integer2) {
        return this.delegate.uv2(integer1, integer2);
    }
    
    public VertexConsumer normal(final float float1, final float float2, final float float3) {
        return this.delegate.normal(float1, float2, float3);
    }
    
    public void endVertex() {
        this.delegate.endVertex();
    }
    
    public void vertex(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final int integer10, final int integer11, final float float12, final float float13, final float float14) {
        this.delegate.vertex(float1, float2, float3, float4, float5, float6, float7, this.sprite.getU(float8 * 16.0f), this.sprite.getV(float9 * 16.0f), integer10, integer11, float12, float13, float14);
    }
}
