package net.minecraft.world.inventory;

import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class BrewingStandMenu extends AbstractContainerMenu {
    private final Container brewingStand;
    private final ContainerData brewingStandData;
    private final Slot ingredientSlot;
    
    public BrewingStandMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, new SimpleContainer(5), new SimpleContainerData(2));
    }
    
    public BrewingStandMenu(final int integer, final Inventory bfs, final Container aok, final ContainerData bii) {
        super(MenuType.BREWING_STAND, integer);
        AbstractContainerMenu.checkContainerSize(aok, 5);
        AbstractContainerMenu.checkContainerDataCount(bii, 2);
        this.brewingStand = aok;
        this.brewingStandData = bii;
        this.addSlot(new PotionSlot(aok, 0, 56, 51));
        this.addSlot(new PotionSlot(aok, 1, 79, 58));
        this.addSlot(new PotionSlot(aok, 2, 102, 51));
        this.ingredientSlot = this.addSlot(new IngredientsSlot(aok, 3, 79, 17));
        this.addSlot(new FuelSlot(aok, 4, 17, 17));
        this.addDataSlots(bii);
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 8 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 8 + integer2 * 18, 142));
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.brewingStand.stillValid(bft);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if ((integer >= 0 && integer <= 2) || integer == 3 || integer == 4) {
                if (!this.moveItemStackTo(bly5, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (FuelSlot.mayPlaceItem(bly4)) {
                if (this.moveItemStackTo(bly5, 4, 5, false) || (this.ingredientSlot.mayPlace(bly5) && !this.moveItemStackTo(bly5, 3, 4, false))) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.ingredientSlot.mayPlace(bly5)) {
                if (!this.moveItemStackTo(bly5, 3, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (PotionSlot.mayPlaceItem(bly4) && bly4.getCount() == 1) {
                if (!this.moveItemStackTo(bly5, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 5 && integer < 32) {
                if (!this.moveItemStackTo(bly5, 32, 41, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 32 && integer < 41) {
                if (!this.moveItemStackTo(bly5, 5, 32, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 5, 41, false)) {
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
    
    public int getFuel() {
        return this.brewingStandData.get(1);
    }
    
    public int getBrewingTicks() {
        return this.brewingStandData.get(0);
    }
    
    static class PotionSlot extends Slot {
        public PotionSlot(final Container aok, final int integer2, final int integer3, final int integer4) {
            super(aok, integer2, integer3, integer4);
        }
        
        @Override
        public boolean mayPlace(final ItemStack bly) {
            return mayPlaceItem(bly);
        }
        
        @Override
        public int getMaxStackSize() {
            return 1;
        }
        
        @Override
        public ItemStack onTake(final Player bft, final ItemStack bly) {
            final Potion bnq4 = PotionUtils.getPotion(bly);
            if (bft instanceof ServerPlayer) {
                CriteriaTriggers.BREWED_POTION.trigger((ServerPlayer)bft, bnq4);
            }
            super.onTake(bft, bly);
            return bly;
        }
        
        public static boolean mayPlaceItem(final ItemStack bly) {
            final Item blu2 = bly.getItem();
            return blu2 == Items.POTION || blu2 == Items.SPLASH_POTION || blu2 == Items.LINGERING_POTION || blu2 == Items.GLASS_BOTTLE;
        }
    }
    
    static class IngredientsSlot extends Slot {
        public IngredientsSlot(final Container aok, final int integer2, final int integer3, final int integer4) {
            super(aok, integer2, integer3, integer4);
        }
        
        @Override
        public boolean mayPlace(final ItemStack bly) {
            return PotionBrewing.isIngredient(bly);
        }
        
        @Override
        public int getMaxStackSize() {
            return 64;
        }
    }
    
    static class FuelSlot extends Slot {
        public FuelSlot(final Container aok, final int integer2, final int integer3, final int integer4) {
            super(aok, integer2, integer3, integer4);
        }
        
        @Override
        public boolean mayPlace(final ItemStack bly) {
            return mayPlaceItem(bly);
        }
        
        public static boolean mayPlaceItem(final ItemStack bly) {
            return bly.getItem() == Items.BLAZE_POWDER;
        }
        
        @Override
        public int getMaxStackSize() {
            return 64;
        }
    }
}
