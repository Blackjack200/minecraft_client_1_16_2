package com.mojang.blaze3d.font;

public interface GlyphInfo {
    float getAdvance();
    
    default float getAdvance(final boolean boolean1) {
        return this.getAdvance() + (boolean1 ? this.getBoldOffset() : 0.0f);
    }
    
    default float getBearingX() {
        return 0.0f;
    }
    
    default float getBoldOffset() {
        return 1.0f;
    }
    
    default float getShadowOffset() {
        return 1.0f;
    }
}
