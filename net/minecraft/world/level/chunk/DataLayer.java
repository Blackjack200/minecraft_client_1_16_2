package net.minecraft.world.level.chunk;

import net.minecraft.Util;
import javax.annotation.Nullable;

public class DataLayer {
    @Nullable
    protected byte[] data;
    
    public DataLayer() {
    }
    
    public DataLayer(final byte[] arr) {
        this.data = arr;
        if (arr.length != 2048) {
            throw Util.<IllegalArgumentException>pauseInIde(new IllegalArgumentException(new StringBuilder().append("ChunkNibbleArrays should be 2048 bytes not: ").append(arr.length).toString()));
        }
    }
    
    protected DataLayer(final int integer) {
        this.data = new byte[integer];
    }
    
    public int get(final int integer1, final int integer2, final int integer3) {
        return this.get(this.getIndex(integer1, integer2, integer3));
    }
    
    public void set(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.set(this.getIndex(integer1, integer2, integer3), integer4);
    }
    
    protected int getIndex(final int integer1, final int integer2, final int integer3) {
        return integer2 << 8 | integer3 << 4 | integer1;
    }
    
    private int get(final int integer) {
        if (this.data == null) {
            return 0;
        }
        final int integer2 = this.getPosition(integer);
        if (this.isFirst(integer)) {
            return this.data[integer2] & 0xF;
        }
        return this.data[integer2] >> 4 & 0xF;
    }
    
    private void set(final int integer1, final int integer2) {
        if (this.data == null) {
            this.data = new byte[2048];
        }
        final int integer3 = this.getPosition(integer1);
        if (this.isFirst(integer1)) {
            this.data[integer3] = (byte)((this.data[integer3] & 0xF0) | (integer2 & 0xF));
        }
        else {
            this.data[integer3] = (byte)((this.data[integer3] & 0xF) | (integer2 & 0xF) << 4);
        }
    }
    
    private boolean isFirst(final int integer) {
        return (integer & 0x1) == 0x0;
    }
    
    private int getPosition(final int integer) {
        return integer >> 1;
    }
    
    public byte[] getData() {
        if (this.data == null) {
            this.data = new byte[2048];
        }
        return this.data;
    }
    
    public DataLayer copy() {
        if (this.data == null) {
            return new DataLayer();
        }
        return new DataLayer(this.data.clone());
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        for (int integer3 = 0; integer3 < 4096; ++integer3) {
            stringBuilder2.append(Integer.toHexString(this.get(integer3)));
            if ((integer3 & 0xF) == 0xF) {
                stringBuilder2.append("\n");
            }
            if ((integer3 & 0xFF) == 0xFF) {
                stringBuilder2.append("\n");
            }
        }
        return stringBuilder2.toString();
    }
    
    public boolean isEmpty() {
        return this.data == null;
    }
}
