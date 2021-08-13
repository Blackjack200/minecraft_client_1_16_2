package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;

public class LootItemRandomChanceWithLootingCondition implements LootItemCondition {
    private final float percent;
    private final float lootingMultiplier;
    
    private LootItemRandomChanceWithLootingCondition(final float float1, final float float2) {
        this.percent = float1;
        this.lootingMultiplier = float2;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.RANDOM_CHANCE_WITH_LOOTING;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.KILLER_ENTITY);
    }
    
    public boolean test(final LootContext cys) {
        final Entity apx3 = cys.<Entity>getParamOrNull(LootContextParams.KILLER_ENTITY);
        int integer4 = 0;
        if (apx3 instanceof LivingEntity) {
            integer4 = EnchantmentHelper.getMobLooting((LivingEntity)apx3);
        }
        return cys.getRandom().nextFloat() < this.percent + integer4 * this.lootingMultiplier;
    }
    
    public static Builder randomChanceAndLootingBoost(final float float1, final float float2) {
        return () -> new LootItemRandomChanceWithLootingCondition(float1, float2);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemRandomChanceWithLootingCondition> {
        public void serialize(final JsonObject jsonObject, final LootItemRandomChanceWithLootingCondition dbr, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("chance", (Number)dbr.percent);
            jsonObject.addProperty("looting_multiplier", (Number)dbr.lootingMultiplier);
        }
        
        public LootItemRandomChanceWithLootingCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            return new LootItemRandomChanceWithLootingCondition(GsonHelper.getAsFloat(jsonObject, "chance"), GsonHelper.getAsFloat(jsonObject, "looting_multiplier"), null);
        }
    }
}
