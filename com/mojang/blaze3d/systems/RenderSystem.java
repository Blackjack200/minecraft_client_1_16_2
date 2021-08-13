package com.mojang.blaze3d.systems;

import com.google.common.collect.Queues;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.Options;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import com.mojang.math.Vector3f;
import java.util.function.IntSupplier;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import java.util.function.LongSupplier;
import com.mojang.blaze3d.platform.GLX;
import java.util.function.Consumer;
import java.nio.ByteBuffer;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.glfw.GLFW;
import java.util.function.Supplier;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.pipeline.RenderCall;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.Logger;

public class RenderSystem {
    private static final Logger LOGGER;
    private static final ConcurrentLinkedQueue<RenderCall> recordingQueue;
    private static final Tesselator RENDER_THREAD_TESSELATOR;
    public static final float DEFAULTALPHACUTOFF = 0.1f;
    private static final int MINIMUM_ATLAS_TEXTURE_SIZE = 1024;
    private static boolean isReplayingQueue;
    private static Thread gameThread;
    private static Thread renderThread;
    private static int MAX_SUPPORTED_TEXTURE_SIZE;
    private static boolean isInInit;
    private static double lastDrawTime;
    
    public static void initRenderThread() {
        if (RenderSystem.renderThread != null || RenderSystem.gameThread == Thread.currentThread()) {
            throw new IllegalStateException("Could not initialize render thread");
        }
        RenderSystem.renderThread = Thread.currentThread();
    }
    
    public static boolean isOnRenderThread() {
        return Thread.currentThread() == RenderSystem.renderThread;
    }
    
    public static boolean isOnRenderThreadOrInit() {
        return RenderSystem.isInInit || isOnRenderThread();
    }
    
    public static void initGameThread(final boolean boolean1) {
        final boolean boolean2 = RenderSystem.renderThread == Thread.currentThread();
        if (RenderSystem.gameThread != null || RenderSystem.renderThread == null || boolean2 == boolean1) {
            throw new IllegalStateException("Could not initialize tick thread");
        }
        RenderSystem.gameThread = Thread.currentThread();
    }
    
    public static boolean isOnGameThread() {
        return true;
    }
    
    public static boolean isOnGameThreadOrInit() {
        return RenderSystem.isInInit || isOnGameThread();
    }
    
    public static void assertThread(final Supplier<Boolean> supplier) {
        if (!(boolean)supplier.get()) {
            throw new IllegalStateException("Rendersystem called from wrong thread");
        }
    }
    
    public static boolean isInInitPhase() {
        return true;
    }
    
    public static void recordRenderCall(final RenderCall deb) {
        RenderSystem.recordingQueue.add(deb);
    }
    
    public static void flipFrame(final long long1) {
        GLFW.glfwPollEvents();
        replayQueue();
        Tesselator.getInstance().getBuilder().clear();
        GLFW.glfwSwapBuffers(long1);
        GLFW.glfwPollEvents();
    }
    
    public static void replayQueue() {
        RenderSystem.isReplayingQueue = true;
        while (!RenderSystem.recordingQueue.isEmpty()) {
            final RenderCall deb1 = (RenderCall)RenderSystem.recordingQueue.poll();
            deb1.execute();
        }
        RenderSystem.isReplayingQueue = false;
    }
    
    public static void limitDisplayFPS(final int integer) {
        double double2;
        double double3;
        for (double2 = RenderSystem.lastDrawTime + 1.0 / integer, double3 = GLFW.glfwGetTime(); double3 < double2; double3 = GLFW.glfwGetTime()) {
            GLFW.glfwWaitEventsTimeout(double2 - double3);
        }
        RenderSystem.lastDrawTime = double3;
    }
    
