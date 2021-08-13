package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

public class IdenticalMerger implements IndexMerger {
    private final DoubleList coords;
    
    public IdenticalMerger(final DoubleList doubleList) {
        this.coords = doubleList;
    }
    
    public boolean forMergedIndexes(final IndexConsumer a) {
        for (int integer3 = 0; integer3 <= this.coords.size(); ++integer3) {
            if (!a.merge(integer3, integer3, integer3)) {
                return false;
            }
        }
        return true;
    }
    
    public DoubleList getList() {
        return this.coords;
    }
}
