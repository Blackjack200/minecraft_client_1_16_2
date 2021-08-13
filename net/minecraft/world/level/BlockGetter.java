package net.minecraft.world.level;

import net.minecraft.util.Mth;
import net.minecraft.core.Direction;
import java.util.function.Supplier;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;
import java.util.function.BiFunction;
import net.minecraft.world.phys.BlockHitResult;
import java.util.stream.Stream;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

public interface BlockGetter {
    @Nullable
    BlockEntity getBlockEntity(final BlockPos fx);
    
    BlockState getBlockState(final BlockPos fx);
    
    FluidState getFluidState(final BlockPos fx);
    
    default int getLightEmission(final BlockPos fx) {
        return this.getBlockState(fx).getLightEmission();
    }
    
    default int getMaxLightLevel() {
        return 15;
    }
    
    default int getMaxBuildHeight() {
        return 256;
    }
    
    default Stream<BlockState> getBlockStates(final AABB dcf) {
        return (Stream<BlockState>)BlockPos.betweenClosedStream(dcf).map(this::getBlockState);
    }
    
    default BlockHitResult clip(final ClipContext brc) {
        return BlockGetter.<BlockHitResult>traverseBlocks(brc, (java.util.function.BiFunction<ClipContext, BlockPos, BlockHitResult>)((brc, fx) -> {
            final BlockState cee4 = this.getBlockState(fx);
            final FluidState cuu5 = this.getFluidState(fx);
            final Vec3 dck6 = brc.getFrom();
            final Vec3 dck7 = brc.getTo();
            final VoxelShape dde8 = brc.getBlockShape(cee4, this, fx);
            final BlockHitResult dcg9 = this.clipWithInteractionOverride(dck6, dck7, fx, dde8, cee4);
            final VoxelShape dde9 = brc.getFluidShape(cuu5, this, fx);
            final BlockHitResult dcg10 = dde9.clip(dck6, dck7, fx);
            final double double12 = (dcg9 == null) ? Double.MAX_VALUE : brc.getFrom().distanceToSqr(dcg9.getLocation());
            final double double13 = (dcg10 == null) ? Double.MAX_VALUE : brc.getFrom().distanceToSqr(dcg10.getLocation());
            return (double12 <= double13) ? dcg9 : dcg10;
        }), (java.util.function.Function<ClipContext, BlockHitResult>)(brc -> {
            final Vec3 dck2 = brc.getFrom().subtract(brc.getTo());
            return BlockHitResult.miss(brc.getTo(), Direction.getNearest(dck2.x, dck2.y, dck2.z), new BlockPos(brc.getTo()));
        }));
    }
    
    @Nullable
    default BlockHitResult clipWithInteractionOverride(final Vec3 dck1, final Vec3 dck2, final BlockPos fx, final VoxelShape dde, final BlockState cee) {
        final BlockHitResult dcg7 = dde.clip(dck1, dck2, fx);
        if (dcg7 != null) {
            final BlockHitResult dcg8 = cee.getInteractionShape(this, fx).clip(dck1, dck2, fx);
            if (dcg8 != null && dcg8.getLocation().subtract(dck1).lengthSqr() < dcg7.getLocation().subtract(dck1).lengthSqr()) {
                return dcg7.withDirection(dcg8.getDirection());
            }
        }
        return dcg7;
    }
    
    default double getBlockFloorHeight(final VoxelShape dde, final Supplier<VoxelShape> supplier) {
        if (!dde.isEmpty()) {
            return dde.max(Direction.Axis.Y);
        }
        final double double4 = ((VoxelShape)supplier.get()).max(Direction.Axis.Y);
        if (double4 >= 1.0) {
            return double4 - 1.0;
        }
        return Double.NEGATIVE_INFINITY;
    }
    
    default double getBlockFloorHeight(final BlockPos fx) {
        return this.getBlockFloorHeight(this.getBlockState(fx).getCollisionShape(this, fx), (Supplier<VoxelShape>)(() -> {
            final BlockPos fx2 = fx.below();
            return this.getBlockState(fx2).getCollisionShape(this, fx2);
        }));
    }
    
    default <T> T traverseBlocks(final ClipContext brc, final BiFunction<ClipContext, BlockPos, T> biFunction, final Function<ClipContext, T> function) {
        final Vec3 dck4 = brc.getFrom();
        final Vec3 dck5 = brc.getTo();
        if (dck4.equals(dck5)) {
            return (T)function.apply(brc);
        }
        final double double6 = Mth.lerp(-1.0E-7, dck5.x, dck4.x);
        final double double7 = Mth.lerp(-1.0E-7, dck5.y, dck4.y);
        final double double8 = Mth.lerp(-1.0E-7, dck5.z, dck4.z);
        final double double9 = Mth.lerp(-1.0E-7, dck4.x, dck5.x);
        final double double10 = Mth.lerp(-1.0E-7, dck4.y, dck5.y);
        final double double11 = Mth.lerp(-1.0E-7, dck4.z, dck5.z);
        int integer18 = Mth.floor(double9);
        int integer19 = Mth.floor(double10);
        int integer20 = Mth.floor(double11);
        final BlockPos.MutableBlockPos a21 = new BlockPos.MutableBlockPos(integer18, integer19, integer20);
        final T object22 = (T)biFunction.apply(brc, a21);
        if (object22 != null) {
            return object22;
        }
        final double double12 = double6 - double9;
        final double double13 = double7 - double10;
        final double double14 = double8 - double11;
        final int integer21 = Mth.sign(double12);
        final int integer22 = Mth.sign(double13);
        final int integer23 = Mth.sign(double14);
        final double double15 = (integer21 == 0) ? Double.MAX_VALUE : (integer21 / double12);
        final double double16 = (integer22 == 0) ? Double.MAX_VALUE : (integer22 / double13);
        final double double17 = (integer23 == 0) ? Double.MAX_VALUE : (integer23 / double14);
        double double18 = double15 * ((integer21 > 0) ? (1.0 - Mth.frac(double9)) : Mth.frac(double9));
        double double19 = double16 * ((integer22 > 0) ? (1.0 - Mth.frac(double10)) : Mth.frac(double10));
        double double20 = double17 * ((integer23 > 0) ? (1.0 - Mth.frac(double11)) : Mth.frac(double11));
        while (double18 <= 1.0 || double19 <= 1.0 || double20 <= 1.0) {
            if (double18 < double19) {
                if (double18 < double20) {
                    integer18 += integer21;
                    double18 += double15;
                }
                else {
                    integer20 += integer23;
                    double20 += double17;
                }
            }
            else if (double19 < double20) {
                integer19 += integer22;
                double19 += double16;
            }
            else {
                integer20 += integer23;
                double20 += double17;
            }
            final T object23 = (T)biFunction.apply(brc, a21.set(integer18, integer19, integer20));
            if (object23 != null) {
                return object23;
            }
        }
        return (T)function.apply(brc);
    }
}
