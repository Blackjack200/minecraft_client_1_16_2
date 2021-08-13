package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

public class CubePointRange extends AbstractDoubleList {
    private final int parts;
    
    CubePointRange(final int integer) {
        this.parts = integer;
    }
    
    public double getDouble(final int integer) {
        return integer / (double)this.parts;
    }
    
    public int size() {
        return this.parts + 1;
    }
}
