package com.mojang.blaze3d.vertex;

import org.apache.logging.log4j.LogManager;
import com.google.common.primitives.Floats;
import com.mojang.datafixers.util.Pair;
import com.google.common.collect.ImmutableList;
import java.nio.FloatBuffer;
import java.util.BitSet;
import it.unimi.dsi.fastutil.ints.IntArrays;
import com.mojang.blaze3d.platform.MemoryTracker;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.List;
import java.nio.ByteBuffer;
import org.apache.logging.log4j.Logger;

public class BufferBuilder extends DefaultedVertexConsumer implements BufferVertexConsumer {
    private static final Logger LOGGER;
    private ByteBuffer buffer;
    private final List<DrawState> vertexCounts;
    private int lastRenderedCountIndex;
    private int totalRenderedBytes;
    private int nextElementByte;
    private int totalUploadedBytes;
    private int vertices;
    @Nullable
    private VertexFormatElement currentElement;
    private int elementIndex;
    private int mode;
    private VertexFormat format;
    private boolean fastFormat;
    private boolean fullFormat;
    private boolean building;
    
    public BufferBuilder(final int integer) {
        this.vertexCounts = (List<DrawState>)Lists.newArrayList();
        this.lastRenderedCountIndex = 0;
        this.totalRenderedBytes = 0;
        this.nextElementByte = 0;
        this.totalUploadedBytes = 0;
        this.buffer = MemoryTracker.createByteBuffer(integer * 4);
    }
    
    protected void ensureVertexCapacity() {
        this.ensureCapacity(this.format.getVertexSize());
    }
    
    private void ensureCapacity(final int integer) {
        if (this.nextElementByte + integer <= this.buffer.capacity()) {
            return;
        }
        final int integer2 = this.buffer.capacity();
        final int integer3 = integer2 + roundUp(integer);
        BufferBuilder.LOGGER.debug("Needed to grow BufferBuilder buffer: Old size {} bytes, new size {} bytes.", integer2, integer3);
        final ByteBuffer byteBuffer5 = MemoryTracker.createByteBuffer(integer3);
        this.buffer.position(0);
        byteBuffer5.put(this.buffer);
        byteBuffer5.rewind();
        this.buffer = byteBuffer5;
    }
    
    private static int roundUp(final int integer) {
        int integer2 = 2097152;
        if (integer == 0) {
            return integer2;
        }
        if (integer < 0) {
            integer2 *= -1;
        }
        final int integer3 = integer % integer2;
        if (integer3 == 0) {
            return integer;
        }
        return integer + integer2 - integer3;
    }
    
    public void sortQuads(final float float1, final float float2, final float float3) {
        this.buffer.clear();
        final FloatBuffer floatBuffer5 = this.buffer.asFloatBuffer();
        final int integer6 = this.vertices / 4;
        final float[] arr7 = new float[integer6];
        for (int integer7 = 0; integer7 < integer6; ++integer7) {
            arr7[integer7] = getQuadDistanceFromPlayer(floatBuffer5, float1, float2, float3, this.format.getIntegerSize(), this.totalRenderedBytes / 4 + integer7 * this.format.getVertexSize());
        }
        final int[] arr8 = new int[integer6];
        for (int integer8 = 0; integer8 < arr8.length; ++integer8) {
            arr8[integer8] = integer8;
        }
        IntArrays.mergeSort(arr8, (integer2, integer3) -> Floats.compare(arr7[integer3], arr7[integer2]));
        final BitSet bitSet9 = new BitSet();
        final FloatBuffer floatBuffer6 = MemoryTracker.createFloatBuffer(this.format.getIntegerSize() * 4);
        for (int integer9 = bitSet9.nextClearBit(0); integer9 < arr8.length; integer9 = bitSet9.nextClearBit(integer9 + 1)) {
            final int integer10 = arr8[integer9];
            if (integer10 != integer9) {
                this.limitToVertex(floatBuffer5, integer10);
                floatBuffer6.clear();
                floatBuffer6.put(floatBuffer5);
                for (int integer11 = integer10, integer12 = arr8[integer11]; integer11 != integer9; integer11 = integer12, integer12 = arr8[integer11]) {
                    this.limitToVertex(floatBuffer5, integer12);
                    final FloatBuffer floatBuffer7 = floatBuffer5.slice();
                    this.limitToVertex(floatBuffer5, integer11);
                    floatBuffer5.put(floatBuffer7);
                    bitSet9.set(integer11);
                }
                this.limitToVertex(floatBuffer5, integer9);
                floatBuffer6.flip();
                floatBuffer5.put(floatBuffer6);
            }
            bitSet9.set(integer9);
        }
    }
    
