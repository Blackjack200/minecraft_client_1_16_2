package net.minecraft.core;

public class PositionImpl implements Position {
    protected final double x;
    protected final double y;
    protected final double z;
    
    public PositionImpl(final double double1, final double double2, final double double3) {
        this.x = double1;
        this.y = double2;
        this.z = double3;
    }
    
    public double x() {
        return this.x;
    }
    
    public double y() {
        return this.y;
    }
    
    public double z() {
        return this.z;
    }
}
