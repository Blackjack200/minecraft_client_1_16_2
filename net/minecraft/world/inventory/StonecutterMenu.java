package net.minecraft.world.inventory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import java.util.List;
import net.minecraft.world.level.Level;

public class StonecutterMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<StonecutterRecipe> recipes;
    private ItemStack input;
    private long lastSoundTime;
    final Slot inputSlot;
    final Slot resultSlot;
    private Runnable slotUpdateListener;
    public final Container container;
    private final ResultContainer resultContainer;
    
    public StonecutterMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public StonecutterMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.STONECUTTER, integer);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipes = (List<StonecutterRecipe>)Lists.newArrayList();
        this.input = ItemStack.EMPTY;
        this.slotUpdateListener = (() -> {});
        this.container = new SimpleContainer(1) {
            @Override
            public void setChanged() {
                super.setChanged();
                StonecutterMenu.this.slotsChanged(this);
                StonecutterMenu.this.slotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.access = bij;
        this.level = bfs.player.level;
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return false;
            }
            
            @Override
            public ItemStack onTake(final Player bft, final ItemStack bly) {
                bly.onCraftedBy(bft.level, bft, bly.getCount());
                StonecutterMenu.this.resultContainer.awardUsedRecipes(bft);
                final ItemStack bly2 = StonecutterMenu.this.inputSlot.remove(1);
                if (!bly2.isEmpty()) {
                    StonecutterMenu.this.setupResultSlot();
                }
                bij.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
                    final long long4 = bru.getGameTime();
                    if (StonecutterMenu.this.lastSoundTime != long4) {
                        bru.playSound(null, fx, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
                        StonecutterMenu.this.lastSoundTime = long4;
                    }
                }));
                return super.onTake(bft, bly);
            }
        });
        for (int integer2 = 0; integer2 < 3; ++integer2) {
            for (int integer3 = 0; integer3 < 9; ++integer3) {
                this.addSlot(new Slot(bfs, integer3 + integer2 * 9 + 9, 8 + integer3 * 18, 84 + integer2 * 18));
            }
        }
        for (int integer2 = 0; integer2 < 9; ++integer2) {
            this.addSlot(new Slot(bfs, integer2, 8 + integer2 * 18, 142));
        }
        this.addDataSlot(this.selectedRecipeIndex);
    }
    
    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }
    
    public List<StonecutterRecipe> getRecipes() {
        return this.recipes;
    }
    
    public int getNumRecipes() {
        return this.recipes.size();
    }
    
    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.STONECUTTER);
    }
    
    @Override
    public boolean clickMenuButton(final Player bft, final int integer) {
        if (this.isValidRecipeIndex(integer)) {
            this.selectedRecipeIndex.set(integer);
            this.setupResultSlot();
        }
        return true;
    }
    
    private boolean isValidRecipeIndex(final int integer) {
        return integer >= 0 && integer < this.recipes.size();
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        final ItemStack bly3 = this.inputSlot.getItem();
        if (bly3.getItem() != this.input.getItem()) {
            this.input = bly3.copy();
            this.setupRecipeList(aok, bly3);
        }
    }
    
    private void setupRecipeList(final Container aok, final ItemStack bly) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        if (!bly.isEmpty()) {
            this.recipes = this.level.getRecipeManager().<Container, StonecutterRecipe>getRecipesFor(RecipeType.STONECUTTING, aok, this.level);
        }
    }
    
    private void setupResultSlot() {
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            final StonecutterRecipe bpb2 = (StonecutterRecipe)this.recipes.get(this.selectedRecipeIndex.get());
            this.resultContainer.setRecipeUsed(bpb2);
            this.resultSlot.set(bpb2.assemble(this.container));
        }
        else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }
    
    @Override
    public MenuType<?> getType() {
        return MenuType.STONECUTTER;
    }
    
    public void registerUpdateListener(final Runnable runnable) {
        this.slotUpdateListener = runnable;
    }
    
    @Override
    public boolean canTakeItemForPickAll(final ItemStack bly, final Slot bjo) {
        return bjo.container != this.resultContainer && super.canTakeItemForPickAll(bly, bjo);
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            final Item blu7 = bly5.getItem();
            bly4 = bly5.copy();
            if (integer == 1) {
                blu7.onCraftedBy(bly5, bft.level, bft);
                if (!this.moveItemStackTo(bly5, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (integer == 0) {
                if (!this.moveItemStackTo(bly5, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (this.level.getRecipeManager().<SimpleContainer, StonecutterRecipe>getRecipeFor(RecipeType.STONECUTTING, new SimpleContainer(new ItemStack[] { bly5 }), this.level).isPresent()) {
                if (!this.moveItemStackTo(bly5, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 2 && integer < 29) {
                if (!this.moveItemStackTo(bly5, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 29 && integer < 38 && !this.moveItemStackTo(bly5, 2, 29, false)) {
                return ItemStack.EMPTY;
            }
            if (bly5.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            bjo5.setChanged();
            if (bly5.getCount() == bly4.getCount()) {
                return ItemStack.EMPTY;
            }
            bjo5.onTake(bft, bly5);
            this.broadcastChanges();
        }
        return bly4;
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bft.level, this.container)));
    }
}
