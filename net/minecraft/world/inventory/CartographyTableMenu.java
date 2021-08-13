package net.minecraft.world.inventory;

import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class CartographyTableMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private long lastSoundTime;
    public final Container container;
    private final ResultContainer resultContainer;
    
    public CartographyTableMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public CartographyTableMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.CARTOGRAPHY_TABLE, integer);
        this.container = new SimpleContainer(2) {
            @Override
            public void setChanged() {
                CartographyTableMenu.this.slotsChanged(this);
                super.setChanged();
            }
        };
        this.resultContainer = new ResultContainer() {
            @Override
            public void setChanged() {
                CartographyTableMenu.this.slotsChanged(this);
                super.setChanged();
            }
        };
        this.access = bij;
        this.addSlot(new Slot(this.container, 0, 15, 15) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.getItem() == Items.FILLED_MAP;
            }
        });
        this.addSlot(new Slot(this.container, 1, 15, 52) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                final Item blu3 = bly.getItem();
                return blu3 == Items.PAPER || blu3 == Items.MAP || blu3 == Items.GLASS_PANE;
            }
        });
        this.addSlot(new Slot(this.resultContainer, 2, 145, 39) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return false;
            }
            
            @Override
            public ItemStack onTake(final Player bft, final ItemStack bly) {
                ((Slot)CartographyTableMenu.this.slots.get(0)).remove(1);
                ((Slot)CartographyTableMenu.this.slots.get(1)).remove(1);
                bly.getItem().onCraftedBy(bly, bft.level, bft);
                bij.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
                    final long long4 = bru.getGameTime();
                    if (CartographyTableMenu.this.lastSoundTime != long4) {
                        bru.playSound(null, fx, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
                        CartographyTableMenu.this.lastSoundTime = long4;
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
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.CARTOGRAPHY_TABLE);
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        final ItemStack bly3 = this.container.getItem(0);
        final ItemStack bly4 = this.container.getItem(1);
        final ItemStack bly5 = this.resultContainer.getItem(2);
        if (!bly5.isEmpty() && (bly3.isEmpty() || bly4.isEmpty())) {
            this.resultContainer.removeItemNoUpdate(2);
        }
        else if (!bly3.isEmpty() && !bly4.isEmpty()) {
            this.setupResultSlot(bly3, bly4, bly5);
        }
    }
    
    private void setupResultSlot(final ItemStack bly1, final ItemStack bly2, final ItemStack bly3) {
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
            final Item blu7 = bly2.getItem();
            final MapItemSavedData cxu8 = MapItem.getSavedData(bly1, bru);
            if (cxu8 == null) {
                return;
            }
            ItemStack bly4;
            if (blu7 == Items.PAPER && !cxu8.locked && cxu8.scale < 4) {
                bly4 = bly1.copy();
                bly4.setCount(1);
                bly4.getOrCreateTag().putInt("map_scale_direction", 1);
                this.broadcastChanges();
            }
            else if (blu7 == Items.GLASS_PANE && !cxu8.locked) {
                bly4 = bly1.copy();
                bly4.setCount(1);
                bly4.getOrCreateTag().putBoolean("map_to_lock", true);
                this.broadcastChanges();
            }
            else {
                if (blu7 != Items.MAP) {
                    this.resultContainer.removeItemNoUpdate(2);
                    this.broadcastChanges();
                    return;
                }
                bly4 = bly1.copy();
                bly4.setCount(2);
                this.broadcastChanges();
            }
            if (!ItemStack.matches(bly4, bly3)) {
                this.resultContainer.setItem(2, bly4);
                this.broadcastChanges();
            }
        }));
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
            final ItemStack bly6;
            final ItemStack bly5 = bly6 = bjo5.getItem();
            final Item blu8 = bly6.getItem();
            bly4 = bly6.copy();
            if (integer == 2) {
                blu8.onCraftedBy(bly6, bft.level, bft);
                if (!this.moveItemStackTo(bly6, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly6, bly4);
            }
            else if (integer == 1 || integer == 0) {
                if (!this.moveItemStackTo(bly6, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (blu8 == Items.FILLED_MAP) {
                if (!this.moveItemStackTo(bly6, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (blu8 == Items.PAPER || blu8 == Items.MAP || blu8 == Items.GLASS_PANE) {
                if (!this.moveItemStackTo(bly6, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 3 && integer < 30) {
                if (!this.moveItemStackTo(bly6, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 30 && integer < 39 && !this.moveItemStackTo(bly6, 3, 30, false)) {
                return ItemStack.EMPTY;
            }
            if (bly6.isEmpty()) {
                bjo5.set(ItemStack.EMPTY);
            }
            bjo5.setChanged();
            if (bly6.getCount() == bly4.getCount()) {
                return ItemStack.EMPTY;
            }
            bjo5.onTake(bft, bly6);
            this.broadcastChanges();
        }
        return bly4;
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.resultContainer.removeItemNoUpdate(2);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bft.level, this.container)));
    }
}
