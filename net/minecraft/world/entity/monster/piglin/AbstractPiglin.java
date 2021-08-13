package net.minecraft.world.entity.monster.piglin;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.item.TieredItem;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.monster.Monster;

public abstract class AbstractPiglin extends Monster {
    protected static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION;
    protected int timeInOverworld;
    
    public AbstractPiglin(final EntityType<? extends AbstractPiglin> aqb, final Level bru) {
        super(aqb, bru);
        this.timeInOverworld = 0;
        this.setCanPickUpLoot(true);
        this.applyOpenDoorsAbility();
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0f);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0f);
    }
    
    private void applyOpenDoorsAbility() {
        if (GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        }
    }
    
    protected abstract boolean canHunt();
    
    public void setImmuneToZombification(final boolean boolean1) {
        this.getEntityData().<Boolean>set(AbstractPiglin.DATA_IMMUNE_TO_ZOMBIFICATION, boolean1);
    }
    
    protected boolean isImmuneToZombification() {
        return this.getEntityData().<Boolean>get(AbstractPiglin.DATA_IMMUNE_TO_ZOMBIFICATION);
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(AbstractPiglin.DATA_IMMUNE_TO_ZOMBIFICATION, false);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.isImmuneToZombification()) {
            md.putBoolean("IsImmuneToZombification", true);
        }
        md.putInt("TimeInOverworld", this.timeInOverworld);
    }
    
    public double getMyRidingOffset() {
        return this.isBaby() ? -0.05 : -0.45;
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setImmuneToZombification(md.getBoolean("IsImmuneToZombification"));
        this.timeInOverworld = md.getInt("TimeInOverworld");
    }
    
    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isConverting()) {
            ++this.timeInOverworld;
        }
        else {
            this.timeInOverworld = 0;
        }
        if (this.timeInOverworld > 300) {
            this.playConvertedSound();
            this.finishConversion((ServerLevel)this.level);
        }
    }
    
    public boolean isConverting() {
        return !this.level.dimensionType().piglinSafe() && !this.isImmuneToZombification() && !this.isNoAi();
    }
    
    protected void finishConversion(final ServerLevel aag) {
        final ZombifiedPiglin bei3 = this.<ZombifiedPiglin>convertTo(EntityType.ZOMBIFIED_PIGLIN, true);
        if (bei3 != null) {
            bei3.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        }
    }
    
    public boolean isAdult() {
        return !this.isBaby();
    }
    
    public abstract PiglinArmPose getArmPose();
    
    @Nullable
    @Override
    public LivingEntity getTarget() {
        return (LivingEntity)this.brain.<LivingEntity>getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }
    
    protected boolean isHoldingMeleeWeapon() {
        return this.getMainHandItem().getItem() instanceof TieredItem;
    }
    
    @Override
    public void playAmbientSound() {
        if (PiglinAi.isIdle(this)) {
            super.playAmbientSound();
        }
    }
    
    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }
    
    protected abstract void playConvertedSound();
    
    static {
        DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.<Boolean>defineId(AbstractPiglin.class, EntityDataSerializers.BOOLEAN);
    }
}
