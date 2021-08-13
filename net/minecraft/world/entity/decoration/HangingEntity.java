package net.minecraft.world.entity.decoration;

import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;

public abstract class HangingEntity extends Entity {
    protected static final Predicate<Entity> HANGING_ENTITY;
    private int checkInterval;
    protected BlockPos pos;
    protected Direction direction;
    
    protected HangingEntity(final EntityType<? extends HangingEntity> aqb, final Level bru) {
        super(aqb, bru);
        this.direction = Direction.SOUTH;
    }
    
    protected HangingEntity(final EntityType<? extends HangingEntity> aqb, final Level bru, final BlockPos fx) {
        this(aqb, bru);
        this.pos = fx;
    }
    
    @Override
    protected void defineSynchedData() {
    }
    
    protected void setDirection(final Direction gc) {
        Validate.notNull(gc);
        Validate.isTrue(gc.getAxis().isHorizontal());
        this.direction = gc;
        this.yRot = (float)(this.direction.get2DDataValue() * 90);
        this.yRotO = this.yRot;
        this.recalculateBoundingBox();
    }
    
    protected void recalculateBoundingBox() {
        if (this.direction == null) {
            return;
        }
        double double2 = this.pos.getX() + 0.5;
        double double3 = this.pos.getY() + 0.5;
        double double4 = this.pos.getZ() + 0.5;
        final double double5 = 0.46875;
        final double double6 = this.offs(this.getWidth());
        final double double7 = this.offs(this.getHeight());
        double2 -= this.direction.getStepX() * 0.46875;
        double4 -= this.direction.getStepZ() * 0.46875;
        double3 += double7;
        final Direction gc14 = this.direction.getCounterClockWise();
        double2 += double6 * gc14.getStepX();
        double4 += double6 * gc14.getStepZ();
        this.setPosRaw(double2, double3, double4);
        double double8 = this.getWidth();
        double double9 = this.getHeight();
        double double10 = this.getWidth();
        if (this.direction.getAxis() == Direction.Axis.Z) {
            double10 = 1.0;
        }
        else {
            double8 = 1.0;
        }
        double8 /= 32.0;
        double9 /= 32.0;
        double10 /= 32.0;
        this.setBoundingBox(new AABB(double2 - double8, double3 - double9, double4 - double10, double2 + double8, double3 + double9, double4 + double10));
    }
    
    private double offs(final int integer) {
        return (integer % 32 == 0) ? 0.5 : 0.0;
    }
    
    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            if (this.getY() < -64.0) {
                this.outOfWorld();
            }
            if (this.checkInterval++ == 100) {
                this.checkInterval = 0;
                if (!this.removed && !this.survives()) {
                    this.remove();
                    this.dropItem(null);
                }
            }
        }
    }
    
    public boolean survives() {
        if (!this.level.noCollision(this)) {
            return false;
        }
        final int integer2 = Math.max(1, this.getWidth() / 16);
        final int integer3 = Math.max(1, this.getHeight() / 16);
        final BlockPos fx4 = this.pos.relative(this.direction.getOpposite());
        final Direction gc5 = this.direction.getCounterClockWise();
        final BlockPos.MutableBlockPos a6 = new BlockPos.MutableBlockPos();
        for (int integer4 = 0; integer4 < integer2; ++integer4) {
            for (int integer5 = 0; integer5 < integer3; ++integer5) {
                final int integer6 = (integer2 - 1) / -2;
                final int integer7 = (integer3 - 1) / -2;
                a6.set(fx4).move(gc5, integer4 + integer6).move(Direction.UP, integer5 + integer7);
                final BlockState cee11 = this.level.getBlockState(a6);
                if (!cee11.getMaterial().isSolid() && !DiodeBlock.isDiode(cee11)) {
                    return false;
                }
            }
        }
        return this.level.getEntities(this, this.getBoundingBox(), HangingEntity.HANGING_ENTITY).isEmpty();
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public boolean skipAttackInteraction(final Entity apx) {
        if (apx instanceof Player) {
            final Player bft3 = (Player)apx;
            return !this.level.mayInteract(bft3, this.pos) || this.hurt(DamageSource.playerAttack(bft3), 0.0f);
        }
        return false;
    }
    
    @Override
    public Direction getDirection() {
        return this.direction;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (!this.removed && !this.level.isClientSide) {
            this.remove();
            this.markHurt();
            this.dropItem(aph.getEntity());
        }
        return true;
    }
    
    @Override
    public void move(final MoverType aqo, final Vec3 dck) {
        if (!this.level.isClientSide && !this.removed && dck.lengthSqr() > 0.0) {
            this.remove();
            this.dropItem(null);
        }
    }
    
    @Override
    public void push(final double double1, final double double2, final double double3) {
        if (!this.level.isClientSide && !this.removed && double1 * double1 + double2 * double2 + double3 * double3 > 0.0) {
            this.remove();
            this.dropItem(null);
        }
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        final BlockPos fx3 = this.getPos();
        md.putInt("TileX", fx3.getX());
        md.putInt("TileY", fx3.getY());
        md.putInt("TileZ", fx3.getZ());
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        this.pos = new BlockPos(md.getInt("TileX"), md.getInt("TileY"), md.getInt("TileZ"));
    }
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public abstract void dropItem(@Nullable final Entity apx);
    
    public abstract void playPlacementSound();
    
    @Override
    public ItemEntity spawnAtLocation(final ItemStack bly, final float float2) {
        final ItemEntity bcs4 = new ItemEntity(this.level, this.getX() + this.direction.getStepX() * 0.15f, this.getY() + float2, this.getZ() + this.direction.getStepZ() * 0.15f, bly);
        bcs4.setDefaultPickUpDelay();
        this.level.addFreshEntity(bcs4);
        return bcs4;
    }
    
    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }
    
    @Override
    public void setPos(final double double1, final double double2, final double double3) {
        this.pos = new BlockPos(double1, double2, double3);
        this.recalculateBoundingBox();
        this.hasImpulse = true;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    @Override
    public float rotate(final Rotation bzj) {
        if (this.direction.getAxis() != Direction.Axis.Y) {
            switch (bzj) {
                case CLOCKWISE_180: {
                    this.direction = this.direction.getOpposite();
                    break;
                }
                case COUNTERCLOCKWISE_90: {
                    this.direction = this.direction.getCounterClockWise();
                    break;
                }
                case CLOCKWISE_90: {
                    this.direction = this.direction.getClockWise();
                    break;
                }
            }
        }
        final float float3 = Mth.wrapDegrees(this.yRot);
        switch (bzj) {
            case CLOCKWISE_180: {
                return float3 + 180.0f;
            }
            case COUNTERCLOCKWISE_90: {
                return float3 + 90.0f;
            }
            case CLOCKWISE_90: {
                return float3 + 270.0f;
            }
            default: {
                return float3;
            }
        }
    }
    
    @Override
    public float mirror(final Mirror byd) {
        return this.rotate(byd.getRotation(this.direction));
    }
    
    @Override
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
    }
    
    @Override
    public void refreshDimensions() {
    }
    
    static {
        HANGING_ENTITY = (apx -> apx instanceof HangingEntity);
    }
}
