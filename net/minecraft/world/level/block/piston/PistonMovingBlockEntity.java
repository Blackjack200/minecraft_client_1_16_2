package net.minecraft.world.level.block.piston;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import java.util.function.Predicate;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PistonMovingBlockEntity extends BlockEntity implements TickableBlockEntity {
    private BlockState movedState;
    private Direction direction;
    private boolean extending;
    private boolean isSourcePiston;
    private static final ThreadLocal<Direction> NOCLIP;
    private float progress;
    private float progressO;
    private long lastTicked;
    private int deathTicks;
    
    public PistonMovingBlockEntity() {
        super(BlockEntityType.PISTON);
    }
    
    public PistonMovingBlockEntity(final BlockState cee, final Direction gc, final boolean boolean3, final boolean boolean4) {
        this();
        this.movedState = cee;
        this.direction = gc;
        this.extending = boolean3;
        this.isSourcePiston = boolean4;
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public boolean isExtending() {
        return this.extending;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public boolean isSourcePiston() {
        return this.isSourcePiston;
    }
    
    public float getProgress(float float1) {
        if (float1 > 1.0f) {
            float1 = 1.0f;
        }
        return Mth.lerp(float1, this.progressO, this.progress);
    }
    
    public float getXOff(final float float1) {
        return this.direction.getStepX() * this.getExtendedProgress(this.getProgress(float1));
    }
    
    public float getYOff(final float float1) {
        return this.direction.getStepY() * this.getExtendedProgress(this.getProgress(float1));
    }
    
    public float getZOff(final float float1) {
        return this.direction.getStepZ() * this.getExtendedProgress(this.getProgress(float1));
    }
    
    private float getExtendedProgress(final float float1) {
        return this.extending ? (float1 - 1.0f) : (1.0f - float1);
    }
    
    private BlockState getCollisionRelatedBlockState() {
        if (!this.isExtending() && this.isSourcePiston() && this.movedState.getBlock() instanceof PistonBaseBlock) {
            return ((((StateHolder<O, BlockState>)Blocks.PISTON_HEAD.defaultBlockState()).setValue((Property<Comparable>)PistonHeadBlock.SHORT, this.progress > 0.25f)).setValue((Property<Comparable>)PistonHeadBlock.TYPE, this.movedState.is(Blocks.STICKY_PISTON) ? PistonType.STICKY : PistonType.DEFAULT)).<Comparable, Comparable>setValue((Property<Comparable>)PistonHeadBlock.FACING, (Comparable)this.movedState.<V>getValue((Property<V>)PistonBaseBlock.FACING));
        }
        return this.movedState;
    }
    
    private void moveCollidedEntities(final float float1) {
        final Direction gc3 = this.getMovementDirection();
        final double double4 = float1 - this.progress;
        final VoxelShape dde6 = this.getCollisionRelatedBlockState().getCollisionShape(this.level, this.getBlockPos());
        if (dde6.isEmpty()) {
            return;
        }
        final AABB dcf7 = this.moveByPositionAndProgress(dde6.bounds());
        final List<Entity> list8 = this.level.getEntities(null, PistonMath.getMovementArea(dcf7, gc3, double4).minmax(dcf7));
        if (list8.isEmpty()) {
            return;
        }
        final List<AABB> list9 = dde6.toAabbs();
        final boolean boolean10 = this.movedState.is(Blocks.SLIME_BLOCK);
        for (final Entity apx12 : list8) {
            if (apx12.getPistonPushReaction() == PushReaction.IGNORE) {
                continue;
            }
            if (boolean10) {
                if (apx12 instanceof ServerPlayer) {
                    continue;
                }
                final Vec3 dck13 = apx12.getDeltaMovement();
                double double5 = dck13.x;
                double double6 = dck13.y;
                double double7 = dck13.z;
                switch (gc3.getAxis()) {
                    case X: {
                        double5 = gc3.getStepX();
                        break;
                    }
                    case Y: {
                        double6 = gc3.getStepY();
                        break;
                    }
                    case Z: {
                        double7 = gc3.getStepZ();
                        break;
                    }
                }
                apx12.setDeltaMovement(double5, double6, double7);
            }
            double double8 = 0.0;
            for (final AABB dcf8 : list9) {
                final AABB dcf9 = PistonMath.getMovementArea(this.moveByPositionAndProgress(dcf8), gc3, double4);
                final AABB dcf10 = apx12.getBoundingBox();
                if (!dcf9.intersects(dcf10)) {
                    continue;
                }
                double8 = Math.max(double8, getMovement(dcf9, gc3, dcf10));
                if (double8 >= double4) {
                    break;
                }
            }
            if (double8 <= 0.0) {
                continue;
            }
            double8 = Math.min(double8, double4) + 0.01;
            moveEntityByPiston(gc3, apx12, double8, gc3);
            if (this.extending || !this.isSourcePiston) {
                continue;
            }
            this.fixEntityWithinPistonBase(apx12, gc3, double4);
        }
    }
    
    private static void moveEntityByPiston(final Direction gc1, final Entity apx, final double double3, final Direction gc4) {
        PistonMovingBlockEntity.NOCLIP.set(gc1);
        apx.move(MoverType.PISTON, new Vec3(double3 * gc4.getStepX(), double3 * gc4.getStepY(), double3 * gc4.getStepZ()));
        PistonMovingBlockEntity.NOCLIP.set(null);
    }
    
    private void moveStuckEntities(final float float1) {
        if (!this.isStickyForEntities()) {
            return;
        }
        final Direction gc3 = this.getMovementDirection();
        if (!gc3.getAxis().isHorizontal()) {
            return;
        }
        final double double4 = this.movedState.getCollisionShape(this.level, this.worldPosition).max(Direction.Axis.Y);
        final AABB dcf6 = this.moveByPositionAndProgress(new AABB(0.0, double4, 0.0, 1.0, 1.5000000999999998, 1.0));
        final double double5 = float1 - this.progress;
        final List<Entity> list9 = this.level.getEntities(null, dcf6, (apx -> matchesStickyCritera(dcf6, apx)));
        for (final Entity apx11 : list9) {
            moveEntityByPiston(gc3, apx11, double5, gc3);
        }
    }
    
    private static boolean matchesStickyCritera(final AABB dcf, final Entity apx) {
        return apx.getPistonPushReaction() == PushReaction.NORMAL && apx.isOnGround() && apx.getX() >= dcf.minX && apx.getX() <= dcf.maxX && apx.getZ() >= dcf.minZ && apx.getZ() <= dcf.maxZ;
    }
    
    private boolean isStickyForEntities() {
        return this.movedState.is(Blocks.HONEY_BLOCK);
    }
    
    public Direction getMovementDirection() {
        return this.extending ? this.direction : this.direction.getOpposite();
    }
    
    private static double getMovement(final AABB dcf1, final Direction gc, final AABB dcf3) {
        switch (gc) {
            case EAST: {
                return dcf1.maxX - dcf3.minX;
            }
            case WEST: {
                return dcf3.maxX - dcf1.minX;
            }
            default: {
                return dcf1.maxY - dcf3.minY;
            }
            case DOWN: {
                return dcf3.maxY - dcf1.minY;
            }
            case SOUTH: {
                return dcf1.maxZ - dcf3.minZ;
            }
            case NORTH: {
                return dcf3.maxZ - dcf1.minZ;
            }
        }
    }
    
    private AABB moveByPositionAndProgress(final AABB dcf) {
        final double double3 = this.getExtendedProgress(this.progress);
        return dcf.move(this.worldPosition.getX() + double3 * this.direction.getStepX(), this.worldPosition.getY() + double3 * this.direction.getStepY(), this.worldPosition.getZ() + double3 * this.direction.getStepZ());
    }
    
    private void fixEntityWithinPistonBase(final Entity apx, final Direction gc, final double double3) {
        final AABB dcf6 = apx.getBoundingBox();
        final AABB dcf7 = Shapes.block().bounds().move(this.worldPosition);
        if (dcf6.intersects(dcf7)) {
            final Direction gc2 = gc.getOpposite();
            double double4 = getMovement(dcf7, gc2, dcf6) + 0.01;
            final double double5 = getMovement(dcf7, gc2, dcf6.intersect(dcf7)) + 0.01;
            if (Math.abs(double4 - double5) < 0.01) {
                double4 = Math.min(double4, double3) + 0.01;
                moveEntityByPiston(gc, apx, double4, gc2);
            }
        }
    }
    
    public BlockState getMovedState() {
        return this.movedState;
    }
    
    public void finalTick() {
        if (this.level != null && (this.progressO < 1.0f || this.level.isClientSide)) {
            this.progress = 1.0f;
            this.progressO = this.progress;
            this.level.removeBlockEntity(this.worldPosition);
            this.setRemoved();
            if (this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
                BlockState cee2;
                if (this.isSourcePiston) {
                    cee2 = Blocks.AIR.defaultBlockState();
                }
                else {
                    cee2 = Block.updateFromNeighbourShapes(this.movedState, this.level, this.worldPosition);
                }
                this.level.setBlock(this.worldPosition, cee2, 3);
                this.level.neighborChanged(this.worldPosition, cee2.getBlock(), this.worldPosition);
            }
        }
    }
    
    @Override
    public void tick() {
        this.lastTicked = this.level.getGameTime();
        this.progressO = this.progress;
        if (this.progressO < 1.0f) {
            final float float2 = this.progress + 0.5f;
            this.moveCollidedEntities(float2);
            this.moveStuckEntities(float2);
            this.progress = float2;
            if (this.progress >= 1.0f) {
                this.progress = 1.0f;
            }
            return;
        }
        if (this.level.isClientSide && this.deathTicks < 5) {
            ++this.deathTicks;
            return;
        }
        this.level.removeBlockEntity(this.worldPosition);
        this.setRemoved();
        if (this.movedState != null && this.level.getBlockState(this.worldPosition).is(Blocks.MOVING_PISTON)) {
            BlockState cee2 = Block.updateFromNeighbourShapes(this.movedState, this.level, this.worldPosition);
            if (cee2.isAir()) {
                this.level.setBlock(this.worldPosition, this.movedState, 84);
                Block.updateOrDestroy(this.movedState, cee2, this.level, this.worldPosition, 3);
            }
            else {
                if (cee2.<Comparable>hasProperty((Property<Comparable>)BlockStateProperties.WATERLOGGED) && cee2.<Boolean>getValue((Property<Boolean>)BlockStateProperties.WATERLOGGED)) {
                    cee2 = ((StateHolder<O, BlockState>)cee2).<Comparable, Boolean>setValue((Property<Comparable>)BlockStateProperties.WATERLOGGED, false);
                }
                this.level.setBlock(this.worldPosition, cee2, 67);
                this.level.neighborChanged(this.worldPosition, cee2.getBlock(), this.worldPosition);
            }
        }
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.movedState = NbtUtils.readBlockState(md.getCompound("blockState"));
        this.direction = Direction.from3DDataValue(md.getInt("facing"));
        this.progress = md.getFloat("progress");
        this.progressO = this.progress;
        this.extending = md.getBoolean("extending");
        this.isSourcePiston = md.getBoolean("source");
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.put("blockState", (Tag)NbtUtils.writeBlockState(this.movedState));
        md.putInt("facing", this.direction.get3DDataValue());
        md.putFloat("progress", this.progressO);
        md.putBoolean("extending", this.extending);
        md.putBoolean("source", this.isSourcePiston);
        return md;
    }
    
    public VoxelShape getCollisionShape(final BlockGetter bqz, final BlockPos fx) {
        VoxelShape dde4;
        if (!this.extending && this.isSourcePiston) {
            dde4 = ((StateHolder<O, BlockState>)this.movedState).<Comparable, Boolean>setValue((Property<Comparable>)PistonBaseBlock.EXTENDED, true).getCollisionShape(bqz, fx);
        }
        else {
            dde4 = Shapes.empty();
        }
        final Direction gc5 = (Direction)PistonMovingBlockEntity.NOCLIP.get();
        if (this.progress < 1.0 && gc5 == this.getMovementDirection()) {
            return dde4;
        }
        BlockState cee6;
        if (this.isSourcePiston()) {
            cee6 = (((StateHolder<O, BlockState>)Blocks.PISTON_HEAD.defaultBlockState()).setValue((Property<Comparable>)PistonHeadBlock.FACING, this.direction)).<Comparable, Boolean>setValue((Property<Comparable>)PistonHeadBlock.SHORT, this.extending != 1.0f - this.progress < 0.25f);
        }
        else {
            cee6 = this.movedState;
        }
        final float float7 = this.getExtendedProgress(this.progress);
        final double double8 = this.direction.getStepX() * float7;
        final double double9 = this.direction.getStepY() * float7;
        final double double10 = this.direction.getStepZ() * float7;
        return Shapes.or(dde4, cee6.getCollisionShape(bqz, fx).move(double8, double9, double10));
    }
    
    public long getLastTicked() {
        return this.lastTicked;
    }
    
    @Override
    public double getViewDistance() {
        return 68.0;
    }
    
    static {
        NOCLIP = ThreadLocal.withInitial(() -> null);
    }
}
