package com.mojang.math;

import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.Arrays;

public enum SymmetricGroup3 {
    P123(0, 1, 2), 
    P213(1, 0, 2), 
    P132(0, 2, 1), 
    P231(1, 2, 0), 
    P312(2, 0, 1), 
    P321(2, 1, 0);
    
    private final int[] permutation;
    private final Matrix3f transformation;
    private static final SymmetricGroup3[][] cayleyTable;
    
    private SymmetricGroup3(final int integer3, final int integer4, final int integer5) {
        this.permutation = new int[] { integer3, integer4, integer5 };
        (this.transformation = new Matrix3f()).set(0, this.permutation(0), 1.0f);
        this.transformation.set(1, this.permutation(1), 1.0f);
        this.transformation.set(2, this.permutation(2), 1.0f);
    }
    
    public SymmetricGroup3 compose(final SymmetricGroup3 e) {
        return SymmetricGroup3.cayleyTable[this.ordinal()][e.ordinal()];
    }
    
    public int permutation(final int integer) {
        return this.permutation[integer];
    }
    
    public Matrix3f transformation() {
        return this.transformation;
    }
    
    static {
        cayleyTable = Util.<SymmetricGroup3[][]>make(new SymmetricGroup3[values().length][values().length], (java.util.function.Consumer<SymmetricGroup3[][]>)(arr -> {
            for (final SymmetricGroup3 e5 : values()) {
                for (final SymmetricGroup3 e6 : values()) {
                    final int[] arr2 = new int[3];
                    for (int integer11 = 0; integer11 < 3; ++integer11) {
                        arr2[integer11] = e5.permutation[e6.permutation[integer11]];
                    }
                    final SymmetricGroup3 e7 = (SymmetricGroup3)Arrays.stream((Object[])values()).filter(e -> Arrays.equals(e.permutation, arr2)).findFirst().get();
                    arr[e5.ordinal()][e6.ordinal()] = e7;
                }
            }
        }));
    }
}
