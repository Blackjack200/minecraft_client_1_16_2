package com.mojang.blaze3d.vertex;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.function.IntConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormatElement {
    private static final Logger LOGGER;
    private final Type type;
    private final Usage usage;
    private final int index;
    private final int count;
    private final int byteSize;
    
    public VertexFormatElement(final int integer1, final Type a, final Usage b, final int integer4) {
        if (this.supportsUsage(integer1, b)) {
            this.usage = b;
        }
        else {
            VertexFormatElement.LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
            this.usage = Usage.UV;
        }
        this.type = a;
        this.index = integer1;
        this.count = integer4;
        this.byteSize = a.getSize() * this.count;
    }
    
    private boolean supportsUsage(final int integer, final Usage b) {
        return integer == 0 || b == Usage.UV;
    }
    
    public final Type getType() {
        return this.type;
    }
    
    public final Usage getUsage() {
        return this.usage;
    }
    
    public final int getIndex() {
        return this.index;
    }
    
    public String toString() {
        return new StringBuilder().append(this.count).append(",").append(this.usage.getName()).append(",").append(this.type.getName()).toString();
    }
    
    public final int getByteSize() {
        return this.byteSize;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final VertexFormatElement dfp3 = (VertexFormatElement)object;
        return this.count == dfp3.count && this.index == dfp3.index && this.type == dfp3.type && this.usage == dfp3.usage;
    }
    
    public int hashCode() {
        int integer2 = this.type.hashCode();
        integer2 = 31 * integer2 + this.usage.hashCode();
        integer2 = 31 * integer2 + this.index;
        integer2 = 31 * integer2 + this.count;
        return integer2;
    }
    
    public void setupBufferState(final long long1, final int integer) {
        this.usage.setupBufferState(this.count, this.type.getGlType(), integer, long1, this.index);
    }
    
    public void clearBufferState() {
        this.usage.clearBufferState(this.index);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public enum Usage {
        POSITION("Position", (integer1, integer2, integer3, long4, integer5) -> {
            GlStateManager._vertexPointer(integer1, integer2, integer3, long4);
            GlStateManager._enableClientState(32884);
            return;
        }, integer -> GlStateManager._disableClientState(32884)), 
        NORMAL("Normal", (integer1, integer2, integer3, long4, integer5) -> {
            GlStateManager._normalPointer(integer2, integer3, long4);
            GlStateManager._enableClientState(32885);
            return;
        }, integer -> GlStateManager._disableClientState(32885)), 
        COLOR("Vertex Color", (integer1, integer2, integer3, long4, integer5) -> {
            GlStateManager._colorPointer(integer1, integer2, integer3, long4);
            GlStateManager._enableClientState(32886);
            return;
        }, integer -> {
            GlStateManager._disableClientState(32886);
            GlStateManager._clearCurrentColor();
        }), 
        UV("UV", (integer1, integer2, integer3, long4, integer5) -> {
            GlStateManager._glClientActiveTexture(33984 + integer5);
            GlStateManager._texCoordPointer(integer1, integer2, integer3, long4);
            GlStateManager._enableClientState(32888);
            GlStateManager._glClientActiveTexture(33984);
            return;
        }, integer -> {
            GlStateManager._glClientActiveTexture(33984 + integer);
            GlStateManager._disableClientState(32888);
            GlStateManager._glClientActiveTexture(33984);
        }), 
        PADDING("Padding", (integer1, integer2, integer3, long4, integer5) -> {}, integer -> {}), 
        GENERIC("Generic", (integer1, integer2, integer3, long4, integer5) -> {
            GlStateManager._enableVertexAttribArray(integer5);
            GlStateManager._vertexAttribPointer(integer5, integer1, integer2, false, integer3, long4);
            return;
        }, GlStateManager::_disableVertexAttribArray);
        
        private final String name;
        private final SetupState setupState;
        private final IntConsumer clearState;
        
        private Usage(final String string3, final SetupState a, final IntConsumer intConsumer) {
            this.name = string3;
            this.setupState = a;
            this.clearState = intConsumer;
        }
        
        private void setupBufferState(final int integer1, final int integer2, final int integer3, final long long4, final int integer5) {
            this.setupState.setupBufferState(integer1, integer2, integer3, long4, integer5);
        }
        
        public void clearBufferState(final int integer) {
            this.clearState.accept(integer);
        }
        
        public String getName() {
            return this.name;
        }
        
        interface SetupState {
            void setupBufferState(final int integer1, final int integer2, final int integer3, final long long4, final int integer5);
        }
    }
    
    public enum Type {
        FLOAT(4, "Float", 5126), 
        UBYTE(1, "Unsigned Byte", 5121), 
        BYTE(1, "Byte", 5120), 
        USHORT(2, "Unsigned Short", 5123), 
        SHORT(2, "Short", 5122), 
        UINT(4, "Unsigned Int", 5125), 
        INT(4, "Int", 5124);
        
        private final int size;
        private final String name;
        private final int glType;
        
        private Type(final int integer3, final String string4, final int integer5) {
            this.size = integer3;
            this.name = string4;
            this.glType = integer5;
        }
        
        public int getSize() {
            return this.size;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getGlType() {
            return this.glType;
        }
    }
}
