package net.minecraft.world.level.block;

import java.util.List;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.AbstractFlowerFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
    public GrassBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return bqz.getBlockState(fx.above()).isAir();
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        final BlockPos fx2 = fx.above();
        final BlockState cee2 = Blocks.GRASS.defaultBlockState();
        int integer8 = 0;
    Label_0280_Outer:
        while (integer8 < 128) {
            BlockPos fx3 = fx2;
            int integer9 = 0;
            while (true) {
                while (integer9 < integer8 / 16) {
                    fx3 = fx3.offset(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                    if (aag.getBlockState(fx3.below()).is(this)) {
                        if (!aag.getBlockState(fx3).isCollisionShapeFullBlock(aag, fx3)) {
                            ++integer9;
                            continue Label_0280_Outer;
                        }
                    }
                    ++integer8;
                    continue Label_0280_Outer;
                }
                final BlockState cee3 = aag.getBlockState(fx3);
                if (cee3.is(cee2.getBlock()) && random.nextInt(10) == 0) {
                    ((BonemealableBlock)cee2.getBlock()).performBonemeal(aag, random, fx3, cee3);
                }
                if (!cee3.isAir()) {
                    continue;
                }
                BlockState cee4;
                if (random.nextInt(8) == 0) {
                    final List<ConfiguredFeature<?, ?>> list12 = aag.getBiome(fx3).getGenerationSettings().getFlowerFeatures();
                    if (list12.isEmpty()) {
                        continue;
                    }
                    final ConfiguredFeature<?, ?> cis13 = list12.get(0);
                    final AbstractFlowerFeature cif14 = (AbstractFlowerFeature)cis13.feature;
                    cee4 = cif14.getRandomFlower(random, fx3, (FeatureConfiguration)cis13.config());
                }
                else {
                    cee4 = cee2;
                }
                if (cee4.canSurvive(aag, fx3)) {
                    aag.setBlock(fx3, cee4, 3);
                }
                continue;
            }
        }
    }
}
