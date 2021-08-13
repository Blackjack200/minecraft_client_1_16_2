package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.LootContext;

public class LootItemRandomChanceCondition implements LootItemCondition {
    private final float probability;
    
    private LootItemRandomChanceCondition(final float float1) {
        this.probability = float1;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.RANDOM_CHANCE;
    }
    
    public boolean test(final LootContext cys) {
        return cys.getRandom().nextFloat() < this.probability;
    }
    
    public static Builder randomChance(final float float1) {
        return () -> new LootItemRandomChanceCondition(float1);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemRandomChanceCondition> {
        public void serialize(final JsonObject jsonObject, final LootItemRandomChanceCondition dbq, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("chance", (Number)dbq.probability);
        }
        
        public LootItemRandomChanceCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            return new LootItemRandomChanceCondition(GsonHelper.getAsFloat(jsonObject, "chance"), null);
        }
    }
}
