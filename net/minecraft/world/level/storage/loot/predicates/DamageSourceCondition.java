package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.advancements.critereon.DamageSourcePredicate;

public class DamageSourceCondition implements LootItemCondition {
    private final DamageSourcePredicate predicate;
    
    private DamageSourceCondition(final DamageSourcePredicate aw) {
        this.predicate = aw;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.DAMAGE_SOURCE_PROPERTIES;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.ORIGIN, LootContextParams.DAMAGE_SOURCE);
    }
    
    public boolean test(final LootContext cys) {
        final DamageSource aph3 = cys.<DamageSource>getParamOrNull(LootContextParams.DAMAGE_SOURCE);
        final Vec3 dck4 = cys.<Vec3>getParamOrNull(LootContextParams.ORIGIN);
        return dck4 != null && aph3 != null && this.predicate.matches(cys.getLevel(), dck4, aph3);
    }
    
    public static Builder hasDamageSource(final DamageSourcePredicate.Builder a) {
        return () -> new DamageSourceCondition(a.build());
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<DamageSourceCondition> {
        public void serialize(final JsonObject jsonObject, final DamageSourceCondition dbf, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("predicate", dbf.predicate.serializeToJson());
        }
        
        public DamageSourceCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final DamageSourcePredicate aw4 = DamageSourcePredicate.fromJson(jsonObject.get("predicate"));
            return new DamageSourceCondition(aw4, null);
        }
    }
}
