package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.item.Item;

public class LootItem extends LootPoolSingletonContainer {
    private final Item item;
    
    private LootItem(final Item blu, final int integer2, final int integer3, final LootItemCondition[] arr, final LootItemFunction[] arr) {
        super(integer2, integer3, arr, arr);
        this.item = blu;
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.ITEM;
    }
    
    public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
        consumer.accept(new ItemStack(this.item));
    }
    
    public static Builder<?> lootTableItem(final ItemLike brt) {
        return LootPoolSingletonContainer.simpleBuilder((integer2, integer3, arr, arr) -> new LootItem(brt.asItem(), integer2, integer3, arr, arr));
    }
    
    public static class Serializer extends LootPoolSingletonContainer.Serializer<LootItem> {
        @Override
        public void serialize(final JsonObject jsonObject, final LootItem czk, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czk, jsonSerializationContext);
            final ResourceLocation vk5 = Registry.ITEM.getKey(czk.item);
            if (vk5 == null) {
                throw new IllegalArgumentException(new StringBuilder().append("Can't serialize unknown item ").append(czk.item).toString());
            }
            jsonObject.addProperty("name", vk5.toString());
        }
        
        @Override
        protected LootItem deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr) {
            final Item blu8 = GsonHelper.getAsItem(jsonObject, "name");
            return new LootItem(blu8, integer3, integer4, arr, arr, null);
        }
    }
}
