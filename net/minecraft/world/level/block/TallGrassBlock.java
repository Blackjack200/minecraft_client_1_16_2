package net.minecraft.world.level.block;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TallGrassBlock extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE;
    
    protected TallGrassBlock(final Properties c) {
        super(c);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return TallGrassBlock.SHAPE;
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return true;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        final DoublePlantBlock bwa6 = (DoublePlantBlock)((this == Blocks.FERN) ? Blocks.LARGE_FERN : Blocks.TALL_GRASS);
        if (bwa6.defaultBlockState().canSurvive(aag, fx) && aag.isEmptyBlock(fx.above())) {
            bwa6.placeAt(aag, fx, 2);
        }
    }
    
    public OffsetType getOffsetType() {
        return OffsetType.XYZ;
    }
    
    static {
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    }
}
