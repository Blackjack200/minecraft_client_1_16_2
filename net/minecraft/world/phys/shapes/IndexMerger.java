package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

interface IndexMerger {
    DoubleList getList();
    
    boolean forMergedIndexes(final IndexConsumer a);
    
    public interface IndexConsumer {
        boolean merge(final int integer1, final int integer2, final int integer3);
    }
}
