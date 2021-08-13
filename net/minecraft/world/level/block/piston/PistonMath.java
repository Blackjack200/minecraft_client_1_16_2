package net.minecraft.world.level.block.piston;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class PistonMath {
    public static AABB getMovementArea(final AABB dcf, final Direction gc, final double double3) {
        final double double4 = double3 * gc.getAxisDirection().getStep();
        final double double5 = Math.min(double4, 0.0);
        final double double6 = Math.max(double4, 0.0);
        switch (gc) {
            case WEST: {
                return new AABB(dcf.minX + double5, dcf.minY, dcf.minZ, dcf.minX + double6, dcf.maxY, dcf.maxZ);
            }
            case EAST: {
                return new AABB(dcf.maxX + double5, dcf.minY, dcf.minZ, dcf.maxX + double6, dcf.maxY, dcf.maxZ);
            }
            case DOWN: {
                return new AABB(dcf.minX, dcf.minY + double5, dcf.minZ, dcf.maxX, dcf.minY + double6, dcf.maxZ);
            }
            default: {
                return new AABB(dcf.minX, dcf.maxY + double5, dcf.minZ, dcf.maxX, dcf.maxY + double6, dcf.maxZ);
            }
            case NORTH: {
                return new AABB(dcf.minX, dcf.minY, dcf.minZ + double5, dcf.maxX, dcf.maxY, dcf.minZ + double6);
            }
            case SOUTH: {
                return new AABB(dcf.minX, dcf.minY, dcf.maxZ + double5, dcf.maxX, dcf.maxY, dcf.maxZ + double6);
            }
        }
    }
}
