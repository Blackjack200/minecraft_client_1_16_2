package net.minecraft.world.level.block;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrassPathBlock extends Block {
    protected static final VoxelShape SHAPE;
    
    protected GrassPathBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        if (!this.defaultBlockState().canSurvive(bnv.getLevel(), bnv.getClickedPos())) {
            return Block.pushEntitiesUp(this.defaultBlockState(), Blocks.DIRT.defaultBlockState(), bnv.getLevel(), bnv.getClickedPos());
        }
        return super.getStateForPlacement(bnv);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.UP && !cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        FarmBlock.turnToDirt(cee, aag, fx);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.above());
        return !cee2.getMaterial().isSolid() || cee2.getBlock() instanceof FenceGateBlock;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return GrassPathBlock.SHAPE;
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        SHAPE = FarmBlock.SHAPE;
    }
}
