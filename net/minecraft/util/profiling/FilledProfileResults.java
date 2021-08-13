package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import org.apache.commons.lang3.ObjectUtils;
import java.util.Locale;
import net.minecraft.SharedConstants;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.io.File;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import com.google.common.base.Splitter;
import org.apache.logging.log4j.Logger;

public class FilledProfileResults implements ProfileResults {
    private static final Logger LOGGER;
    private static final ProfilerPathEntry EMPTY;
    private static final Splitter SPLITTER;
    private static final Comparator<Map.Entry<String, CounterCollector>> COUNTER_ENTRY_COMPARATOR;
    private final Map<String, ? extends ProfilerPathEntry> entries;
    private final long startTimeNano;
    private final int startTimeTicks;
    private final long endTimeNano;
    private final int endTimeTicks;
    private final int tickDuration;
    
    public FilledProfileResults(final Map<String, ? extends ProfilerPathEntry> map, final long long2, final int integer3, final long long4, final int integer5) {
        this.entries = map;
        this.startTimeNano = long2;
        this.startTimeTicks = integer3;
        this.endTimeNano = long4;
        this.endTimeTicks = integer5;
        this.tickDuration = integer5 - integer3;
    }
    
    private ProfilerPathEntry getEntry(final String string) {
        final ProfilerPathEntry anu3 = (ProfilerPathEntry)this.entries.get(string);
        return (anu3 != null) ? anu3 : FilledProfileResults.EMPTY;
    }
    
    public List<ResultField> getTimes(String string) {
        final String string2 = string;
        final ProfilerPathEntry anu4 = this.getEntry("root");
        long long5 = anu4.getDuration();
        final ProfilerPathEntry anu5 = this.getEntry(string);
        final long long6 = anu5.getDuration();
        final long long7 = anu5.getCount();
        final List<ResultField> list12 = (List<ResultField>)Lists.newArrayList();
        if (!string.isEmpty()) {
            string += '\u001e';
        }
        long long8 = 0L;
        for (final String string3 : this.entries.keySet()) {
            if (isDirectChild(string, string3)) {
                long8 += this.getEntry(string3).getDuration();
            }
        }
        final float float15 = (float)long8;
        if (long8 < long6) {
            long8 = long6;
        }
        if (long5 < long8) {
            long5 = long8;
        }
        for (final String string4 : this.entries.keySet()) {
            if (isDirectChild(string, string4)) {
                final ProfilerPathEntry anu6 = this.getEntry(string4);
                final long long9 = anu6.getDuration();
                final double double21 = long9 * 100.0 / long8;
                final double double22 = long9 * 100.0 / long5;
                final String string5 = string4.substring(string.length());
                list12.add(new ResultField(string5, double21, double22, anu6.getCount()));
            }
        }
        if (long8 > float15) {
            list12.add(new ResultField("unspecified", (long8 - float15) * 100.0 / long8, (long8 - float15) * 100.0 / long5, long7));
        }
        Collections.sort((List)list12);
        list12.add(0, new ResultField(string2, 100.0, long8 * 100.0 / long5, long7));
        return list12;
    }
    
    private static boolean isDirectChild(final String string1, final String string2) {
        return string2.length() > string1.length() && string2.startsWith(string1) && string2.indexOf(30, string1.length() + 1) < 0;
    }
    
    private Map<String, CounterCollector> getCounterValues() {
        final Map<String, CounterCollector> map2 = (Map<String, CounterCollector>)Maps.newTreeMap();
        this.entries.forEach((string, anu) -> {
            final Object2LongMap<String> object2LongMap4 = anu.getCounters();
            if (!object2LongMap4.isEmpty()) {
                final List<String> list5 = (List<String>)FilledProfileResults.SPLITTER.splitToList((CharSequence)string);
                object2LongMap4.forEach((string, long4) -> ((CounterCollector)map2.computeIfAbsent(string, string -> new CounterCollector())).addValue((Iterator<String>)list5.iterator(), long4));
            }
        });
        return map2;
    }
    
    public long getStartTimeNano() {
        return this.startTimeNano;
    }
    
    public int getStartTimeTicks() {
        return this.startTimeTicks;
    }
    
    public long getEndTimeNano() {
        return this.endTimeNano;
    }
    
    public int getEndTimeTicks() {
        return this.endTimeTicks;
    }
    
    public boolean saveResults(final File file) {
        file.getParentFile().mkdirs();
        Writer writer3 = null;
        try {
            writer3 = (Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(file), StandardCharsets.UTF_8);
            writer3.write(this.getProfilerResults(this.getNanoDuration(), this.getTickDuration()));
            return true;
        }
        catch (Throwable throwable4) {
            FilledProfileResults.LOGGER.error("Could not save profiler results to {}", file, throwable4);
            return false;
        }
        finally {
            IOUtils.closeQuietly(writer3);
        }
    }
    
