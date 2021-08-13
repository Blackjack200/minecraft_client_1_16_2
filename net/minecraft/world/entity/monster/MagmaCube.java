package net.minecraft.world.entity.monster;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.Difficulty;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class MagmaCube extends Slime {
    public MagmaCube(final EntityType<? extends MagmaCube> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.20000000298023224);
    }
    
    public static boolean checkMagmaCubeSpawnRules(final EntityType<MagmaCube> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getDifficulty() != Difficulty.PEACEFUL;
    }
    
    @Override
    public boolean checkSpawnObstruction(final LevelReader brw) {
        return brw.isUnobstructed(this) && !brw.containsAnyLiquid(this.getBoundingBox());
    }
    
    @Override
    protected void setSize(final int integer, final boolean boolean2) {
        super.setSize(integer, boolean2);
        this.getAttribute(Attributes.ARMOR).setBaseValue(integer * 3);
    }
    
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    protected ParticleOptions getParticleType() {
        return ParticleTypes.FLAME;
    }
    
    @Override
    protected ResourceLocation getDefaultLootTable() {
        return this.isTiny() ? BuiltInLootTables.EMPTY : this.getType().getDefaultLootTable();
    }
    
    public boolean isOnFire() {
        return false;
    }
    
    @Override
    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }
    
    @Override
    protected void decreaseSquish() {
        this.targetSquish *= 0.9f;
    }
    
    @Override
    protected void jumpFromGround() {
        final Vec3 dck2 = this.getDeltaMovement();
        this.setDeltaMovement(dck2.x, this.getJumpPower() + this.getSize() * 0.1f, dck2.z);
        this.hasImpulse = true;
    }
    
    @Override
    protected void jumpInLiquid(final Tag<Fluid> aej) {
        if (aej == FluidTags.LAVA) {
            final Vec3 dck3 = this.getDeltaMovement();
            this.setDeltaMovement(dck3.x, 0.22f + this.getSize() * 0.05f, dck3.z);
            this.hasImpulse = true;
        }
        else {
            super.jumpInLiquid(aej);
        }
    }
    
    @Override
    public boolean causeFallDamage(final float float1, final float float2) {
        return false;
    }
    
    @Override
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }
    
    @Override
    protected float getAttackDamage() {
        return super.getAttackDamage() + 2.0f;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        if (this.isTiny()) {
            return SoundEvents.MAGMA_CUBE_HURT_SMALL;
        }
        return SoundEvents.MAGMA_CUBE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        if (this.isTiny()) {
            return SoundEvents.MAGMA_CUBE_DEATH_SMALL;
        }
        return SoundEvents.MAGMA_CUBE_DEATH;
    }
    
    @Override
    protected SoundEvent getSquishSound() {
        if (this.isTiny()) {
            return SoundEvents.MAGMA_CUBE_SQUISH_SMALL;
        }
        return SoundEvents.MAGMA_CUBE_SQUISH;
    }
    
    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.MAGMA_CUBE_JUMP;
    }
}
