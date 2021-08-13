package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import java.util.Random;
import net.minecraft.world.item.SuspiciousStewItem;
import com.google.common.collect.Iterables;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import net.minecraft.world.effect.MobEffect;
import java.util.Map;

public class SetStewEffectFunction extends LootItemConditionalFunction {
    private final Map<MobEffect, RandomValueBounds> effectDurationMap;
    
    private SetStewEffectFunction(final LootItemCondition[] arr, final Map<MobEffect, RandomValueBounds> map) {
        super(arr);
        this.effectDurationMap = (Map<MobEffect, RandomValueBounds>)ImmutableMap.copyOf((Map)map);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_STEW_EFFECT;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.getItem() != Items.SUSPICIOUS_STEW || this.effectDurationMap.isEmpty()) {
            return bly;
        }
        final Random random4 = cys.getRandom();
        final int integer5 = random4.nextInt(this.effectDurationMap.size());
        final Map.Entry<MobEffect, RandomValueBounds> entry6 = (Map.Entry<MobEffect, RandomValueBounds>)Iterables.get((Iterable)this.effectDurationMap.entrySet(), integer5);
        final MobEffect app7 = (MobEffect)entry6.getKey();
        int integer6 = ((RandomValueBounds)entry6.getValue()).getInt(random4);
        if (!app7.isInstantenous()) {
            integer6 *= 20;
        }
        SuspiciousStewItem.saveMobEffect(bly, app7, integer6);
        return bly;
    }
    
    public static Builder stewEffect() {
        return new Builder();
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final Map<MobEffect, RandomValueBounds> effectDurationMap;
        
        public Builder() {
            this.effectDurationMap = (Map<MobEffect, RandomValueBounds>)Maps.newHashMap();
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public Builder withEffect(final MobEffect app, final RandomValueBounds cza) {
            this.effectDurationMap.put(app, cza);
            return this;
        }
        
        public LootItemFunction build() {
            return new SetStewEffectFunction(this.getConditions(), this.effectDurationMap, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetStewEffectFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetStewEffectFunction das, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, das, jsonSerializationContext);
            if (!das.effectDurationMap.isEmpty()) {
                final JsonArray jsonArray5 = new JsonArray();
                for (final MobEffect app7 : das.effectDurationMap.keySet()) {
                    final JsonObject jsonObject2 = new JsonObject();
                    final ResourceLocation vk9 = Registry.MOB_EFFECT.getKey(app7);
                    if (vk9 == null) {
                        throw new IllegalArgumentException(new StringBuilder().append("Don't know how to serialize mob effect ").append(app7).toString());
                    }
                    jsonObject2.add("type", (JsonElement)new JsonPrimitive(vk9.toString()));
                    jsonObject2.add("duration", jsonSerializationContext.serialize(das.effectDurationMap.get(app7)));
                    jsonArray5.add((JsonElement)jsonObject2);
                }
                jsonObject.add("effects", (JsonElement)jsonArray5);
            }
        }
        
        @Override
        public SetStewEffectFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final Map<MobEffect, RandomValueBounds> map5 = (Map<MobEffect, RandomValueBounds>)Maps.newHashMap();
            if (jsonObject.has("effects")) {
                final JsonArray jsonArray6 = GsonHelper.getAsJsonArray(jsonObject, "effects");
                for (final JsonElement jsonElement8 : jsonArray6) {
                    final String string9 = GsonHelper.getAsString(jsonElement8.getAsJsonObject(), "type");
                    final MobEffect app10 = (MobEffect)Registry.MOB_EFFECT.getOptional(new ResourceLocation(string9)).orElseThrow(() -> new JsonSyntaxException("Unknown mob effect '" + string9 + "'"));
                    final RandomValueBounds cza11 = GsonHelper.<RandomValueBounds>getAsObject(jsonElement8.getAsJsonObject(), "duration", jsonDeserializationContext, (java.lang.Class<? extends RandomValueBounds>)RandomValueBounds.class);
                    map5.put(app10, cza11);
                }
            }
            return new SetStewEffectFunction(arr, map5, null);
        }
    }
}
