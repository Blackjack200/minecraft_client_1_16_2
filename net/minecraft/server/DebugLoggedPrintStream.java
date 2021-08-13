package net.minecraft.server;

import java.io.OutputStream;

public class DebugLoggedPrintStream extends LoggedPrintStream {
    public DebugLoggedPrintStream(final String string, final OutputStream outputStream) {
        super(string, outputStream);
    }
    
    @Override
    protected void logLine(final String string) {
        final StackTraceElement[] arr3 = Thread.currentThread().getStackTrace();
        final StackTraceElement stackTraceElement4 = arr3[Math.min(3, arr3.length)];
        DebugLoggedPrintStream.LOGGER.info("[{}]@.({}:{}): {}", this.name, stackTraceElement4.getFileName(), stackTraceElement4.getLineNumber(), string);
    }
}
