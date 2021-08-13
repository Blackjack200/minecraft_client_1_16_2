package net.minecraft.world.level.chunk.storage;

import java.util.BitSet;

public class RegionBitmap {
    private final BitSet used;
    
    public RegionBitmap() {
        this.used = new BitSet();
    }
    
    public void force(final int integer1, final int integer2) {
        this.used.set(integer1, integer1 + integer2);
    }
    
    public void free(final int integer1, final int integer2) {
        this.used.clear(integer1, integer1 + integer2);
    }
    
    public int allocate(final int integer) {
        int integer2 = 0;
        int integer3;
        while (true) {
            integer3 = this.used.nextClearBit(integer2);
            final int integer4 = this.used.nextSetBit(integer3);
            if (integer4 == -1 || integer4 - integer3 >= integer) {
                break;
            }
            integer2 = integer4;
        }
        this.force(integer3, integer);
        return integer3;
    }
}
