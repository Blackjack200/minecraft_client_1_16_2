package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;

public class SliceShape extends VoxelShape {
    private final VoxelShape delegate;
    private final Direction.Axis axis;
    private static final DoubleList SLICE_COORDS;
    
    public SliceShape(final VoxelShape dde, final Direction.Axis a, final int integer) {
        super(makeSlice(dde.shape, a, integer));
        this.delegate = dde;
        this.axis = a;
    }
    
    private static DiscreteVoxelShape makeSlice(final DiscreteVoxelShape dct, final Direction.Axis a, final int integer) {
        return new SubShape(dct, a.choose(integer, 0, 0), a.choose(0, integer, 0), a.choose(0, 0, integer), a.choose(integer + 1, dct.xSize, dct.xSize), a.choose(dct.ySize, integer + 1, dct.ySize), a.choose(dct.zSize, dct.zSize, integer + 1));
    }
    
    @Override
    protected DoubleList getCoords(final Direction.Axis a) {
        if (a == this.axis) {
            return SliceShape.SLICE_COORDS;
        }
        return this.delegate.getCoords(a);
    }
    
    static {
        SLICE_COORDS = (DoubleList)new CubePointRange(1);
    }
}
