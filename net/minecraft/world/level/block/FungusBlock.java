package net.minecraft.world.level.block;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FungusBlock extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE;
    private final Supplier<ConfiguredFeature<HugeFungusConfiguration, ?>> feature;
    
    protected FungusBlock(final Properties c, final Supplier<ConfiguredFeature<HugeFungusConfiguration, ?>> supplier) {
        super(c);
        this.feature = supplier;
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return FungusBlock.SHAPE;
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.is(BlockTags.NYLIUM) || cee.is(Blocks.MYCELIUM) || cee.is(Blocks.SOUL_SOIL) || super.mayPlaceOn(cee, bqz, fx);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        final Block bul6 = ((HugeFungusConfiguration)((ConfiguredFeature)this.feature.get()).config).validBaseState.getBlock();
        final Block bul7 = bqz.getBlockState(fx.below()).getBlock();
        return bul7 == bul6;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return random.nextFloat() < 0.4;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        ((ConfiguredFeature)this.feature.get()).place(aag, aag.getChunkSource().getGenerator(), random, fx);
    }
    
    static {
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);
    }
}
