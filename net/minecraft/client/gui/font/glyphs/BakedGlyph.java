package net.minecraft.client.gui.font.glyphs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.RenderType;

public class BakedGlyph {
    private final RenderType normalType;
    private final RenderType seeThroughType;
    private final float u0;
    private final float u1;
    private final float v0;
    private final float v1;
    private final float left;
    private final float right;
    private final float up;
    private final float down;
    
    public BakedGlyph(final RenderType eag1, final RenderType eag2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final float float10) {
        this.normalType = eag1;
        this.seeThroughType = eag2;
        this.u0 = float3;
        this.u1 = float4;
        this.v0 = float5;
        this.v1 = float6;
        this.left = float7;
        this.right = float8;
        this.up = float9;
        this.down = float10;
    }
    
    public void render(final boolean boolean1, final float float2, final float float3, final Matrix4f b, final VertexConsumer dfn, final float float6, final float float7, final float float8, final float float9, final int integer) {
        final int integer2 = 3;
        final float float10 = float2 + this.left;
        final float float11 = float2 + this.right;
        final float float12 = this.up - 3.0f;
        final float float13 = this.down - 3.0f;
        final float float14 = float3 + float12;
        final float float15 = float3 + float13;
        final float float16 = boolean1 ? (1.0f - 0.25f * float12) : 0.0f;
        final float float17 = boolean1 ? (1.0f - 0.25f * float13) : 0.0f;
        dfn.vertex(b, float10 + float16, float14, 0.0f).color(float6, float7, float8, float9).uv(this.u0, this.v0).uv2(integer).endVertex();
        dfn.vertex(b, float10 + float17, float15, 0.0f).color(float6, float7, float8, float9).uv(this.u0, this.v1).uv2(integer).endVertex();
        dfn.vertex(b, float11 + float17, float15, 0.0f).color(float6, float7, float8, float9).uv(this.u1, this.v1).uv2(integer).endVertex();
        dfn.vertex(b, float11 + float16, float14, 0.0f).color(float6, float7, float8, float9).uv(this.u1, this.v0).uv2(integer).endVertex();
    }
    
    public void renderEffect(final Effect a, final Matrix4f b, final VertexConsumer dfn, final int integer) {
        dfn.vertex(b, a.x0, a.y0, a.depth).color(a.r, a.g, a.b, a.a).uv(this.u0, this.v0).uv2(integer).endVertex();
        dfn.vertex(b, a.x1, a.y0, a.depth).color(a.r, a.g, a.b, a.a).uv(this.u0, this.v1).uv2(integer).endVertex();
        dfn.vertex(b, a.x1, a.y1, a.depth).color(a.r, a.g, a.b, a.a).uv(this.u1, this.v1).uv2(integer).endVertex();
        dfn.vertex(b, a.x0, a.y1, a.depth).color(a.r, a.g, a.b, a.a).uv(this.u1, this.v0).uv2(integer).endVertex();
    }
    
    public RenderType renderType(final boolean boolean1) {
        return boolean1 ? this.seeThroughType : this.normalType;
    }
    
    public static class Effect {
        protected final float x0;
        protected final float y0;
        protected final float x1;
        protected final float y1;
        protected final float depth;
        protected final float r;
        protected final float g;
        protected final float b;
        protected final float a;
        
        public Effect(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9) {
            this.x0 = float1;
            this.y0 = float2;
            this.x1 = float3;
            this.y1 = float4;
            this.depth = float5;
            this.r = float6;
            this.g = float7;
            this.b = float8;
            this.a = float9;
        }
    }
}
