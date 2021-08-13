package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class LecternBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty HAS_BOOK;
    public static final VoxelShape SHAPE_BASE;
    public static final VoxelShape SHAPE_POST;
    public static final VoxelShape SHAPE_COMMON;
    public static final VoxelShape SHAPE_TOP_PLATE;
    public static final VoxelShape SHAPE_COLLISION;
    public static final VoxelShape SHAPE_WEST;
    public static final VoxelShape SHAPE_NORTH;
    public static final VoxelShape SHAPE_EAST;
    public static final VoxelShape SHAPE_SOUTH;
    
    protected LecternBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)LecternBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)LecternBlock.POWERED, false)).<Comparable, Boolean>setValue((Property<Comparable>)LecternBlock.HAS_BOOK, false));
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public VoxelShape getOcclusionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return LecternBlock.SHAPE_COMMON;
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Level bru3 = bnv.getLevel();
        final ItemStack bly4 = bnv.getItemInHand();
        final CompoundTag md5 = bly4.getTag();
        final Player bft6 = bnv.getPlayer();
        boolean boolean7 = false;
        if (!bru3.isClientSide && bft6 != null && md5 != null && bft6.canUseGameMasterBlocks() && md5.contains("BlockEntityTag")) {
            final CompoundTag md6 = md5.getCompound("BlockEntityTag");
            if (md6.contains("Book")) {
                boolean7 = true;
            }
        }
        return (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)LecternBlock.FACING, bnv.getHorizontalDirection().getOpposite())).<Comparable, Boolean>setValue((Property<Comparable>)LecternBlock.HAS_BOOK, boolean7);
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return LecternBlock.SHAPE_COLLISION;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction>getValue((Property<Direction>)LecternBlock.FACING)) {
            case NORTH: {
                return LecternBlock.SHAPE_NORTH;
            }
            case SOUTH: {
                return LecternBlock.SHAPE_SOUTH;
            }
            case EAST: {
                return LecternBlock.SHAPE_EAST;
            }
            case WEST: {
                return LecternBlock.SHAPE_WEST;
            }
            default: {
                return LecternBlock.SHAPE_COMMON;
            }
        }
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)LecternBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)LecternBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)LecternBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(LecternBlock.FACING, LecternBlock.POWERED, LecternBlock.HAS_BOOK);
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new LecternBlockEntity();
    }
    
    public static boolean tryPlaceBook(final Level bru, final BlockPos fx, final BlockState cee, final ItemStack bly) {
        if (!cee.<Boolean>getValue((Property<Boolean>)LecternBlock.HAS_BOOK)) {
            if (!bru.isClientSide) {
                placeBook(bru, fx, cee, bly);
            }
            return true;
        }
        return false;
    }
    
    private static void placeBook(final Level bru, final BlockPos fx, final BlockState cee, final ItemStack bly) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof LecternBlockEntity) {
            final LecternBlockEntity ccy6 = (LecternBlockEntity)ccg5;
            ccy6.setBook(bly.split(1));
            resetBookState(bru, fx, cee, true);
            bru.playSound(null, fx, SoundEvents.BOOK_PUT, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
    }
    
    public static void resetBookState(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        bru.setBlock(fx, (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)LecternBlock.POWERED, false)).<Comparable, Boolean>setValue((Property<Comparable>)LecternBlock.HAS_BOOK, boolean4), 3);
        updateBelow(bru, fx, cee);
    }
    
    public static void signalPageChange(final Level bru, final BlockPos fx, final BlockState cee) {
        changePowered(bru, fx, cee, true);
        bru.getBlockTicks().scheduleTick(fx, cee.getBlock(), 2);
        bru.levelEvent(1043, fx, 0);
    }
    
    private static void changePowered(final Level bru, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)LecternBlock.POWERED, boolean4), 3);
        updateBelow(bru, fx, cee);
    }
    
    private static void updateBelow(final Level bru, final BlockPos fx, final BlockState cee) {
        bru.updateNeighborsAt(fx.below(), cee.getBlock());
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        changePowered(aag, fx, cee, false);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)LecternBlock.HAS_BOOK)) {
            this.popBook(cee1, bru, fx);
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)LecternBlock.POWERED)) {
            bru.updateNeighborsAt(fx.below(), this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    private void popBook(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof LecternBlockEntity) {
            final LecternBlockEntity ccy6 = (LecternBlockEntity)ccg5;
            final Direction gc7 = cee.<Direction>getValue((Property<Direction>)LecternBlock.FACING);
            final ItemStack bly8 = ccy6.getBook().copy();
            final float float9 = 0.25f * gc7.getStepX();
            final float float10 = 0.25f * gc7.getStepZ();
            final ItemEntity bcs11 = new ItemEntity(bru, fx.getX() + 0.5 + float9, fx.getY() + 1, fx.getZ() + 0.5 + float10, bly8);
            bcs11.setDefaultPickUpDelay();
            bru.addFreshEntity(bcs11);
            ccy6.clearContent();
        }
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Boolean>getValue((Property<Boolean>)LecternBlock.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return (gc == Direction.UP && cee.<Boolean>getValue((Property<Boolean>)LecternBlock.POWERED)) ? 15 : 0;
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        if (cee.<Boolean>getValue((Property<Boolean>)LecternBlock.HAS_BOOK)) {
            final BlockEntity ccg5 = bru.getBlockEntity(fx);
            if (ccg5 instanceof LecternBlockEntity) {
                return ((LecternBlockEntity)ccg5).getRedstoneSignal();
            }
        }
        return 0;
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (cee.<Boolean>getValue((Property<Boolean>)LecternBlock.HAS_BOOK)) {
            if (!bru.isClientSide) {
                this.openScreen(bru, fx, bft);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        final ItemStack bly8 = bft.getItemInHand(aoq);
        if (bly8.isEmpty() || bly8.getItem().is(ItemTags.LECTERN_BOOKS)) {
            return InteractionResult.PASS;
        }
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        if (!cee.<Boolean>getValue((Property<Boolean>)LecternBlock.HAS_BOOK)) {
            return null;
        }
        return super.getMenuProvider(cee, bru, fx);
    }
    
    private void openScreen(final Level bru, final BlockPos fx, final Player bft) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof LecternBlockEntity) {
            bft.openMenu((MenuProvider)ccg5);
            bft.awardStat(Stats.INTERACT_WITH_LECTERN);
        }
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        POWERED = BlockStateProperties.POWERED;
        HAS_BOOK = BlockStateProperties.HAS_BOOK;
        SHAPE_BASE = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
        SHAPE_POST = Block.box(4.0, 2.0, 4.0, 12.0, 14.0, 12.0);
        SHAPE_COMMON = Shapes.or(LecternBlock.SHAPE_BASE, LecternBlock.SHAPE_POST);
        SHAPE_TOP_PLATE = Block.box(0.0, 15.0, 0.0, 16.0, 15.0, 16.0);
        SHAPE_COLLISION = Shapes.or(LecternBlock.SHAPE_COMMON, LecternBlock.SHAPE_TOP_PLATE);
        SHAPE_WEST = Shapes.or(Block.box(1.0, 10.0, 0.0, 5.333333, 14.0, 16.0), Block.box(5.333333, 12.0, 0.0, 9.666667, 16.0, 16.0), Block.box(9.666667, 14.0, 0.0, 14.0, 18.0, 16.0), LecternBlock.SHAPE_COMMON);
        SHAPE_NORTH = Shapes.or(Block.box(0.0, 10.0, 1.0, 16.0, 14.0, 5.333333), Block.box(0.0, 12.0, 5.333333, 16.0, 16.0, 9.666667), Block.box(0.0, 14.0, 9.666667, 16.0, 18.0, 14.0), LecternBlock.SHAPE_COMMON);
        SHAPE_EAST = Shapes.or(Block.box(15.0, 10.0, 0.0, 10.666667, 14.0, 16.0), Block.box(10.666667, 12.0, 0.0, 6.333333, 16.0, 16.0), Block.box(6.333333, 14.0, 0.0, 2.0, 18.0, 16.0), LecternBlock.SHAPE_COMMON);
        SHAPE_SOUTH = Shapes.or(Block.box(0.0, 10.0, 15.0, 16.0, 14.0, 10.666667), Block.box(0.0, 12.0, 10.666667, 16.0, 16.0, 6.333333), Block.box(0.0, 14.0, 6.333333, 16.0, 18.0, 2.0), LecternBlock.SHAPE_COMMON);
    }
}
