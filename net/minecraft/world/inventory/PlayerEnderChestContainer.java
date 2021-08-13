package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.SimpleContainer;

public class PlayerEnderChestContainer extends SimpleContainer {
    private EnderChestBlockEntity activeChest;
    
    public PlayerEnderChestContainer() {
        super(27);
    }
    
    public void setActiveChest(final EnderChestBlockEntity ccs) {
        this.activeChest = ccs;
    }
    
    @Override
    public void fromTag(final ListTag mj) {
        for (int integer3 = 0; integer3 < this.getContainerSize(); ++integer3) {
            this.setItem(integer3, ItemStack.EMPTY);
        }
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            final int integer4 = md4.getByte("Slot") & 0xFF;
            if (integer4 >= 0 && integer4 < this.getContainerSize()) {
                this.setItem(integer4, ItemStack.of(md4));
            }
        }
    }
    
    @Override
    public ListTag createTag() {
        final ListTag mj2 = new ListTag();
        for (int integer3 = 0; integer3 < this.getContainerSize(); ++integer3) {
            final ItemStack bly4 = this.getItem(integer3);
            if (!bly4.isEmpty()) {
                final CompoundTag md5 = new CompoundTag();
                md5.putByte("Slot", (byte)integer3);
                bly4.save(md5);
                mj2.add(md5);
            }
        }
        return mj2;
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return (this.activeChest == null || this.activeChest.stillValid(bft)) && super.stillValid(bft);
    }
    
    public void startOpen(final Player bft) {
        if (this.activeChest != null) {
            this.activeChest.startOpen();
        }
        super.startOpen(bft);
    }
    
    public void stopOpen(final Player bft) {
        if (this.activeChest != null) {
            this.activeChest.stopOpen();
        }
        super.stopOpen(bft);
        this.activeChest = null;
    }
}
