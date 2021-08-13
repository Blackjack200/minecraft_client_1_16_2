package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;

public class EnderChestBlock extends AbstractChestBlock<EnderChestBlockEntity> implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape SHAPE;
    private static final Component CONTAINER_TITLE;
    
    protected EnderChestBlock(final Properties c) {
        super(c, () -> BlockEntityType.ENDER_CHEST);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)EnderChestBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)EnderChestBlock.WATERLOGGED, false));
    }
    
    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(final BlockState cee, final Level bru, final BlockPos fx, final boolean boolean4) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return EnderChestBlock.SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final FluidState cuu3 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        return (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)EnderChestBlock.FACING, bnv.getHorizontalDirection().getOpposite())).<Comparable, Boolean>setValue((Property<Comparable>)EnderChestBlock.WATERLOGGED, cuu3.getType() == Fluids.WATER);
    }
    
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final PlayerEnderChestContainer bjf8 = bft.getEnderChestInventory();
        final BlockEntity ccg9 = bru.getBlockEntity(fx);
        if (bjf8 == null || !(ccg9 instanceof EnderChestBlockEntity)) {
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        final BlockPos fx2 = fx.above();
        if (bru.getBlockState(fx2).isRedstoneConductor(bru, fx2)) {
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        final EnderChestBlockEntity ccs11 = (EnderChestBlockEntity)ccg9;
        bjf8.setActiveChest(ccs11);
        bft.openMenu(new SimpleMenuProvider((integer, bfs, bft) -> ChestMenu.threeRows(integer, bfs, bjf8), EnderChestBlock.CONTAINER_TITLE));
        bft.awardStat(Stats.OPEN_ENDERCHEST);
        PiglinAi.angerNearbyPiglins(bft, true);
        return InteractionResult.CONSUME;
    }
    
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new EnderChestBlockEntity();
    }
    
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        for (int integer6 = 0; integer6 < 3; ++integer6) {
            final int integer7 = random.nextInt(2) * 2 - 1;
            final int integer8 = random.nextInt(2) * 2 - 1;
            final double double9 = fx.getX() + 0.5 + 0.25 * integer7;
            final double double10 = fx.getY() + random.nextFloat();
            final double double11 = fx.getZ() + 0.5 + 0.25 * integer8;
            final double double12 = random.nextFloat() * integer7;
            final double double13 = (random.nextFloat() - 0.5) * 0.125;
            final double double14 = random.nextFloat() * integer8;
            bru.addParticle(ParticleTypes.PORTAL, double9, double10, double11, double12, double13, double14);
        }
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)EnderChestBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)EnderChestBlock.FACING)));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)EnderChestBlock.FACING)));
    }
    
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(EnderChestBlock.FACING, EnderChestBlock.WATERLOGGED);
    }
    
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)EnderChestBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)EnderChestBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
        CONTAINER_TITLE = new TranslatableComponent("container.enderchest");
    }
}
