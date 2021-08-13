package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import net.minecraft.world.level.storage.loot.RandomIntGenerators;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.RandomIntGenerator;

public class SetItemCountFunction extends LootItemConditionalFunction {
    private final RandomIntGenerator value;
    
    private SetItemCountFunction(final LootItemCondition[] arr, final RandomIntGenerator cyy) {
        super(arr);
        this.value = cyy;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_COUNT;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        bly.setCount(this.value.getInt(cys.getRandom()));
        return bly;
    }
    
    public static Builder<?> setCount(final RandomIntGenerator cyy) {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)(arr -> new SetItemCountFunction(arr, cyy)));
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetItemCountFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetItemCountFunction dan, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dan, jsonSerializationContext);
            jsonObject.add("count", RandomIntGenerators.serialize(dan.value, jsonSerializationContext));
        }
        
        @Override
        public SetItemCountFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final RandomIntGenerator cyy5 = RandomIntGenerators.deserialize(jsonObject.get("count"), jsonDeserializationContext);
            return new SetItemCountFunction(arr, cyy5, null);
        }
    }
}
