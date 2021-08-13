package net.minecraft.world.entity;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import java.util.Objects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import java.util.UUID;

public interface NeutralMob {
    int getRemainingPersistentAngerTime();
    
    void setRemainingPersistentAngerTime(final int integer);
    
    @Nullable
    UUID getPersistentAngerTarget();
    
    void setPersistentAngerTarget(@Nullable final UUID uUID);
    
    void startPersistentAngerTimer();
    
    default void addPersistentAngerSaveData(final CompoundTag md) {
        md.putInt("AngerTime", this.getRemainingPersistentAngerTime());
        if (this.getPersistentAngerTarget() != null) {
            md.putUUID("AngryAt", this.getPersistentAngerTarget());
        }
    }
    
    default void readPersistentAngerSaveData(final ServerLevel aag, final CompoundTag md) {
        this.setRemainingPersistentAngerTime(md.getInt("AngerTime"));
        if (!md.hasUUID("AngryAt")) {
            this.setPersistentAngerTarget(null);
            return;
        }
        final UUID uUID4 = md.getUUID("AngryAt");
        this.setPersistentAngerTarget(uUID4);
        final Entity apx5 = aag.getEntity(uUID4);
        if (apx5 == null) {
            return;
        }
        if (apx5 instanceof Mob) {
            this.setLastHurtByMob((LivingEntity)apx5);
        }
        if (apx5.getType() == EntityType.PLAYER) {
            this.setLastHurtByPlayer((Player)apx5);
        }
    }
    
    default void updatePersistentAnger(final ServerLevel aag, final boolean boolean2) {
        final LivingEntity aqj4 = this.getTarget();
        final UUID uUID5 = this.getPersistentAngerTarget();
        if ((aqj4 == null || aqj4.isDeadOrDying()) && uUID5 != null && aag.getEntity(uUID5) instanceof Mob) {
            this.stopBeingAngry();
            return;
        }
        if (aqj4 != null && !Objects.equals(uUID5, aqj4.getUUID())) {
            this.setPersistentAngerTarget(aqj4.getUUID());
            this.startPersistentAngerTimer();
        }
        if (this.getRemainingPersistentAngerTime() > 0 && (aqj4 == null || aqj4.getType() != EntityType.PLAYER || !boolean2)) {
            this.setRemainingPersistentAngerTime(this.getRemainingPersistentAngerTime() - 1);
            if (this.getRemainingPersistentAngerTime() == 0) {
                this.stopBeingAngry();
            }
        }
    }
    
    default boolean isAngryAt(final LivingEntity aqj) {
        return EntitySelector.ATTACK_ALLOWED.test(aqj) && ((aqj.getType() == EntityType.PLAYER && this.isAngryAtAllPlayers(aqj.level)) || aqj.getUUID().equals(this.getPersistentAngerTarget()));
    }
    
    default boolean isAngryAtAllPlayers(final Level bru) {
        return bru.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.isAngry() && this.getPersistentAngerTarget() == null;
    }
    
    default boolean isAngry() {
        return this.getRemainingPersistentAngerTime() > 0;
    }
    
    default void playerDied(final Player bft) {
        if (!bft.level.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
            return;
        }
        if (!bft.getUUID().equals(this.getPersistentAngerTarget())) {
            return;
        }
        this.stopBeingAngry();
    }
    
    default void forgetCurrentTargetAndRefreshUniversalAnger() {
        this.stopBeingAngry();
        this.startPersistentAngerTimer();
    }
    
    default void stopBeingAngry() {
        this.setLastHurtByMob(null);
        this.setPersistentAngerTarget(null);
        this.setTarget(null);
        this.setRemainingPersistentAngerTime(0);
    }
    
    void setLastHurtByMob(@Nullable final LivingEntity aqj);
    
    void setLastHurtByPlayer(@Nullable final Player bft);
    
    void setTarget(@Nullable final LivingEntity aqj);
    
    @Nullable
    LivingEntity getTarget();
}
