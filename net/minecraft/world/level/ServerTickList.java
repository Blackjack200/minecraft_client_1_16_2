package net.minecraft.world.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Vec3i;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.Iterator;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.ReportedException;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReport;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.Comparator;
import com.google.common.collect.Sets;
import java.util.function.Consumer;
import java.util.List;
import java.util.Queue;
import net.minecraft.server.level.ServerLevel;
import java.util.TreeSet;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import java.util.function.Predicate;

public class ServerTickList<T> implements TickList<T> {
    protected final Predicate<T> ignore;
    private final Function<T, ResourceLocation> toId;
    private final Set<TickNextTickData<T>> tickNextTickSet;
    private final TreeSet<TickNextTickData<T>> tickNextTickList;
    private final ServerLevel level;
    private final Queue<TickNextTickData<T>> currentlyTicking;
    private final List<TickNextTickData<T>> alreadyTicked;
    private final Consumer<TickNextTickData<T>> ticker;
    
    public ServerTickList(final ServerLevel aag, final Predicate<T> predicate, final Function<T, ResourceLocation> function, final Consumer<TickNextTickData<T>> consumer) {
        this.tickNextTickSet = (Set<TickNextTickData<T>>)Sets.newHashSet();
        this.tickNextTickList = (TreeSet<TickNextTickData<T>>)Sets.newTreeSet((Comparator)TickNextTickData.createTimeComparator());
        this.currentlyTicking = (Queue<TickNextTickData<T>>)Queues.newArrayDeque();
        this.alreadyTicked = (List<TickNextTickData<T>>)Lists.newArrayList();
        this.ignore = predicate;
        this.toId = function;
        this.level = aag;
        this.ticker = consumer;
    }
    
