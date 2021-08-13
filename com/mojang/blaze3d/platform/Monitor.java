package com.mojang.blaze3d.platform;

import java.util.Iterator;
import java.util.Optional;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFW;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import com.google.common.collect.Lists;
import java.util.List;

public final class Monitor {
    private final long monitor;
    private final List<VideoMode> videoModes;
    private VideoMode currentMode;
    private int x;
    private int y;
    
    public Monitor(final long long1) {
        this.monitor = long1;
        this.videoModes = (List<VideoMode>)Lists.newArrayList();
        this.refreshVideoModes();
    }
    
    public void refreshVideoModes() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        this.videoModes.clear();
        final GLFWVidMode.Buffer buffer2 = GLFW.glfwGetVideoModes(this.monitor);
        for (int integer3 = buffer2.limit() - 1; integer3 >= 0; --integer3) {
            buffer2.position(integer3);
            final VideoMode dev4 = new VideoMode(buffer2);
            if (dev4.getRedBits() >= 8 && dev4.getGreenBits() >= 8 && dev4.getBlueBits() >= 8) {
                this.videoModes.add(dev4);
            }
        }
        final int[] arr3 = { 0 };
        final int[] arr4 = { 0 };
        GLFW.glfwGetMonitorPos(this.monitor, arr3, arr4);
        this.x = arr3[0];
        this.y = arr4[0];
        final GLFWVidMode gLFWVidMode5 = GLFW.glfwGetVideoMode(this.monitor);
        this.currentMode = new VideoMode(gLFWVidMode5);
    }
    
    public VideoMode getPreferredVidMode(final Optional<VideoMode> optional) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        if (optional.isPresent()) {
            final VideoMode dev3 = (VideoMode)optional.get();
            for (final VideoMode dev4 : this.videoModes) {
                if (dev4.equals(dev3)) {
                    return dev4;
                }
            }
        }
        return this.getCurrentMode();
    }
    
    public int getVideoModeIndex(final VideoMode dev) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isInInitPhase);
        return this.videoModes.indexOf(dev);
    }
    
    public VideoMode getCurrentMode() {
        return this.currentMode;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public VideoMode getMode(final int integer) {
        return (VideoMode)this.videoModes.get(integer);
    }
    
    public int getModeCount() {
        return this.videoModes.size();
    }
    
    public long getMonitor() {
        return this.monitor;
    }
    
    public String toString() {
        return String.format("Monitor[%s %sx%s %s]", new Object[] { this.monitor, this.x, this.y, this.currentMode });
    }
}
