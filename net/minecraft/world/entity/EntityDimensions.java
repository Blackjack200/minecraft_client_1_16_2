package net.minecraft.world.entity;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityDimensions {
    public final float width;
    public final float height;
    public final boolean fixed;
    
    public EntityDimensions(final float float1, final float float2, final boolean boolean3) {
        this.width = float1;
        this.height = float2;
        this.fixed = boolean3;
    }
    
    public AABB makeBoundingBox(final Vec3 dck) {
        return this.makeBoundingBox(dck.x, dck.y, dck.z);
    }
    
    public AABB makeBoundingBox(final double double1, final double double2, final double double3) {
        final float float8 = this.width / 2.0f;
        final float float9 = this.height;
        return new AABB(double1 - float8, double2, double3 - float8, double1 + float8, double2 + float9, double3 + float8);
    }
    
    public EntityDimensions scale(final float float1) {
        return this.scale(float1, float1);
    }
    
    public EntityDimensions scale(final float float1, final float float2) {
        if (this.fixed || (float1 == 1.0f && float2 == 1.0f)) {
            return this;
        }
        return scalable(this.width * float1, this.height * float2);
    }
    
    public static EntityDimensions scalable(final float float1, final float float2) {
        return new EntityDimensions(float1, float2, false);
    }
    
    public static EntityDimensions fixed(final float float1, final float float2) {
        return new EntityDimensions(float1, float2, true);
    }
    
    public String toString() {
        return new StringBuilder().append("EntityDimensions w=").append(this.width).append(", h=").append(this.height).append(", fixed=").append(this.fixed).toString();
    }
}
