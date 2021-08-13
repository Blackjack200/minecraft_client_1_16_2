package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.IntLimiter;

public class LimitCount extends LootItemConditionalFunction {
    private final IntLimiter limiter;
    
    private LimitCount(final LootItemCondition[] arr, final IntLimiter cyr) {
        super(arr);
        this.limiter = cyr;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.LIMIT_COUNT;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final int integer4 = this.limiter.applyAsInt(bly.getCount());
        bly.setCount(integer4);
        return bly;
    }
    
    public static Builder<?> limitCount(final IntLimiter cyr) {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)(arr -> new LimitCount(arr, cyr)));
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<LimitCount> {
        @Override
        public void serialize(final JsonObject jsonObject, final LimitCount dae, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dae, jsonSerializationContext);
            jsonObject.add("limit", jsonSerializationContext.serialize(dae.limiter));
        }
        
        @Override
        public LimitCount deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final IntLimiter cyr5 = GsonHelper.<IntLimiter>getAsObject(jsonObject, "limit", jsonDeserializationContext, (java.lang.Class<? extends IntLimiter>)IntLimiter.class);
            return new LimitCount(arr, cyr5, null);
        }
    }
}
