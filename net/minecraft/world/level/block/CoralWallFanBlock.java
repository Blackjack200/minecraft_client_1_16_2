package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CoralWallFanBlock extends BaseCoralWallFanBlock {
    private final Block deadBlock;
    
    protected CoralWallFanBlock(final Block bul, final Properties c) {
        super(c);
        this.deadBlock = bul;
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        this.tryScheduleDieTick(cee1, bru, fx);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!BaseCoralPlantTypeBlock.scanForWater(cee, aag, fx)) {
            aag.setBlock(fx, (((StateHolder<O, BlockState>)this.deadBlock.defaultBlockState()).setValue((Property<Comparable>)CoralWallFanBlock.WATERLOGGED, false)).<Comparable, Comparable>setValue((Property<Comparable>)CoralWallFanBlock.FACING, (Comparable)cee.<V>getValue((Property<V>)CoralWallFanBlock.FACING)), 2);
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)CoralWallFanBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)CoralWallFanBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        this.tryScheduleDieTick(cee1, brv, fx5);
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
}
