package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BeetrootBlock extends CropBlock {
    public static final IntegerProperty AGE;
    private static final VoxelShape[] SHAPE_BY_AGE;
    
    public BeetrootBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public IntegerProperty getAgeProperty() {
        return BeetrootBlock.AGE;
    }
    
    @Override
    public int getMaxAge() {
        return 3;
    }
    
    @Override
    protected ItemLike getBaseSeedId() {
        return Items.BEETROOT_SEEDS;
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (random.nextInt(3) != 0) {
            super.randomTick(cee, aag, fx, random);
        }
    }
    
    @Override
    protected int getBonemealAgeIncrease(final Level bru) {
        return super.getBonemealAgeIncrease(bru) / 3;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BeetrootBlock.AGE);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return BeetrootBlock.SHAPE_BY_AGE[cee.<Integer>getValue((Property<Integer>)this.getAgeProperty())];
    }
    
    static {
        AGE = BlockStateProperties.AGE_3;
        SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0) };
    }
}
