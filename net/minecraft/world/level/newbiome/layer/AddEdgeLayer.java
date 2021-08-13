package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.layer.traits.C0Transformer;
import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public class AddEdgeLayer {
    public enum CoolWarm implements CastleTransformer {
        INSTANCE;
        
        public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
            if (integer6 == 1 && (integer2 == 3 || integer3 == 3 || integer5 == 3 || integer4 == 3 || integer2 == 4 || integer3 == 4 || integer5 == 4 || integer4 == 4)) {
                return 2;
            }
            return integer6;
        }
    }
    
    public enum HeatIce implements CastleTransformer {
        INSTANCE;
        
        public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
            if (integer6 == 4 && (integer2 == 1 || integer3 == 1 || integer5 == 1 || integer4 == 1 || integer2 == 2 || integer3 == 2 || integer5 == 2 || integer4 == 2)) {
                return 3;
            }
            return integer6;
        }
    }
    
    public enum IntroduceSpecial implements C0Transformer {
        INSTANCE;
        
        public int apply(final Context cvh, int integer) {
            if (!Layers.isShallowOcean(integer) && cvh.nextRandom(13) == 0) {
                integer |= (1 + cvh.nextRandom(15) << 8 & 0xF00);
            }
            return integer;
        }
    }
}
