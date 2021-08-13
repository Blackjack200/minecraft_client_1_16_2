package net.minecraft.world.level.block;

import net.minecraft.world.phys.shapes.CollisionContext;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import javax.annotation.Nullable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;

public abstract class GrowingPlantBlock extends Block {
    protected final Direction growthDirection;
    protected final boolean scheduleFluidTicks;
    protected final VoxelShape shape;
    
    protected GrowingPlantBlock(final Properties c, final Direction gc, final VoxelShape dde, final boolean boolean4) {
        super(c);
        this.growthDirection = gc;
        this.shape = dde;
        this.scheduleFluidTicks = boolean4;
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockState cee3 = bnv.getLevel().getBlockState(bnv.getClickedPos().relative(this.growthDirection));
        if (cee3.is(this.getHeadBlock()) || cee3.is(this.getBodyBlock())) {
            return this.getBodyBlock().defaultBlockState();
        }
        return this.getStateForPlacement(bnv.getLevel());
    }
    
    public BlockState getStateForPlacement(final LevelAccessor brv) {
        return this.defaultBlockState();
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.relative(this.growthDirection.getOpposite());
        final BlockState cee2 = brw.getBlockState(fx2);
        final Block bul7 = cee2.getBlock();
        return this.canAttachToBlock(bul7) && (bul7 == this.getHeadBlock() || bul7 == this.getBodyBlock() || cee2.isFaceSturdy(brw, fx2, this.growthDirection));
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            aag.destroyBlock(fx, true);
        }
    }
    
    protected boolean canAttachToBlock(final Block bul) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.shape;
    }
    
    protected abstract GrowingPlantHeadBlock getHeadBlock();
    
    protected abstract Block getBodyBlock();
}
