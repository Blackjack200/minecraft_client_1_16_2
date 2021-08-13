package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public abstract class AbstractHugeMushroomFeature extends Feature<HugeMushroomFeatureConfiguration> {
    public AbstractHugeMushroomFeature(final Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }
    
    protected void placeTrunk(final LevelAccessor brv, final Random random, final BlockPos fx, final HugeMushroomFeatureConfiguration cly, final int integer, final BlockPos.MutableBlockPos a) {
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            a.set(fx).move(Direction.UP, integer2);
            if (!brv.getBlockState(a).isSolidRender(brv, a)) {
                this.setBlock(brv, a, cly.stemProvider.getState(random, fx));
            }
        }
    }
    
    protected int getTreeHeight(final Random random) {
        int integer3 = random.nextInt(3) + 4;
        if (random.nextInt(12) == 0) {
            integer3 *= 2;
        }
        return integer3;
    }
    
    protected boolean isValidPosition(final LevelAccessor brv, final BlockPos fx, final int integer, final BlockPos.MutableBlockPos a, final HugeMushroomFeatureConfiguration cly) {
        final int integer2 = fx.getY();
        if (integer2 < 1 || integer2 + integer + 1 >= 256) {
            return false;
        }
        final Block bul8 = brv.getBlockState(fx.below()).getBlock();
        if (!Feature.isDirt(bul8) && !bul8.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return false;
        }
        for (int integer3 = 0; integer3 <= integer; ++integer3) {
            for (int integer4 = this.getTreeRadiusForHeight(-1, -1, cly.foliageRadius, integer3), integer5 = -integer4; integer5 <= integer4; ++integer5) {
                for (int integer6 = -integer4; integer6 <= integer4; ++integer6) {
                    final BlockState cee13 = brv.getBlockState(a.setWithOffset(fx, integer5, integer3, integer6));
                    if (!cee13.isAir() && !cee13.is(BlockTags.LEAVES)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final HugeMushroomFeatureConfiguration cly) {
        final int integer7 = this.getTreeHeight(random);
        final BlockPos.MutableBlockPos a8 = new BlockPos.MutableBlockPos();
        if (!this.isValidPosition(bso, fx, integer7, a8, cly)) {
            return false;
        }
        this.makeCap(bso, random, fx, integer7, a8, cly);
        this.placeTrunk(bso, random, fx, cly, integer7, a8);
        return true;
    }
    
    protected abstract int getTreeRadiusForHeight(final int integer1, final int integer2, final int integer3, final int integer4);
    
    protected abstract void makeCap(final LevelAccessor brv, final Random random, final BlockPos fx, final int integer, final BlockPos.MutableBlockPos a, final HugeMushroomFeatureConfiguration cly);
}
