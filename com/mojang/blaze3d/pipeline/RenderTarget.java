package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.nio.IntBuffer;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;

public class RenderTarget {
    public int width;
    public int height;
    public int viewWidth;
    public int viewHeight;
    public final boolean useDepth;
    public int frameBufferId;
    private int colorTextureId;
    private int depthBufferId;
    public final float[] clearChannels;
    public int filterMode;
    
    public RenderTarget(final int integer1, final int integer2, final boolean boolean3, final boolean boolean4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.useDepth = boolean3;
        this.frameBufferId = -1;
        this.colorTextureId = -1;
        this.depthBufferId = -1;
        (this.clearChannels = new float[4])[0] = 1.0f;
        this.clearChannels[1] = 1.0f;
        this.clearChannels[2] = 1.0f;
        this.clearChannels[3] = 0.0f;
        this.resize(integer1, integer2, boolean4);
    }
    
    public void resize(final int integer1, final int integer2, final boolean boolean3) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this._resize(integer1, integer2, boolean3));
        }
        else {
            this._resize(integer1, integer2, boolean3);
        }
    }
    
    private void _resize(final int integer1, final int integer2, final boolean boolean3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager._enableDepthTest();
        if (this.frameBufferId >= 0) {
            this.destroyBuffers();
        }
        this.createBuffers(integer1, integer2, boolean3);
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
    }
    
    public void destroyBuffers() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.unbindRead();
        this.unbindWrite();
        if (this.depthBufferId > -1) {
            TextureUtil.releaseTextureId(this.depthBufferId);
            this.depthBufferId = -1;
        }
        if (this.colorTextureId > -1) {
            TextureUtil.releaseTextureId(this.colorTextureId);
            this.colorTextureId = -1;
        }
        if (this.frameBufferId > -1) {
            GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
            GlStateManager._glDeleteFramebuffers(this.frameBufferId);
            this.frameBufferId = -1;
        }
    }
    
    public void copyDepthFrom(final RenderTarget ded) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (GlStateManager.supportsFramebufferBlit()) {
            GlStateManager._glBindFramebuffer(36008, ded.frameBufferId);
            GlStateManager._glBindFramebuffer(36009, this.frameBufferId);
            GlStateManager._glBlitFrameBuffer(0, 0, ded.width, ded.height, 0, 0, this.width, this.height, 256, 9728);
        }
        else {
            GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, this.frameBufferId);
            final int integer3 = GlStateManager.getFramebufferDepthTexture();
            if (integer3 != 0) {
                final int integer4 = GlStateManager.getActiveTextureName();
                GlStateManager._bindTexture(integer3);
                GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, ded.frameBufferId);
                GlStateManager._glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Math.min(this.width, ded.width), Math.min(this.height, ded.height));
                GlStateManager._bindTexture(integer4);
            }
        }
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
    }
    
    public void createBuffers(final int integer1, final int integer2, final boolean boolean3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.viewWidth = integer1;
        this.viewHeight = integer2;
        this.width = integer1;
        this.height = integer2;
        this.frameBufferId = GlStateManager.glGenFramebuffers();
        this.colorTextureId = TextureUtil.generateTextureId();
        if (this.useDepth) {
            GlStateManager._bindTexture(this.depthBufferId = TextureUtil.generateTextureId());
            GlStateManager._texParameter(3553, 10241, 9728);
            GlStateManager._texParameter(3553, 10240, 9728);
            GlStateManager._texParameter(3553, 10242, 10496);
            GlStateManager._texParameter(3553, 10243, 10496);
            GlStateManager._texParameter(3553, 34892, 0);
            GlStateManager._texImage2D(3553, 0, 6402, this.width, this.height, 0, 6402, 5126, null);
        }
        this.setFilterMode(9728);
        GlStateManager._bindTexture(this.colorTextureId);
        GlStateManager._texImage2D(3553, 0, 32856, this.width, this.height, 0, 6408, 5121, null);
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, this.frameBufferId);
        GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GlConst.GL_COLOR_ATTACHMENT0, 3553, this.colorTextureId, 0);
        if (this.useDepth) {
            GlStateManager._glFramebufferTexture2D(GlConst.GL_FRAMEBUFFER, GlConst.GL_DEPTH_ATTACHMENT, 3553, this.depthBufferId, 0);
        }
        this.checkStatus();
        this.clear(boolean3);
        this.unbindRead();
    }
    
    public void setFilterMode(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.filterMode = integer;
        GlStateManager._bindTexture(this.colorTextureId);
        GlStateManager._texParameter(3553, 10241, integer);
        GlStateManager._texParameter(3553, 10240, integer);
        GlStateManager._texParameter(3553, 10242, 10496);
        GlStateManager._texParameter(3553, 10243, 10496);
        GlStateManager._bindTexture(0);
    }
    
    public void checkStatus() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        final int integer2 = GlStateManager.glCheckFramebufferStatus(GlConst.GL_FRAMEBUFFER);
        if (integer2 == GlConst.GL_FRAMEBUFFER_COMPLETE) {
            return;
        }
        if (integer2 == GlConst.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
        }
        if (integer2 == GlConst.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT");
        }
        if (integer2 == GlConst.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER");
        }
        if (integer2 == GlConst.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER) {
            throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER");
        }
        throw new RuntimeException(new StringBuilder().append("glCheckFramebufferStatus returned unknown status:").append(integer2).toString());
    }
    
    public void bindRead() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager._bindTexture(this.colorTextureId);
    }
    
    public void unbindRead() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager._bindTexture(0);
    }
    
    public void bindWrite(final boolean boolean1) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this._bindWrite(boolean1));
        }
        else {
            this._bindWrite(boolean1);
        }
    }
    
    private void _bindWrite(final boolean boolean1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, this.frameBufferId);
        if (boolean1) {
            GlStateManager._viewport(0, 0, this.viewWidth, this.viewHeight);
        }
    }
    
    public void unbindWrite() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0));
        }
        else {
            GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
        }
    }
    
    public void setClearColor(final float float1, final float float2, final float float3, final float float4) {
        this.clearChannels[0] = float1;
        this.clearChannels[1] = float2;
        this.clearChannels[2] = float3;
        this.clearChannels[3] = float4;
    }
    
    public void blitToScreen(final int integer1, final int integer2) {
        this.blitToScreen(integer1, integer2, true);
    }
    
    public void blitToScreen(final int integer1, final int integer2, final boolean boolean3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        if (!RenderSystem.isInInitPhase()) {
            RenderSystem.recordRenderCall(() -> this._blitToScreen(integer1, integer2, boolean3));
        }
        else {
            this._blitToScreen(integer1, integer2, boolean3);
        }
    }
    
    private void _blitToScreen(final int integer1, final int integer2, final boolean boolean3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager._colorMask(true, true, true, false);
        GlStateManager._disableDepthTest();
        GlStateManager._depthMask(false);
        GlStateManager._matrixMode(5889);
        GlStateManager._loadIdentity();
        GlStateManager._ortho(0.0, integer1, integer2, 0.0, 1000.0, 3000.0);
        GlStateManager._matrixMode(5888);
        GlStateManager._loadIdentity();
        GlStateManager._translatef(0.0f, 0.0f, -2000.0f);
        GlStateManager._viewport(0, 0, integer1, integer2);
        GlStateManager._enableTexture();
        GlStateManager._disableLighting();
        GlStateManager._disableAlphaTest();
        if (boolean3) {
            GlStateManager._disableBlend();
            GlStateManager._enableColorMaterial();
        }
        GlStateManager._color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.bindRead();
        final float float5 = (float)integer1;
        final float float6 = (float)integer2;
        final float float7 = this.viewWidth / (float)this.width;
        final float float8 = this.viewHeight / (float)this.height;
        final Tesselator dfl9 = RenderSystem.renderThreadTesselator();
        final BufferBuilder dfe10 = dfl9.getBuilder();
        dfe10.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe10.vertex(0.0, float6, 0.0).uv(0.0f, 0.0f).color(255, 255, 255, 255).endVertex();
        dfe10.vertex(float5, float6, 0.0).uv(float7, 0.0f).color(255, 255, 255, 255).endVertex();
        dfe10.vertex(float5, 0.0, 0.0).uv(float7, float8).color(255, 255, 255, 255).endVertex();
        dfe10.vertex(0.0, 0.0, 0.0).uv(0.0f, float8).color(255, 255, 255, 255).endVertex();
        dfl9.end();
        this.unbindRead();
        GlStateManager._depthMask(true);
        GlStateManager._colorMask(true, true, true, true);
    }
    
    public void clear(final boolean boolean1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        this.bindWrite(true);
        GlStateManager._clearColor(this.clearChannels[0], this.clearChannels[1], this.clearChannels[2], this.clearChannels[3]);
        int integer3 = 16384;
        if (this.useDepth) {
            GlStateManager._clearDepth(1.0);
            integer3 |= 0x100;
        }
        GlStateManager._clear(integer3, boolean1);
        this.unbindWrite();
    }
    
    public int getColorTextureId() {
        return this.colorTextureId;
    }
    
    public int getDepthTextureId() {
        return this.depthBufferId;
    }
}
