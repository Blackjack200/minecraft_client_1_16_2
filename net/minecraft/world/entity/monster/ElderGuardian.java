package net.minecraft.world.entity.monster;

import net.minecraft.world.entity.Entity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ElderGuardian extends Guardian {
    public static final float ELDER_SIZE_SCALE;
    
    public ElderGuardian(final EntityType<? extends ElderGuardian> aqb, final Level bru) {
        super(aqb, bru);
        this.setPersistenceRequired();
        if (this.randomStrollGoal != null) {
            this.randomStrollGoal.setInterval(400);
        }
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Guardian.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.30000001192092896).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.MAX_HEALTH, 80.0);
    }
    
    @Override
    public int getAttackDuration() {
        return 60;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_AMBIENT : SoundEvents.ELDER_GUARDIAN_AMBIENT_LAND;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_HURT : SoundEvents.ELDER_GUARDIAN_HURT_LAND;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_DEATH : SoundEvents.ELDER_GUARDIAN_DEATH_LAND;
    }
    
    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ELDER_GUARDIAN_FLOP;
    }
    
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        final int integer2 = 1200;
        if ((this.tickCount + this.getId()) % 1200 == 0) {
            final MobEffect app3 = MobEffects.DIG_SLOWDOWN;
            final List<ServerPlayer> list4 = ((ServerLevel)this.level).getPlayers((aah -> this.distanceToSqr(aah) < 2500.0 && aah.gameMode.isSurvival()));
            final int integer3 = 2;
            final int integer4 = 6000;
            final int integer5 = 1200;
            for (final ServerPlayer aah9 : list4) {
                if (!aah9.hasEffect(app3) || aah9.getEffect(app3).getAmplifier() < 2 || aah9.getEffect(app3).getDuration() < 1200) {
                    aah9.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, this.isSilent() ? 0.0f : 1.0f));
                    aah9.addEffect(new MobEffectInstance(app3, 6000, 2));
                }
            }
        }
        if (!this.hasRestriction()) {
            this.restrictTo(this.blockPosition(), 16);
        }
    }
    
    static {
        ELDER_SIZE_SCALE = EntityType.ELDER_GUARDIAN.getWidth() / EntityType.GUARDIAN.getWidth();
    }
}
