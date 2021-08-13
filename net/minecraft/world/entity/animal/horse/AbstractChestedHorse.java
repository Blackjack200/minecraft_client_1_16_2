package net.minecraft.world.entity.animal.horse;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class AbstractChestedHorse extends AbstractHorse {
    private static final EntityDataAccessor<Boolean> DATA_ID_CHEST;
    
    protected AbstractChestedHorse(final EntityType<? extends AbstractChestedHorse> aqb, final Level bru) {
        super(aqb, bru);
        this.canGallop = false;
    }
    
    @Override
    protected void randomizeAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.generateRandomMaxHealth());
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.<Boolean>define(AbstractChestedHorse.DATA_ID_CHEST, false);
    }
    
    public static AttributeSupplier.Builder createBaseChestedHorseAttributes() {
        return AbstractHorse.createBaseHorseAttributes().add(Attributes.MOVEMENT_SPEED, 0.17499999701976776).add(Attributes.JUMP_STRENGTH, 0.5);
    }
    
    public boolean hasChest() {
        return this.entityData.<Boolean>get(AbstractChestedHorse.DATA_ID_CHEST);
    }
    
    public void setChest(final boolean boolean1) {
        this.entityData.<Boolean>set(AbstractChestedHorse.DATA_ID_CHEST, boolean1);
    }
    
    @Override
    protected int getInventorySize() {
        if (this.hasChest()) {
            return 17;
        }
        return super.getInventorySize();
    }
    
    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() - 0.25;
    }
    
    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.hasChest()) {
            if (!this.level.isClientSide) {
                this.spawnAtLocation(Blocks.CHEST);
            }
            this.setChest(false);
        }
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("ChestedHorse", this.hasChest());
        if (this.hasChest()) {
            final ListTag mj3 = new ListTag();
            for (int integer4 = 2; integer4 < this.inventory.getContainerSize(); ++integer4) {
                final ItemStack bly5 = this.inventory.getItem(integer4);
                if (!bly5.isEmpty()) {
                    final CompoundTag md2 = new CompoundTag();
                    md2.putByte("Slot", (byte)integer4);
                    bly5.save(md2);
                    mj3.add(md2);
                }
            }
            md.put("Items", (Tag)mj3);
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        this.setChest(md.getBoolean("ChestedHorse"));
        if (this.hasChest()) {
            final ListTag mj3 = md.getList("Items", 10);
            this.createInventory();
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                final CompoundTag md2 = mj3.getCompound(integer4);
                final int integer5 = md2.getByte("Slot") & 0xFF;
                if (integer5 >= 2 && integer5 < this.inventory.getContainerSize()) {
                    this.inventory.setItem(integer5, ItemStack.of(md2));
                }
            }
        }
        this.updateContainerEquipment();
    }
    
    @Override
    public boolean setSlot(final int integer, final ItemStack bly) {
        if (integer == 499) {
            if (this.hasChest() && bly.isEmpty()) {
                this.setChest(false);
                this.createInventory();
                return true;
            }
            if (!this.hasChest() && bly.getItem() == Blocks.CHEST.asItem()) {
                this.setChest(true);
                this.createInventory();
                return true;
            }
        }
        return super.setSlot(integer, bly);
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
            if (!this.isTamed()) {
                this.makeMad();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            if (!this.hasChest() && bly4.getItem() == Blocks.CHEST.asItem()) {
                this.setChest(true);
                this.playChestEquipsSound();
                if (!bft.abilities.instabuild) {
                    bly4.shrink(1);
                }
                this.createInventory();
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            if (!this.isBaby() && !this.isSaddled() && bly4.getItem() == Items.SADDLE) {
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
    
    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.DONKEY_CHEST, 1.0f, (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f);
    }
    
    public int getInventoryColumns() {
        return 5;
    }
    
    static {
        DATA_ID_CHEST = SynchedEntityData.<Boolean>defineId(AbstractChestedHorse.class, EntityDataSerializers.BOOLEAN);
    }
}
