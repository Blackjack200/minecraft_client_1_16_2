package net.minecraft.world.entity.ai.attributes;

import net.minecraft.util.Mth;

public class RangedAttribute extends Attribute {
    private final double minValue;
    private final double maxValue;
    
    public RangedAttribute(final String string, final double double2, final double double3, final double double4) {
        super(string, double2);
        this.minValue = double3;
        this.maxValue = double4;
        if (double3 > double4) {
            throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
        }
        if (double2 < double3) {
            throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
        }
        if (double2 > double4) {
            throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
        }
    }
    
    @Override
    public double sanitizeValue(double double1) {
        double1 = Mth.clamp(double1, this.minValue, this.maxValue);
        return double1;
    }
}
