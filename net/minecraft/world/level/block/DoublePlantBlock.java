package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class DoublePlantBlock extends BushBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    
    public DoublePlantBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final DoubleBlockHalf cfa8 = cee1.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF);
        if (gc.getAxis() == Direction.Axis.Y && cfa8 == DoubleBlockHalf.LOWER == (gc == Direction.UP) && (!cee3.is(this) || cee3.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) == cfa8)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (cfa8 == DoubleBlockHalf.LOWER && gc == Direction.DOWN && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockPos fx3 = bnv.getClickedPos();
        if (fx3.getY() < 255 && bnv.getLevel().getBlockState(fx3.above()).canBeReplaced(bnv)) {
            return super.getStateForPlacement(bnv);
        }
        return null;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        bru.setBlock(fx.above(), ((StateHolder<O, BlockState>)this.defaultBlockState()).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 3);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        if (cee.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            final BlockState cee2 = brw.getBlockState(fx.below());
            return cee2.is(this) && cee2.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER;
        }
        return super.canSurvive(cee, brw, fx);
    }
    
    public void placeAt(final LevelAccessor brv, final BlockPos fx, final int integer) {
        brv.setBlock(fx, ((StateHolder<O, BlockState>)this.defaultBlockState()).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), integer);
        brv.setBlock(fx.above(), ((StateHolder<O, BlockState>)this.defaultBlockState()).<DoubleBlockHalf, DoubleBlockHalf>setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), integer);
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide) {
            if (bft.isCreative()) {
                preventCreativeDropFromBottomPart(bru, fx, cee, bft);
            }
            else {
                Block.dropResources(cee, bru, fx, null, bft, bft.getMainHandItem());
            }
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Override
    public void playerDestroy(final Level bru, final Player bft, final BlockPos fx, final BlockState cee, @Nullable final BlockEntity ccg, final ItemStack bly) {
        super.playerDestroy(bru, bft, fx, Blocks.AIR.defaultBlockState(), ccg, bly);
    }
    
    protected static void preventCreativeDropFromBottomPart(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        final DoubleBlockHalf cfa5 = cee.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF);
        if (cfa5 == DoubleBlockHalf.UPPER) {
            final BlockPos fx2 = fx.below();
            final BlockState cee2 = bru.getBlockState(fx2);
            if (cee2.getBlock() == cee.getBlock() && cee2.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
                bru.setBlock(fx2, Blocks.AIR.defaultBlockState(), 35);
                bru.levelEvent(bft, 2001, fx2, Block.getId(cee2));
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(DoublePlantBlock.HALF);
    }
    
    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
    
    @Override
    public long getSeed(final BlockState cee, final BlockPos fx) {
        return Mth.getSeed(fx.getX(), fx.below((cee.<DoubleBlockHalf>getValue(DoublePlantBlock.HALF) != DoubleBlockHalf.LOWER) ? 1 : 0).getY(), fx.getZ());
    }
    
    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    }
}
