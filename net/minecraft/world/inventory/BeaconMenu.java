package net.minecraft.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Container;

public class BeaconMenu extends AbstractContainerMenu {
    private final Container beacon;
    private final PaymentSlot paymentSlot;
    private final ContainerLevelAccess access;
    private final ContainerData beaconData;
    
    public BeaconMenu(final int integer, final Container aok) {
        this(integer, aok, new SimpleContainerData(3), ContainerLevelAccess.NULL);
    }
    
    public BeaconMenu(final int integer, final Container aok, final ContainerData bii, final ContainerLevelAccess bij) {
        super(MenuType.BEACON, integer);
        this.beacon = new SimpleContainer(1) {
            public boolean canPlaceItem(final int integer, final ItemStack bly) {
                return bly.getItem().is(ItemTags.BEACON_PAYMENT_ITEMS);
            }
            
            public int getMaxStackSize() {
                return 1;
            }
        };
        AbstractContainerMenu.checkContainerDataCount(bii, 3);
        this.beaconData = bii;
        this.access = bij;
        this.addSlot(this.paymentSlot = new PaymentSlot(this.beacon, 0, 136, 110));
        this.addDataSlots(bii);
        final int integer2 = 36;
        final int integer3 = 137;
        for (int integer4 = 0; integer4 < 3; ++integer4) {
            for (int integer5 = 0; integer5 < 9; ++integer5) {
                this.addSlot(new Slot(aok, integer5 + integer4 * 9 + 9, 36 + integer5 * 18, 137 + integer4 * 18));
            }
        }
        for (int integer4 = 0; integer4 < 9; ++integer4) {
            this.addSlot(new Slot(aok, integer4, 36 + integer4 * 18, 195));
        }
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        if (bft.level.isClientSide) {
            return;
        }
        final ItemStack bly3 = this.paymentSlot.remove(this.paymentSlot.getMaxStackSize());
        if (!bly3.isEmpty()) {
            bft.drop(bly3, false);
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.BEACON);
    }
    
    @Override
    public void setData(final int integer1, final int integer2) {
        super.setData(integer1, integer2);
        this.broadcastChanges();
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer == 0) {
                if (!this.moveItemStackTo(bly5, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (!this.paymentSlot.hasItem() && this.paymentSlot.mayPlace(bly5) && bly5.getCount() == 1) {
                if (!this.moveItemStackTo(bly5, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 1 && integer < 28) {
                if (!this.moveItemStackTo(bly5, 28, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 28 && integer < 37) {
                if (!this.moveItemStackTo(bly5, 1, 28, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 1, 37, false)) {
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
    
    public int getLevels() {
        return this.beaconData.get(0);
    }
    
    @Nullable
    public MobEffect getPrimaryEffect() {
        return MobEffect.byId(this.beaconData.get(1));
    }
    
    @Nullable
    public MobEffect getSecondaryEffect() {
        return MobEffect.byId(this.beaconData.get(2));
    }
    
    public void updateEffects(final int integer1, final int integer2) {
        if (this.paymentSlot.hasItem()) {
            this.beaconData.set(1, integer1);
            this.beaconData.set(2, integer2);
            this.paymentSlot.remove(1);
        }
    }
    
    public boolean hasPayment() {
        return !this.beacon.getItem(0).isEmpty();
    }
    
    class PaymentSlot extends Slot {
        public PaymentSlot(final Container aok, final int integer3, final int integer4, final int integer5) {
            super(aok, integer3, integer4, integer5);
        }
        
        @Override
        public boolean mayPlace(final ItemStack bly) {
            return bly.getItem().is(ItemTags.BEACON_PAYMENT_ITEMS);
        }
        
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
