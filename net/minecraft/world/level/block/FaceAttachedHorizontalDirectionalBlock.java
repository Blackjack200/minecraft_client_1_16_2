package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class FaceAttachedHorizontalDirectionalBlock extends HorizontalDirectionalBlock {
    public static final EnumProperty<AttachFace> FACE;
    
    protected FaceAttachedHorizontalDirectionalBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return canAttach(brw, fx, getConnectedDirection(cee).getOpposite());
    }
    
    public static boolean canAttach(final LevelReader brw, final BlockPos fx, final Direction gc) {
        final BlockPos fx2 = fx.relative(gc);
        return brw.getBlockState(fx2).isFaceSturdy(brw, fx2, gc.getOpposite());
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        for (final Direction gc6 : bnv.getNearestLookingDirections()) {
            BlockState cee7;
            if (gc6.getAxis() == Direction.Axis.Y) {
                cee7 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)FaceAttachedHorizontalDirectionalBlock.FACE, (gc6 == Direction.UP) ? AttachFace.CEILING : AttachFace.FLOOR)).<Comparable, Direction>setValue((Property<Comparable>)FaceAttachedHorizontalDirectionalBlock.FACING, bnv.getHorizontalDirection());
            }
            else {
                cee7 = (((StateHolder<O, BlockState>)this.defaultBlockState()).setValue(FaceAttachedHorizontalDirectionalBlock.FACE, AttachFace.WALL)).<Comparable, Direction>setValue((Property<Comparable>)FaceAttachedHorizontalDirectionalBlock.FACING, gc6.getOpposite());
            }
            if (cee7.canSurvive(bnv.getLevel(), bnv.getClickedPos())) {
                return cee7;
            }
        }
        return null;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (getConnectedDirection(cee1).getOpposite() == gc && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    protected static Direction getConnectedDirection(final BlockState cee) {
        switch (cee.<AttachFace>getValue(FaceAttachedHorizontalDirectionalBlock.FACE)) {
            case CEILING: {
                return Direction.DOWN;
            }
            case FLOOR: {
                return Direction.UP;
            }
            default: {
                return cee.<Direction>getValue((Property<Direction>)FaceAttachedHorizontalDirectionalBlock.FACING);
            }
        }
    }
    
    static {
        FACE = BlockStateProperties.ATTACH_FACE;
    }
}
