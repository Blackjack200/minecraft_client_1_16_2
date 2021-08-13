package net.minecraft.server.level;

import com.google.common.collect.Lists;
import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.longs.LongCollection;
import javax.annotation.Nullable;
import com.mojang.datafixers.util.Either;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.level.ChunkPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Optional;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import java.util.List;

public class ChunkTaskPriorityQueue<T> {
    public static final int PRIORITY_LEVEL_COUNT;
    private final List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>> taskQueue;
    private volatile int firstQueue;
    private final String name;
    private final LongSet acquired;
    private final int maxTasks;
    
    public ChunkTaskPriorityQueue(final String string, final int integer) {
        this.taskQueue = (List<Long2ObjectLinkedOpenHashMap<List<Optional<T>>>>)IntStream.range(0, ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT).mapToObj(integer -> new Long2ObjectLinkedOpenHashMap()).collect(Collectors.toList());
        this.firstQueue = ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT;
        this.acquired = (LongSet)new LongOpenHashSet();
        this.name = string;
        this.maxTasks = integer;
    }
    
    protected void resortChunkTasks(final int integer1, final ChunkPos bra, final int integer3) {
        if (integer1 >= ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT) {
            return;
        }
        final Long2ObjectLinkedOpenHashMap<List<Optional<T>>> long2ObjectLinkedOpenHashMap5 = (Long2ObjectLinkedOpenHashMap<List<Optional<T>>>)this.taskQueue.get(integer1);
        final List<Optional<T>> list6 = (List<Optional<T>>)long2ObjectLinkedOpenHashMap5.remove(bra.toLong());
        if (integer1 == this.firstQueue) {
            while (this.firstQueue < ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT && ((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(this.firstQueue)).isEmpty()) {
                ++this.firstQueue;
            }
        }
        if (list6 != null && !list6.isEmpty()) {
            ((List)((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(integer3)).computeIfAbsent(bra.toLong(), long1 -> Lists.newArrayList())).addAll((Collection)list6);
            this.firstQueue = Math.min(this.firstQueue, integer3);
        }
    }
    
    protected void submit(final Optional<T> optional, final long long2, final int integer) {
        ((List)((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(integer)).computeIfAbsent(long2, long1 -> Lists.newArrayList())).add(optional);
        this.firstQueue = Math.min(this.firstQueue, integer);
    }
    
    protected void release(final long long1, final boolean boolean2) {
        for (final Long2ObjectLinkedOpenHashMap<List<Optional<T>>> long2ObjectLinkedOpenHashMap6 : this.taskQueue) {
            final List<Optional<T>> list7 = (List<Optional<T>>)long2ObjectLinkedOpenHashMap6.get(long1);
            if (list7 == null) {
                continue;
            }
            if (boolean2) {
                list7.clear();
            }
            else {
                list7.removeIf(optional -> !optional.isPresent());
            }
            if (!list7.isEmpty()) {
                continue;
            }
            long2ObjectLinkedOpenHashMap6.remove(long1);
        }
        while (this.firstQueue < ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT && ((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(this.firstQueue)).isEmpty()) {
            ++this.firstQueue;
        }
        this.acquired.remove(long1);
    }
    
    private Runnable acquire(final long long1) {
        return () -> this.acquired.add(long1);
    }
    
    @Nullable
    public Stream<Either<T, Runnable>> pop() {
        if (this.acquired.size() >= this.maxTasks) {
            return null;
        }
        if (this.firstQueue < ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT) {
            final int integer2 = this.firstQueue;
            final Long2ObjectLinkedOpenHashMap<List<Optional<T>>> long2ObjectLinkedOpenHashMap3 = (Long2ObjectLinkedOpenHashMap<List<Optional<T>>>)this.taskQueue.get(integer2);
            final long long4 = long2ObjectLinkedOpenHashMap3.firstLongKey();
            final List<Optional<T>> list6 = (List<Optional<T>>)long2ObjectLinkedOpenHashMap3.removeFirst();
            while (this.firstQueue < ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT && ((Long2ObjectLinkedOpenHashMap)this.taskQueue.get(this.firstQueue)).isEmpty()) {
                ++this.firstQueue;
            }
            return (Stream<Either<T, Runnable>>)list6.stream().map(optional -> (Either)optional.map(Either::left).orElseGet(() -> Either.right(this.acquire(long4))));
        }
        return null;
    }
    
    public String toString() {
        return this.name + " " + this.firstQueue + "...";
    }
    
    @VisibleForTesting
    LongSet getAcquired() {
        return (LongSet)new LongOpenHashSet((LongCollection)this.acquired);
    }
    
    static {
        PRIORITY_LEVEL_COUNT = ChunkMap.MAX_CHUNK_DISTANCE + 2;
    }
}
