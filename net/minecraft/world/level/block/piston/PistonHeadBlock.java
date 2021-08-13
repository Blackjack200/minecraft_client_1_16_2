package net.minecraft.world.level.block.piston;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.Arrays;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.DirectionalBlock;

public class PistonHeadBlock extends DirectionalBlock {
    public static final EnumProperty<PistonType> TYPE;
    public static final BooleanProperty SHORT;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;
    protected static final VoxelShape UP_AABB;
    protected static final VoxelShape DOWN_AABB;
    protected static final VoxelShape UP_ARM_AABB;
    protected static final VoxelShape DOWN_ARM_AABB;
    protected static final VoxelShape SOUTH_ARM_AABB;
    protected static final VoxelShape NORTH_ARM_AABB;
    protected static final VoxelShape EAST_ARM_AABB;
    protected static final VoxelShape WEST_ARM_AABB;
    protected static final VoxelShape SHORT_UP_ARM_AABB;
    protected static final VoxelShape SHORT_DOWN_ARM_AABB;
    protected static final VoxelShape SHORT_SOUTH_ARM_AABB;
    protected static final VoxelShape SHORT_NORTH_ARM_AABB;
    protected static final VoxelShape SHORT_EAST_ARM_AABB;
    protected static final VoxelShape SHORT_WEST_ARM_AABB;
    private static final VoxelShape[] SHAPES_SHORT;
    private static final VoxelShape[] SHAPES_LONG;
    
    private static VoxelShape[] makeShapes(final boolean boolean1) {
        return (VoxelShape[])Arrays.stream((Object[])Direction.values()).map(gc -> calculateShape(gc, boolean1)).toArray(VoxelShape[]::new);
    }
    
    private static VoxelShape calculateShape(final Direction gc, final boolean boolean2) {
        switch (gc) {
            default: {
                return Shapes.or(PistonHeadBlock.DOWN_AABB, boolean2 ? PistonHeadBlock.SHORT_DOWN_ARM_AABB : PistonHeadBlock.DOWN_ARM_AABB);
            }
            case UP: {
                return Shapes.or(PistonHeadBlock.UP_AABB, boolean2 ? PistonHeadBlock.SHORT_UP_ARM_AABB : PistonHeadBlock.UP_ARM_AABB);
            }
            case NORTH: {
                return Shapes.or(PistonHeadBlock.NORTH_AABB, boolean2 ? PistonHeadBlock.SHORT_NORTH_ARM_AABB : PistonHeadBlock.NORTH_ARM_AABB);
            }
            case SOUTH: {
                return Shapes.or(PistonHeadBlock.SOUTH_AABB, boolean2 ? PistonHeadBlock.SHORT_SOUTH_ARM_AABB : PistonHeadBlock.SOUTH_ARM_AABB);
            }
            case WEST: {
                return Shapes.or(PistonHeadBlock.WEST_AABB, boolean2 ? PistonHeadBlock.SHORT_WEST_ARM_AABB : PistonHeadBlock.WEST_ARM_AABB);
            }
            case EAST: {
                return Shapes.or(PistonHeadBlock.EAST_AABB, boolean2 ? PistonHeadBlock.SHORT_EAST_ARM_AABB : PistonHeadBlock.EAST_ARM_AABB);
            }
        }
    }
    
    public PistonHeadBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)PistonHeadBlock.FACING, Direction.NORTH)).setValue(PistonHeadBlock.TYPE, PistonType.DEFAULT)).<Comparable, Boolean>setValue((Property<Comparable>)PistonHeadBlock.SHORT, false));
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (cee.<Boolean>getValue((Property<Boolean>)PistonHeadBlock.SHORT) ? PistonHeadBlock.SHAPES_SHORT : PistonHeadBlock.SHAPES_LONG)[cee.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING).ordinal()];
    }
    
    private boolean isFittingBase(final BlockState cee1, final BlockState cee2) {
        final Block bul4 = (cee1.<PistonType>getValue(PistonHeadBlock.TYPE) == PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON;
        return cee2.is(bul4) && cee2.<Boolean>getValue((Property<Boolean>)PistonBaseBlock.EXTENDED) && cee2.<Comparable>getValue((Property<Comparable>)PistonHeadBlock.FACING) == cee1.<Comparable>getValue((Property<Comparable>)PistonHeadBlock.FACING);
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        if (!bru.isClientSide && bft.abilities.instabuild) {
            final BlockPos fx2 = fx.relative(cee.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING).getOpposite());
            if (this.isFittingBase(cee, bru.getBlockState(fx2))) {
                bru.destroyBlock(fx2, false);
            }
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
        final BlockPos fx2 = fx.relative(cee1.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING).getOpposite());
        if (this.isFittingBase(cee1, bru.getBlockState(fx2))) {
            bru.destroyBlock(fx2, true);
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)PistonHeadBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.relative(cee.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING).getOpposite()));
        return this.isFittingBase(cee, cee2) || (cee2.is(Blocks.MOVING_PISTON) && cee2.<Comparable>getValue((Property<Comparable>)PistonHeadBlock.FACING) == cee.<Comparable>getValue((Property<Comparable>)PistonHeadBlock.FACING));
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (cee.canSurvive(bru, fx3)) {
            final BlockPos fx6 = fx3.relative(cee.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING).getOpposite());
            bru.getBlockState(fx6).neighborChanged(bru, fx6, bul, fx5, false);
        }
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack((cee.<PistonType>getValue(PistonHeadBlock.TYPE) == PistonType.STICKY) ? Blocks.STICKY_PISTON : Blocks.PISTON);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)PistonHeadBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)PistonHeadBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(PistonHeadBlock.FACING, PistonHeadBlock.TYPE, PistonHeadBlock.SHORT);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        TYPE = BlockStateProperties.PISTON_TYPE;
        SHORT = BlockStateProperties.SHORT;
        EAST_AABB = Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        WEST_AABB = Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0);
        NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0);
        UP_AABB = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);
        DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);
        UP_ARM_AABB = Block.box(6.0, -4.0, 6.0, 10.0, 12.0, 10.0);
        DOWN_ARM_AABB = Block.box(6.0, 4.0, 6.0, 10.0, 20.0, 10.0);
        SOUTH_ARM_AABB = Block.box(6.0, 6.0, -4.0, 10.0, 10.0, 12.0);
        NORTH_ARM_AABB = Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 20.0);
        EAST_ARM_AABB = Block.box(-4.0, 6.0, 6.0, 12.0, 10.0, 10.0);
        WEST_ARM_AABB = Block.box(4.0, 6.0, 6.0, 20.0, 10.0, 10.0);
        SHORT_UP_ARM_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 12.0, 10.0);
        SHORT_DOWN_ARM_AABB = Block.box(6.0, 4.0, 6.0, 10.0, 16.0, 10.0);
        SHORT_SOUTH_ARM_AABB = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 12.0);
        SHORT_NORTH_ARM_AABB = Block.box(6.0, 6.0, 4.0, 10.0, 10.0, 16.0);
        SHORT_EAST_ARM_AABB = Block.box(0.0, 6.0, 6.0, 12.0, 10.0, 10.0);
        SHORT_WEST_ARM_AABB = Block.box(4.0, 6.0, 6.0, 16.0, 10.0, 10.0);
        SHAPES_SHORT = makeShapes(true);
        SHAPES_LONG = makeShapes(false);
    }
}
