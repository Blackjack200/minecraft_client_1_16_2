package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;

public class LootItemKilledByPlayerCondition implements LootItemCondition {
    private static final LootItemKilledByPlayerCondition INSTANCE;
    
    private LootItemKilledByPlayerCondition() {
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.KILLED_BY_PLAYER;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
    }
    
    public boolean test(final LootContext cys) {
        return cys.hasParam(LootContextParams.LAST_DAMAGE_PLAYER);
    }
    
    public static Builder killedByPlayer() {
        return () -> LootItemKilledByPlayerCondition.INSTANCE;
    }
    
    static {
        INSTANCE = new LootItemKilledByPlayerCondition();
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemKilledByPlayerCondition> {
        public void serialize(final JsonObject jsonObject, final LootItemKilledByPlayerCondition dbp, final JsonSerializationContext jsonSerializationContext) {
        }
        
        public LootItemKilledByPlayerCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            return LootItemKilledByPlayerCondition.INSTANCE;
        }
    }
}
