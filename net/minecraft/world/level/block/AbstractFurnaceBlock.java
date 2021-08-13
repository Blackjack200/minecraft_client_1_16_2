package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public abstract class AbstractFurnaceBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty LIT;
    
    protected AbstractFurnaceBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)AbstractFurnaceBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)AbstractFurnaceBlock.LIT, false));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        this.openContainer(bru, fx, bft);
        return InteractionResult.CONSUME;
    }
    
    protected abstract void openContainer(final Level bru, final BlockPos fx, final Player bft);
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)AbstractFurnaceBlock.FACING, bnv.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof AbstractFurnaceBlockEntity) {
                ((AbstractFurnaceBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof AbstractFurnaceBlockEntity) {
            Containers.dropContents(bru, fx, (Container)ccg7);
            ((AbstractFurnaceBlockEntity)ccg7).getRecipesToAwardAndPopExperience(bru, Vec3.atCenterOf(fx));
            bru.updateNeighbourForOutputSignal(fx, this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(bru.getBlockEntity(fx));
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)AbstractFurnaceBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)AbstractFurnaceBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)AbstractFurnaceBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(AbstractFurnaceBlock.FACING, AbstractFurnaceBlock.LIT);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        LIT = BlockStateProperties.LIT;
    }
}
