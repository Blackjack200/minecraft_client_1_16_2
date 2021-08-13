package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.MenuProvider;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.network.chat.Component;

public class StonecutterBlock extends Block {
    private static final Component CONTAINER_TITLE;
    public static final DirectionProperty FACING;
    protected static final VoxelShape SHAPE;
    
    public StonecutterBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)StonecutterBlock.FACING, Direction.NORTH));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StonecutterBlock.FACING, bnv.getHorizontalDirection().getOpposite());
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        bft.awardStat(Stats.INTERACT_WITH_STONECUTTER);
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return new SimpleMenuProvider((integer, bfs, bft) -> new StonecutterMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), StonecutterBlock.CONTAINER_TITLE);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return StonecutterBlock.SHAPE;
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)StonecutterBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)StonecutterBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)StonecutterBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(StonecutterBlock.FACING);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        CONTAINER_TITLE = new TranslatableComponent("container.stonecutter");
        FACING = HorizontalDirectionalBlock.FACING;
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
    }
}
