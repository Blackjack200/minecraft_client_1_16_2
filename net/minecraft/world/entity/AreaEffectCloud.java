package net.minecraft.world.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.ParticleArgument;
import com.mojang.brigadier.StringReader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.util.Mth;
import java.util.Collection;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.core.particles.ParticleTypes;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import java.util.UUID;
import java.util.Map;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.List;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.syncher.EntityDataAccessor;
import org.apache.logging.log4j.Logger;

public class AreaEffectCloud extends Entity {
    private static final Logger LOGGER;
    private static final EntityDataAccessor<Float> DATA_RADIUS;
    private static final EntityDataAccessor<Integer> DATA_COLOR;
    private static final EntityDataAccessor<Boolean> DATA_WAITING;
    private static final EntityDataAccessor<ParticleOptions> DATA_PARTICLE;
    private Potion potion;
    private final List<MobEffectInstance> effects;
    private final Map<Entity, Integer> victims;
    private int duration;
    private int waitTime;
    private int reapplicationDelay;
    private boolean fixedColor;
    private int durationOnUse;
    private float radiusOnUse;
    private float radiusPerTick;
    private LivingEntity owner;
    private UUID ownerUUID;
    
    public AreaEffectCloud(final EntityType<? extends AreaEffectCloud> aqb, final Level bru) {
        super(aqb, bru);
        this.potion = Potions.EMPTY;
        this.effects = (List<MobEffectInstance>)Lists.newArrayList();
        this.victims = (Map<Entity, Integer>)Maps.newHashMap();
        this.duration = 600;
        this.waitTime = 20;
        this.reapplicationDelay = 20;
        this.noPhysics = true;
        this.setRadius(3.0f);
    }
    
    public AreaEffectCloud(final Level bru, final double double2, final double double3, final double double4) {
        this(EntityType.AREA_EFFECT_CLOUD, bru);
        this.setPos(double2, double3, double4);
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<Integer>define(AreaEffectCloud.DATA_COLOR, 0);
        this.getEntityData().<Float>define(AreaEffectCloud.DATA_RADIUS, 0.5f);
        this.getEntityData().<Boolean>define(AreaEffectCloud.DATA_WAITING, false);
        this.getEntityData().<ParticleOptions>define(AreaEffectCloud.DATA_PARTICLE, ParticleTypes.ENTITY_EFFECT);
    }
    
    public void setRadius(final float float1) {
        if (!this.level.isClientSide) {
            this.getEntityData().<Float>set(AreaEffectCloud.DATA_RADIUS, float1);
        }
    }
    
    @Override
    public void refreshDimensions() {
        final double double2 = this.getX();
        final double double3 = this.getY();
        final double double4 = this.getZ();
        super.refreshDimensions();
        this.setPos(double2, double3, double4);
    }
    
    public float getRadius() {
        return this.getEntityData().<Float>get(AreaEffectCloud.DATA_RADIUS);
    }
    
    public void setPotion(final Potion bnq) {
        this.potion = bnq;
        if (!this.fixedColor) {
            this.updateColor();
        }
    }
    
