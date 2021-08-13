package net.minecraft.world.level.chunk;

public class OldDataLayer {
    public final byte[] data;
    private final int depthBits;
    private final int depthBitsPlusFour;
    
    public OldDataLayer(final byte[] arr, final int integer) {
        this.data = arr;
        this.depthBits = integer;
        this.depthBitsPlusFour = integer + 4;
    }
    
    public int get(final int integer1, final int integer2, final int integer3) {
        final int integer4 = integer1 << this.depthBitsPlusFour | integer3 << this.depthBits | integer2;
        final int integer5 = integer4 >> 1;
        final int integer6 = integer4 & 0x1;
        if (integer6 == 0) {
            return this.data[integer5] & 0xF;
        }
        return this.data[integer5] >> 4 & 0xF;
    }
}
