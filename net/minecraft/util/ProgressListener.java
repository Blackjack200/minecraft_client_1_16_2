package net.minecraft.util;

import net.minecraft.network.chat.Component;

public interface ProgressListener {
    void progressStartNoAbort(final Component nr);
    
    void progressStart(final Component nr);
    
    void progressStage(final Component nr);
    
    void progressStagePercentage(final int integer);
    
    void stop();
}
