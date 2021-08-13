package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class WallSignBlock extends SignBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> AABBS;
    
    public WallSignBlock(final Properties c, final WoodType cfn) {
        super(c, cfn);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)WallSignBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)WallSignBlock.WATERLOGGED, false));
    }
    
    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)WallSignBlock.AABBS.get(cee.getValue((Property<Object>)WallSignBlock.FACING));
    }
    
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return brw.getBlockState(fx.relative(cee.<Direction>getValue((Property<Direction>)WallSignBlock.FACING).getOpposite())).getMaterial().isSolid();
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = this.defaultBlockState();
        final FluidState cuu4 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final LevelReader brw5 = bnv.getLevel();
        final BlockPos fx6 = bnv.getClickedPos();
        final Direction[] nearestLookingDirections;
        final Direction[] arr7 = nearestLookingDirections = bnv.getNearestLookingDirections();
        for (final Direction gc11 : nearestLookingDirections) {
            if (gc11.getAxis().isHorizontal()) {
                final Direction gc12 = gc11.getOpposite();
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)WallSignBlock.FACING, gc12);
                if (cee3.canSurvive(brw5, fx6)) {
                    return ((StateHolder<O, BlockState>)cee3).<Comparable, Boolean>setValue((Property<Comparable>)WallSignBlock.WATERLOGGED, cuu4.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)WallSignBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)WallSignBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)WallSignBlock.FACING)));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)WallSignBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(WallSignBlock.FACING, WallSignBlock.WATERLOGGED);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, Block.box(0.0, 4.5, 14.0, 16.0, 12.5, 16.0), Direction.SOUTH, Block.box(0.0, 4.5, 0.0, 16.0, 12.5, 2.0), Direction.EAST, Block.box(0.0, 4.5, 0.0, 2.0, 12.5, 16.0), Direction.WEST, Block.box(14.0, 4.5, 0.0, 16.0, 12.5, 16.0)));
    }
}
