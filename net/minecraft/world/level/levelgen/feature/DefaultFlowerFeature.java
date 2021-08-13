package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;

public class DefaultFlowerFeature extends AbstractFlowerFeature<RandomPatchConfiguration> {
    public DefaultFlowerFeature(final Codec<RandomPatchConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean isValid(final LevelAccessor brv, final BlockPos fx, final RandomPatchConfiguration cmk) {
        return !cmk.blacklist.contains(brv.getBlockState(fx));
    }
    
    @Override
    public int getCount(final RandomPatchConfiguration cmk) {
        return cmk.tries;
    }
    
    @Override
    public BlockPos getPos(final Random random, final BlockPos fx, final RandomPatchConfiguration cmk) {
        return fx.offset(random.nextInt(cmk.xspread) - random.nextInt(cmk.xspread), random.nextInt(cmk.yspread) - random.nextInt(cmk.yspread), random.nextInt(cmk.zspread) - random.nextInt(cmk.zspread));
    }
    
    @Override
    public BlockState getRandomFlower(final Random random, final BlockPos fx, final RandomPatchConfiguration cmk) {
        return cmk.stateProvider.getState(random, fx);
    }
}
