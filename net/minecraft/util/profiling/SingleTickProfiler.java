package net.minecraft.util.profiling;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.function.LongSupplier;
import org.apache.logging.log4j.Logger;

public class SingleTickProfiler {
    private static final Logger LOGGER;
    private final LongSupplier realTime;
    private final long saveThreshold;
    private int tick;
    private final File location;
    private ProfileCollector profiler;
    
    public ProfilerFiller startTick() {
        this.profiler = new ActiveProfiler(this.realTime, () -> this.tick, false);
        ++this.tick;
        return this.profiler;
    }
    
    public void endTick() {
        if (this.profiler == InactiveProfiler.INSTANCE) {
            return;
        }
        final ProfileResults ans2 = this.profiler.getResults();
        this.profiler = InactiveProfiler.INSTANCE;
        if (ans2.getNanoDuration() >= this.saveThreshold) {
            final File file3 = new File(this.location, "tick-results-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".txt");
            ans2.saveResults(file3);
            SingleTickProfiler.LOGGER.info("Recorded long tick -- wrote info to: {}", file3.getAbsolutePath());
        }
    }
    
    @Nullable
    public static SingleTickProfiler createTickProfiler(final String string) {
        return null;
    }
    
    public static ProfilerFiller decorateFiller(final ProfilerFiller ant, @Nullable final SingleTickProfiler anw) {
        if (anw != null) {
            return ProfilerFiller.tee(anw.startTick(), ant);
        }
        return ant;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
