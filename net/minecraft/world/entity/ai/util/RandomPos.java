package net.minecraft.world.entity.ai.util;

import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import java.util.Random;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import java.util.function.Predicate;
import net.minecraft.core.Position;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.function.ToDoubleFunction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.PathfinderMob;

public class RandomPos {
    @Nullable
    public static Vec3 getPos(final PathfinderMob aqr, final int integer2, final int integer3) {
        return generateRandomPos(aqr, integer2, integer3, 0, null, true, 1.5707963705062866, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, false, 0, 0, true);
    }
    
    @Nullable
    public static Vec3 getAirPos(final PathfinderMob aqr, final int integer2, final int integer3, final int integer4, @Nullable final Vec3 dck, final double double6) {
        return generateRandomPos(aqr, integer2, integer3, integer4, dck, true, double6, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, true, 0, 0, false);
    }
    
    @Nullable
    public static Vec3 getLandPos(final PathfinderMob aqr, final int integer2, final int integer3) {
        return getLandPos(aqr, integer2, integer3, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue);
    }
    
    @Nullable
    public static Vec3 getLandPos(final PathfinderMob aqr, final int integer2, final int integer3, final ToDoubleFunction<BlockPos> toDoubleFunction) {
        return generateRandomPos(aqr, integer2, integer3, 0, null, false, 0.0, toDoubleFunction, true, 0, 0, true);
    }
    
    @Nullable
    public static Vec3 getAboveLandPos(final PathfinderMob aqr, final int integer2, final int integer3, final Vec3 dck, final float float5, final int integer6, final int integer7) {
        return generateRandomPos(aqr, integer2, integer3, 0, dck, false, float5, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, true, integer6, integer7, true);
    }
    
    @Nullable
    public static Vec3 getLandPosTowards(final PathfinderMob aqr, final int integer2, final int integer3, final Vec3 dck) {
        final Vec3 dck2 = dck.subtract(aqr.getX(), aqr.getY(), aqr.getZ());
        return generateRandomPos(aqr, integer2, integer3, 0, dck2, false, 1.5707963705062866, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, true, 0, 0, true);
    }
    
    @Nullable
    public static Vec3 getPosTowards(final PathfinderMob aqr, final int integer2, final int integer3, final Vec3 dck) {
        final Vec3 dck2 = dck.subtract(aqr.getX(), aqr.getY(), aqr.getZ());
        return generateRandomPos(aqr, integer2, integer3, 0, dck2, true, 1.5707963705062866, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, false, 0, 0, true);
    }
    
    @Nullable
    public static Vec3 getPosTowards(final PathfinderMob aqr, final int integer2, final int integer3, final Vec3 dck, final double double5) {
        final Vec3 dck2 = dck.subtract(aqr.getX(), aqr.getY(), aqr.getZ());
        return generateRandomPos(aqr, integer2, integer3, 0, dck2, true, double5, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, false, 0, 0, true);
    }
    
    @Nullable
    public static Vec3 getAirPosTowards(final PathfinderMob aqr, final int integer2, final int integer3, final int integer4, final Vec3 dck, final double double6) {
        final Vec3 dck2 = dck.subtract(aqr.getX(), aqr.getY(), aqr.getZ());
        return generateRandomPos(aqr, integer2, integer3, integer4, dck2, false, double6, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, true, 0, 0, false);
    }
    
    @Nullable
    public static Vec3 getPosAvoid(final PathfinderMob aqr, final int integer2, final int integer3, final Vec3 dck) {
        final Vec3 dck2 = aqr.position().subtract(dck);
        return generateRandomPos(aqr, integer2, integer3, 0, dck2, true, 1.5707963705062866, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, false, 0, 0, true);
    }
    
    @Nullable
    public static Vec3 getLandPosAvoid(final PathfinderMob aqr, final int integer2, final int integer3, final Vec3 dck) {
        final Vec3 dck2 = aqr.position().subtract(dck);
        return generateRandomPos(aqr, integer2, integer3, 0, dck2, false, 1.5707963705062866, (ToDoubleFunction<BlockPos>)aqr::getWalkTargetValue, true, 0, 0, true);
    }
    
