package com.mojang.blaze3d.vertex;

public class VertexMultiConsumer {
    public static VertexConsumer create(final VertexConsumer dfn1, final VertexConsumer dfn2) {
        return new Double(dfn1, dfn2);
    }
    
    static class Double implements VertexConsumer {
        private final VertexConsumer first;
        private final VertexConsumer second;
        
        public Double(final VertexConsumer dfn1, final VertexConsumer dfn2) {
            if (dfn1 == dfn2) {
                throw new IllegalArgumentException("Duplicate delegates");
            }
            this.first = dfn1;
            this.second = dfn2;
        }
        
        public VertexConsumer vertex(final double double1, final double double2, final double double3) {
            this.first.vertex(double1, double2, double3);
            this.second.vertex(double1, double2, double3);
            return this;
        }
        
        public VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4) {
            this.first.color(integer1, integer2, integer3, integer4);
            this.second.color(integer1, integer2, integer3, integer4);
            return this;
        }
        
        public VertexConsumer uv(final float float1, final float float2) {
            this.first.uv(float1, float2);
            this.second.uv(float1, float2);
            return this;
        }
        
        public VertexConsumer overlayCoords(final int integer1, final int integer2) {
            this.first.overlayCoords(integer1, integer2);
            this.second.overlayCoords(integer1, integer2);
            return this;
        }
        
        public VertexConsumer uv2(final int integer1, final int integer2) {
            this.first.uv2(integer1, integer2);
            this.second.uv2(integer1, integer2);
            return this;
        }
        
        public VertexConsumer normal(final float float1, final float float2, final float float3) {
            this.first.normal(float1, float2, float3);
            this.second.normal(float1, float2, float3);
            return this;
        }
        
        public void vertex(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final int integer10, final int integer11, final float float12, final float float13, final float float14) {
            this.first.vertex(float1, float2, float3, float4, float5, float6, float7, float8, float9, integer10, integer11, float12, float13, float14);
            this.second.vertex(float1, float2, float3, float4, float5, float6, float7, float8, float9, integer10, integer11, float12, float13, float14);
        }
        
        public void endVertex() {
            this.first.endVertex();
            this.second.endVertex();
        }
    }
}
