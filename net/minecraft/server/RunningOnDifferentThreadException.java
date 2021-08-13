package net.minecraft.server;

public final class RunningOnDifferentThreadException extends RuntimeException {
    public static final RunningOnDifferentThreadException RUNNING_ON_DIFFERENT_THREAD;
    
    private RunningOnDifferentThreadException() {
        this.setStackTrace(new StackTraceElement[0]);
    }
    
    public synchronized Throwable fillInStackTrace() {
        this.setStackTrace(new StackTraceElement[0]);
        return (Throwable)this;
    }
    
    static {
        RUNNING_ON_DIFFERENT_THREAD = new RunningOnDifferentThreadException();
    }
}
