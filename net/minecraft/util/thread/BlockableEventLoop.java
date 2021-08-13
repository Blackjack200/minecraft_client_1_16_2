package net.minecraft.util.thread;

import org.apache.logging.log4j.LogManager;
import java.util.function.BooleanSupplier;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import com.google.common.collect.Queues;
import java.util.Queue;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.Executor;

public abstract class BlockableEventLoop<R extends Runnable> implements ProcessorHandle<R>, Executor {
    private final String name;
    private static final Logger LOGGER;
    private final Queue<R> pendingRunnables;
    private int blockingCount;
    
    protected BlockableEventLoop(final String string) {
        this.pendingRunnables = (Queue<R>)Queues.newConcurrentLinkedQueue();
        this.name = string;
    }
    
    protected abstract R wrapRunnable(final Runnable runnable);
    
    protected abstract boolean shouldRun(final R runnable);
    
    public boolean isSameThread() {
        return Thread.currentThread() == this.getRunningThread();
    }
    
    protected abstract Thread getRunningThread();
    
    protected boolean scheduleExecutables() {
        return !this.isSameThread();
    }
    
    public int getPendingTasksCount() {
        return this.pendingRunnables.size();
    }
    
    public String name() {
        return this.name;
    }
    
    public <V> CompletableFuture<V> submit(final Supplier<V> supplier) {
        if (this.scheduleExecutables()) {
            return (CompletableFuture<V>)CompletableFuture.supplyAsync((Supplier)supplier, (Executor)this);
        }
        return (CompletableFuture<V>)CompletableFuture.completedFuture(supplier.get());
    }
    
    private CompletableFuture<Void> submitAsync(final Runnable runnable) {
        return (CompletableFuture<Void>)CompletableFuture.supplyAsync(() -> {
            runnable.run();
            return null;
        }, (Executor)this);
    }
    
    public CompletableFuture<Void> submit(final Runnable runnable) {
        if (this.scheduleExecutables()) {
            return this.submitAsync(runnable);
        }
        runnable.run();
        return (CompletableFuture<Void>)CompletableFuture.completedFuture(null);
    }
    
    public void executeBlocking(final Runnable runnable) {
        if (!this.isSameThread()) {
            this.submitAsync(runnable).join();
        }
        else {
            runnable.run();
        }
    }
    
    public void tell(final R runnable) {
        this.pendingRunnables.add(runnable);
        LockSupport.unpark(this.getRunningThread());
    }
    
    public void execute(final Runnable runnable) {
        if (this.scheduleExecutables()) {
            this.tell(this.wrapRunnable(runnable));
        }
        else {
            runnable.run();
        }
    }
    
    protected void dropAllTasks() {
        this.pendingRunnables.clear();
    }
    
    protected void runAllTasks() {
        while (this.pollTask()) {}
    }
    
    protected boolean pollTask() {
        final R runnable2 = (R)this.pendingRunnables.peek();
        if (runnable2 == null) {
            return false;
        }
        if (this.blockingCount == 0 && !this.shouldRun(runnable2)) {
            return false;
        }
        this.doRunTask((Runnable)this.pendingRunnables.remove());
        return true;
    }
    
    public void managedBlock(final BooleanSupplier booleanSupplier) {
        ++this.blockingCount;
        try {
            while (!booleanSupplier.getAsBoolean()) {
                if (!this.pollTask()) {
                    this.waitForTasks();
                }
            }
        }
        finally {
            --this.blockingCount;
        }
    }
    
    protected void waitForTasks() {
        Thread.yield();
        LockSupport.parkNanos("waiting for tasks", 100000L);
    }
    
    protected void doRunTask(final R runnable) {
        try {
            runnable.run();
        }
        catch (Exception exception3) {
            BlockableEventLoop.LOGGER.fatal("Error executing task on {}", this.name(), exception3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
