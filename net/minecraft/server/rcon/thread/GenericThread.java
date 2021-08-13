package net.minecraft.server.rcon.thread;

import org.apache.logging.log4j.LogManager;
import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;

public abstract class GenericThread implements Runnable {
    private static final Logger LOGGER;
    private static final AtomicInteger UNIQUE_THREAD_ID;
    protected volatile boolean running;
    protected final String name;
    @Nullable
    protected Thread thread;
    
    protected GenericThread(final String string) {
        this.name = string;
    }
    
    public synchronized boolean start() {
        if (this.running) {
            return true;
        }
        this.running = true;
        (this.thread = new Thread((Runnable)this, this.name + " #" + GenericThread.UNIQUE_THREAD_ID.incrementAndGet())).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandlerWithName(GenericThread.LOGGER));
        this.thread.start();
        GenericThread.LOGGER.info("Thread {} started", this.name);
        return true;
    }
    
    public synchronized void stop() {
        this.running = false;
        if (null == this.thread) {
            return;
        }
        int integer2 = 0;
        while (this.thread.isAlive()) {
            try {
                this.thread.join(1000L);
                if (++integer2 >= 5) {
                    GenericThread.LOGGER.warn("Waited {} seconds attempting force stop!", integer2);
                }
                else {
                    if (!this.thread.isAlive()) {
                        continue;
                    }
                    GenericThread.LOGGER.warn("Thread {} ({}) failed to exit after {} second(s)", this, this.thread.getState(), integer2, new Exception("Stack:"));
                    this.thread.interrupt();
                }
            }
            catch (InterruptedException ex) {}
        }
        GenericThread.LOGGER.info("Thread {} stopped", this.name);
        this.thread = null;
    }
    
    public boolean isRunning() {
        return this.running;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        UNIQUE_THREAD_ID = new AtomicInteger(0);
    }
}
