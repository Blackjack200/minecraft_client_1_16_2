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

public class HugeBrownMushroomFeature extends AbstractHugeMushroomFeature {
    public HugeBrownMushroomFeature(final Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    protected void makeCap(final LevelAccessor brv, final Random random, final BlockPos fx, final int integer, final BlockPos.MutableBlockPos a, final HugeMushroomFeatureConfiguration cly) {
        for (int integer2 = cly.foliageRadius, integer3 = -integer2; integer3 <= integer2; ++integer3) {
            for (int integer4 = -integer2; integer4 <= integer2; ++integer4) {
                final boolean boolean11 = integer3 == -integer2;
                final boolean boolean12 = integer3 == integer2;
                final boolean boolean13 = integer4 == -integer2;
                final boolean boolean14 = integer4 == integer2;
                final boolean boolean15 = boolean11 || boolean12;
                final boolean boolean16 = boolean13 || boolean14;
                if (!boolean15 || !boolean16) {
                    a.setWithOffset(fx, integer3, integer, integer4);
                    if (!brv.getBlockState(a).isSolidRender(brv, a)) {
                        final boolean boolean17 = boolean11 || (boolean16 && integer3 == 1 - integer2);
                        final boolean boolean18 = boolean12 || (boolean16 && integer3 == integer2 - 1);
                        final boolean boolean19 = boolean13 || (boolean15 && integer4 == 1 - integer2);
                        final boolean boolean20 = boolean14 || (boolean15 && integer4 == integer2 - 1);
                        this.setBlock(brv, a, (((((StateHolder<O, BlockState>)cly.capProvider.getState(random, fx)).setValue((Property<Comparable>)HugeMushroomBlock.WEST, boolean17)).setValue((Property<Comparable>)HugeMushroomBlock.EAST, boolean18)).setValue((Property<Comparable>)HugeMushroomBlock.NORTH, boolean19)).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.SOUTH, boolean20));
                    }
                }
            }
        }
    }
    
    @Override
    protected int getTreeRadiusForHeight(final int integer1, final int integer2, final int integer3, final int integer4) {
        return (integer4 <= 3) ? 0 : integer3;
    }
}
