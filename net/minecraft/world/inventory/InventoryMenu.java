package net.minecraft.world.inventory;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.entity.player.StackedContents;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;

public class InventoryMenu extends RecipeBookMenu<CraftingContainer> {
    public static final ResourceLocation BLOCK_ATLAS;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS;
    public static final ResourceLocation EMPTY_ARMOR_SLOT_SHIELD;
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS;
    private static final EquipmentSlot[] SLOT_IDS;
    private final CraftingContainer craftSlots;
    private final ResultContainer resultSlots;
    public final boolean active;
    private final Player owner;
    
    public InventoryMenu(final Inventory bfs, final boolean boolean2, final Player bft) {
        super(null, 0);
        this.craftSlots = new CraftingContainer(this, 2, 2);
        this.resultSlots = new ResultContainer();
        this.active = boolean2;
        this.owner = bft;
        this.addSlot(new ResultSlot(bfs.player, this.craftSlots, this.resultSlots, 0, 154, 28));
        for (int integer5 = 0; integer5 < 2; ++integer5) {
            for (int integer6 = 0; integer6 < 2; ++integer6) {
                this.addSlot(new Slot(this.craftSlots, integer6 + integer5 * 2, 98 + integer6 * 18, 18 + integer5 * 18));
            }
        }
        for (int integer5 = 0; integer5 < 4; ++integer5) {
            final EquipmentSlot aqc6 = InventoryMenu.SLOT_IDS[integer5];
            this.addSlot(new Slot(bfs, 39 - integer5, 8, 8 + integer5 * 18) {
                @Override
                public int getMaxStackSize() {
                    return 1;
                }
                
                @Override
                public boolean mayPlace(final ItemStack bly) {
                    return aqc6 == Mob.getEquipmentSlotForItem(bly);
                }
                
                @Override
                public boolean mayPickup(final Player bft) {
                    final ItemStack bly3 = this.getItem();
                    return (bly3.isEmpty() || bft.isCreative() || !EnchantmentHelper.hasBindingCurse(bly3)) && super.mayPickup(bft);
                }
                
                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return (Pair<ResourceLocation, ResourceLocation>)Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.TEXTURE_EMPTY_SLOTS[aqc6.getIndex()]);
                }
            });
        }
        for (int integer5 = 0; integer5 < 3; ++integer5) {
            for (int integer6 = 0; integer6 < 9; ++integer6) {
                this.addSlot(new Slot(bfs, integer6 + (integer5 + 1) * 9, 8 + integer6 * 18, 84 + integer5 * 18));
            }
        }
        for (int integer5 = 0; integer5 < 9; ++integer5) {
            this.addSlot(new Slot(bfs, integer5, 8 + integer5 * 18, 142));
        }
        this.addSlot(new Slot(bfs, 40, 77, 62) {
            @Override
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return (Pair<ResourceLocation, ResourceLocation>)Pair.of(InventoryMenu.BLOCK_ATLAS, InventoryMenu.EMPTY_ARMOR_SLOT_SHIELD);
            }
        });
    }
    
    @Override
    public void fillCraftSlotsStackedContents(final StackedContents bfv) {
        this.craftSlots.fillStackedContents(bfv);
    }
    
    @Override
    public void clearCraftingContent() {
        this.resultSlots.clearContent();
        this.craftSlots.clearContent();
    }
    
    @Override
    public boolean recipeMatches(final Recipe<? super CraftingContainer> bon) {
        return bon.matches(this.craftSlots, this.owner.level);
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        CraftingMenu.slotChangedCraftingGrid(this.containerId, this.owner.level, this.owner, this.craftSlots, this.resultSlots);
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.resultSlots.clearContent();
        if (bft.level.isClientSide) {
            return;
        }
        this.clearContainer(bft, bft.level, this.craftSlots);
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return true;
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            final EquipmentSlot aqc7 = Mob.getEquipmentSlotForItem(bly4);
            if (integer == 0) {
                if (!this.moveItemStackTo(bly5, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (integer >= 1 && integer < 5) {
                if (!this.moveItemStackTo(bly5, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 5 && integer < 9) {
                if (!this.moveItemStackTo(bly5, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (aqc7.getType() == EquipmentSlot.Type.ARMOR && !((Slot)this.slots.get(8 - aqc7.getIndex())).hasItem()) {
                final int integer2 = 8 - aqc7.getIndex();
                if (!this.moveItemStackTo(bly5, integer2, integer2 + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (aqc7 == EquipmentSlot.OFFHAND && !((Slot)this.slots.get(45)).hasItem()) {
                if (!this.moveItemStackTo(bly5, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 9 && integer < 36) {
                if (!this.moveItemStackTo(bly5, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 36 && integer < 45) {
                if (!this.moveItemStackTo(bly5, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(bly5, 9, 45, false)) {
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
            final ItemStack bly6 = bjo5.onTake(bft, bly5);
            if (integer == 0) {
                bft.drop(bly6, false);
            }
        }
        return bly4;
    }
    
    @Override
    public boolean canTakeItemForPickAll(final ItemStack bly, final Slot bjo) {
        return bjo.container != this.resultSlots && super.canTakeItemForPickAll(bly, bjo);
    }
    
    @Override
    public int getResultSlotIndex() {
        return 0;
    }
    
    @Override
    public int getGridWidth() {
        return this.craftSlots.getWidth();
    }
    
    @Override
    public int getGridHeight() {
        return this.craftSlots.getHeight();
    }
    
    @Override
    public int getSize() {
        return 5;
    }
    
    public CraftingContainer getCraftSlots() {
        return this.craftSlots;
    }
    
    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
    
    static {
        BLOCK_ATLAS = new ResourceLocation("textures/atlas/blocks.png");
        EMPTY_ARMOR_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
        EMPTY_ARMOR_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
        EMPTY_ARMOR_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
        EMPTY_ARMOR_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
        EMPTY_ARMOR_SLOT_SHIELD = new ResourceLocation("item/empty_armor_slot_shield");
        TEXTURE_EMPTY_SLOTS = new ResourceLocation[] { InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS, InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS, InventoryMenu.EMPTY_ARMOR_SLOT_CHESTPLATE, InventoryMenu.EMPTY_ARMOR_SLOT_HELMET };
        SLOT_IDS = new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
    }
}
