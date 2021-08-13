package net.minecraft.world.level.block;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.data.worldgen.Features;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MushroomBlock extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE;
    
    public MushroomBlock(final Properties c) {
        super(c);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return MushroomBlock.SHAPE;
    }
    
    public void randomTick(final BlockState cee, final ServerLevel aag, BlockPos fx, final Random random) {
        if (random.nextInt(25) == 0) {
            int integer6 = 5;
            final int integer7 = 4;
            for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-4, -1, -4), fx.offset(4, 1, 4))) {
                if (aag.getBlockState(fx2).is(this) && --integer6 <= 0) {
                    return;
                }
            }
            BlockPos fx3 = fx.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            for (int integer8 = 0; integer8 < 4; ++integer8) {
                if (aag.isEmptyBlock(fx3) && cee.canSurvive(aag, fx3)) {
                    fx = fx3;
                }
                fx3 = fx.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            if (aag.isEmptyBlock(fx3) && cee.canSurvive(aag, fx3)) {
                aag.setBlock(fx3, cee, 2);
            }
        }
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.isSolidRender(bqz, fx);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        final BlockState cee2 = brw.getBlockState(fx2);
        return cee2.is(BlockTags.MUSHROOM_GROW_BLOCK) || (brw.getRawBrightness(fx, 0) < 13 && this.mayPlaceOn(cee2, brw, fx2));
    }
    
    public boolean growMushroom(final ServerLevel aag, final BlockPos fx, final BlockState cee, final Random random) {
        aag.removeBlock(fx, false);
        ConfiguredFeature<?, ?> cis6;
        if (this == Blocks.BROWN_MUSHROOM) {
            cis6 = Features.HUGE_BROWN_MUSHROOM;
        }
        else {
            if (this != Blocks.RED_MUSHROOM) {
                aag.setBlock(fx, cee, 3);
                return false;
            }
            cis6 = Features.HUGE_RED_MUSHROOM;
        }
        if (cis6.place(aag, aag.getChunkSource().getGenerator(), random, fx)) {
            return true;
        }
        aag.setBlock(fx, cee, 3);
        return false;
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return true;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return random.nextFloat() < 0.4;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        this.growMushroom(aag, fx, cee, random);
    }
    
    static {
        SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
    }
}
