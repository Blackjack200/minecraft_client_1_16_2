package net.minecraft;

import org.apache.logging.log4j.Logger;

public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Logger logger;
    
    public DefaultUncaughtExceptionHandler(final Logger logger) {
        this.logger = logger;
    }
    
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        this.logger.error("Caught previously unhandled exception :", throwable);
    }
}
