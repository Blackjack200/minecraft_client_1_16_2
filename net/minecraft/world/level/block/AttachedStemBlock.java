package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class AttachedStemBlock extends BushBlock {
    public static final DirectionProperty FACING;
    private final StemGrownBlock fruit;
    private static final Map<Direction, VoxelShape> AABBS;
    
    protected AttachedStemBlock(final StemGrownBlock cak, final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)AttachedStemBlock.FACING, Direction.NORTH));
        this.fruit = cak;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)AttachedStemBlock.AABBS.get(cee.getValue((Property<Object>)AttachedStemBlock.FACING));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee3.is(this.fruit) && gc == cee1.<Comparable>getValue((Property<Comparable>)AttachedStemBlock.FACING)) {
            return ((StateHolder<O, BlockState>)this.fruit.getStem().defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)StemBlock.AGE, 7);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.is(Blocks.FARMLAND);
    }
    
    protected Item getSeedItem() {
        if (this.fruit == Blocks.PUMPKIN) {
            return Items.PUMPKIN_SEEDS;
        }
        if (this.fruit == Blocks.MELON) {
            return Items.MELON_SEEDS;
        }
        return Items.AIR;
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack(this.getSeedItem());
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)AttachedStemBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)AttachedStemBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)AttachedStemBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(AttachedStemBlock.FACING);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.SOUTH, Block.box(6.0, 0.0, 6.0, 10.0, 10.0, 16.0), Direction.WEST, Block.box(0.0, 0.0, 6.0, 10.0, 10.0, 10.0), Direction.NORTH, Block.box(6.0, 0.0, 0.0, 10.0, 10.0, 10.0), Direction.EAST, Block.box(6.0, 0.0, 6.0, 16.0, 10.0, 10.0)));
    }
}