    private void limitToVertex(final FloatBuffer floatBuffer, final int integer) {
        final int integer2 = this.format.getIntegerSize() * 4;
        floatBuffer.limit(this.totalRenderedBytes / 4 + (integer + 1) * integer2);
        floatBuffer.position(this.totalRenderedBytes / 4 + integer * integer2);
    }
    
    public State getState() {
        this.buffer.limit(this.nextElementByte);
        this.buffer.position(this.totalRenderedBytes);
        final ByteBuffer byteBuffer2 = ByteBuffer.allocate(this.vertices * this.format.getVertexSize());
        byteBuffer2.put(this.buffer);
        this.buffer.clear();
        return new State(byteBuffer2, this.format);
    }
    
    private static float getQuadDistanceFromPlayer(final FloatBuffer floatBuffer, final float float2, final float float3, final float float4, final int integer5, final int integer6) {
        final float float5 = floatBuffer.get(integer6 + integer5 * 0 + 0);
        final float float6 = floatBuffer.get(integer6 + integer5 * 0 + 1);
        final float float7 = floatBuffer.get(integer6 + integer5 * 0 + 2);
        final float float8 = floatBuffer.get(integer6 + integer5 * 1 + 0);
        final float float9 = floatBuffer.get(integer6 + integer5 * 1 + 1);
        final float float10 = floatBuffer.get(integer6 + integer5 * 1 + 2);
        final float float11 = floatBuffer.get(integer6 + integer5 * 2 + 0);
        final float float12 = floatBuffer.get(integer6 + integer5 * 2 + 1);
        final float float13 = floatBuffer.get(integer6 + integer5 * 2 + 2);
        final float float14 = floatBuffer.get(integer6 + integer5 * 3 + 0);
        final float float15 = floatBuffer.get(integer6 + integer5 * 3 + 1);
        final float float16 = floatBuffer.get(integer6 + integer5 * 3 + 2);
        final float float17 = (float5 + float8 + float11 + float14) * 0.25f - float2;
        final float float18 = (float6 + float9 + float12 + float15) * 0.25f - float3;
        final float float19 = (float7 + float10 + float13 + float16) * 0.25f - float4;
        return float17 * float17 + float18 * float18 + float19 * float19;
    }
    
    public void restoreState(final State b) {
        b.data.clear();
        final int integer3 = b.data.capacity();
        this.ensureCapacity(integer3);
        this.buffer.limit(this.buffer.capacity());
        this.buffer.position(this.totalRenderedBytes);
        this.buffer.put(b.data);
        this.buffer.clear();
        final VertexFormat dfo4 = b.format;
        this.switchFormat(dfo4);
        this.vertices = integer3 / dfo4.getVertexSize();
        this.nextElementByte = this.totalRenderedBytes + this.vertices * dfo4.getVertexSize();
    }
    
    public void begin(final int integer, final VertexFormat dfo) {
        if (this.building) {
            throw new IllegalStateException("Already building!");
        }
        this.building = true;
        this.mode = integer;
        this.switchFormat(dfo);
        this.currentElement = (VertexFormatElement)dfo.getElements().get(0);
        this.elementIndex = 0;
        this.buffer.clear();
    }
    
    private void switchFormat(final VertexFormat dfo) {
        if (this.format == dfo) {
            return;
        }
        this.format = dfo;
        final boolean boolean3 = dfo == DefaultVertexFormat.NEW_ENTITY;
        final boolean boolean4 = dfo == DefaultVertexFormat.BLOCK;
        this.fastFormat = (boolean3 || boolean4);
        this.fullFormat = boolean3;
    }
    
    public void end() {
        if (!this.building) {
            throw new IllegalStateException("Not building!");
        }
        this.building = false;
        this.vertexCounts.add(new DrawState(this.format, this.vertices, this.mode));
        this.totalRenderedBytes += this.vertices * this.format.getVertexSize();
        this.vertices = 0;
        this.currentElement = null;
        this.elementIndex = 0;
    }
    
    @Override
    public void putByte(final int integer, final byte byte2) {
        this.buffer.put(this.nextElementByte + integer, byte2);
    }
    
    @Override
    public void putShort(final int integer, final short short2) {
        this.buffer.putShort(this.nextElementByte + integer, short2);
    }
    
    @Override
    public void putFloat(final int integer, final float float2) {
        this.buffer.putFloat(this.nextElementByte + integer, float2);
    }
    
    public void endVertex() {
        if (this.elementIndex != 0) {
            throw new IllegalStateException("Not filled all elements of the vertex");
        }
        ++this.vertices;
        this.ensureVertexCapacity();
    }
    
