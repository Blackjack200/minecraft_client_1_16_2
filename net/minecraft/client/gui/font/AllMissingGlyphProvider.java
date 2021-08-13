package net.minecraft.client.gui.font;

import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.ints.IntSet;
import javax.annotation.Nullable;
import net.minecraft.client.gui.font.glyphs.MissingGlyph;
import com.mojang.blaze3d.font.RawGlyph;
import com.mojang.blaze3d.font.GlyphProvider;

public class AllMissingGlyphProvider implements GlyphProvider {
    @Nullable
    public RawGlyph getGlyph(final int integer) {
        return MissingGlyph.INSTANCE;
    }
    
    public IntSet getSupportedGlyphs() {
        return (IntSet)IntSets.EMPTY_SET;
    }
}
