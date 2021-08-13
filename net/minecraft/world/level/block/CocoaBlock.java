package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CocoaBlock extends HorizontalDirectionalBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    protected static final VoxelShape[] EAST_AABB;
    protected static final VoxelShape[] WEST_AABB;
    protected static final VoxelShape[] NORTH_AABB;
    protected static final VoxelShape[] SOUTH_AABB;
    
    public CocoaBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)CocoaBlock.FACING, Direction.NORTH)).<Comparable, Integer>setValue((Property<Comparable>)CocoaBlock.AGE, 0));
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)CocoaBlock.AGE) < 2;
    }
    
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.random.nextInt(5) == 0) {
            final int integer6 = cee.<Integer>getValue((Property<Integer>)CocoaBlock.AGE);
            if (integer6 < 2) {
                aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)CocoaBlock.AGE, integer6 + 1), 2);
            }
        }
    }
    
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final Block bul5 = brw.getBlockState(fx.relative(cee.<Direction>getValue((Property<Direction>)CocoaBlock.FACING))).getBlock();
        return bul5.is(BlockTags.JUNGLE_LOGS);
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final int integer6 = cee.<Integer>getValue((Property<Integer>)CocoaBlock.AGE);
        switch (cee.<Direction>getValue((Property<Direction>)CocoaBlock.FACING)) {
            case SOUTH: {
                return CocoaBlock.SOUTH_AABB[integer6];
            }
            default: {
                return CocoaBlock.NORTH_AABB[integer6];
            }
            case WEST: {
                return CocoaBlock.WEST_AABB[integer6];
            }
            case EAST: {
                return CocoaBlock.EAST_AABB[integer6];
            }
        }
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = this.defaultBlockState();
        final LevelReader brw4 = bnv.getLevel();
        final BlockPos fx5 = bnv.getClickedPos();
        for (final Direction gc9 : bnv.getNearestLookingDirections()) {
            if (gc9.getAxis().isHorizontal()) {
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)CocoaBlock.FACING, gc9);
                if (cee3.canSurvive(brw4, fx5)) {
                    return cee3;
                }
            }
        }
        return null;
    }
    
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == cee1.<Comparable>getValue((Property<Comparable>)CocoaBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return cee.<Integer>getValue((Property<Integer>)CocoaBlock.AGE) < 2;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)CocoaBlock.AGE, cee.<Integer>getValue((Property<Integer>)CocoaBlock.AGE) + 1), 2);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CocoaBlock.FACING, CocoaBlock.AGE);
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        AGE = BlockStateProperties.AGE_2;
        EAST_AABB = new VoxelShape[] { Block.box(11.0, 7.0, 6.0, 15.0, 12.0, 10.0), Block.box(9.0, 5.0, 5.0, 15.0, 12.0, 11.0), Block.box(7.0, 3.0, 4.0, 15.0, 12.0, 12.0) };
        WEST_AABB = new VoxelShape[] { Block.box(1.0, 7.0, 6.0, 5.0, 12.0, 10.0), Block.box(1.0, 5.0, 5.0, 7.0, 12.0, 11.0), Block.box(1.0, 3.0, 4.0, 9.0, 12.0, 12.0) };
        NORTH_AABB = new VoxelShape[] { Block.box(6.0, 7.0, 1.0, 10.0, 12.0, 5.0), Block.box(5.0, 5.0, 1.0, 11.0, 12.0, 7.0), Block.box(4.0, 3.0, 1.0, 12.0, 12.0, 9.0) };
        SOUTH_AABB = new VoxelShape[] { Block.box(6.0, 7.0, 11.0, 10.0, 12.0, 15.0), Block.box(5.0, 5.0, 9.0, 11.0, 12.0, 15.0), Block.box(4.0, 3.0, 7.0, 12.0, 12.0, 15.0) };
    }
}
