package com.mojang.blaze3d.vertex;

import net.minecraft.util.Mth;

public interface BufferVertexConsumer extends VertexConsumer {
    VertexFormatElement currentElement();
    
    void nextElement();
    
    void putByte(final int integer, final byte byte2);
    
    void putShort(final int integer, final short short2);
    
    void putFloat(final int integer, final float float2);
    
    default VertexConsumer vertex(final double double1, final double double2, final double double3) {
        if (this.currentElement().getType() != VertexFormatElement.Type.FLOAT) {
            throw new IllegalStateException();
        }
        this.putFloat(0, (float)double1);
        this.putFloat(4, (float)double2);
        this.putFloat(8, (float)double3);
        this.nextElement();
        return this;
    }
    
    default VertexConsumer color(final int integer1, final int integer2, final int integer3, final int integer4) {
        final VertexFormatElement dfp6 = this.currentElement();
        if (dfp6.getUsage() != VertexFormatElement.Usage.COLOR) {
            return this;
        }
        if (dfp6.getType() != VertexFormatElement.Type.UBYTE) {
            throw new IllegalStateException();
        }
        this.putByte(0, (byte)integer1);
        this.putByte(1, (byte)integer2);
        this.putByte(2, (byte)integer3);
        this.putByte(3, (byte)integer4);
        this.nextElement();
        return this;
    }
    
    default VertexConsumer uv(final float float1, final float float2) {
        final VertexFormatElement dfp4 = this.currentElement();
        if (dfp4.getUsage() != VertexFormatElement.Usage.UV || dfp4.getIndex() != 0) {
            return this;
        }
        if (dfp4.getType() != VertexFormatElement.Type.FLOAT) {
            throw new IllegalStateException();
        }
        this.putFloat(0, float1);
        this.putFloat(4, float2);
        this.nextElement();
        return this;
    }
    
    default VertexConsumer overlayCoords(final int integer1, final int integer2) {
        return this.uvShort((short)integer1, (short)integer2, 1);
    }
    
    default VertexConsumer uv2(final int integer1, final int integer2) {
        return this.uvShort((short)integer1, (short)integer2, 2);
    }
    
    default VertexConsumer uvShort(final short short1, final short short2, final int integer) {
        final VertexFormatElement dfp5 = this.currentElement();
        if (dfp5.getUsage() != VertexFormatElement.Usage.UV || dfp5.getIndex() != integer) {
            return this;
        }
        if (dfp5.getType() != VertexFormatElement.Type.SHORT) {
            throw new IllegalStateException();
        }
        this.putShort(0, short1);
        this.putShort(2, short2);
        this.nextElement();
        return this;
    }
    
    default VertexConsumer normal(final float float1, final float float2, final float float3) {
        final VertexFormatElement dfp5 = this.currentElement();
        if (dfp5.getUsage() != VertexFormatElement.Usage.NORMAL) {
            return this;
        }
        if (dfp5.getType() != VertexFormatElement.Type.BYTE) {
            throw new IllegalStateException();
        }
        this.putByte(0, normalIntValue(float1));
        this.putByte(1, normalIntValue(float2));
        this.putByte(2, normalIntValue(float3));
        this.nextElement();
        return this;
    }
    
    default byte normalIntValue(final float float1) {
        return (byte)((int)(Mth.clamp(float1, -1.0f, 1.0f) * 127.0f) & 0xFF);
    }
}
