package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.item.DyeColor;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BannerBlock extends AbstractBannerBlock {
    public static final IntegerProperty ROTATION;
    private static final Map<DyeColor, Block> BY_COLOR;
    private static final VoxelShape SHAPE;
    
    public BannerBlock(final DyeColor bku, final Properties c) {
        super(bku, c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)BannerBlock.ROTATION, 0));
        BannerBlock.BY_COLOR.put(bku, this);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return brw.getBlockState(fx.below()).getMaterial().isSolid();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return BannerBlock.SHAPE;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)BannerBlock.ROTATION, Mth.floor((180.0f + bnv.getRotation()) * 16.0f / 360.0f + 0.5) & 0xF);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)BannerBlock.ROTATION, bzj.rotate(cee.<Integer>getValue((Property<Integer>)BannerBlock.ROTATION), 16));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)BannerBlock.ROTATION, byd.mirror(cee.<Integer>getValue((Property<Integer>)BannerBlock.ROTATION), 16));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(BannerBlock.ROTATION);
    }
    
    public static Block byColor(final DyeColor bku) {
        return (Block)BannerBlock.BY_COLOR.getOrDefault(bku, Blocks.WHITE_BANNER);
    }
    
    static {
        ROTATION = BlockStateProperties.ROTATION_16;
        BY_COLOR = (Map)Maps.newHashMap();
        SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    }
}
