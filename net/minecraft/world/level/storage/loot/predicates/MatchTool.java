package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.advancements.critereon.ItemPredicate;

public class MatchTool implements LootItemCondition {
    private final ItemPredicate predicate;
    
    public MatchTool(final ItemPredicate bq) {
        this.predicate = bq;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.MATCH_TOOL;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.TOOL);
    }
    
    public boolean test(final LootContext cys) {
        final ItemStack bly3 = cys.<ItemStack>getParamOrNull(LootContextParams.TOOL);
        return bly3 != null && this.predicate.matches(bly3);
    }
    
    public static Builder toolMatches(final ItemPredicate.Builder a) {
        return () -> new MatchTool(a.build());
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<MatchTool> {
        public void serialize(final JsonObject jsonObject, final MatchTool dbs, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("predicate", dbs.predicate.serializeToJson());
        }
        
        public MatchTool deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final ItemPredicate bq4 = ItemPredicate.fromJson(jsonObject.get("predicate"));
            return new MatchTool(bq4);
        }
    }
}
