package com.mojang.blaze3d.font;

public interface RawGlyph extends GlyphInfo {
    int getPixelWidth();
    
    int getPixelHeight();
    
    void upload(final int integer1, final int integer2);
    
    boolean isColored();
    
    float getOversample();
    
    default float getLeft() {
        return this.getBearingX();
    }
    
    default float getRight() {
        return this.getLeft() + this.getPixelWidth() / this.getOversample();
    }
    
    default float getUp() {
        return this.getBearingY();
    }
    
    default float getDown() {
        return this.getUp() + this.getPixelHeight() / this.getOversample();
    }
    
    default float getBearingY() {
        return 3.0f;
    }
}
