package net.minecraft.world.entity.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import com.google.common.collect.Sets;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.Set;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.network.syncher.EntityDataAccessor;

public class Arrow extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ID_EFFECT_COLOR;
    private Potion potion;
    private final Set<MobEffectInstance> effects;
    private boolean fixedColor;
    
    public Arrow(final EntityType<? extends Arrow> aqb, final Level bru) {
        super(aqb, bru);
        this.potion = Potions.EMPTY;
        this.effects = (Set<MobEffectInstance>)Sets.newHashSet();
    }
    
    public Arrow(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.ARROW, double2, double3, double4, bru);
        this.potion = Potions.EMPTY;
        this.effects = (Set<MobEffectInstance>)Sets.newHashSet();
    }
    
    public Arrow(final Level bru, final LivingEntity aqj) {
        super(EntityType.ARROW, aqj, bru);
        this.potion = Potions.EMPTY;
        this.effects = (Set<MobEffectInstance>)Sets.newHashSet();
    }
    
    public void setEffectsFromItem(final ItemStack bly) {
        if (bly.getItem() == Items.TIPPED_ARROW) {
            this.potion = PotionUtils.getPotion(bly);
            final Collection<MobEffectInstance> collection3 = (Collection<MobEffectInstance>)PotionUtils.getCustomEffects(bly);
            if (!collection3.isEmpty()) {
                for (final MobEffectInstance apr5 : collection3) {
                    this.effects.add(new MobEffectInstance(apr5));
                }
            }
            final int integer4 = getCustomColor(bly);
            if (integer4 == -1) {
                this.updateColor();
            }
            else {
                this.setFixedColor(integer4);
            }
        }
        else if (bly.getItem() == Items.ARROW) {
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.<Integer>set(Arrow.ID_EFFECT_COLOR, -1);
        }
    }
    
    public static int getCustomColor(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        if (md2 != null && md2.contains("CustomPotionColor", 99)) {
            return md2.getInt("CustomPotionColor");
        }
        return -1;
    }
    
    private void updateColor() {
        this.fixedColor = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.entityData.<Integer>set(Arrow.ID_EFFECT_COLOR, -1);
        }
        else {
            this.entityData.<Integer>set(Arrow.ID_EFFECT_COLOR, PotionUtils.getColor((Collection<MobEffectInstance>)PotionUtils.getAllEffects(this.potion, (Collection<MobEffectInstance>)this.effects)));
        }
    }
    
    public void addEffect(final MobEffectInstance apr) {
        this.effects.add(apr);
        this.getEntityData().<Integer>set(Arrow.ID_EFFECT_COLOR, PotionUtils.getColor((Collection<MobEffectInstance>)PotionUtils.getAllEffects(this.potion, (Collection<MobEffectInstance>)this.effects)));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Arrow.ID_EFFECT_COLOR, -1);
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeParticle(1);
                }
            }
            else {
                this.makeParticle(2);
            }
        }
        else if (this.inGround && this.inGroundTime != 0 && !this.effects.isEmpty() && this.inGroundTime >= 600) {
            this.level.broadcastEntityEvent(this, (byte)0);
            this.potion = Potions.EMPTY;
            this.effects.clear();
            this.entityData.<Integer>set(Arrow.ID_EFFECT_COLOR, -1);
        }
    }
    
    private void makeParticle(final int integer) {
        final int integer2 = this.getColor();
        if (integer2 == -1 || integer <= 0) {
            return;
        }
        final double double4 = (integer2 >> 16 & 0xFF) / 255.0;
        final double double5 = (integer2 >> 8 & 0xFF) / 255.0;
        final double double6 = (integer2 >> 0 & 0xFF) / 255.0;
        for (int integer3 = 0; integer3 < integer; ++integer3) {
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), double4, double5, double6);
        }
    }
    
    public int getColor() {
        return this.entityData.<Integer>get(Arrow.ID_EFFECT_COLOR);
    }
    
    private void setFixedColor(final int integer) {
        this.fixedColor = true;
        this.entityData.<Integer>set(Arrow.ID_EFFECT_COLOR, integer);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        if (this.potion != Potions.EMPTY && this.potion != null) {
            md.putString("Potion", Registry.POTION.getKey(this.potion).toString());
        }
        if (this.fixedColor) {
            md.putInt("Color", this.getColor());
        }
        if (!this.effects.isEmpty()) {
            final ListTag mj3 = new ListTag();
            for (final MobEffectInstance apr5 : this.effects) {
                mj3.add(apr5.save(new CompoundTag()));
            }
            md.put("CustomPotionEffects", (Tag)mj3);
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        if (md.contains("Potion", 8)) {
            this.potion = PotionUtils.getPotion(md);
        }
        for (final MobEffectInstance apr4 : PotionUtils.getCustomEffects(md)) {
            this.addEffect(apr4);
        }
        if (md.contains("Color", 99)) {
            this.setFixedColor(md.getInt("Color"));
        }
        else {
            this.updateColor();
        }
    }
    
    @Override
    protected void doPostHurtEffects(final LivingEntity aqj) {
        super.doPostHurtEffects(aqj);
        for (final MobEffectInstance apr4 : this.potion.getEffects()) {
            aqj.addEffect(new MobEffectInstance(apr4.getEffect(), Math.max(apr4.getDuration() / 8, 1), apr4.getAmplifier(), apr4.isAmbient(), apr4.isVisible()));
        }
        if (!this.effects.isEmpty()) {
            for (final MobEffectInstance apr4 : this.effects) {
                aqj.addEffect(apr4);
            }
        }
    }
    
    @Override
    protected ItemStack getPickupItem() {
        if (this.effects.isEmpty() && this.potion == Potions.EMPTY) {
            return new ItemStack(Items.ARROW);
        }
        final ItemStack bly2 = new ItemStack(Items.TIPPED_ARROW);
        PotionUtils.setPotion(bly2, this.potion);
        PotionUtils.setCustomEffects(bly2, (Collection<MobEffectInstance>)this.effects);
        if (this.fixedColor) {
            bly2.getOrCreateTag().putInt("CustomPotionColor", this.getColor());
        }
        return bly2;
    }
    
    @Override
    public void handleEntityEvent(final byte byte1) {
        if (byte1 == 0) {
            final int integer3 = this.getColor();
            if (integer3 != -1) {
                final double double4 = (integer3 >> 16 & 0xFF) / 255.0;
                final double double5 = (integer3 >> 8 & 0xFF) / 255.0;
                final double double6 = (integer3 >> 0 & 0xFF) / 255.0;
                for (int integer4 = 0; integer4 < 20; ++integer4) {
                    this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), double4, double5, double6);
                }
            }
        }
        else {
            super.handleEntityEvent(byte1);
        }
    }
    
    static {
        ID_EFFECT_COLOR = SynchedEntityData.<Integer>defineId(Arrow.class, EntityDataSerializers.INT);
    }
}
