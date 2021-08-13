package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import com.google.common.math.IntMath;

public final class DiscreteCubeMerger implements IndexMerger {
    private final CubePointRange result;
    private final int firstSize;
    private final int secondSize;
    private final int gcd;
    
    DiscreteCubeMerger(final int integer1, final int integer2) {
        this.result = new CubePointRange((int)Shapes.lcm(integer1, integer2));
        this.firstSize = integer1;
        this.secondSize = integer2;
        this.gcd = IntMath.gcd(integer1, integer2);
    }
    
    public boolean forMergedIndexes(final IndexConsumer a) {
        final int integer3 = this.firstSize / this.gcd;
        final int integer4 = this.secondSize / this.gcd;
        for (int integer5 = 0; integer5 <= this.result.size(); ++integer5) {
            if (!a.merge(integer5 / integer4, integer5 / integer3, integer5)) {
                return false;
            }
        }
        return true;
    }
    
    public DoubleList getList() {
        return (DoubleList)this.result;
    }
}
