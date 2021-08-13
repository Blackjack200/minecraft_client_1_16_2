package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import java.util.Random;

public class RandomValueBounds implements RandomIntGenerator {
    private final float min;
    private final float max;
    
    public RandomValueBounds(final float float1, final float float2) {
        this.min = float1;
        this.max = float2;
    }
    
    public RandomValueBounds(final float float1) {
        this.min = float1;
        this.max = float1;
    }
    
    public static RandomValueBounds between(final float float1, final float float2) {
        return new RandomValueBounds(float1, float2);
    }
    
    public float getMin() {
        return this.min;
    }
    
    public float getMax() {
        return this.max;
    }
    
    public int getInt(final Random random) {
        return Mth.nextInt(random, Mth.floor(this.min), Mth.floor(this.max));
    }
    
    public float getFloat(final Random random) {
        return Mth.nextFloat(random, this.min, this.max);
    }
    
    public boolean matchesValue(final int integer) {
        return integer <= this.max && integer >= this.min;
    }
    
    public ResourceLocation getType() {
        return RandomValueBounds.UNIFORM;
    }
    
    public static class Serializer implements JsonDeserializer<RandomValueBounds>, JsonSerializer<RandomValueBounds> {
        public RandomValueBounds deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (GsonHelper.isNumberValue(jsonElement)) {
                return new RandomValueBounds(GsonHelper.convertToFloat(jsonElement, "value"));
            }
            final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "value");
            final float float6 = GsonHelper.getAsFloat(jsonObject5, "min");
            final float float7 = GsonHelper.getAsFloat(jsonObject5, "max");
            return new RandomValueBounds(float6, float7);
        }
        
        public JsonElement serialize(final RandomValueBounds cza, final Type type, final JsonSerializationContext jsonSerializationContext) {
            if (cza.min == cza.max) {
                return (JsonElement)new JsonPrimitive((Number)cza.min);
            }
            final JsonObject jsonObject5 = new JsonObject();
            jsonObject5.addProperty("min", (Number)cza.min);
            jsonObject5.addProperty("max", (Number)cza.max);
            return (JsonElement)jsonObject5;
        }
    }
}
