package net.minecraft.util.thread;

import com.google.common.collect.Queues;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.List;
import java.util.Queue;
import javax.annotation.Nullable;

public interface StrictQueue<T, F> {
    @Nullable
    F pop();
    
    boolean push(final T object);
    
    boolean isEmpty();
    
    public static final class QueueStrictQueue<T> implements StrictQueue<T, T> {
        private final Queue<T> queue;
        
        public QueueStrictQueue(final Queue<T> queue) {
            this.queue = queue;
        }
        
        @Nullable
        public T pop() {
            return (T)this.queue.poll();
        }
        
        public boolean push(final T object) {
            return this.queue.add(object);
        }
        
        public boolean isEmpty() {
            return this.queue.isEmpty();
        }
    }
    
    public static final class IntRunnable implements Runnable {
        private final int priority;
        private final Runnable task;
        
        public IntRunnable(final int integer, final Runnable runnable) {
            this.priority = integer;
            this.task = runnable;
        }
        
        public void run() {
            this.task.run();
        }
        
        public int getPriority() {
            return this.priority;
        }
    }
    
    public static final class FixedPriorityQueue implements StrictQueue<IntRunnable, Runnable> {
        private final List<Queue<Runnable>> queueList;
        
        public FixedPriorityQueue(final int integer) {
            this.queueList = (List<Queue<Runnable>>)IntStream.range(0, integer).mapToObj(integer -> Queues.newConcurrentLinkedQueue()).collect(Collectors.toList());
        }
        
        @Nullable
        public Runnable pop() {
            for (final Queue<Runnable> queue3 : this.queueList) {
                final Runnable runnable4 = (Runnable)queue3.poll();
                if (runnable4 != null) {
                    return runnable4;
                }
            }
            return null;
        }
        
        public boolean push(final IntRunnable b) {
            final int integer3 = b.getPriority();
            ((Queue)this.queueList.get(integer3)).add(b);
            return true;
        }
        
        public boolean isEmpty() {
            return this.queueList.stream().allMatch(Collection::isEmpty);
        }
    }
}
