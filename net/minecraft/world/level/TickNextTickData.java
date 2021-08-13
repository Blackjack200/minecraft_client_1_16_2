package net.minecraft.world.level;

import java.util.Comparator;
import net.minecraft.core.BlockPos;

public class TickNextTickData<T> {
    private static long counter;
    private final T type;
    public final BlockPos pos;
    public final long triggerTick;
    public final TickPriority priority;
    private final long c;
    
    public TickNextTickData(final BlockPos fx, final T object) {
        this(fx, object, 0L, TickPriority.NORMAL);
    }
    
    public TickNextTickData(final BlockPos fx, final T object, final long long3, final TickPriority bsn) {
        this.c = TickNextTickData.counter++;
        this.pos = fx.immutable();
        this.type = object;
        this.triggerTick = long3;
        this.priority = bsn;
    }
    
    public boolean equals(final Object object) {
        if (object instanceof TickNextTickData) {
            final TickNextTickData<?> bsm3 = object;
            return this.pos.equals(bsm3.pos) && this.type == bsm3.type;
        }
        return false;
    }
    
    public int hashCode() {
        return this.pos.hashCode();
    }
    
    public static <T> Comparator<TickNextTickData<T>> createTimeComparator() {
        return (Comparator<TickNextTickData<T>>)Comparator.comparingLong(bsm -> bsm.triggerTick).thenComparing(bsm -> bsm.priority).thenComparingLong(bsm -> bsm.c);
    }
    
    public String toString() {
        return new StringBuilder().append(this.type).append(": ").append(this.pos).append(", ").append(this.triggerTick).append(", ").append(this.priority).append(", ").append(this.c).toString();
    }
    
    public T getType() {
        return this.type;
    }
}
