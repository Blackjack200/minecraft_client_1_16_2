package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import java.util.Random;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ApplyExplosionDecay extends LootItemConditionalFunction {
    private ApplyExplosionDecay(final LootItemCondition[] arr) {
        super(arr);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.EXPLOSION_DECAY;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final Float float4 = cys.<Float>getParamOrNull(LootContextParams.EXPLOSION_RADIUS);
        if (float4 != null) {
            final Random random5 = cys.getRandom();
            final float float5 = 1.0f / float4;
            final int integer7 = bly.getCount();
            int integer8 = 0;
            for (int integer9 = 0; integer9 < integer7; ++integer9) {
                if (random5.nextFloat() <= float5) {
                    ++integer8;
                }
            }
            bly.setCount(integer8);
        }
        return bly;
    }
    
    public static Builder<?> explosionDecay() {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)ApplyExplosionDecay::new);
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<ApplyExplosionDecay> {
        @Override
        public ApplyExplosionDecay deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            return new ApplyExplosionDecay(arr, null);
        }
    }
}
