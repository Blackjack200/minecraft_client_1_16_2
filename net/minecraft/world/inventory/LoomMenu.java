package net.minecraft.world.inventory;

import net.minecraft.world.item.DyeColor;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;

public class LoomMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final DataSlot selectedBannerPatternIndex;
    private Runnable slotUpdateListener;
    private final Slot bannerSlot;
    private final Slot dyeSlot;
    private final Slot patternSlot;
    private final Slot resultSlot;
    private long lastSoundTime;
    private final Container inputContainer;
    private final Container outputContainer;
    
    public LoomMenu(final int integer, final Inventory bfs) {
        this(integer, bfs, ContainerLevelAccess.NULL);
    }
    
    public LoomMenu(final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(MenuType.LOOM, integer);
        this.selectedBannerPatternIndex = DataSlot.standalone();
        this.slotUpdateListener = (() -> {});
        this.inputContainer = new SimpleContainer(3) {
            @Override
            public void setChanged() {
                super.setChanged();
                LoomMenu.this.slotsChanged(this);
                LoomMenu.this.slotUpdateListener.run();
            }
        };
        this.outputContainer = new SimpleContainer(1) {
            @Override
            public void setChanged() {
                super.setChanged();
                LoomMenu.this.slotUpdateListener.run();
            }
        };
        this.access = bij;
        this.bannerSlot = this.addSlot(new Slot(this.inputContainer, 0, 13, 26) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.getItem() instanceof BannerItem;
            }
        });
        this.dyeSlot = this.addSlot(new Slot(this.inputContainer, 1, 33, 26) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.getItem() instanceof DyeItem;
            }
        });
        this.patternSlot = this.addSlot(new Slot(this.inputContainer, 2, 23, 45) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return bly.getItem() instanceof BannerPatternItem;
            }
        });
        this.resultSlot = this.addSlot(new Slot(this.outputContainer, 0, 143, 58) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return false;
            }
            
            @Override
            public ItemStack onTake(final Player bft, final ItemStack bly) {
                LoomMenu.this.bannerSlot.remove(1);
                LoomMenu.this.dyeSlot.remove(1);
                if (!LoomMenu.this.bannerSlot.hasItem() || !LoomMenu.this.dyeSlot.hasItem()) {
                    LoomMenu.this.selectedBannerPatternIndex.set(0);
                }
                bij.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> {
                    final long long4 = bru.getGameTime();
                    if (LoomMenu.this.lastSoundTime != long4) {
                        bru.playSound(null, fx, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1.0f, 1.0f);
                        LoomMenu.this.lastSoundTime = long4;
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
        this.addDataSlot(this.selectedBannerPatternIndex);
    }
    
    public int getSelectedBannerPatternIndex() {
        return this.selectedBannerPatternIndex.get();
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return AbstractContainerMenu.stillValid(this.access, bft, Blocks.LOOM);
    }
    
    @Override
    public boolean clickMenuButton(final Player bft, final int integer) {
        if (integer > 0 && integer <= BannerPattern.AVAILABLE_PATTERNS) {
            this.selectedBannerPatternIndex.set(integer);
            this.setupResultSlot();
            return true;
        }
        return false;
    }
    
    @Override
    public void slotsChanged(final Container aok) {
        final ItemStack bly3 = this.bannerSlot.getItem();
        final ItemStack bly4 = this.dyeSlot.getItem();
        final ItemStack bly5 = this.patternSlot.getItem();
        final ItemStack bly6 = this.resultSlot.getItem();
        if (!bly6.isEmpty() && (bly3.isEmpty() || bly4.isEmpty() || this.selectedBannerPatternIndex.get() <= 0 || (this.selectedBannerPatternIndex.get() >= BannerPattern.COUNT - BannerPattern.PATTERN_ITEM_COUNT && bly5.isEmpty()))) {
            this.resultSlot.set(ItemStack.EMPTY);
            this.selectedBannerPatternIndex.set(0);
        }
        else if (!bly5.isEmpty() && bly5.getItem() instanceof BannerPatternItem) {
            final CompoundTag md7 = bly3.getOrCreateTagElement("BlockEntityTag");
            final boolean boolean8 = md7.contains("Patterns", 9) && !bly3.isEmpty() && md7.getList("Patterns", 10).size() >= 6;
            if (boolean8) {
                this.selectedBannerPatternIndex.set(0);
            }
            else {
                this.selectedBannerPatternIndex.set(((BannerPatternItem)bly5.getItem()).getBannerPattern().ordinal());
            }
        }
        this.setupResultSlot();
        this.broadcastChanges();
    }
    
    public void registerUpdateListener(final Runnable runnable) {
        this.slotUpdateListener = runnable;
    }
    
    @Override
    public ItemStack quickMoveStack(final Player bft, final int integer) {
        ItemStack bly4 = ItemStack.EMPTY;
        final Slot bjo5 = (Slot)this.slots.get(integer);
        if (bjo5 != null && bjo5.hasItem()) {
            final ItemStack bly5 = bjo5.getItem();
            bly4 = bly5.copy();
            if (integer == this.resultSlot.index) {
                if (!this.moveItemStackTo(bly5, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                bjo5.onQuickCraft(bly5, bly4);
            }
            else if (integer == this.dyeSlot.index || integer == this.bannerSlot.index || integer == this.patternSlot.index) {
                if (!this.moveItemStackTo(bly5, 4, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (bly5.getItem() instanceof BannerItem) {
                if (!this.moveItemStackTo(bly5, this.bannerSlot.index, this.bannerSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (bly5.getItem() instanceof DyeItem) {
                if (!this.moveItemStackTo(bly5, this.dyeSlot.index, this.dyeSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (bly5.getItem() instanceof BannerPatternItem) {
                if (!this.moveItemStackTo(bly5, this.patternSlot.index, this.patternSlot.index + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 4 && integer < 31) {
                if (!this.moveItemStackTo(bly5, 31, 40, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 31 && integer < 40 && !this.moveItemStackTo(bly5, 4, 31, false)) {
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
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bft.level, this.inputContainer)));
    }
    
    private void setupResultSlot() {
        if (this.selectedBannerPatternIndex.get() > 0) {
            final ItemStack bly2 = this.bannerSlot.getItem();
            final ItemStack bly3 = this.dyeSlot.getItem();
            ItemStack bly4 = ItemStack.EMPTY;
            if (!bly2.isEmpty() && !bly3.isEmpty()) {
                bly4 = bly2.copy();
                bly4.setCount(1);
                final BannerPattern cby5 = BannerPattern.values()[this.selectedBannerPatternIndex.get()];
                final DyeColor bku6 = ((DyeItem)bly3.getItem()).getDyeColor();
                final CompoundTag md7 = bly4.getOrCreateTagElement("BlockEntityTag");
                ListTag mj8;
                if (md7.contains("Patterns", 9)) {
                    mj8 = md7.getList("Patterns", 10);
                }
                else {
                    mj8 = new ListTag();
                    md7.put("Patterns", (Tag)mj8);
                }
                final CompoundTag md8 = new CompoundTag();
                md8.putString("Pattern", cby5.getHashname());
                md8.putInt("Color", bku6.getId());
                mj8.add(md8);
            }
            if (!ItemStack.matches(bly4, this.resultSlot.getItem())) {
                this.resultSlot.set(bly4);
            }
        }
    }
    
    public Slot getBannerSlot() {
        return this.bannerSlot;
    }
    
    public Slot getDyeSlot() {
        return this.dyeSlot;
    }
    
    public Slot getPatternSlot() {
        return this.patternSlot;
    }
    
    public Slot getResultSlot() {
        return this.resultSlot;
    }
}
