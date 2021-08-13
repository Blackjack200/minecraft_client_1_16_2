package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.inventory.AbstractContainerMenu;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.animal.Cat;
import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import java.util.function.BiPredicate;
import java.util.function.Function;
import net.minecraft.stats.Stats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.stats.Stat;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Container;
import java.util.Optional;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ChestBlock extends AbstractChestBlock<ChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<ChestType> TYPE;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape AABB;
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>> CHEST_COMBINER;
    private static final DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>> MENU_PROVIDER_COMBINER;
    
    protected ChestBlock(final Properties c, final Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(c, supplier);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)ChestBlock.FACING, Direction.NORTH)).setValue(ChestBlock.TYPE, ChestType.SINGLE)).<Comparable, Boolean>setValue((Property<Comparable>)ChestBlock.WATERLOGGED, false));
    }
    
    public static DoubleBlockCombiner.BlockType getBlockType(final BlockState cee) {
        final ChestType cew2 = cee.<ChestType>getValue(ChestBlock.TYPE);
        if (cew2 == ChestType.SINGLE) {
            return DoubleBlockCombiner.BlockType.SINGLE;
        }
        if (cew2 == ChestType.RIGHT) {
            return DoubleBlockCombiner.BlockType.FIRST;
        }
        return DoubleBlockCombiner.BlockType.SECOND;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)ChestBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (cee3.is(this) && gc.getAxis().isHorizontal()) {
            final ChestType cew8 = cee3.<ChestType>getValue(ChestBlock.TYPE);
            if (cee1.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE && cew8 != ChestType.SINGLE && cee1.<Comparable>getValue((Property<Comparable>)ChestBlock.FACING) == cee3.<Comparable>getValue((Property<Comparable>)ChestBlock.FACING) && getConnectedDirection(cee3) == gc.getOpposite()) {
                return ((StateHolder<O, BlockState>)cee1).<ChestType, ChestType>setValue(ChestBlock.TYPE, cew8.getOpposite());
            }
        }
        else if (getConnectedDirection(cee1) == gc) {
            return ((StateHolder<O, BlockState>)cee1).<ChestType, ChestType>setValue(ChestBlock.TYPE, ChestType.SINGLE);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (cee.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
            return ChestBlock.AABB;
        }
        switch (getConnectedDirection(cee)) {
            default: {
                return ChestBlock.NORTH_AABB;
            }
            case SOUTH: {
                return ChestBlock.SOUTH_AABB;
            }
            case WEST: {
                return ChestBlock.WEST_AABB;
            }
            case EAST: {
                return ChestBlock.EAST_AABB;
            }
        }
    }
    
    public static Direction getConnectedDirection(final BlockState cee) {
        final Direction gc2 = cee.<Direction>getValue((Property<Direction>)ChestBlock.FACING);
        return (cee.<ChestType>getValue(ChestBlock.TYPE) == ChestType.LEFT) ? gc2.getClockWise() : gc2.getCounterClockWise();
    }
    
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        ChestType cew3 = ChestType.SINGLE;
        Direction gc4 = bnv.getHorizontalDirection().getOpposite();
        final FluidState cuu5 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final boolean boolean6 = bnv.isSecondaryUseActive();
        final Direction gc5 = bnv.getClickedFace();
        if (gc5.getAxis().isHorizontal() && boolean6) {
            final Direction gc6 = this.candidatePartnerFacing(bnv, gc5.getOpposite());
            if (gc6 != null && gc6.getAxis() != gc5.getAxis()) {
                gc4 = gc6;
                cew3 = ((gc4.getCounterClockWise() == gc5.getOpposite()) ? ChestType.RIGHT : ChestType.LEFT);
            }
        }
        if (cew3 == ChestType.SINGLE && !boolean6) {
            if (gc4 == this.candidatePartnerFacing(bnv, gc4.getClockWise())) {
                cew3 = ChestType.LEFT;
            }
            else if (gc4 == this.candidatePartnerFacing(bnv, gc4.getCounterClockWise())) {
                cew3 = ChestType.RIGHT;
            }
        }
        return ((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)ChestBlock.FACING, gc4)).setValue(ChestBlock.TYPE, cew3)).<Comparable, Boolean>setValue((Property<Comparable>)ChestBlock.WATERLOGGED, cuu5.getType() == Fluids.WATER);
    }
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)ChestBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Nullable
    private Direction candidatePartnerFacing(final BlockPlaceContext bnv, final Direction gc) {
        final BlockState cee4 = bnv.getLevel().getBlockState(bnv.getClickedPos().relative(gc));
        return (cee4.is(this) && cee4.<ChestType>getValue(ChestBlock.TYPE) == ChestType.SINGLE) ? cee4.<Direction>getValue((Property<Direction>)ChestBlock.FACING) : null;
    }
    
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof ChestBlockEntity) {
                ((ChestBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
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
    
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final MenuProvider aou8 = this.getMenuProvider(cee, bru, fx);
        if (aou8 != null) {
            bft.openMenu(aou8);
            bft.awardStat(this.getOpenChestStat());
            PiglinAi.angerNearbyPiglins(bft, true);
        }
        return InteractionResult.CONSUME;
    }
    
    protected Stat<ResourceLocation> getOpenChestStat() {
        return Stats.CUSTOM.get(Stats.OPEN_CHEST);
    }
    
    @Nullable
    public static Container getContainer(final ChestBlock bvb, final BlockState cee, final Level bru, final BlockPos fx, final boolean boolean5) {
        return (Container)bvb.combine(cee, bru, fx, boolean5).<Optional<Container>>apply(ChestBlock.CHEST_COMBINER).orElse(null);
    }
    
    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(final BlockState cee, final Level bru, final BlockPos fx, final boolean boolean4) {
        BiPredicate<LevelAccessor, BlockPos> biPredicate6;
        if (boolean4) {
            biPredicate6 = (BiPredicate<LevelAccessor, BlockPos>)((brv, fx) -> false);
        }
        else {
            biPredicate6 = (BiPredicate<LevelAccessor, BlockPos>)ChestBlock::isChestBlockedAt;
        }
        return DoubleBlockCombiner.combineWithNeigbour(this.blockEntityType.get(), (Function<BlockState, DoubleBlockCombiner.BlockType>)ChestBlock::getBlockType, (Function<BlockState, Direction>)ChestBlock::getConnectedDirection, ChestBlock.FACING, cee, bru, fx, biPredicate6);
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return (MenuProvider)this.combine(cee, bru, fx, false).<Optional<MenuProvider>>apply(ChestBlock.MENU_PROVIDER_COMBINER).orElse(null);
    }
    
    public static DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction> opennessCombiner(final LidBlockEntity ccz) {
        return new DoubleBlockCombiner.Combiner<ChestBlockEntity, Float2FloatFunction>() {
            public Float2FloatFunction acceptDouble(final ChestBlockEntity cck1, final ChestBlockEntity cck2) {
                return float3 -> Math.max(cck1.getOpenNess(float3), cck2.getOpenNess(float3));
            }
            
            public Float2FloatFunction acceptSingle(final ChestBlockEntity cck) {
                return cck::getOpenNess;
            }
            
            public Float2FloatFunction acceptNone() {
                return ccz::getOpenNess;
            }
        };
    }
    
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new ChestBlockEntity();
    }
    
    public static boolean isChestBlockedAt(final LevelAccessor brv, final BlockPos fx) {
        return isBlockedChestByBlock(brv, fx) || isCatSittingOnChest(brv, fx);
    }
    
    private static boolean isBlockedChestByBlock(final BlockGetter bqz, final BlockPos fx) {
        final BlockPos fx2 = fx.above();
        return bqz.getBlockState(fx2).isRedstoneConductor(bqz, fx2);
    }
    
    private static boolean isCatSittingOnChest(final LevelAccessor brv, final BlockPos fx) {
        final List<Cat> list3 = brv.<Cat>getEntitiesOfClass((java.lang.Class<? extends Cat>)Cat.class, new AABB(fx.getX(), fx.getY() + 1, fx.getZ(), fx.getX() + 1, fx.getY() + 2, fx.getZ() + 1));
        if (!list3.isEmpty()) {
            for (final Cat azy5 : list3) {
                if (azy5.isInSittingPose()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(getContainer(this, cee, bru, fx, false));
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)ChestBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)ChestBlock.FACING)));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)ChestBlock.FACING)));
    }
    
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ChestBlock.FACING, ChestBlock.TYPE, ChestBlock.WATERLOGGED);
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        TYPE = BlockStateProperties.CHEST_TYPE;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        NORTH_AABB = Block.box(1.0, 0.0, 0.0, 15.0, 14.0, 15.0);
        SOUTH_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 1.0, 15.0, 14.0, 15.0);
        EAST_AABB = Block.box(1.0, 0.0, 1.0, 16.0, 14.0, 15.0);
        AABB = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
        CHEST_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<Container>>() {
            public Optional<Container> acceptDouble(final ChestBlockEntity cck1, final ChestBlockEntity cck2) {
                return (Optional<Container>)Optional.of(new CompoundContainer(cck1, cck2));
            }
            
            public Optional<Container> acceptSingle(final ChestBlockEntity cck) {
                return (Optional<Container>)Optional.of(cck);
            }
            
            public Optional<Container> acceptNone() {
                return (Optional<Container>)Optional.empty();
            }
        };
        MENU_PROVIDER_COMBINER = new DoubleBlockCombiner.Combiner<ChestBlockEntity, Optional<MenuProvider>>() {
            public Optional<MenuProvider> acceptDouble(final ChestBlockEntity cck1, final ChestBlockEntity cck2) {
                final Container aok4 = new CompoundContainer(cck1, cck2);
                return (Optional<MenuProvider>)Optional.of(new MenuProvider() {
                    @Nullable
                    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
                        if (cck1.canOpen(bft) && cck2.canOpen(bft)) {
                            cck1.unpackLootTable(bfs.player);
                            cck2.unpackLootTable(bfs.player);
                            return ChestMenu.sixRows(integer, bfs, aok4);
                        }
                        return null;
                    }
                    
                    public Component getDisplayName() {
                        if (cck1.hasCustomName()) {
                            return cck1.getDisplayName();
                        }
                        if (cck2.hasCustomName()) {
                            return cck2.getDisplayName();
                        }
                        return new TranslatableComponent("container.chestDouble");
                    }
                });
            }
            
            public Optional<MenuProvider> acceptSingle(final ChestBlockEntity cck) {
                return (Optional<MenuProvider>)Optional.of(cck);
            }
            
            public Optional<MenuProvider> acceptNone() {
                return (Optional<MenuProvider>)Optional.empty();
            }
        };
    }
}
