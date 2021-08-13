package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TwistingVinesFeature extends Feature<NoneFeatureConfiguration> {
    public TwistingVinesFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        return place(bso, random, fx, 8, 4, 8);
    }
    
    public static boolean place(final LevelAccessor brv, final Random random, final BlockPos fx, final int integer4, final int integer5, final int integer6) {
        if (isInvalidPlacementLocation(brv, fx)) {
            return false;
        }
        placeTwistingVines(brv, random, fx, integer4, integer5, integer6);
        return true;
    }
    
    private static void placeTwistingVines(final LevelAccessor brv, final Random random, final BlockPos fx, final int integer4, final int integer5, final int integer6) {
        final BlockPos.MutableBlockPos a7 = new BlockPos.MutableBlockPos();
        for (int integer7 = 0; integer7 < integer4 * integer4; ++integer7) {
            a7.set(fx).move(Mth.nextInt(random, -integer4, integer4), Mth.nextInt(random, -integer5, integer5), Mth.nextInt(random, -integer4, integer4));
            if (findFirstAirBlockAboveGround(brv, a7)) {
                if (!isInvalidPlacementLocation(brv, a7)) {
                    int integer8 = Mth.nextInt(random, 1, integer6);
                    if (random.nextInt(6) == 0) {
                        integer8 *= 2;
                    }
                    if (random.nextInt(5) == 0) {
                        integer8 = 1;
                    }
                    final int integer9 = 17;
                    final int integer10 = 25;
                    placeWeepingVinesColumn(brv, random, a7, integer8, 17, 25);
                }
            }
        }
    }
    
    private static boolean findFirstAirBlockAboveGround(final LevelAccessor brv, final BlockPos.MutableBlockPos a) {
        do {
            a.move(0, -1, 0);
            if (Level.isOutsideBuildHeight(a)) {
                return false;
            }
        } while (brv.getBlockState(a).isAir());
        a.move(0, 1, 0);
        return true;
    }
    
    public static void placeWeepingVinesColumn(final LevelAccessor brv, final Random random, final BlockPos.MutableBlockPos a, final int integer4, final int integer5, final int integer6) {
        for (int integer7 = 1; integer7 <= integer4; ++integer7) {
            if (brv.isEmptyBlock(a)) {
                if (integer7 == integer4 || !brv.isEmptyBlock(a.above())) {
                    brv.setBlock(a, ((StateHolder<O, BlockState>)Blocks.TWISTING_VINES.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)GrowingPlantHeadBlock.AGE, Mth.nextInt(random, integer5, integer6)), 2);
                    break;
                }
                brv.setBlock(a, Blocks.TWISTING_VINES_PLANT.defaultBlockState(), 2);
            }
            a.move(Direction.UP);
        }
    }
    
    private static boolean isInvalidPlacementLocation(final LevelAccessor brv, final BlockPos fx) {
        if (!brv.isEmptyBlock(fx)) {
            return true;
        }
        final BlockState cee3 = brv.getBlockState(fx.below());
        return !cee3.is(Blocks.NETHERRACK) && !cee3.is(Blocks.WARPED_NYLIUM) && !cee3.is(Blocks.WARPED_WART_BLOCK);
    }
}
