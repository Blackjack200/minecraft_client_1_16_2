package net.minecraft.world;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class ShulkerSharedHelper {
    public static AABB openBoundingBox(final BlockPos fx, final Direction gc) {
        return Shapes.block().bounds().expandTowards(0.5f * gc.getStepX(), 0.5f * gc.getStepY(), 0.5f * gc.getStepZ()).contract(gc.getStepX(), gc.getStepY(), gc.getStepZ()).move(fx.relative(gc));
    }
}
