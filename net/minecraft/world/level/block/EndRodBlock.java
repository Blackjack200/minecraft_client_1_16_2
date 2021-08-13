package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EndRodBlock extends DirectionalBlock {
    protected static final VoxelShape Y_AXIS_AABB;
    protected static final VoxelShape Z_AXIS_AABB;
    protected static final VoxelShape X_AXIS_AABB;
    
    protected EndRodBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)EndRodBlock.FACING, Direction.UP));
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)EndRodBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)EndRodBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)EndRodBlock.FACING, byd.mirror(cee.<Direction>getValue((Property<Direction>)EndRodBlock.FACING)));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction>getValue((Property<Direction>)EndRodBlock.FACING).getAxis()) {
            default: {
                return EndRodBlock.X_AXIS_AABB;
            }
            case Z: {
                return EndRodBlock.Z_AXIS_AABB;
            }
            case Y: {
                return EndRodBlock.Y_AXIS_AABB;
            }
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Direction gc3 = bnv.getClickedFace();
        final BlockState cee4 = bnv.getLevel().getBlockState(bnv.getClickedPos().relative(gc3.getOpposite()));
        if (cee4.is(this) && cee4.<Comparable>getValue((Property<Comparable>)EndRodBlock.FACING) == gc3) {
            return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)EndRodBlock.FACING, gc3.getOpposite());
        }
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)EndRodBlock.FACING, gc3);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)EndRodBlock.FACING);
        final double double7 = fx.getX() + 0.55 - random.nextFloat() * 0.1f;
        final double double8 = fx.getY() + 0.55 - random.nextFloat() * 0.1f;
        final double double9 = fx.getZ() + 0.55 - random.nextFloat() * 0.1f;
        final double double10 = 0.4f - (random.nextFloat() + random.nextFloat()) * 0.4f;
        if (random.nextInt(5) == 0) {
            bru.addParticle(ParticleTypes.END_ROD, double7 + gc6.getStepX() * double10, double8 + gc6.getStepY() * double10, double9 + gc6.getStepZ() * double10, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005, random.nextGaussian() * 0.005);
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(EndRodBlock.FACING);
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.NORMAL;
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        Y_AXIS_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
        Z_AXIS_AABB = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 16.0);
        X_AXIS_AABB = Block.box(0.0, 6.0, 6.0, 16.0, 10.0, 10.0);
    }
}
