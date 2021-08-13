package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;

public class SetContainerLootTable extends LootItemConditionalFunction {
    private final ResourceLocation name;
    private final long seed;
    
    private SetContainerLootTable(final LootItemCondition[] arr, final ResourceLocation vk, final long long3) {
        super(arr);
        this.name = vk;
        this.seed = long3;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_LOOT_TABLE;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.isEmpty()) {
            return bly;
        }
        final CompoundTag md4 = new CompoundTag();
        md4.putString("LootTable", this.name.toString());
        if (this.seed != 0L) {
            md4.putLong("LootTableSeed", this.seed);
        }
        bly.getOrCreateTag().put("BlockEntityTag", (Tag)md4);
        return bly;
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
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetContainerLootTable> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetContainerLootTable dam, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dam, jsonSerializationContext);
            jsonObject.addProperty("name", dam.name.toString());
            if (dam.seed != 0L) {
                jsonObject.addProperty("seed", (Number)dam.seed);
            }
        }
        
        @Override
        public SetContainerLootTable deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final ResourceLocation vk5 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "name"));
            final long long6 = GsonHelper.getAsLong(jsonObject, "seed", 0L);
            return new SetContainerLootTable(arr, vk5, long6, null);
        }
    }
}
