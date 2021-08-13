package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import com.mojang.math.OctahedralGroup;

public enum Mirror {
    NONE(OctahedralGroup.IDENTITY), 
    LEFT_RIGHT(OctahedralGroup.INVERT_Z), 
    FRONT_BACK(OctahedralGroup.INVERT_X);
    
    private final OctahedralGroup rotation;
    
    private Mirror(final OctahedralGroup c) {
        this.rotation = c;
    }
    
    public int mirror(final int integer1, final int integer2) {
        final int integer3 = integer2 / 2;
        final int integer4 = (integer1 > integer3) ? (integer1 - integer2) : integer1;
        switch (this) {
            case FRONT_BACK: {
                return (integer2 - integer4) % integer2;
            }
            case LEFT_RIGHT: {
                return (integer3 - integer4 + integer2) % integer2;
            }
            default: {
                return integer1;
            }
        }
    }
    
    public Rotation getRotation(final Direction gc) {
        final Direction.Axis a3 = gc.getAxis();
        return ((this == Mirror.LEFT_RIGHT && a3 == Direction.Axis.Z) || (this == Mirror.FRONT_BACK && a3 == Direction.Axis.X)) ? Rotation.CLOCKWISE_180 : Rotation.NONE;
    }
    
    public Direction mirror(final Direction gc) {
        if (this == Mirror.FRONT_BACK && gc.getAxis() == Direction.Axis.X) {
            return gc.getOpposite();
        }
        if (this == Mirror.LEFT_RIGHT && gc.getAxis() == Direction.Axis.Z) {
            return gc.getOpposite();
        }
        return gc;
    }
    
    public OctahedralGroup rotation() {
        return this.rotation;
    }
}
