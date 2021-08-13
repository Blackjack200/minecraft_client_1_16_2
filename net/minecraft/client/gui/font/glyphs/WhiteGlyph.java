package net.minecraft.client.gui.font.glyphs;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.font.RawGlyph;

public enum WhiteGlyph implements RawGlyph {
    INSTANCE;
    
    private static final NativeImage IMAGE_DATA;
    
    public int getPixelWidth() {
        return 5;
    }
    
    public int getPixelHeight() {
        return 8;
    }
    
    public float getAdvance() {
        return 6.0f;
    }
    
    public float getOversample() {
        return 1.0f;
    }
    
    public void upload(final int integer1, final int integer2) {
        WhiteGlyph.IMAGE_DATA.upload(0, integer1, integer2, false);
    }
    
    public boolean isColored() {
        return true;
    }
    
    static {
        IMAGE_DATA = Util.<NativeImage>make(new NativeImage(NativeImage.Format.RGBA, 5, 8, false), (java.util.function.Consumer<NativeImage>)(deq -> {
            for (int integer2 = 0; integer2 < 8; ++integer2) {
                for (int integer3 = 0; integer3 < 5; ++integer3) {
                    final boolean boolean4 = integer3 == 0 || integer3 + 1 == 5 || integer2 == 0 || integer2 + 1 == 8;
                    deq.setPixelRGBA(integer3, integer2, -1);
                }
            }
            deq.untrack();
        }));
    }
}
