package net.minecraft.world.inventory;

import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.entity.npc.ClientSideMerchant;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.trading.Merchant;

public class MerchantMenu extends AbstractContainerMenu {
    private final Merchant trader;
    private final MerchantContainer tradeContainer;
    private int merchantLevel;
    private boolean showProgressBar;
    private boolean canRestock;
    
    public MerchantMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, new ClientSideMerchant(bfs.player));
    }
    
    public MerchantMenu(final int integer, final Inventory bfs, final Merchant bqr) {
        super(MenuType.MERCHANT, integer);
        this.trader = bqr;
        this.tradeContainer = new MerchantContainer(bqr);
        this.addSlot(new Slot(this.tradeContainer, 0, 136, 37));
        this.addSlot(new Slot(this.tradeContainer, 1, 162, 37));
        this.addSlot(new MerchantResultSlot(bfs.player, bqr, this.tradeContainer, 2, 220, 37));
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 108 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 108 + integer2 * 18, 142));
        }
    }
    
    public void setShowProgressBar(final boolean boolean1) {
        this.showProgressBar = boolean1;
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        this.tradeContainer.updateSellItem();
        super.slotsChanged(aok);
    }
    
    public void setSelectionHint(final int integer) {
        this.tradeContainer.setSelectionHint(integer);
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.trader.getTradingPlayer() == bft;
    }
    
    public int getTraderXp() {
        return this.trader.getVillagerXp();
    }
    
    public int getFutureTraderXp() {
        return this.tradeContainer.getFutureXp();
    }
    
    public void setXp(final int integer) {
        this.trader.overrideXp(integer);
    }
    
    public int getTraderLevel() {
        return this.merchantLevel;
    }
    
    public void setMerchantLevel(final int integer) {
        this.merchantLevel = integer;
    }
    
    public void setCanRestock(final boolean boolean1) {
        this.canRestock = boolean1;
    }
    
    public boolean canRestock() {
        return this.canRestock;
    }
    
    @Override
    public boolean canTakeItemForPickAll(final ItemStack bly, final Slot bjo) {
        return false;
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer == 2) {
                if (!this.moveItemStackTo(bly5, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
                this.playTradeSound();
            }
            else if (integer == 0 || integer == 1) {
                if (!this.moveItemStackTo(bly5, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 3 && integer < 30) {
                if (!this.moveItemStackTo(bly5, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 30 && integer < 39 && !this.moveItemStackTo(bly5, 3, 30, false)) {
                return ItemStack.EMPTY;
            }
            if (bly5.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            else {
                bjo5.setChanged();
            }
            if (bly5.getCount() == bly4.getCount()) {
                return ItemStack.EMPTY;
            }
            bjo5.onTake(bft, bly5);
        }
        return bly4;
    }
    
    private void playTradeSound() {
        if (!this.trader.getLevel().isClientSide) {
            final Entity apx2 = (Entity)this.trader;
            this.trader.getLevel().playLocalSound(apx2.getX(), apx2.getY(), apx2.getZ(), this.trader.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0f, 1.0f, false);
        }
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.trader.setTradingPlayer(null);
        if (this.trader.getLevel().isClientSide) {
            return;
        }
        if (!bft.isAlive() || (bft instanceof ServerPlayer && ((ServerPlayer)bft).hasDisconnected())) {
            ItemStack bly3 = this.tradeContainer.removeItemNoUpdate(0);
            if (!bly3.isEmpty()) {
                bft.drop(bly3, false);
            }
            bly3 = this.tradeContainer.removeItemNoUpdate(1);
            if (!bly3.isEmpty()) {
                bft.drop(bly3, false);
            }
        }
        else {
            bft.inventory.placeItemBackInInventory(bft.level, this.tradeContainer.removeItemNoUpdate(0));
            bft.inventory.placeItemBackInInventory(bft.level, this.tradeContainer.removeItemNoUpdate(1));
        }
    }
    
    public void tryMoveItems(final int integer) {
        if (this.getOffers().size() <= integer) {
            return;
        }
        final ItemStack bly3 = this.tradeContainer.getItem(0);
        if (!bly3.isEmpty()) {
            if (!this.moveItemStackTo(bly3, 3, 39, true)) {
                return;
            }
            this.tradeContainer.setItem(0, bly3);
        }
        final ItemStack bly4 = this.tradeContainer.getItem(1);
        if (!bly4.isEmpty()) {
            if (!this.moveItemStackTo(bly4, 3, 39, true)) {
                return;
            }
            this.tradeContainer.setItem(1, bly4);
        }
        if (this.tradeContainer.getItem(0).isEmpty() && this.tradeContainer.getItem(1).isEmpty()) {
            final ItemStack bly5 = ((MerchantOffer)this.getOffers().get(integer)).getCostA();
            this.moveFromInventoryToPaymentSlot(0, bly5);
            final ItemStack bly6 = ((MerchantOffer)this.getOffers().get(integer)).getCostB();
            this.moveFromInventoryToPaymentSlot(1, bly6);
        }
    }
    
    private void moveFromInventoryToPaymentSlot(final int integer, final ItemStack bly) {
        if (!bly.isEmpty()) {
            for (int integer2 = 3; integer2 < 39; ++integer2) {
                final ItemStack bly2 = ((Slot)this.slots.get(integer2)).getItem();
                if (!bly2.isEmpty() && this.isSameItem(bly, bly2)) {
                    final ItemStack bly3 = this.tradeContainer.getItem(integer);
                    final int integer3 = bly3.isEmpty() ? 0 : bly3.getCount();
                    final int integer4 = Math.min(bly.getMaxStackSize() - integer3, bly2.getCount());
                    final ItemStack bly4 = bly2.copy();
                    final int integer5 = integer3 + integer4;
                    bly2.shrink(integer4);
                    bly4.setCount(integer5);
                    this.tradeContainer.setItem(integer, bly4);
                    if (integer5 >= bly.getMaxStackSize()) {
                        break;
                    }
                }
            }
        }
    }
    
    private boolean isSameItem(final ItemStack bly1, final ItemStack bly2) {
        return bly1.getItem() == bly2.getItem() && ItemStack.tagMatches(bly1, bly2);
    }
    
    public void setOffers(final MerchantOffers bqt) {
        this.trader.overrideOffers(bqt);
    }
    
    public MerchantOffers getOffers() {
        return this.trader.getOffers();
    }
    
    public boolean showProgressBar() {
        return this.showProgressBar;
    }
}
