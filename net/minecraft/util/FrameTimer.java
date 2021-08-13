package net.minecraft.util;

public class FrameTimer {
    private final long[] loggedTimes;
    private int logStart;
    private int logLength;
    private int logEnd;
    
    public FrameTimer() {
        this.loggedTimes = new long[240];
    }
    
    public void logFrameDuration(final long long1) {
        this.loggedTimes[this.logEnd] = long1;
        ++this.logEnd;
        if (this.logEnd == 240) {
            this.logEnd = 0;
        }
        if (this.logLength < 240) {
            this.logStart = 0;
            ++this.logLength;
        }
        else {
            this.logStart = this.wrapIndex(this.logEnd + 1);
        }
    }
    
    public int scaleSampleTo(final long long1, final int integer2, final int integer3) {
        final double double6 = long1 / (double)(1000000000L / integer3);
        return (int)(double6 * integer2);
    }
    
    public int getLogStart() {
        return this.logStart;
    }
    
    public int getLogEnd() {
        return this.logEnd;
    }
    
    public int wrapIndex(final int integer) {
        return integer % 240;
    }
    
    public long[] getLog() {
        return this.loggedTimes;
    }
}
