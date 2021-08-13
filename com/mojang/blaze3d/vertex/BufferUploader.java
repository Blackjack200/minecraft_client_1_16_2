package com.mojang.blaze3d.vertex;

import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.system.MemoryUtil;
import java.util.function.Supplier;
import com.mojang.datafixers.util.Pair;
import java.nio.ByteBuffer;
import com.mojang.blaze3d.systems.RenderSystem;

public class BufferUploader {
    public static void end(final BufferBuilder dfe) {
        if (!RenderSystem.isOnRenderThread()) {
            final Pair<BufferBuilder.DrawState, ByteBuffer> pair2;
            final BufferBuilder.DrawState a3;
            RenderSystem.recordRenderCall(() -> {
                pair2 = dfe.popNextBuffer();
                a3 = (BufferBuilder.DrawState)pair2.getFirst();
                _end((ByteBuffer)pair2.getSecond(), a3.mode(), a3.format(), a3.vertexCount());
            });
        }
        else {
            final Pair<BufferBuilder.DrawState, ByteBuffer> pair3 = dfe.popNextBuffer();
            final BufferBuilder.DrawState a4 = (BufferBuilder.DrawState)pair3.getFirst();
            _end((ByteBuffer)pair3.getSecond(), a4.mode(), a4.format(), a4.vertexCount());
        }
    }
    
    private static void _end(final ByteBuffer byteBuffer, final int integer2, final VertexFormat dfo, final int integer4) {
        RenderSystem.assertThread((Supplier<Boolean>)RenderSystem::isOnRenderThread);
        byteBuffer.clear();
        if (integer4 <= 0) {
            return;
        }
        dfo.setupBufferState(MemoryUtil.memAddress(byteBuffer));
        GlStateManager._drawArrays(integer2, 0, integer4);
        dfo.clearBufferState();
    }
}
