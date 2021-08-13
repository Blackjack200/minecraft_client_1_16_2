package net.minecraft.world.entity;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.GameRules;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import net.minecraft.advancements.CriteriaTriggers;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;

public class LightningBolt extends Entity {
    private int life;
    public long seed;
    private int flashes;
    private boolean visualOnly;
    @Nullable
    private ServerPlayer cause;
    
    public LightningBolt(final EntityType<? extends LightningBolt> aqb, final Level bru) {
        super(aqb, bru);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }
    
    public void setVisualOnly(final boolean boolean1) {
        this.visualOnly = boolean1;
    }
    
    @Override
    public SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }
    
    public void setCause(@Nullable final ServerPlayer aah) {
        this.cause = aah;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.life == 2) {
            final Difficulty aoo2 = this.level.getDifficulty();
            if (aoo2 == Difficulty.NORMAL || aoo2 == Difficulty.HARD) {
                this.spawnFire(4);
            }
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0f, 0.8f + this.random.nextFloat() * 0.2f);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0f, 0.5f + this.random.nextFloat() * 0.2f);
        }
        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.remove();
            }
            else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
                this.spawnFire(0);
            }
        }
        if (this.life >= 0) {
            if (!(this.level instanceof ServerLevel)) {
                this.level.setSkyFlashTime(2);
            }
            else if (!this.visualOnly) {
                final double double2 = 3.0;
                final List<Entity> list4 = this.level.getEntities(this, new AABB(this.getX() - 3.0, this.getY() - 3.0, this.getZ() - 3.0, this.getX() + 3.0, this.getY() + 6.0 + 3.0, this.getZ() + 3.0), Entity::isAlive);
                for (final Entity apx6 : list4) {
                    apx6.thunderHit((ServerLevel)this.level, this);
                }
                if (this.cause != null) {
                    CriteriaTriggers.CHANNELED_LIGHTNING.trigger(this.cause, list4);
                }
            }
        }
    }
    
    private void spawnFire(final int integer) {
        if (this.visualOnly || this.level.isClientSide || !this.level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }
        final BlockPos fx3 = this.blockPosition();
        BlockState cee4 = BaseFireBlock.getState(this.level, fx3);
        if (this.level.getBlockState(fx3).isAir() && cee4.canSurvive(this.level, fx3)) {
            this.level.setBlockAndUpdate(fx3, cee4);
        }
        for (int integer2 = 0; integer2 < integer; ++integer2) {
            final BlockPos fx4 = fx3.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);
            cee4 = BaseFireBlock.getState(this.level, fx4);
            if (this.level.getBlockState(fx4).isAir() && cee4.canSurvive(this.level, fx4)) {
                this.level.setBlockAndUpdate(fx4, cee4);
            }
        }
    }
    
    @Override
    public boolean shouldRenderAtSqrDistance(final double double1) {
        final double double2 = 64.0 * getViewScale();
        return double1 < double2 * double2;
    }
    
    @Override
    protected void defineSynchedData() {
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
