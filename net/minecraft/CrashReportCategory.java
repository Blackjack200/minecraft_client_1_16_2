package net.minecraft;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import java.util.Locale;
import com.google.common.collect.Lists;
import java.util.List;

public class CrashReportCategory {
    private final CrashReport report;
    private final String title;
    private final List<Entry> entries;
    private StackTraceElement[] stackTrace;
    
    public CrashReportCategory(final CrashReport l, final String string) {
        this.entries = (List<Entry>)Lists.newArrayList();
        this.stackTrace = new StackTraceElement[0];
        this.report = l;
        this.title = string;
    }
    
    public static String formatLocation(final double double1, final double double2, final double double3) {
        return String.format(Locale.ROOT, "%.2f,%.2f,%.2f - %s", new Object[] { double1, double2, double3, formatLocation(new BlockPos(double1, double2, double3)) });
    }
    
    public static String formatLocation(final BlockPos fx) {
        return formatLocation(fx.getX(), fx.getY(), fx.getZ());
    }
    
    public static String formatLocation(final int integer1, final int integer2, final int integer3) {
        final StringBuilder stringBuilder4 = new StringBuilder();
        try {
            stringBuilder4.append(String.format("World: (%d,%d,%d)", new Object[] { integer1, integer2, integer3 }));
        }
        catch (Throwable throwable5) {
            stringBuilder4.append("(Error finding world loc)");
        }
        stringBuilder4.append(", ");
        try {
            final int integer4 = integer1 >> 4;
            final int integer5 = integer3 >> 4;
            final int integer6 = integer1 & 0xF;
            final int integer7 = integer2 >> 4;
            final int integer8 = integer3 & 0xF;
            final int integer9 = integer4 << 4;
            final int integer10 = integer5 << 4;
            final int integer11 = (integer4 + 1 << 4) - 1;
            final int integer12 = (integer5 + 1 << 4) - 1;
            stringBuilder4.append(String.format("Chunk: (at %d,%d,%d in %d,%d; contains blocks %d,0,%d to %d,255,%d)", new Object[] { integer6, integer7, integer8, integer4, integer5, integer9, integer10, integer11, integer12 }));
        }
        catch (Throwable throwable5) {
            stringBuilder4.append("(Error finding chunk loc)");
        }
        stringBuilder4.append(", ");
        try {
            final int integer4 = integer1 >> 9;
            final int integer5 = integer3 >> 9;
            final int integer6 = integer4 << 5;
            final int integer7 = integer5 << 5;
            final int integer8 = (integer4 + 1 << 5) - 1;
            final int integer9 = (integer5 + 1 << 5) - 1;
            final int integer10 = integer4 << 9;
            final int integer11 = integer5 << 9;
            final int integer12 = (integer4 + 1 << 9) - 1;
            final int integer13 = (integer5 + 1 << 9) - 1;
            stringBuilder4.append(String.format("Region: (%d,%d; contains chunks %d,%d to %d,%d, blocks %d,0,%d to %d,255,%d)", new Object[] { integer4, integer5, integer6, integer7, integer8, integer9, integer10, integer11, integer12, integer13 }));
        }
        catch (Throwable throwable5) {
            stringBuilder4.append("(Error finding world loc)");
        }
        return stringBuilder4.toString();
    }
    
    public CrashReportCategory setDetail(final String string, final CrashReportDetail<String> n) {
        try {
            this.setDetail(string, n.call());
        }
        catch (Throwable throwable4) {
            this.setDetailError(string, throwable4);
        }
        return this;
    }
    
    public CrashReportCategory setDetail(final String string, final Object object) {
        this.entries.add(new Entry(string, object));
        return this;
    }
    
    public void setDetailError(final String string, final Throwable throwable) {
        this.setDetail(string, throwable);
    }
    
    public int fillInStackTrace(final int integer) {
        final StackTraceElement[] arr3 = Thread.currentThread().getStackTrace();
        if (arr3.length <= 0) {
            return 0;
        }
        System.arraycopy(arr3, 3 + integer, (this.stackTrace = new StackTraceElement[arr3.length - 3 - integer]), 0, this.stackTrace.length);
        return this.stackTrace.length;
    }
    
    public boolean validateStackTrace(final StackTraceElement stackTraceElement1, final StackTraceElement stackTraceElement2) {
        if (this.stackTrace.length == 0 || stackTraceElement1 == null) {
            return false;
        }
        final StackTraceElement stackTraceElement3 = this.stackTrace[0];
        if (stackTraceElement3.isNativeMethod() != stackTraceElement1.isNativeMethod() || !stackTraceElement3.getClassName().equals(stackTraceElement1.getClassName()) || !stackTraceElement3.getFileName().equals(stackTraceElement1.getFileName()) || !stackTraceElement3.getMethodName().equals(stackTraceElement1.getMethodName())) {
            return false;
        }
        if (stackTraceElement2 != null != this.stackTrace.length > 1) {
            return false;
        }
        if (stackTraceElement2 != null && !this.stackTrace[1].equals(stackTraceElement2)) {
            return false;
        }
        this.stackTrace[0] = stackTraceElement1;
        return true;
    }
    
    public void trimStacktrace(final int integer) {
        final StackTraceElement[] arr3 = new StackTraceElement[this.stackTrace.length - integer];
        System.arraycopy(this.stackTrace, 0, arr3, 0, arr3.length);
        this.stackTrace = arr3;
    }
    
    public void getDetails(final StringBuilder stringBuilder) {
        stringBuilder.append("-- ").append(this.title).append(" --\n");
        stringBuilder.append("Details:");
        for (final Entry a4 : this.entries) {
            stringBuilder.append("\n\t");
            stringBuilder.append(a4.getKey());
            stringBuilder.append(": ");
            stringBuilder.append(a4.getValue());
        }
        if (this.stackTrace != null && this.stackTrace.length > 0) {
            stringBuilder.append("\nStacktrace:");
            for (final StackTraceElement stackTraceElement6 : this.stackTrace) {
                stringBuilder.append("\n\tat ");
                stringBuilder.append(stackTraceElement6);
            }
        }
    }
    
    public StackTraceElement[] getStacktrace() {
        return this.stackTrace;
    }
    
    public static void populateBlockDetails(final CrashReportCategory m, final BlockPos fx, @Nullable final BlockState cee) {
        if (cee != null) {
            m.setDetail("Block", (CrashReportDetail<String>)cee::toString);
        }
        m.setDetail("Block location", (CrashReportDetail<String>)(() -> formatLocation(fx)));
    }
    
    static class Entry {
        private final String key;
        private final String value;
        
        public Entry(final String string, @Nullable final Object object) {
            this.key = string;
            if (object == null) {
                this.value = "~~NULL~~";
            }
            else if (object instanceof Throwable) {
                final Throwable throwable4 = (Throwable)object;
                this.value = "~~ERROR~~ " + throwable4.getClass().getSimpleName() + ": " + throwable4.getMessage();
            }
            else {
                this.value = object.toString();
            }
        }
        
        public String getKey() {
            return this.key;
        }
        
        public String getValue() {
            return this.value;
        }
    }
}
