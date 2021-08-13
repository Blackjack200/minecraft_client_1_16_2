package net.minecraft.world.entity.projectile;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.HitResult;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import java.util.List;
import net.minecraft.util.Mth;
import com.google.common.collect.Lists;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

public class ShulkerBullet extends Projectile {
    private Entity finalTarget;
    @Nullable
    private Direction currentMoveDirection;
    private int flightSteps;
    private double targetDeltaX;
    private double targetDeltaY;
    private double targetDeltaZ;
    @Nullable
    private UUID targetId;
    
    public ShulkerBullet(final EntityType<? extends ShulkerBullet> aqb, final Level bru) {
        super(aqb, bru);
        this.noPhysics = true;
    }
    
    public ShulkerBullet(final Level bru, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        this(EntityType.SHULKER_BULLET, bru);
        this.moveTo(double2, double3, double4, this.yRot, this.xRot);
        this.setDeltaMovement(double5, double6, double7);
    }
    
    public ShulkerBullet(final Level bru, final LivingEntity aqj, final Entity apx, final Direction.Axis a) {
        this(EntityType.SHULKER_BULLET, bru);
        this.setOwner(aqj);
        final BlockPos fx6 = aqj.blockPosition();
        final double double7 = fx6.getX() + 0.5;
        final double double8 = fx6.getY() + 0.5;
        final double double9 = fx6.getZ() + 0.5;
        this.moveTo(double7, double8, double9, this.yRot, this.xRot);
        this.finalTarget = apx;
        this.currentMoveDirection = Direction.UP;
        this.selectNextMoveDirection(a);
    }
    
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.finalTarget != null) {
            md.putUUID("Target", this.finalTarget.getUUID());
        }
        if (this.currentMoveDirection != null) {
            md.putInt("Dir", this.currentMoveDirection.get3DDataValue());
        }
        md.putInt("Steps", this.flightSteps);
        md.putDouble("TXD", this.targetDeltaX);
        md.putDouble("TYD", this.targetDeltaY);
        md.putDouble("TZD", this.targetDeltaZ);
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.flightSteps = md.getInt("Steps");
        this.targetDeltaX = md.getDouble("TXD");
        this.targetDeltaY = md.getDouble("TYD");
        this.targetDeltaZ = md.getDouble("TZD");
        if (md.contains("Dir", 99)) {
            this.currentMoveDirection = Direction.from3DDataValue(md.getInt("Dir"));
        }
        if (md.hasUUID("Target")) {
            this.targetId = md.getUUID("Target");
        }
    }
    
    @Override
    protected void defineSynchedData() {
    }
    
    private void setMoveDirection(@Nullable final Direction gc) {
        this.currentMoveDirection = gc;
    }
    
    private void selectNextMoveDirection(@Nullable final Direction.Axis a) {
        double double4 = 0.5;
        BlockPos fx3;
        if (this.finalTarget == null) {
            fx3 = this.blockPosition().below();
        }
        else {
            double4 = this.finalTarget.getBbHeight() * 0.5;
            fx3 = new BlockPos(this.finalTarget.getX(), this.finalTarget.getY() + double4, this.finalTarget.getZ());
        }
        double double5 = fx3.getX() + 0.5;
        double double6 = fx3.getY() + double4;
        double double7 = fx3.getZ() + 0.5;
        Direction gc12 = null;
        if (!fx3.closerThan(this.position(), 2.0)) {
            final BlockPos fx4 = this.blockPosition();
            final List<Direction> list14 = (List<Direction>)Lists.newArrayList();
            if (a != Direction.Axis.X) {
                if (fx4.getX() < fx3.getX() && this.level.isEmptyBlock(fx4.east())) {
                    list14.add(Direction.EAST);
                }
                else if (fx4.getX() > fx3.getX() && this.level.isEmptyBlock(fx4.west())) {
                    list14.add(Direction.WEST);
                }
            }
            if (a != Direction.Axis.Y) {
                if (fx4.getY() < fx3.getY() && this.level.isEmptyBlock(fx4.above())) {
                    list14.add(Direction.UP);
                }
                else if (fx4.getY() > fx3.getY() && this.level.isEmptyBlock(fx4.below())) {
                    list14.add(Direction.DOWN);
                }
            }
            if (a != Direction.Axis.Z) {
                if (fx4.getZ() < fx3.getZ() && this.level.isEmptyBlock(fx4.south())) {
                    list14.add(Direction.SOUTH);
                }
                else if (fx4.getZ() > fx3.getZ() && this.level.isEmptyBlock(fx4.north())) {
                    list14.add(Direction.NORTH);
                }
            }
            gc12 = Direction.getRandom(this.random);
            if (list14.isEmpty()) {
                for (int integer15 = 5; !this.level.isEmptyBlock(fx4.relative(gc12)) && integer15 > 0; gc12 = Direction.getRandom(this.random), --integer15) {}
            }
            else {
                gc12 = (Direction)list14.get(this.random.nextInt(list14.size()));
            }
            double5 = this.getX() + gc12.getStepX();
            double6 = this.getY() + gc12.getStepY();
            double7 = this.getZ() + gc12.getStepZ();
        }
        this.setMoveDirection(gc12);
        final double double8 = double5 - this.getX();
        final double double9 = double6 - this.getY();
        final double double10 = double7 - this.getZ();
        final double double11 = Mth.sqrt(double8 * double8 + double9 * double9 + double10 * double10);
        if (double11 == 0.0) {
            this.targetDeltaX = 0.0;
            this.targetDeltaY = 0.0;
            this.targetDeltaZ = 0.0;
        }
        else {
            this.targetDeltaX = double8 / double11 * 0.15;
            this.targetDeltaY = double9 / double11 * 0.15;
            this.targetDeltaZ = double10 / double11 * 0.15;
        }
        this.hasImpulse = true;
        this.flightSteps = 10 + this.random.nextInt(5) * 10;
    }
    
    @Override
    public void checkDespawn() {
        if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (this.finalTarget == null && this.targetId != null) {
                this.finalTarget = ((ServerLevel)this.level).getEntity(this.targetId);
                if (this.finalTarget == null) {
                    this.targetId = null;
                }
            }
            if (this.finalTarget != null && this.finalTarget.isAlive() && (!(this.finalTarget instanceof Player) || !((Player)this.finalTarget).isSpectator())) {
                this.targetDeltaX = Mth.clamp(this.targetDeltaX * 1.025, -1.0, 1.0);
                this.targetDeltaY = Mth.clamp(this.targetDeltaY * 1.025, -1.0, 1.0);
                this.targetDeltaZ = Mth.clamp(this.targetDeltaZ * 1.025, -1.0, 1.0);
                final Vec3 dck2 = this.getDeltaMovement();
                this.setDeltaMovement(dck2.add((this.targetDeltaX - dck2.x) * 0.2, (this.targetDeltaY - dck2.y) * 0.2, (this.targetDeltaZ - dck2.z) * 0.2));
            }
            else if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
            }
            final HitResult dci2 = ProjectileUtil.getHitResult(this, (Predicate<Entity>)this::canHitEntity);
            if (dci2.getType() != HitResult.Type.MISS) {
                this.onHit(dci2);
            }
        }
        this.checkInsideBlocks();
        final Vec3 dck2 = this.getDeltaMovement();
        this.setPos(this.getX() + dck2.x, this.getY() + dck2.y, this.getZ() + dck2.z);
        ProjectileUtil.rotateTowardsMovement(this, 0.5f);
        if (this.level.isClientSide) {
            this.level.addParticle(ParticleTypes.END_ROD, this.getX() - dck2.x, this.getY() - dck2.y + 0.15, this.getZ() - dck2.z, 0.0, 0.0, 0.0);
        }
        else if (this.finalTarget != null && !this.finalTarget.removed) {
            if (this.flightSteps > 0) {
                --this.flightSteps;
                if (this.flightSteps == 0) {
                    this.selectNextMoveDirection((this.currentMoveDirection == null) ? null : this.currentMoveDirection.getAxis());
                }
            }
            if (this.currentMoveDirection != null) {
                final BlockPos fx3 = this.blockPosition();
                final Direction.Axis a4 = this.currentMoveDirection.getAxis();
                if (this.level.loadedAndEntityCanStandOn(fx3.relative(this.currentMoveDirection), this)) {
                    this.selectNextMoveDirection(a4);
                }
                else {
                    final BlockPos fx4 = this.finalTarget.blockPosition();
                    if ((a4 == Direction.Axis.X && fx3.getX() == fx4.getX()) || (a4 == Direction.Axis.Z && fx3.getZ() == fx4.getZ()) || (a4 == Direction.Axis.Y && fx3.getY() == fx4.getY())) {
                        this.selectNextMoveDirection(a4);
                    }
                }
            }
        }
    }
    
    @Override
    protected boolean canHitEntity(final Entity apx) {
        return super.canHitEntity(apx) && !apx.noPhysics;
    }
    
    @Override
    public boolean isOnFire() {
        return false;
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        return double1 < 16384.0;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    protected void onHitEntity(final EntityHitResult dch) {
        super.onHitEntity(dch);
        final Entity apx3 = dch.getEntity();
        final Entity apx4 = this.getOwner();
        final LivingEntity aqj5 = (apx4 instanceof LivingEntity) ? ((LivingEntity)apx4) : null;
        final boolean boolean6 = apx3.hurt(DamageSource.indirectMobAttack(this, aqj5).setProjectile(), 4.0f);
        if (boolean6) {
            this.doEnchantDamageEffects(aqj5, apx3);
            if (apx3 instanceof LivingEntity) {
                ((LivingEntity)apx3).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200));
            }
        }
    }
    
    @Override
    protected void onHitBlock(final BlockHitResult dcg) {
        super.onHitBlock(dcg);
        ((ServerLevel)this.level).<SimpleParticleType>sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 2, 0.2, 0.2, 0.2, 0.0);
        this.playSound(SoundEvents.SHULKER_BULLET_HIT, 1.0f, 1.0f);
    }
    
    @Override
    protected void onHit(final HitResult dci) {
        super.onHit(dci);
        this.remove();
    }
    
    @Override
    public boolean isPickable() {
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (!this.level.isClientSide) {
            this.playSound(SoundEvents.SHULKER_BULLET_HURT, 1.0f, 1.0f);
            ((ServerLevel)this.level).<SimpleParticleType>sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
            this.remove();
        }
        return true;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
