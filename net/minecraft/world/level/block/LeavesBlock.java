package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Vec3i;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class LeavesBlock extends Block {
    public static final IntegerProperty DISTANCE;
    public static final BooleanProperty PERSISTENT;
    
    public LeavesBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)LeavesBlock.DISTANCE, 7)).<Comparable, Boolean>setValue((Property<Comparable>)LeavesBlock.PERSISTENT, false));
    }
    
    @Override
    public VoxelShape getBlockSupportShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return Shapes.empty();
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)LeavesBlock.DISTANCE) == 7 && !cee.<Boolean>getValue((Property<Boolean>)LeavesBlock.PERSISTENT);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)LeavesBlock.PERSISTENT) && cee.<Integer>getValue((Property<Integer>)LeavesBlock.DISTANCE) == 7) {
            Block.dropResources(cee, aag, fx);
            aag.removeBlock(fx, false);
        }
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        aag.setBlock(fx, updateDistance(cee, aag, fx), 3);
    }
    
    @Override
    public int getLightBlock(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return 1;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final int integer8 = getDistanceAt(cee3) + 1;
        if (integer8 != 1 || cee1.<Integer>getValue((Property<Integer>)LeavesBlock.DISTANCE) != integer8) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return cee1;
    }
    
    private static BlockState updateDistance(final BlockState cee, final LevelAccessor brv, final BlockPos fx) {
        int integer4 = 7;
        final BlockPos.MutableBlockPos a5 = new BlockPos.MutableBlockPos();
        for (final Direction gc9 : Direction.values()) {
            a5.setWithOffset(fx, gc9);
            integer4 = Math.min(integer4, getDistanceAt(brv.getBlockState(a5)) + 1);
            if (integer4 == 1) {
                break;
            }
        }
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)LeavesBlock.DISTANCE, integer4);
    }
    
    private static int getDistanceAt(final BlockState cee) {
        if (BlockTags.LOGS.contains(cee.getBlock())) {
            return 0;
        }
        if (cee.getBlock() instanceof LeavesBlock) {
            return cee.<Integer>getValue((Property<Integer>)LeavesBlock.DISTANCE);
        }
        return 7;
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!bru.isRainingAt(fx.above())) {
            return;
        }
        if (random.nextInt(15) != 1) {
            return;
        }
        final BlockPos fx2 = fx.below();
        final BlockState cee2 = bru.getBlockState(fx2);
        if (cee2.canOcclude() && cee2.isFaceSturdy(bru, fx2, Direction.UP)) {
            return;
        }
        final double double8 = fx.getX() + random.nextDouble();
        final double double9 = fx.getY() - 0.05;
        final double double10 = fx.getZ() + random.nextDouble();
        bru.addParticle(ParticleTypes.DRIPPING_WATER, double8, double9, double10, 0.0, 0.0, 0.0);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(LeavesBlock.DISTANCE, LeavesBlock.PERSISTENT);
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return updateDistance(((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)LeavesBlock.PERSISTENT, true), bnv.getLevel(), bnv.getClickedPos());
    }
    
    static {
        DISTANCE = BlockStateProperties.DISTANCE;
        PERSISTENT = BlockStateProperties.PERSISTENT;
    }
}
