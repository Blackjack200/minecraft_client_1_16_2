package net.minecraft.world.level.block;

import net.minecraft.world.level.levelgen.feature.TwistingVinesFeature;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.NetherForestVegetationFeature;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.lighting.LayerLightEngine;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class NyliumBlock extends Block implements BonemealableBlock {
    protected NyliumBlock(final Properties c) {
        super(c);
    }
    
    private static boolean canBeNylium(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.above();
        final BlockState cee2 = brw.getBlockState(fx2);
        final int integer6 = LayerLightEngine.getLightBlockInto(brw, cee, fx, cee2, fx2, Direction.UP, cee2.getLightBlock(brw, fx2));
        return integer6 < brw.getMaxLightLevel();
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!canBeNylium(cee, aag, fx)) {
            aag.setBlockAndUpdate(fx, Blocks.NETHERRACK.defaultBlockState());
        }
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
        final BlockState cee2 = aag.getBlockState(fx);
        final BlockPos fx2 = fx.above();
        if (cee2.is(Blocks.CRIMSON_NYLIUM)) {
            NetherForestVegetationFeature.place(aag, random, fx2, Features.Configs.CRIMSON_FOREST_CONFIG, 3, 1);
        }
        else if (cee2.is(Blocks.WARPED_NYLIUM)) {
            NetherForestVegetationFeature.place(aag, random, fx2, Features.Configs.WARPED_FOREST_CONFIG, 3, 1);
            NetherForestVegetationFeature.place(aag, random, fx2, Features.Configs.NETHER_SPROUTS_CONFIG, 3, 1);
            if (random.nextInt(8) == 0) {
                TwistingVinesFeature.place(aag, random, fx2, 3, 1, 2);
            }
        }
    }
}
