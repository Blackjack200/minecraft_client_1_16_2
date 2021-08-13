package net.minecraft.util.datafix;

import org.apache.commons.lang3.Validate;
import net.minecraft.util.Mth;

public class PackedBitStorage {
    private final long[] data;
    private final int bits;
    private final long mask;
    private final int size;
    
    public PackedBitStorage(final int integer1, final int integer2) {
        this(integer1, integer2, new long[Mth.roundUp(integer2 * integer1, 64) / 64]);
    }
    
    public PackedBitStorage(final int integer1, final int integer2, final long[] arr) {
        Validate.inclusiveBetween(1L, 32L, (long)integer1);
        this.size = integer2;
        this.bits = integer1;
        this.data = arr;
        this.mask = (1L << integer1) - 1L;
        final int integer3 = Mth.roundUp(integer2 * integer1, 64) / 64;
        if (arr.length != integer3) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid length given for storage, got: ").append(arr.length).append(" but expected: ").append(integer3).toString());
        }
    }
    
    public void set(final int integer1, final int integer2) {
        Validate.inclusiveBetween(0L, (long)(this.size - 1), (long)integer1);
        Validate.inclusiveBetween(0L, this.mask, (long)integer2);
        final int integer3 = integer1 * this.bits;
        final int integer4 = integer3 >> 6;
        final int integer5 = (integer1 + 1) * this.bits - 1 >> 6;
        final int integer6 = integer3 ^ integer4 << 6;
        this.data[integer4] = ((this.data[integer4] & ~(this.mask << integer6)) | ((long)integer2 & this.mask) << integer6);
        if (integer4 != integer5) {
            final int integer7 = 64 - integer6;
            final int integer8 = this.bits - integer7;
            this.data[integer5] = (this.data[integer5] >>> integer8 << integer8 | ((long)integer2 & this.mask) >> integer7);
        }
    }
    
    public int get(final int integer) {
        Validate.inclusiveBetween(0L, (long)(this.size - 1), (long)integer);
        final int integer2 = integer * this.bits;
        final int integer3 = integer2 >> 6;
        final int integer4 = (integer + 1) * this.bits - 1 >> 6;
        final int integer5 = integer2 ^ integer3 << 6;
        if (integer3 == integer4) {
            return (int)(this.data[integer3] >>> integer5 & this.mask);
        }
        final int integer6 = 64 - integer5;
        return (int)((this.data[integer3] >>> integer5 | this.data[integer4] << integer6) & this.mask);
    }
    
    public long[] getRaw() {
        return this.data;
    }
    
    public int getBits() {
        return this.bits;
    }
}
