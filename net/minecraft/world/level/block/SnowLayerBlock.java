package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SnowLayerBlock extends Block {
    public static final IntegerProperty LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER;
    
    protected SnowLayerBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)SnowLayerBlock.LAYERS, 1));
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        switch (cxb) {
            case LAND: {
                return cee.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS) < 5;
            }
            case WATER: {
                return false;
            }
            case AIR: {
                return false;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SnowLayerBlock.SHAPE_BY_LAYER[cee.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS)];
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SnowLayerBlock.SHAPE_BY_LAYER[cee.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS) - 1];
    }
    
    @Override
    public VoxelShape getBlockSupportShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return SnowLayerBlock.SHAPE_BY_LAYER[cee.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS)];
    }
    
    @Override
    public VoxelShape getVisualShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SnowLayerBlock.SHAPE_BY_LAYER[cee.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS)];
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.below());
        return !cee2.is(Blocks.ICE) && !cee2.is(Blocks.PACKED_ICE) && !cee2.is(Blocks.BARRIER) && (cee2.is(Blocks.HONEY_BLOCK) || cee2.is(Blocks.SOUL_SAND) || Block.isFaceFull(cee2.getCollisionShape(brw, fx.below()), Direction.UP) || (cee2.getBlock() == this && cee2.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS) == 8));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.getBrightness(LightLayer.BLOCK, fx) > 11) {
            Block.dropResources(cee, aag, fx);
            aag.removeBlock(fx, false);
        }
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final BlockPlaceContext bnv) {
        final int integer4 = cee.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS);
        if (bnv.getItemInHand().getItem() == this.asItem() && integer4 < 8) {
            return !bnv.replacingClickedOnBlock() || bnv.getClickedFace() == Direction.UP;
        }
        return integer4 == 1;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = bnv.getLevel().getBlockState(bnv.getClickedPos());
        if (cee3.is(this)) {
            final int integer4 = cee3.<Integer>getValue((Property<Integer>)SnowLayerBlock.LAYERS);
            return ((StateHolder<O, BlockState>)cee3).<Comparable, Integer>setValue((Property<Comparable>)SnowLayerBlock.LAYERS, Math.min(8, integer4 + 1));
        }
        return super.getStateForPlacement(bnv);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SnowLayerBlock.LAYERS);
    }
    
    static {
        LAYERS = BlockStateProperties.LAYERS;
        SHAPE_BY_LAYER = new VoxelShape[] { Shapes.empty(), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) };
    }
}
