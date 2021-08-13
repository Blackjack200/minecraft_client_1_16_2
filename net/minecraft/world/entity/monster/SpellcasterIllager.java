package net.minecraft.world.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class SpellcasterIllager extends AbstractIllager {
    private static final EntityDataAccessor<Byte> DATA_SPELL_CASTING_ID;
    protected int spellCastingTickCount;
    private IllagerSpell currentSpell;
    
    protected SpellcasterIllager(final EntityType<? extends SpellcasterIllager> aqb, final Level bru) {
        super(aqb, bru);
        this.currentSpell = IllagerSpell.NONE;
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Byte>define(SpellcasterIllager.DATA_SPELL_CASTING_ID, (Byte)0);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.spellCastingTickCount = md.getInt("SpellTicks");
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("SpellTicks", this.spellCastingTickCount);
    }
    
    @Override
    public IllagerArmPose getArmPose() {
        if (this.isCastingSpell()) {
            return IllagerArmPose.SPELLCASTING;
        }
        if (this.isCelebrating()) {
            return IllagerArmPose.CELEBRATING;
        }
        return IllagerArmPose.CROSSED;
    }
    
    public boolean isCastingSpell() {
        if (this.level.isClientSide) {
            return this.entityData.<Byte>get(SpellcasterIllager.DATA_SPELL_CASTING_ID) > 0;
        }
        return this.spellCastingTickCount > 0;
    }
    
    public void setIsCastingSpell(final IllagerSpell a) {
        this.currentSpell = a;
        this.entityData.<Byte>set(SpellcasterIllager.DATA_SPELL_CASTING_ID, (byte)a.id);
    }
    
    protected IllagerSpell getCurrentSpell() {
        if (!this.level.isClientSide) {
            return this.currentSpell;
        }
        return IllagerSpell.byId(this.entityData.<Byte>get(SpellcasterIllager.DATA_SPELL_CASTING_ID));
    }
    
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.spellCastingTickCount > 0) {
            --this.spellCastingTickCount;
        }
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide && this.isCastingSpell()) {
            final IllagerSpell a2 = this.getCurrentSpell();
            final double double3 = a2.spellColor[0];
            final double double4 = a2.spellColor[1];
            final double double5 = a2.spellColor[2];
            final float float9 = this.yBodyRot * 0.017453292f + Mth.cos(this.tickCount * 0.6662f) * 0.25f;
            final float float10 = Mth.cos(float9);
            final float float11 = Mth.sin(float9);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + float10 * 0.6, this.getY() + 1.8, this.getZ() + float11 * 0.6, double3, double4, double5);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - float10 * 0.6, this.getY() + 1.8, this.getZ() - float11 * 0.6, double3, double4, double5);
        }
    }
    
    protected int getSpellCastingTime() {
        return this.spellCastingTickCount;
    }
    
    protected abstract SoundEvent getCastingSoundEvent();
    
    static {
        DATA_SPELL_CASTING_ID = SynchedEntityData.<Byte>defineId(SpellcasterIllager.class, EntityDataSerializers.BYTE);
    }
    
    public class SpellcasterCastingSpellGoal extends Goal {
        public SpellcasterCastingSpellGoal() {
            this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE, (Enum)Flag.LOOK));
        }
        
        @Override
        public boolean canUse() {
            return SpellcasterIllager.this.getSpellCastingTime() > 0;
        }
        
        @Override
        public void start() {
            super.start();
            SpellcasterIllager.this.navigation.stop();
        }
        
        @Override
        public void stop() {
            super.stop();
            SpellcasterIllager.this.setIsCastingSpell(IllagerSpell.NONE);
        }
        
        @Override
        public void tick() {
            if (SpellcasterIllager.this.getTarget() != null) {
                SpellcasterIllager.this.getLookControl().setLookAt(SpellcasterIllager.this.getTarget(), (float)SpellcasterIllager.this.getMaxHeadYRot(), (float)SpellcasterIllager.this.getMaxHeadXRot());
            }
        }
    }
    
    public abstract class SpellcasterUseSpellGoal extends Goal {
        protected int attackWarmupDelay;
        protected int nextAttackTickCount;
        
        protected SpellcasterUseSpellGoal() {
        }
        
        @Override
        public boolean canUse() {
            final LivingEntity aqj2 = SpellcasterIllager.this.getTarget();
            return aqj2 != null && aqj2.isAlive() && !SpellcasterIllager.this.isCastingSpell() && SpellcasterIllager.this.tickCount >= this.nextAttackTickCount;
        }
        
        @Override
        public boolean canContinueToUse() {
            final LivingEntity aqj2 = SpellcasterIllager.this.getTarget();
            return aqj2 != null && aqj2.isAlive() && this.attackWarmupDelay > 0;
        }
        
        @Override
        public void start() {
            this.attackWarmupDelay = this.getCastWarmupTime();
            SpellcasterIllager.this.spellCastingTickCount = this.getCastingTime();
            this.nextAttackTickCount = SpellcasterIllager.this.tickCount + this.getCastingInterval();
            final SoundEvent adn2 = this.getSpellPrepareSound();
            if (adn2 != null) {
                SpellcasterIllager.this.playSound(adn2, 1.0f, 1.0f);
            }
            SpellcasterIllager.this.setIsCastingSpell(this.getSpell());
        }
        
        @Override
        public void tick() {
            --this.attackWarmupDelay;
            if (this.attackWarmupDelay == 0) {
                this.performSpellCasting();
                SpellcasterIllager.this.playSound(SpellcasterIllager.this.getCastingSoundEvent(), 1.0f, 1.0f);
            }
        }
        
        protected abstract void performSpellCasting();
        
        protected int getCastWarmupTime() {
            return 20;
        }
        
        protected abstract int getCastingTime();
        
        protected abstract int getCastingInterval();
        
        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();
        
        protected abstract IllagerSpell getSpell();
    }
    
    public enum IllagerSpell {
        NONE(0, 0.0, 0.0, 0.0), 
        SUMMON_VEX(1, 0.7, 0.7, 0.8), 
        FANGS(2, 0.4, 0.3, 0.35), 
        WOLOLO(3, 0.7, 0.5, 0.2), 
        DISAPPEAR(4, 0.3, 0.3, 0.8), 
        BLINDNESS(5, 0.1, 0.1, 0.2);
        
        private final int id;
        private final double[] spellColor;
        
        private IllagerSpell(final int integer3, final double double4, final double double5, final double double6) {
            this.id = integer3;
            this.spellColor = new double[] { double4, double5, double6 };
        }
        
        public static IllagerSpell byId(final int integer) {
            for (final IllagerSpell a5 : values()) {
                if (integer == a5.id) {
                    return a5;
                }
            }
            return IllagerSpell.NONE;
        }
    }
}
