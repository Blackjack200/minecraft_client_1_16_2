package net.minecraft.world.inventory;

import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.entity.player.Player;

public class MerchantResultSlot extends Slot {
    private final MerchantContainer slots;
    private final Player player;
    private int removeCount;
    private final Merchant merchant;
    
    public MerchantResultSlot(final Player bft, final Merchant bqr, final MerchantContainer bjc, final int integer4, final int integer5, final int integer6) {
        super(bjc, integer4, integer5, integer6);
        this.player = bft;
        this.merchant = bqr;
        this.slots = bjc;
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
    protected void onQuickCraft(final ItemStack bly, final int integer) {
        this.removeCount += integer;
        this.checkTakeAchievements(bly);
    }
    
    @Override
    protected void checkTakeAchievements(final ItemStack bly) {
        bly.onCraftedBy(this.player.level, this.player, this.removeCount);
        this.removeCount = 0;
    }
    
    @Override
    public ItemStack onTake(final Player bft, final ItemStack bly) {
        this.checkTakeAchievements(bly);
        final MerchantOffer bqs4 = this.slots.getActiveOffer();
        if (bqs4 != null) {
            final ItemStack bly2 = this.slots.getItem(0);
            final ItemStack bly3 = this.slots.getItem(1);
            if (bqs4.take(bly2, bly3) || bqs4.take(bly3, bly2)) {
                this.merchant.notifyTrade(bqs4);
                bft.awardStat(Stats.TRADED_WITH_VILLAGER);
                this.slots.setItem(0, bly2);
                this.slots.setItem(1, bly3);
            }
            this.merchant.overrideXp(this.merchant.getVillagerXp() + bqs4.getXp());
        }
        return bly;
    }
}
