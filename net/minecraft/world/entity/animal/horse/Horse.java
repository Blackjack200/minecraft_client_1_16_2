package net.minecraft.world.entity.animal.horse;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.Util;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;
import java.util.UUID;

public class Horse extends AbstractHorse {
    private static final UUID ARMOR_MODIFIER_UUID;
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE_VARIANT;
    
    public Horse(final EntityType<? extends Horse> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    @Override
    protected void randomizeAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.generateRandomMaxHealth());
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.generateRandomSpeed());
        this.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(this.generateRandomJumpStrength());
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Integer>define(Horse.DATA_ID_TYPE_VARIANT, 0);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putInt("Variant", this.getTypeVariant());
        if (!this.inventory.getItem(1).isEmpty()) {
            md.put("ArmorItem", (Tag)this.inventory.getItem(1).save(new CompoundTag()));
        }
    }
    
    public ItemStack getArmor() {
        return this.getItemBySlot(EquipmentSlot.CHEST);
    }
    
    private void setArmor(final ItemStack bly) {
        this.setItemSlot(EquipmentSlot.CHEST, bly);
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setTypeVariant(md.getInt("Variant"));
        if (md.contains("ArmorItem", 10)) {
            final ItemStack bly3 = ItemStack.of(md.getCompound("ArmorItem"));
            if (!bly3.isEmpty() && this.isArmor(bly3)) {
                this.inventory.setItem(1, bly3);
            }
        }
        this.updateContainerEquipment();
    }
    
    private void setTypeVariant(final int integer) {
        this.entityData.<Integer>set(Horse.DATA_ID_TYPE_VARIANT, integer);
    }
    
    private int getTypeVariant() {
        return this.entityData.<Integer>get(Horse.DATA_ID_TYPE_VARIANT);
    }
    
    private void setVariantAndMarkings(final Variant bbh, final Markings bbc) {
        this.setTypeVariant((bbh.getId() & 0xFF) | (bbc.getId() << 8 & 0xFF00));
    }
    
    public Variant getVariant() {
        return Variant.byId(this.getTypeVariant() & 0xFF);
    }
    
    public Markings getMarkings() {
        return Markings.byId((this.getTypeVariant() & 0xFF00) >> 8);
    }
    
    @Override
    protected void updateContainerEquipment() {
        if (this.level.isClientSide) {
            return;
        }
        super.updateContainerEquipment();
        this.setArmorEquipment(this.inventory.getItem(1));
        this.setDropChance(EquipmentSlot.CHEST, 0.0f);
    }
    
    private void setArmorEquipment(final ItemStack bly) {
        this.setArmor(bly);
        if (!this.level.isClientSide) {
            this.getAttribute(Attributes.ARMOR).removeModifier(Horse.ARMOR_MODIFIER_UUID);
            if (this.isArmor(bly)) {
                final int integer3 = ((HorseArmorItem)bly.getItem()).getProtection();
                if (integer3 != 0) {
                    this.getAttribute(Attributes.ARMOR).addTransientModifier(new AttributeModifier(Horse.ARMOR_MODIFIER_UUID, "Horse armor bonus", (double)integer3, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }
    
    @Override
    public void containerChanged(final Container aok) {
        final ItemStack bly3 = this.getArmor();
        super.containerChanged(aok);
        final ItemStack bly4 = this.getArmor();
        if (this.tickCount > 20 && this.isArmor(bly4) && bly3 != bly4) {
            this.playSound(SoundEvents.HORSE_ARMOR, 0.5f, 1.0f);
        }
    }
    
    @Override
    protected void playGallopSound(final SoundType cab) {
        super.playGallopSound(cab);
        if (this.random.nextInt(10) == 0) {
            this.playSound(SoundEvents.HORSE_BREATHE, cab.getVolume() * 0.6f, cab.getPitch());
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        super.getAmbientSound();
        return SoundEvents.HORSE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        super.getDeathSound();
        return SoundEvents.HORSE_DEATH;
    }
    
    @Nullable
    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource aph) {
        super.getHurtSound(aph);
        return SoundEvents.HORSE_HURT;
    }
    
    @Override
    protected SoundEvent getAngrySound() {
        super.getAngrySound();
        return SoundEvents.HORSE_ANGRY;
    }
    
    @Override
    public InteractionResult mobInteract(final Player bft, final InteractionHand aoq) {
        final ItemStack bly4 = bft.getItemInHand(aoq);
        if (!this.isBaby()) {
            if (this.isTamed() && bft.isSecondaryUseActive()) {
                this.openInventory(bft);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            if (this.isVehicle()) {
                return super.mobInteract(bft, aoq);
            }
        }
        if (!bly4.isEmpty()) {
            if (this.isFood(bly4)) {
                return this.fedFood(bft, bly4);
            }
            final InteractionResult aor5 = bly4.interactLivingEntity(bft, this, aoq);
            if (aor5.consumesAction()) {
                return aor5;
            }
            if (!this.isTamed()) {
                this.makeMad();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            final boolean boolean6 = !this.isBaby() && !this.isSaddled() && bly4.getItem() == Items.SADDLE;
            if (this.isArmor(bly4) || boolean6) {
                this.openInventory(bft);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        if (this.isBaby()) {
            return super.mobInteract(bft, aoq);
        }
        this.doPlayerRide(bft);
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }
    
    @Override
    public boolean canMate(final Animal azw) {
        return azw != this && (azw instanceof Donkey || azw instanceof Horse) && this.canParent() && ((AbstractHorse)azw).canParent();
    }
    
    @Override
    public AgableMob getBreedOffspring(final ServerLevel aag, final AgableMob apv) {
        AbstractHorse bay4;
        if (apv instanceof Donkey) {
            bay4 = EntityType.MULE.create(aag);
        }
        else {
            final Horse bba5 = (Horse)apv;
            bay4 = EntityType.HORSE.create(aag);
            final int integer7 = this.random.nextInt(9);
            Variant bbh6;
            if (integer7 < 4) {
                bbh6 = this.getVariant();
            }
            else if (integer7 < 8) {
                bbh6 = bba5.getVariant();
            }
            else {
                bbh6 = Util.<Variant>getRandom(Variant.values(), this.random);
            }
            final int integer8 = this.random.nextInt(5);
            Markings bbc8;
            if (integer8 < 2) {
                bbc8 = this.getMarkings();
            }
            else if (integer8 < 4) {
                bbc8 = bba5.getMarkings();
            }
            else {
                bbc8 = Util.<Markings>getRandom(Markings.values(), this.random);
            }
            ((Horse)bay4).setVariantAndMarkings(bbh6, bbc8);
        }
        this.setOffspringAttributes(apv, bay4);
        return bay4;
    }
    
    @Override
    public boolean canWearArmor() {
        return true;
    }
    
    @Override
    public boolean isArmor(final ItemStack bly) {
        return bly.getItem() instanceof HorseArmorItem;
    }
    
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(final ServerLevelAccessor bsh, final DifficultyInstance aop, final MobSpawnType aqm, @Nullable SpawnGroupData aqz, @Nullable final CompoundTag md) {
        Variant bbh7;
        if (aqz instanceof HorseGroupData) {
            bbh7 = ((HorseGroupData)aqz).variant;
        }
        else {
            bbh7 = Util.<Variant>getRandom(Variant.values(), this.random);
            aqz = new HorseGroupData(bbh7);
        }
        this.setVariantAndMarkings(bbh7, Util.<Markings>getRandom(Markings.values(), this.random));
        return super.finalizeSpawn(bsh, aop, aqm, aqz, md);
    }
    
    static {
        ARMOR_MODIFIER_UUID = UUID.fromString("556E1665-8B10-40C8-8F9D-CF9B1667F295");
        DATA_ID_TYPE_VARIANT = SynchedEntityData.<Integer>defineId(Horse.class, EntityDataSerializers.INT);
    }
    
    public static class HorseGroupData extends AgableMobGroupData {
        public final Variant variant;
        
        public HorseGroupData(final Variant bbh) {
            super(true);
            this.variant = bbh;
        }
    }
}
