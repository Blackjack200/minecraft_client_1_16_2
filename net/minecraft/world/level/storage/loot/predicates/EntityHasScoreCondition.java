package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.Maps;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.scores.Objective;
import java.util.Iterator;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import java.util.Map;

public class EntityHasScoreCondition implements LootItemCondition {
    private final Map<String, RandomValueBounds> scores;
    private final LootContext.EntityTarget entityTarget;
    
    private EntityHasScoreCondition(final Map<String, RandomValueBounds> map, final LootContext.EntityTarget c) {
        this.scores = (Map<String, RandomValueBounds>)ImmutableMap.copyOf((Map)map);
        this.entityTarget = c;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.ENTITY_SCORES;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(this.entityTarget.getParam());
    }
    
    public boolean test(final LootContext cys) {
        final Entity apx3 = cys.<Entity>getParamOrNull(this.entityTarget.getParam());
        if (apx3 == null) {
            return false;
        }
        final Scoreboard ddk4 = apx3.level.getScoreboard();
        for (final Map.Entry<String, RandomValueBounds> entry6 : this.scores.entrySet()) {
            if (!this.hasScore(apx3, ddk4, (String)entry6.getKey(), (RandomValueBounds)entry6.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean hasScore(final Entity apx, final Scoreboard ddk, final String string, final RandomValueBounds cza) {
        final Objective ddh6 = ddk.getObjective(string);
        if (ddh6 == null) {
            return false;
        }
        final String string2 = apx.getScoreboardName();
        return ddk.hasPlayerScore(string2, ddh6) && cza.matchesValue(ddk.getOrCreatePlayerScore(string2, ddh6).getScore());
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EntityHasScoreCondition> {
        public void serialize(final JsonObject jsonObject, final EntityHasScoreCondition dbg, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject2 = new JsonObject();
            for (final Map.Entry<String, RandomValueBounds> entry7 : dbg.scores.entrySet()) {
                jsonObject2.add((String)entry7.getKey(), jsonSerializationContext.serialize(entry7.getValue()));
            }
            jsonObject.add("scores", (JsonElement)jsonObject2);
            jsonObject.add("entity", jsonSerializationContext.serialize(dbg.entityTarget));
        }
        
        public EntityHasScoreCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final Set<Map.Entry<String, JsonElement>> set4 = (Set<Map.Entry<String, JsonElement>>)GsonHelper.getAsJsonObject(jsonObject, "scores").entrySet();
            final Map<String, RandomValueBounds> map5 = (Map<String, RandomValueBounds>)Maps.newLinkedHashMap();
            for (final Map.Entry<String, JsonElement> entry7 : set4) {
                map5.put(entry7.getKey(), GsonHelper.convertToObject((JsonElement)entry7.getValue(), "score", jsonDeserializationContext, (java.lang.Class<?>)RandomValueBounds.class));
            }
            return new EntityHasScoreCondition(map5, GsonHelper.<LootContext.EntityTarget>getAsObject(jsonObject, "entity", jsonDeserializationContext, (java.lang.Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class), null);
        }
    }
}
