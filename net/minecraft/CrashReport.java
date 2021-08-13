package net.minecraft;

import org.apache.logging.log4j.LogManager;
import java.util.stream.Collectors;
import java.util.concurrent.CompletionException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.io.FileOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import org.apache.commons.io.IOUtils;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class CrashReport {
    private static final Logger LOGGER;
    private final String title;
    private final Throwable exception;
    private final CrashReportCategory systemDetails;
    private final List<CrashReportCategory> details;
    private File saveFile;
    private boolean trackingStackTrace;
    private StackTraceElement[] uncategorizedStackTrace;
    
    public CrashReport(final String string, final Throwable throwable) {
        this.systemDetails = new CrashReportCategory(this, "System Details");
        this.details = (List<CrashReportCategory>)Lists.newArrayList();
        this.trackingStackTrace = true;
        this.uncategorizedStackTrace = new StackTraceElement[0];
        this.title = string;
        this.exception = throwable;
        this.initDetails();
    }
    
    private void initDetails() {
        this.systemDetails.setDetail("Minecraft Version", (CrashReportDetail<String>)(() -> SharedConstants.getCurrentVersion().getName()));
        this.systemDetails.setDetail("Minecraft Version ID", (CrashReportDetail<String>)(() -> SharedConstants.getCurrentVersion().getId()));
        this.systemDetails.setDetail("Operating System", (CrashReportDetail<String>)(() -> System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version")));
        this.systemDetails.setDetail("Java Version", (CrashReportDetail<String>)(() -> System.getProperty("java.version") + ", " + System.getProperty("java.vendor")));
        this.systemDetails.setDetail("Java VM Version", (CrashReportDetail<String>)(() -> System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor")));
        this.systemDetails.setDetail("Memory", (CrashReportDetail<String>)(() -> {
            final Runtime runtime1 = Runtime.getRuntime();
            final long long2 = runtime1.maxMemory();
            final long long3 = runtime1.totalMemory();
            final long long4 = runtime1.freeMemory();
            final long long5 = long2 / 1024L / 1024L;
            final long long6 = long3 / 1024L / 1024L;
            final long long7 = long4 / 1024L / 1024L;
            return new StringBuilder().append(long4).append(" bytes (").append(long7).append(" MB) / ").append(long3).append(" bytes (").append(long6).append(" MB) up to ").append(long2).append(" bytes (").append(long5).append(" MB)").toString();
        }));
        this.systemDetails.setDetail("CPUs", Runtime.getRuntime().availableProcessors());
        this.systemDetails.setDetail("JVM Flags", (CrashReportDetail<String>)(() -> {
            final List<String> list1 = (List<String>)Util.getVmArguments().collect(Collectors.toList());
            return String.format("%d total; %s", new Object[] { list1.size(), list1.stream().collect(Collectors.joining(" ")) });
        }));
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public Throwable getException() {
        return this.exception;
    }
    
    public void getDetails(final StringBuilder stringBuilder) {
        if ((this.uncategorizedStackTrace == null || this.uncategorizedStackTrace.length <= 0) && !this.details.isEmpty()) {
            this.uncategorizedStackTrace = (StackTraceElement[])ArrayUtils.subarray((Object[])((CrashReportCategory)this.details.get(0)).getStacktrace(), 0, 1);
        }
        if (this.uncategorizedStackTrace != null && this.uncategorizedStackTrace.length > 0) {
            stringBuilder.append("-- Head --\n");
            stringBuilder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            stringBuilder.append("Stacktrace:\n");
            for (final StackTraceElement stackTraceElement6 : this.uncategorizedStackTrace) {
                stringBuilder.append("\t").append("at ").append(stackTraceElement6);
                stringBuilder.append("\n");
            }
            stringBuilder.append("\n");
        }
        for (final CrashReportCategory m4 : this.details) {
            m4.getDetails(stringBuilder);
            stringBuilder.append("\n\n");
        }
        this.systemDetails.getDetails(stringBuilder);
    }
    
    public String getExceptionMessage() {
        StringWriter stringWriter2 = null;
        PrintWriter printWriter3 = null;
        Throwable throwable4 = this.exception;
        if (throwable4.getMessage() == null) {
            if (throwable4 instanceof NullPointerException) {
                throwable4 = (Throwable)new NullPointerException(this.title);
            }
            else if (throwable4 instanceof StackOverflowError) {
                throwable4 = (Throwable)new StackOverflowError(this.title);
            }
            else if (throwable4 instanceof OutOfMemoryError) {
                throwable4 = (Throwable)new OutOfMemoryError(this.title);
            }
            throwable4.setStackTrace(this.exception.getStackTrace());
        }
        try {
            stringWriter2 = new StringWriter();
            printWriter3 = new PrintWriter((Writer)stringWriter2);
            throwable4.printStackTrace(printWriter3);
            return stringWriter2.toString();
        }
        finally {
            IOUtils.closeQuietly((Writer)stringWriter2);
            IOUtils.closeQuietly((Writer)printWriter3);
        }
    }
    
    public String getFriendlyReport() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("---- Minecraft Crash Report ----\n");
        stringBuilder2.append("// ");
        stringBuilder2.append(getErrorComment());
        stringBuilder2.append("\n\n");
        stringBuilder2.append("Time: ");
        stringBuilder2.append(new SimpleDateFormat().format(new Date()));
        stringBuilder2.append("\n");
        stringBuilder2.append("Description: ");
        stringBuilder2.append(this.title);
        stringBuilder2.append("\n\n");
        stringBuilder2.append(this.getExceptionMessage());
        stringBuilder2.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");
        for (int integer3 = 0; integer3 < 87; ++integer3) {
            stringBuilder2.append("-");
        }
        stringBuilder2.append("\n\n");
        this.getDetails(stringBuilder2);
        return stringBuilder2.toString();
    }
    
    public File getSaveFile() {
        return this.saveFile;
    }
    
    public boolean saveToFile(final File file) {
        if (this.saveFile != null) {
            return false;
        }
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        Writer writer3 = null;
        try {
            writer3 = (Writer)new OutputStreamWriter((OutputStream)new FileOutputStream(file), StandardCharsets.UTF_8);
            writer3.write(this.getFriendlyReport());
            this.saveFile = file;
            return true;
        }
        catch (Throwable throwable4) {
            CrashReport.LOGGER.error("Could not save crash report to {}", file, throwable4);
            return false;
        }
        finally {
            IOUtils.closeQuietly(writer3);
        }
    }
    
    public CrashReportCategory getSystemDetails() {
        return this.systemDetails;
    }
    
    public CrashReportCategory addCategory(final String string) {
        return this.addCategory(string, 1);
    }
    
    public CrashReportCategory addCategory(final String string, final int integer) {
        final CrashReportCategory m4 = new CrashReportCategory(this, string);
        if (this.trackingStackTrace) {
            final int integer2 = m4.fillInStackTrace(integer);
            final StackTraceElement[] arr6 = this.exception.getStackTrace();
            StackTraceElement stackTraceElement7 = null;
            StackTraceElement stackTraceElement8 = null;
            final int integer3 = arr6.length - integer2;
            if (integer3 < 0) {
                System.out.println(new StringBuilder().append("Negative index in crash report handler (").append(arr6.length).append("/").append(integer2).append(")").toString());
            }
            if (arr6 != null && 0 <= integer3 && integer3 < arr6.length) {
                stackTraceElement7 = arr6[integer3];
                if (arr6.length + 1 - integer2 < arr6.length) {
                    stackTraceElement8 = arr6[arr6.length + 1 - integer2];
                }
            }
            this.trackingStackTrace = m4.validateStackTrace(stackTraceElement7, stackTraceElement8);
            if (integer2 > 0 && !this.details.isEmpty()) {
                final CrashReportCategory m5 = (CrashReportCategory)this.details.get(this.details.size() - 1);
                m5.trimStacktrace(integer2);
            }
            else if (arr6 != null && arr6.length >= integer2 && 0 <= integer3 && integer3 < arr6.length) {
                System.arraycopy(arr6, 0, (this.uncategorizedStackTrace = new StackTraceElement[integer3]), 0, this.uncategorizedStackTrace.length);
            }
            else {
                this.trackingStackTrace = false;
            }
        }
        this.details.add(m4);
        return m4;
    }
    
    private static String getErrorComment() {
        final String[] arr1 = { "Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine." };
        try {
            return arr1[(int)(Util.getNanos() % arr1.length)];
        }
        catch (Throwable throwable2) {
            return "Witty comment unavailable :(";
        }
    }
    
    public static CrashReport forThrowable(Throwable throwable, final String string) {
        while (throwable instanceof CompletionException && throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        CrashReport l3;
        if (throwable instanceof ReportedException) {
            l3 = ((ReportedException)throwable).getReport();
        }
        else {
            l3 = new CrashReport(string, throwable);
        }
        return l3;
    }
    
    public static void preload() {
        new CrashReport("Don't panic!", new Throwable()).getFriendlyReport();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
