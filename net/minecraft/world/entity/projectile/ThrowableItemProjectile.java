package net.minecraft.world.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class ThrowableItemProjectile extends ThrowableProjectile implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;
    
    public ThrowableItemProjectile(final EntityType<? extends ThrowableItemProjectile> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public ThrowableItemProjectile(final EntityType<? extends ThrowableItemProjectile> aqb, final double double2, final double double3, final double double4, final Level bru) {
        super(aqb, double2, double3, double4, bru);
    }
    
    public ThrowableItemProjectile(final EntityType<? extends ThrowableItemProjectile> aqb, final LivingEntity aqj, final Level bru) {
        super(aqb, aqj, bru);
    }
    
    public void setItem(final ItemStack bly) {
        if (bly.getItem() != this.getDefaultItem() || bly.hasTag()) {
            this.getEntityData().<ItemStack>set(ThrowableItemProjectile.DATA_ITEM_STACK, (ItemStack)Util.<T>make((T)bly.copy(), (java.util.function.Consumer<T>)(bly -> bly.setCount(1))));
        }
    }
    
    protected abstract Item getDefaultItem();
    
    protected ItemStack getItemRaw() {
        return this.getEntityData().<ItemStack>get(ThrowableItemProjectile.DATA_ITEM_STACK);
    }
    
    @Override
    public ItemStack getItem() {
        final ItemStack bly2 = this.getItemRaw();
        return bly2.isEmpty() ? new ItemStack(this.getDefaultItem()) : bly2;
    }
    
    protected void defineSynchedData() {
        this.getEntityData().<ItemStack>define(ThrowableItemProjectile.DATA_ITEM_STACK, ItemStack.EMPTY);
    }
    
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        final ItemStack bly3 = this.getItemRaw();
        if (!bly3.isEmpty()) {
            md.put("Item", (Tag)bly3.save(new CompoundTag()));
        }
    }
    
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        final ItemStack bly3 = ItemStack.of(md.getCompound("Item"));
        this.setItem(bly3);
    }
    
    static {
        DATA_ITEM_STACK = SynchedEntityData.<ItemStack>defineId(ThrowableItemProjectile.class, EntityDataSerializers.ITEM_STACK);
    }
}
