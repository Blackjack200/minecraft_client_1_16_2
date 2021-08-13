package net.minecraft.gametest.framework;

import java.util.function.Consumer;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Collection;

public class MultipleTestTracker {
    private final Collection<GameTestInfo> tests;
    @Nullable
    private Collection<GameTestListener> listeners;
    
    public MultipleTestTracker() {
        this.tests = (Collection<GameTestInfo>)Lists.newArrayList();
        this.listeners = (Collection<GameTestListener>)Lists.newArrayList();
    }
    
    public MultipleTestTracker(final Collection<GameTestInfo> collection) {
        this.tests = (Collection<GameTestInfo>)Lists.newArrayList();
        this.listeners = (Collection<GameTestListener>)Lists.newArrayList();
        this.tests.addAll((Collection)collection);
    }
    
    public void addTestToTrack(final GameTestInfo lf) {
        this.tests.add(lf);
        this.listeners.forEach(lf::addListener);
    }
    
    public void addListener(final GameTestListener lg) {
        this.listeners.add(lg);
        this.tests.forEach(lf -> lf.addListener(lg));
    }
    
    public void addFailureListener(final Consumer<GameTestInfo> consumer) {
        this.addListener(new GameTestListener() {
            public void testStructureLoaded(final GameTestInfo lf) {
            }
            
            public void testFailed(final GameTestInfo lf) {
                consumer.accept(lf);
            }
        });
    }
    
    public int getFailedRequiredCount() {
        return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isRequired).count();
    }
    
    public int getFailedOptionalCount() {
        return (int)this.tests.stream().filter(GameTestInfo::hasFailed).filter(GameTestInfo::isOptional).count();
    }
    
    public int getDoneCount() {
        return (int)this.tests.stream().filter(GameTestInfo::isDone).count();
    }
    
    public boolean hasFailedRequired() {
        return this.getFailedRequiredCount() > 0;
    }
    
    public boolean hasFailedOptional() {
        return this.getFailedOptionalCount() > 0;
    }
    
    public int getTotalCount() {
        return this.tests.size();
    }
    
    public boolean isDone() {
        return this.getDoneCount() == this.getTotalCount();
    }
    
    public String getProgressBar() {
        final StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append('[');
        this.tests.forEach(lf -> {
            if (!lf.hasStarted()) {
                stringBuffer2.append(' ');
            }
            else if (lf.hasSucceeded()) {
                stringBuffer2.append('+');
            }
            else if (lf.hasFailed()) {
                stringBuffer2.append(lf.isRequired() ? 'X' : 'x');
            }
            else {
                stringBuffer2.append('_');
            }
        });
        stringBuffer2.append(']');
        return stringBuffer2.toString();
    }
    
    public String toString() {
        return this.getProgressBar();
    }
}
