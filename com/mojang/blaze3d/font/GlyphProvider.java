package com.mojang.blaze3d.font;

import it.unimi.dsi.fastutil.ints.IntSet;
import javax.annotation.Nullable;
import java.io.Closeable;

public interface GlyphProvider extends Closeable {
    default void close() {
    }
    
    @Nullable
    default RawGlyph getGlyph(final int integer) {
        return null;
    }
    
    IntSet getSupportedGlyphs();
}
