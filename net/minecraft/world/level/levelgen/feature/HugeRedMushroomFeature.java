package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import com.mojang.serialization.Codec;

public class HugeRedMushroomFeature extends AbstractHugeMushroomFeature {
    public HugeRedMushroomFeature(final Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected void makeCap(final LevelAccessor brv, final Random random, final BlockPos fx, final int integer, final BlockPos.MutableBlockPos a, final HugeMushroomFeatureConfiguration cly) {
        for (int integer2 = integer - 3; integer2 <= integer; ++integer2) {
            final int integer3 = (integer2 < integer) ? cly.foliageRadius : (cly.foliageRadius - 1);
            final int integer4 = cly.foliageRadius - 2;
            for (int integer5 = -integer3; integer5 <= integer3; ++integer5) {
                for (int integer6 = -integer3; integer6 <= integer3; ++integer6) {
                    final boolean boolean13 = integer5 == -integer3;
                    final boolean boolean14 = integer5 == integer3;
                    final boolean boolean15 = integer6 == -integer3;
                    final boolean boolean16 = integer6 == integer3;
                    final boolean boolean17 = boolean13 || boolean14;
                    final boolean boolean18 = boolean15 || boolean16;
                    if (integer2 >= integer || boolean17 != boolean18) {
                        a.setWithOffset(fx, integer5, integer2, integer6);
                        if (!brv.getBlockState(a).isSolidRender(brv, a)) {
                            this.setBlock(brv, a, ((((((StateHolder<O, BlockState>)cly.capProvider.getState(random, fx)).setValue((Property<Comparable>)HugeMushroomBlock.UP, integer2 >= integer - 1)).setValue((Property<Comparable>)HugeMushroomBlock.WEST, integer5 < -integer4)).setValue((Property<Comparable>)HugeMushroomBlock.EAST, integer5 > integer4)).setValue((Property<Comparable>)HugeMushroomBlock.NORTH, integer6 < -integer4)).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.SOUTH, integer6 > integer4));
                        }
                    }
                }
            }
        }
    }
    
    @Override
    protected int getTreeRadiusForHeight(final int integer1, final int integer2, final int integer3, final int integer4) {
        int integer5 = 0;
        if (integer4 < integer2 && integer4 >= integer2 - 3) {
            integer5 = integer3;
        }
        else if (integer4 == integer2) {
            integer5 = integer3;
        }
        return integer5;
    }
}
