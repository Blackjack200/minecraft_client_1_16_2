package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AnvilMenu;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class AnvilBlock extends FallingBlock {
    public static final DirectionProperty FACING;
    private static final VoxelShape BASE;
    private static final VoxelShape X_LEG1;
    private static final VoxelShape X_LEG2;
    private static final VoxelShape X_TOP;
    private static final VoxelShape Z_LEG1;
    private static final VoxelShape Z_LEG2;
    private static final VoxelShape Z_TOP;
    private static final VoxelShape X_AXIS_AABB;
    private static final VoxelShape Z_AXIS_AABB;
    private static final Component CONTAINER_TITLE;
    
    public AnvilBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)AnvilBlock.FACING, Direction.NORTH));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)AnvilBlock.FACING, bnv.getHorizontalDirection().getClockWise());
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        bft.awardStat(Stats.INTERACT_WITH_ANVIL);
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        return new SimpleMenuProvider((integer, bfs, bft) -> new AnvilMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), AnvilBlock.CONTAINER_TITLE);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)AnvilBlock.FACING);
        if (gc6.getAxis() == Direction.Axis.X) {
            return AnvilBlock.X_AXIS_AABB;
        }
        return AnvilBlock.Z_AXIS_AABB;
    }
    
    @Override
    protected void falling(final FallingBlockEntity bcr) {
        bcr.setHurtsEntities(true);
    }
    
    @Override
    public void onLand(final Level bru, final BlockPos fx, final BlockState cee3, final BlockState cee4, final FallingBlockEntity bcr) {
        if (!bcr.isSilent()) {
            bru.levelEvent(1031, fx, 0);
        }
    }
    
    @Override
    public void onBroken(final Level bru, final BlockPos fx, final FallingBlockEntity bcr) {
        if (!bcr.isSilent()) {
            bru.levelEvent(1029, fx, 0);
        }
    }
    
    @Nullable
    public static BlockState damage(final BlockState cee) {
        if (cee.is(Blocks.ANVIL)) {
            return ((StateHolder<O, BlockState>)Blocks.CHIPPED_ANVIL.defaultBlockState()).<Comparable, Comparable>setValue((Property<Comparable>)AnvilBlock.FACING, (Comparable)cee.<V>getValue((Property<V>)AnvilBlock.FACING));
        }
        if (cee.is(Blocks.CHIPPED_ANVIL)) {
            return ((StateHolder<O, BlockState>)Blocks.DAMAGED_ANVIL.defaultBlockState()).<Comparable, Comparable>setValue((Property<Comparable>)AnvilBlock.FACING, (Comparable)cee.<V>getValue((Property<V>)AnvilBlock.FACING));
        }
        return null;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)AnvilBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)AnvilBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(AnvilBlock.FACING);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    @Override
    public int getDustColor(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.getMapColor(bqz, fx).col;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        BASE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
        X_LEG1 = Block.box(3.0, 4.0, 4.0, 13.0, 5.0, 12.0);
        X_LEG2 = Block.box(4.0, 5.0, 6.0, 12.0, 10.0, 10.0);
        X_TOP = Block.box(0.0, 10.0, 3.0, 16.0, 16.0, 13.0);
        Z_LEG1 = Block.box(4.0, 4.0, 3.0, 12.0, 5.0, 13.0);
        Z_LEG2 = Block.box(6.0, 5.0, 4.0, 10.0, 10.0, 12.0);
        Z_TOP = Block.box(3.0, 10.0, 0.0, 13.0, 16.0, 16.0);
        X_AXIS_AABB = Shapes.or(AnvilBlock.BASE, AnvilBlock.X_LEG1, AnvilBlock.X_LEG2, AnvilBlock.X_TOP);
        Z_AXIS_AABB = Shapes.or(AnvilBlock.BASE, AnvilBlock.Z_LEG1, AnvilBlock.Z_LEG2, AnvilBlock.Z_TOP);
        CONTAINER_TITLE = new TranslatableComponent("container.repair");
    }
}
