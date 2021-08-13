package net.minecraft.world.inventory;

import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.entity.player.Player;
import java.util.List;
import net.minecraft.world.ContainerHelper;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.Container;

public class MerchantContainer implements Container {
    private final Merchant merchant;
    private final NonNullList<ItemStack> itemStacks;
    @Nullable
    private MerchantOffer activeOffer;
    private int selectionHint;
    private int futureXp;
    
    public MerchantContainer(final Merchant bqr) {
        this.itemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
        this.merchant = bqr;
    }
    
    public int getContainerSize() {
        return this.itemStacks.size();
    }
    
    public boolean isEmpty() {
        for (final ItemStack bly3 : this.itemStacks) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public ItemStack getItem(final int integer) {
        return this.itemStacks.get(integer);
    }
    
    public ItemStack removeItem(final int integer1, final int integer2) {
        final ItemStack bly4 = this.itemStacks.get(integer1);
        if (integer1 == 2 && !bly4.isEmpty()) {
            return ContainerHelper.removeItem((List<ItemStack>)this.itemStacks, integer1, bly4.getCount());
        }
        final ItemStack bly5 = ContainerHelper.removeItem((List<ItemStack>)this.itemStacks, integer1, integer2);
        if (!bly5.isEmpty() && this.isPaymentSlot(integer1)) {
            this.updateSellItem();
        }
        return bly5;
    }
    
    private boolean isPaymentSlot(final int integer) {
        return integer == 0 || integer == 1;
    }
    
    public ItemStack removeItemNoUpdate(final int integer) {
        return ContainerHelper.takeItem((List<ItemStack>)this.itemStacks, integer);
    }
    
    public void setItem(final int integer, final ItemStack bly) {
        this.itemStacks.set(integer, bly);
        if (!bly.isEmpty() && bly.getCount() > this.getMaxStackSize()) {
            bly.setCount(this.getMaxStackSize());
        }
        if (this.isPaymentSlot(integer)) {
            this.updateSellItem();
        }
    }
    
    public boolean stillValid(final Player bft) {
        return this.merchant.getTradingPlayer() == bft;
    }
    
    public void setChanged() {
        this.updateSellItem();
    }
    
    public void updateSellItem() {
        this.activeOffer = null;
        ItemStack bly2;
        ItemStack bly3;
        if (this.itemStacks.get(0).isEmpty()) {
            bly2 = this.itemStacks.get(1);
            bly3 = ItemStack.EMPTY;
        }
        else {
            bly2 = this.itemStacks.get(0);
            bly3 = this.itemStacks.get(1);
        }
        if (bly2.isEmpty()) {
            this.setItem(2, ItemStack.EMPTY);
            this.futureXp = 0;
            return;
        }
        final MerchantOffers bqt4 = this.merchant.getOffers();
        if (!bqt4.isEmpty()) {
            MerchantOffer bqs5 = bqt4.getRecipeFor(bly2, bly3, this.selectionHint);
            if (bqs5 == null || bqs5.isOutOfStock()) {
                this.activeOffer = bqs5;
                bqs5 = bqt4.getRecipeFor(bly3, bly2, this.selectionHint);
            }
            if (bqs5 != null && !bqs5.isOutOfStock()) {
                this.activeOffer = bqs5;
                this.setItem(2, bqs5.assemble());
                this.futureXp = bqs5.getXp();
            }
            else {
                this.setItem(2, ItemStack.EMPTY);
                this.futureXp = 0;
            }
        }
        this.merchant.notifyTradeUpdated(this.getItem(2));
    }
    
    @Nullable
    public MerchantOffer getActiveOffer() {
        return this.activeOffer;
    }
    
    public void setSelectionHint(final int integer) {
        this.selectionHint = integer;
        this.updateSellItem();
    }
    
    public void clearContent() {
        this.itemStacks.clear();
    }
    
    public int getFutureXp() {
        return this.futureXp;
    }
}
