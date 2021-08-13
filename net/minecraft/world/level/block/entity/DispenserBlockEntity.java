package net.minecraft.world.level.block.entity;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import java.util.Random;

public class DispenserBlockEntity extends RandomizableContainerBlockEntity {
    private static final Random RANDOM;
    private NonNullList<ItemStack> items;
    
    protected DispenserBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
        this.items = NonNullList.<ItemStack>withSize(9, ItemStack.EMPTY);
    }
    
    public DispenserBlockEntity() {
        this(BlockEntityType.DISPENSER);
    }
    
    @Override
    public int getContainerSize() {
        return 9;
    }
    
    public int getRandomSlot() {
        this.unpackLootTable(null);
        int integer2 = -1;
        int integer3 = 1;
        for (int integer4 = 0; integer4 < this.items.size(); ++integer4) {
            if (!this.items.get(integer4).isEmpty() && DispenserBlockEntity.RANDOM.nextInt(integer3++) == 0) {
                integer2 = integer4;
            }
        }
        return integer2;
    }
    
    public int addItem(final ItemStack bly) {
        for (int integer3 = 0; integer3 < this.items.size(); ++integer3) {
            if (this.items.get(integer3).isEmpty()) {
                this.setItem(integer3, bly);
                return integer3;
            }
        }
        return -1;
    }
    
    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.dispenser");
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.items = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(md)) {
            ContainerHelper.loadAllItems(md, this.items);
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (!this.trySaveLootTable(md)) {
            ContainerHelper.saveAllItems(md, this.items);
        }
        return md;
    }
    
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }
    
    @Override
    protected void setItems(final NonNullList<ItemStack> gj) {
        this.items = gj;
    }
    
    @Override
    protected AbstractContainerMenu createMenu(final int integer, final Inventory bfs) {
        return new DispenserMenu(integer, bfs, this);
    }
    
    static {
        RANDOM = new Random();
    }
}
