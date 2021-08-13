package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import javax.annotation.Nullable;

public class TimeCheck implements LootItemCondition {
    @Nullable
    private final Long period;
    private final RandomValueBounds value;
    
    private TimeCheck(@Nullable final Long long1, final RandomValueBounds cza) {
        this.period = long1;
        this.value = cza;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.TIME_CHECK;
    }
    
    public boolean test(final LootContext cys) {
        final ServerLevel aag3 = cys.getLevel();
        long long4 = aag3.getDayTime();
        if (this.period != null) {
            long4 %= this.period;
        }
        return this.value.matchesValue((int)long4);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<TimeCheck> {
        public void serialize(final JsonObject jsonObject, final TimeCheck dbt, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("period", (Number)dbt.period);
            jsonObject.add("value", jsonSerializationContext.serialize(dbt.value));
        }
        
        public TimeCheck deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final Long long4 = jsonObject.has("period") ? Long.valueOf(GsonHelper.getAsLong(jsonObject, "period")) : null;
            final RandomValueBounds cza5 = GsonHelper.<RandomValueBounds>getAsObject(jsonObject, "value", jsonDeserializationContext, (java.lang.Class<? extends RandomValueBounds>)RandomValueBounds.class);
            return new TimeCheck(long4, cza5, null);
        }
    }
}
