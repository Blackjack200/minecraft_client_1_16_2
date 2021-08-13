package net.minecraft.server.dedicated;

import org.apache.logging.log4j.LogManager;
import java.util.TimerTask;
import java.util.Timer;
import net.minecraft.CrashReportCategory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.File;
import net.minecraft.CrashReport;
import java.lang.management.ManagementFactory;
import java.util.Locale;
import net.minecraft.Util;
import org.apache.logging.log4j.Logger;

public class ServerWatchdog implements Runnable {
    private static final Logger LOGGER;
    private final DedicatedServer server;
    private final long maxTickTime;
    
    public ServerWatchdog(final DedicatedServer zg) {
        this.server = zg;
        this.maxTickTime = zg.getMaxTickLength();
    }
    
    public void run() {
        while (this.server.isRunning()) {
            final long long2 = this.server.getNextTickTime();
            final long long3 = Util.getMillis();
            final long long4 = long3 - long2;
            if (long4 > this.maxTickTime) {
                ServerWatchdog.LOGGER.fatal("A single server tick took {} seconds (should be max {})", String.format(Locale.ROOT, "%.2f", new Object[] { long4 / 1000.0f }), String.format(Locale.ROOT, "%.2f", new Object[] { 0.05f }));
                ServerWatchdog.LOGGER.fatal("Considering it to be crashed, server will forcibly shutdown.");
                final ThreadMXBean threadMXBean8 = ManagementFactory.getThreadMXBean();
                final ThreadInfo[] arr9 = threadMXBean8.dumpAllThreads(true, true);
                final StringBuilder stringBuilder10 = new StringBuilder();
                final Error error11 = new Error();
                for (final ThreadInfo threadInfo15 : arr9) {
                    if (threadInfo15.getThreadId() == this.server.getRunningThread().getId()) {
                        error11.setStackTrace(threadInfo15.getStackTrace());
                    }
                    stringBuilder10.append(threadInfo15);
                    stringBuilder10.append("\n");
                }
                final CrashReport l12 = new CrashReport("Watching Server", (Throwable)error11);
                this.server.fillReport(l12);
                final CrashReportCategory m13 = l12.addCategory("Thread Dump");
                m13.setDetail("Threads", stringBuilder10);
                final File file14 = new File(new File(this.server.getServerDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
                if (l12.saveToFile(file14)) {
                    ServerWatchdog.LOGGER.error("This crash report has been saved to: {}", file14.getAbsolutePath());
                }
                else {
                    ServerWatchdog.LOGGER.error("We were unable to save this crash report to disk.");
                }
                this.exit();
            }
            try {
                Thread.sleep(long2 + this.maxTickTime - long3);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    private void exit() {
        try {
            final Timer timer2 = new Timer();
            timer2.schedule((TimerTask)new TimerTask() {
                public void run() {
                    Runtime.getRuntime().halt(1);
                }
            }, 10000L);
            System.exit(1);
        }
        catch (Throwable throwable2) {
            Runtime.getRuntime().halt(1);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
