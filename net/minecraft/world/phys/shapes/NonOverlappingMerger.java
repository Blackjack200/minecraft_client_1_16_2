package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

public class NonOverlappingMerger extends AbstractDoubleList implements IndexMerger {
    private final DoubleList lower;
    private final DoubleList upper;
    private final boolean swap;
    
    public NonOverlappingMerger(final DoubleList doubleList1, final DoubleList doubleList2, final boolean boolean3) {
        this.lower = doubleList1;
        this.upper = doubleList2;
        this.swap = boolean3;
    }
    
    public int size() {
        return this.lower.size() + this.upper.size();
    }
    
    public boolean forMergedIndexes(final IndexConsumer a) {
        if (this.swap) {
            return this.forNonSwappedIndexes((integer2, integer3, integer4) -> a.merge(integer3, integer2, integer4));
        }
        return this.forNonSwappedIndexes(a);
    }
    
    private boolean forNonSwappedIndexes(final IndexConsumer a) {
        final int integer3 = this.lower.size() - 1;
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            if (!a.merge(integer4, -1, integer4)) {
                return false;
            }
        }
        if (!a.merge(integer3, -1, integer3)) {
            return false;
        }
        for (int integer4 = 0; integer4 < this.upper.size(); ++integer4) {
            if (!a.merge(integer3, integer4, integer3 + 1 + integer4)) {
                return false;
            }
        }
        return true;
    }
    
    public double getDouble(final int integer) {
        if (integer < this.lower.size()) {
            return this.lower.getDouble(integer);
        }
        return this.upper.getDouble(integer - this.lower.size());
    }
    
    public DoubleList getList() {
        return (DoubleList)this;
    }
}
