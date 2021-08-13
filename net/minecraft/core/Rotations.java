package net.minecraft.core;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;

public class Rotations {
    protected final float x;
    protected final float y;
    protected final float z;
    
    public Rotations(final float float1, final float float2, final float float3) {
        this.x = ((Float.isInfinite(float1) || Float.isNaN(float1)) ? 0.0f : (float1 % 360.0f));
        this.y = ((Float.isInfinite(float2) || Float.isNaN(float2)) ? 0.0f : (float2 % 360.0f));
        this.z = ((Float.isInfinite(float3) || Float.isNaN(float3)) ? 0.0f : (float3 % 360.0f));
    }
    
    public Rotations(final ListTag mj) {
        this(mj.getFloat(0), mj.getFloat(1), mj.getFloat(2));
    }
    
    public ListTag save() {
        final ListTag mj2 = new ListTag();
        mj2.add(FloatTag.valueOf(this.x));
        mj2.add(FloatTag.valueOf(this.y));
        mj2.add(FloatTag.valueOf(this.z));
        return mj2;
    }
    
    public boolean equals(final Object object) {
        if (!(object instanceof Rotations)) {
            return false;
        }
        final Rotations go3 = (Rotations)object;
        return this.x == go3.x && this.y == go3.y && this.z == go3.z;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getZ() {
        return this.z;
    }
}
