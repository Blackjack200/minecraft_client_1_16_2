package net.minecraft.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;

public class Slot {
    private final int slot;
    public final Container container;
    public int index;
    public final int x;
    public final int y;
    
    public Slot(final Container aok, final int integer2, final int integer3, final int integer4) {
        this.container = aok;
        this.slot = integer2;
        this.x = integer3;
        this.y = integer4;
    }
    
    public void onQuickCraft(final ItemStack bly1, final ItemStack bly2) {
        final int integer4 = bly2.getCount() - bly1.getCount();
        if (integer4 > 0) {
            this.onQuickCraft(bly2, integer4);
        }
    }
    
    protected void onQuickCraft(final ItemStack bly, final int integer) {
    }
    
    protected void onSwapCraft(final int integer) {
    }
    
    protected void checkTakeAchievements(final ItemStack bly) {
    }
    
    public ItemStack onTake(final Player bft, final ItemStack bly) {
        this.setChanged();
        return bly;
    }
    
    public boolean mayPlace(final ItemStack bly) {
        return true;
    }
    
    public ItemStack getItem() {
        return this.container.getItem(this.slot);
    }
    
    public boolean hasItem() {
        return !this.getItem().isEmpty();
    }
    
    public void set(final ItemStack bly) {
        this.container.setItem(this.slot, bly);
        this.setChanged();
    }
    
    public void setChanged() {
        this.container.setChanged();
    }
    
    public int getMaxStackSize() {
        return this.container.getMaxStackSize();
    }
    
    public int getMaxStackSize(final ItemStack bly) {
        return this.getMaxStackSize();
    }
    
    @Nullable
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return null;
    }
    
    public ItemStack remove(final int integer) {
        return this.container.removeItem(this.slot, integer);
    }
    
    public boolean mayPickup(final Player bft) {
        return true;
    }
    
    public boolean isActive() {
        return true;
    }
}
