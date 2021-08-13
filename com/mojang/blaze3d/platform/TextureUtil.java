package com.mojang.blaze3d.platform;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import java.nio.Buffer;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.Channels;
import org.lwjgl.system.MemoryUtil;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.SharedConstants;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.logging.log4j.Logger;

public class TextureUtil {
    private static final Logger LOGGER;
    
    public static int generateTextureId() {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            final int[] arr1 = new int[ThreadLocalRandom.current().nextInt(15) + 1];
            GlStateManager._genTextures(arr1);
            final int integer2 = GlStateManager._genTexture();
            GlStateManager._deleteTextures(arr1);
            return integer2;
        }
        return GlStateManager._genTexture();
    }
    
    public static void releaseTextureId(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager._deleteTexture(integer);
    }
    
    public static void prepareImage(final int integer1, final int integer2, final int integer3) {
        prepareImage(NativeImage.InternalGlFormat.RGBA, integer1, 0, integer2, integer3);
    }
    
    public static void prepareImage(final NativeImage.InternalGlFormat b, final int integer2, final int integer3, final int integer4) {
        prepareImage(b, integer2, 0, integer3, integer4);
    }
    
    public static void prepareImage(final int integer1, final int integer2, final int integer3, final int integer4) {
        prepareImage(NativeImage.InternalGlFormat.RGBA, integer1, integer2, integer3, integer4);
    }
    
    public static void prepareImage(final NativeImage.InternalGlFormat b, final int integer2, final int integer3, final int integer4, final int integer5) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        bind(integer2);
        if (integer3 >= 0) {
            GlStateManager._texParameter(3553, 33085, integer3);
            GlStateManager._texParameter(3553, 33082, 0);
            GlStateManager._texParameter(3553, 33083, integer3);
            GlStateManager._texParameter(3553, 34049, 0.0f);
        }
        for (int integer6 = 0; integer6 <= integer3; ++integer6) {
            GlStateManager._texImage2D(3553, integer6, b.glFormat(), integer4 >> integer6, integer5 >> integer6, 0, 6408, 5121, null);
        }
    }
    
    private static void bind(final int integer) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThreadOrInit);
        GlStateManager._bindTexture(integer);
    }
    
    public static ByteBuffer readResource(final InputStream inputStream) throws IOException {
        ByteBuffer byteBuffer2;
        if (inputStream instanceof FileInputStream) {
            final FileInputStream fileInputStream3 = (FileInputStream)inputStream;
            final FileChannel fileChannel4 = fileInputStream3.getChannel();
            byteBuffer2 = MemoryUtil.memAlloc((int)fileChannel4.size() + 1);
            while (fileChannel4.read(byteBuffer2) != -1) {}
        }
        else {
            byteBuffer2 = MemoryUtil.memAlloc(8192);
            for (ReadableByteChannel readableByteChannel3 = Channels.newChannel(inputStream); readableByteChannel3.read(byteBuffer2) != -1; byteBuffer2 = MemoryUtil.memRealloc(byteBuffer2, byteBuffer2.capacity() * 2)) {
                if (byteBuffer2.remaining() == 0) {}
            }
        }
        return byteBuffer2;
    }
    
    public static String readResourceAsString(final InputStream inputStream) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        ByteBuffer byteBuffer2 = null;
        try {
            byteBuffer2 = readResource(inputStream);
            final int integer3 = byteBuffer2.position();
            byteBuffer2.rewind();
            return MemoryUtil.memASCII(byteBuffer2, integer3);
        }
        catch (IOException ex) {}
        finally {
            if (byteBuffer2 != null) {
                MemoryUtil.memFree((Buffer)byteBuffer2);
            }
        }
        return null;
    }
    
    public static void initTexture(final IntBuffer intBuffer, final int integer2, final int integer3) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        GL11.glPixelStorei(3312, 0);
        GL11.glPixelStorei(3313, 0);
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glPixelStorei(3316, 0);
        GL11.glPixelStorei(3317, 4);
        GL11.glTexImage2D(3553, 0, 6408, integer2, integer3, 0, 32993, 33639, intBuffer);
        GL11.glTexParameteri(3553, 10242, 10497);
        GL11.glTexParameteri(3553, 10243, 10497);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10241, 9729);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