    @Nullable
    private static Vec3 generateRandomPos(final PathfinderMob aqr, final int integer2, final int integer3, final int integer4, @Nullable final Vec3 dck, final boolean boolean6, final double double7, final ToDoubleFunction<BlockPos> toDoubleFunction, final boolean boolean9, final int integer10, final int integer11, final boolean boolean12) {
        final PathNavigation ayg14 = aqr.getNavigation();
        final Random random15 = aqr.getRandom();
        final boolean boolean13 = aqr.hasRestriction() && aqr.getRestrictCenter().closerThan(aqr.position(), aqr.getRestrictRadius() + integer2 + 1.0);
        boolean boolean14 = false;
        double double8 = Double.NEGATIVE_INFINITY;
        BlockPos fx20 = aqr.blockPosition();
        for (int integer12 = 0; integer12 < 10; ++integer12) {
            final BlockPos fx21 = getRandomDelta(random15, integer2, integer3, integer4, dck, double7);
            if (fx21 != null) {
                int integer13 = fx21.getX();
                final int integer14 = fx21.getY();
                int integer15 = fx21.getZ();
                if (aqr.hasRestriction() && integer2 > 1) {
                    final BlockPos fx22 = aqr.getRestrictCenter();
                    if (aqr.getX() > fx22.getX()) {
                        integer13 -= random15.nextInt(integer2 / 2);
                    }
                    else {
                        integer13 += random15.nextInt(integer2 / 2);
                    }
                    if (aqr.getZ() > fx22.getZ()) {
                        integer15 -= random15.nextInt(integer2 / 2);
                    }
                    else {
                        integer15 += random15.nextInt(integer2 / 2);
                    }
                }
                BlockPos fx22 = new BlockPos(integer13 + aqr.getX(), integer14 + aqr.getY(), integer15 + aqr.getZ());
                if (fx22.getY() >= 0) {
                    if (fx22.getY() <= aqr.level.getMaxBuildHeight()) {
                        if (!boolean13 || aqr.isWithinRestriction(fx22)) {
                            if (!boolean12 || ayg14.isStableDestination(fx22)) {
                                if (boolean9) {
                                    fx22 = moveUpToAboveSolid(fx22, random15.nextInt(integer10 + 1) + integer11, aqr.level.getMaxBuildHeight(), (Predicate<BlockPos>)(fx -> aqr.level.getBlockState(fx).getMaterial().isSolid()));
                                }
                                if (boolean6 || !aqr.level.getFluidState(fx22).is(FluidTags.WATER)) {
                                    final BlockPathTypes cww27 = WalkNodeEvaluator.getBlockPathTypeStatic(aqr.level, fx22.mutable());
                                    if (aqr.getPathfindingMalus(cww27) == 0.0f) {
                                        final double double9 = toDoubleFunction.applyAsDouble(fx22);
                                        if (double9 > double8) {
                                            double8 = double9;
                                            fx20 = fx22;
                                            boolean14 = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (boolean14) {
            return Vec3.atBottomCenterOf(fx20);
        }
        return null;
    }
    
    @Nullable
    private static BlockPos getRandomDelta(final Random random, final int integer2, final int integer3, final int integer4, @Nullable final Vec3 dck, final double double6) {
        if (dck == null || double6 >= 3.141592653589793) {
            final int integer5 = random.nextInt(2 * integer2 + 1) - integer2;
            final int integer6 = random.nextInt(2 * integer3 + 1) - integer3 + integer4;
            final int integer7 = random.nextInt(2 * integer2 + 1) - integer2;
            return new BlockPos(integer5, integer6, integer7);
        }
        final double double7 = Mth.atan2(dck.z, dck.x) - 1.5707963705062866;
        final double double8 = double7 + (2.0f * random.nextFloat() - 1.0f) * double6;
        final double double9 = Math.sqrt(random.nextDouble()) * Mth.SQRT_OF_TWO * integer2;
        final double double10 = -double9 * Math.sin(double8);
        final double double11 = double9 * Math.cos(double8);
        if (Math.abs(double10) > integer2 || Math.abs(double11) > integer2) {
            return null;
        }
        final int integer8 = random.nextInt(2 * integer3 + 1) - integer3 + integer4;
        return new BlockPos(double10, integer8, double11);
    }
    
    static BlockPos moveUpToAboveSolid(final BlockPos fx, final int integer2, final int integer3, final Predicate<BlockPos> predicate) {
        if (integer2 < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("aboveSolidAmount was ").append(integer2).append(", expected >= 0").toString());
        }
        if (predicate.test(fx)) {
            BlockPos fx2;
            for (fx2 = fx.above(); fx2.getY() < integer3 && predicate.test(fx2); fx2 = fx2.above()) {}
            BlockPos fx3;
            BlockPos fx4;
            for (fx3 = fx2; fx3.getY() < integer3 && fx3.getY() - fx2.getY() < integer2; fx3 = fx4) {
                fx4 = fx3.above();
                if (predicate.test(fx4)) {
                    break;
                }
            }
            return fx3;
        }
        return fx;
    }
}
