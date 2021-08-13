package net.minecraft.world.phys.shapes;

import net.minecraft.core.Direction;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import java.util.Arrays;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public final class ArrayVoxelShape extends VoxelShape {
    private final DoubleList xs;
    private final DoubleList ys;
    private final DoubleList zs;
    
    protected ArrayVoxelShape(final DiscreteVoxelShape dct, final double[] arr2, final double[] arr3, final double[] arr4) {
        this(dct, (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(arr2, dct.getXSize() + 1)), (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(arr3, dct.getYSize() + 1)), (DoubleList)DoubleArrayList.wrap(Arrays.copyOf(arr4, dct.getZSize() + 1)));
    }
    
    ArrayVoxelShape(final DiscreteVoxelShape dct, final DoubleList doubleList2, final DoubleList doubleList3, final DoubleList doubleList4) {
        super(dct);
        final int integer6 = dct.getXSize() + 1;
        final int integer7 = dct.getYSize() + 1;
        final int integer8 = dct.getZSize() + 1;
        if (integer6 != doubleList2.size() || integer7 != doubleList3.size() || integer8 != doubleList4.size()) {
            throw Util.<IllegalArgumentException>pauseInIde(new IllegalArgumentException("Lengths of point arrays must be consistent with the size of the VoxelShape."));
        }
        this.xs = doubleList2;
        this.ys = doubleList3;
        this.zs = doubleList4;
    }
    
    @Override
    protected DoubleList getCoords(final Direction.Axis a) {
        switch (a) {
            case X: {
                return this.xs;
            }
            case Y: {
                return this.ys;
            }
            case Z: {
                return this.zs;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
