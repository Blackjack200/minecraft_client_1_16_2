package net.minecraft.world.entity.ambient;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import java.time.temporal.TemporalField;
import java.time.temporal.ChronoField;
import java.time.LocalDate;
import java.util.Random;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Position;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Bat extends AmbientCreature {
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS;
    private static final TargetingConditions BAT_RESTING_TARGETING;
    private BlockPos targetPosition;
    
    public Bat(final EntityType<? extends Bat> aqb, final Level bru) {
        super(aqb, bru);
        this.setResting(true);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(Bat.DATA_ID_FLAGS, (Byte)0);
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.1f;
    }
    
    @Override
    protected float getVoicePitch() {
        return super.getVoicePitch() * 0.95f;
    }
    
    @Nullable
    public SoundEvent getAmbientSound() {
        if (this.isResting() && this.random.nextInt(4) != 0) {
            return null;
        }
        return SoundEvents.BAT_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.BAT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BAT_DEATH;
    }
    
    @Override
    public boolean isPushable() {
        return false;
    }
    
    @Override
    protected void doPush(final Entity apx) {
    }
    
    @Override
    protected void pushEntities() {
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }
    
    public boolean isResting() {
        return (this.entityData.<Byte>get(Bat.DATA_ID_FLAGS) & 0x1) != 0x0;
    }
    
    public void setResting(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(Bat.DATA_ID_FLAGS);
        if (boolean1) {
            this.entityData.<Byte>set(Bat.DATA_ID_FLAGS, (byte)(byte3 | 0x1));
        }
        else {
            this.entityData.<Byte>set(Bat.DATA_ID_FLAGS, (byte)(byte3 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.isResting()) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setPosRaw(this.getX(), Mth.floor(this.getY()) + 1.0 - this.getBbHeight(), this.getZ());
        }
        else {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }
    }
    
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        final BlockPos fx2 = this.blockPosition();
        final BlockPos fx3 = fx2.above();
        if (this.isResting()) {
            final boolean boolean4 = this.isSilent();
            if (this.level.getBlockState(fx3).isRedstoneConductor(this.level, fx2)) {
                if (this.random.nextInt(200) == 0) {
                    this.yHeadRot = (float)this.random.nextInt(360);
                }
                if (this.level.getNearestPlayer(Bat.BAT_RESTING_TARGETING, this) != null) {
                    this.setResting(false);
                    if (!boolean4) {
                        this.level.levelEvent(null, 1025, fx2, 0);
                    }
                }
            }
            else {
                this.setResting(false);
                if (!boolean4) {
                    this.level.levelEvent(null, 1025, fx2, 0);
                }
            }
        }
        else {
            if (this.targetPosition != null && (!this.level.isEmptyBlock(this.targetPosition) || this.targetPosition.getY() < 1)) {
                this.targetPosition = null;
            }
            if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerThan(this.position(), 2.0)) {
                this.targetPosition = new BlockPos(this.getX() + this.random.nextInt(7) - this.random.nextInt(7), this.getY() + this.random.nextInt(6) - 2.0, this.getZ() + this.random.nextInt(7) - this.random.nextInt(7));
            }
            final double double4 = this.targetPosition.getX() + 0.5 - this.getX();
            final double double5 = this.targetPosition.getY() + 0.1 - this.getY();
            final double double6 = this.targetPosition.getZ() + 0.5 - this.getZ();
            final Vec3 dck10 = this.getDeltaMovement();
            final Vec3 dck11 = dck10.add((Math.signum(double4) * 0.5 - dck10.x) * 0.10000000149011612, (Math.signum(double5) * 0.699999988079071 - dck10.y) * 0.10000000149011612, (Math.signum(double6) * 0.5 - dck10.z) * 0.10000000149011612);
            this.setDeltaMovement(dck11);
            final float float12 = (float)(Mth.atan2(dck11.z, dck11.x) * 57.2957763671875) - 90.0f;
            final float float13 = Mth.wrapDegrees(float12 - this.yRot);
            this.zza = 0.5f;
            this.yRot += float13;
            if (this.random.nextInt(100) == 0 && this.level.getBlockState(fx3).isRedstoneConductor(this.level, fx3)) {
                this.setResting(true);
            }
        }
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Override
    protected void checkFallDamage(final double double1, final boolean boolean2, final BlockState cee, final BlockPos fx) {
    }
    
    @Override
    public boolean isIgnoringBlockTriggers() {
        return true;
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        if (!this.level.isClientSide && this.isResting()) {
            this.setResting(false);
        }
        return super.hurt(aph, float2);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.entityData.<Byte>set(Bat.DATA_ID_FLAGS, md.getByte("BatFlags"));
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putByte("BatFlags", (byte)this.entityData.<Byte>get(Bat.DATA_ID_FLAGS));
    }
    
    public static boolean checkBatSpawnRules(final EntityType<Bat> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        if (fx.getY() >= brv.getSeaLevel()) {
            return false;
        }
        final int integer6 = brv.getMaxLocalRawBrightness(fx);
        int integer7 = 4;
        if (isHalloween()) {
            integer7 = 7;
        }
        else if (random.nextBoolean()) {
            return false;
        }
        return integer6 <= random.nextInt(integer7) && Mob.checkMobSpawnRules(aqb, brv, aqm, fx, random);
    }
    
    private static boolean isHalloween() {
        final LocalDate localDate1 = LocalDate.now();
        final int integer2 = localDate1.get((TemporalField)ChronoField.DAY_OF_MONTH);
        final int integer3 = localDate1.get((TemporalField)ChronoField.MONTH_OF_YEAR);
        return (integer3 == 10 && integer2 >= 20) || (integer3 == 11 && integer2 <= 3);
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height / 2.0f;
    }
    
    static {
        DATA_ID_FLAGS = SynchedEntityData.<Byte>defineId(Bat.class, EntityDataSerializers.BYTE);
        BAT_RESTING_TARGETING = new TargetingConditions().range(4.0).allowSameTeam();
    }
}
