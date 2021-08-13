package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SkullBlock extends AbstractSkullBlock {
    public static final IntegerProperty ROTATION;
    protected static final VoxelShape SHAPE;
    
    protected SkullBlock(final Type a, final Properties c) {
        super(a, c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)SkullBlock.ROTATION, 0));
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return SkullBlock.SHAPE;
    }
    
    public VoxelShape getOcclusionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return Shapes.empty();
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)SkullBlock.ROTATION, Mth.floor(bnv.getRotation() * 16.0f / 360.0f + 0.5) & 0xF);
    }
    
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SkullBlock.ROTATION, bzj.rotate(cee.<Integer>getValue((Property<Integer>)SkullBlock.ROTATION), 16));
    }
    
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)SkullBlock.ROTATION, byd.mirror(cee.<Integer>getValue((Property<Integer>)SkullBlock.ROTATION), 16));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(SkullBlock.ROTATION);
    }
    
    static {
        ROTATION = BlockStateProperties.ROTATION_16;
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    }
    
    public enum Types implements Type {
        SKELETON, 
        WITHER_SKELETON, 
        PLAYER, 
        ZOMBIE, 
        CREEPER, 
        DRAGON;
    }
    
    public interface Type {
    }
}