    @Deprecated
    public static void pushLightingAttributes() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._pushLightingAttributes();
    }
    
    @Deprecated
    public static void pushTextureAttributes() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._pushTextureAttributes();
    }
    
    @Deprecated
    public static void popAttributes() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._popAttributes();
    }
    
    @Deprecated
    public static void disableAlphaTest() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableAlphaTest();
    }
    
    @Deprecated
    public static void enableAlphaTest() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableAlphaTest();
    }
    
    @Deprecated
    public static void alphaFunc(final int integer, final float float2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._alphaFunc(integer, float2);
    }
    
    @Deprecated
    public static void enableLighting() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableLighting();
    }
    
    @Deprecated
    public static void disableLighting() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableLighting();
    }
    
    @Deprecated
    public static void enableColorMaterial() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableColorMaterial();
    }
    
    @Deprecated
    public static void disableColorMaterial() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableColorMaterial();
    }
    
    @Deprecated
    public static void colorMaterial(final int integer1, final int integer2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._colorMaterial(integer1, integer2);
    }
    
    @Deprecated
    public static void normal3f(final float float1, final float float2, final float float3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._normal3f(float1, float2, float3);
    }
    
    public static void disableDepthTest() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableDepthTest();
    }
    
    public static void enableDepthTest() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._enableDepthTest();
    }
    
    public static void depthFunc(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._depthFunc(integer);
    }
    
    public static void depthMask(final boolean boolean1) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._depthMask(boolean1);
    }
    
    public static void enableBlend() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableBlend();
    }
    
    public static void disableBlend() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableBlend();
    }
    
    public static void blendFunc(final GlStateManager.SourceFactor q, final GlStateManager.DestFactor j) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._blendFunc(q.value, j.value);
    }
    
    public static void blendFunc(final int integer1, final int integer2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._blendFunc(integer1, integer2);
    }
    
    public static void blendFuncSeparate(final GlStateManager.SourceFactor q1, final GlStateManager.DestFactor j2, final GlStateManager.SourceFactor q3, final GlStateManager.DestFactor j4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._blendFuncSeparate(q1.value, j2.value, q3.value, j4.value);
    }
    
    public static void blendFuncSeparate(final int integer1, final int integer2, final int integer3, final int integer4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._blendFuncSeparate(integer1, integer2, integer3, integer4);
    }
    
    public static void blendEquation(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._blendEquation(integer);
    }
    
    public static void blendColor(final float float1, final float float2, final float float3, final float float4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._blendColor(float1, float2, float3, float4);
    }
    
    @Deprecated
    public static void enableFog() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableFog();
    }
    
    @Deprecated
    public static void disableFog() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableFog();
    }
    
    @Deprecated
    public static void fogMode(final GlStateManager.FogMode m) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fogMode(m.value);
    }
    
    @Deprecated
    public static void fogMode(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fogMode(integer);
    }
    
    @Deprecated
    public static void fogDensity(final float float1) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fogDensity(float1);
    }
    
    @Deprecated
    public static void fogStart(final float float1) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fogStart(float1);
    }
    
    @Deprecated
    public static void fogEnd(final float float1) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fogEnd(float1);
    }
    
    @Deprecated
    public static void fog(final int integer, final float float2, final float float3, final float float4, final float float5) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fog(integer, new float[] { float2, float3, float4, float5 });
    }
    
    @Deprecated
    public static void fogi(final int integer1, final int integer2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._fogi(integer1, integer2);
    }
    
    public static void enableCull() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableCull();
    }
    
    public static void disableCull() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableCull();
    }
    
    public static void polygonMode(final int integer1, final int integer2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._polygonMode(integer1, integer2);
    }
    
    public static void enablePolygonOffset() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enablePolygonOffset();
    }
    
    public static void disablePolygonOffset() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disablePolygonOffset();
    }
    
    public static void enableLineOffset() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableLineOffset();
    }
    
    public static void disableLineOffset() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableLineOffset();
    }
    
    public static void polygonOffset(final float float1, final float float2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._polygonOffset(float1, float2);
    }
    
    public static void enableColorLogicOp() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableColorLogicOp();
    }
    
    public static void disableColorLogicOp() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableColorLogicOp();
    }
    
    public static void logicOp(final GlStateManager.LogicOp o) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._logicOp(o.value);
    }
    
    public static void activeTexture(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._activeTexture(integer);
    }
    
    public static void enableTexture() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableTexture();
    }
    
    public static void disableTexture() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableTexture();
    }
    
    public static void texParameter(final int integer1, final int integer2, final int integer3) {
        GlStateManager._texParameter(integer1, integer2, integer3);
    }
    
    public static void deleteTexture(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._deleteTexture(integer);
    }
    
    public static void bindTexture(final int integer) {
        GlStateManager._bindTexture(integer);
    }
    
    @Deprecated
    public static void shadeModel(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._shadeModel(integer);
    }
    
    @Deprecated
    public static void enableRescaleNormal() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._enableRescaleNormal();
    }
    
    @Deprecated
    public static void disableRescaleNormal() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._disableRescaleNormal();
    }
    
    public static void viewport(final int integer1, final int integer2, final int integer3, final int integer4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._viewport(integer1, integer2, integer3, integer4);
    }
    
    public static void colorMask(final boolean boolean1, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._colorMask(boolean1, boolean2, boolean3, boolean4);
    }
    
    public static void stencilFunc(final int integer1, final int integer2, final int integer3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._stencilFunc(integer1, integer2, integer3);
    }
    
    public static void stencilMask(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._stencilMask(integer);
    }
    
    public static void stencilOp(final int integer1, final int integer2, final int integer3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._stencilOp(integer1, integer2, integer3);
    }
    
    public static void clearDepth(final double double1) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._clearDepth(double1);
    }
    
    public static void clearColor(final float float1, final float float2, final float float3, final float float4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._clearColor(float1, float2, float3, float4);
    }
    
    public static void clearStencil(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._clearStencil(integer);
    }
    
    public static void clear(final int integer, final boolean boolean2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._clear(integer, boolean2);
    }
    
    @Deprecated
    public static void matrixMode(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._matrixMode(integer);
    }
    
    @Deprecated
    public static void loadIdentity() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._loadIdentity();
    }
    
    @Deprecated
    public static void pushMatrix() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._pushMatrix();
    }
    
    @Deprecated
    public static void popMatrix() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._popMatrix();
    }
    
    @Deprecated
    public static void ortho(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._ortho(double1, double2, double3, double4, double5, double6);
    }
    
    @Deprecated
    public static void rotatef(final float float1, final float float2, final float float3, final float float4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._rotatef(float1, float2, float3, float4);
    }
    
    @Deprecated
    public static void scalef(final float float1, final float float2, final float float3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._scalef(float1, float2, float3);
    }
    
    @Deprecated
    public static void scaled(final double double1, final double double2, final double double3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._scaled(double1, double2, double3);
    }
    
    @Deprecated
    public static void translatef(final float float1, final float float2, final float float3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._translatef(float1, float2, float3);
    }
    
    @Deprecated
    public static void translated(final double double1, final double double2, final double double3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._translated(double1, double2, double3);
    }
    
    @Deprecated
    public static void multMatrix(final Matrix4f b) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._multMatrix(b);
    }
    
    @Deprecated
    public static void color4f(final float float1, final float float2, final float float3, final float float4) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._color4f(float1, float2, float3, float4);
    }
    
    @Deprecated
    public static void color3f(final float float1, final float float2, final float float3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._color4f(float1, float2, float3, 1.0f);
    }
    
    @Deprecated
    public static void clearCurrentColor() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._clearCurrentColor();
    }
    
    public static void drawArrays(final int integer1, final int integer2, final int integer3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._drawArrays(integer1, integer2, integer3);
    }
    
    public static void lineWidth(final float float1) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._lineWidth(float1);
    }
    
    public static void pixelStore(final int integer1, final int integer2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThreadOrInit);
        GlStateManager._pixelStore(integer1, integer2);
    }
    
    public static void pixelTransfer(final int integer, final float float2) {
        GlStateManager._pixelTransfer(integer, float2);
    }
    
    public static void readPixels(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final ByteBuffer byteBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._readPixels(integer1, integer2, integer3, integer4, integer5, integer6, byteBuffer);
    }
    
    public static void getString(final int integer, final Consumer<String> consumer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        consumer.accept(GlStateManager._getString(integer));
    }
    
    public static String getBackendDescription() {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return String.format("LWJGL version %s", new Object[] { GLX._getLWJGLVersion() });
    }
    
    public static String getApiDescription() {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return GLX.getOpenGLVersionString();
    }
    
    public static LongSupplier initBackendSystem() {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return GLX._initGlfw();
    }
    
    public static void initRenderer(final int integer, final boolean boolean2) {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        GLX._init(integer, boolean2);
    }
    
    public static void setErrorCallback(final GLFWErrorCallbackI gLFWErrorCallbackI) {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        GLX._setGlfwErrorCallback(gLFWErrorCallbackI);
    }
    
    public static void renderCrosshair(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GLX._renderCrosshair(integer, true, true, true);
    }
    
    public static void setupNvFogDistance() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GLX._setupNvFogDistance();
    }
    
    @Deprecated
    public static void glMultiTexCoord2f(final int integer, final float float2, final float float3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glMultiTexCoord2f(integer, float2, float3);
    }
    
    public static String getCapsString() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        return GLX._getCapsString();
    }
    
    public static void setupDefaultState(final int integer1, final int integer2, final int integer3, final int integer4) {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        GlStateManager._enableTexture();
        GlStateManager._shadeModel(7425);
        GlStateManager._clearDepth(1.0);
        GlStateManager._enableDepthTest();
        GlStateManager._depthFunc(515);
        GlStateManager._enableAlphaTest();
        GlStateManager._alphaFunc(516, 0.1f);
        GlStateManager._matrixMode(5889);
        GlStateManager._loadIdentity();
        GlStateManager._matrixMode(5888);
        GlStateManager._viewport(integer1, integer2, integer3, integer4);
    }
    
    public static int maxSupportedTextureSize() {
        assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        if (RenderSystem.MAX_SUPPORTED_TEXTURE_SIZE == -1) {
            final int integer1 = GlStateManager._getInteger(3379);
            for (int integer2 = Math.max(32768, integer1); integer2 >= 1024; integer2 >>= 1) {
                GlStateManager._texImage2D(32868, 0, 6408, integer2, integer2, 0, 6408, 5121, null);
                final int integer3 = GlStateManager._getTexLevelParameter(32868, 0, 4096);
                if (integer3 != 0) {
                    return RenderSystem.MAX_SUPPORTED_TEXTURE_SIZE = integer2;
                }
            }
            RenderSystem.MAX_SUPPORTED_TEXTURE_SIZE = Math.max(integer1, 1024);
            RenderSystem.LOGGER.info("Failed to determine maximum texture size by probing, trying GL_MAX_TEXTURE_SIZE = {}", RenderSystem.MAX_SUPPORTED_TEXTURE_SIZE);
        }
        return RenderSystem.MAX_SUPPORTED_TEXTURE_SIZE;
    }
    
    public static void glBindBuffer(final int integer, final Supplier<Integer> supplier) {
        GlStateManager._glBindBuffer(integer, (int)supplier.get());
    }
    
    public static void glBufferData(final int integer1, final ByteBuffer byteBuffer, final int integer3) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager._glBufferData(integer1, byteBuffer, integer3);
    }
    
    public static void glDeleteBuffers(final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glDeleteBuffers(integer);
    }
    
    public static void glUniform1i(final int integer1, final int integer2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform1i(integer1, integer2);
    }
    
    public static void glUniform1(final int integer, final IntBuffer intBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform1(integer, intBuffer);
    }
    
    public static void glUniform2(final int integer, final IntBuffer intBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform2(integer, intBuffer);
    }
    
    public static void glUniform3(final int integer, final IntBuffer intBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform3(integer, intBuffer);
    }
    
    public static void glUniform4(final int integer, final IntBuffer intBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform4(integer, intBuffer);
    }
    
    public static void glUniform1(final int integer, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform1(integer, floatBuffer);
    }
    
    public static void glUniform2(final int integer, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform2(integer, floatBuffer);
    }
    
    public static void glUniform3(final int integer, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform3(integer, floatBuffer);
    }
    
    public static void glUniform4(final int integer, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniform4(integer, floatBuffer);
    }
    
    public static void glUniformMatrix2(final int integer, final boolean boolean2, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniformMatrix2(integer, boolean2, floatBuffer);
    }
    
    public static void glUniformMatrix3(final int integer, final boolean boolean2, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniformMatrix3(integer, boolean2, floatBuffer);
    }
    
    public static void glUniformMatrix4(final int integer, final boolean boolean2, final FloatBuffer floatBuffer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager._glUniformMatrix4(integer, boolean2, floatBuffer);
    }
    
    public static void setupOutline() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.setupOutline();
    }
    
    public static void teardownOutline() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.teardownOutline();
    }
    
    public static void setupOverlayColor(final IntSupplier intSupplier, final int integer) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.setupOverlayColor(intSupplier.getAsInt(), integer);
    }
    
    public static void teardownOverlayColor() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.teardownOverlayColor();
    }
    
    public static void setupLevelDiffuseLighting(final Vector3f g1, final Vector3f g2, final Matrix4f b) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.setupLevelDiffuseLighting(g1, g2, b);
    }
    
    public static void setupGuiFlatDiffuseLighting(final Vector3f g1, final Vector3f g2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.setupGuiFlatDiffuseLighting(g1, g2);
    }
    
    public static void setupGui3DDiffuseLighting(final Vector3f g1, final Vector3f g2) {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.setupGui3DDiffuseLighting(g1, g2);
    }
    
    public static void mulTextureByProjModelView() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.mulTextureByProjModelView();
    }
    
    public static void setupEndPortalTexGen() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.setupEndPortalTexGen();
    }
    
    public static void clearTexGen() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnGameThread);
        GlStateManager.clearTexGen();
    }
    
    public static void beginInitialization() {
        RenderSystem.isInInit = true;
    }
    
    public static void finishInitialization() {
        RenderSystem.isInInit = false;
        if (!RenderSystem.recordingQueue.isEmpty()) {
            replayQueue();
        }
        if (!RenderSystem.recordingQueue.isEmpty()) {
            throw new IllegalStateException("Recorded to render queue during initialization");
        }
    }
    
    public static void glGenBuffers(final Consumer<Integer> consumer) {
        if (!isOnRenderThread()) {
            recordRenderCall(() -> consumer.accept(GlStateManager._glGenBuffers()));
        }
        else {
            consumer.accept(GlStateManager._glGenBuffers());
        }
    }
    
    public static Tesselator renderThreadTesselator() {
        assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        return RenderSystem.RENDER_THREAD_TESSELATOR;
    }
    
    public static void defaultBlendFunc() {
        blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }
    
    public static void defaultAlphaFunc() {
        alphaFunc(516, 0.1f);
    }
    
    @Deprecated
    public static void runAsFancy(final Runnable runnable) {
        final boolean boolean2 = Minecraft.useShaderTransparency();
        if (!boolean2) {
            runnable.run();
            return;
        }
        final Options dka3 = Minecraft.getInstance().options;
        final GraphicsStatus djq4 = dka3.graphicsMode;
        dka3.graphicsMode = GraphicsStatus.FANCY;
        runnable.run();
        dka3.graphicsMode = djq4;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        recordingQueue = Queues.newConcurrentLinkedQueue();
        RENDER_THREAD_TESSELATOR = new Tesselator();
        RenderSystem.MAX_SUPPORTED_TEXTURE_SIZE = -1;
        RenderSystem.lastDrawTime = Double.MIN_VALUE;
    }
}
