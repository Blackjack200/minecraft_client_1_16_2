package net.minecraft.world.level;

import net.minecraft.core.Vec3i;
import java.util.Iterator;
import net.minecraft.core.BlockPos;
import com.google.common.collect.Lists;
import java.util.List;

public class PotentialCalculator {
    private final List<PointCharge> charges;
    
    public PotentialCalculator() {
        this.charges = (List<PointCharge>)Lists.newArrayList();
    }
    
    public void addCharge(final BlockPos fx, final double double2) {
        if (double2 != 0.0) {
            this.charges.add(new PointCharge(fx, double2));
        }
    }
    
    public double getPotentialEnergyChange(final BlockPos fx, final double double2) {
        if (double2 == 0.0) {
            return 0.0;
        }
        double double3 = 0.0;
        for (final PointCharge a8 : this.charges) {
            double3 += a8.getPotentialChange(fx);
        }
        return double3 * double2;
    }
    
    static class PointCharge {
        private final BlockPos pos;
        private final double charge;
        
        public PointCharge(final BlockPos fx, final double double2) {
            this.pos = fx;
            this.charge = double2;
        }
        
        public double getPotentialChange(final BlockPos fx) {
            final double double3 = this.pos.distSqr(fx);
            if (double3 == 0.0) {
                return Double.POSITIVE_INFINITY;
            }
            return this.charge / Math.sqrt(double3);
        }
    }
}
