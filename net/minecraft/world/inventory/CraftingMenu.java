package net.minecraft.world.inventory;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.core.BlockPos;
import java.util.function.BiConsumer;
import java.util.Optional;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class CraftingMenu extends RecipeBookMenu<CraftingContainer> {
    private final CraftingContainer craftSlots;
    private final ResultContainer resultSlots;
    private final ContainerLevelAccess access;
    private final Player player;
    
    public CraftingMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public CraftingMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.CRAFTING, integer);
        this.craftSlots = new CraftingContainer(this, 3, 3);
        this.resultSlots = new ResultContainer();
        this.access = bij;
        this.player = bfs.player;
        this.addSlot(new ResultSlot(bfs.player, this.craftSlots, this.resultSlots, 0, 124, 35));
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 3; ++integer3) {
                this.addSlot(new Slot(this.craftSlots, integer3 + integer2 * 3, 30 + integer3 * 18, 17 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 8 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 8 + integer2 * 18, 142));
        }
    }
    
    protected static void slotChangedCraftingGrid(final int integer, final Level bru, final Player bft, final CraftingContainer bil, final ResultContainer bjj) {
        if (bru.isClientSide) {
            return;
        }
        final ServerPlayer aah6 = (ServerPlayer)bft;
        ItemStack bly7 = ItemStack.EMPTY;
        final Optional<CraftingRecipe> optional8 = bru.getServer().getRecipeManager().<CraftingContainer, CraftingRecipe>getRecipeFor(RecipeType.CRAFTING, bil, bru);
        if (optional8.isPresent()) {
            final CraftingRecipe bof9 = (CraftingRecipe)optional8.get();
            if (bjj.setRecipeUsed(bru, aah6, bof9)) {
                bly7 = bof9.assemble(bil);
            }
        }
        bjj.setItem(0, bly7);
        aah6.connection.send(new ClientboundContainerSetSlotPacket(integer, 0, bly7));
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> slotChangedCraftingGrid(this.containerId, bru, this.player, this.craftSlots, this.resultSlots)));
    }
    
    @Override
    public void fillCraftSlotsStackedContents(final StackedContents bfv) {
        this.craftSlots.fillStackedContents(bfv);
    }
    
    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }
    
    @Override
    public boolean recipeMatches(final Recipe<? super CraftingContainer> bon) {
        return bon.matches(this.craftSlots, this.player.level);
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bru, this.craftSlots)));
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.CRAFTING_TABLE);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer == 0) {
                this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> bly5.getItem().onCraftedBy(bly5, bru, bft)));
                if (!this.moveItemStackTo(bly5, 10, 46, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (integer >= 10 && integer < 46) {
                if (!this.moveItemStackTo(bly5, 1, 10, false)) {
                    if (integer < 37) {
                        if (!this.moveItemStackTo(bly5, 37, 46, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.moveItemStackTo(bly5, 10, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (!this.moveItemStackTo(bly5, 10, 46, false)) {
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
        return 10;
    }
    
    @Override
    public RecipeBookType getRecipeBookType() {
        return RecipeBookType.CRAFTING;
    }
}
