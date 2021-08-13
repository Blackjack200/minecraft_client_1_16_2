package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.core.PositionImpl;
import net.minecraft.core.Position;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;
import net.minecraft.core.BlockSourceImpl;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
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
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class DispenserBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty TRIGGERED;
    private static final Map<Item, DispenseItemBehavior> DISPENSER_REGISTRY;
    
    public static void registerBehavior(final ItemLike brt, final DispenseItemBehavior gw) {
        DispenserBlock.DISPENSER_REGISTRY.put(brt.asItem(), gw);
    }
    
    protected DispenserBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)DispenserBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)DispenserBlock.TRIGGERED, false));
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof DispenserBlockEntity) {
            bft.openMenu((MenuProvider)ccg8);
            if (ccg8 instanceof DropperBlockEntity) {
                bft.awardStat(Stats.INSPECT_DROPPER);
            }
            else {
                bft.awardStat(Stats.INSPECT_DISPENSER);
            }
        }
        return InteractionResult.CONSUME;
    }
    
    protected void dispenseFrom(final ServerLevel aag, final BlockPos fx) {
        final BlockSourceImpl fz4 = new BlockSourceImpl(aag, fx);
        final DispenserBlockEntity ccp5 = fz4.<DispenserBlockEntity>getEntity();
        final int integer6 = ccp5.getRandomSlot();
        if (integer6 < 0) {
            aag.levelEvent(1001, fx, 0);
            return;
        }
        final ItemStack bly7 = ccp5.getItem(integer6);
        final DispenseItemBehavior gw8 = this.getDispenseMethod(bly7);
        if (gw8 != DispenseItemBehavior.NOOP) {
            ccp5.setItem(integer6, gw8.dispense(fz4, bly7));
        }
    }
    
    protected DispenseItemBehavior getDispenseMethod(final ItemStack bly) {
        return (DispenseItemBehavior)DispenserBlock.DISPENSER_REGISTRY.get(bly.getItem());
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        final boolean boolean7 = bru.hasNeighborSignal(fx3) || bru.hasNeighborSignal(fx3.above());
        final boolean boolean8 = cee.<Boolean>getValue((Property<Boolean>)DispenserBlock.TRIGGERED);
        if (boolean7 && !boolean8) {
            bru.getBlockTicks().scheduleTick(fx3, this, 4);
            bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DispenserBlock.TRIGGERED, true), 4);
        }
        else if (!boolean7 && boolean8) {
            bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)DispenserBlock.TRIGGERED, false), 4);
        }
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.dispenseFrom(aag, fx);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new DispenserBlockEntity();
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)DispenserBlock.FACING, bnv.getNearestLookingDirection().getOpposite());
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof DispenserBlockEntity) {
                ((DispenserBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof DispenserBlockEntity) {
            Containers.dropContents(bru, fx, (Container)ccg7);
            bru.updateNeighbourForOutputSignal(fx, this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    public static Position getDispensePosition(final BlockSource fy) {
        final Direction gc2 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
        final double double3 = fy.x() + 0.7 * gc2.getStepX();
        final double double4 = fy.y() + 0.7 * gc2.getStepY();
        final double double5 = fy.z() + 0.7 * gc2.getStepZ();
        return new PositionImpl(double3, double4, double5);
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
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)DispenserBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)DispenserBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)DispenserBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(DispenserBlock.FACING, DispenserBlock.TRIGGERED);
    }
    
    static {
        FACING = DirectionalBlock.FACING;
        TRIGGERED = BlockStateProperties.TRIGGERED;
        DISPENSER_REGISTRY = Util.<Map>make((Map)new Object2ObjectOpenHashMap(), (java.util.function.Consumer<Map>)(object2ObjectOpenHashMap -> object2ObjectOpenHashMap.defaultReturnValue(new DefaultDispenseItemBehavior())));
    }
}
