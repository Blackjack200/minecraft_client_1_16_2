package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import java.util.Random;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BubbleColumnBlock extends Block implements BucketPickup {
    public static final BooleanProperty DRAG_DOWN;
    
    public BubbleColumnBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Boolean>setValue((Property<Comparable>)BubbleColumnBlock.DRAG_DOWN, true));
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        final BlockState cee2 = bru.getBlockState(fx.above());
        if (cee2.isAir()) {
            apx.onAboveBubbleCol(cee.<Boolean>getValue((Property<Boolean>)BubbleColumnBlock.DRAG_DOWN));
            if (!bru.isClientSide) {
                final ServerLevel aag7 = (ServerLevel)bru;
                for (int integer8 = 0; integer8 < 2; ++integer8) {
                    aag7.<SimpleParticleType>sendParticles(ParticleTypes.SPLASH, fx.getX() + bru.random.nextDouble(), fx.getY() + 1, fx.getZ() + bru.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                    aag7.<SimpleParticleType>sendParticles(ParticleTypes.BUBBLE, fx.getX() + bru.random.nextDouble(), fx.getY() + 1, fx.getZ() + bru.random.nextDouble(), 1, 0.0, 0.01, 0.0, 0.2);
                }
            }
        }
        else {
            apx.onInsideBubbleColumn(cee.<Boolean>getValue((Property<Boolean>)BubbleColumnBlock.DRAG_DOWN));
        }
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        growColumn(bru, fx.above(), getDrag(bru, fx.below()));
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        growColumn(aag, fx.above(), getDrag(aag, fx));
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        return Fluids.WATER.getSource(false);
    }
    
    public static void growColumn(final LevelAccessor brv, final BlockPos fx, final boolean boolean3) {
        if (canExistIn(brv, fx)) {
            brv.setBlock(fx, ((StateHolder<O, BlockState>)Blocks.BUBBLE_COLUMN.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)BubbleColumnBlock.DRAG_DOWN, boolean3), 2);
        }
    }
    
    public static boolean canExistIn(final LevelAccessor brv, final BlockPos fx) {
        final FluidState cuu3 = brv.getFluidState(fx);
        return brv.getBlockState(fx).is(Blocks.WATER) && cuu3.getAmount() >= 8 && cuu3.isSource();
    }
    
    private static boolean getDrag(final BlockGetter bqz, final BlockPos fx) {
        final BlockState cee3 = bqz.getBlockState(fx);
        if (cee3.is(Blocks.BUBBLE_COLUMN)) {
            return cee3.<Boolean>getValue((Property<Boolean>)BubbleColumnBlock.DRAG_DOWN);
        }
        return !cee3.is(Blocks.SOUL_SAND);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final double double6 = fx.getX();
        final double double7 = fx.getY();
        final double double8 = fx.getZ();
        if (cee.<Boolean>getValue((Property<Boolean>)BubbleColumnBlock.DRAG_DOWN)) {
            bru.addAlwaysVisibleParticle(ParticleTypes.CURRENT_DOWN, double6 + 0.5, double7 + 0.8, double8, 0.0, 0.0, 0.0);
            if (random.nextInt(200) == 0) {
                bru.playLocalSound(double6, double7, double8, SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }
        else {
            bru.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, double6 + 0.5, double7, double8 + 0.5, 0.0, 0.04, 0.0);
            bru.addAlwaysVisibleParticle(ParticleTypes.BUBBLE_COLUMN_UP, double6 + random.nextFloat(), double7 + random.nextFloat(), double8 + random.nextFloat(), 0.0, 0.04, 0.0);
            if (random.nextInt(200) == 0) {
                bru.playLocalSound(double6, double7, double8, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            return Blocks.WATER.defaultBlockState();
        }
        if (gc == Direction.DOWN) {
            brv.setBlock(fx5, ((StateHolder<O, BlockState>)Blocks.BUBBLE_COLUMN.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)BubbleColumnBlock.DRAG_DOWN, getDrag(brv, fx6)), 2);
        }
        else if (gc == Direction.UP && !cee3.is(Blocks.BUBBLE_COLUMN) && canExistIn(brv, fx6)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 5);
        }
        brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.below());
        return cee2.is(Blocks.BUBBLE_COLUMN) || cee2.is(Blocks.MAGMA_BLOCK) || cee2.is(Blocks.SOUL_SAND);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.empty();
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BubbleColumnBlock.DRAG_DOWN);
    }
    
    @Override
    public Fluid takeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        brv.setBlock(fx, Blocks.AIR.defaultBlockState(), 11);
        return Fluids.WATER;
    }
    
    static {
        DRAG_DOWN = BlockStateProperties.DRAG;
    }
}
