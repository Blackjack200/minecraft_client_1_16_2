package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;

public class DynamicLoot extends LootPoolSingletonContainer {
    private final ResourceLocation name;
    
    private DynamicLoot(final ResourceLocation vk, final int integer2, final int integer3, final LootItemCondition[] arr, final LootItemFunction[] arr) {
        super(integer2, integer3, arr, arr);
        this.name = vk;
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.DYNAMIC;
    }
    
    public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
        cys.addDynamicDrops(this.name, consumer);
    }
    
    public static Builder<?> dynamicEntry(final ResourceLocation vk) {
        return LootPoolSingletonContainer.simpleBuilder((integer2, integer3, arr, arr) -> new DynamicLoot(vk, integer2, integer3, arr, arr));
    }
    
    public static class Serializer extends LootPoolSingletonContainer.Serializer<DynamicLoot> {
        @Override
        public void serialize(final JsonObject jsonObject, final DynamicLoot czh, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czh, jsonSerializationContext);
            jsonObject.addProperty("name", czh.name.toString());
        }
        
        @Override
        protected DynamicLoot deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr) {
            final ResourceLocation vk8 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "name"));
            return new DynamicLoot(vk8, integer3, integer4, arr, arr, null);
        }
    }
}
