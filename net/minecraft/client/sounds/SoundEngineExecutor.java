package net.minecraft.client.sounds;

import java.util.concurrent.locks.LockSupport;
import net.minecraft.util.thread.BlockableEventLoop;

public class SoundEngineExecutor extends BlockableEventLoop<Runnable> {
    private Thread thread;
    private volatile boolean shutdown;
    
    public SoundEngineExecutor() {
        super("Sound executor");
        this.thread = this.createThread();
    }
    
    private Thread createThread() {
        final Thread thread2 = new Thread(this::run);
        thread2.setDaemon(true);
        thread2.setName("Sound engine");
        thread2.start();
        return thread2;
    }
    
    @Override
    protected Runnable wrapRunnable(final Runnable runnable) {
        return runnable;
    }
    
    @Override
    protected boolean shouldRun(final Runnable runnable) {
        return !this.shutdown;
    }
    
    @Override
    protected Thread getRunningThread() {
        return this.thread;
    }
    
    private void run() {
        while (!this.shutdown) {
            this.managedBlock(() -> this.shutdown);
        }
    }
    
    @Override
    protected void waitForTasks() {
        LockSupport.park("waiting for tasks");
    }
    
    public void flush() {
        this.shutdown = true;
        this.thread.interrupt();
        try {
            this.thread.join();
        }
        catch (InterruptedException interruptedException2) {
            Thread.currentThread().interrupt();
        }
        this.dropAllTasks();
        this.shutdown = false;
        this.thread = this.createThread();
    }
}
