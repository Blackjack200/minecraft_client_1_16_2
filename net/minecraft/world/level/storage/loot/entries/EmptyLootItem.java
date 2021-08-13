package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EmptyLootItem extends LootPoolSingletonContainer {
    private EmptyLootItem(final int integer1, final int integer2, final LootItemCondition[] arr, final LootItemFunction[] arr) {
        super(integer1, integer2, arr, arr);
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.EMPTY;
    }
    
    public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
    }
    
    public static Builder<?> emptyItem() {
        return LootPoolSingletonContainer.simpleBuilder(EmptyLootItem::new);
    }
    
    public static class Serializer extends LootPoolSingletonContainer.Serializer<EmptyLootItem> {
        public EmptyLootItem deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr) {
            return new EmptyLootItem(integer3, integer4, arr, arr, null);
        }
    }
}
