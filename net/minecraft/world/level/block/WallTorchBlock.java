package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class WallTorchBlock extends TorchBlock {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> AABBS;
    
    protected WallTorchBlock(final Properties c, final ParticleOptions hf) {
        super(c, hf);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, Direction.NORTH));
    }
    
    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return getShape(cee);
    }
    
    public static VoxelShape getShape(final BlockState cee) {
        return (VoxelShape)WallTorchBlock.AABBS.get(cee.getValue((Property<Object>)WallTorchBlock.FACING));
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)WallTorchBlock.FACING);
        final BlockPos fx2 = fx.relative(gc5.getOpposite());
        final BlockState cee2 = brw.getBlockState(fx2);
        return cee2.isFaceSturdy(brw, fx2, gc5);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        BlockState cee3 = this.defaultBlockState();
        final LevelReader brw4 = bnv.getLevel();
        final BlockPos fx5 = bnv.getClickedPos();
        final Direction[] nearestLookingDirections;
        final Direction[] arr6 = nearestLookingDirections = bnv.getNearestLookingDirections();
        for (final Direction gc10 : nearestLookingDirections) {
            if (gc10.getAxis().isHorizontal()) {
                final Direction gc11 = gc10.getOpposite();
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, gc11);
                if (cee3.canSurvive(brw4, fx5)) {
                    return cee3;
                }
            }
        }
        return null;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)WallTorchBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return cee1;
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)WallTorchBlock.FACING);
        final double double7 = fx.getX() + 0.5;
        final double double8 = fx.getY() + 0.7;
        final double double9 = fx.getZ() + 0.5;
        final double double10 = 0.22;
        final double double11 = 0.27;
        final Direction gc7 = gc6.getOpposite();
        bru.addParticle(ParticleTypes.SMOKE, double7 + 0.27 * gc7.getStepX(), double8 + 0.22, double9 + 0.27 * gc7.getStepZ(), 0.0, 0.0, 0.0);
        bru.addParticle(this.flameParticle, double7 + 0.27 * gc7.getStepX(), double8 + 0.22, double9 + 0.27 * gc7.getStepZ(), 0.0, 0.0, 0.0);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)WallTorchBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)WallTorchBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)WallTorchBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(WallTorchBlock.FACING);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        AABBS = (Map)Maps.newEnumMap((Map)ImmutableMap.of(Direction.NORTH, Block.box(5.5, 3.0, 11.0, 10.5, 13.0, 16.0), Direction.SOUTH, Block.box(5.5, 3.0, 0.0, 10.5, 13.0, 5.0), Direction.WEST, Block.box(11.0, 3.0, 5.5, 16.0, 13.0, 10.5), Direction.EAST, Block.box(0.0, 3.0, 5.5, 5.0, 13.0, 10.5)));
    }
}
