package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import java.util.Collections;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.material.FluidState;
import java.util.List;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class LiquidBlock extends Block implements BucketPickup {
    public static final IntegerProperty LEVEL;
    protected final FlowingFluid fluid;
    private final List<FluidState> stateCache;
    public static final VoxelShape STABLE_SHAPE;
    
    protected LiquidBlock(final FlowingFluid cus, final Properties c) {
        super(c);
        this.fluid = cus;
        (this.stateCache = (List<FluidState>)Lists.newArrayList()).add(cus.getSource(false));
        for (int integer4 = 1; integer4 < 8; ++integer4) {
            this.stateCache.add(cus.getFlowing(8 - integer4, false));
        }
        this.stateCache.add(cus.getFlowing(8, true));
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)LiquidBlock.LEVEL, 0));
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        if (dcp.isAbove(LiquidBlock.STABLE_SHAPE, fx, true) && cee.<Integer>getValue((Property<Integer>)LiquidBlock.LEVEL) == 0 && dcp.canStandOnFluid(bqz.getFluidState(fx.above()), this.fluid)) {
            return LiquidBlock.STABLE_SHAPE;
        }
        return Shapes.empty();
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.getFluidState().isRandomlyTicking();
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        cee.getFluidState().randomTick(aag, fx, random);
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return false;
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return !this.fluid.is(FluidTags.LAVA);
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        final int integer3 = cee.<Integer>getValue((Property<Integer>)LiquidBlock.LEVEL);
        return (FluidState)this.stateCache.get(Math.min(integer3, 8));
    }
    
    @Override
    public boolean skipRendering(final BlockState cee1, final BlockState cee2, final Direction gc) {
        return cee2.getFluidState().getType().isSame(this.fluid);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.INVISIBLE;
    }
    
    @Override
    public List<ItemStack> getDrops(final BlockState cee, final LootContext.Builder a) {
        return (List<ItemStack>)Collections.emptyList();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return Shapes.empty();
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (this.shouldSpreadLiquid(bru, fx, cee1)) {
            bru.getLiquidTicks().scheduleTick(fx, cee1.getFluidState().getType(), this.fluid.getTickDelay(bru));
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.getFluidState().isSource() || cee3.getFluidState().isSource()) {
            brv.getLiquidTicks().scheduleTick(fx5, cee1.getFluidState().getType(), this.fluid.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (this.shouldSpreadLiquid(bru, fx3, cee)) {
            bru.getLiquidTicks().scheduleTick(fx3, cee.getFluidState().getType(), this.fluid.getTickDelay(bru));
        }
    }
    
    private boolean shouldSpreadLiquid(final Level bru, final BlockPos fx, final BlockState cee) {
        if (this.fluid.is(FluidTags.LAVA)) {
            final boolean boolean5 = bru.getBlockState(fx.below()).is(Blocks.SOUL_SOIL);
            for (final Direction gc9 : Direction.values()) {
                if (gc9 != Direction.DOWN) {
                    final BlockPos fx2 = fx.relative(gc9);
                    if (bru.getFluidState(fx2).is(FluidTags.WATER)) {
                        final Block bul11 = bru.getFluidState(fx).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
                        bru.setBlockAndUpdate(fx, bul11.defaultBlockState());
                        this.fizz(bru, fx);
                        return false;
                    }
                    if (boolean5 && bru.getBlockState(fx2).is(Blocks.BLUE_ICE)) {
                        bru.setBlockAndUpdate(fx, Blocks.BASALT.defaultBlockState());
                        this.fizz(bru, fx);
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private void fizz(final LevelAccessor brv, final BlockPos fx) {
        brv.levelEvent(1501, fx, 0);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(LiquidBlock.LEVEL);
    }
    
    @Override
    public Fluid takeLiquid(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        if (cee.<Integer>getValue((Property<Integer>)LiquidBlock.LEVEL) == 0) {
            brv.setBlock(fx, Blocks.AIR.defaultBlockState(), 11);
            return this.fluid;
        }
        return Fluids.EMPTY;
    }
    
    static {
        LEVEL = BlockStateProperties.LEVEL;
        STABLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    }
}
