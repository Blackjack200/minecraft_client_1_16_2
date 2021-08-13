package net.minecraft.world.level.timers;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Collections;
import java.util.Set;
import java.util.Collection;
import com.google.common.collect.HashBasedTable;
import java.util.PriorityQueue;
import net.minecraft.nbt.Tag;
import com.mojang.serialization.Dynamic;
import java.util.stream.Stream;
import java.util.Comparator;
import com.google.common.collect.Table;
import com.google.common.primitives.UnsignedLong;
import java.util.Queue;
import org.apache.logging.log4j.Logger;

public class TimerQueue<T> {
    private static final Logger LOGGER;
    private final TimerCallbacks<T> callbacksRegistry;
    private final Queue<Event<T>> queue;
    private UnsignedLong sequentialId;
    private final Table<String, Long, Event<T>> events;
    
    private static <T> Comparator<Event<T>> createComparator() {
        return (Comparator<Event<T>>)Comparator.comparingLong(a -> a.triggerTime).thenComparing(a -> a.sequentialId);
    }
    
    public TimerQueue(final TimerCallbacks<T> dcb, final Stream<Dynamic<Tag>> stream) {
        this(dcb);
        this.queue.clear();
        this.events.clear();
        this.sequentialId = UnsignedLong.ZERO;
        stream.forEach(dynamic -> {
            if (!(dynamic.getValue() instanceof CompoundTag)) {
                TimerQueue.LOGGER.warn("Invalid format of events: {}", dynamic);
                return;
            }
            this.loadEvent((CompoundTag)dynamic.getValue());
        });
    }
    
    public TimerQueue(final TimerCallbacks<T> dcb) {
        this.queue = (Queue<Event<T>>)new PriorityQueue((Comparator)TimerQueue.createComparator());
        this.sequentialId = UnsignedLong.ZERO;
        this.events = (Table<String, Long, Event<T>>)HashBasedTable.create();
        this.callbacksRegistry = dcb;
    }
    
    public void tick(final T object, final long long2) {
        while (true) {
            final Event<T> a5 = (Event<T>)this.queue.peek();
            if (a5 == null || a5.triggerTime > long2) {
                break;
            }
            this.queue.remove();
            this.events.remove(a5.id, long2);
            a5.callback.handle(object, this, long2);
        }
    }
    
    public void schedule(final String string, final long long2, final TimerCallback<T> dca) {
        if (this.events.contains(string, long2)) {
            return;
        }
        this.sequentialId = this.sequentialId.plus(UnsignedLong.ONE);
        final Event<T> a6 = new Event<T>(long2, this.sequentialId, string, (TimerCallback)dca);
        this.events.put(string, long2, a6);
        this.queue.add(a6);
    }
    
    public int remove(final String string) {
        final Collection<Event<T>> collection3 = (Collection<Event<T>>)this.events.row(string).values();
        collection3.forEach(this.queue::remove);
        final int integer4 = collection3.size();
        collection3.clear();
        return integer4;
    }
    
    public Set<String> getEventsIds() {
        return (Set<String>)Collections.unmodifiableSet(this.events.rowKeySet());
    }
    
    private void loadEvent(final CompoundTag md) {
        final CompoundTag md2 = md.getCompound("Callback");
        final TimerCallback<T> dca4 = this.callbacksRegistry.deserialize(md2);
        if (dca4 != null) {
            final String string5 = md.getString("Name");
            final long long6 = md.getLong("TriggerTime");
            this.schedule(string5, long6, dca4);
        }
    }
    
    private CompoundTag storeEvent(final Event<T> a) {
        final CompoundTag md3 = new CompoundTag();
        md3.putString("Name", a.id);
        md3.putLong("TriggerTime", a.triggerTime);
        md3.put("Callback", (Tag)this.callbacksRegistry.<TimerCallback<T>>serialize(a.callback));
        return md3;
    }
    
    public ListTag store() {
        final ListTag mj2 = new ListTag();
        this.queue.stream().sorted((Comparator)TimerQueue.createComparator()).map(this::storeEvent).forEach(mj2::add);
        return mj2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Event<T> {
        public final long triggerTime;
        public final UnsignedLong sequentialId;
        public final String id;
        public final TimerCallback<T> callback;
        
        private Event(final long long1, final UnsignedLong unsignedLong, final String string, final TimerCallback<T> dca) {
            this.triggerTime = long1;
            this.sequentialId = unsignedLong;
            this.id = string;
            this.callback = dca;
        }
    }
}
