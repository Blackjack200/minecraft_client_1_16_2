package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SaplingBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty STAGE;
    protected static final VoxelShape SHAPE;
    private final AbstractTreeGrower treeGrower;
    
    protected SaplingBlock(final AbstractTreeGrower cdn, final Properties c) {
        super(c);
        this.treeGrower = cdn;
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)SaplingBlock.STAGE, 0));
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SaplingBlock.SHAPE;
    }
    
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.getMaxLocalRawBrightness(fx.above()) >= 9 && random.nextInt(7) == 0) {
            this.advanceTree(aag, fx, cee, random);
        }
    }
    
    public void advanceTree(final ServerLevel aag, final BlockPos fx, final BlockState cee, final Random random) {
        if (cee.<Integer>getValue((Property<Integer>)SaplingBlock.STAGE) == 0) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)SaplingBlock.STAGE), 4);
        }
        else {
            this.treeGrower.growTree(aag, aag.getChunkSource().getGenerator(), fx, cee, random);
        }
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return true;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return bru.random.nextFloat() < 0.45;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        this.advanceTree(aag, fx, cee, random);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SaplingBlock.STAGE);
    }
    
    static {
        STAGE = BlockStateProperties.STAGE;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
    }
}
