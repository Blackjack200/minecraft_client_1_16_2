package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class BaseCoralWallFanBlock extends BaseCoralFanBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> SHAPES;
    
    protected BaseCoralWallFanBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)BaseCoralWallFanBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)BaseCoralWallFanBlock.WATERLOGGED, true));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)BaseCoralWallFanBlock.SHAPES.get(cee.getValue((Property<Object>)BaseCoralWallFanBlock.FACING));
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)BaseCoralWallFanBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)BaseCoralWallFanBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)BaseCoralWallFanBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BaseCoralWallFanBlock.FACING, BaseCoralWallFanBlock.WATERLOGGED);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)BaseCoralWallFanBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)BaseCoralWallFanBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return cee1;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)BaseCoralWallFanBlock.FACING);
        final BlockPos fx2 = fx.relative(gc5.getOpposite());
        final BlockState cee2 = brw.getBlockState(fx2);
        return cee2.isFaceSturdy(brw, fx2, gc5);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = super.getStateForPlacement(bnv);
        final LevelReader brw4 = bnv.getLevel();
        final BlockPos fx5 = bnv.getClickedPos();
        final Direction[] nearestLookingDirections;
        final Direction[] arr6 = nearestLookingDirections = bnv.getNearestLookingDirections();
        for (final Direction gc10 : nearestLookingDirections) {
            if (gc10.getAxis().isHorizontal()) {
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)BaseCoralWallFanBlock.FACING, gc10.getOpposite());
                if (cee3.canSurvive(brw4, fx5)) {
                    return cee3;
                }
            }
        }
        return null;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        SHAPES = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, Block.box(0.0, 4.0, 5.0, 16.0, 12.0, 16.0), Direction.SOUTH, Block.box(0.0, 4.0, 0.0, 16.0, 12.0, 11.0), Direction.WEST, Block.box(5.0, 4.0, 0.0, 16.0, 12.0, 16.0), Direction.EAST, Block.box(0.0, 4.0, 0.0, 11.0, 12.0, 16.0)));
    }
}
