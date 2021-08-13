package net.minecraft.world.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.Util;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.scores.Team;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import java.util.UUID;
import java.util.Optional;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.animal.Animal;

public abstract class TamableAnimal extends Animal {
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID;
    private boolean orderedToSit;
    
    protected TamableAnimal(final EntityType<? extends TamableAnimal> aqb, final Level bru) {
        super(aqb, bru);
        this.reassessTameGoals();
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(TamableAnimal.DATA_FLAGS_ID, (Byte)0);
        this.entityData.<Optional<UUID>>define(TamableAnimal.DATA_OWNERUUID_ID, (Optional<UUID>)Optional.empty());
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.getOwnerUUID() != null) {
            md.putUUID("Owner", this.getOwnerUUID());
        }
        md.putBoolean("Sitting", this.orderedToSit);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        UUID uUID3;
        if (md.hasUUID("Owner")) {
            uUID3 = md.getUUID("Owner");
        }
        else {
            final String string4 = md.getString("Owner");
            uUID3 = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), string4);
        }
        if (uUID3 != null) {
            try {
                this.setOwnerUUID(uUID3);
                this.setTame(true);
            }
            catch (Throwable throwable4) {
                this.setTame(false);
            }
        }
        this.setInSittingPose(this.orderedToSit = md.getBoolean("Sitting"));
    }
    
    @Override
    public boolean canBeLeashed(final Player bft) {
        return !this.isLeashed();
    }
    
    protected void spawnTamingParticles(final boolean boolean1) {
        ParticleOptions hf3 = ParticleTypes.HEART;
        if (!boolean1) {
            hf3 = ParticleTypes.SMOKE;
        }
        for (int integer4 = 0; integer4 < 7; ++integer4) {
            final double double5 = this.random.nextGaussian() * 0.02;
            final double double6 = this.random.nextGaussian() * 0.02;
            final double double7 = this.random.nextGaussian() * 0.02;
            this.level.addParticle(hf3, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), double5, double6, double7);
        }
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 7) {
            this.spawnTamingParticles(true);
        }
        else if (byte1 == 6) {
            this.spawnTamingParticles(false);
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    public boolean isTame() {
        return (this.entityData.<Byte>get(TamableAnimal.DATA_FLAGS_ID) & 0x4) != 0x0;
    }
    
    public void setTame(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(TamableAnimal.DATA_FLAGS_ID);
        if (boolean1) {
            this.entityData.<Byte>set(TamableAnimal.DATA_FLAGS_ID, (byte)(byte3 | 0x4));
        }
        else {
            this.entityData.<Byte>set(TamableAnimal.DATA_FLAGS_ID, (byte)(byte3 & 0xFFFFFFFB));
        }
        this.reassessTameGoals();
    }
    
    protected void reassessTameGoals() {
    }
    
    public boolean isInSittingPose() {
        return (this.entityData.<Byte>get(TamableAnimal.DATA_FLAGS_ID) & 0x1) != 0x0;
    }
    
    public void setInSittingPose(final boolean boolean1) {
        final byte byte3 = this.entityData.<Byte>get(TamableAnimal.DATA_FLAGS_ID);
        if (boolean1) {
            this.entityData.<Byte>set(TamableAnimal.DATA_FLAGS_ID, (byte)(byte3 | 0x1));
        }
        else {
            this.entityData.<Byte>set(TamableAnimal.DATA_FLAGS_ID, (byte)(byte3 & 0xFFFFFFFE));
        }
    }
    
    @Nullable
    public UUID getOwnerUUID() {
        return (UUID)this.entityData.<Optional<UUID>>get(TamableAnimal.DATA_OWNERUUID_ID).orElse(null);
    }
    
    public void setOwnerUUID(@Nullable final UUID uUID) {
        this.entityData.<Optional<UUID>>set(TamableAnimal.DATA_OWNERUUID_ID, (Optional<UUID>)Optional.ofNullable(uUID));
    }
    
    public void tame(final Player bft) {
        this.setTame(true);
        this.setOwnerUUID(bft.getUUID());
        if (bft instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)bft, this);
        }
    }
    
    @Nullable
    public LivingEntity getOwner() {
        try {
            final UUID uUID2 = this.getOwnerUUID();
            if (uUID2 == null) {
                return null;
            }
            return this.level.getPlayerByUUID(uUID2);
        }
        catch (IllegalArgumentException illegalArgumentException2) {
            return null;
        }
    }
    
    @Override
    public boolean canAttack(final LivingEntity aqj) {
        return !this.isOwnedBy(aqj) && super.canAttack(aqj);
    }
    
    public boolean isOwnedBy(final LivingEntity aqj) {
        return aqj == this.getOwner();
    }
    
    public boolean wantsToAttack(final LivingEntity aqj1, final LivingEntity aqj2) {
        return true;
    }
    
    @Override
    public Team getTeam() {
        if (this.isTame()) {
            final LivingEntity aqj2 = this.getOwner();
            if (aqj2 != null) {
                return aqj2.getTeam();
            }
        }
        return super.getTeam();
    }
    
    @Override
    public boolean isAlliedTo(final Entity apx) {
        if (this.isTame()) {
            final LivingEntity aqj3 = this.getOwner();
            if (apx == aqj3) {
                return true;
            }
            if (aqj3 != null) {
                return aqj3.isAlliedTo(apx);
            }
        }
        return super.isAlliedTo(apx);
    }
    
    @Override
    public void die(final DamageSource aph) {
        if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayer) {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
        super.die(aph);
    }
    
    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }
    
    public void setOrderedToSit(final boolean boolean1) {
        this.orderedToSit = boolean1;
    }
    
    static {
        DATA_FLAGS_ID = SynchedEntityData.<Byte>defineId(TamableAnimal.class, EntityDataSerializers.BYTE);
        DATA_OWNERUUID_ID = SynchedEntityData.<Optional<UUID>>defineId(TamableAnimal.class, EntityDataSerializers.OPTIONAL_UUID);
    }
}
