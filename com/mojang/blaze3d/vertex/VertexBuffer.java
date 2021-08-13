package com.mojang.blaze3d.vertex;

import com.mojang.math.Matrix4f;
import com.mojang.datafixers.util.Pair;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.function.Consumer;
import com.mojang.blaze3d.systems.RenderSystem;

public class VertexBuffer implements AutoCloseable {
    private int id;
    private final VertexFormat format;
    private int vertexCount;
    
    public VertexBuffer(final VertexFormat dfo) {
        this.format = dfo;
        RenderSystem.glGenBuffers((Consumer<Integer>)(integer -> this.id = integer));
    }
    
    public void bind() {
        RenderSystem.glBindBuffer(34962, (Supplier<Integer>)(() -> this.id));
    }
    
    public void upload(final BufferBuilder dfe) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this.upload_(dfe));
        }
        else {
            this.upload_(dfe);
        }
    }
    
    public CompletableFuture<Void> uploadLater(final BufferBuilder dfe) {
        if (!RenderSystem.isOnRenderThread()) {
            return (CompletableFuture<Void>)CompletableFuture.runAsync(() -> this.upload_(dfe), runnable -> RenderSystem.recordRenderCall(runnable::run));
        }
        this.upload_(dfe);
        return (CompletableFuture<Void>)CompletableFuture.completedFuture(null);
    }
    
    private void upload_(final BufferBuilder dfe) {
        final Pair<BufferBuilder.DrawState, ByteBuffer> pair3 = dfe.popNextBuffer();
        if (this.id == -1) {
            return;
        }
        final ByteBuffer byteBuffer4 = (ByteBuffer)pair3.getSecond();
        this.vertexCount = byteBuffer4.remaining() / this.format.getVertexSize();
        this.bind();
        RenderSystem.glBufferData(34962, byteBuffer4, 35044);
        unbind();
    }
    
    public void draw(final Matrix4f b, final int integer) {
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(b);
        RenderSystem.drawArrays(integer, 0, this.vertexCount);
        RenderSystem.popMatrix();
    }
    
    public static void unbind() {
        RenderSystem.glBindBuffer(34962, (Supplier<Integer>)(() -> 0));
    }
    
    public void close() {
        if (this.id >= 0) {
            RenderSystem.glDeleteBuffers(this.id);
            this.id = -1;
        }
    }
}
