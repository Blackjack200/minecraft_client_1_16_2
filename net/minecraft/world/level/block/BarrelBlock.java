package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
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

public class BarrelBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty OPEN;
    
    public BarrelBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)BarrelBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)BarrelBlock.OPEN, false));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof BarrelBlockEntity) {
            bft.openMenu((MenuProvider)ccg8);
            bft.awardStat(Stats.OPEN_BARREL);
            PiglinAi.angerNearbyPiglins(bft, true);
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof Container) {
            Containers.dropContents(bru, fx, (Container)ccg7);
            bru.updateNeighbourForOutputSignal(fx, this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final BlockEntity ccg6 = aag.getBlockEntity(fx);
        if (ccg6 instanceof BarrelBlockEntity) {
            ((BarrelBlockEntity)ccg6).recheckOpen();
        }
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new BarrelBlockEntity();
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof BarrelBlockEntity) {
                ((BarrelBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
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
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)BarrelBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)BarrelBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)BarrelBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BarrelBlock.FACING, BarrelBlock.OPEN);
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)BarrelBlock.FACING, bnv.getNearestLookingDirection().getOpposite());
    }
    
    static {
        FACING = BlockStateProperties.FACING;
        OPEN = BlockStateProperties.OPEN;
    }
}
