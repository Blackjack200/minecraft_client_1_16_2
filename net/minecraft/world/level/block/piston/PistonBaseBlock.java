package net.minecraft.world.level.block.piston;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.DirectionalBlock;

public class PistonBaseBlock extends DirectionalBlock {
    public static final BooleanProperty EXTENDED;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape UP_AABB;
    protected static final VoxelShape DOWN_AABB;
    private final boolean isSticky;
    
    public PistonBaseBlock(final boolean boolean1, final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)PistonBaseBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)PistonBaseBlock.EXTENDED, false));
        this.isSticky = boolean1;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (!cee.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED)) {
            return Shapes.block();
        }
        switch (cee.<Direction>getValue((Property<Direction>)PistonBaseBlock.FACING)) {
            case DOWN: {
                return PistonBaseBlock.DOWN_AABB;
            }
            default: {
                return PistonBaseBlock.UP_AABB;
            }
            case NORTH: {
                return PistonBaseBlock.NORTH_AABB;
            }
            case SOUTH: {
                return PistonBaseBlock.SOUTH_AABB;
            }
            case WEST: {
                return PistonBaseBlock.WEST_AABB;
            }
            case EAST: {
                return PistonBaseBlock.EAST_AABB;
            }
        }
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (!bru.isClientSide) {
            this.checkIfExtend(bru, fx, cee);
        }
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (!bru.isClientSide) {
            this.checkIfExtend(bru, fx3, cee);
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        if (!bru.isClientSide && bru.getBlockEntity(fx) == null) {
            this.checkIfExtend(bru, fx, cee1);
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)PistonBaseBlock.FACING, bnv.getNearestLookingDirection().getOpposite())).<Comparable, Boolean>setValue((Property<Comparable>)PistonBaseBlock.EXTENDED, false);
    }
    
    private void checkIfExtend(final Level bru, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)PistonBaseBlock.FACING);
        final boolean boolean6 = this.getNeighborSignal(bru, fx, gc5);
        if (boolean6 && !cee.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED)) {
            if (new PistonStructureResolver(bru, fx, gc5, true).resolve()) {
                bru.blockEvent(fx, this, 0, gc5.get3DDataValue());
            }
        }
        else if (!boolean6 && cee.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED)) {
            final BlockPos fx2 = fx.relative(gc5, 2);
            final BlockState cee2 = bru.getBlockState(fx2);
            int integer9 = 1;
            if (cee2.is(Blocks.MOVING_PISTON) && cee2.<Comparable>getValue((Property<Comparable>)PistonBaseBlock.FACING) == gc5) {
                final BlockEntity ccg10 = bru.getBlockEntity(fx2);
                if (ccg10 instanceof PistonMovingBlockEntity) {
                    final PistonMovingBlockEntity cea11 = (PistonMovingBlockEntity)ccg10;
                    if (cea11.isExtending() && (cea11.getProgress(0.0f) < 0.5f || bru.getGameTime() == cea11.getLastTicked() || ((ServerLevel)bru).isHandlingTick())) {
                        integer9 = 2;
                    }
                }
            }
            bru.blockEvent(fx, this, integer9, gc5.get3DDataValue());
        }
    }
    
    private boolean getNeighborSignal(final Level bru, final BlockPos fx, final Direction gc) {
        for (final Direction gc2 : Direction.values()) {
            if (gc2 != gc && bru.hasSignal(fx.relative(gc2), gc2)) {
                return true;
            }
        }
        if (bru.hasSignal(fx, Direction.DOWN)) {
            return true;
        }
        final BlockPos fx2 = fx.above();
        for (final Direction gc3 : Direction.values()) {
            if (gc3 != Direction.DOWN && bru.hasSignal(fx2.relative(gc3), gc3)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean triggerEvent(final BlockState cee, final Level bru, final BlockPos fx, final int integer4, final int integer5) {
        final Direction gc7 = cee.<Direction>getValue((Property<Direction>)PistonBaseBlock.FACING);
        if (!bru.isClientSide) {
            final boolean boolean8 = this.getNeighborSignal(bru, fx, gc7);
            if (boolean8 && (integer4 == 1 || integer4 == 2)) {
                bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)PistonBaseBlock.EXTENDED, true), 2);
                return false;
            }
            if (!boolean8 && integer4 == 0) {
                return false;
            }
        }
        if (integer4 == 0) {
            if (!this.moveBlocks(bru, fx, gc7, true)) {
                return false;
            }
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)PistonBaseBlock.EXTENDED, true), 67);
            bru.playSound(null, fx, SoundEvents.PISTON_EXTEND, SoundSource.BLOCKS, 0.5f, bru.random.nextFloat() * 0.25f + 0.6f);
        }
        else if (integer4 == 1 || integer4 == 2) {
            final BlockEntity ccg8 = bru.getBlockEntity(fx.relative(gc7));
            if (ccg8 instanceof PistonMovingBlockEntity) {
                ((PistonMovingBlockEntity)ccg8).finalTick();
            }
            final BlockState cee2 = (((StateHolder<O, BlockState>)Blocks.MOVING_PISTON.defaultBlockState()).setValue((Property<Comparable>)MovingPistonBlock.FACING, gc7)).<Comparable, PistonType>setValue((Property<Comparable>)MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
            bru.setBlock(fx, cee2, 20);
            bru.setBlockEntity(fx, MovingPistonBlock.newMovingBlockEntity(((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)PistonBaseBlock.FACING, Direction.from3DDataValue(integer5 & 0x7)), gc7, false, true));
            bru.blockUpdated(fx, cee2.getBlock());
            cee2.updateNeighbourShapes(bru, fx, 2);
            if (this.isSticky) {
                final BlockPos fx2 = fx.offset(gc7.getStepX() * 2, gc7.getStepY() * 2, gc7.getStepZ() * 2);
                final BlockState cee3 = bru.getBlockState(fx2);
                boolean boolean9 = false;
                if (cee3.is(Blocks.MOVING_PISTON)) {
                    final BlockEntity ccg9 = bru.getBlockEntity(fx2);
                    if (ccg9 instanceof PistonMovingBlockEntity) {
                        final PistonMovingBlockEntity cea14 = (PistonMovingBlockEntity)ccg9;
                        if (cea14.getDirection() == gc7 && cea14.isExtending()) {
                            cea14.finalTick();
                            boolean9 = true;
                        }
                    }
                }
                if (!boolean9) {
                    if (integer4 == 1 && !cee3.isAir() && isPushable(cee3, bru, fx2, gc7.getOpposite(), false, gc7) && (cee3.getPistonPushReaction() == PushReaction.NORMAL || cee3.is(Blocks.PISTON) || cee3.is(Blocks.STICKY_PISTON))) {
                        this.moveBlocks(bru, fx, gc7, false);
                    }
                    else {
                        bru.removeBlock(fx.relative(gc7), false);
                    }
                }
            }
            else {
                bru.removeBlock(fx.relative(gc7), false);
            }
            bru.playSound(null, fx, SoundEvents.PISTON_CONTRACT, SoundSource.BLOCKS, 0.5f, bru.random.nextFloat() * 0.15f + 0.6f);
        }
        return true;
    }
    
    public static boolean isPushable(final BlockState cee, final Level bru, final BlockPos fx, final Direction gc4, final boolean boolean5, final Direction gc6) {
        if (fx.getY() < 0 || fx.getY() > bru.getMaxBuildHeight() - 1 || !bru.getWorldBorder().isWithinBounds(fx)) {
            return false;
        }
        if (cee.isAir()) {
            return true;
        }
        if (cee.is(Blocks.OBSIDIAN) || cee.is(Blocks.CRYING_OBSIDIAN) || cee.is(Blocks.RESPAWN_ANCHOR)) {
            return false;
        }
        if (gc4 == Direction.DOWN && fx.getY() == 0) {
            return false;
        }
        if (gc4 == Direction.UP && fx.getY() == bru.getMaxBuildHeight() - 1) {
            return false;
        }
        if (cee.is(Blocks.PISTON) || cee.is(Blocks.STICKY_PISTON)) {
            if (cee.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED)) {
                return false;
            }
        }
        else {
            if (cee.getDestroySpeed(bru, fx) == -1.0f) {
                return false;
            }
            switch (cee.getPistonPushReaction()) {
                case BLOCK: {
                    return false;
                }
                case DESTROY: {
                    return boolean5;
                }
                case PUSH_ONLY: {
                    return gc4 == gc6;
                }
            }
        }
        return !cee.getBlock().isEntityBlock();
    }
    
    private boolean moveBlocks(final Level bru, final BlockPos fx, final Direction gc, final boolean boolean4) {
        final BlockPos fx2 = fx.relative(gc);
        if (!boolean4 && bru.getBlockState(fx2).is(Blocks.PISTON_HEAD)) {
            bru.setBlock(fx2, Blocks.AIR.defaultBlockState(), 20);
        }
        final PistonStructureResolver ceb7 = new PistonStructureResolver(bru, fx, gc, boolean4);
        if (!ceb7.resolve()) {
            return false;
        }
        final Map<BlockPos, BlockState> map8 = (Map<BlockPos, BlockState>)Maps.newHashMap();
        final List<BlockPos> list9 = ceb7.getToPush();
        final List<BlockState> list10 = (List<BlockState>)Lists.newArrayList();
        for (int integer11 = 0; integer11 < list9.size(); ++integer11) {
            final BlockPos fx3 = (BlockPos)list9.get(integer11);
            final BlockState cee13 = bru.getBlockState(fx3);
            list10.add(cee13);
            map8.put(fx3, cee13);
        }
        final List<BlockPos> list11 = ceb7.getToDestroy();
        final BlockState[] arr12 = new BlockState[list9.size() + list11.size()];
        final Direction gc2 = boolean4 ? gc : gc.getOpposite();
        int integer12 = 0;
        for (int integer13 = list11.size() - 1; integer13 >= 0; --integer13) {
            final BlockPos fx4 = (BlockPos)list11.get(integer13);
            final BlockState cee14 = bru.getBlockState(fx4);
            final BlockEntity ccg18 = cee14.getBlock().isEntityBlock() ? bru.getBlockEntity(fx4) : null;
            Block.dropResources(cee14, bru, fx4, ccg18);
            bru.setBlock(fx4, Blocks.AIR.defaultBlockState(), 18);
            arr12[integer12++] = cee14;
        }
        for (int integer13 = list9.size() - 1; integer13 >= 0; --integer13) {
            BlockPos fx4 = (BlockPos)list9.get(integer13);
            final BlockState cee14 = bru.getBlockState(fx4);
            fx4 = fx4.relative(gc2);
            map8.remove(fx4);
            bru.setBlock(fx4, ((StateHolder<O, BlockState>)Blocks.MOVING_PISTON.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)PistonBaseBlock.FACING, gc), 68);
            bru.setBlockEntity(fx4, MovingPistonBlock.newMovingBlockEntity((BlockState)list10.get(integer13), gc, boolean4, false));
            arr12[integer12++] = cee14;
        }
        if (boolean4) {
            final PistonType cff15 = this.isSticky ? PistonType.STICKY : PistonType.DEFAULT;
            final BlockState cee15 = (((StateHolder<O, BlockState>)Blocks.PISTON_HEAD.defaultBlockState()).setValue((Property<Comparable>)PistonHeadBlock.FACING, gc)).<PistonType, PistonType>setValue(PistonHeadBlock.TYPE, cff15);
            final BlockState cee14 = (((StateHolder<O, BlockState>)Blocks.MOVING_PISTON.defaultBlockState()).setValue((Property<Comparable>)MovingPistonBlock.FACING, gc)).<Comparable, PistonType>setValue((Property<Comparable>)MovingPistonBlock.TYPE, this.isSticky ? PistonType.STICKY : PistonType.DEFAULT);
            map8.remove(fx2);
            bru.setBlock(fx2, cee14, 68);
            bru.setBlockEntity(fx2, MovingPistonBlock.newMovingBlockEntity(cee15, gc, true, true));
        }
        final BlockState cee16 = Blocks.AIR.defaultBlockState();
        for (final BlockPos fx5 : map8.keySet()) {
            bru.setBlock(fx5, cee16, 82);
        }
        for (final Map.Entry<BlockPos, BlockState> entry17 : map8.entrySet()) {
            final BlockPos fx6 = (BlockPos)entry17.getKey();
            final BlockState cee17 = (BlockState)entry17.getValue();
            cee17.updateIndirectNeighbourShapes(bru, fx6, 2);
            cee16.updateNeighbourShapes(bru, fx6, 2);
            cee16.updateIndirectNeighbourShapes(bru, fx6, 2);
        }
        integer12 = 0;
        for (int integer14 = list11.size() - 1; integer14 >= 0; --integer14) {
            final BlockState cee14 = arr12[integer12++];
            final BlockPos fx6 = (BlockPos)list11.get(integer14);
            cee14.updateIndirectNeighbourShapes(bru, fx6, 2);
            bru.updateNeighborsAt(fx6, cee14.getBlock());
        }
        for (int integer14 = list9.size() - 1; integer14 >= 0; --integer14) {
            bru.updateNeighborsAt((BlockPos)list9.get(integer14), arr12[integer12++].getBlock());
        }
        if (boolean4) {
            bru.updateNeighborsAt(fx2, Blocks.PISTON_HEAD);
        }
        return true;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)PistonBaseBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)PistonBaseBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)PistonBaseBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(PistonBaseBlock.FACING, PistonBaseBlock.EXTENDED);
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return cee.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        EXTENDED = BlockStateProperties.EXTENDED;
        EAST_AABB = Block.box(0.0, 0.0, 0.0, 12.0, 16.0, 16.0);
        WEST_AABB = Block.box(4.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 12.0);
        NORTH_AABB = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 16.0);
        UP_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
        DOWN_AABB = Block.box(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);
    }
}
