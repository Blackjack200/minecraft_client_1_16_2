package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import com.google.common.base.MoreObjects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class TripWireHookBlock extends Block {
    public static final DirectionProperty FACING;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty ATTACHED;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    
    public TripWireHookBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)TripWireHookBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)TripWireHookBlock.POWERED, false)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, false));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction>getValue((Property<Direction>)TripWireHookBlock.FACING)) {
            default: {
                return TripWireHookBlock.EAST_AABB;
            }
            case WEST: {
                return TripWireHookBlock.WEST_AABB;
            }
            case SOUTH: {
                return TripWireHookBlock.SOUTH_AABB;
            }
            case NORTH: {
                return TripWireHookBlock.NORTH_AABB;
            }
        }
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)TripWireHookBlock.FACING);
        final BlockPos fx2 = fx.relative(gc5.getOpposite());
        final BlockState cee2 = brw.getBlockState(fx2);
        return gc5.getAxis().isHorizontal() && cee2.isFaceSturdy(brw, fx2, gc5);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)TripWireHookBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)TripWireHookBlock.POWERED, false)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, false);
        final LevelReader brw4 = bnv.getLevel();
        final BlockPos fx5 = bnv.getClickedPos();
        final Direction[] nearestLookingDirections;
        final Direction[] arr6 = nearestLookingDirections = bnv.getNearestLookingDirections();
        for (final Direction gc10 : nearestLookingDirections) {
            if (gc10.getAxis().isHorizontal()) {
                final Direction gc11 = gc10.getOpposite();
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)TripWireHookBlock.FACING, gc11);
                if (cee3.canSurvive(brw4, fx5)) {
                    return cee3;
                }
            }
        }
        return null;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        this.calculateState(bru, fx, cee, false, false, -1, null);
    }
    
    public void calculateState(final Level bru, final BlockPos fx, final BlockState cee3, final boolean boolean4, final boolean boolean5, final int integer, @Nullable final BlockState cee7) {
        final Direction gc9 = cee3.<Direction>getValue((Property<Direction>)TripWireHookBlock.FACING);
        final boolean boolean6 = cee3.<Boolean>getValue((Property<Boolean>)TripWireHookBlock.ATTACHED);
        final boolean boolean7 = cee3.<Boolean>getValue((Property<Boolean>)TripWireHookBlock.POWERED);
        boolean boolean8 = !boolean4;
        boolean boolean9 = false;
        int integer2 = 0;
        final BlockState[] arr15 = new BlockState[42];
        int integer3 = 1;
        while (integer3 < 42) {
            final BlockPos fx2 = fx.relative(gc9, integer3);
            BlockState cee8 = bru.getBlockState(fx2);
            if (cee8.is(Blocks.TRIPWIRE_HOOK)) {
                if (cee8.<Comparable>getValue((Property<Comparable>)TripWireHookBlock.FACING) == gc9.getOpposite()) {
                    integer2 = integer3;
                    break;
                }
                break;
            }
            else {
                if (cee8.is(Blocks.TRIPWIRE) || integer3 == integer) {
                    if (integer3 == integer) {
                        cee8 = (BlockState)MoreObjects.firstNonNull(cee7, cee8);
                    }
                    final boolean boolean10 = !cee8.<Boolean>getValue((Property<Boolean>)TripWireBlock.DISARMED);
                    final boolean boolean11 = cee8.<Boolean>getValue((Property<Boolean>)TripWireBlock.POWERED);
                    boolean9 |= (boolean10 && boolean11);
                    arr15[integer3] = cee8;
                    if (integer3 == integer) {
                        bru.getBlockTicks().scheduleTick(fx, this, 10);
                        boolean8 &= boolean10;
                    }
                }
                else {
                    arr15[integer3] = null;
                    boolean8 = false;
                }
                ++integer3;
            }
        }
        boolean8 &= (integer2 > 1);
        boolean9 &= boolean8;
        final BlockState cee9 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, boolean8)).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.POWERED, boolean9);
        if (integer2 > 0) {
            final BlockPos fx2 = fx.relative(gc9, integer2);
            final Direction gc10 = gc9.getOpposite();
            bru.setBlock(fx2, ((StateHolder<O, BlockState>)cee9).<Comparable, Direction>setValue((Property<Comparable>)TripWireHookBlock.FACING, gc10), 3);
            this.notifyNeighbors(bru, fx2, gc10);
            this.playSound(bru, fx2, boolean8, boolean9, boolean6, boolean7);
        }
        this.playSound(bru, fx, boolean8, boolean9, boolean6, boolean7);
        if (!boolean4) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee9).<Comparable, Direction>setValue((Property<Comparable>)TripWireHookBlock.FACING, gc9), 3);
            if (boolean5) {
                this.notifyNeighbors(bru, fx, gc9);
            }
        }
        if (boolean6 != boolean8) {
            for (int integer4 = 1; integer4 < integer2; ++integer4) {
                final BlockPos fx3 = fx.relative(gc9, integer4);
                final BlockState cee10 = arr15[integer4];
                if (cee10 != null) {
                    bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee10).<Comparable, Boolean>setValue((Property<Comparable>)TripWireHookBlock.ATTACHED, boolean8), 3);
                    if (!bru.getBlockState(fx3).isAir()) {}
                }
            }
        }
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        this.calculateState(aag, fx, cee, false, true, -1, null);
    }
    
    private void playSound(final Level bru, final BlockPos fx, final boolean boolean3, final boolean boolean4, final boolean boolean5, final boolean boolean6) {
        if (boolean4 && !boolean6) {
            bru.playSound(null, fx, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.4f, 0.6f);
        }
        else if (!boolean4 && boolean6) {
            bru.playSound(null, fx, SoundEvents.TRIPWIRE_CLICK_OFF, SoundSource.BLOCKS, 0.4f, 0.5f);
        }
        else if (boolean3 && !boolean5) {
            bru.playSound(null, fx, SoundEvents.TRIPWIRE_ATTACH, SoundSource.BLOCKS, 0.4f, 0.7f);
        }
        else if (!boolean3 && boolean5) {
            bru.playSound(null, fx, SoundEvents.TRIPWIRE_DETACH, SoundSource.BLOCKS, 0.4f, 1.2f / (bru.random.nextFloat() * 0.2f + 0.9f));
        }
    }
    
    private void notifyNeighbors(final Level bru, final BlockPos fx, final Direction gc) {
        bru.updateNeighborsAt(fx, this);
        bru.updateNeighborsAt(fx.relative(gc.getOpposite()), this);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        final boolean boolean6 = cee1.<Boolean>getValue((Property<Boolean>)TripWireHookBlock.ATTACHED);
        final boolean boolean7 = cee1.<Boolean>getValue((Property<Boolean>)TripWireHookBlock.POWERED);
        if (boolean6 || boolean7) {
            this.calculateState(bru, fx, cee1, true, false, -1, null);
        }
        if (boolean7) {
            bru.updateNeighborsAt(fx, this);
            bru.updateNeighborsAt(fx.relative(cee1.<Direction>getValue((Property<Direction>)TripWireHookBlock.FACING).getOpposite()), this);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Boolean>getValue((Property<Boolean>)TripWireHookBlock.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (!cee.<Boolean>getValue((Property<Boolean>)TripWireHookBlock.POWERED)) {
            return 0;
        }
        if (cee.<Comparable>getValue((Property<Comparable>)TripWireHookBlock.FACING) == gc) {
            return 15;
        }
        return 0;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)TripWireHookBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)TripWireHookBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)TripWireHookBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(TripWireHookBlock.FACING, TripWireHookBlock.POWERED, TripWireHookBlock.ATTACHED);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        POWERED = BlockStateProperties.POWERED;
        ATTACHED = BlockStateProperties.ATTACHED;
        NORTH_AABB = Block.box(5.0, 0.0, 10.0, 11.0, 10.0, 16.0);
        SOUTH_AABB = Block.box(5.0, 0.0, 0.0, 11.0, 10.0, 6.0);
        WEST_AABB = Block.box(10.0, 0.0, 5.0, 16.0, 10.0, 11.0);
        EAST_AABB = Block.box(0.0, 0.0, 5.0, 6.0, 10.0, 11.0);
    }
}
