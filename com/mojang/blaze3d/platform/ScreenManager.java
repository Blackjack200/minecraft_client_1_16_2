package com.mojang.blaze3d.platform;

import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWMonitorCallbackI;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

public class ScreenManager {
    private final Long2ObjectMap<Monitor> monitors;
    private final MonitorCreator monitorCreator;
    
    public ScreenManager(final MonitorCreator dep) {
        this.monitors = (Long2ObjectMap<Monitor>)new Long2ObjectOpenHashMap();
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        this.monitorCreator = dep;
        GLFW.glfwSetMonitorCallback(this::onMonitorChange);
        final PointerBuffer pointerBuffer3 = GLFW.glfwGetMonitors();
        if (pointerBuffer3 != null) {
            for (int integer4 = 0; integer4 < pointerBuffer3.limit(); ++integer4) {
                final long long5 = pointerBuffer3.get(integer4);
                this.monitors.put(long5, dep.createMonitor(long5));
            }
        }
    }
    
    private void onMonitorChange(final long long1, final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        if (integer == 262145) {
            this.monitors.put(long1, this.monitorCreator.createMonitor(long1));
        }
        else if (integer == 262146) {
            this.monitors.remove(long1);
        }
    }
    
    @Nullable
    public Monitor getMonitor(final long long1) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return (Monitor)this.monitors.get(long1);
    }
    
    @Nullable
    public Monitor findBestMonitor(final Window dew) {
        final long long3 = GLFW.glfwGetWindowMonitor(dew.getWindow());
        if (long3 != 0L) {
            return this.getMonitor(long3);
        }
        final int integer5 = dew.getX();
        final int integer6 = integer5 + dew.getScreenWidth();
        final int integer7 = dew.getY();
        final int integer8 = integer7 + dew.getScreenHeight();
        int integer9 = -1;
        Monitor deo10 = null;
        for (final Monitor deo11 : this.monitors.values()) {
            final int integer10 = deo11.getX();
            final int integer11 = integer10 + deo11.getCurrentMode().getWidth();
            final int integer12 = deo11.getY();
            final int integer13 = integer12 + deo11.getCurrentMode().getHeight();
            final int integer14 = clamp(integer5, integer10, integer11);
            final int integer15 = clamp(integer6, integer10, integer11);
            final int integer16 = clamp(integer7, integer12, integer13);
            final int integer17 = clamp(integer8, integer12, integer13);
            final int integer18 = Math.max(0, integer15 - integer14);
            final int integer19 = Math.max(0, integer17 - integer16);
            final int integer20 = integer18 * integer19;
            if (integer20 > integer9) {
                deo10 = deo11;
                integer9 = integer20;
            }
        }
        return deo10;
    }
    
    public static int clamp(final int integer1, final int integer2, final int integer3) {
        if (integer1 < integer2) {
            return integer2;
        }
        if (integer1 > integer3) {
            return integer3;
        }
        return integer1;
    }
    
    public void shutdown() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        final GLFWMonitorCallback gLFWMonitorCallback2 = GLFW.glfwSetMonitorCallback((GLFWMonitorCallbackI)null);
        if (gLFWMonitorCallback2 != null) {
            gLFWMonitorCallback2.free();
        }
    }
}
