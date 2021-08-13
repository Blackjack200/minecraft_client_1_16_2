package net.minecraft.world.entity.monster;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.PowerableMob;

public class Creeper extends Monster implements PowerableMob {
    private static final EntityDataAccessor<Integer> DATA_SWELL_DIR;
    private static final EntityDataAccessor<Boolean> DATA_IS_POWERED;
    private static final EntityDataAccessor<Boolean> DATA_IS_IGNITED;
    private int oldSwell;
    private int swell;
    private int maxSwell;
    private int explosionRadius;
    private int droppedSkulls;
    
    public Creeper(final EntityType<? extends Creeper> aqb, final Level bru) {
        super(aqb, bru);
        this.maxSwell = 30;
        this.explosionRadius = 3;
    }
    
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Ocelot.class, 6.0f, 1.0, 1.2));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, Cat.class, 6.0f, 1.0, 1.2));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, new Class[0]));
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }
    
    public int getMaxFallDistance() {
        if (this.getTarget() == null) {
            return 3;
        }
        return 3 + (int)(this.getHealth() - 1.0f);
    }
    
    public boolean causeFallDamage(final float float1, final float float2) {
        final boolean boolean4 = super.causeFallDamage(float1, float2);
        this.swell += (int)(float1 * 1.5f);
        if (this.swell > this.maxSwell - 5) {
            this.swell = this.maxSwell - 5;
        }
        return boolean4;
    }
    
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Creeper.DATA_SWELL_DIR, -1);
        this.entityData.<Boolean>define(Creeper.DATA_IS_POWERED, false);
        this.entityData.<Boolean>define(Creeper.DATA_IS_IGNITED, false);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.entityData.<Boolean>get(Creeper.DATA_IS_POWERED)) {
            md.putBoolean("powered", true);
        }
        md.putShort("Fuse", (short)this.maxSwell);
        md.putByte("ExplosionRadius", (byte)this.explosionRadius);
        md.putBoolean("ignited", this.isIgnited());
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.entityData.<Boolean>set(Creeper.DATA_IS_POWERED, md.getBoolean("powered"));
        if (md.contains("Fuse", 99)) {
            this.maxSwell = md.getShort("Fuse");
        }
        if (md.contains("ExplosionRadius", 99)) {
            this.explosionRadius = md.getByte("ExplosionRadius");
        }
        if (md.getBoolean("ignited")) {
            this.ignite();
        }
    }
    
    public void tick() {
        if (this.isAlive()) {
            this.oldSwell = this.swell;
            if (this.isIgnited()) {
                this.setSwellDir(1);
            }
            final int integer2 = this.getSwellDir();
            if (integer2 > 0 && this.swell == 0) {
                this.playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 0.5f);
            }
            this.swell += integer2;
            if (this.swell < 0) {
                this.swell = 0;
            }
            if (this.swell >= this.maxSwell) {
                this.swell = this.maxSwell;
                this.explodeCreeper();
            }
        }
        super.tick();
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        return SoundEvents.CREEPER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CREEPER_DEATH;
    }
    
    protected void dropCustomDeathLoot(final DamageSource aph, final int integer, final boolean boolean3) {
        super.dropCustomDeathLoot(aph, integer, boolean3);
        final Entity apx5 = aph.getEntity();
        if (apx5 != this && apx5 instanceof Creeper) {
            final Creeper bcz6 = (Creeper)apx5;
            if (bcz6.canDropMobsSkull()) {
                bcz6.increaseDroppedSkulls();
                this.spawnAtLocation(Items.CREEPER_HEAD);
            }
        }
    }
    
    public boolean doHurtTarget(final Entity apx) {
        return true;
    }
    
    @Override
    public boolean isPowered() {
        return this.entityData.<Boolean>get(Creeper.DATA_IS_POWERED);
    }
    
    public float getSwelling(final float float1) {
        return Mth.lerp(float1, (float)this.oldSwell, (float)this.swell) / (this.maxSwell - 2);
    }
    
    public int getSwellDir() {
        return this.entityData.<Integer>get(Creeper.DATA_SWELL_DIR);
    }
    
    public void setSwellDir(final int integer) {
        this.entityData.<Integer>set(Creeper.DATA_SWELL_DIR, integer);
    }
    
    public void thunderHit(final ServerLevel aag, final LightningBolt aqi) {
        super.thunderHit(aag, aqi);
        this.entityData.<Boolean>set(Creeper.DATA_IS_POWERED, true);
    }
    
    protected InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (bly4.getItem() == Items.FLINT_AND_STEEL) {
            this.level.playSound(bft, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE, this.getSoundSource(), 1.0f, this.random.nextFloat() * 0.4f + 0.8f);
            if (!this.level.isClientSide) {
                this.ignite();
                bly4.<Player>hurtAndBreak(1, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(bft, aoq);
    }
    
    private void explodeCreeper() {
        if (!this.level.isClientSide) {
            final Explosion.BlockInteraction a2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            final float float3 = this.isPowered() ? 2.0f : 1.0f;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius * float3, a2);
            this.remove();
            this.spawnLingeringCloud();
        }
    }
    
    private void spawnLingeringCloud() {
        final Collection<MobEffectInstance> collection2 = this.getActiveEffects();
        if (!collection2.isEmpty()) {
            final AreaEffectCloud apw3 = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
            apw3.setRadius(2.5f);
            apw3.setRadiusOnUse(-0.5f);
            apw3.setWaitTime(10);
            apw3.setDuration(apw3.getDuration() / 2);
            apw3.setRadiusPerTick(-apw3.getRadius() / apw3.getDuration());
            for (final MobEffectInstance apr5 : collection2) {
                apw3.addEffect(new MobEffectInstance(apr5));
            }
            this.level.addFreshEntity(apw3);
        }
    }
    
    public boolean isIgnited() {
        return this.entityData.<Boolean>get(Creeper.DATA_IS_IGNITED);
    }
    
    public void ignite() {
        this.entityData.<Boolean>set(Creeper.DATA_IS_IGNITED, true);
    }
    
    public boolean canDropMobsSkull() {
        return this.isPowered() && this.droppedSkulls < 1;
    }
    
    public void increaseDroppedSkulls() {
        ++this.droppedSkulls;
    }
    
    static {
        DATA_SWELL_DIR = SynchedEntityData.<Integer>defineId(Creeper.class, EntityDataSerializers.INT);
        DATA_IS_POWERED = SynchedEntityData.<Boolean>defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
        DATA_IS_IGNITED = SynchedEntityData.<Boolean>defineId(Creeper.class, EntityDataSerializers.BOOLEAN);
    }
}
