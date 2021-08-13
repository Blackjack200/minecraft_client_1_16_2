package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.resources.ResourceLocation;
import java.util.Random;

public final class ConstantIntValue implements RandomIntGenerator {
    private final int value;
    
    public ConstantIntValue(final int integer) {
        this.value = integer;
    }
    
    public int getInt(final Random random) {
        return this.value;
    }
    
    public ResourceLocation getType() {
        return ConstantIntValue.CONSTANT;
    }
    
    public static ConstantIntValue exactly(final int integer) {
        return new ConstantIntValue(integer);
    }
    
    public static class Serializer implements JsonDeserializer<ConstantIntValue>, JsonSerializer<ConstantIntValue> {
        public ConstantIntValue deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new ConstantIntValue(GsonHelper.convertToInt(jsonElement, "value"));
        }
        
        public JsonElement serialize(final ConstantIntValue cyo, final Type type, final JsonSerializationContext jsonSerializationContext) {
            return (JsonElement)new JsonPrimitive((Number)cyo.value);
        }
    }
}
