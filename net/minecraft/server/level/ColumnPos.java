package net.minecraft.server.level;

import net.minecraft.core.BlockPos;

public class ColumnPos {
    public final int x;
    public final int z;
    
    public ColumnPos(final int integer1, final int integer2) {
        this.x = integer1;
        this.z = integer2;
    }
    
    public ColumnPos(final BlockPos fx) {
        this.x = fx.getX();
        this.z = fx.getZ();
    }
    
    public String toString() {
        return new StringBuilder().append("[").append(this.x).append(", ").append(this.z).append("]").toString();
    }
    
    public int hashCode() {
        final int integer2 = 1664525 * this.x + 1013904223;
        final int integer3 = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
        return integer2 ^ integer3;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ColumnPos) {
            final ColumnPos zw3 = (ColumnPos)object;
            return this.x == zw3.x && this.z == zw3.z;
        }
        return false;
    }
}
