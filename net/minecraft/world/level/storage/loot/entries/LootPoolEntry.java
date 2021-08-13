package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;

public interface LootPoolEntry {
    int getWeight(final float float1);
    
    void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys);
}
