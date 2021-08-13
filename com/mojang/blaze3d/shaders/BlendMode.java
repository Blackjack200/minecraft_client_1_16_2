package com.mojang.blaze3d.shaders;

import java.util.Locale;
import com.mojang.blaze3d.systems.RenderSystem;

public class BlendMode {
    private static BlendMode lastApplied;
    private final int srcColorFactor;
    private final int srcAlphaFactor;
    private final int dstColorFactor;
    private final int dstAlphaFactor;
    private final int blendFunc;
    private final boolean separateBlend;
    private final boolean opaque;
    
    private BlendMode(final boolean boolean1, final boolean boolean2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        this.separateBlend = boolean1;
        this.srcColorFactor = integer3;
        this.dstColorFactor = integer4;
        this.srcAlphaFactor = integer5;
        this.dstAlphaFactor = integer6;
        this.opaque = boolean2;
        this.blendFunc = integer7;
    }
    
    public BlendMode() {
        this(false, true, 1, 0, 1, 0, 32774);
    }
    
    public BlendMode(final int integer1, final int integer2, final int integer3) {
        this(false, false, integer1, integer2, integer1, integer2, integer3);
    }
    
    public BlendMode(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
        this(true, false, integer1, integer2, integer3, integer4, integer5);
    }
    
    public void apply() {
        if (this.equals(BlendMode.lastApplied)) {
            return;
        }
        if (BlendMode.lastApplied == null || this.opaque != BlendMode.lastApplied.isOpaque()) {
            BlendMode.lastApplied = this;
            if (this.opaque) {
                RenderSystem.disableBlend();
                return;
            }
            RenderSystem.enableBlend();
        }
        RenderSystem.blendEquation(this.blendFunc);
        if (this.separateBlend) {
            RenderSystem.blendFuncSeparate(this.srcColorFactor, this.dstColorFactor, this.srcAlphaFactor, this.dstAlphaFactor);
        }
        else {
            RenderSystem.blendFunc(this.srcColorFactor, this.dstColorFactor);
        }
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BlendMode)) {
            return false;
        }
        final BlendMode dez3 = (BlendMode)object;
        return this.blendFunc == dez3.blendFunc && this.dstAlphaFactor == dez3.dstAlphaFactor && this.dstColorFactor == dez3.dstColorFactor && this.opaque == dez3.opaque && this.separateBlend == dez3.separateBlend && this.srcAlphaFactor == dez3.srcAlphaFactor && this.srcColorFactor == dez3.srcColorFactor;
    }
    
    public int hashCode() {
        int integer2 = this.srcColorFactor;
        integer2 = 31 * integer2 + this.srcAlphaFactor;
        integer2 = 31 * integer2 + this.dstColorFactor;
        integer2 = 31 * integer2 + this.dstAlphaFactor;
        integer2 = 31 * integer2 + this.blendFunc;
        integer2 = 31 * integer2 + (this.separateBlend ? 1 : 0);
        integer2 = 31 * integer2 + (this.opaque ? 1 : 0);
        return integer2;
    }
    
    public boolean isOpaque() {
        return this.opaque;
    }
    
    public static int stringToBlendFunc(final String string) {
        final String string2 = string.trim().toLowerCase(Locale.ROOT);
        if ("add".equals(string2)) {
            return 32774;
        }
        if ("subtract".equals(string2)) {
            return 32778;
        }
        if ("reversesubtract".equals(string2)) {
            return 32779;
        }
        if ("reverse_subtract".equals(string2)) {
            return 32779;
        }
        if ("min".equals(string2)) {
            return 32775;
        }
        if ("max".equals(string2)) {
            return 32776;
        }
        return 32774;
    }
    
    public static int stringToBlendFactor(final String string) {
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        string2 = string2.replaceAll("_", "");
        string2 = string2.replaceAll("one", "1");
        string2 = string2.replaceAll("zero", "0");
        string2 = string2.replaceAll("minus", "-");
        if ("0".equals(string2)) {
            return 0;
        }
        if ("1".equals(string2)) {
            return 1;
        }
        if ("srccolor".equals(string2)) {
            return 768;
        }
        if ("1-srccolor".equals(string2)) {
            return 769;
        }
        if ("dstcolor".equals(string2)) {
            return 774;
        }
        if ("1-dstcolor".equals(string2)) {
            return 775;
        }
        if ("srcalpha".equals(string2)) {
            return 770;
        }
        if ("1-srcalpha".equals(string2)) {
            return 771;
        }
        if ("dstalpha".equals(string2)) {
            return 772;
        }
        if ("1-dstalpha".equals(string2)) {
            return 773;
        }
        return -1;
    }
}
