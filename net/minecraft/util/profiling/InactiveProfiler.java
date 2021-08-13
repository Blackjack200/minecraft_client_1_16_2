package net.minecraft.util.profiling;

import java.util.function.Supplier;

public class InactiveProfiler implements ProfileCollector {
    public static final InactiveProfiler INSTANCE;
    
    private InactiveProfiler() {
    }
    
    public void startTick() {
    }
    
    public void endTick() {
    }
    
    public void push(final String string) {
    }
    
    public void push(final Supplier<String> supplier) {
    }
    
    public void pop() {
    }
    
    public void popPush(final String string) {
    }
    
    public void popPush(final Supplier<String> supplier) {
    }
    
    public void incrementCounter(final String string) {
    }
    
    public void incrementCounter(final Supplier<String> supplier) {
    }
    
    public ProfileResults getResults() {
        return EmptyProfileResults.EMPTY;
    }
    
    static {
        INSTANCE = new InactiveProfiler();
    }
}
