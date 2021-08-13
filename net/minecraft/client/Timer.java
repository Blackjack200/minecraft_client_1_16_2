package net.minecraft.client;

public class Timer {
    public float partialTick;
    public float tickDelta;
    private long lastMs;
    private final float msPerTick;
    
    public Timer(final float float1, final long long2) {
        this.msPerTick = 1000.0f / float1;
        this.lastMs = long2;
    }
    
    public int advanceTime(final long long1) {
        this.tickDelta = (long1 - this.lastMs) / this.msPerTick;
        this.lastMs = long1;
        this.partialTick += this.tickDelta;
        final int integer4 = (int)this.partialTick;
        this.partialTick -= integer4;
        return integer4;
    }
}
