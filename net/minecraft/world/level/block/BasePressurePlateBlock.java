package net.minecraft.world.level.block;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BasePressurePlateBlock extends Block {
    protected static final VoxelShape PRESSED_AABB;
    protected static final VoxelShape AABB;
    protected static final AABB TOUCH_AABB;
    
    protected BasePressurePlateBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (this.getSignalForState(cee) > 0) ? BasePressurePlateBlock.PRESSED_AABB : BasePressurePlateBlock.AABB;
    }
    
    protected int getPressedTime() {
        return 20;
    }
    
    @Override
    public boolean isPossibleToRespawnInThis() {
        return true;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockPos fx2 = fx.below();
        return Block.canSupportRigidBlock(brw, fx2) || Block.canSupportCenter(brw, fx2, Direction.UP);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final int integer6 = this.getSignalForState(cee);
        if (integer6 > 0) {
            this.checkPressed(aag, fx, cee, integer6);
        }
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (bru.isClientSide) {
            return;
        }
        final int integer6 = this.getSignalForState(cee);
        if (integer6 == 0) {
            this.checkPressed(bru, fx, cee, integer6);
        }
    }
    
    protected void checkPressed(final Level bru, final BlockPos fx, final BlockState cee, final int integer) {
        final int integer2 = this.getSignalStrength(bru, fx);
        final boolean boolean7 = integer > 0;
        final boolean boolean8 = integer2 > 0;
        if (integer != integer2) {
            final BlockState cee2 = this.setSignalForState(cee, integer2);
            bru.setBlock(fx, cee2, 2);
            this.updateNeighbours(bru, fx);
            bru.setBlocksDirty(fx, cee, cee2);
        }
        if (!boolean8 && boolean7) {
            this.playOffSound(bru, fx);
        }
        else if (boolean8 && !boolean7) {
            this.playOnSound(bru, fx);
        }
        if (boolean8) {
            bru.getBlockTicks().scheduleTick(new BlockPos(fx), this, this.getPressedTime());
        }
    }
    
    protected abstract void playOnSound(final LevelAccessor brv, final BlockPos fx);
    
    protected abstract void playOffSound(final LevelAccessor brv, final BlockPos fx);
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (boolean5 || cee1.is(cee4.getBlock())) {
            return;
        }
        if (this.getSignalForState(cee1) > 0) {
            this.updateNeighbours(bru, fx);
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    protected void updateNeighbours(final Level bru, final BlockPos fx) {
        bru.updateNeighborsAt(fx, this);
        bru.updateNeighborsAt(fx.below(), this);
    }
    
    @Override
    public int getSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        return this.getSignalForState(cee);
    }
    
    @Override
    public int getDirectSignal(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        if (gc == Direction.UP) {
            return this.getSignalForState(cee);
        }
        return 0;
    }
    
    @Override
    public boolean isSignalSource(final BlockState cee) {
        return true;
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.DESTROY;
    }
    
    protected abstract int getSignalStrength(final Level bru, final BlockPos fx);
    
    protected abstract int getSignalForState(final BlockState cee);
    
    protected abstract BlockState setSignalForState(final BlockState cee, final int integer);
    
    static {
        PRESSED_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 0.5, 15.0);
        AABB = Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
        TOUCH_AABB = new AABB(0.125, 0.0, 0.125, 0.875, 0.25, 0.875);
    }
}
