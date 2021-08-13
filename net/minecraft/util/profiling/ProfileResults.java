package net.minecraft.util.profiling;

import java.io.File;
import java.util.List;

public interface ProfileResults {
    List<ResultField> getTimes(final String string);
    
    boolean saveResults(final File file);
    
    long getStartTimeNano();
    
    int getStartTimeTicks();
    
    long getEndTimeNano();
    
    int getEndTimeTicks();
    
    default long getNanoDuration() {
        return this.getEndTimeNano() - this.getStartTimeNano();
    }
    
    default int getTickDuration() {
        return this.getEndTimeTicks() - this.getStartTimeTicks();
    }
    
    default String demanglePath(final String string) {
        return string.replace('\u001e', '.');
    }
}
