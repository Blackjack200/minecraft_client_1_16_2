package net.minecraft.util.thread;

import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.ints.Int2BooleanFunction;
import java.util.concurrent.RejectedExecutionException;
import net.minecraft.SharedConstants;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;

public class ProcessorMailbox<T> implements ProcessorHandle<T>, AutoCloseable, Runnable {
    private static final Logger LOGGER;
    private final AtomicInteger status;
    public final StrictQueue<? super T, ? extends Runnable> queue;
    private final Executor dispatcher;
    private final String name;
    
    public static ProcessorMailbox<Runnable> create(final Executor executor, final String string) {
        return new ProcessorMailbox<Runnable>(new StrictQueue.QueueStrictQueue((java.util.Queue<Object>)new ConcurrentLinkedQueue()), executor, string);
    }
    
    public ProcessorMailbox(final StrictQueue<? super T, ? extends Runnable> aod, final Executor executor, final String string) {
        this.status = new AtomicInteger(0);
        this.dispatcher = executor;
        this.queue = aod;
        this.name = string;
    }
    
    private boolean setAsScheduled() {
        int integer2;
        do {
            integer2 = this.status.get();
            if ((integer2 & 0x3) != 0x0) {
                return false;
            }
        } while (!this.status.compareAndSet(integer2, integer2 | 0x2));
        return true;
    }
    
    private void setAsIdle() {
        int integer2;
        do {
            integer2 = this.status.get();
        } while (!this.status.compareAndSet(integer2, integer2 & 0xFFFFFFFD));
    }
    
    private boolean canBeScheduled() {
        return (this.status.get() & 0x1) == 0x0 && !this.queue.isEmpty();
    }
    
    public void close() {
        int integer2;
        do {
            integer2 = this.status.get();
        } while (!this.status.compareAndSet(integer2, integer2 | 0x1));
    }
    
    private boolean shouldProcess() {
        return (this.status.get() & 0x2) != 0x0;
    }
    
    private boolean pollTask() {
        if (!this.shouldProcess()) {
            return false;
        }
        final Runnable runnable2 = (Runnable)this.queue.pop();
        if (runnable2 == null) {
            return false;
        }
        Thread thread4;
        String string3;
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            thread4 = Thread.currentThread();
            string3 = thread4.getName();
            thread4.setName(this.name);
        }
        else {
            thread4 = null;
            string3 = null;
        }
        runnable2.run();
        if (thread4 != null) {
            thread4.setName(string3);
        }
        return true;
    }
    
    public void run() {
        try {
            this.pollUntil(integer -> integer == 0);
        }
        finally {
            this.setAsIdle();
            this.registerForExecution();
        }
    }
    
    public void tell(final T object) {
        this.queue.push(object);
        this.registerForExecution();
    }
    
    private void registerForExecution() {
        if (this.canBeScheduled() && this.setAsScheduled()) {
            try {
                this.dispatcher.execute((Runnable)this);
            }
            catch (RejectedExecutionException rejectedExecutionException4) {
                try {
                    this.dispatcher.execute((Runnable)this);
                }
                catch (RejectedExecutionException rejectedExecutionException3) {
                    ProcessorMailbox.LOGGER.error("Cound not schedule mailbox", (Throwable)rejectedExecutionException3);
                }
            }
        }
    }
    
    private int pollUntil(final Int2BooleanFunction int2BooleanFunction) {
        int integer3;
        for (integer3 = 0; int2BooleanFunction.get(integer3) && this.pollTask(); ++integer3) {}
        return integer3;
    }
    
    public String toString() {
        return this.name + " " + this.status.get() + " " + this.queue.isEmpty();
    }
    
    public String name() {
        return this.name;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
