package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.apache.logging.log4j.LogManager;
import java.time.Duration;
import net.minecraft.Util;
import org.apache.logging.log4j.util.Supplier;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.function.LongSupplier;
import java.util.function.IntSupplier;
import java.util.Map;
import it.unimi.dsi.fastutil.longs.LongList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class ActiveProfiler implements ProfileCollector {
    private static final long WARNING_TIME_NANOS;
    private static final Logger LOGGER;
    private final List<String> paths;
    private final LongList startTimes;
    private final Map<String, PathEntry> entries;
    private final IntSupplier getTickTime;
    private final LongSupplier getRealTime;
    private final long startTimeNano;
    private final int startTimeTicks;
    private String path;
    private boolean started;
    @Nullable
    private PathEntry currentEntry;
    private final boolean warn;
    
    public ActiveProfiler(final LongSupplier longSupplier, final IntSupplier intSupplier, final boolean boolean3) {
        this.paths = (List<String>)Lists.newArrayList();
        this.startTimes = (LongList)new LongArrayList();
        this.entries = (Map<String, PathEntry>)Maps.newHashMap();
        this.path = "";
        this.startTimeNano = longSupplier.getAsLong();
        this.getRealTime = longSupplier;
        this.startTimeTicks = intSupplier.getAsInt();
        this.getTickTime = intSupplier;
        this.warn = boolean3;
    }
    
    public void startTick() {
        if (this.started) {
            ActiveProfiler.LOGGER.error("Profiler tick already started - missing endTick()?");
            return;
        }
        this.started = true;
        this.path = "";
        this.paths.clear();
        this.push("root");
    }
    
    public void endTick() {
        if (!this.started) {
            ActiveProfiler.LOGGER.error("Profiler tick already ended - missing startTick()?");
            return;
        }
        this.pop();
        this.started = false;
        if (!this.path.isEmpty()) {
            ActiveProfiler.LOGGER.error("Profiler tick ended before path was fully popped (remainder: '{}'). Mismatched push/pop?", new Supplier[] { () -> ProfileResults.demanglePath(this.path) });
        }
    }
    
    public void push(final String string) {
        if (!this.started) {
            ActiveProfiler.LOGGER.error("Cannot push '{}' to profiler if profiler tick hasn't started - missing startTick()?", string);
            return;
        }
        if (!this.path.isEmpty()) {
            this.path += '\u001e';
        }
        this.path += string;
        this.paths.add(this.path);
        this.startTimes.add(Util.getNanos());
        this.currentEntry = null;
    }
    
    public void push(final java.util.function.Supplier<String> supplier) {
        this.push((String)supplier.get());
    }
    
    public void pop() {
        if (!this.started) {
            ActiveProfiler.LOGGER.error("Cannot pop from profiler if profiler tick hasn't started - missing startTick()?");
            return;
        }
        if (this.startTimes.isEmpty()) {
            ActiveProfiler.LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
            return;
        }
        final long long2 = Util.getNanos();
        final long long3 = this.startTimes.removeLong(this.startTimes.size() - 1);
        this.paths.remove(this.paths.size() - 1);
        final long long4 = long2 - long3;
        final PathEntry a8 = this.getCurrentEntry();
        a8.duration += long4;
        ++a8.count;
        if (this.warn && long4 > ActiveProfiler.WARNING_TIME_NANOS) {
            ActiveProfiler.LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", new Supplier[] { () -> ProfileResults.demanglePath(this.path), () -> long4 / 1000000.0 });
        }
        this.path = (this.paths.isEmpty() ? "" : ((String)this.paths.get(this.paths.size() - 1)));
        this.currentEntry = null;
    }
    
    public void popPush(final String string) {
        this.pop();
        this.push(string);
    }
    
    public void popPush(final java.util.function.Supplier<String> supplier) {
        this.pop();
        this.push(supplier);
    }
    
    private PathEntry getCurrentEntry() {
        if (this.currentEntry == null) {
            this.currentEntry = (PathEntry)this.entries.computeIfAbsent(this.path, string -> new PathEntry());
        }
        return this.currentEntry;
    }
    
    public void incrementCounter(final String string) {
        this.getCurrentEntry().counters.addTo(string, 1L);
    }
    
    public void incrementCounter(final java.util.function.Supplier<String> supplier) {
        this.getCurrentEntry().counters.addTo(supplier.get(), 1L);
    }
    
    public ProfileResults getResults() {
        return new FilledProfileResults(this.entries, this.startTimeNano, this.startTimeTicks, this.getRealTime.getAsLong(), this.getTickTime.getAsInt());
    }
    
    static {
        WARNING_TIME_NANOS = Duration.ofMillis(100L).toNanos();
        LOGGER = LogManager.getLogger();
    }
    
    static class PathEntry implements ProfilerPathEntry {
        private long duration;
        private long count;
        private Object2LongOpenHashMap<String> counters;
        
        private PathEntry() {
            this.counters = (Object2LongOpenHashMap<String>)new Object2LongOpenHashMap();
        }
        
        public long getDuration() {
            return this.duration;
        }
        
        public long getCount() {
            return this.count;
        }
        
        public Object2LongMap<String> getCounters() {
            return (Object2LongMap<String>)Object2LongMaps.unmodifiable((Object2LongMap)this.counters);
        }
    }
}
