package com.mojang.blaze3d.platform;

import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import java.util.function.Consumer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import org.lwjgl.opengl.GL11;
import oshi.hardware.Processor;
import org.lwjgl.opengl.GLCapabilities;
import oshi.SystemInfo;
import org.lwjgl.opengl.GL;
import java.util.Iterator;
import org.lwjgl.glfw.GLFWErrorCallback;
import java.util.List;
import com.google.common.base.Joiner;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import com.google.common.collect.Lists;
import java.util.function.BiConsumer;
import java.util.function.LongSupplier;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFW;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class GLX {
    private static final Logger LOGGER;
    private static String capsString;
    private static String cpuInfo;
    private static final Map<Integer, String> LOOKUP_MAP;
    
    public static String getOpenGLVersionString() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (GLFW.glfwGetCurrentContext() == 0L) {
            return "NO CONTEXT";
        }
        return GlStateManager._getString(7937) + " GL version " + GlStateManager._getString(7938) + ", " + GlStateManager._getString(7936);
    }
    
    public static int _getRefreshRate(final Window dew) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        long long2 = GLFW.glfwGetWindowMonitor(dew.getWindow());
        if (long2 == 0L) {
            long2 = GLFW.glfwGetPrimaryMonitor();
        }
        final GLFWVidMode gLFWVidMode4 = (long2 == 0L) ? null : GLFW.glfwGetVideoMode(long2);
        return (gLFWVidMode4 == null) ? 0 : gLFWVidMode4.refreshRate();
    }
    
    public static String _getLWJGLVersion() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return Version.getVersion();
    }
    
    public static LongSupplier _initGlfw() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        Window.checkGlfwError((BiConsumer<Integer, String>)((integer, string) -> {
            throw new IllegalStateException(String.format("GLFW error before init: [0x%X]%s", new Object[] { integer, string }));
        }));
        final List<String> list1 = (List<String>)Lists.newArrayList();
        final GLFWErrorCallback gLFWErrorCallback2 = GLFW.glfwSetErrorCallback((integer, long3) -> list1.add(String.format("GLFW error during init: [0x%X]%s", new Object[] { integer, long3 })));
        if (GLFW.glfwInit()) {
            final LongSupplier longSupplier3 = () -> (long)(GLFW.glfwGetTime() * 1.0E9);
            for (final String string5 : list1) {
                GLX.LOGGER.error("GLFW error collected during initialization: {}", string5);
            }
            RenderSystem.setErrorCallback((GLFWErrorCallbackI)gLFWErrorCallback2);
            return longSupplier3;
        }
        throw new IllegalStateException("Failed to initialize GLFW, errors: " + Joiner.on(",").join((Iterable)list1));
    }
    
    public static void _setGlfwErrorCallback(final GLFWErrorCallbackI gLFWErrorCallbackI) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        final GLFWErrorCallback gLFWErrorCallback2 = GLFW.glfwSetErrorCallback(gLFWErrorCallbackI);
        if (gLFWErrorCallback2 != null) {
            gLFWErrorCallback2.free();
        }
    }
    
    public static boolean _shouldClose(final Window dew) {
        return GLFW.glfwWindowShouldClose(dew.getWindow());
    }
    
    public static void _setupNvFogDistance() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (GL.getCapabilities().GL_NV_fog_distance) {
            GlStateManager._fogi(34138, 34139);
        }
    }
    
    public static void _init(final int integer, final boolean boolean2) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        final GLCapabilities gLCapabilities3 = GL.getCapabilities();
        GLX.capsString = "Using framebuffer using " + GlStateManager._init_fbo(gLCapabilities3);
        try {
            final Processor[] arr4 = new SystemInfo().getHardware().getProcessors();
            GLX.cpuInfo = String.format("%dx %s", new Object[] { arr4.length, arr4[0] }).replaceAll("\\s+", " ");
        }
        catch (Throwable t) {}
        GlDebug.enableDebugCallback(integer, boolean2);
    }
    
    public static String _getCapsString() {
        return GLX.capsString;
    }
    
    public static String _getCpuInfo() {
        return (GLX.cpuInfo == null) ? "<unknown>" : GLX.cpuInfo;
    }
    
    public static void _renderCrosshair(final int integer, final boolean boolean2, final boolean boolean3, final boolean boolean4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GlStateManager._disableTexture();
        GlStateManager._depthMask(false);
        final Tesselator dfl5 = RenderSystem.renderThreadTesselator();
        final BufferBuilder dfe6 = dfl5.getBuilder();
        GL11.glLineWidth(4.0f);
        dfe6.begin(1, DefaultVertexFormat.POSITION_COLOR);
        if (boolean2) {
            dfe6.vertex(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
            dfe6.vertex(integer, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        }
        if (boolean3) {
            dfe6.vertex(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
            dfe6.vertex(0.0, integer, 0.0).color(0, 0, 0, 255).endVertex();
        }
        if (boolean4) {
            dfe6.vertex(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
            dfe6.vertex(0.0, 0.0, integer).color(0, 0, 0, 255).endVertex();
        }
        dfl5.end();
        GL11.glLineWidth(2.0f);
        dfe6.begin(1, DefaultVertexFormat.POSITION_COLOR);
        if (boolean2) {
            dfe6.vertex(0.0, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
            dfe6.vertex(integer, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        }
        if (boolean3) {
            dfe6.vertex(0.0, 0.0, 0.0).color(0, 255, 0, 255).endVertex();
            dfe6.vertex(0.0, integer, 0.0).color(0, 255, 0, 255).endVertex();
        }
        if (boolean4) {
            dfe6.vertex(0.0, 0.0, 0.0).color(127, 127, 255, 255).endVertex();
            dfe6.vertex(0.0, 0.0, integer).color(127, 127, 255, 255).endVertex();
        }
        dfl5.end();
        GL11.glLineWidth(1.0f);
        GlStateManager._depthMask(true);
        GlStateManager._enableTexture();
    }
    
    public static String getErrorString(final int integer) {
        return (String)GLX.LOOKUP_MAP.get(integer);
    }
    
    public static <T> T make(final Supplier<T> supplier) {
        return (T)supplier.get();
    }
    
    public static <T> T make(final T object, final Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GLX.capsString = "";
        LOOKUP_MAP = GLX.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(0, "No error");
            hashMap.put(1280, "Enum parameter is invalid for this function");
            hashMap.put(1281, "Parameter is invalid for this function");
            hashMap.put(1282, "Current state is invalid for this function");
            hashMap.put(1283, "Stack overflow");
            hashMap.put(1284, "Stack underflow");
            hashMap.put(1285, "Out of memory");
            hashMap.put(1286, "Operation on incomplete framebuffer");
            hashMap.put(1286, "Operation on incomplete framebuffer");
        }));
    }
}
