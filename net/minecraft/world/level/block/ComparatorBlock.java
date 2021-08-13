package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.TickPriority;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ComparatorBlock extends DiodeBlock implements EntityBlock {
    public static final EnumProperty<ComparatorMode> MODE;
    
    public ComparatorBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ComparatorBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)ComparatorBlock.POWERED, false)).<ComparatorMode, ComparatorMode>setValue(ComparatorBlock.MODE, ComparatorMode.COMPARE));
    }
    
    @Override
    protected int getDelay(final BlockState cee) {
        return 2;
    }
    
    @Override
    protected int getOutputSignal(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        final BlockEntity ccg5 = bqz.getBlockEntity(fx);
        if (ccg5 instanceof ComparatorBlockEntity) {
            return ((ComparatorBlockEntity)ccg5).getOutputSignal();
        }
        return 0;
    }
    
    private int calculateOutputSignal(final Level bru, final BlockPos fx, final BlockState cee) {
        if (cee.<ComparatorMode>getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT) {
            return Math.max(this.getInputSignal(bru, fx, cee) - this.getAlternateSignal(bru, fx, cee), 0);
        }
        return this.getInputSignal(bru, fx, cee);
    }
    
    @Override
    protected boolean shouldTurnOn(final Level bru, final BlockPos fx, final BlockState cee) {
        final int integer5 = this.getInputSignal(bru, fx, cee);
        if (integer5 == 0) {
            return false;
        }
        final int integer6 = this.getAlternateSignal(bru, fx, cee);
        return integer5 > integer6 || (integer5 == integer6 && cee.<ComparatorMode>getValue(ComparatorBlock.MODE) == ComparatorMode.COMPARE);
    }
    
    @Override
    protected int getInputSignal(final Level bru, final BlockPos fx, final BlockState cee) {
        int integer5 = super.getInputSignal(bru, fx, cee);
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)ComparatorBlock.FACING);
        BlockPos fx2 = fx.relative(gc6);
        BlockState cee2 = bru.getBlockState(fx2);
        if (cee2.hasAnalogOutputSignal()) {
            integer5 = cee2.getAnalogOutputSignal(bru, fx2);
        }
        else if (integer5 < 15 && cee2.isRedstoneConductor(bru, fx2)) {
            fx2 = fx2.relative(gc6);
            cee2 = bru.getBlockState(fx2);
            final ItemFrame bcm9 = this.getItemFrame(bru, gc6, fx2);
            final int integer6 = Math.max((bcm9 == null) ? Integer.MIN_VALUE : bcm9.getAnalogOutput(), cee2.hasAnalogOutputSignal() ? cee2.getAnalogOutputSignal(bru, fx2) : Integer.MIN_VALUE);
            if (integer6 != Integer.MIN_VALUE) {
                integer5 = integer6;
            }
        }
        return integer5;
    }
    
    @Nullable
    private ItemFrame getItemFrame(final Level bru, final Direction gc, final BlockPos fx) {
        final List<ItemFrame> list5 = bru.<ItemFrame>getEntitiesOfClass((java.lang.Class<? extends ItemFrame>)ItemFrame.class, new AABB(fx.getX(), fx.getY(), fx.getZ(), fx.getX() + 1, fx.getY() + 1, fx.getZ() + 1), (java.util.function.Predicate<? super ItemFrame>)(bcm -> bcm != null && bcm.getDirection() == gc));
        if (list5.size() == 1) {
            return (ItemFrame)list5.get(0);
        }
        return null;
    }
    
    public InteractionResult use(BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (!bft.abilities.mayBuild) {
            return InteractionResult.PASS;
        }
        cee = ((StateHolder<O, BlockState>)cee).<ComparatorMode>cycle(ComparatorBlock.MODE);
        final float float8 = (cee.<ComparatorMode>getValue(ComparatorBlock.MODE) == ComparatorMode.SUBTRACT) ? 0.55f : 0.5f;
        bru.playSound(bft, fx, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3f, float8);
        bru.setBlock(fx, cee, 2);
        this.refreshOutputState(bru, fx, cee);
        return InteractionResult.sidedSuccess(bru.isClientSide);
    }
    
    @Override
    protected void checkTickOnNeighbor(final Level bru, final BlockPos fx, final BlockState cee) {
        if (bru.getBlockTicks().willTickThisTick(fx, this)) {
            return;
        }
        final int integer5 = this.calculateOutputSignal(bru, fx, cee);
        final BlockEntity ccg6 = bru.getBlockEntity(fx);
        final int integer6 = (ccg6 instanceof ComparatorBlockEntity) ? ((ComparatorBlockEntity)ccg6).getOutputSignal() : 0;
        if (integer5 != integer6 || cee.<Boolean>getValue((Property<Boolean>)ComparatorBlock.POWERED) != this.shouldTurnOn(bru, fx, cee)) {
            final TickPriority bsn8 = this.shouldPrioritize(bru, fx, cee) ? TickPriority.HIGH : TickPriority.NORMAL;
            bru.getBlockTicks().scheduleTick(fx, this, 2, bsn8);
        }
    }
    
    private void refreshOutputState(final Level bru, final BlockPos fx, final BlockState cee) {
        final int integer5 = this.calculateOutputSignal(bru, fx, cee);
        final BlockEntity ccg6 = bru.getBlockEntity(fx);
        int integer6 = 0;
        if (ccg6 instanceof ComparatorBlockEntity) {
            final ComparatorBlockEntity ccm8 = (ComparatorBlockEntity)ccg6;
            integer6 = ccm8.getOutputSignal();
            ccm8.setOutputSignal(integer5);
        }
        if (integer6 != integer5 || cee.<ComparatorMode>getValue(ComparatorBlock.MODE) == ComparatorMode.COMPARE) {
            final boolean boolean8 = this.shouldTurnOn(bru, fx, cee);
            final boolean boolean9 = cee.<Boolean>getValue((Property<Boolean>)ComparatorBlock.POWERED);
            if (boolean9 && !boolean8) {
                bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ComparatorBlock.POWERED, false), 2);
            }
            else if (!boolean9 && boolean8) {
                bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)ComparatorBlock.POWERED, true), 2);
            }
            this.updateNeighborsInFront(bru, fx, cee);
        }
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.refreshOutputState(aag, fx, cee);
    }
    
    public boolean triggerEvent(final BlockState cee, final Level bru, final BlockPos fx, final int integer4, final int integer5) {
        super.triggerEvent(cee, bru, fx, integer4, integer5);
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        return ccg7 != null && ccg7.triggerEvent(integer4, integer5);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new ComparatorBlockEntity();
    }
    
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ComparatorBlock.FACING, ComparatorBlock.MODE, ComparatorBlock.POWERED);
    }
    
    static {
        MODE = BlockStateProperties.MODE_COMPARATOR;
    }
}
