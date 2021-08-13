package net.minecraft.world.level;

import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.BlockPos;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import java.util.List;

public class ChunkTickList<T> implements TickList<T> {
    private final List<ScheduledTick<T>> ticks;
    private final Function<T, ResourceLocation> toId;
    
    public ChunkTickList(final Function<T, ResourceLocation> function, final List<TickNextTickData<T>> list, final long long3) {
        this((Function)function, (List)list.stream().map(bsm -> new ScheduledTick(bsm.getType(), bsm.pos, (int)(bsm.triggerTick - long3), bsm.priority)).collect(Collectors.toList()));
    }
    
    private ChunkTickList(final Function<T, ResourceLocation> function, final List<ScheduledTick<T>> list) {
        this.ticks = list;
        this.toId = function;
    }
    
    public boolean hasScheduledTick(final BlockPos fx, final T object) {
        return false;
    }
    
    public void scheduleTick(final BlockPos fx, final T object, final int integer, final TickPriority bsn) {
        this.ticks.add(new ScheduledTick((Object)object, fx, integer, bsn));
    }
    
    public boolean willTickThisTick(final BlockPos fx, final T object) {
        return false;
    }
    
    public ListTag save() {
        final ListTag mj2 = new ListTag();
        for (final ScheduledTick<T> a4 : this.ticks) {
            final CompoundTag md5 = new CompoundTag();
            md5.putString("i", ((ResourceLocation)this.toId.apply(((ScheduledTick<Object>)a4).type)).toString());
            md5.putInt("x", a4.pos.getX());
            md5.putInt("y", a4.pos.getY());
            md5.putInt("z", a4.pos.getZ());
            md5.putInt("t", a4.delay);
            md5.putInt("p", a4.priority.getValue());
            mj2.add(md5);
        }
        return mj2;
    }
    
    public static <T> ChunkTickList<T> create(final ListTag mj, final Function<T, ResourceLocation> function2, final Function<ResourceLocation, T> function3) {
        final List<ScheduledTick<T>> list4 = (List<ScheduledTick<T>>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < mj.size(); ++integer5) {
            final CompoundTag md6 = mj.getCompound(integer5);
            final T object7 = (T)function3.apply(new ResourceLocation(md6.getString("i")));
            if (object7 != null) {
                final BlockPos fx8 = new BlockPos(md6.getInt("x"), md6.getInt("y"), md6.getInt("z"));
                list4.add(new ScheduledTick((Object)object7, fx8, md6.getInt("t"), TickPriority.byValue(md6.getInt("p"))));
            }
        }
        return new ChunkTickList<T>(function2, list4);
    }
    
    public void copyOut(final TickList<T> bsl) {
        this.ticks.forEach(a -> bsl.scheduleTick(a.pos, a.type, a.delay, a.priority));
    }
    
    static class ScheduledTick<T> {
        private final T type;
        public final BlockPos pos;
        public final int delay;
        public final TickPriority priority;
        
        private ScheduledTick(final T object, final BlockPos fx, final int integer, final TickPriority bsn) {
            this.type = object;
            this.pos = fx;
            this.delay = integer;
            this.priority = bsn;
        }
        
        public String toString() {
            return new StringBuilder().append(this.type).append(": ").append(this.pos).append(", ").append(this.delay).append(", ").append(this.priority).toString();
        }
    }
}
