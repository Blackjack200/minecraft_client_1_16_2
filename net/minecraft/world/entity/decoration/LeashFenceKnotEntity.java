package net.minecraft.world.entity.decoration;

import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class LeashFenceKnotEntity extends HangingEntity {
    public LeashFenceKnotEntity(final EntityType<? extends LeashFenceKnotEntity> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public LeashFenceKnotEntity(final Level bru, final BlockPos fx) {
        super(EntityType.LEASH_KNOT, bru, fx);
        this.setPos(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5);
        final float float4 = 0.125f;
        final float float5 = 0.1875f;
        final float float6 = 0.25f;
        this.setBoundingBox(new AABB(this.getX() - 0.1875, this.getY() - 0.25 + 0.125, this.getZ() - 0.1875, this.getX() + 0.1875, this.getY() + 0.25 + 0.125, this.getZ() + 0.1875));
        this.forcedLoading = true;
    }
    
    @Override
    public void setPos(final double double1, final double double2, final double double3) {
        super.setPos(Mth.floor(double1) + 0.5, Mth.floor(double2) + 0.5, Mth.floor(double3) + 0.5);
    }
    
    @Override
    protected void recalculateBoundingBox() {
        this.setPosRaw(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5);
    }
    
    public void setDirection(final Direction gc) {
    }
    
    @Override
    public int getWidth() {
        return 9;
    }
    
    @Override
    public int getHeight() {
        return 9;
    }
    
    @Override
    protected float getEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return -0.0625f;
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        return double1 < 1024.0;
    }
    
    @Override
    public void dropItem(@Nullable final Entity apx) {
        this.playSound(SoundEvents.LEASH_KNOT_BREAK, 1.0f, 1.0f);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        if (this.level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        boolean boolean4 = false;
        final double double5 = 7.0;
        final List<Mob> list7 = this.level.<Mob>getEntitiesOfClass((java.lang.Class<? extends Mob>)Mob.class, new AABB(this.getX() - 7.0, this.getY() - 7.0, this.getZ() - 7.0, this.getX() + 7.0, this.getY() + 7.0, this.getZ() + 7.0));
        for (final Mob aqk9 : list7) {
            if (aqk9.getLeashHolder() == bft) {
                aqk9.setLeashedTo(this, true);
                boolean4 = true;
            }
        }
        if (!boolean4) {
            this.remove();
            if (bft.abilities.instabuild) {
                for (final Mob aqk9 : list7) {
                    if (aqk9.isLeashed() && aqk9.getLeashHolder() == this) {
                        aqk9.dropLeash(true, false);
                    }
                }
            }
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public boolean survives() {
        return this.level.getBlockState(this.pos).getBlock().is(BlockTags.FENCES);
    }
    
    public static LeashFenceKnotEntity getOrCreateKnot(final Level bru, final BlockPos fx) {
        final int integer3 = fx.getX();
        final int integer4 = fx.getY();
        final int integer5 = fx.getZ();
        final List<LeashFenceKnotEntity> list6 = bru.<LeashFenceKnotEntity>getEntitiesOfClass((java.lang.Class<? extends LeashFenceKnotEntity>)LeashFenceKnotEntity.class, new AABB(integer3 - 1.0, integer4 - 1.0, integer5 - 1.0, integer3 + 1.0, integer4 + 1.0, integer5 + 1.0));
        for (final LeashFenceKnotEntity bcn8 : list6) {
            if (bcn8.getPos().equals(fx)) {
                return bcn8;
            }
        }
        final LeashFenceKnotEntity bcn9 = new LeashFenceKnotEntity(bru, fx);
        bru.addFreshEntity(bcn9);
        bcn9.playPlacementSound();
        return bcn9;
    }
    
    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.LEASH_KNOT_PLACE, 1.0f, 1.0f);
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, this.getType(), 0, this.getPos());
    }
    
    @Override
    public Vec3 getRopeHoldPosition(final float float1) {
        return this.getPosition(float1).add(0.0, 0.2, 0.0);
    }
}
