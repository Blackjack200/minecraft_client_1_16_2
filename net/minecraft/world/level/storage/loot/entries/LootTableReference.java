package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;

public class LootTableReference extends LootPoolSingletonContainer {
    private final ResourceLocation name;
    
    private LootTableReference(final ResourceLocation vk, final int integer2, final int integer3, final LootItemCondition[] arr, final LootItemFunction[] arr) {
        super(integer2, integer3, arr, arr);
        this.name = vk;
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.REFERENCE;
    }
    
    public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
        final LootTable cyv4 = cys.getLootTable(this.name);
        cyv4.getRandomItemsRaw(cys, consumer);
    }
    
    @Override
    public void validate(final ValidationContext czd) {
        if (czd.hasVisitedTable(this.name)) {
            czd.reportProblem(new StringBuilder().append("Table ").append(this.name).append(" is recursively called").toString());
            return;
        }
        super.validate(czd);
        final LootTable cyv3 = czd.resolveLootTable(this.name);
        if (cyv3 == null) {
            czd.reportProblem(new StringBuilder().append("Unknown loot table called ").append(this.name).toString());
        }
        else {
            cyv3.validate(czd.enterTable(new StringBuilder().append("->{").append(this.name).append("}").toString(), this.name));
        }
    }
    
    public static Builder<?> lootTableReference(final ResourceLocation vk) {
        return LootPoolSingletonContainer.simpleBuilder((integer2, integer3, arr, arr) -> new LootTableReference(vk, integer2, integer3, arr, arr));
    }
    
    public static class Serializer extends LootPoolSingletonContainer.Serializer<LootTableReference> {
        @Override
        public void serialize(final JsonObject jsonObject, final LootTableReference czq, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czq, jsonSerializationContext);
            jsonObject.addProperty("name", czq.name.toString());
        }
        
        @Override
        protected LootTableReference deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr) {
            final ResourceLocation vk8 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "name"));
            return new LootTableReference(vk8, integer3, integer4, arr, arr, null);
        }
    }
}
