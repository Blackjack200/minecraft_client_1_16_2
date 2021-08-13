package net.minecraft.client.renderer;

public class RunningTrimmedMean {
    private final long[] values;
    private int count;
    private int cursor;
    
    public RunningTrimmedMean(final int integer) {
        this.values = new long[integer];
    }
    
    public long registerValueAndGetMean(final long long1) {
        if (this.count < this.values.length) {
            ++this.count;
        }
        this.values[this.cursor] = long1;
        this.cursor = (this.cursor + 1) % this.values.length;
        long long2 = Long.MAX_VALUE;
        long long3 = Long.MIN_VALUE;
        long long4 = 0L;
        for (int integer10 = 0; integer10 < this.count; ++integer10) {
            final long long5 = this.values[integer10];
            long4 += long5;
            long2 = Math.min(long2, long5);
            long3 = Math.max(long3, long5);
        }
        if (this.count > 2) {
            long4 -= long2 + long3;
            return long4 / (this.count - 2);
        }
        if (long4 > 0L) {
            return this.count / long4;
        }
        return 0L;
    }
}
