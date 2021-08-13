package net.minecraft.world.level.storage.loot;

import com.google.common.collect.Maps;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class RandomIntGenerators {
    private static final Map<ResourceLocation, Class<? extends RandomIntGenerator>> GENERATORS;
    
    public static RandomIntGenerator deserialize(final JsonElement jsonElement, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            return (RandomIntGenerator)jsonDeserializationContext.deserialize(jsonElement, (Type)ConstantIntValue.class);
        }
        final JsonObject jsonObject3 = jsonElement.getAsJsonObject();
        final String string4 = GsonHelper.getAsString(jsonObject3, "type", RandomIntGenerator.UNIFORM.toString());
        final Class<? extends RandomIntGenerator> class5 = RandomIntGenerators.GENERATORS.get(new ResourceLocation(string4));
        if (class5 == null) {
            throw new JsonParseException("Unknown generator: " + string4);
        }
        return (RandomIntGenerator)jsonDeserializationContext.deserialize((JsonElement)jsonObject3, (Type)class5);
    }
    
    public static JsonElement serialize(final RandomIntGenerator cyy, final JsonSerializationContext jsonSerializationContext) {
        final JsonElement jsonElement3 = jsonSerializationContext.serialize(cyy);
        if (jsonElement3.isJsonObject()) {
            jsonElement3.getAsJsonObject().addProperty("type", cyy.getType().toString());
        }
        return jsonElement3;
    }
    
    static {
        (GENERATORS = (Map)Maps.newHashMap()).put(RandomIntGenerator.UNIFORM, RandomValueBounds.class);
        RandomIntGenerators.GENERATORS.put(RandomIntGenerator.BINOMIAL, BinomialDistributionGenerator.class);
        RandomIntGenerators.GENERATORS.put(RandomIntGenerator.CONSTANT, ConstantIntValue.class);
    }
}
