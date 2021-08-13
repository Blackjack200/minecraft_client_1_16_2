package net.minecraft.world.entity.vehicle;

import net.minecraft.core.Vec3i;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import java.util.function.Function;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.core.Direction;

public class DismountHelper {
    public static int[][] offsetsForDirection(final Direction gc) {
        final Direction gc2 = gc.getClockWise();
        final Direction gc3 = gc2.getOpposite();
        final Direction gc4 = gc.getOpposite();
        return new int[][] { { gc2.getStepX(), gc2.getStepZ() }, { gc3.getStepX(), gc3.getStepZ() }, { gc4.getStepX() + gc2.getStepX(), gc4.getStepZ() + gc2.getStepZ() }, { gc4.getStepX() + gc3.getStepX(), gc4.getStepZ() + gc3.getStepZ() }, { gc.getStepX() + gc2.getStepX(), gc.getStepZ() + gc2.getStepZ() }, { gc.getStepX() + gc3.getStepX(), gc.getStepZ() + gc3.getStepZ() }, { gc4.getStepX(), gc4.getStepZ() }, { gc.getStepX(), gc.getStepZ() } };
    }
    
    public static boolean isBlockFloorValid(final double double1) {
        return !Double.isInfinite(double1) && double1 < 1.0;
    }
    
    public static boolean canDismountTo(final CollisionGetter brd, final LivingEntity aqj, final AABB dcf) {
        return brd.getBlockCollisions(aqj, dcf).allMatch(VoxelShape::isEmpty);
    }
    
    @Nullable
    public static Vec3 findDismountLocation(final CollisionGetter brd, final double double2, final double double3, final double double4, final LivingEntity aqj, final Pose aqu) {
        if (isBlockFloorValid(double3)) {
            final Vec3 dck10 = new Vec3(double2, double3, double4);
            if (canDismountTo(brd, aqj, aqj.getLocalBoundsForPose(aqu).move(dck10))) {
                return dck10;
            }
        }
        return null;
    }
    
    public static VoxelShape nonClimbableShape(final BlockGetter bqz, final BlockPos fx) {
        final BlockState cee3 = bqz.getBlockState(fx);
        if (cee3.is(BlockTags.CLIMBABLE) || (cee3.getBlock() instanceof TrapDoorBlock && cee3.<Boolean>getValue((Property<Boolean>)TrapDoorBlock.OPEN))) {
            return Shapes.empty();
        }
        return cee3.getCollisionShape(bqz, fx);
    }
    
    public static double findCeilingFrom(final BlockPos fx, final int integer, final Function<BlockPos, VoxelShape> function) {
        final BlockPos.MutableBlockPos a4 = fx.mutable();
        int integer2 = 0;
        while (integer2 < integer) {
            final VoxelShape dde6 = (VoxelShape)function.apply(a4);
            if (!dde6.isEmpty()) {
                return fx.getY() + integer2 + dde6.min(Direction.Axis.Y);
            }
            ++integer2;
            a4.move(Direction.UP);
        }
        return Double.POSITIVE_INFINITY;
    }
    
    @Nullable
    public static Vec3 findSafeDismountLocation(final EntityType<?> aqb, final CollisionGetter brd, final BlockPos fx, final boolean boolean4) {
        if (boolean4 && aqb.isBlockDangerous(brd.getBlockState(fx))) {
            return null;
        }
        final double double5 = brd.getBlockFloorHeight(nonClimbableShape(brd, fx), (Supplier<VoxelShape>)(() -> nonClimbableShape(brd, fx.below())));
        if (!isBlockFloorValid(double5)) {
            return null;
        }
        if (boolean4 && double5 <= 0.0 && aqb.isBlockDangerous(brd.getBlockState(fx.below()))) {
            return null;
        }
        final Vec3 dck7 = Vec3.upFromBottomCenterOf(fx, double5);
        if (brd.getBlockCollisions(null, aqb.getDimensions().makeBoundingBox(dck7)).allMatch(VoxelShape::isEmpty)) {
            return dck7;
        }
        return null;
    }
}
