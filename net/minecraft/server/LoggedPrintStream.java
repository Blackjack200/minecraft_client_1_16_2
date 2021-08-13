package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import javax.annotation.Nullable;
import java.io.OutputStream;
import org.apache.logging.log4j.Logger;
import java.io.PrintStream;

public class LoggedPrintStream extends PrintStream {
    protected static final Logger LOGGER;
    protected final String name;
    
    public LoggedPrintStream(final String string, final OutputStream outputStream) {
        super(outputStream);
        this.name = string;
    }
    
    public void println(@Nullable final String string) {
        this.logLine(string);
    }
    
    public void println(final Object object) {
        this.logLine(String.valueOf(object));
    }
    
    protected void logLine(@Nullable final String string) {
        LoggedPrintStream.LOGGER.info("[{}]: {}", this.name, string);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
