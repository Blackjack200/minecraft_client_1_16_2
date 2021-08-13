package com.mojang.blaze3d.platform;

import java.util.OptionalInt;

public class DisplayData {
    public final int width;
    public final int height;
    public final OptionalInt fullscreenWidth;
    public final OptionalInt fullscreenHeight;
    public final boolean isFullscreen;
    
    public DisplayData(final int integer1, final int integer2, final OptionalInt optionalInt3, final OptionalInt optionalInt4, final boolean boolean5) {
        this.width = integer1;
        this.height = integer2;
        this.fullscreenWidth = optionalInt3;
        this.fullscreenHeight = optionalInt4;
        this.isFullscreen = boolean5;
    }
}
