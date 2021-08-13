package com.mojang.blaze3d.platform;

import java.util.stream.IntStream;
import java.util.function.Consumer;
import org.lwjgl.system.MemoryUtil;
import javax.annotation.Nullable;
import com.mojang.math.Vector4f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.EXTFramebufferBlit;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL30;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL15;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL11;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.FloatBuffer;

public class GlStateManager {
    private static final FloatBuffer MATRIX_BUFFER;
    private static final AlphaState ALPHA_TEST;
    private static final BooleanState LIGHTING;
    private static final BooleanState[] LIGHT_ENABLE;
    private static final ColorMaterialState COLOR_MATERIAL;
    private static final BlendState BLEND;
    private static final DepthState DEPTH;
    private static final FogState FOG;
    private static final CullState CULL;
    private static final PolygonOffsetState POLY_OFFSET;
    private static final ColorLogicState COLOR_LOGIC;
    private static final TexGenState TEX_GEN;
    private static final StencilState STENCIL;
    private static final FloatBuffer FLOAT_ARG_BUFFER;
    private static int activeTexture;
    private static final TextureState[] TEXTURES;
    private static int shadeModel;
    private static final BooleanState RESCALE_NORMAL;
    private static final ColorMask COLOR_MASK;
    private static final Color COLOR;
    private static FboMode fboMode;
    private static FboBlitMode fboBlitMode;
    
