package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PotatoBlock extends CropBlock {
    private static final VoxelShape[] SHAPE_BY_AGE;
    
    public PotatoBlock(final Properties c) {
        super(c);
    }
    
    @Override
    protected ItemLike getBaseSeedId() {
        return Items.POTATO;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return PotatoBlock.SHAPE_BY_AGE[cee.<Integer>getValue((Property<Integer>)this.getAgeProperty())];
    }
    
    static {
        SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0) };
    }
}
