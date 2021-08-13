package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Iterator;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FarmBlock extends Block {
    public static final IntegerProperty MOISTURE;
    protected static final VoxelShape SHAPE;
    
    protected FarmBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)FarmBlock.MOISTURE, 0));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.UP && !cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.above());
        return !cee2.getMaterial().isSolid() || cee2.getBlock() instanceof FenceGateBlock || cee2.getBlock() instanceof MovingPistonBlock;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        if (!this.defaultBlockState().canSurvive(bnv.getLevel(), bnv.getClickedPos())) {
            return Blocks.DIRT.defaultBlockState();
        }
        return super.getStateForPlacement(bnv);
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return FarmBlock.SHAPE;
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            turnToDirt(cee, aag, fx);
        }
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final int integer6 = cee.<Integer>getValue((Property<Integer>)FarmBlock.MOISTURE);
        if (isNearWater(aag, fx) || aag.isRainingAt(fx.above())) {
            if (integer6 < 7) {
                aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)FarmBlock.MOISTURE, 7), 2);
            }
        }
        else if (integer6 > 0) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)FarmBlock.MOISTURE, integer6 - 1), 2);
        }
        else if (!isUnderCrops(aag, fx)) {
            turnToDirt(cee, aag, fx);
        }
    }
    
    @Override
    public void fallOn(final Level bru, final BlockPos fx, final Entity apx, final float float4) {
        if (!bru.isClientSide && bru.random.nextFloat() < float4 - 0.5f && apx instanceof LivingEntity && (apx instanceof Player || bru.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && apx.getBbWidth() * apx.getBbWidth() * apx.getBbHeight() > 0.512f) {
            turnToDirt(bru.getBlockState(fx), bru, fx);
        }
        super.fallOn(bru, fx, apx, float4);
    }
    
    public static void turnToDirt(final BlockState cee, final Level bru, final BlockPos fx) {
        bru.setBlockAndUpdate(fx, Block.pushEntitiesUp(cee, Blocks.DIRT.defaultBlockState(), bru, fx));
    }
    
    private static boolean isUnderCrops(final BlockGetter bqz, final BlockPos fx) {
        final Block bul3 = bqz.getBlockState(fx.above()).getBlock();
        return bul3 instanceof CropBlock || bul3 instanceof StemBlock || bul3 instanceof AttachedStemBlock;
    }
    
    private static boolean isNearWater(final LevelReader brw, final BlockPos fx) {
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-4, 0, -4), fx.offset(4, 1, 4))) {
            if (brw.getFluidState(fx2).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(FarmBlock.MOISTURE);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        MOISTURE = BlockStateProperties.MOISTURE;
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    }
}
