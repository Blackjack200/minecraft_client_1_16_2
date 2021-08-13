package net.minecraft.world.inventory;

import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.function.BiConsumer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;

public abstract class ItemCombinerMenu extends AbstractContainerMenu {
    protected final ResultContainer resultSlots;
    protected final Container inputSlots;
    protected final ContainerLevelAccess access;
    protected final Player player;
    
    protected abstract boolean mayPickup(final Player bft, final boolean boolean2);
    
    protected abstract ItemStack onTake(final Player bft, final ItemStack bly);
    
    protected abstract boolean isValidBlock(final BlockState cee);
    
    public ItemCombinerMenu(@Nullable final MenuType<?> bjb, final int integer, final Inventory bfs, final ContainerLevelAccess bij) {
        super(bjb, integer);
        this.resultSlots = new ResultContainer();
        this.inputSlots = new SimpleContainer(2) {
            @Override
            public void setChanged() {
                super.setChanged();
                ItemCombinerMenu.this.slotsChanged(this);
            }
        };
        this.access = bij;
        this.player = bfs.player;
        this.addSlot(new Slot(this.inputSlots, 0, 27, 47));
        this.addSlot(new Slot(this.inputSlots, 1, 76, 47));
        this.addSlot(new Slot(this.resultSlots, 2, 134, 47) {
            @Override
            public boolean mayPlace(final ItemStack bly) {
                return false;
            }
            
            @Override
            public boolean mayPickup(final Player bft) {
                return ItemCombinerMenu.this.mayPickup(bft, this.hasItem());
            }
            
            @Override
            public ItemStack onTake(final Player bft, final ItemStack bly) {
                return ItemCombinerMenu.this.onTake(bft, bly);
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
    
    public abstract void createResult();
    
    @Override
    public void slotsChanged(final Container aok) {
        super.slotsChanged(aok);
        if (aok == this.inputSlots) {
            this.createResult();
        }
    }
    
    @Override
    public void removed(final Player bft) {
        super.removed(bft);
        this.access.execute((BiConsumer<Level, BlockPos>)((bru, fx) -> this.clearContainer(bft, bru, this.inputSlots)));
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.access.<Boolean>evaluate((java.util.function.BiFunction<Level, BlockPos, Boolean>)((bru, fx) -> {
            if (!this.isValidBlock(bru.getBlockState(fx))) {
                return false;
            }
            return bft.distanceToSqr(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5) <= 64.0;
        }), true);
    }
    
    protected boolean shouldQuickMoveToAdditionalSlot(final ItemStack bly) {
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
            }
            else if (integer == 0 || integer == 1) {
                if (!this.moveItemStackTo(bly5, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (integer >= 3 && integer < 39) {
                final int integer2 = this.shouldQuickMoveToAdditionalSlot(bly4) ? 1 : 0;
                if (!this.moveItemStackTo(bly5, integer2, 2, false)) {
                    return ItemStack.EMPTY;
                }
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
}
