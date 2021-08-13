package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import java.util.function.IntUnaryOperator;

public class IntLimiter implements IntUnaryOperator {
    private final Integer min;
    private final Integer max;
    private final IntUnaryOperator op;
    
    private IntLimiter(@Nullable final Integer integer1, @Nullable final Integer integer2) {
        this.min = integer1;
        this.max = integer2;
        if (integer1 == null) {
            if (integer2 == null) {
                this.op = (integer -> integer);
            }
            else {
                final int integer3 = integer2;
                this.op = (integer2 -> Math.min(integer3, integer2));
            }
        }
        else {
            final int integer3 = integer1;
            if (integer2 == null) {
                this.op = (integer2 -> Math.max(integer3, integer2));
            }
            else {
                final int integer4 = integer2;
                this.op = (integer3 -> Mth.clamp(integer3, integer3, integer4));
            }
        }
    }
    
    public static IntLimiter clamp(final int integer1, final int integer2) {
        return new IntLimiter(integer1, integer2);
    }
    
    public static IntLimiter lowerBound(final int integer) {
        return new IntLimiter(integer, null);
    }
    
    public static IntLimiter upperBound(final int integer) {
        return new IntLimiter(null, integer);
    }
    
    public int applyAsInt(final int integer) {
        return this.op.applyAsInt(integer);
    }
    
    public static class Serializer implements JsonDeserializer<IntLimiter>, JsonSerializer<IntLimiter> {
        public IntLimiter deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "value");
            final Integer integer6 = jsonObject5.has("min") ? Integer.valueOf(GsonHelper.getAsInt(jsonObject5, "min")) : null;
            final Integer integer7 = jsonObject5.has("max") ? Integer.valueOf(GsonHelper.getAsInt(jsonObject5, "max")) : null;
            return new IntLimiter(integer6, integer7, null);
        }
        
        public JsonElement serialize(final IntLimiter cyr, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            if (cyr.max != null) {
                jsonObject5.addProperty("max", (Number)cyr.max);
            }
            if (cyr.min != null) {
                jsonObject5.addProperty("min", (Number)cyr.min);
            }
            return (JsonElement)jsonObject5;
        }
    }
}
