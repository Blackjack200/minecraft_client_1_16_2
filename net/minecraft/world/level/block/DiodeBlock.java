package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.TickPriority;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class DiodeBlock extends HorizontalDirectionalBlock {
    protected static final VoxelShape SHAPE;
    public static final BooleanProperty POWERED;
    
    protected DiodeBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return DiodeBlock.SHAPE;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return Block.canSupportRigidBlock(brw, fx.below());
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (this.isLocked(aag, fx, cee)) {
            return;
        }
        final boolean boolean6 = cee.<Boolean>getValue((Property<Boolean>)DiodeBlock.POWERED);
        final boolean boolean7 = this.shouldTurnOn(aag, fx, cee);
        if (boolean6 && !boolean7) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DiodeBlock.POWERED, false), 2);
        }
        else if (!boolean6) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DiodeBlock.POWERED, true), 2);
            if (!boolean7) {
                aag.getBlockTicks().scheduleTick(fx, this, this.getDelay(cee), TickPriority.VERY_HIGH);
            }
        }
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.getSignal(bqz, fx, gc);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (!cee.<Boolean>getValue((Property<Boolean>)DiodeBlock.POWERED)) {
            return 0;
        }
        if (cee.<Comparable>getValue((Property<Comparable>)DiodeBlock.FACING) == gc) {
            return this.getOutputSignal(bqz, fx, cee);
        }
        return 0;
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (cee.canSurvive(bru, fx3)) {
            this.checkTickOnNeighbor(bru, fx3, cee);
            return;
        }
        final BlockEntity ccg8 = this.isEntityBlock() ? bru.getBlockEntity(fx3) : null;
        Block.dropResources(cee, bru, fx3, ccg8);
        bru.removeBlock(fx3, false);
        for (final Direction gc12 : Direction.values()) {
            bru.updateNeighborsAt(fx3.relative(gc12), this);
        }
    }
    
    protected void checkTickOnNeighbor(final Level bru, final BlockPos fx, final BlockState cee) {
        if (this.isLocked(bru, fx, cee)) {
            return;
        }
        final boolean boolean5 = cee.<Boolean>getValue((Property<Boolean>)DiodeBlock.POWERED);
        final boolean boolean6 = this.shouldTurnOn(bru, fx, cee);
        if (boolean5 != boolean6 && !bru.getBlockTicks().willTickThisTick(fx, this)) {
            TickPriority bsn7 = TickPriority.HIGH;
            if (this.shouldPrioritize(bru, fx, cee)) {
                bsn7 = TickPriority.EXTREMELY_HIGH;
            }
            else if (boolean5) {
                bsn7 = TickPriority.VERY_HIGH;
            }
            bru.getBlockTicks().scheduleTick(fx, this, this.getDelay(cee), bsn7);
        }
    }
    
    public boolean isLocked(final LevelReader brw, final BlockPos fx, final BlockState cee) {
        return false;
    }
    
    protected boolean shouldTurnOn(final Level bru, final BlockPos fx, final BlockState cee) {
        return this.getInputSignal(bru, fx, cee) > 0;
    }
    
    protected int getInputSignal(final Level bru, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)DiodeBlock.FACING);
        final BlockPos fx2 = fx.relative(gc5);
        final int integer7 = bru.getSignal(fx2, gc5);
        if (integer7 >= 15) {
            return integer7;
        }
        final BlockState cee2 = bru.getBlockState(fx2);
        return Math.max(integer7, cee2.is(Blocks.REDSTONE_WIRE) ? ((int)cee2.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER)) : 0);
    }
    
    protected int getAlternateSignal(final LevelReader brw, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)DiodeBlock.FACING);
        final Direction gc6 = gc5.getClockWise();
        final Direction gc7 = gc5.getCounterClockWise();
        return Math.max(this.getAlternateSignalAt(brw, fx.relative(gc6), gc6), this.getAlternateSignalAt(brw, fx.relative(gc7), gc7));
    }
    
    protected int getAlternateSignalAt(final LevelReader brw, final BlockPos fx, final Direction gc) {
        final BlockState cee5 = brw.getBlockState(fx);
        if (!this.isAlternateInput(cee5)) {
            return 0;
        }
        if (cee5.is(Blocks.REDSTONE_BLOCK)) {
            return 15;
        }
        if (cee5.is(Blocks.REDSTONE_WIRE)) {
            return cee5.<Integer>getValue((Property<Integer>)RedStoneWireBlock.POWER);
        }
        return brw.getDirectSignal(fx, gc);
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)DiodeBlock.FACING, bnv.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (this.shouldTurnOn(bru, fx, cee)) {
            bru.getBlockTicks().scheduleTick(fx, this, 1);
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        this.updateNeighborsInFront(bru, fx, cee1);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
        this.updateNeighborsInFront(bru, fx, cee1);
    }
    
    protected void updateNeighborsInFront(final Level bru, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)DiodeBlock.FACING);
        final BlockPos fx2 = fx.relative(gc5.getOpposite());
        bru.neighborChanged(fx2, this, fx);
        bru.updateNeighborsAtExceptFromFacing(fx2, this, gc5);
    }
    
    protected boolean isAlternateInput(final BlockState cee) {
        return cee.isSignalSource();
    }
    
    protected int getOutputSignal(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return 15;
    }
    
    public static boolean isDiode(final BlockState cee) {
        return cee.getBlock() instanceof DiodeBlock;
    }
    
    public boolean shouldPrioritize(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)DiodeBlock.FACING).getOpposite();
        final BlockState cee2 = bqz.getBlockState(fx.relative(gc5));
        return isDiode(cee2) && cee2.<Comparable>getValue((Property<Comparable>)DiodeBlock.FACING) != gc5;
    }
    
    protected abstract int getDelay(final BlockState cee);
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        POWERED = BlockStateProperties.POWERED;
    }
}
