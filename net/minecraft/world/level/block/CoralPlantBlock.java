package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.phys.shapes.CollisionContext;
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
import net.minecraft.world.phys.shapes.VoxelShape;

public class CoralPlantBlock extends BaseCoralPlantTypeBlock {
    private final Block deadBlock;
    protected static final VoxelShape SHAPE;
    
    protected CoralPlantBlock(final Block bul, final Properties c) {
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
            aag.setBlock(fx, ((StateHolder<O, BlockState>)this.deadBlock.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)CoralPlantBlock.WATERLOGGED, false), 2);
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        this.tryScheduleDieTick(cee1, brv, fx5);
        if (cee1.<Boolean>getValue((Property<Boolean>)CoralPlantBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return CoralPlantBlock.SHAPE;
    }
    
    static {
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 15.0, 14.0);
    }
}