    @Override
    public void nextElement() {
        final ImmutableList<VertexFormatElement> immutableList2 = this.format.getElements();
        this.elementIndex = (this.elementIndex + 1) % immutableList2.size();
        this.nextElementByte += this.currentElement.getByteSize();
        final VertexFormatElement dfp3 = (VertexFormatElement)immutableList2.get(this.elementIndex);
        this.currentElement = dfp3;
        if (dfp3.getUsage() == VertexFormatElement.Usage.PADDING) {
            this.nextElement();
        }
        if (this.defaultColorSet && this.currentElement.getUsage() == VertexFormatElement.Usage.COLOR) {
            super.color(this.defaultR, this.defaultG, this.defaultB, this.defaultA);
        }
    }
    
    @Override
    public VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4) {
        if (this.defaultColorSet) {
            throw new IllegalStateException();
        }
        return super.color(integer1, integer2, integer3, integer4);
    }
    
    public void vertex(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6, final float float7, final float float8, final float float9, final int integer10, final int integer11, final float float12, final float float13, final float float14) {
        if (this.defaultColorSet) {
            throw new IllegalStateException();
        }
        if (this.fastFormat) {
            this.putFloat(0, float1);
            this.putFloat(4, float2);
            this.putFloat(8, float3);
            this.putByte(12, (byte)(float4 * 255.0f));
            this.putByte(13, (byte)(float5 * 255.0f));
            this.putByte(14, (byte)(float6 * 255.0f));
            this.putByte(15, (byte)(float7 * 255.0f));
            this.putFloat(16, float8);
            this.putFloat(20, float9);
            int integer12;
            if (this.fullFormat) {
                this.putShort(24, (short)(integer10 & 0xFFFF));
                this.putShort(26, (short)(integer10 >> 16 & 0xFFFF));
                integer12 = 28;
            }
            else {
                integer12 = 24;
            }
            this.putShort(integer12 + 0, (short)(integer11 & 0xFFFF));
            this.putShort(integer12 + 2, (short)(integer11 >> 16 & 0xFFFF));
            this.putByte(integer12 + 4, BufferVertexConsumer.normalIntValue(float12));
            this.putByte(integer12 + 5, BufferVertexConsumer.normalIntValue(float13));
            this.putByte(integer12 + 6, BufferVertexConsumer.normalIntValue(float14));
            this.nextElementByte += integer12 + 8;
            this.endVertex();
            return;
        }
        super.vertex(float1, float2, float3, float4, float5, float6, float7, float8, float9, integer10, integer11, float12, float13, float14);
    }
    
    public Pair<DrawState, ByteBuffer> popNextBuffer() {
        final DrawState a2 = (DrawState)this.vertexCounts.get(this.lastRenderedCountIndex++);
        this.buffer.position(this.totalUploadedBytes);
        this.totalUploadedBytes += a2.vertexCount() * a2.format().getVertexSize();
        this.buffer.limit(this.totalUploadedBytes);
        if (this.lastRenderedCountIndex == this.vertexCounts.size() && this.vertices == 0) {
            this.clear();
        }
        final ByteBuffer byteBuffer3 = this.buffer.slice();
        this.buffer.clear();
        return (Pair<DrawState, ByteBuffer>)Pair.of(a2, byteBuffer3);
    }
    
    public void clear() {
        if (this.totalRenderedBytes != this.totalUploadedBytes) {
            BufferBuilder.LOGGER.warn(new StringBuilder().append("Bytes mismatch ").append(this.totalRenderedBytes).append(" ").append(this.totalUploadedBytes).toString());
        }
        this.discard();
    }
    
    public void discard() {
        this.totalRenderedBytes = 0;
        this.totalUploadedBytes = 0;
        this.nextElementByte = 0;
        this.vertexCounts.clear();
        this.lastRenderedCountIndex = 0;
    }
    
    @Override
    public VertexFormatElement currentElement() {
        if (this.currentElement == null) {
            throw new IllegalStateException("BufferBuilder not started");
        }
        return this.currentElement;
    }
    
    public boolean building() {
        return this.building;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class State {
        private final ByteBuffer data;
        private final VertexFormat format;
        
        private State(final ByteBuffer byteBuffer, final VertexFormat dfo) {
            this.data = byteBuffer;
            this.format = dfo;
        }
    }
    
    public static final class DrawState {
        private final VertexFormat format;
        private final int vertexCount;
        private final int mode;
        
        private DrawState(final VertexFormat dfo, final int integer2, final int integer3) {
            this.format = dfo;
            this.vertexCount = integer2;
            this.mode = integer3;
        }
        
        public VertexFormat format() {
            return this.format;
        }
        
        public int vertexCount() {
            return this.vertexCount;
        }
        
        public int mode() {
            return this.mode;
        }
    }
}
