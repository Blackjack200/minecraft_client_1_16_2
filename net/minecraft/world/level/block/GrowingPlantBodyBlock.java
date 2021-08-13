package net.minecraft.world.level.block;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import java.util.Random;
import net.minecraft.world.level.Level;
import java.util.Optional;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;

public abstract class GrowingPlantBodyBlock extends GrowingPlantBlock implements BonemealableBlock {
    protected GrowingPlantBodyBlock(final Properties c, final Direction gc, final VoxelShape dde, final boolean boolean4) {
        super(c, gc, dde, boolean4);
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == this.growthDirection.getOpposite() && !cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        final GrowingPlantHeadBlock bxe8 = this.getHeadBlock();
        if (gc == this.growthDirection) {
            final Block bul9 = cee3.getBlock();
            if (bul9 != this && bul9 != bxe8) {
                return bxe8.getStateForPlacement(brv);
            }
        }
        if (this.scheduleFluidTicks) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack(this.getHeadBlock());
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        final Optional<BlockPos> optional6 = this.getHeadPos(bqz, fx, cee);
        return optional6.isPresent() && this.getHeadBlock().canGrowInto(bqz.getBlockState(((BlockPos)optional6.get()).relative(this.growthDirection)));
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        final Optional<BlockPos> optional6 = this.getHeadPos(aag, fx, cee);
        if (optional6.isPresent()) {
            final BlockState cee2 = aag.getBlockState((BlockPos)optional6.get());
            ((GrowingPlantHeadBlock)cee2.getBlock()).performBonemeal(aag, random, (BlockPos)optional6.get(), cee2);
        }
    }
    
    private Optional<BlockPos> getHeadPos(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        BlockPos fx2 = fx;
        Block bul6;
        do {
            fx2 = fx2.relative(this.growthDirection);
            bul6 = bqz.getBlockState(fx2).getBlock();
        } while (bul6 == cee.getBlock());
        if (bul6 == this.getHeadBlock()) {
            return (Optional<BlockPos>)Optional.of(fx2);
        }
        return (Optional<BlockPos>)Optional.empty();
    }
    
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        final boolean boolean4 = super.canBeReplaced(cee, bnv);
        return (!boolean4 || bnv.getItemInHand().getItem() != this.getHeadBlock().asItem()) && boolean4;
    }
    
    @Override
    protected Block getBodyBlock() {
        return this;
    }
}
