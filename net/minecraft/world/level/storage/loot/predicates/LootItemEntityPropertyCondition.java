package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.advancements.critereon.EntityPredicate;

public class LootItemEntityPropertyCondition implements LootItemCondition {
    private final EntityPredicate predicate;
    private final LootContext.EntityTarget entityTarget;
    
    private LootItemEntityPropertyCondition(final EntityPredicate bg, final LootContext.EntityTarget c) {
        this.predicate = bg;
        this.entityTarget = c;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.ENTITY_PROPERTIES;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.ORIGIN, this.entityTarget.getParam());
    }
    
    public boolean test(final LootContext cys) {
        final Entity apx3 = cys.<Entity>getParamOrNull(this.entityTarget.getParam());
        final Vec3 dck4 = cys.<Vec3>getParamOrNull(LootContextParams.ORIGIN);
        return this.predicate.matches(cys.getLevel(), dck4, apx3);
    }
    
    public static Builder entityPresent(final LootContext.EntityTarget c) {
        return hasProperties(c, EntityPredicate.Builder.entity());
    }
    
    public static Builder hasProperties(final LootContext.EntityTarget c, final EntityPredicate.Builder a) {
        return () -> new LootItemEntityPropertyCondition(a.build(), c);
    }
    
    public static Builder hasProperties(final LootContext.EntityTarget c, final EntityPredicate bg) {
        return () -> new LootItemEntityPropertyCondition(bg, c);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemEntityPropertyCondition> {
        public void serialize(final JsonObject jsonObject, final LootItemEntityPropertyCondition dbo, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("predicate", dbo.predicate.serializeToJson());
            jsonObject.add("entity", jsonSerializationContext.serialize(dbo.entityTarget));
        }
        
        public LootItemEntityPropertyCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final EntityPredicate bg4 = EntityPredicate.fromJson(jsonObject.get("predicate"));
            return new LootItemEntityPropertyCondition(bg4, GsonHelper.<LootContext.EntityTarget>getAsObject(jsonObject, "entity", jsonDeserializationContext, (java.lang.Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class), null);
        }
    }
}
