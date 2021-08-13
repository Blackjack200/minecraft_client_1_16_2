package net.minecraft.util;

public class SmoothDouble {
    private double targetValue;
    private double remainingValue;
    private double lastAmount;
    
    public double getNewDeltaValue(final double double1, final double double2) {
        this.targetValue += double1;
        double double3 = this.targetValue - this.remainingValue;
        final double double4 = Mth.lerp(0.5, this.lastAmount, double3);
        final double double5 = Math.signum(double3);
        if (double5 * double3 > double5 * this.lastAmount) {
            double3 = double4;
        }
        this.lastAmount = double4;
        this.remainingValue += double3 * double2;
        return double3 * double2;
    }
    
    public void reset() {
        this.targetValue = 0.0;
        this.remainingValue = 0.0;
        this.lastAmount = 0.0;
    }
}
