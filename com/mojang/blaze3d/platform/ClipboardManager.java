package com.mojang.blaze3d.platform;

import java.nio.Buffer;
import org.lwjgl.system.MemoryUtil;
import com.google.common.base.Charsets;
import org.lwjgl.glfw.GLFWErrorCallback;
import net.minecraft.util.StringDecomposer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;

public class ClipboardManager {
    private final ByteBuffer clipboardScratchBuffer;
    
    public ClipboardManager() {
        this.clipboardScratchBuffer = BufferUtils.createByteBuffer(8192);
    }
    
    public String getClipboard(final long long1, final GLFWErrorCallbackI gLFWErrorCallbackI) {
        final GLFWErrorCallback gLFWErrorCallback5 = GLFW.glfwSetErrorCallback(gLFWErrorCallbackI);
        String string6 = GLFW.glfwGetClipboardString(long1);
        string6 = ((string6 != null) ? StringDecomposer.filterBrokenSurrogates(string6) : "");
        final GLFWErrorCallback gLFWErrorCallback6 = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)gLFWErrorCallback5);
        if (gLFWErrorCallback6 != null) {
            gLFWErrorCallback6.free();
        }
        return string6;
    }
    
    private static void pushClipboard(final long long1, final ByteBuffer byteBuffer, final byte[] arr) {
        byteBuffer.clear();
        byteBuffer.put(arr);
        byteBuffer.put((byte)0);
        byteBuffer.flip();
        GLFW.glfwSetClipboardString(long1, byteBuffer);
    }
    
    public void setClipboard(final long long1, final String string) {
        final byte[] arr5 = string.getBytes(Charsets.UTF_8);
        final int integer6 = arr5.length + 1;
        if (integer6 < this.clipboardScratchBuffer.capacity()) {
            pushClipboard(long1, this.clipboardScratchBuffer, arr5);
        }
        else {
            final ByteBuffer byteBuffer7 = MemoryUtil.memAlloc(integer6);
            try {
                pushClipboard(long1, byteBuffer7, arr5);
            }
            finally {
                MemoryUtil.memFree((Buffer)byteBuffer7);
            }
        }
    }
}
