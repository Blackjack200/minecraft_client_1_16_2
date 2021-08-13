package net.minecraft.world.entity.animal;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Mob;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Pufferfish extends AbstractFish {
    private static final EntityDataAccessor<Integer> PUFF_STATE;
    private int inflateCounter;
    private int deflateTimer;
    private static final Predicate<LivingEntity> NO_SPECTATORS_AND_NO_WATER_MOB;
    
    public Pufferfish(final EntityType<? extends Pufferfish> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Pufferfish.PUFF_STATE, 0);
    }
    
    public int getPuffState() {
        return this.entityData.<Integer>get(Pufferfish.PUFF_STATE);
    }
    
    public void setPuffState(final int integer) {
        this.entityData.<Integer>set(Pufferfish.PUFF_STATE, integer);
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (Pufferfish.PUFF_STATE.equals(us)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("PuffState", this.getPuffState());
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setPuffState(md.getInt("PuffState"));
    }
    
    @Override
    protected ItemStack getBucketItemStack() {
        return new ItemStack(Items.PUFFERFISH_BUCKET);
    }
    
    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new PufferfishPuffGoal(this));
    }
    
    @Override
    public void tick() {
        if (!this.level.isClientSide && this.isAlive() && this.isEffectiveAi()) {
            if (this.inflateCounter > 0) {
                if (this.getPuffState() == 0) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(1);
                }
                else if (this.inflateCounter > 40 && this.getPuffState() == 1) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_UP, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(2);
                }
                ++this.inflateCounter;
            }
            else if (this.getPuffState() != 0) {
                if (this.deflateTimer > 60 && this.getPuffState() == 2) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(1);
                }
                else if (this.deflateTimer > 100 && this.getPuffState() == 1) {
                    this.playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, this.getSoundVolume(), this.getVoicePitch());
                    this.setPuffState(0);
                }
                ++this.deflateTimer;
            }
        }
        super.tick();
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.getPuffState() > 0) {
            final List<Mob> list2 = this.level.<Mob>getEntitiesOfClass((java.lang.Class<? extends Mob>)Mob.class, this.getBoundingBox().inflate(0.3), (java.util.function.Predicate<? super Mob>)Pufferfish.NO_SPECTATORS_AND_NO_WATER_MOB);
            for (final Mob aqk4 : list2) {
                if (aqk4.isAlive()) {
                    this.touch(aqk4);
                }
            }
        }
    }
    
    private void touch(final Mob aqk) {
        final int integer3 = this.getPuffState();
        if (aqk.hurt(DamageSource.mobAttack(this), (float)(1 + integer3))) {
            aqk.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * integer3, 0));
            this.playSound(SoundEvents.PUFFER_FISH_STING, 1.0f, 1.0f);
        }
    }
    
    @Override
    public void playerTouch(final Player bft) {
        final int integer3 = this.getPuffState();
        if (bft instanceof ServerPlayer && integer3 > 0 && bft.hurt(DamageSource.mobAttack(this), (float)(1 + integer3))) {
            if (!this.isSilent()) {
                ((ServerPlayer)bft).connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PUFFER_FISH_STING, 0.0f));
            }
            bft.addEffect(new MobEffectInstance(MobEffects.POISON, 60 * integer3, 0));
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PUFFER_FISH_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.PUFFER_FISH_HURT;
    }
    
    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.PUFFER_FISH_FLOP;
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return super.getDimensions(aqu).scale(getScale(this.getPuffState()));
    }
    
    private static float getScale(final int integer) {
        switch (integer) {
            case 1: {
                return 0.7f;
            }
            case 0: {
                return 0.5f;
            }
            default: {
                return 1.0f;
            }
        }
    }
    
    static {
        PUFF_STATE = SynchedEntityData.<Integer>defineId(Pufferfish.class, EntityDataSerializers.INT);
        NO_SPECTATORS_AND_NO_WATER_MOB = (aqj -> aqj != null && (!(aqj instanceof Player) || (!aqj.isSpectator() && !((Player)aqj).isCreative())) && aqj.getMobType() != MobType.WATER);
    }
    
    static class PufferfishPuffGoal extends Goal {
        private final Pufferfish fish;
        
        public PufferfishPuffGoal(final Pufferfish bam) {
            this.fish = bam;
        }
        
        @Override
        public boolean canUse() {
            final List<LivingEntity> list2 = this.fish.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.fish.getBoundingBox().inflate(2.0), (java.util.function.Predicate<? super LivingEntity>)Pufferfish.NO_SPECTATORS_AND_NO_WATER_MOB);
            return !list2.isEmpty();
        }
        
        @Override
        public void start() {
            this.fish.inflateCounter = 1;
            this.fish.deflateTimer = 0;
        }
        
        @Override
        public void stop() {
            this.fish.inflateCounter = 0;
        }
        
        @Override
        public boolean canContinueToUse() {
            final List<LivingEntity> list2 = this.fish.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.fish.getBoundingBox().inflate(2.0), (java.util.function.Predicate<? super LivingEntity>)Pufferfish.NO_SPECTATORS_AND_NO_WATER_MOB);
            return !list2.isEmpty();
        }
    }
}
