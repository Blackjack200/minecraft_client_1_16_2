package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import javax.annotation.Nullable;

public class WeatherCheck implements LootItemCondition {
    @Nullable
    private final Boolean isRaining;
    @Nullable
    private final Boolean isThundering;
    
    private WeatherCheck(@Nullable final Boolean boolean1, @Nullable final Boolean boolean2) {
        this.isRaining = boolean1;
        this.isThundering = boolean2;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.WEATHER_CHECK;
    }
    
    public boolean test(final LootContext cys) {
        final ServerLevel aag3 = cys.getLevel();
        return (this.isRaining == null || this.isRaining == aag3.isRaining()) && (this.isThundering == null || this.isThundering == aag3.isThundering());
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<WeatherCheck> {
        public void serialize(final JsonObject jsonObject, final WeatherCheck dbu, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("raining", dbu.isRaining);
            jsonObject.addProperty("thundering", dbu.isThundering);
        }
        
        public WeatherCheck deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final Boolean boolean4 = jsonObject.has("raining") ? Boolean.valueOf(GsonHelper.getAsBoolean(jsonObject, "raining")) : null;
            final Boolean boolean5 = jsonObject.has("thundering") ? Boolean.valueOf(GsonHelper.getAsBoolean(jsonObject, "thundering")) : null;
            return new WeatherCheck(boolean4, boolean5, null);
        }
    }
}
