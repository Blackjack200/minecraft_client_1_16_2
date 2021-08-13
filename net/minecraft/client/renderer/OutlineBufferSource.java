package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import java.util.Optional;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder;

public class OutlineBufferSource implements MultiBufferSource {
    private final BufferSource bufferSource;
    private final BufferSource outlineBufferSource;
    private int teamR;
    private int teamG;
    private int teamB;
    private int teamA;
    
    public OutlineBufferSource(final BufferSource a) {
        this.outlineBufferSource = MultiBufferSource.immediate(new BufferBuilder(256));
        this.teamR = 255;
        this.teamG = 255;
        this.teamB = 255;
        this.teamA = 255;
        this.bufferSource = a;
    }
    
    public VertexConsumer getBuffer(final RenderType eag) {
        if (eag.isOutline()) {
            final VertexConsumer dfn3 = this.outlineBufferSource.getBuffer(eag);
            return new EntityOutlineGenerator(dfn3, this.teamR, this.teamG, this.teamB, this.teamA);
        }
        final VertexConsumer dfn3 = this.bufferSource.getBuffer(eag);
        final Optional<RenderType> optional4 = eag.outline();
        if (optional4.isPresent()) {
            final VertexConsumer dfn4 = this.outlineBufferSource.getBuffer((RenderType)optional4.get());
            final EntityOutlineGenerator a6 = new EntityOutlineGenerator(dfn4, this.teamR, this.teamG, this.teamB, this.teamA);
            return VertexMultiConsumer.create(a6, dfn3);
        }
        return dfn3;
    }
    
    public void setColor(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.teamR = integer1;
        this.teamG = integer2;
        this.teamB = integer3;
        this.teamA = integer4;
    }
    
    public void endOutlineBatch() {
        this.outlineBufferSource.endBatch();
    }
    
    static class EntityOutlineGenerator extends DefaultedVertexConsumer {
        private final VertexConsumer delegate;
        private double x;
        private double y;
        private double z;
        private float u;
        private float v;
        
        private EntityOutlineGenerator(final VertexConsumer dfn, final int integer2, final int integer3, final int integer4, final int integer5) {
            this.delegate = dfn;
            super.defaultColor(integer2, integer3, integer4, integer5);
        }
        
        @Override
        public void defaultColor(final int integer1, final int integer2, final int integer3, final int integer4) {
        }
        
        public VertexConsumer vertex(final double double1, final double double2, final double double3) {
            this.x = double1;
            this.y = double2;
            this.z = double3;
            return this;
        }
        
        public VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4) {
            return this;
        }
        
        public VertexConsumer uv(final float float1, final float float2) {
            this.u = float1;
            this.v = float2;
            return this;
        }
        
        public VertexConsumer overlayCoords(final int integer1, final int integer2) {
            return this;
        }
        
        public VertexConsumer uv2(final int integer1, final int integer2) {
            return this;
        }
        
        public VertexConsumer normal(final float float1, final float float2, final float float3) {
            return this;
        }
        
        public void vertex(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final int integer10, final int integer11, final float float12, final float float13, final float float14) {
            this.delegate.vertex(float1, float2, float3).color(this.defaultR, this.defaultG, this.defaultB, this.defaultA).uv(float8, float9).endVertex();
        }
        
        public void endVertex() {
            this.delegate.vertex(this.x, this.y, this.z).color(this.defaultR, this.defaultG, this.defaultB, this.defaultA).uv(this.u, this.v).endVertex();
        }
    }
}
