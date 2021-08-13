package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

public class OffsetDoubleList extends AbstractDoubleList {
    private final DoubleList delegate;
    private final double offset;
    
    public OffsetDoubleList(final DoubleList doubleList, final double double2) {
        this.delegate = doubleList;
        this.offset = double2;
    }
    
    public double getDouble(final int integer) {
        return this.delegate.getDouble(integer) + this.offset;
    }
    
    public int size() {
        return this.delegate.size();
    }
}
