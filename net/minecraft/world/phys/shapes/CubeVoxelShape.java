package net.minecraft.world.phys.shapes;

import net.minecraft.util.Mth;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;

public final class CubeVoxelShape extends VoxelShape {
    protected CubeVoxelShape(final DiscreteVoxelShape dct) {
        super(dct);
    }
    
    @Override
    protected DoubleList getCoords(final Direction.Axis a) {
        return (DoubleList)new CubePointRange(this.shape.getSize(a));
    }
    
    @Override
    protected int findIndex(final Direction.Axis a, final double double2) {
        final int integer5 = this.shape.getSize(a);
        return Mth.clamp(Mth.floor(double2 * integer5), -1, integer5);
    }
}
