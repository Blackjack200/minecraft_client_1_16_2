package net.minecraft.world.level.newbiome.layer;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.level.newbiome.context.Context;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public enum ShoreLayer implements CastleTransformer {
    INSTANCE;
    
    private static final IntSet SNOWY;
    private static final IntSet JUNGLES;
    
    public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        if (integer6 == 14) {
            if (Layers.isShallowOcean(integer2) || Layers.isShallowOcean(integer3) || Layers.isShallowOcean(integer4) || Layers.isShallowOcean(integer5)) {
                return 15;
            }
        }
        else if (ShoreLayer.JUNGLES.contains(integer6)) {
            if (!isJungleCompatible(integer2) || !isJungleCompatible(integer3) || !isJungleCompatible(integer4) || !isJungleCompatible(integer5)) {
                return 23;
            }
            if (Layers.isOcean(integer2) || Layers.isOcean(integer3) || Layers.isOcean(integer4) || Layers.isOcean(integer5)) {
                return 16;
            }
        }
        else if (integer6 == 3 || integer6 == 34 || integer6 == 20) {
            if (!Layers.isOcean(integer6) && (Layers.isOcean(integer2) || Layers.isOcean(integer3) || Layers.isOcean(integer4) || Layers.isOcean(integer5))) {
                return 25;
            }
        }
        else if (ShoreLayer.SNOWY.contains(integer6)) {
            if (!Layers.isOcean(integer6) && (Layers.isOcean(integer2) || Layers.isOcean(integer3) || Layers.isOcean(integer4) || Layers.isOcean(integer5))) {
                return 26;
            }
        }
        else if (integer6 == 37 || integer6 == 38) {
            if (!Layers.isOcean(integer2) && !Layers.isOcean(integer3) && !Layers.isOcean(integer4) && !Layers.isOcean(integer5) && (!this.isMesa(integer2) || !this.isMesa(integer3) || !this.isMesa(integer4) || !this.isMesa(integer5))) {
                return 2;
            }
        }
        else if (!Layers.isOcean(integer6) && integer6 != 7 && integer6 != 6 && (Layers.isOcean(integer2) || Layers.isOcean(integer3) || Layers.isOcean(integer4) || Layers.isOcean(integer5))) {
            return 16;
        }
        return integer6;
    }
    
    private static boolean isJungleCompatible(final int integer) {
        return ShoreLayer.JUNGLES.contains(integer) || integer == 4 || integer == 5 || Layers.isOcean(integer);
    }
    
    private boolean isMesa(final int integer) {
        return integer == 37 || integer == 38 || integer == 39 || integer == 165 || integer == 166 || integer == 167;
    }
    
    static {
        SNOWY = (IntSet)new IntOpenHashSet(new int[] { 26, 11, 12, 13, 140, 30, 31, 158, 10 });
        JUNGLES = (IntSet)new IntOpenHashSet(new int[] { 168, 169, 21, 22, 23, 149, 151 });
    }
}
