package net.minecraft.world.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;

public abstract class Fireball extends AbstractHurtingProjectile implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK;
    
    public Fireball(final EntityType<? extends Fireball> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public Fireball(final EntityType<? extends Fireball> aqb, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final Level bru) {
        super(aqb, double2, double3, double4, double5, double6, double7, bru);
    }
    
    public Fireball(final EntityType<? extends Fireball> aqb, final LivingEntity aqj, final double double3, final double double4, final double double5, final Level bru) {
        super(aqb, aqj, double3, double4, double5, bru);
    }
    
    public void setItem(final ItemStack bly) {
        if (bly.getItem() != Items.FIRE_CHARGE || bly.hasTag()) {
            this.getEntityData().<ItemStack>set(Fireball.DATA_ITEM_STACK, (ItemStack)Util.<T>make((T)bly.copy(), (java.util.function.Consumer<T>)(bly -> bly.setCount(1))));
        }
    }
    
    protected ItemStack getItemRaw() {
        return this.getEntityData().<ItemStack>get(Fireball.DATA_ITEM_STACK);
    }
    
    @Override
    public ItemStack getItem() {
        final ItemStack bly2 = this.getItemRaw();
        return bly2.isEmpty() ? new ItemStack(Items.FIRE_CHARGE) : bly2;
    }
    
    @Override
    protected void defineSynchedData() {
        this.getEntityData().<ItemStack>define(Fireball.DATA_ITEM_STACK, ItemStack.EMPTY);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        final ItemStack bly3 = this.getItemRaw();
        if (!bly3.isEmpty()) {
            md.put("Item", (Tag)bly3.save(new CompoundTag()));
        }
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        super.readAdditionalSaveData(md);
        final ItemStack bly3 = ItemStack.of(md.getCompound("Item"));
        this.setItem(bly3);
    }
    
    static {
        DATA_ITEM_STACK = SynchedEntityData.<ItemStack>defineId(Fireball.class, EntityDataSerializers.ITEM_STACK);
    }
}
