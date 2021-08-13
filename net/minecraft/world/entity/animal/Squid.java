package net.minecraft.world.entity.animal;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.MoverType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Squid extends WaterAnimal {
    public float xBodyRot;
    public float xBodyRotO;
    public float zBodyRot;
    public float zBodyRotO;
    public float tentacleMovement;
    public float oldTentacleMovement;
    public float tentacleAngle;
    public float oldTentacleAngle;
    private float speed;
    private float tentacleSpeed;
    private float rotateSpeed;
    private float tx;
    private float ty;
    private float tz;
    
    public Squid(final EntityType<? extends Squid> aqb, final Level bru) {
        super(aqb, bru);
        this.random.setSeed((long)this.getId());
        this.tentacleSpeed = 1.0f / (this.random.nextFloat() + 1.0f) * 0.2f;
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SquidRandomMovementGoal(this));
        this.goalSelector.addGoal(1, new SquidFleeGoal());
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0);
    }
    
    @Override
    protected float getStandingEyeHeight(final Pose aqu, final EntityDimensions apy) {
        return apy.height * 0.5f;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.SQUID_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Override
    protected boolean isMovementNoisy() {
        return false;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;
        this.oldTentacleMovement = this.tentacleMovement;
        this.oldTentacleAngle = this.tentacleAngle;
        this.tentacleMovement += this.tentacleSpeed;
        if (this.tentacleMovement > 6.283185307179586) {
            if (this.level.isClientSide) {
                this.tentacleMovement = 6.2831855f;
            }
            else {
                this.tentacleMovement -= (float)6.283185307179586;
                if (this.random.nextInt(10) == 0) {
                    this.tentacleSpeed = 1.0f / (this.random.nextFloat() + 1.0f) * 0.2f;
                }
                this.level.broadcastEntityEvent(this, (byte)19);
            }
        }
        if (this.isInWaterOrBubble()) {
            if (this.tentacleMovement < 3.1415927f) {
                final float float2 = this.tentacleMovement / 3.1415927f;
                this.tentacleAngle = Mth.sin(float2 * float2 * 3.1415927f) * 3.1415927f * 0.25f;
                if (float2 > 0.75) {
                    this.speed = 1.0f;
                    this.rotateSpeed = 1.0f;
                }
                else {
                    this.rotateSpeed *= 0.8f;
                }
            }
            else {
                this.tentacleAngle = 0.0f;
                this.speed *= 0.9f;
                this.rotateSpeed *= 0.99f;
            }
            if (!this.level.isClientSide) {
                this.setDeltaMovement(this.tx * this.speed, this.ty * this.speed, this.tz * this.speed);
            }
            final Vec3 dck2 = this.getDeltaMovement();
            final float float3 = Mth.sqrt(Entity.getHorizontalDistanceSqr(dck2));
            this.yBodyRot += (-(float)Mth.atan2(dck2.x, dck2.z) * 57.295776f - this.yBodyRot) * 0.1f;
            this.yRot = this.yBodyRot;
            this.zBodyRot += (float)(3.141592653589793 * this.rotateSpeed * 1.5);
            this.xBodyRot += (-(float)Mth.atan2(float3, dck2.y) * 57.295776f - this.xBodyRot) * 0.1f;
        }
        else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * 3.1415927f * 0.25f;
            if (!this.level.isClientSide) {
                double double2 = this.getDeltaMovement().y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    double2 = 0.05 * (this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1);
                }
                else if (!this.isNoGravity()) {
                    double2 -= 0.08;
                }
                this.setDeltaMovement(0.0, double2 * 0.9800000190734863, 0.0);
            }
            this.xBodyRot += (float)((-90.0f - this.xBodyRot) * 0.02);
        }
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (super.hurt(aph, float2) && this.getLastHurtByMob() != null) {
            this.spawnInk();
            return true;
        }
        return false;
    }
    
    private Vec3 rotateVector(final Vec3 dck) {
        Vec3 dck2 = dck.xRot(this.xBodyRotO * 0.017453292f);
        dck2 = dck2.yRot(-this.yBodyRotO * 0.017453292f);
        return dck2;
    }
    
    private void spawnInk() {
        this.playSound(SoundEvents.SQUID_SQUIRT, this.getSoundVolume(), this.getVoicePitch());
        final Vec3 dck2 = this.rotateVector(new Vec3(0.0, -1.0, 0.0)).add(this.getX(), this.getY(), this.getZ());
        for (int integer3 = 0; integer3 < 30; ++integer3) {
            final Vec3 dck3 = this.rotateVector(new Vec3(this.random.nextFloat() * 0.6 - 0.3, -1.0, this.random.nextFloat() * 0.6 - 0.3));
            final Vec3 dck4 = dck3.scale(0.3 + this.random.nextFloat() * 2.0f);
            ((ServerLevel)this.level).<SimpleParticleType>sendParticles(ParticleTypes.SQUID_INK, dck2.x, dck2.y + 0.5, dck2.z, 0, dck4.x, dck4.y, dck4.z, 0.10000000149011612);
        }
    }
    
    @Override
    public void travel(final Vec3 dck) {
        this.move(MoverType.SELF, this.getDeltaMovement());
    }
    
    public static boolean checkSquidSpawnRules(final EntityType<Squid> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return fx.getY() > 45 && fx.getY() < brv.getSeaLevel();
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 19) {
            this.tentacleMovement = 0.0f;
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public void setMovementVector(final float float1, final float float2, final float float3) {
        this.tx = float1;
        this.ty = float2;
        this.tz = float3;
    }
    
    public boolean hasMovementVector() {
        return this.tx != 0.0f || this.ty != 0.0f || this.tz != 0.0f;
    }
    
    class SquidRandomMovementGoal extends Goal {
        private final Squid squid;
        
        public SquidRandomMovementGoal(final Squid bas2) {
            this.squid = bas2;
        }
        
        @Override
        public boolean canUse() {
            return true;
        }
        
        @Override
        public void tick() {
            final int integer2 = this.squid.getNoActionTime();
            if (integer2 > 100) {
                this.squid.setMovementVector(0.0f, 0.0f, 0.0f);
            }
            else if (this.squid.getRandom().nextInt(50) == 0 || !this.squid.wasTouchingWater || !this.squid.hasMovementVector()) {
                final float float3 = this.squid.getRandom().nextFloat() * 6.2831855f;
                final float float4 = Mth.cos(float3) * 0.2f;
                final float float5 = -0.1f + this.squid.getRandom().nextFloat() * 0.2f;
                final float float6 = Mth.sin(float3) * 0.2f;
                this.squid.setMovementVector(float4, float5, float6);
            }
        }
    }
    
    class SquidFleeGoal extends Goal {
        private int fleeTicks;
        
        private SquidFleeGoal() {
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = Squid.this.getLastHurtByMob();
            return Squid.this.isInWater() && aqj2 != null && Squid.this.distanceToSqr(aqj2) < 100.0;
        }
        
        @Override
        public void start() {
            this.fleeTicks = 0;
        }
        
        @Override
        public void tick() {
            ++this.fleeTicks;
            final LivingEntity aqj2 = Squid.this.getLastHurtByMob();
            if (aqj2 == null) {
                return;
            }
            Vec3 dck3 = new Vec3(Squid.this.getX() - aqj2.getX(), Squid.this.getY() - aqj2.getY(), Squid.this.getZ() - aqj2.getZ());
            final BlockState cee4 = Squid.this.level.getBlockState(new BlockPos(Squid.this.getX() + dck3.x, Squid.this.getY() + dck3.y, Squid.this.getZ() + dck3.z));
            final FluidState cuu5 = Squid.this.level.getFluidState(new BlockPos(Squid.this.getX() + dck3.x, Squid.this.getY() + dck3.y, Squid.this.getZ() + dck3.z));
            if (cuu5.is(FluidTags.WATER) || cee4.isAir()) {
                final double double6 = dck3.length();
                if (double6 > 0.0) {
                    dck3.normalize();
                    float float8 = 3.0f;
                    if (double6 > 5.0) {
                        float8 -= (float)((double6 - 5.0) / 5.0);
                    }
                    if (float8 > 0.0f) {
                        dck3 = dck3.scale(float8);
                    }
                }
                if (cee4.isAir()) {
                    dck3 = dck3.subtract(0.0, dck3.y, 0.0);
                }
                Squid.this.setMovementVector((float)dck3.x / 20.0f, (float)dck3.y / 20.0f, (float)dck3.z / 20.0f);
            }
            if (this.fleeTicks % 10 == 5) {
                Squid.this.level.addParticle(ParticleTypes.BUBBLE, Squid.this.getX(), Squid.this.getY(), Squid.this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
}