    private void updateColor() {
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.getEntityData().<Integer>set(AreaEffectCloud.DATA_COLOR, 0);
        }
        else {
            this.getEntityData().<Integer>set(AreaEffectCloud.DATA_COLOR, PotionUtils.getColor((Collection<MobEffectInstance>)PotionUtils.getAllEffects(this.potion, (Collection<MobEffectInstance>)this.effects)));
        }
    }
    
    public void addEffect(final MobEffectInstance apr) {
        this.effects.add(apr);
        if (!this.fixedColor) {
            this.updateColor();
        }
    }
    
    public int getColor() {
        return this.getEntityData().<Integer>get(AreaEffectCloud.DATA_COLOR);
    }
    
    public void setFixedColor(final int integer) {
        this.fixedColor = true;
        this.getEntityData().<Integer>set(AreaEffectCloud.DATA_COLOR, integer);
    }
    
    public ParticleOptions getParticle() {
        return this.getEntityData().<ParticleOptions>get(AreaEffectCloud.DATA_PARTICLE);
    }
    
    public void setParticle(final ParticleOptions hf) {
        this.getEntityData().<ParticleOptions>set(AreaEffectCloud.DATA_PARTICLE, hf);
    }
    
    protected void setWaiting(final boolean boolean1) {
        this.getEntityData().<Boolean>set(AreaEffectCloud.DATA_WAITING, boolean1);
    }
    
    public boolean isWaiting() {
        return this.getEntityData().<Boolean>get(AreaEffectCloud.DATA_WAITING);
    }
    
    public int getDuration() {
        return this.duration;
    }
    
    public void setDuration(final int integer) {
        this.duration = integer;
    }
    
    @Override
    public void tick() {
        super.tick();
        final boolean boolean2 = this.isWaiting();
        float float3 = this.getRadius();
        if (this.level.isClientSide) {
            final ParticleOptions hf4 = this.getParticle();
            if (boolean2) {
                if (this.random.nextBoolean()) {
                    for (int integer5 = 0; integer5 < 2; ++integer5) {
                        final float float4 = this.random.nextFloat() * 6.2831855f;
                        final float float5 = Mth.sqrt(this.random.nextFloat()) * 0.2f;
                        final float float6 = Mth.cos(float4) * float5;
                        final float float7 = Mth.sin(float4) * float5;
                        if (hf4.getType() == ParticleTypes.ENTITY_EFFECT) {
                            final int integer6 = this.random.nextBoolean() ? 16777215 : this.getColor();
                            final int integer7 = integer6 >> 16 & 0xFF;
                            final int integer8 = integer6 >> 8 & 0xFF;
                            final int integer9 = integer6 & 0xFF;
                            this.level.addAlwaysVisibleParticle(hf4, this.getX() + float6, this.getY(), this.getZ() + float7, integer7 / 255.0f, integer8 / 255.0f, integer9 / 255.0f);
                        }
                        else {
                            this.level.addAlwaysVisibleParticle(hf4, this.getX() + float6, this.getY(), this.getZ() + float7, 0.0, 0.0, 0.0);
                        }
                    }
                }
            }
            else {
                final float float8 = 3.1415927f * float3 * float3;
                for (int integer10 = 0; integer10 < float8; ++integer10) {
                    final float float5 = this.random.nextFloat() * 6.2831855f;
                    final float float6 = Mth.sqrt(this.random.nextFloat()) * float3;
                    final float float7 = Mth.cos(float5) * float6;
                    final float float9 = Mth.sin(float5) * float6;
                    if (hf4.getType() == ParticleTypes.ENTITY_EFFECT) {
                        final int integer7 = this.getColor();
                        final int integer8 = integer7 >> 16 & 0xFF;
                        final int integer9 = integer7 >> 8 & 0xFF;
                        final int integer11 = integer7 & 0xFF;
                        this.level.addAlwaysVisibleParticle(hf4, this.getX() + float7, this.getY(), this.getZ() + float9, integer8 / 255.0f, integer9 / 255.0f, integer11 / 255.0f);
                    }
                    else {
                        this.level.addAlwaysVisibleParticle(hf4, this.getX() + float7, this.getY(), this.getZ() + float9, (0.5 - this.random.nextDouble()) * 0.15, 0.009999999776482582, (0.5 - this.random.nextDouble()) * 0.15);
                    }
                }
            }
        }
        else {
            if (this.tickCount >= this.waitTime + this.duration) {
                this.remove();
                return;
            }
            final boolean boolean3 = this.tickCount < this.waitTime;
            if (boolean2 != boolean3) {
                this.setWaiting(boolean3);
            }
            if (boolean3) {
                return;
            }
            if (this.radiusPerTick != 0.0f) {
                float3 += this.radiusPerTick;
                if (float3 < 0.5f) {
                    this.remove();
                    return;
                }
                this.setRadius(float3);
            }
            if (this.tickCount % 5 == 0) {
                final Iterator<Map.Entry<Entity, Integer>> iterator5 = (Iterator<Map.Entry<Entity, Integer>>)this.victims.entrySet().iterator();
                while (iterator5.hasNext()) {
                    final Map.Entry<Entity, Integer> entry6 = (Map.Entry<Entity, Integer>)iterator5.next();
                    if (this.tickCount >= (int)entry6.getValue()) {
                        iterator5.remove();
                    }
                }
                final List<MobEffectInstance> list5 = (List<MobEffectInstance>)Lists.newArrayList();
                for (final MobEffectInstance apr7 : this.potion.getEffects()) {
                    list5.add(new MobEffectInstance(apr7.getEffect(), apr7.getDuration() / 4, apr7.getAmplifier(), apr7.isAmbient(), apr7.isVisible()));
                }
                list5.addAll((Collection)this.effects);
                if (list5.isEmpty()) {
                    this.victims.clear();
                }
                else {
                    final List<LivingEntity> list6 = this.level.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, this.getBoundingBox());
                    if (!list6.isEmpty()) {
                        for (final LivingEntity aqj8 : list6) {
                            if (!this.victims.containsKey(aqj8)) {
                                if (!aqj8.isAffectedByPotions()) {
                                    continue;
                                }
                                final double double9 = aqj8.getX() - this.getX();
                                final double double10 = aqj8.getZ() - this.getZ();
                                final double double11 = double9 * double9 + double10 * double10;
                                if (double11 > float3 * float3) {
                                    continue;
                                }
                                this.victims.put(aqj8, (this.tickCount + this.reapplicationDelay));
                                for (final MobEffectInstance apr8 : list5) {
                                    if (apr8.getEffect().isInstantenous()) {
                                        apr8.getEffect().applyInstantenousEffect(this, this.getOwner(), aqj8, apr8.getAmplifier(), 0.5);
                                    }
                                    else {
                                        aqj8.addEffect(new MobEffectInstance(apr8));
                                    }
                                }
                                if (this.radiusOnUse != 0.0f) {
                                    float3 += this.radiusOnUse;
                                    if (float3 < 0.5f) {
                                        this.remove();
                                        return;
                                    }
                                    this.setRadius(float3);
                                }
                                if (this.durationOnUse == 0) {
                                    continue;
                                }
                                this.duration += this.durationOnUse;
                                if (this.duration <= 0) {
                                    this.remove();
                                    return;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public void setRadiusOnUse(final float float1) {
        this.radiusOnUse = float1;
    }
    
    public void setRadiusPerTick(final float float1) {
        this.radiusPerTick = float1;
    }
    
    public void setWaitTime(final int integer) {
        this.waitTime = integer;
    }
    
    public void setOwner(@Nullable final LivingEntity aqj) {
        this.owner = aqj;
        this.ownerUUID = ((aqj == null) ? null : aqj.getUUID());
    }
    
    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            final Entity apx2 = ((ServerLevel)this.level).getEntity(this.ownerUUID);
            if (apx2 instanceof LivingEntity) {
                this.owner = (LivingEntity)apx2;
            }
        }
        return this.owner;
    }
    
    @Override
    protected void readAdditionalSaveData(final CompoundTag md) {
        this.tickCount = md.getInt("Age");
        this.duration = md.getInt("Duration");
        this.waitTime = md.getInt("WaitTime");
        this.reapplicationDelay = md.getInt("ReapplicationDelay");
        this.durationOnUse = md.getInt("DurationOnUse");
        this.radiusOnUse = md.getFloat("RadiusOnUse");
        this.radiusPerTick = md.getFloat("RadiusPerTick");
        this.setRadius(md.getFloat("Radius"));
        if (md.hasUUID("Owner")) {
            this.ownerUUID = md.getUUID("Owner");
        }
        if (md.contains("Particle", 8)) {
            try {
                this.setParticle(ParticleArgument.readParticle(new StringReader(md.getString("Particle"))));
            }
            catch (CommandSyntaxException commandSyntaxException3) {
                AreaEffectCloud.LOGGER.warn("Couldn't load custom particle {}", md.getString("Particle"), commandSyntaxException3);
            }
        }
        if (md.contains("Color", 99)) {
            this.setFixedColor(md.getInt("Color"));
        }
        if (md.contains("Potion", 8)) {
            this.setPotion(PotionUtils.getPotion(md));
        }
        if (md.contains("Effects", 9)) {
            final ListTag mj3 = md.getList("Effects", 10);
            this.effects.clear();
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                final MobEffectInstance apr5 = MobEffectInstance.load(mj3.getCompound(integer4));
                if (apr5 != null) {
                    this.addEffect(apr5);
                }
            }
        }
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        md.putInt("Age", this.tickCount);
        md.putInt("Duration", this.duration);
        md.putInt("WaitTime", this.waitTime);
        md.putInt("ReapplicationDelay", this.reapplicationDelay);
        md.putInt("DurationOnUse", this.durationOnUse);
        md.putFloat("RadiusOnUse", this.radiusOnUse);
        md.putFloat("RadiusPerTick", this.radiusPerTick);
        md.putFloat("Radius", this.getRadius());
        md.putString("Particle", this.getParticle().writeToString());
        if (this.ownerUUID != null) {
            md.putUUID("Owner", this.ownerUUID);
        }
        if (this.fixedColor) {
            md.putInt("Color", this.getColor());
        }
        if (this.potion != Potions.EMPTY && this.potion != null) {
            md.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }
        if (!this.effects.isEmpty()) {
            final ListTag mj3 = new ListTag();
            for (final MobEffectInstance apr5 : this.effects) {
                mj3.add(apr5.save(new CompoundTag()));
            }
            md.put("Effects", (Tag)mj3);
        }
    }
    
    @Override
    public void onSyncedDataUpdated(final EntityDataAccessor<?> us) {
        if (AreaEffectCloud.DATA_RADIUS.equals(us)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(us);
    }
    
    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
    
    @Override
    public EntityDimensions getDimensions(final Pose aqu) {
        return EntityDimensions.scalable(this.getRadius() * 2.0f, 0.5f);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DATA_RADIUS = SynchedEntityData.<Float>defineId(AreaEffectCloud.class, EntityDataSerializers.FLOAT);
        DATA_COLOR = SynchedEntityData.<Integer>defineId(AreaEffectCloud.class, EntityDataSerializers.INT);
        DATA_WAITING = SynchedEntityData.<Boolean>defineId(AreaEffectCloud.class, EntityDataSerializers.BOOLEAN);
        DATA_PARTICLE = SynchedEntityData.<ParticleOptions>defineId(AreaEffectCloud.class, EntityDataSerializers.PARTICLE);
    }
}
