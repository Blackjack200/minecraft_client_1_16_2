package net.minecraft.world.level.newbiome.layer;

import net.minecraft.world.level.newbiome.context.Context;
import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;

public enum BiomeEdgeLayer implements CastleTransformer {
    INSTANCE;
    
    public int apply(final Context cvh, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        final int[] arr8 = { 0 };
        if (this.checkEdge(arr8, integer6) || this.checkEdgeStrict(arr8, integer2, integer3, integer4, integer5, integer6, 38, 37) || this.checkEdgeStrict(arr8, integer2, integer3, integer4, integer5, integer6, 39, 37) || this.checkEdgeStrict(arr8, integer2, integer3, integer4, integer5, integer6, 32, 5)) {
            return arr8[0];
        }
        if (integer6 == 2 && (integer2 == 12 || integer3 == 12 || integer5 == 12 || integer4 == 12)) {
            return 34;
        }
        if (integer6 == 6) {
            if (integer2 == 2 || integer3 == 2 || integer5 == 2 || integer4 == 2 || integer2 == 30 || integer3 == 30 || integer5 == 30 || integer4 == 30 || integer2 == 12 || integer3 == 12 || integer5 == 12 || integer4 == 12) {
                return 1;
            }
            if (integer2 == 21 || integer4 == 21 || integer3 == 21 || integer5 == 21 || integer2 == 168 || integer4 == 168 || integer3 == 168 || integer5 == 168) {
                return 23;
            }
        }
        return integer6;
    }
    
    private boolean checkEdge(final int[] arr, final int integer) {
        if (!Layers.isSame(integer, 3)) {
            return false;
        }
        arr[0] = integer;
        return true;
    }
    
    private boolean checkEdgeStrict(final int[] arr, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
        if (integer6 != integer7) {
            return false;
        }
        if (Layers.isSame(integer2, integer7) && Layers.isSame(integer3, integer7) && Layers.isSame(integer5, integer7) && Layers.isSame(integer4, integer7)) {
            arr[0] = integer6;
        }
        else {
            arr[0] = integer8;
        }
        return true;
    }
}
