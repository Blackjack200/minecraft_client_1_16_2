package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer1;

public enum ZoomLayer implements AreaTransformer1 {
    NORMAL, 
    FUZZY {
        @Override
        protected int modeOrRandom(final BigContext<?> cvg, final int integer2, final int integer3, final int integer4, final int integer5) {
            return cvg.random(integer2, integer3, integer4, integer5);
        }
    };
    
    public int getParentX(final int integer) {
        return integer >> 1;
    }
    
    public int getParentY(final int integer) {
        return integer >> 1;
    }
    
    public int applyPixel(final BigContext<?> cvg, final Area cvc, final int integer3, final int integer4) {
        final int integer5 = cvc.get(this.getParentX(integer3), this.getParentY(integer4));
        cvg.initRandom(integer3 >> 1 << 1, integer4 >> 1 << 1);
        final int integer6 = integer3 & 0x1;
        final int integer7 = integer4 & 0x1;
        if (integer6 == 0 && integer7 == 0) {
            return integer5;
        }
        final int integer8 = cvc.get(this.getParentX(integer3), this.getParentY(integer4 + 1));
        final int integer9 = cvg.random(integer5, integer8);
        if (integer6 == 0 && integer7 == 1) {
            return integer9;
        }
        final int integer10 = cvc.get(this.getParentX(integer3 + 1), this.getParentY(integer4));
        final int integer11 = cvg.random(integer5, integer10);
        if (integer6 == 1 && integer7 == 0) {
            return integer11;
        }
        final int integer12 = cvc.get(this.getParentX(integer3 + 1), this.getParentY(integer4 + 1));
        return this.modeOrRandom(cvg, integer5, integer10, integer8, integer12);
    }
    
    protected int modeOrRandom(final BigContext<?> cvg, final int integer2, final int integer3, final int integer4, final int integer5) {
        if (integer3 == integer4 && integer4 == integer5) {
            return integer3;
        }
        if (integer2 == integer3 && integer2 == integer4) {
            return integer2;
        }
        if (integer2 == integer3 && integer2 == integer5) {
            return integer2;
        }
        if (integer2 == integer4 && integer2 == integer5) {
            return integer2;
        }
        if (integer2 == integer3 && integer4 != integer5) {
            return integer2;
        }
        if (integer2 == integer4 && integer3 != integer5) {
            return integer2;
        }
        if (integer2 == integer5 && integer3 != integer4) {
            return integer2;
        }
        if (integer3 == integer4 && integer2 != integer5) {
            return integer3;
        }
        if (integer3 == integer5 && integer2 != integer4) {
            return integer3;
        }
        if (integer4 == integer5 && integer2 != integer3) {
            return integer4;
        }
        return cvg.random(integer2, integer3, integer4, integer5);
    }
}
