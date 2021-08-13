package com.mojang.blaze3d.vertex;

import java.util.List;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.stream.Collectors;
import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import com.google.common.collect.ImmutableList;

public class VertexFormat {
    private final ImmutableList<VertexFormatElement> elements;
    private final IntList offsets;
    private final int vertexSize;
    
    public VertexFormat(final ImmutableList<VertexFormatElement> immutableList) {
        this.offsets = (IntList)new IntArrayList();
        this.elements = immutableList;
        int integer3 = 0;
        for (final VertexFormatElement dfp5 : immutableList) {
            this.offsets.add(integer3);
            integer3 += dfp5.getByteSize();
        }
        this.vertexSize = integer3;
    }
    
    public String toString() {
        return new StringBuilder().append("format: ").append(this.elements.size()).append(" elements: ").append((String)this.elements.stream().map(Object::toString).collect(Collectors.joining(" "))).toString();
    }
    
    public int getIntegerSize() {
        return this.getVertexSize() / 4;
    }
    
    public int getVertexSize() {
        return this.vertexSize;
    }
    
    public ImmutableList<VertexFormatElement> getElements() {
        return this.elements;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final VertexFormat dfo3 = (VertexFormat)object;
        return this.vertexSize == dfo3.vertexSize && this.elements.equals(dfo3.elements);
    }
    
    public int hashCode() {
        return this.elements.hashCode();
    }
    
    public void setupBufferState(final long long1) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> this.setupBufferState(long1));
            return;
        }
        final int integer4 = this.getVertexSize();
        final List<VertexFormatElement> list5 = (List<VertexFormatElement>)this.getElements();
        for (int integer5 = 0; integer5 < list5.size(); ++integer5) {
            ((VertexFormatElement)list5.get(integer5)).setupBufferState(long1 + this.offsets.getInt(integer5), integer4);
        }
    }
    
    public void clearBufferState() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::clearBufferState);
            return;
        }
        for (final VertexFormatElement dfp3 : this.getElements()) {
            dfp3.clearBufferState();
        }
    }
}
