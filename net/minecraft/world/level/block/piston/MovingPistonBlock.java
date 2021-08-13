package net.minecraft.world.level.block.piston;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.Collections;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.BaseEntityBlock;

public class MovingPistonBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<PistonType> TYPE;
    
    public MovingPistonBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)MovingPistonBlock.FACING, Direction.NORTH)).<PistonType, PistonType>setValue(MovingPistonBlock.TYPE, PistonType.DEFAULT));
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return null;
    }
    
    public static BlockEntity newMovingBlockEntity(final BlockState cee, final Direction gc, final boolean boolean3, final boolean boolean4) {
        return new PistonMovingBlockEntity(cee, gc, boolean3, boolean4);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof PistonMovingBlockEntity) {
            ((PistonMovingBlockEntity)ccg7).finalTick();
        }
    }
    
    @Override
    public void destroy(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        final BlockPos fx2 = fx.relative(cee.<Direction>getValue((Property<Direction>)MovingPistonBlock.FACING).getOpposite());
        final BlockState cee2 = brv.getBlockState(fx2);
        if (cee2.getBlock() instanceof PistonBaseBlock && cee2.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED)) {
            brv.removeBlock(fx2, false);
        }
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (!bru.isClientSide && bru.getBlockEntity(fx) == null) {
            bru.removeBlock(fx, false);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public List<ItemStack> getDrops(final BlockState cee, final LootContext.Builder a) {
        final PistonMovingBlockEntity cea4 = this.getBlockEntity(a.getLevel(), new BlockPos(a.<Vec3>getParameter(LootContextParams.ORIGIN)));
        if (cea4 == null) {
            return (List<ItemStack>)Collections.emptyList();
        }
        return cea4.getMovedState().getDrops(a);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.empty();
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final PistonMovingBlockEntity cea6 = this.getBlockEntity(bqz, fx);
        if (cea6 != null) {
            return cea6.getCollisionShape(bqz, fx);
        }
        return Shapes.empty();
    }
    
    @Nullable
    private PistonMovingBlockEntity getBlockEntity(final BlockGetter bqz, final BlockPos fx) {
        final BlockEntity ccg4 = bqz.getBlockEntity(fx);
        if (ccg4 instanceof PistonMovingBlockEntity) {
            return (PistonMovingBlockEntity)ccg4;
        }
        return null;
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return ItemStack.EMPTY;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)MovingPistonBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)MovingPistonBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)MovingPistonBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(MovingPistonBlock.FACING, MovingPistonBlock.TYPE);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        FACING = PistonHeadBlock.FACING;
        TYPE = PistonHeadBlock.TYPE;
    }
}
