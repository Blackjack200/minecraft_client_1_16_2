package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Material;
import java.util.Iterator;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CactusBlock extends Block {
    public static final IntegerProperty AGE;
    protected static final VoxelShape COLLISION_SHAPE;
    protected static final VoxelShape OUTLINE_SHAPE;
    
    protected CactusBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)CactusBlock.AGE, 0));
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            aag.destroyBlock(fx, true);
        }
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final BlockPos fx2 = fx.above();
        if (!aag.isEmptyBlock(fx2)) {
            return;
        }
        int integer7;
        for (integer7 = 1; aag.getBlockState(fx.below(integer7)).is(this); ++integer7) {}
        if (integer7 >= 3) {
            return;
        }
        final int integer8 = cee.<Integer>getValue((Property<Integer>)CactusBlock.AGE);
        if (integer8 == 15) {
            aag.setBlockAndUpdate(fx2, this.defaultBlockState());
            final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)CactusBlock.AGE, 0);
            aag.setBlock(fx, cee2, 4);
            cee2.neighborChanged(aag, fx2, this, fx, false);
        }
        else {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)CactusBlock.AGE, integer8 + 1), 4);
        }
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return CactusBlock.COLLISION_SHAPE;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return CactusBlock.OUTLINE_SHAPE;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        for (final Direction gc6 : Direction.Plane.HORIZONTAL) {
            final BlockState cee2 = brw.getBlockState(fx.relative(gc6));
            final Material cux8 = cee2.getMaterial();
            if (cux8.isSolid() || brw.getFluidState(fx.relative(gc6)).is(FluidTags.LAVA)) {
                return false;
            }
        }
        final BlockState cee3 = brw.getBlockState(fx.below());
        return (cee3.is(Blocks.CACTUS) || cee3.is(Blocks.SAND) || cee3.is(Blocks.RED_SAND)) && !brw.getBlockState(fx.above()).getMaterial().isLiquid();
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        apx.hurt(DamageSource.CACTUS, 1.0f);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CactusBlock.AGE);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        AGE = BlockStateProperties.AGE_15;
        COLLISION_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 15.0, 15.0);
        OUTLINE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    }
}
