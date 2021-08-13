package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;

public final class IndirectMerger implements IndexMerger {
    private final DoubleArrayList result;
    private final IntArrayList firstIndices;
    private final IntArrayList secondIndices;
    
    protected IndirectMerger(final DoubleList doubleList1, final DoubleList doubleList2, final boolean boolean3, final boolean boolean4) {
        int integer6 = 0;
        int integer7 = 0;
        double double8 = Double.NaN;
        final int integer8 = doubleList1.size();
        final int integer9 = doubleList2.size();
        final int integer10 = integer8 + integer9;
        this.result = new DoubleArrayList(integer10);
        this.firstIndices = new IntArrayList(integer10);
        this.secondIndices = new IntArrayList(integer10);
        while (true) {
            final boolean boolean5 = integer6 < integer8;
            final boolean boolean6 = integer7 < integer9;
            if (!boolean5 && !boolean6) {
                break;
            }
            final boolean boolean7 = boolean5 && (!boolean6 || doubleList1.getDouble(integer6) < doubleList2.getDouble(integer7) + 1.0E-7);
            final double double9 = boolean7 ? doubleList1.getDouble(integer6++) : doubleList2.getDouble(integer7++);
            if ((integer6 == 0 || !boolean5) && !boolean7 && !boolean4) {
                continue;
            }
            if ((integer7 == 0 || !boolean6) && boolean7 && !boolean3) {
                continue;
            }
            if (double8 < double9 - 1.0E-7) {
                this.firstIndices.add(integer6 - 1);
                this.secondIndices.add(integer7 - 1);
                this.result.add(double9);
                double8 = double9;
            }
            else {
                if (this.result.isEmpty()) {
                    continue;
                }
                this.firstIndices.set(this.firstIndices.size() - 1, integer6 - 1);
                this.secondIndices.set(this.secondIndices.size() - 1, integer7 - 1);
            }
        }
        if (this.result.isEmpty()) {
            this.result.add(Math.min(doubleList1.getDouble(integer8 - 1), doubleList2.getDouble(integer9 - 1)));
        }
    }
    
    public boolean forMergedIndexes(final IndexConsumer a) {
        for (int integer3 = 0; integer3 < this.result.size() - 1; ++integer3) {
            if (!a.merge(this.firstIndices.getInt(integer3), this.secondIndices.getInt(integer3), integer3)) {
                return false;
            }
        }
        return true;
    }
    
    public DoubleList getList() {
        return (DoubleList)this.result;
    }
}
