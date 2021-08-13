package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Random;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class LeverBlock extends FaceAttachedHorizontalDirectionalBlock {
    public static final BooleanProperty POWERED;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape UP_AABB_Z;
    protected static final VoxelShape UP_AABB_X;
    protected static final VoxelShape DOWN_AABB_Z;
    protected static final VoxelShape DOWN_AABB_X;
    
    protected LeverBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)LeverBlock.FACING, Direction.NORTH)).setValue((Property<Comparable>)LeverBlock.POWERED, false)).<AttachFace, AttachFace>setValue(LeverBlock.FACE, AttachFace.WALL));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<AttachFace>getValue(LeverBlock.FACE)) {
            case FLOOR: {
                switch (cee.<Direction>getValue((Property<Direction>)LeverBlock.FACING).getAxis()) {
                    case X: {
                        return LeverBlock.UP_AABB_X;
                    }
                    default: {
                        return LeverBlock.UP_AABB_Z;
                    }
                }
                break;
            }
            case WALL: {
                switch (cee.<Direction>getValue((Property<Direction>)LeverBlock.FACING)) {
                    case EAST: {
                        return LeverBlock.EAST_AABB;
                    }
                    case WEST: {
                        return LeverBlock.WEST_AABB;
                    }
                    case SOUTH: {
                        return LeverBlock.SOUTH_AABB;
                    }
                    default: {
                        return LeverBlock.NORTH_AABB;
                    }
                }
                break;
            }
            default: {
                switch (cee.<Direction>getValue((Property<Direction>)LeverBlock.FACING).getAxis()) {
                    case X: {
                        return LeverBlock.DOWN_AABB_X;
                    }
                    default: {
                        return LeverBlock.DOWN_AABB_Z;
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)LeverBlock.POWERED);
            if (cee2.<Boolean>getValue((Property<Boolean>)LeverBlock.POWERED)) {
                makeParticle(cee2, bru, fx, 1.0f);
            }
            return InteractionResult.SUCCESS;
        }
        final BlockState cee2 = this.pull(cee, bru, fx);
        final float float9 = cee2.<Boolean>getValue((Property<Boolean>)LeverBlock.POWERED) ? 0.6f : 0.5f;
        bru.playSound(null, fx, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3f, float9);
        return InteractionResult.CONSUME;
    }
    
    public BlockState pull(BlockState cee, final Level bru, final BlockPos fx) {
        cee = ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)LeverBlock.POWERED);
        bru.setBlock(fx, cee, 3);
        this.updateNeighbours(cee, bru, fx);
        return cee;
    }
    
    private static void makeParticle(final BlockState cee, final LevelAccessor brv, final BlockPos fx, final float float4) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)LeverBlock.FACING).getOpposite();
        final Direction gc6 = FaceAttachedHorizontalDirectionalBlock.getConnectedDirection(cee).getOpposite();
        final double double7 = fx.getX() + 0.5 + 0.1 * gc5.getStepX() + 0.2 * gc6.getStepX();
        final double double8 = fx.getY() + 0.5 + 0.1 * gc5.getStepY() + 0.2 * gc6.getStepY();
        final double double9 = fx.getZ() + 0.5 + 0.1 * gc5.getStepZ() + 0.2 * gc6.getStepZ();
        brv.addParticle(new DustParticleOptions(1.0f, 0.0f, 0.0f, float4), double7, double8, double9, 0.0, 0.0, 0.0);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (cee.<Boolean>getValue((Property<Boolean>)LeverBlock.POWERED) && random.nextFloat() < 0.25f) {
            makeParticle(cee, bru, fx, 0.5f);
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)LeverBlock.POWERED)) {
            this.updateNeighbours(cee1, bru, fx);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return cee.<Boolean>getValue((Property<Boolean>)LeverBlock.POWERED) ? 15 : 0;
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (cee.<Boolean>getValue((Property<Boolean>)LeverBlock.POWERED) && FaceAttachedHorizontalDirectionalBlock.getConnectedDirection(cee) == gc) {
            return 15;
        }
        return 0;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    private void updateNeighbours(final BlockState cee, final Level bru, final BlockPos fx) {
        bru.updateNeighborsAt(fx, this);
        bru.updateNeighborsAt(fx.relative(FaceAttachedHorizontalDirectionalBlock.getConnectedDirection(cee).getOpposite()), this);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(LeverBlock.FACE, LeverBlock.FACING, LeverBlock.POWERED);
    }
    
    static {
        POWERED = BlockStateProperties.POWERED;
        NORTH_AABB = Block.box(5.0, 4.0, 10.0, 11.0, 12.0, 16.0);
        SOUTH_AABB = Block.box(5.0, 4.0, 0.0, 11.0, 12.0, 6.0);
        WEST_AABB = Block.box(10.0, 4.0, 5.0, 16.0, 12.0, 11.0);
        EAST_AABB = Block.box(0.0, 4.0, 5.0, 6.0, 12.0, 11.0);
        UP_AABB_Z = Block.box(5.0, 0.0, 4.0, 11.0, 6.0, 12.0);
        UP_AABB_X = Block.box(4.0, 0.0, 5.0, 12.0, 6.0, 11.0);
        DOWN_AABB_Z = Block.box(5.0, 10.0, 4.0, 11.0, 16.0, 12.0);
        DOWN_AABB_X = Block.box(4.0, 10.0, 5.0, 12.0, 16.0, 11.0);
    }
}
