package net.minecraft.world.level.levelgen.feature;

import java.util.Iterator;
import net.minecraft.Util;
import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import com.mojang.serialization.Codec;

public class CoralClawFeature extends CoralFeature {
    public CoralClawFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected boolean placeFeature(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee) {
        if (!this.placeCoralBlock(brv, random, fx, cee)) {
            return false;
        }
        final Direction gc6 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        final int integer7 = random.nextInt(2) + 2;
        final List<Direction> list8 = (List<Direction>)Lists.newArrayList((Object[])new Direction[] { gc6, gc6.getClockWise(), gc6.getCounterClockWise() });
        Collections.shuffle((List)list8, random);
        final List<Direction> list9 = (List<Direction>)list8.subList(0, integer7);
        for (final Direction gc7 : list9) {
            final BlockPos.MutableBlockPos a12 = fx.mutable();
            final int integer8 = random.nextInt(2) + 1;
            a12.move(gc7);
            Direction gc8;
            int integer9;
            if (gc7 == gc6) {
                gc8 = gc6;
                integer9 = random.nextInt(3) + 2;
            }
            else {
                a12.move(Direction.UP);
                final Direction[] arr16 = { gc7, Direction.UP };
                gc8 = Util.<Direction>getRandom(arr16, random);
                integer9 = random.nextInt(3) + 3;
            }
            for (int integer10 = 0; integer10 < integer8 && this.placeCoralBlock(brv, random, a12, cee); ++integer10) {
                a12.move(gc8);
            }
            a12.move(gc8.getOpposite());
            a12.move(Direction.UP);
            for (int integer10 = 0; integer10 < integer9; ++integer10) {
                a12.move(gc6);
                if (!this.placeCoralBlock(brv, random, a12, cee)) {
                    break;
                }
                if (random.nextFloat() < 0.25f) {
                    a12.move(Direction.UP);
                }
            }
        }
        return true;
    }
}
