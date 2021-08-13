package com.mojang.blaze3d.platform;

import java.nio.FloatBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public class MemoryTracker {
    public static synchronized ByteBuffer createByteBuffer(final int integer) {
        return ByteBuffer.allocateDirect(integer).order(ByteOrder.nativeOrder());
    }
    
    public static FloatBuffer createFloatBuffer(final int integer) {
        return createByteBuffer(integer << 2).asFloatBuffer();
    }
}
