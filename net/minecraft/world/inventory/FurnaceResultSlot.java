package net.minecraft.world.inventory;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public class FurnaceResultSlot extends Slot {
    private final Player player;
    private int removeCount;
    
    public FurnaceResultSlot(final Player bft, final Container aok, final int integer3, final int integer4, final int integer5) {
        super(aok, integer3, integer4, integer5);
        this.player = bft;
    }
    
    @Override
    public boolean mayPlace(final ItemStack bly) {
        return false;
    }
    
    @Override
    public ItemStack remove(final int integer) {
        if (this.hasItem()) {
            this.removeCount += Math.min(integer, this.getItem().getCount());
        }
        return super.remove(integer);
    }
    
    @Override
    public ItemStack onTake(final Player bft, final ItemStack bly) {
        this.checkTakeAchievements(bly);
        super.onTake(bft, bly);
        return bly;
    }
    
    @Override
    protected void onQuickCraft(final ItemStack bly, final int integer) {
        this.removeCount += integer;
        this.checkTakeAchievements(bly);
    }
    
    @Override
    protected void checkTakeAchievements(final ItemStack bly) {
        bly.onCraftedBy(this.player.level, this.player, this.removeCount);
        if (!this.player.level.isClientSide && this.container instanceof AbstractFurnaceBlockEntity) {
            ((AbstractFurnaceBlockEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
        }
        this.removeCount = 0;
    }
}
