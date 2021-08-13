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
import net.minecraft.resources.ResourceLocation;
import java.util.Random;

public final class BinomialDistributionGenerator implements RandomIntGenerator {
    private final int n;
    private final float p;
    
    public BinomialDistributionGenerator(final int integer, final float float2) {
        this.n = integer;
        this.p = float2;
    }
    
    public int getInt(final Random random) {
        int integer3 = 0;
        for (int integer4 = 0; integer4 < this.n; ++integer4) {
            if (random.nextFloat() < this.p) {
                ++integer3;
            }
        }
        return integer3;
    }
    
    public static BinomialDistributionGenerator binomial(final int integer, final float float2) {
        return new BinomialDistributionGenerator(integer, float2);
    }
    
    public ResourceLocation getType() {
        return BinomialDistributionGenerator.BINOMIAL;
    }
    
    public static class Serializer implements JsonDeserializer<BinomialDistributionGenerator>, JsonSerializer<BinomialDistributionGenerator> {
        public BinomialDistributionGenerator deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "value");
            final int integer6 = GsonHelper.getAsInt(jsonObject5, "n");
            final float float7 = GsonHelper.getAsFloat(jsonObject5, "p");
            return new BinomialDistributionGenerator(integer6, float7);
        }
        
        public JsonElement serialize(final BinomialDistributionGenerator cym, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            jsonObject5.addProperty("n", (Number)cym.n);
            jsonObject5.addProperty("p", (Number)cym.p);
            return (JsonElement)jsonObject5;
        }
    }
}
