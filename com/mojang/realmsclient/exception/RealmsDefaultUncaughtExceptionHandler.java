package com.mojang.realmsclient.exception;

import org.apache.logging.log4j.Logger;

public class RealmsDefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Logger logger;
    
    public RealmsDefaultUncaughtExceptionHandler(final Logger logger) {
        this.logger = logger;
    }
    
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        this.logger.error("Caught previously unhandled exception :");
        this.logger.error(throwable);
    }
}
