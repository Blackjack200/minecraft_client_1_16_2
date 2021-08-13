package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;

public enum OceanMixerLayer implements AreaTransformer2, DimensionOffset0Transformer {
    INSTANCE;
    
    public int applyPixel(final Context cvh, final Area cvc2, final Area cvc3, final int integer4, final int integer5) {
        final int integer6 = cvc2.get(this.getParentX(integer4), this.getParentY(integer5));
        final int integer7 = cvc3.get(this.getParentX(integer4), this.getParentY(integer5));
        if (!Layers.isOcean(integer6)) {
            return integer6;
        }
        final int integer8 = 8;
        final int integer9 = 4;
        for (int integer10 = -8; integer10 <= 8; integer10 += 4) {
            for (int integer11 = -8; integer11 <= 8; integer11 += 4) {
                final int integer12 = cvc2.get(this.getParentX(integer4 + integer10), this.getParentY(integer5 + integer11));
                if (!Layers.isOcean(integer12)) {
                    if (integer7 == 44) {
                        return 45;
                    }
                    if (integer7 == 10) {
                        return 46;
                    }
                }
            }
        }
        if (integer6 == 24) {
            if (integer7 == 45) {
                return 48;
            }
            if (integer7 == 0) {
                return 24;
            }
            if (integer7 == 46) {
                return 49;
            }
            if (integer7 == 10) {
                return 50;
            }
        }
        return integer7;
    }
}