    public void tick() {
        int integer2 = this.tickNextTickList.size();
        if (integer2 != this.tickNextTickSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (integer2 > 65536) {
            integer2 = 65536;
        }
        final ServerChunkCache aae3 = this.level.getChunkSource();
        final Iterator<TickNextTickData<T>> iterator4 = (Iterator<TickNextTickData<T>>)this.tickNextTickList.iterator();
        this.level.getProfiler().push("cleaning");
        while (integer2 > 0 && iterator4.hasNext()) {
            final TickNextTickData<T> bsm5 = (TickNextTickData<T>)iterator4.next();
            if (bsm5.triggerTick > this.level.getGameTime()) {
                break;
            }
            if (!aae3.isTickingChunk(bsm5.pos)) {
                continue;
            }
            iterator4.remove();
            this.tickNextTickSet.remove(bsm5);
            this.currentlyTicking.add(bsm5);
            --integer2;
        }
        this.level.getProfiler().popPush("ticking");
        TickNextTickData<T> bsm5;
        while ((bsm5 = (TickNextTickData<T>)this.currentlyTicking.poll()) != null) {
            if (aae3.isTickingChunk(bsm5.pos)) {
                try {
                    this.alreadyTicked.add(bsm5);
                    this.ticker.accept(bsm5);
                    continue;
                }
                catch (Throwable throwable6) {
                    final CrashReport l7 = CrashReport.forThrowable(throwable6, "Exception while ticking");
                    final CrashReportCategory m8 = l7.addCategory("Block being ticked");
                    CrashReportCategory.populateBlockDetails(m8, bsm5.pos, null);
                    throw new ReportedException(l7);
                }
            }
            this.scheduleTick(bsm5.pos, bsm5.getType(), 0);
        }
        this.level.getProfiler().pop();
        this.alreadyTicked.clear();
        this.currentlyTicking.clear();
    }
    
    public boolean willTickThisTick(final BlockPos fx, final T object) {
        return this.currentlyTicking.contains(new TickNextTickData(fx, object));
    }
    
    public List<TickNextTickData<T>> fetchTicksInChunk(final ChunkPos bra, final boolean boolean2, final boolean boolean3) {
        final int integer5 = (bra.x << 4) - 2;
        final int integer6 = integer5 + 16 + 2;
        final int integer7 = (bra.z << 4) - 2;
        final int integer8 = integer7 + 16 + 2;
        return this.fetchTicksInArea(new BoundingBox(integer5, 0, integer7, integer6, 256, integer8), boolean2, boolean3);
    }
    
    public List<TickNextTickData<T>> fetchTicksInArea(final BoundingBox cqx, final boolean boolean2, final boolean boolean3) {
        List<TickNextTickData<T>> list5 = this.fetchTicksInArea(null, (Collection<TickNextTickData<T>>)this.tickNextTickList, cqx, boolean2);
        if (boolean2 && list5 != null) {
            this.tickNextTickSet.removeAll((Collection)list5);
        }
        list5 = this.fetchTicksInArea(list5, (Collection<TickNextTickData<T>>)this.currentlyTicking, cqx, boolean2);
        if (!boolean3) {
            list5 = this.fetchTicksInArea(list5, (Collection<TickNextTickData<T>>)this.alreadyTicked, cqx, boolean2);
        }
        return (List<TickNextTickData<T>>)((list5 == null) ? Collections.emptyList() : list5);
    }
    
    @Nullable
    private List<TickNextTickData<T>> fetchTicksInArea(@Nullable List<TickNextTickData<T>> list, final Collection<TickNextTickData<T>> collection, final BoundingBox cqx, final boolean boolean4) {
        final Iterator<TickNextTickData<T>> iterator6 = (Iterator<TickNextTickData<T>>)collection.iterator();
        while (iterator6.hasNext()) {
            final TickNextTickData<T> bsm7 = (TickNextTickData<T>)iterator6.next();
            final BlockPos fx8 = bsm7.pos;
            if (fx8.getX() >= cqx.x0 && fx8.getX() < cqx.x1 && fx8.getZ() >= cqx.z0 && fx8.getZ() < cqx.z1) {
                if (boolean4) {
                    iterator6.remove();
                }
                if (list == null) {
                    list = (List<TickNextTickData<T>>)Lists.newArrayList();
                }
                list.add(bsm7);
            }
        }
        return list;
    }
    
    public void copy(final BoundingBox cqx, final BlockPos fx) {
        final List<TickNextTickData<T>> list4 = this.fetchTicksInArea(cqx, false, false);
        for (final TickNextTickData<T> bsm6 : list4) {
            if (cqx.isInside(bsm6.pos)) {
                final BlockPos fx2 = bsm6.pos.offset(fx);
                final T object8 = bsm6.getType();
                this.addTickData(new TickNextTickData<T>(fx2, object8, bsm6.triggerTick, bsm6.priority));
            }
        }
    }
    
    public ListTag save(final ChunkPos bra) {
        final List<TickNextTickData<T>> list3 = this.fetchTicksInChunk(bra, false, true);
        return ServerTickList.<T>saveTickList(this.toId, (java.lang.Iterable<TickNextTickData<T>>)list3, this.level.getGameTime());
    }
    
    private static <T> ListTag saveTickList(final Function<T, ResourceLocation> function, final Iterable<TickNextTickData<T>> iterable, final long long3) {
        final ListTag mj5 = new ListTag();
        for (final TickNextTickData<T> bsm7 : iterable) {
            final CompoundTag md8 = new CompoundTag();
            md8.putString("i", ((ResourceLocation)function.apply(bsm7.getType())).toString());
            md8.putInt("x", bsm7.pos.getX());
            md8.putInt("y", bsm7.pos.getY());
            md8.putInt("z", bsm7.pos.getZ());
            md8.putInt("t", (int)(bsm7.triggerTick - long3));
            md8.putInt("p", bsm7.priority.getValue());
            mj5.add(md8);
        }
        return mj5;
    }
    
    public boolean hasScheduledTick(final BlockPos fx, final T object) {
        return this.tickNextTickSet.contains(new TickNextTickData(fx, object));
    }
    
    public void scheduleTick(final BlockPos fx, final T object, final int integer, final TickPriority bsn) {
        if (!this.ignore.test(object)) {
            this.addTickData(new TickNextTickData<T>(fx, object, integer + this.level.getGameTime(), bsn));
        }
    }
    
    private void addTickData(final TickNextTickData<T> bsm) {
        if (!this.tickNextTickSet.contains(bsm)) {
            this.tickNextTickSet.add(bsm);
            this.tickNextTickList.add(bsm);
        }
    }
    
    public int size() {
        return this.tickNextTickSet.size();
    }
}
