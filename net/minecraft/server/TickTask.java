package net.minecraft.server;

public class TickTask implements Runnable {
    private final int tick;
    private final Runnable runnable;
    
    public TickTask(final int integer, final Runnable runnable) {
        this.tick = integer;
        this.runnable = runnable;
    }
    
    public int getTick() {
        return this.tick;
    }
    
    public void run() {
        this.runnable.run();
    }
}
