package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.context.BlockPlaceContext;
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

public class WallSkullBlock extends AbstractSkullBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> AABBS;
    
    protected WallSkullBlock(final SkullBlock.Type a, final Properties c) {
        super(a, c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)WallSkullBlock.FACING, Direction.NORTH));
    }
    
    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)WallSkullBlock.AABBS.get(cee.getValue((Property<Object>)WallSkullBlock.FACING));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = this.defaultBlockState();
        final BlockGetter bqz4 = bnv.getLevel();
        final BlockPos fx5 = bnv.getClickedPos();
        final Direction[] nearestLookingDirections;
        final Direction[] arr6 = nearestLookingDirections = bnv.getNearestLookingDirections();
        for (final Direction gc10 : nearestLookingDirections) {
            if (gc10.getAxis().isHorizontal()) {
                final Direction gc11 = gc10.getOpposite();
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)WallSkullBlock.FACING, gc11);
                if (!bqz4.getBlockState(fx5.relative(gc10)).canBeReplaced(bnv)) {
                    return cee3;
                }
            }
        }
        return null;
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)WallSkullBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)WallSkullBlock.FACING)));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)WallSkullBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(WallSkullBlock.FACING);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, Block.box(4.0, 4.0, 8.0, 12.0, 12.0, 16.0), Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 8.0), Direction.EAST, Block.box(0.0, 4.0, 4.0, 8.0, 12.0, 12.0), Direction.WEST, Block.box(8.0, 4.0, 4.0, 16.0, 12.0, 12.0)));
    }
}