    @Deprecated
    public static void _pushLightingAttributes() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPushAttrib(8256);
    }
    
    @Deprecated
    public static void _pushTextureAttributes() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPushAttrib(270336);
    }
    
    @Deprecated
    public static void _popAttributes() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPopAttrib();
    }
    
    @Deprecated
    public static void _disableAlphaTest() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.ALPHA_TEST.mode.disable();
    }
    
    @Deprecated
    public static void _enableAlphaTest() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.ALPHA_TEST.mode.enable();
    }
    
    @Deprecated
    public static void _alphaFunc(final int integer, final float float2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (integer != GlStateManager.ALPHA_TEST.func || float2 != GlStateManager.ALPHA_TEST.reference) {
            GL11.glAlphaFunc(GlStateManager.ALPHA_TEST.func = integer, GlStateManager.ALPHA_TEST.reference = float2);
        }
    }
    
    @Deprecated
    public static void _enableLighting() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.LIGHTING.enable();
    }
    
    @Deprecated
    public static void _disableLighting() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.LIGHTING.disable();
    }
    
    @Deprecated
    public static void _enableLight(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.LIGHT_ENABLE[integer].enable();
    }
    
    @Deprecated
    public static void _enableColorMaterial() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.COLOR_MATERIAL.enable.enable();
    }
    
    @Deprecated
    public static void _disableColorMaterial() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.COLOR_MATERIAL.enable.disable();
    }
    
    @Deprecated
    public static void _colorMaterial(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer1 != GlStateManager.COLOR_MATERIAL.face || integer2 != GlStateManager.COLOR_MATERIAL.mode) {
            GL11.glColorMaterial(GlStateManager.COLOR_MATERIAL.face = integer1, GlStateManager.COLOR_MATERIAL.mode = integer2);
        }
    }
    
    @Deprecated
    public static void _light(final int integer1, final int integer2, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glLightfv(integer1, integer2, floatBuffer);
    }
    
    @Deprecated
    public static void _lightModel(final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glLightModelfv(integer, floatBuffer);
    }
    
    @Deprecated
    public static void _normal3f(final float float1, final float float2, final float float3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glNormal3f(float1, float2, float3);
    }
    
    public static void _disableDepthTest() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.DEPTH.mode.disable();
    }
    
    public static void _enableDepthTest() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.DEPTH.mode.enable();
    }
    
    public static void _depthFunc(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (integer != GlStateManager.DEPTH.func) {
            GL11.glDepthFunc(GlStateManager.DEPTH.func = integer);
        }
    }
    
    public static void _depthMask(final boolean boolean1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (boolean1 != GlStateManager.DEPTH.mask) {
            GL11.glDepthMask(GlStateManager.DEPTH.mask = boolean1);
        }
    }
    
    public static void _disableBlend() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.BLEND.mode.disable();
    }
    
    public static void _enableBlend() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.BLEND.mode.enable();
    }
    
    public static void _blendFunc(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer1 != GlStateManager.BLEND.srcRgb || integer2 != GlStateManager.BLEND.dstRgb) {
            GL11.glBlendFunc(GlStateManager.BLEND.srcRgb = integer1, GlStateManager.BLEND.dstRgb = integer2);
        }
    }
    
    public static void _blendFuncSeparate(final int integer1, final int integer2, final int integer3, final int integer4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer1 != GlStateManager.BLEND.srcRgb || integer2 != GlStateManager.BLEND.dstRgb || integer3 != GlStateManager.BLEND.srcAlpha || integer4 != GlStateManager.BLEND.dstAlpha) {
            glBlendFuncSeparate(GlStateManager.BLEND.srcRgb = integer1, GlStateManager.BLEND.dstRgb = integer2, GlStateManager.BLEND.srcAlpha = integer3, GlStateManager.BLEND.dstAlpha = integer4);
        }
    }
    
    public static void _blendColor(final float float1, final float float2, final float float3, final float float4) {
        GL14.glBlendColor(float1, float2, float3, float4);
    }
    
    public static void _blendEquation(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL14.glBlendEquation(integer);
    }
    
    public static String _init_fbo(final GLCapabilities gLCapabilities) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        if (gLCapabilities.OpenGL30) {
            GlStateManager.fboBlitMode = FboBlitMode.BASE;
        }
        else if (gLCapabilities.GL_EXT_framebuffer_blit) {
            GlStateManager.fboBlitMode = FboBlitMode.EXT;
        }
        else {
            GlStateManager.fboBlitMode = FboBlitMode.NONE;
        }
        if (gLCapabilities.OpenGL30) {
            GlStateManager.fboMode = FboMode.BASE;
            GlConst.GL_FRAMEBUFFER = 36160;
            GlConst.GL_RENDERBUFFER = 36161;
            GlConst.GL_COLOR_ATTACHMENT0 = 36064;
            GlConst.GL_DEPTH_ATTACHMENT = 36096;
            GlConst.GL_FRAMEBUFFER_COMPLETE = 36053;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 36059;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 36060;
            return "OpenGL 3.0";
        }
        if (gLCapabilities.GL_ARB_framebuffer_object) {
            GlStateManager.fboMode = FboMode.ARB;
            GlConst.GL_FRAMEBUFFER = 36160;
            GlConst.GL_RENDERBUFFER = 36161;
            GlConst.GL_COLOR_ATTACHMENT0 = 36064;
            GlConst.GL_DEPTH_ATTACHMENT = 36096;
            GlConst.GL_FRAMEBUFFER_COMPLETE = 36053;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 36059;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 36060;
            return "ARB_framebuffer_object extension";
        }
        if (gLCapabilities.GL_EXT_framebuffer_object) {
            GlStateManager.fboMode = FboMode.EXT;
            GlConst.GL_FRAMEBUFFER = 36160;
            GlConst.GL_RENDERBUFFER = 36161;
            GlConst.GL_COLOR_ATTACHMENT0 = 36064;
            GlConst.GL_DEPTH_ATTACHMENT = 36096;
            GlConst.GL_FRAMEBUFFER_COMPLETE = 36053;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 36055;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 36054;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 36059;
            GlConst.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 36060;
            return "EXT_framebuffer_object extension";
        }
        throw new IllegalStateException("Could not initialize framebuffer support.");
    }
    
    public static int glGetProgrami(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glGetProgrami(integer1, integer2);
    }
    
    public static void glAttachShader(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glAttachShader(integer1, integer2);
    }
    
    public static void glDeleteShader(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glDeleteShader(integer);
    }
    
    public static int glCreateShader(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glCreateShader(integer);
    }
    
    public static void glShaderSource(final int integer, final CharSequence charSequence) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glShaderSource(integer, charSequence);
    }
    
    public static void glCompileShader(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glCompileShader(integer);
    }
    
    public static int glGetShaderi(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glGetShaderi(integer1, integer2);
    }
    
    public static void _glUseProgram(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUseProgram(integer);
    }
    
    public static int glCreateProgram() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glCreateProgram();
    }
    
    public static void glDeleteProgram(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glDeleteProgram(integer);
    }
    
    public static void glLinkProgram(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glLinkProgram(integer);
    }
    
    public static int _glGetUniformLocation(final int integer, final CharSequence charSequence) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glGetUniformLocation(integer, charSequence);
    }
    
    public static void _glUniform1(final int integer, final IntBuffer intBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform1iv(integer, intBuffer);
    }
    
    public static void _glUniform1i(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform1i(integer1, integer2);
    }
    
    public static void _glUniform1(final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform1fv(integer, floatBuffer);
    }
    
    public static void _glUniform2(final int integer, final IntBuffer intBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform2iv(integer, intBuffer);
    }
    
    public static void _glUniform2(final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform2fv(integer, floatBuffer);
    }
    
    public static void _glUniform3(final int integer, final IntBuffer intBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform3iv(integer, intBuffer);
    }
    
    public static void _glUniform3(final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform3fv(integer, floatBuffer);
    }
    
    public static void _glUniform4(final int integer, final IntBuffer intBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform4iv(integer, intBuffer);
    }
    
    public static void _glUniform4(final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniform4fv(integer, floatBuffer);
    }
    
    public static void _glUniformMatrix2(final int integer, final boolean boolean2, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniformMatrix2fv(integer, boolean2, floatBuffer);
    }
    
    public static void _glUniformMatrix3(final int integer, final boolean boolean2, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniformMatrix3fv(integer, boolean2, floatBuffer);
    }
    
    public static void _glUniformMatrix4(final int integer, final boolean boolean2, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glUniformMatrix4fv(integer, boolean2, floatBuffer);
    }
    
    public static int _glGetAttribLocation(final int integer, final CharSequence charSequence) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glGetAttribLocation(integer, charSequence);
    }
    
    public static int _glGenBuffers() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        return GL15.glGenBuffers();
    }
    
    public static void _glBindBuffer(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL15.glBindBuffer(integer1, integer2);
    }
    
    public static void _glBufferData(final int integer1, final ByteBuffer byteBuffer, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL15.glBufferData(integer1, byteBuffer, integer3);
    }
    
    public static void _glDeleteBuffers(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL15.glDeleteBuffers(integer);
    }
    
    public static void _glCopyTexSubImage2D(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL20.glCopyTexSubImage2D(integer1, integer2, integer3, integer4, integer5, integer6, integer7, integer8);
    }
    
    public static void _glBindFramebuffer(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboMode) {
            case BASE: {
                GL30.glBindFramebuffer(integer1, integer2);
                break;
            }
            case ARB: {
                ARBFramebufferObject.glBindFramebuffer(integer1, integer2);
                break;
            }
            case EXT: {
                EXTFramebufferObject.glBindFramebufferEXT(integer1, integer2);
                break;
            }
        }
    }
    
    public static int getFramebufferDepthTexture() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboMode) {
            case BASE: {
                if (GL30.glGetFramebufferAttachmentParameteri(36160, 36096, 36048) == 5890) {
                    return GL30.glGetFramebufferAttachmentParameteri(36160, 36096, 36049);
                }
                break;
            }
            case ARB: {
                if (ARBFramebufferObject.glGetFramebufferAttachmentParameteri(36160, 36096, 36048) == 5890) {
                    return ARBFramebufferObject.glGetFramebufferAttachmentParameteri(36160, 36096, 36049);
                }
                break;
            }
            case EXT: {
                if (EXTFramebufferObject.glGetFramebufferAttachmentParameteriEXT(36160, 36096, 36048) == 5890) {
                    return EXTFramebufferObject.glGetFramebufferAttachmentParameteriEXT(36160, 36096, 36049);
                }
                break;
            }
        }
        return 0;
    }
    
    public static void _glBlitFrameBuffer(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9, final int integer10) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboBlitMode) {
            case BASE: {
                GL30.glBlitFramebuffer(integer1, integer2, integer3, integer4, integer5, integer6, integer7, integer8, integer9, integer10);
                break;
            }
            case EXT: {
                EXTFramebufferBlit.glBlitFramebufferEXT(integer1, integer2, integer3, integer4, integer5, integer6, integer7, integer8, integer9, integer10);
                break;
            }
        }
    }
    
    public static void _glDeleteFramebuffers(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboMode) {
            case BASE: {
                GL30.glDeleteFramebuffers(integer);
                break;
            }
            case ARB: {
                ARBFramebufferObject.glDeleteFramebuffers(integer);
                break;
            }
            case EXT: {
                EXTFramebufferObject.glDeleteFramebuffersEXT(integer);
                break;
            }
        }
    }
    
    public static int glGenFramebuffers() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboMode) {
            case BASE: {
                return GL30.glGenFramebuffers();
            }
            case ARB: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case EXT: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static int glCheckFramebufferStatus(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboMode) {
            case BASE: {
                return GL30.glCheckFramebufferStatus(integer);
            }
            case ARB: {
                return ARBFramebufferObject.glCheckFramebufferStatus(integer);
            }
            case EXT: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT(integer);
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void _glFramebufferTexture2D(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        switch (GlStateManager.fboMode) {
            case BASE: {
                GL30.glFramebufferTexture2D(integer1, integer2, integer3, integer4, integer5);
                break;
            }
            case ARB: {
                ARBFramebufferObject.glFramebufferTexture2D(integer1, integer2, integer3, integer4, integer5);
                break;
            }
            case EXT: {
                EXTFramebufferObject.glFramebufferTexture2DEXT(integer1, integer2, integer3, integer4, integer5);
                break;
            }
        }
    }
    
    @Deprecated
    public static int getActiveTextureName() {
        return GlStateManager.TEXTURES[GlStateManager.activeTexture].binding;
    }
    
    public static void glActiveTexture(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL13.glActiveTexture(integer);
    }
    
    @Deprecated
    public static void _glClientActiveTexture(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL13.glClientActiveTexture(integer);
    }
    
    @Deprecated
    public static void _glMultiTexCoord2f(final int integer, final float float2, final float float3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL13.glMultiTexCoord2f(integer, float2, float3);
    }
    
    public static void glBlendFuncSeparate(final int integer1, final int integer2, final int integer3, final int integer4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL14.glBlendFuncSeparate(integer1, integer2, integer3, integer4);
    }
    
    public static String glGetShaderInfoLog(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glGetShaderInfoLog(integer1, integer2);
    }
    
    public static String glGetProgramInfoLog(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL20.glGetProgramInfoLog(integer1, integer2);
    }
    
    public static void setupOutline() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        _texEnv(8960, 8704, 34160);
        color1arg(7681, 34168);
    }
    
    public static void teardownOutline() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        _texEnv(8960, 8704, 8448);
        color3arg(8448, 5890, 34168, 34166);
    }
    
    public static void setupOverlayColor(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        _activeTexture(33985);
        _enableTexture();
        _matrixMode(5890);
        _loadIdentity();
        final float float3 = 1.0f / (integer2 - 1);
        _scalef(float3, float3, float3);
        _matrixMode(5888);
        _bindTexture(integer1);
        _texParameter(3553, 10241, 9728);
        _texParameter(3553, 10240, 9728);
        _texParameter(3553, 10242, 10496);
        _texParameter(3553, 10243, 10496);
        _texEnv(8960, 8704, 34160);
        color3arg(34165, 34168, 5890, 5890);
        alpha1arg(7681, 34168);
        _activeTexture(33984);
    }
    
    public static void teardownOverlayColor() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        _activeTexture(33985);
        _disableTexture();
        _activeTexture(33984);
    }
    
    private static void color1arg(final int integer1, final int integer2) {
        _texEnv(8960, 34161, integer1);
        _texEnv(8960, 34176, integer2);
        _texEnv(8960, 34192, 768);
    }
    
    private static void color3arg(final int integer1, final int integer2, final int integer3, final int integer4) {
        _texEnv(8960, 34161, integer1);
        _texEnv(8960, 34176, integer2);
        _texEnv(8960, 34192, 768);
        _texEnv(8960, 34177, integer3);
        _texEnv(8960, 34193, 768);
        _texEnv(8960, 34178, integer4);
        _texEnv(8960, 34194, 770);
    }
    
    private static void alpha1arg(final int integer1, final int integer2) {
        _texEnv(8960, 34162, integer1);
        _texEnv(8960, 34184, integer2);
        _texEnv(8960, 34200, 770);
    }
    
    public static void setupLevelDiffuseLighting(final Vector3f g1, final Vector3f g2, final Matrix4f b) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        _pushMatrix();
        _loadIdentity();
        _enableLight(0);
        _enableLight(1);
        final Vector4f h4 = new Vector4f(g1);
        h4.transform(b);
        _light(16384, 4611, getBuffer(h4.x(), h4.y(), h4.z(), 0.0f));
        final float float5 = 0.6f;
        _light(16384, 4609, getBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        _light(16384, 4608, getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        _light(16384, 4610, getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        final Vector4f h5 = new Vector4f(g2);
        h5.transform(b);
        _light(16385, 4611, getBuffer(h5.x(), h5.y(), h5.z(), 0.0f));
        _light(16385, 4609, getBuffer(0.6f, 0.6f, 0.6f, 1.0f));
        _light(16385, 4608, getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        _light(16385, 4610, getBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        _shadeModel(7424);
        final float float6 = 0.4f;
        _lightModel(2899, getBuffer(0.4f, 0.4f, 0.4f, 1.0f));
        _popMatrix();
    }
    
    public static void setupGuiFlatDiffuseLighting(final Vector3f g1, final Vector3f g2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final Matrix4f b3 = new Matrix4f();
        b3.setIdentity();
        b3.multiply(Matrix4f.createScaleMatrix(1.0f, -1.0f, 1.0f));
        b3.multiply(Vector3f.YP.rotationDegrees(-22.5f));
        b3.multiply(Vector3f.XP.rotationDegrees(135.0f));
        setupLevelDiffuseLighting(g1, g2, b3);
    }
    
    public static void setupGui3DDiffuseLighting(final Vector3f g1, final Vector3f g2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final Matrix4f b3 = new Matrix4f();
        b3.setIdentity();
        b3.multiply(Vector3f.YP.rotationDegrees(62.0f));
        b3.multiply(Vector3f.XP.rotationDegrees(185.5f));
        b3.multiply(Matrix4f.createScaleMatrix(1.0f, -1.0f, 1.0f));
        b3.multiply(Vector3f.YP.rotationDegrees(-22.5f));
        b3.multiply(Vector3f.XP.rotationDegrees(135.0f));
        setupLevelDiffuseLighting(g1, g2, b3);
    }
    
    private static FloatBuffer getBuffer(final float float1, final float float2, final float float3, final float float4) {
        GlStateManager.FLOAT_ARG_BUFFER.clear();
        GlStateManager.FLOAT_ARG_BUFFER.put(float1).put(float2).put(float3).put(float4);
        GlStateManager.FLOAT_ARG_BUFFER.flip();
        return GlStateManager.FLOAT_ARG_BUFFER;
    }
    
    public static void setupEndPortalTexGen() {
        _texGenMode(TexGen.S, 9216);
        _texGenMode(TexGen.T, 9216);
        _texGenMode(TexGen.R, 9216);
        _texGenParam(TexGen.S, 9474, getBuffer(1.0f, 0.0f, 0.0f, 0.0f));
        _texGenParam(TexGen.T, 9474, getBuffer(0.0f, 1.0f, 0.0f, 0.0f));
        _texGenParam(TexGen.R, 9474, getBuffer(0.0f, 0.0f, 1.0f, 0.0f));
        _enableTexGen(TexGen.S);
        _enableTexGen(TexGen.T);
        _enableTexGen(TexGen.R);
    }
    
    public static void clearTexGen() {
        _disableTexGen(TexGen.S);
        _disableTexGen(TexGen.T);
        _disableTexGen(TexGen.R);
    }
    
    public static void mulTextureByProjModelView() {
        _getMatrix(2983, GlStateManager.MATRIX_BUFFER);
        _multMatrix(GlStateManager.MATRIX_BUFFER);
        _getMatrix(2982, GlStateManager.MATRIX_BUFFER);
        _multMatrix(GlStateManager.MATRIX_BUFFER);
    }
    
    @Deprecated
    public static void _enableFog() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.FOG.enable.enable();
    }
    
    @Deprecated
    public static void _disableFog() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.FOG.enable.disable();
    }
    
    @Deprecated
    public static void _fogMode(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer != GlStateManager.FOG.mode) {
            _fogi(2917, GlStateManager.FOG.mode = integer);
        }
    }
    
    @Deprecated
    public static void _fogDensity(final float float1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (float1 != GlStateManager.FOG.density) {
            GL11.glFogf(2914, GlStateManager.FOG.density = float1);
        }
    }
    
    @Deprecated
    public static void _fogStart(final float float1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (float1 != GlStateManager.FOG.start) {
            GL11.glFogf(2915, GlStateManager.FOG.start = float1);
        }
    }
    
    @Deprecated
    public static void _fogEnd(final float float1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (float1 != GlStateManager.FOG.end) {
            GL11.glFogf(2916, GlStateManager.FOG.end = float1);
        }
    }
    
    @Deprecated
    public static void _fog(final int integer, final float[] arr) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glFogfv(integer, arr);
    }
    
    @Deprecated
    public static void _fogi(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glFogi(integer1, integer2);
    }
    
    public static void _enableCull() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.CULL.enable.enable();
    }
    
    public static void _disableCull() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.CULL.enable.disable();
    }
    
    public static void _polygonMode(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPolygonMode(integer1, integer2);
    }
    
    public static void _enablePolygonOffset() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.POLY_OFFSET.fill.enable();
    }
    
    public static void _disablePolygonOffset() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.POLY_OFFSET.fill.disable();
    }
    
    public static void _enableLineOffset() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.POLY_OFFSET.line.enable();
    }
    
    public static void _disableLineOffset() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.POLY_OFFSET.line.disable();
    }
    
    public static void _polygonOffset(final float float1, final float float2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (float1 != GlStateManager.POLY_OFFSET.factor || float2 != GlStateManager.POLY_OFFSET.units) {
            GL11.glPolygonOffset(GlStateManager.POLY_OFFSET.factor = float1, GlStateManager.POLY_OFFSET.units = float2);
        }
    }
    
    public static void _enableColorLogicOp() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.COLOR_LOGIC.enable.enable();
    }
    
    public static void _disableColorLogicOp() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.COLOR_LOGIC.enable.disable();
    }
    
    public static void _logicOp(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer != GlStateManager.COLOR_LOGIC.op) {
            GL11.glLogicOp(GlStateManager.COLOR_LOGIC.op = integer);
        }
    }
    
    @Deprecated
    public static void _enableTexGen(final TexGen t) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        getTexGen(t).enable.enable();
    }
    
    @Deprecated
    public static void _disableTexGen(final TexGen t) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        getTexGen(t).enable.disable();
    }
    
    @Deprecated
    public static void _texGenMode(final TexGen t, final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final TexGenCoord u3 = getTexGen(t);
        if (integer != u3.mode) {
            u3.mode = integer;
            GL11.glTexGeni(u3.coord, 9472, integer);
        }
    }
    
    @Deprecated
    public static void _texGenParam(final TexGen t, final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glTexGenfv(getTexGen(t).coord, integer, floatBuffer);
    }
    
    @Deprecated
    private static TexGenCoord getTexGen(final TexGen t) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        switch (t) {
            case S: {
                return GlStateManager.TEX_GEN.s;
            }
            case T: {
                return GlStateManager.TEX_GEN.t;
            }
            case R: {
                return GlStateManager.TEX_GEN.r;
            }
            case Q: {
                return GlStateManager.TEX_GEN.q;
            }
            default: {
                return GlStateManager.TEX_GEN.s;
            }
        }
    }
    
    public static void _activeTexture(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (GlStateManager.activeTexture != integer - 33984) {
            GlStateManager.activeTexture = integer - 33984;
            glActiveTexture(integer);
        }
    }
    
    public static void _enableTexture() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.TEXTURES[GlStateManager.activeTexture].enable.enable();
    }
    
    public static void _disableTexture() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.TEXTURES[GlStateManager.activeTexture].enable.disable();
    }
    
    @Deprecated
    public static void _texEnv(final int integer1, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glTexEnvi(integer1, integer2, integer3);
    }
    
    public static void _texParameter(final int integer1, final int integer2, final float float3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glTexParameterf(integer1, integer2, float3);
    }
    
    public static void _texParameter(final int integer1, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glTexParameteri(integer1, integer2, integer3);
    }
    
    public static int _getTexLevelParameter(final int integer1, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return GL11.glGetTexLevelParameteri(integer1, integer2, integer3);
    }
    
    public static int _genTexture() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        return GL11.glGenTextures();
    }
    
    public static void _genTextures(final int[] arr) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glGenTextures(arr);
    }
    
    public static void _deleteTexture(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glDeleteTextures(integer);
        for (final TextureState w5 : GlStateManager.TEXTURES) {
            if (w5.binding == integer) {
                w5.binding = -1;
            }
        }
    }
    
    public static void _deleteTextures(final int[] arr) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        for (final TextureState w5 : GlStateManager.TEXTURES) {
            for (final int integer9 : arr) {
                if (w5.binding == integer9) {
                    w5.binding = -1;
                }
            }
        }
        GL11.glDeleteTextures(arr);
    }
    
    public static void _bindTexture(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (integer != GlStateManager.TEXTURES[GlStateManager.activeTexture].binding) {
            GL11.glBindTexture(3553, GlStateManager.TEXTURES[GlStateManager.activeTexture].binding = integer);
        }
    }
    
    public static void _texImage2D(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, @Nullable final IntBuffer intBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glTexImage2D(integer1, integer2, integer3, integer4, integer5, integer6, integer7, integer8, intBuffer);
    }
    
    public static void _texSubImage2D(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final long long9) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glTexSubImage2D(integer1, integer2, integer3, integer4, integer5, integer6, integer7, integer8, long9);
    }
    
    public static void _getTexImage(final int integer1, final int integer2, final int integer3, final int integer4, final long long5) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glGetTexImage(integer1, integer2, integer3, integer4, long5);
    }
    
    @Deprecated
    public static void _shadeModel(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (integer != GlStateManager.shadeModel) {
            GL11.glShadeModel(GlStateManager.shadeModel = integer);
        }
    }
    
    @Deprecated
    public static void _enableRescaleNormal() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.RESCALE_NORMAL.enable();
    }
    
    @Deprecated
    public static void _disableRescaleNormal() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.RESCALE_NORMAL.disable();
    }
    
    public static void _viewport(final int integer1, final int integer2, final int integer3, final int integer4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glViewport(Viewport.INSTANCE.x = integer1, Viewport.INSTANCE.y = integer2, Viewport.INSTANCE.width = integer3, Viewport.INSTANCE.height = integer4);
    }
    
    public static void _colorMask(final boolean boolean1, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (boolean1 != GlStateManager.COLOR_MASK.red || boolean2 != GlStateManager.COLOR_MASK.green || boolean3 != GlStateManager.COLOR_MASK.blue || boolean4 != GlStateManager.COLOR_MASK.alpha) {
            GL11.glColorMask(GlStateManager.COLOR_MASK.red = boolean1, GlStateManager.COLOR_MASK.green = boolean2, GlStateManager.COLOR_MASK.blue = boolean3, GlStateManager.COLOR_MASK.alpha = boolean4);
        }
    }
    
    public static void _stencilFunc(final int integer1, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer1 != GlStateManager.STENCIL.func.func || integer1 != GlStateManager.STENCIL.func.ref || integer1 != GlStateManager.STENCIL.func.mask) {
            GL11.glStencilFunc(GlStateManager.STENCIL.func.func = integer1, GlStateManager.STENCIL.func.ref = integer2, GlStateManager.STENCIL.func.mask = integer3);
        }
    }
    
    public static void _stencilMask(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer != GlStateManager.STENCIL.mask) {
            GL11.glStencilMask(GlStateManager.STENCIL.mask = integer);
        }
    }
    
    public static void _stencilOp(final int integer1, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer1 != GlStateManager.STENCIL.fail || integer2 != GlStateManager.STENCIL.zfail || integer3 != GlStateManager.STENCIL.zpass) {
            GL11.glStencilOp(GlStateManager.STENCIL.fail = integer1, GlStateManager.STENCIL.zfail = integer2, GlStateManager.STENCIL.zpass = integer3);
        }
    }
    
    public static void _clearDepth(final double double1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glClearDepth(double1);
    }
    
    public static void _clearColor(final float float1, final float float2, final float float3, final float float4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glClearColor(float1, float2, float3, float4);
    }
    
    public static void _clearStencil(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glClearStencil(integer);
    }
    
    public static void _clear(final int integer, final boolean boolean2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glClear(integer);
        if (boolean2) {
            _getError();
        }
    }
    
    @Deprecated
    public static void _matrixMode(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glMatrixMode(integer);
    }
    
    @Deprecated
    public static void _loadIdentity() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glLoadIdentity();
    }
    
    @Deprecated
    public static void _pushMatrix() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPushMatrix();
    }
    
    @Deprecated
    public static void _popMatrix() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPopMatrix();
    }
    
    @Deprecated
    public static void _getMatrix(final int integer, final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glGetFloatv(integer, floatBuffer);
    }
    
    @Deprecated
    public static void _ortho(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glOrtho(double1, double2, double3, double4, double5, double6);
    }
    
    @Deprecated
    public static void _rotatef(final float float1, final float float2, final float float3, final float float4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glRotatef(float1, float2, float3, float4);
    }
    
    @Deprecated
    public static void _scalef(final float float1, final float float2, final float float3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glScalef(float1, float2, float3);
    }
    
    @Deprecated
    public static void _scaled(final double double1, final double double2, final double double3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glScaled(double1, double2, double3);
    }
    
    @Deprecated
    public static void _translatef(final float float1, final float float2, final float float3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glTranslatef(float1, float2, float3);
    }
    
    @Deprecated
    public static void _translated(final double double1, final double double2, final double double3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glTranslated(double1, double2, double3);
    }
    
    @Deprecated
    public static void _multMatrix(final FloatBuffer floatBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glMultMatrixf(floatBuffer);
    }
    
    @Deprecated
    public static void _multMatrix(final Matrix4f b) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        b.store(GlStateManager.MATRIX_BUFFER);
        GlStateManager.MATRIX_BUFFER.rewind();
        _multMatrix(GlStateManager.MATRIX_BUFFER);
    }
    
    @Deprecated
    public static void _color4f(final float float1, final float float2, final float float3, final float float4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (float1 != GlStateManager.COLOR.r || float2 != GlStateManager.COLOR.g || float3 != GlStateManager.COLOR.b || float4 != GlStateManager.COLOR.a) {
            GL11.glColor4f(GlStateManager.COLOR.r = float1, GlStateManager.COLOR.g = float2, GlStateManager.COLOR.b = float3, GlStateManager.COLOR.a = float4);
        }
    }
    
    @Deprecated
    public static void _clearCurrentColor() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager.COLOR.r = -1.0f;
        GlStateManager.COLOR.g = -1.0f;
        GlStateManager.COLOR.b = -1.0f;
        GlStateManager.COLOR.a = -1.0f;
    }
    
    @Deprecated
    public static void _normalPointer(final int integer1, final int integer2, final long long3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glNormalPointer(integer1, integer2, long3);
    }
    
    @Deprecated
    public static void _texCoordPointer(final int integer1, final int integer2, final int integer3, final long long4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glTexCoordPointer(integer1, integer2, integer3, long4);
    }
    
    @Deprecated
    public static void _vertexPointer(final int integer1, final int integer2, final int integer3, final long long4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glVertexPointer(integer1, integer2, integer3, long4);
    }
    
    @Deprecated
    public static void _colorPointer(final int integer1, final int integer2, final int integer3, final long long4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glColorPointer(integer1, integer2, integer3, long4);
    }
    
    public static void _vertexAttribPointer(final int integer1, final int integer2, final int integer3, final boolean boolean4, final int integer5, final long long6) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glVertexAttribPointer(integer1, integer2, integer3, boolean4, integer5, long6);
    }
    
    @Deprecated
    public static void _enableClientState(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glEnableClientState(integer);
    }
    
    @Deprecated
    public static void _disableClientState(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glDisableClientState(integer);
    }
    
    public static void _enableVertexAttribArray(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glEnableVertexAttribArray(integer);
    }
    
    public static void _disableVertexAttribArray(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL20.glEnableVertexAttribArray(integer);
    }
    
    public static void _drawArrays(final int integer1, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glDrawArrays(integer1, integer2, integer3);
    }
    
    public static void _lineWidth(final float float1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glLineWidth(float1);
    }
    
    public static void _pixelStore(final int integer1, final int integer2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GL11.glPixelStorei(integer1, integer2);
    }
    
    public static void _pixelTransfer(final int integer, final float float2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPixelTransferf(integer, float2);
    }
    
    public static void _readPixels(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final ByteBuffer byteBuffer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glReadPixels(integer1, integer2, integer3, integer4, integer5, integer6, byteBuffer);
    }
    
    public static int _getError() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL11.glGetError();
    }
    
    public static String _getString(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return GL11.glGetString(integer);
    }
    
    public static int _getInteger(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        return GL11.glGetInteger(integer);
    }
    
    public static boolean supportsFramebufferBlit() {
        return GlStateManager.fboBlitMode != FboBlitMode.NONE;
    }
    
    static {
        MATRIX_BUFFER = GLX.<FloatBuffer>make(MemoryUtil.memAllocFloat(16), (java.util.function.Consumer<FloatBuffer>)(floatBuffer -> DebugMemoryUntracker.untrack(MemoryUtil.memAddress(floatBuffer))));
        ALPHA_TEST = new AlphaState();
        LIGHTING = new BooleanState(2896);
        LIGHT_ENABLE = (BooleanState[])IntStream.range(0, 8).mapToObj(integer -> new BooleanState(16384 + integer)).toArray(BooleanState[]::new);
        COLOR_MATERIAL = new ColorMaterialState();
        BLEND = new BlendState();
        DEPTH = new DepthState();
        FOG = new FogState();
        CULL = new CullState();
        POLY_OFFSET = new PolygonOffsetState();
        COLOR_LOGIC = new ColorLogicState();
        TEX_GEN = new TexGenState();
        STENCIL = new StencilState();
        FLOAT_ARG_BUFFER = MemoryTracker.createFloatBuffer(4);
        TEXTURES = (TextureState[])IntStream.range(0, 12).mapToObj(integer -> new TextureState()).toArray(TextureState[]::new);
        GlStateManager.shadeModel = 7425;
        RESCALE_NORMAL = new BooleanState(32826);
        COLOR_MASK = new ColorMask();
        COLOR = new Color();
    }
    
    @Deprecated
    public enum FogMode {
        LINEAR(9729), 
        EXP(2048), 
        EXP2(2049);
        
        public final int value;
        
        private FogMode(final int integer3) {
            this.value = integer3;
        }
    }
    
    public enum LogicOp {
        AND(5377), 
        AND_INVERTED(5380), 
        AND_REVERSE(5378), 
        CLEAR(5376), 
        COPY(5379), 
        COPY_INVERTED(5388), 
        EQUIV(5385), 
        INVERT(5386), 
        NAND(5390), 
        NOOP(5381), 
        NOR(5384), 
        OR(5383), 
        OR_INVERTED(5389), 
        OR_REVERSE(5387), 
        SET(5391), 
        XOR(5382);
        
        public final int value;
        
        private LogicOp(final int integer3) {
            this.value = integer3;
        }
    }
    
    public enum Viewport {
        INSTANCE;
        
        protected int x;
        protected int y;
        protected int width;
        protected int height;
    }
    
    public enum FboMode {
        BASE, 
        ARB, 
        EXT;
    }
    
    public enum FboBlitMode {
        BASE, 
        EXT, 
        NONE;
    }
    
    static class TextureState {
        public final BooleanState enable;
        public int binding;
        
        private TextureState() {
            this.enable = new BooleanState(3553);
        }
    }
    
    @Deprecated
    static class AlphaState {
        public final BooleanState mode;
        public int func;
        public float reference;
        
        private AlphaState() {
            this.mode = new BooleanState(3008);
            this.func = 519;
            this.reference = -1.0f;
        }
    }
    
    @Deprecated
    static class ColorMaterialState {
        public final BooleanState enable;
        public int face;
        public int mode;
        
        private ColorMaterialState() {
            this.enable = new BooleanState(2903);
            this.face = 1032;
            this.mode = 5634;
        }
    }
    
    static class BlendState {
        public final BooleanState mode;
        public int srcRgb;
        public int dstRgb;
        public int srcAlpha;
        public int dstAlpha;
        
        private BlendState() {
            this.mode = new BooleanState(3042);
            this.srcRgb = 1;
            this.dstRgb = 0;
            this.srcAlpha = 1;
            this.dstAlpha = 0;
        }
    }
    
    static class DepthState {
        public final BooleanState mode;
        public boolean mask;
        public int func;
        
        private DepthState() {
            this.mode = new BooleanState(2929);
            this.mask = true;
            this.func = 513;
        }
    }
    
    @Deprecated
    static class FogState {
        public final BooleanState enable;
        public int mode;
        public float density;
        public float start;
        public float end;
        
        private FogState() {
            this.enable = new BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0f;
            this.end = 1.0f;
        }
    }
    
    static class CullState {
        public final BooleanState enable;
        public int mode;
        
        private CullState() {
            this.enable = new BooleanState(2884);
            this.mode = 1029;
        }
    }
    
    static class PolygonOffsetState {
        public final BooleanState fill;
        public final BooleanState line;
        public float factor;
        public float units;
        
        private PolygonOffsetState() {
            this.fill = new BooleanState(32823);
            this.line = new BooleanState(10754);
        }
    }
    
    static class ColorLogicState {
        public final BooleanState enable;
        public int op;
        
        private ColorLogicState() {
            this.enable = new BooleanState(3058);
            this.op = 5379;
        }
    }
    
    static class StencilFunc {
        public int func;
        public int ref;
        public int mask;
        
        private StencilFunc() {
            this.func = 519;
            this.mask = -1;
        }
    }
    
    static class StencilState {
        public final StencilFunc func;
        public int mask;
        public int fail;
        public int zfail;
        public int zpass;
        
        private StencilState() {
            this.func = new StencilFunc();
            this.mask = -1;
            this.fail = 7680;
            this.zfail = 7680;
            this.zpass = 7680;
        }
    }
    
    @Deprecated
    static class TexGenState {
        public final TexGenCoord s;
        public final TexGenCoord t;
        public final TexGenCoord r;
        public final TexGenCoord q;
        
        private TexGenState() {
            this.s = new TexGenCoord(8192, 3168);
            this.t = new TexGenCoord(8193, 3169);
            this.r = new TexGenCoord(8194, 3170);
            this.q = new TexGenCoord(8195, 3171);
        }
    }
    
    @Deprecated
    static class TexGenCoord {
        public final BooleanState enable;
        public final int coord;
        public int mode;
        
        public TexGenCoord(final int integer1, final int integer2) {
            this.mode = -1;
            this.coord = integer1;
            this.enable = new BooleanState(integer2);
        }
    }
    
    @Deprecated
    public enum TexGen {
        S, 
        T, 
        R, 
        Q;
    }
    
    static class ColorMask {
        public boolean red;
        public boolean green;
        public boolean blue;
        public boolean alpha;
        
        private ColorMask() {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }
    }
    
    @Deprecated
    static class Color {
        public float r;
        public float g;
        public float b;
        public float a;
        
        public Color() {
            this(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        public Color(final float float1, final float float2, final float float3, final float float4) {
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 1.0f;
            this.a = 1.0f;
            this.r = float1;
            this.g = float2;
            this.b = float3;
            this.a = float4;
        }
    }
    
    static class BooleanState {
        private final int state;
        private boolean enabled;
        
        public BooleanState(final int integer) {
            this.state = integer;
        }
        
        public void disable() {
            this.setEnabled(false);
        }
        
        public void enable() {
            this.setEnabled(true);
        }
        
        public void setEnabled(final boolean boolean1) {
            RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
            if (boolean1 != this.enabled) {
                this.enabled = boolean1;
                if (boolean1) {
                    GL11.glEnable(this.state);
                }
                else {
                    GL11.glDisable(this.state);
                }
            }
        }
    }
    
    public enum SourceFactor {
        CONSTANT_ALPHA(32771), 
        CONSTANT_COLOR(32769), 
        DST_ALPHA(772), 
        DST_COLOR(774), 
        ONE(1), 
        ONE_MINUS_CONSTANT_ALPHA(32772), 
        ONE_MINUS_CONSTANT_COLOR(32770), 
        ONE_MINUS_DST_ALPHA(773), 
        ONE_MINUS_DST_COLOR(775), 
        ONE_MINUS_SRC_ALPHA(771), 
        ONE_MINUS_SRC_COLOR(769), 
        SRC_ALPHA(770), 
        SRC_ALPHA_SATURATE(776), 
        SRC_COLOR(768), 
        ZERO(0);
        
        public final int value;
        
        private SourceFactor(final int integer3) {
            this.value = integer3;
        }
    }
    
    public enum DestFactor {
        CONSTANT_ALPHA(32771), 
        CONSTANT_COLOR(32769), 
        DST_ALPHA(772), 
        DST_COLOR(774), 
        ONE(1), 
        ONE_MINUS_CONSTANT_ALPHA(32772), 
        ONE_MINUS_CONSTANT_COLOR(32770), 
        ONE_MINUS_DST_ALPHA(773), 
        ONE_MINUS_DST_COLOR(775), 
        ONE_MINUS_SRC_ALPHA(771), 
        ONE_MINUS_SRC_COLOR(769), 
        SRC_ALPHA(770), 
        SRC_COLOR(768), 
        ZERO(0);
        
        public final int value;
        
        private DestFactor(final int integer3) {
            this.value = integer3;
        }
    }
}
