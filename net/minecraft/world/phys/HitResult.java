package net.minecraft.world.phys;

import net.minecraft.world.entity.Entity;

public abstract class HitResult {
    protected final Vec3 location;
    
    protected HitResult(final Vec3 dck) {
        this.location = dck;
    }
    
    public double distanceTo(final Entity apx) {
        final double double3 = this.location.x - apx.getX();
        final double double4 = this.location.y - apx.getY();
        final double double5 = this.location.z - apx.getZ();
        return double3 * double3 + double4 * double4 + double5 * double5;
    }
    
    public abstract Type getType();
    
    public Vec3 getLocation() {
        return this.location;
    }
    
    public enum Type {
        MISS, 
        BLOCK, 
        ENTITY;
    }
}
