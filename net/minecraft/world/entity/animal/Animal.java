package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.GameRules;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import java.util.Random;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.world.entity.AgableMob;

public abstract class Animal extends AgableMob {
    private int inLove;
    private UUID loveCause;
    
    protected Animal(final EntityType<? extends Animal> aqb, final Level bru) {
        super(aqb, bru);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
    }
    
    @Override
    protected void customServerAiStep() {
        if (this.getAge() != 0) {
            this.inLove = 0;
        }
        super.customServerAiStep();
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getAge() != 0) {
            this.inLove = 0;
        }
        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                final double double2 = this.random.nextGaussian() * 0.02;
                final double double3 = this.random.nextGaussian() * 0.02;
                final double double4 = this.random.nextGaussian() * 0.02;
                this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), double2, double3, double4);
            }
        }
    }
    
    @Override
    public boolean hurt(final DamageSource aph, final float float2) {
        if (this.isInvulnerableTo(aph)) {
            return false;
        }
        this.inLove = 0;
        return super.hurt(aph, float2);
    }
    
    @Override
    public float getWalkTargetValue(final BlockPos fx, final LevelReader brw) {
        if (brw.getBlockState(fx.below()).is(Blocks.GRASS_BLOCK)) {
            return 10.0f;
        }
        return brw.getBrightness(fx) - 0.5f;
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("InLove", this.inLove);
        if (this.loveCause != null) {
            md.putUUID("LoveCause", this.loveCause);
        }
    }
    
    @Override
    public double getMyRidingOffset() {
        return 0.14;
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.inLove = md.getInt("InLove");
        this.loveCause = (md.hasUUID("LoveCause") ? md.getUUID("LoveCause") : null);
    }
    
    public static boolean checkAnimalSpawnRules(final EntityType<? extends Animal> aqb, final LevelAccessor brv, final MobSpawnType aqm, final BlockPos fx, final Random random) {
        return brv.getBlockState(fx.below()).is(Blocks.GRASS_BLOCK) && brv.getRawBrightness(fx, 0) > 8;
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }
    
    @Override
    public boolean removeWhenFarAway(final double double1) {
        return false;
    }
    
    @Override
    protected int getExperienceReward(final Player bft) {
        return 1 + this.level.random.nextInt(3);
    }
    
    public boolean isFood(final ItemStack bly) {
        return bly.getItem() == Items.WHEAT;
    }
    
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (this.isFood(bly4)) {
            final int integer5 = this.getAge();
            if (!this.level.isClientSide && integer5 == 0 && this.canFallInLove()) {
                this.usePlayerItem(bft, bly4);
                this.setInLove(bft);
                return InteractionResult.SUCCESS;
            }
            if (this.isBaby()) {
                this.usePlayerItem(bft, bly4);
                this.ageUp((int)(-integer5 / 20 * 0.1f), true);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        return super.mobInteract(bft, aoq);
    }
    
    protected void usePlayerItem(final Player bft, final ItemStack bly) {
        if (!bft.abilities.instabuild) {
            bly.shrink(1);
        }
    }
    
    public boolean canFallInLove() {
        return this.inLove <= 0;
    }
    
    public void setInLove(@Nullable final Player bft) {
        this.inLove = 600;
        if (bft != null) {
            this.loveCause = bft.getUUID();
        }
        this.level.broadcastEntityEvent(this, (byte)18);
    }
    
    public void setInLoveTime(final int integer) {
        this.inLove = integer;
    }
    
    public int getInLoveTime() {
        return this.inLove;
    }
    
    @Nullable
    public ServerPlayer getLoveCause() {
        if (this.loveCause == null) {
            return null;
        }
        final Player bft2 = this.level.getPlayerByUUID(this.loveCause);
        if (bft2 instanceof ServerPlayer) {
            return (ServerPlayer)bft2;
        }
        return null;
    }
    
    public boolean isInLove() {
        return this.inLove > 0;
    }
    
    public void resetLove() {
        this.inLove = 0;
    }
    
    public boolean canMate(final Animal azw) {
        return azw != this && azw.getClass() == this.getClass() && this.isInLove() && azw.isInLove();
    }
    
    public void spawnChildFromBreeding(final ServerLevel aag, final Animal azw) {
        final AgableMob apv4 = this.getBreedOffspring(aag, azw);
        if (apv4 == null) {
            return;
        }
        ServerPlayer aah5 = this.getLoveCause();
        if (aah5 == null && azw.getLoveCause() != null) {
            aah5 = azw.getLoveCause();
        }
        if (aah5 != null) {
            aah5.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(aah5, this, azw, apv4);
        }
        this.setAge(6000);
        azw.setAge(6000);
        this.resetLove();
        azw.resetLove();
        apv4.setBaby(true);
        apv4.moveTo(this.getX(), this.getY(), this.getZ(), 0.0f, 0.0f);
        aag.addFreshEntityWithPassengers(apv4);
        aag.broadcastEntityEvent(this, (byte)18);
        if (aag.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            aag.addFreshEntity(new ExperienceOrb(aag, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 18) {
            for (int integer3 = 0; integer3 < 7; ++integer3) {
                final double double4 = this.random.nextGaussian() * 0.02;
                final double double5 = this.random.nextGaussian() * 0.02;
                final double double6 = this.random.nextGaussian() * 0.02;
                this.level.addParticle(ParticleTypes.HEART, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), double4, double5, double6);
            }
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
}
