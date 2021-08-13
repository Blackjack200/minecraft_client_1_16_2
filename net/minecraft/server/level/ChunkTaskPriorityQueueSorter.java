package net.minecraft.server.level;

import org.apache.logging.log4j.LogManager;
import java.util.function.Consumer;
import java.util.Optional;
import com.mojang.datafixers.util.Either;
import java.util.stream.Stream;
import java.util.concurrent.CompletableFuture;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.Util;
import java.util.function.IntConsumer;
import net.minecraft.world.level.ChunkPos;
import java.util.function.IntSupplier;
import com.google.common.collect.Sets;
import java.util.stream.Collectors;
import java.util.concurrent.Executor;
import java.util.List;
import net.minecraft.util.thread.StrictQueue;
import net.minecraft.util.thread.ProcessorMailbox;
import java.util.Set;
import net.minecraft.util.Unit;
import java.util.function.Function;
import net.minecraft.util.thread.ProcessorHandle;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ChunkTaskPriorityQueueSorter implements AutoCloseable, ChunkHolder.LevelChangeListener {
    private static final Logger LOGGER;
    private final Map<ProcessorHandle<?>, ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>> queues;
    private final Set<ProcessorHandle<?>> sleeping;
    private final ProcessorMailbox<StrictQueue.IntRunnable> mailbox;
    
    public ChunkTaskPriorityQueueSorter(final List<ProcessorHandle<?>> list, final Executor executor, final int integer) {
        this.queues = (Map<ProcessorHandle<?>, ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>>>)list.stream().collect(Collectors.toMap(Function.identity(), aoa -> new ChunkTaskPriorityQueue(aoa.name() + "_queue", integer)));
        this.sleeping = (Set<ProcessorHandle<?>>)Sets.newHashSet((Iterable)list);
        this.mailbox = new ProcessorMailbox<StrictQueue.IntRunnable>(new StrictQueue.FixedPriorityQueue(4), executor, "sorter");
    }
    
    public static Message<Runnable> message(final Runnable runnable, final long long2, final IntSupplier intSupplier) {
        return new Message<Runnable>(aoa -> () -> {
            runnable.run();
            aoa.tell(Unit.INSTANCE);
        }, long2, intSupplier);
    }
    
    public static Message<Runnable> message(final ChunkHolder zr, final Runnable runnable) {
        return message(runnable, zr.getPos().toLong(), zr::getQueueLevel);
    }
    
    public static Release release(final Runnable runnable, final long long2, final boolean boolean3) {
        return new Release(runnable, long2, boolean3);
    }
    
    public <T> ProcessorHandle<Message<T>> getProcessor(final ProcessorHandle<T> aoa, final boolean boolean2) {
        return (ProcessorHandle<Message<T>>)this.mailbox.ask((java.util.function.Function<? super ProcessorHandle<Object>, ?>)(aoa3 -> new StrictQueue.IntRunnable(0, () -> {
            this.getQueue((ProcessorHandle<Object>)aoa);
            aoa3.tell(ProcessorHandle.of("chunk priority sorter around " + aoa.name(), (java.util.function.Consumer<Object>)(a -> this.submit(aoa, (java.util.function.Function<ProcessorHandle<Unit>, Object>)a.task, a.pos, a.level, boolean2))));
        }))).join();
    }
    
    public ProcessorHandle<Release> getReleaseProcessor(final ProcessorHandle<Runnable> aoa) {
        return (ProcessorHandle<Release>)this.mailbox.ask((java.util.function.Function<? super ProcessorHandle<Object>, ?>)(aoa2 -> new StrictQueue.IntRunnable(0, () -> aoa2.tell(ProcessorHandle.of("chunk priority sorter around " + aoa.name(), (java.util.function.Consumer<Object>)(b -> this.release((ProcessorHandle<Object>)aoa, b.pos, b.task, b.clearQueue))))))).join();
    }
    
    public void onLevelChange(final ChunkPos bra, final IntSupplier intSupplier, final int integer, final IntConsumer intConsumer) {
        this.mailbox.tell(new StrictQueue.IntRunnable(0, () -> {
            final int integer2 = intSupplier.getAsInt();
            this.queues.values().forEach(zt -> zt.resortChunkTasks(integer2, bra, integer));
            intConsumer.accept(integer);
        }));
    }
    
    private <T> void release(final ProcessorHandle<T> aoa, final long long2, final Runnable runnable, final boolean boolean4) {
        this.mailbox.tell(new StrictQueue.IntRunnable(1, () -> {
            final ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> zt7 = this.<T>getQueue((ProcessorHandle<T>)aoa);
            zt7.release(long2, boolean4);
            if (this.sleeping.remove(aoa)) {
                this.<T>pollTask(zt7, aoa);
            }
            runnable.run();
        }));
    }
    
    private <T> void submit(final ProcessorHandle<T> aoa, final Function<ProcessorHandle<Unit>, T> function, final long long3, final IntSupplier intSupplier, final boolean boolean5) {
        this.mailbox.tell(new StrictQueue.IntRunnable(2, () -> {
            final ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> zt8 = this.<T>getQueue((ProcessorHandle<T>)aoa);
            final int integer9 = intSupplier.getAsInt();
            zt8.submit((java.util.Optional<Function<ProcessorHandle<Unit>, T>>)Optional.of(function), long3, integer9);
            if (boolean5) {
                zt8.submit((java.util.Optional<Function<ProcessorHandle<Unit>, T>>)Optional.empty(), long3, integer9);
            }
            if (this.sleeping.remove(aoa)) {
                this.<T>pollTask(zt8, aoa);
            }
        }));
    }
    
    private <T> void pollTask(final ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> zt, final ProcessorHandle<T> aoa) {
        this.mailbox.tell(new StrictQueue.IntRunnable(3, () -> {
            final Stream<Either<Function<ProcessorHandle<Unit>, T>, Runnable>> stream4 = zt.pop();
            if (stream4 == null) {
                this.sleeping.add(aoa);
            }
            else {
                Util.sequence((java.util.List<? extends java.util.concurrent.CompletableFuture<?>>)stream4.map(either -> (CompletableFuture)either.map(aoa::ask, runnable -> {
                    runnable.run();
                    return CompletableFuture.completedFuture(Unit.INSTANCE);
                })).collect(Collectors.toList())).thenAccept(list -> this.pollTask(zt, (ProcessorHandle<Object>)aoa));
            }
        }));
    }
    
    private <T> ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>> getQueue(final ProcessorHandle<T> aoa) {
        final ChunkTaskPriorityQueue<? extends Function<ProcessorHandle<Unit>, ?>> zt3 = this.queues.get(aoa);
        if (zt3 == null) {
            throw Util.<IllegalArgumentException>pauseInIde(new IllegalArgumentException(new StringBuilder().append("No queue for: ").append(aoa).toString()));
        }
        return (ChunkTaskPriorityQueue<Function<ProcessorHandle<Unit>, T>>)zt3;
    }
    
    @VisibleForTesting
    public String getDebugStatus() {
        return (String)this.queues.entrySet().stream().map(entry -> ((ProcessorHandle)entry.getKey()).name() + "=[" + (String)((ChunkTaskPriorityQueue)entry.getValue()).getAcquired().stream().map(long1 -> new StringBuilder().append(long1).append(":").append(new ChunkPos(long1)).toString()).collect(Collectors.joining(",")) + "]").collect(Collectors.joining(",")) + ", s=" + this.sleeping.size();
    }
    
    public void close() {
        this.queues.keySet().forEach(ProcessorHandle::close);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static final class Message<T> {
        private final Function<ProcessorHandle<Unit>, T> task;
        private final long pos;
        private final IntSupplier level;
        
        private Message(final Function<ProcessorHandle<Unit>, T> function, final long long2, final IntSupplier intSupplier) {
            this.task = function;
            this.pos = long2;
            this.level = intSupplier;
        }
    }
    
    public static final class Release {
        private final Runnable task;
        private final long pos;
        private final boolean clearQueue;
        
        private Release(final Runnable runnable, final long long2, final boolean boolean3) {
            this.task = runnable;
            this.pos = long2;
            this.clearQueue = boolean3;
        }
    }
}
