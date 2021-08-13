package net.minecraft.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.texture.TextureAtlas;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureManager;
import com.mojang.blaze3d.vertex.BufferBuilder;

public interface ParticleRenderType {
    public static final ParticleRenderType TERRAIN_SHEET = new ParticleRenderType() {
        public void begin(final BufferBuilder dfe, final TextureManager ejv) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            ejv.bind(TextureAtlas.LOCATION_BLOCKS);
            dfe.begin(7, DefaultVertexFormat.PARTICLE);
        }
        
        public void end(final Tesselator dfl) {
            dfl.end();
        }
        
        public String toString() {
            return "TERRAIN_SHEET";
        }
    };
    public static final ParticleRenderType PARTICLE_SHEET_OPAQUE = new ParticleRenderType() {
        public void begin(final BufferBuilder dfe, final TextureManager ejv) {
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            ejv.bind(TextureAtlas.LOCATION_PARTICLES);
            dfe.begin(7, DefaultVertexFormat.PARTICLE);
        }
        
        public void end(final Tesselator dfl) {
            dfl.end();
        }
        
        public String toString() {
            return "PARTICLE_SHEET_OPAQUE";
        }
    };
    public static final ParticleRenderType PARTICLE_SHEET_TRANSLUCENT = new ParticleRenderType() {
        public void begin(final BufferBuilder dfe, final TextureManager ejv) {
            RenderSystem.depthMask(true);
            ejv.bind(TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.alphaFunc(516, 0.003921569f);
            dfe.begin(7, DefaultVertexFormat.PARTICLE);
        }
        
        public void end(final Tesselator dfl) {
            dfl.end();
        }
        
        public String toString() {
            return "PARTICLE_SHEET_TRANSLUCENT";
        }
    };
    public static final ParticleRenderType PARTICLE_SHEET_LIT = new ParticleRenderType() {
        public void begin(final BufferBuilder dfe, final TextureManager ejv) {
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            ejv.bind(TextureAtlas.LOCATION_PARTICLES);
            dfe.begin(7, DefaultVertexFormat.PARTICLE);
        }
        
        public void end(final Tesselator dfl) {
            dfl.end();
        }
        
        public String toString() {
            return "PARTICLE_SHEET_LIT";
        }
    };
    public static final ParticleRenderType CUSTOM = new ParticleRenderType() {
        public void begin(final BufferBuilder dfe, final TextureManager ejv) {
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
        }
        
        public void end(final Tesselator dfl) {
        }
        
        public String toString() {
            return "CUSTOM";
        }
    };
    public static final ParticleRenderType NO_RENDER = new ParticleRenderType() {
        public void begin(final BufferBuilder dfe, final TextureManager ejv) {
        }
        
        public void end(final Tesselator dfl) {
        }
        
        public String toString() {
            return "NO_RENDER";
        }
    };
    
    void begin(final BufferBuilder dfe, final TextureManager ejv);
    
    void end(final Tesselator dfl);
}
