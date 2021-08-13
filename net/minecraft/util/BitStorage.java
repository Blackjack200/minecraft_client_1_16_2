package net.minecraft.util;

import java.util.function.IntConsumer;
import net.minecraft.Util;
import org.apache.commons.lang3.Validate;
import javax.annotation.Nullable;

public class BitStorage {
    private static final int[] MAGIC;
    private final long[] data;
    private final int bits;
    private final long mask;
    private final int size;
    private final int valuesPerLong;
    private final int divideMul;
    private final int divideAdd;
    private final int divideShift;
    
    public BitStorage(final int integer1, final int integer2) {
        this(integer1, integer2, null);
    }
    
    public BitStorage(final int integer1, final int integer2, @Nullable final long[] arr) {
        Validate.inclusiveBetween(1L, 32L, (long)integer1);
        this.size = integer2;
        this.bits = integer1;
        this.mask = (1L << integer1) - 1L;
        this.valuesPerLong = (char)(64 / integer1);
        final int integer3 = 3 * (this.valuesPerLong - 1);
        this.divideMul = BitStorage.MAGIC[integer3 + 0];
        this.divideAdd = BitStorage.MAGIC[integer3 + 1];
        this.divideShift = BitStorage.MAGIC[integer3 + 2];
        final int integer4 = (integer2 + this.valuesPerLong - 1) / this.valuesPerLong;
        if (arr != null) {
            if (arr.length != integer4) {
                throw Util.<RuntimeException>pauseInIde(new RuntimeException(new StringBuilder().append("Invalid length given for storage, got: ").append(arr.length).append(" but expected: ").append(integer4).toString()));
            }
            this.data = arr;
        }
        else {
            this.data = new long[integer4];
        }
    }
    
    private int cellIndex(final int integer) {
        final long long3 = Integer.toUnsignedLong(this.divideMul);
        final long long4 = Integer.toUnsignedLong(this.divideAdd);
        return (int)(integer * long3 + long4 >> 32 >> this.divideShift);
    }
    
    public int getAndSet(final int integer1, final int integer2) {
        Validate.inclusiveBetween(0L, (long)(this.size - 1), (long)integer1);
        Validate.inclusiveBetween(0L, this.mask, (long)integer2);
        final int integer3 = this.cellIndex(integer1);
        final long long5 = this.data[integer3];
        final int integer4 = (integer1 - integer3 * this.valuesPerLong) * this.bits;
        final int integer5 = (int)(long5 >> integer4 & this.mask);
        this.data[integer3] = ((long5 & ~(this.mask << integer4)) | ((long)integer2 & this.mask) << integer4);
        return integer5;
    }
    
    public void set(final int integer1, final int integer2) {
        Validate.inclusiveBetween(0L, (long)(this.size - 1), (long)integer1);
        Validate.inclusiveBetween(0L, this.mask, (long)integer2);
        final int integer3 = this.cellIndex(integer1);
        final long long5 = this.data[integer3];
        final int integer4 = (integer1 - integer3 * this.valuesPerLong) * this.bits;
        this.data[integer3] = ((long5 & ~(this.mask << integer4)) | ((long)integer2 & this.mask) << integer4);
    }
    
    public int get(final int integer) {
        Validate.inclusiveBetween(0L, (long)(this.size - 1), (long)integer);
        final int integer2 = this.cellIndex(integer);
        final long long4 = this.data[integer2];
        final int integer3 = (integer - integer2 * this.valuesPerLong) * this.bits;
        return (int)(long4 >> integer3 & this.mask);
    }
    
    public long[] getRaw() {
        return this.data;
    }
    
    public int getSize() {
        return this.size;
    }
    
    public void getAll(final IntConsumer intConsumer) {
        int integer3 = 0;
        for (long long7 : this.data) {
            for (int integer4 = 0; integer4 < this.valuesPerLong; ++integer4) {
                intConsumer.accept((int)(long7 & this.mask));
                long7 >>= this.bits;
                if (++integer3 >= this.size) {
                    return;
                }
            }
        }
    }
    
    static {
        MAGIC = new int[] { -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE, 0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756, 0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0, 390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378, 306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135, 0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0, 204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970, 178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862, 0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567, 126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0, 104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE, 0, 5 };
    }
}
