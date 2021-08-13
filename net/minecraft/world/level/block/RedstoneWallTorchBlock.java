package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Random;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class RedstoneWallTorchBlock extends RedstoneTorchBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty LIT;
    
    protected RedstoneWallTorchBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)RedstoneWallTorchBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)RedstoneWallTorchBlock.LIT, true));
    }
    
    @Override
    public String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return WallTorchBlock.getShape(cee);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return Blocks.WALL_TORCH.canSurvive(cee, brw, fx);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        return Blocks.WALL_TORCH.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = Blocks.WALL_TORCH.getStateForPlacement(bnv);
        return (cee3 == null) ? null : ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Comparable>setValue((Property<Comparable>)RedstoneWallTorchBlock.FACING, (Comparable)cee3.<V>getValue((Property<V>)RedstoneWallTorchBlock.FACING));
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)RedstoneWallTorchBlock.LIT)) {
            return;
        }
        final Direction gc6 = cee.<Direction>getValue((Property<Direction>)RedstoneWallTorchBlock.FACING).getOpposite();
        final double double7 = 0.27;
        final double double8 = fx.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + 0.27 * gc6.getStepX();
        final double double9 = fx.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2 + 0.22;
        final double double10 = fx.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2 + 0.27 * gc6.getStepZ();
        bru.addParticle(this.flameParticle, double8, double9, double10, 0.0, 0.0, 0.0);
    }
    
    @Override
    protected boolean hasNeighborSignal(final Level bru, final BlockPos fx, final BlockState cee) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)RedstoneWallTorchBlock.FACING).getOpposite();
        return bru.hasSignal(fx.relative(gc5), gc5);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (cee.<Boolean>getValue((Property<Boolean>)RedstoneWallTorchBlock.LIT) && cee.<Comparable>getValue((Property<Comparable>)RedstoneWallTorchBlock.FACING) != gc) {
            return 15;
        }
        return 0;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return Blocks.WALL_TORCH.rotate(cee, bzj);
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return Blocks.WALL_TORCH.mirror(cee, byd);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RedstoneWallTorchBlock.FACING, RedstoneWallTorchBlock.LIT);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        LIT = RedstoneTorchBlock.LIT;
    }
}