    protected String getProfilerResults(final long long1, final int integer) {
        final StringBuilder stringBuilder5 = new StringBuilder();
        stringBuilder5.append("---- Minecraft Profiler Results ----\n");
        stringBuilder5.append("// ");
        stringBuilder5.append(getComment());
        stringBuilder5.append("\n\n");
        stringBuilder5.append("Version: ").append(SharedConstants.getCurrentVersion().getId()).append('\n');
        stringBuilder5.append("Time span: ").append(long1 / 1000000L).append(" ms\n");
        stringBuilder5.append("Tick span: ").append(integer).append(" ticks\n");
        stringBuilder5.append("// This is approximately ").append(String.format(Locale.ROOT, "%.2f", new Object[] { integer / (long1 / 1.0E9f) })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringBuilder5.append("--- BEGIN PROFILE DUMP ---\n\n");
        this.appendProfilerResults(0, "root", stringBuilder5);
        stringBuilder5.append("--- END PROFILE DUMP ---\n\n");
        final Map<String, CounterCollector> map6 = this.getCounterValues();
        if (!map6.isEmpty()) {
            stringBuilder5.append("--- BEGIN COUNTER DUMP ---\n\n");
            this.appendCounters(map6, stringBuilder5, integer);
            stringBuilder5.append("--- END COUNTER DUMP ---\n\n");
        }
        return stringBuilder5.toString();
    }
    
    private static StringBuilder indentLine(final StringBuilder stringBuilder, final int integer) {
        stringBuilder.append(String.format("[%02d] ", new Object[] { integer }));
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            stringBuilder.append("|   ");
        }
        return stringBuilder;
    }
    
    private void appendProfilerResults(final int integer, final String string, final StringBuilder stringBuilder) {
        final List<ResultField> list5 = this.getTimes(string);
        final Object2LongMap<String> object2LongMap6 = ((ProfilerPathEntry)ObjectUtils.firstNonNull((Object[])new ProfilerPathEntry[] { (ProfilerPathEntry)this.entries.get(string), FilledProfileResults.EMPTY })).getCounters();
        object2LongMap6.forEach((string, long4) -> indentLine(stringBuilder, integer).append('#').append(string).append(' ').append(long4).append('/').append(long4 / this.tickDuration).append('\n'));
        if (list5.size() < 3) {
            return;
        }
        for (int integer2 = 1; integer2 < list5.size(); ++integer2) {
            final ResultField anv8 = (ResultField)list5.get(integer2);
            indentLine(stringBuilder, integer).append(anv8.name).append('(').append(anv8.count).append('/').append(String.format(Locale.ROOT, "%.0f", new Object[] { anv8.count / (float)this.tickDuration })).append(')').append(" - ").append(String.format(Locale.ROOT, "%.2f", new Object[] { anv8.percentage })).append("%/").append(String.format(Locale.ROOT, "%.2f", new Object[] { anv8.globalPercentage })).append("%\n");
            if (!"unspecified".equals(anv8.name)) {
                try {
                    this.appendProfilerResults(integer + 1, string + '\u001e' + anv8.name, stringBuilder);
                }
                catch (Exception exception9) {
                    stringBuilder.append("[[ EXCEPTION ").append(exception9).append(" ]]");
                }
            }
        }
    }
    
    private void appendCounterResults(final int integer1, final String string, final CounterCollector a, final int integer4, final StringBuilder stringBuilder) {
        indentLine(stringBuilder, integer1).append(string).append(" total:").append(a.selfValue).append('/').append(a.totalValue).append(" average: ").append(a.selfValue / integer4).append('/').append(a.totalValue / integer4).append('\n');
        a.children.entrySet().stream().sorted((Comparator)FilledProfileResults.COUNTER_ENTRY_COMPARATOR).forEach(entry -> this.appendCounterResults(integer1 + 1, (String)entry.getKey(), (CounterCollector)entry.getValue(), integer4, stringBuilder));
    }
    
    private void appendCounters(final Map<String, CounterCollector> map, final StringBuilder stringBuilder, final int integer) {
        map.forEach((string, a) -> {
            stringBuilder.append("-- Counter: ").append(string).append(" --\n");
            this.appendCounterResults(0, "root", (CounterCollector)a.children.get("root"), integer, stringBuilder);
            stringBuilder.append("\n\n");
        });
    }
    
    private static String getComment() {
        final String[] arr1 = { "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers", "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server." };
        try {
            return arr1[(int)(Util.getNanos() % arr1.length)];
        }
        catch (Throwable throwable2) {
            return "Witty comment unavailable :(";
        }
    }
    
    public int getTickDuration() {
        return this.tickDuration;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY = new ProfilerPathEntry() {
            public long getDuration() {
                return 0L;
            }
            
            public long getCount() {
                return 0L;
            }
            
            public Object2LongMap<String> getCounters() {
                return (Object2LongMap<String>)Object2LongMaps.emptyMap();
            }
        };
        SPLITTER = Splitter.on('\u001e');
        COUNTER_ENTRY_COMPARATOR = Map.Entry.comparingByValue(Comparator.comparingLong(a -> a.totalValue)).reversed();
    }
    
    static class CounterCollector {
        private long selfValue;
        private long totalValue;
        private final Map<String, CounterCollector> children;
        
        private CounterCollector() {
            this.children = (Map<String, CounterCollector>)Maps.newHashMap();
        }
        
        public void addValue(final Iterator<String> iterator, final long long2) {
            this.totalValue += long2;
            if (!iterator.hasNext()) {
                this.selfValue += long2;
            }
            else {
                ((CounterCollector)this.children.computeIfAbsent(iterator.next(), string -> new CounterCollector())).addValue(iterator, long2);
            }
        }
    }
}
