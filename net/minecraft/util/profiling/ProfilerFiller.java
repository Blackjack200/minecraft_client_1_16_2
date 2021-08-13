package net.minecraft.util.profiling;

import java.util.function.Supplier;

public interface ProfilerFiller {
    void startTick();
    
    void endTick();
    
    void push(final String string);
    
    void push(final Supplier<String> supplier);
    
    void pop();
    
    void popPush(final String string);
    
    void popPush(final Supplier<String> supplier);
    
    void incrementCounter(final String string);
    
    void incrementCounter(final Supplier<String> supplier);
    
    default ProfilerFiller tee(final ProfilerFiller ant1, final ProfilerFiller ant2) {
        if (ant1 == InactiveProfiler.INSTANCE) {
            return ant2;
        }
        if (ant2 == InactiveProfiler.INSTANCE) {
            return ant1;
        }
        return new ProfilerFiller() {
            public void startTick() {
                ant1.startTick();
                ant2.startTick();
            }
            
            public void endTick() {
                ant1.endTick();
                ant2.endTick();
            }
            
            public void push(final String string) {
                ant1.push(string);
                ant2.push(string);
            }
            
            public void push(final Supplier<String> supplier) {
                ant1.push(supplier);
                ant2.push(supplier);
            }
            
            public void pop() {
                ant1.pop();
                ant2.pop();
            }
            
            public void popPush(final String string) {
                ant1.popPush(string);
                ant2.popPush(string);
            }
            
            public void popPush(final Supplier<String> supplier) {
                ant1.popPush(supplier);
                ant2.popPush(supplier);
            }
            
            public void incrementCounter(final String string) {
                ant1.incrementCounter(string);
                ant2.incrementCounter(string);
            }
            
            public void incrementCounter(final Supplier<String> supplier) {
                ant1.incrementCounter(supplier);
                ant2.incrementCounter(supplier);
            }
        };
    }
}
