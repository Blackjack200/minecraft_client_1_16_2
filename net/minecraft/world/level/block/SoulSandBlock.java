package net.minecraft.world.level.block;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoulSandBlock extends Block {
    protected static final VoxelShape SHAPE;
    
    public SoulSandBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SoulSandBlock.SHAPE;
    }
    
    @Override
    public VoxelShape getBlockSupportShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return Shapes.block();
    }
    
    @Override
    public VoxelShape getVisualShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.block();
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        BubbleColumnBlock.growColumn(aag, fx.above(), false);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.UP && cee3.is(Blocks.WATER)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 20);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        bru.getBlockTicks().scheduleTick(fx, this, 20);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0);
    }
}
