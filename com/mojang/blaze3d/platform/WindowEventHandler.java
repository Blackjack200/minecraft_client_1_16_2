package com.mojang.blaze3d.platform;

public interface WindowEventHandler {
    void setWindowActive(final boolean boolean1);
    
    void resizeDisplay();
    
    void cursorEntered();
}
