package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ObserverBlock extends DirectionalBlock {
    public static final BooleanProperty POWERED;
    
    public ObserverBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ObserverBlock.FACING, Direction.SOUTH)).<Comparable, Boolean>setValue((Property<Comparable>)ObserverBlock.POWERED, false));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ObserverBlock.FACING, ObserverBlock.POWERED);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)ObserverBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)ObserverBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)ObserverBlock.FACING)));
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Boolean>getValue((Property<Boolean>)ObserverBlock.POWERED)) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ObserverBlock.POWERED, false), 2);
        }
        else {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ObserverBlock.POWERED, true), 2);
            aag.getBlockTicks().scheduleTick(fx, this, 2);
        }
        this.updateNeighborsInFront(aag, fx, cee);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Comparable>getValue((Property<Comparable>)ObserverBlock.FACING) == gc && !cee1.<Boolean>getValue((Property<Boolean>)ObserverBlock.POWERED)) {
            this.startSignal(brv, fx5);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    private void startSignal(final LevelAccessor brv, final BlockPos fx) {
        if (!brv.isClientSide() && !brv.getBlockTicks().hasScheduledTick(fx, this)) {
            brv.getBlockTicks().scheduleTick(fx, this, 2);
        }
    }
    
    protected void updateNeighborsInFront(final Level bru, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)ObserverBlock.FACING);
        final BlockPos fx2 = fx.relative(gc5.getOpposite());
        bru.neighborChanged(fx2, this, fx);
        bru.updateNeighborsAtExceptFromFacing(fx2, this, gc5);
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.getSignal(bqz, fx, gc);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (cee.<Boolean>getValue((Property<Boolean>)ObserverBlock.POWERED) && cee.<Comparable>getValue((Property<Comparable>)ObserverBlock.FACING) == gc) {
            return 15;
        }
        return 0;
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        if (!bru.isClientSide() && cee1.<Boolean>getValue((Property<Boolean>)ObserverBlock.POWERED) && !bru.getBlockTicks().hasScheduledTick(fx, this)) {
            final BlockState cee5 = ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)ObserverBlock.POWERED, false);
            bru.setBlock(fx, cee5, 18);
            this.updateNeighborsInFront(bru, fx, cee5);
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        if (!bru.isClientSide && cee1.<Boolean>getValue((Property<Boolean>)ObserverBlock.POWERED) && bru.getBlockTicks().hasScheduledTick(fx, this)) {
            this.updateNeighborsInFront(bru, fx, ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)ObserverBlock.POWERED, false));
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)ObserverBlock.FACING, bnv.getNearestLookingDirection().getOpposite().getOpposite());
    }
    
    static {
        POWERED = BlockStateProperties.POWERED;
    }
}
