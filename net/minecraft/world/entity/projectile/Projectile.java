package net.minecraft.world.entity.projectile;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import java.util.Iterator;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.world.entity.Entity;

public abstract class Projectile extends Entity {
    private UUID ownerUUID;
    private int ownerNetworkId;
    private boolean leftOwner;
    
    Projectile(final EntityType<? extends Projectile> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public void setOwner(@Nullable final Entity apx) {
        if (apx != null) {
            this.ownerUUID = apx.getUUID();
            this.ownerNetworkId = apx.getId();
        }
    }
    
    @Nullable
    public Entity getOwner() {
        if (this.ownerUUID != null && this.level instanceof ServerLevel) {
            return ((ServerLevel)this.level).getEntity(this.ownerUUID);
        }
        if (this.ownerNetworkId != 0) {
            return this.level.getEntity(this.ownerNetworkId);
        }
        return null;
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        if (this.ownerUUID != null) {
            md.putUUID("Owner", this.ownerUUID);
        }
        if (this.leftOwner) {
            md.putBoolean("LeftOwner", true);
        }
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        if (md.hasUUID("Owner")) {
            this.ownerUUID = md.getUUID("Owner");
        }
        this.leftOwner = md.getBoolean("LeftOwner");
    }
    
    @Override
    public void tick() {
        if (!this.leftOwner) {
            this.leftOwner = this.checkLeftOwner();
        }
        super.tick();
    }
    
    private boolean checkLeftOwner() {
        final Entity apx2 = this.getOwner();
        if (apx2 != null) {
            for (final Entity apx3 : this.level.getEntities(this, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0), (apx -> !apx.isSpectator() && apx.isPickable()))) {
                if (apx3.getRootVehicle() == apx2.getRootVehicle()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void shoot(final double double1, final double double2, final double double3, final float float4, final float float5) {
        final Vec3 dck10 = new Vec3(double1, double2, double3).normalize().add(this.random.nextGaussian() * 0.007499999832361937 * float5, this.random.nextGaussian() * 0.007499999832361937 * float5, this.random.nextGaussian() * 0.007499999832361937 * float5).scale(float4);
        this.setDeltaMovement(dck10);
        final float float6 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck10));
        this.yRot = (float)(Mth.atan2(dck10.x, dck10.z) * 57.2957763671875);
        this.xRot = (float)(Mth.atan2(dck10.y, float6) * 57.2957763671875);
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }
    
    public void shootFromRotation(final Entity apx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final float float7 = -Mth.sin(float3 * 0.017453292f) * Mth.cos(float2 * 0.017453292f);
        final float float8 = -Mth.sin((float2 + float4) * 0.017453292f);
        final float float9 = Mth.cos(float3 * 0.017453292f) * Mth.cos(float2 * 0.017453292f);
        this.shoot(float7, float8, float9, float5, float6);
        final Vec3 dck11 = apx.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(dck11.x, apx.isOnGround() ? 0.0 : dck11.y, dck11.z));
    }
    
    protected void onHit(final HitResult dci) {
        final HitResult.Type a3 = dci.getType();
        if (a3 == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult)dci);
        }
        else if (a3 == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult)dci);
        }
    }
    
    protected void onHitEntity(final EntityHitResult dch) {
    }
    
    protected void onHitBlock(final BlockHitResult dcg) {
        final BlockHitResult dcg2 = dcg;
        final BlockState cee4 = this.level.getBlockState(dcg2.getBlockPos());
        cee4.onProjectileHit(this.level, cee4, dcg2, this);
    }
    
    @Override
    public void lerpMotion(final double double1, final double double2, final double double3) {
        this.setDeltaMovement(double1, double2, double3);
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            final float float8 = Mth.sqrt(double1 * double1 + double3 * double3);
            this.xRot = (float)(Mth.atan2(double2, float8) * 57.2957763671875);
            this.yRot = (float)(Mth.atan2(double1, double3) * 57.2957763671875);
            this.xRotO = this.xRot;
            this.yRotO = this.yRot;
            this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
        }
    }
    
    protected boolean canHitEntity(final Entity apx) {
        if (apx.isSpectator() || !apx.isAlive() || !apx.isPickable()) {
            return false;
        }
        final Entity apx2 = this.getOwner();
        return apx2 == null || this.leftOwner || !apx2.isPassengerOfSameVehicle(apx);
    }
    
    protected void updateRotation() {
        final Vec3 dck2 = this.getDeltaMovement();
        final float float3 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck2));
        this.xRot = lerpRotation(this.xRotO, (float)(Mth.atan2(dck2.y, float3) * 57.2957763671875));
        this.yRot = lerpRotation(this.yRotO, (float)(Mth.atan2(dck2.x, dck2.z) * 57.2957763671875));
    }
    
    protected static float lerpRotation(float float1, final float float2) {
        while (float2 - float1 < -180.0f) {
            float1 -= 360.0f;
        }
        while (float2 - float1 >= 180.0f) {
            float1 += 360.0f;
        }
        return Mth.lerp(0.2f, float1, float2);
    }
}
