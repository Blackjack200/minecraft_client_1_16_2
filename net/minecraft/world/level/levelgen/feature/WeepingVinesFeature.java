package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WeepingVinesFeature extends Feature<NoneFeatureConfiguration> {
    private static final Direction[] DIRECTIONS;
    
    public WeepingVinesFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        if (!bso.isEmptyBlock(fx)) {
            return false;
        }
        final BlockState cee7 = bso.getBlockState(fx.above());
        if (!cee7.is(Blocks.NETHERRACK) && !cee7.is(Blocks.NETHER_WART_BLOCK)) {
            return false;
        }
        this.placeRoofNetherWart(bso, random, fx);
        this.placeRoofWeepingVines(bso, random, fx);
        return true;
    }
    
    private void placeRoofNetherWart(final LevelAccessor brv, final Random random, final BlockPos fx) {
        brv.setBlock(fx, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        for (int integer7 = 0; integer7 < 200; ++integer7) {
            a5.setWithOffset(fx, random.nextInt(6) - random.nextInt(6), random.nextInt(2) - random.nextInt(5), random.nextInt(6) - random.nextInt(6));
            if (brv.isEmptyBlock(a5)) {
                int integer8 = 0;
                for (final Direction gc12 : WeepingVinesFeature.DIRECTIONS) {
                    final BlockState cee13 = brv.getBlockState(a6.setWithOffset(a5, gc12));
                    if (cee13.is(Blocks.NETHERRACK) || cee13.is(Blocks.NETHER_WART_BLOCK)) {
                        ++integer8;
                    }
                    if (integer8 > 1) {
                        break;
                    }
                }
                if (integer8 == 1) {
                    brv.setBlock(a5, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
                }
            }
        }
    }
    
    private void placeRoofWeepingVines(final LevelAccessor brv, final Random random, final BlockPos fx) {
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        for (int integer6 = 0; integer6 < 100; ++integer6) {
            a5.setWithOffset(fx, random.nextInt(8) - random.nextInt(8), random.nextInt(2) - random.nextInt(7), random.nextInt(8) - random.nextInt(8));
            if (brv.isEmptyBlock(a5)) {
                final BlockState cee7 = brv.getBlockState(a5.above());
                if (cee7.is(Blocks.NETHERRACK) || cee7.is(Blocks.NETHER_WART_BLOCK)) {
                    int integer7 = Mth.nextInt(random, 1, 8);
                    if (random.nextInt(6) == 0) {
                        integer7 *= 2;
                    }
                    if (random.nextInt(5) == 0) {
                        integer7 = 1;
                    }
                    final int integer8 = 17;
                    final int integer9 = 25;
                    placeWeepingVinesColumn(brv, random, a5, integer7, 17, 25);
                }
            }
        }
    }
    
    public static void placeWeepingVinesColumn(final LevelAccessor brv, final Random random, final BlockPos.MutableBlockPos a, final int integer4, final int integer5, final int integer6) {
        for (int integer7 = 0; integer7 <= integer4; ++integer7) {
            if (brv.isEmptyBlock(a)) {
                if (integer7 == integer4 || !brv.isEmptyBlock(a.below())) {
                    brv.setBlock(a, ((StateHolder<O, BlockState>)Blocks.WEEPING_VINES.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)GrowingPlantHeadBlock.AGE, Mth.nextInt(random, integer5, integer6)), 2);
                    break;
                }
                brv.setBlock(a, Blocks.WEEPING_VINES_PLANT.defaultBlockState(), 2);
            }
            a.move(Direction.DOWN);
        }
    }
    
    static {
        DIRECTIONS = Direction.values();
    }
}
