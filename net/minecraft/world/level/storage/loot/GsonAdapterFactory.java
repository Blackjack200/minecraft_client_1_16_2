package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import javax.annotation.Nullable;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import net.minecraft.core.Registry;

public class GsonAdapterFactory {
    public static <E, T extends SerializerType<E>> Builder<E, T> builder(final Registry<T> gm, final String string2, final String string3, final Function<E, T> function) {
        return new Builder<E, T>((Registry)gm, string2, string3, (Function)function);
    }
    
    public static class Builder<E, T extends SerializerType<E>> {
        private final Registry<T> registry;
        private final String elementName;
        private final String typeKey;
        private final Function<E, T> typeGetter;
        @Nullable
        private Pair<T, DefaultSerializer<? extends E>> defaultType;
        
        private Builder(final Registry<T> gm, final String string2, final String string3, final Function<E, T> function) {
            this.registry = gm;
            this.elementName = string2;
            this.typeKey = string3;
            this.typeGetter = function;
        }
        
        public Object build() {
            return new JsonAdapter((Registry)this.registry, this.elementName, this.typeKey, (Function)this.typeGetter, (Pair)this.defaultType);
        }
    }
    
    static class JsonAdapter<E, T extends SerializerType<E>> implements JsonDeserializer<E>, JsonSerializer<E> {
        private final Registry<T> registry;
        private final String elementName;
        private final String typeKey;
        private final Function<E, T> typeGetter;
        @Nullable
        private final Pair<T, DefaultSerializer<? extends E>> defaultType;
        
        private JsonAdapter(final Registry<T> gm, final String string2, final String string3, final Function<E, T> function, @Nullable final Pair<T, DefaultSerializer<? extends E>> pair) {
            this.registry = gm;
            this.elementName = string2;
            this.typeKey = string3;
            this.typeGetter = function;
            this.defaultType = pair;
        }
        
        public E deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement.isJsonObject()) {
                final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, this.elementName);
                final ResourceLocation vk6 = new ResourceLocation(GsonHelper.getAsString(jsonObject5, this.typeKey));
                final T czc7 = this.registry.get(vk6);
                if (czc7 == null) {
                    throw new JsonSyntaxException(new StringBuilder().append("Unknown type '").append(vk6).append("'").toString());
                }
                return (E)czc7.getSerializer().deserialize(jsonObject5, jsonDeserializationContext);
            }
            else {
                if (this.defaultType == null) {
                    throw new UnsupportedOperationException(new StringBuilder().append("Object ").append(jsonElement).append(" can't be deserialized").toString());
                }
                return ((DefaultSerializer)this.defaultType.getSecond()).deserialize(jsonElement, jsonDeserializationContext);
            }
        }
        
        public JsonElement serialize(final E object, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final T czc5 = (T)this.typeGetter.apply(object);
            if (this.defaultType != null && this.defaultType.getFirst() == czc5) {
                return ((DefaultSerializer)this.defaultType.getSecond()).serialize(object, jsonSerializationContext);
            }
            if (czc5 == null) {
                throw new JsonSyntaxException(new StringBuilder().append("Unknown type: ").append(object).toString());
            }
            final JsonObject jsonObject6 = new JsonObject();
            jsonObject6.addProperty(this.typeKey, this.registry.getKey(czc5).toString());
            czc5.getSerializer().serialize(jsonObject6, (E)object, jsonSerializationContext);
            return (JsonElement)jsonObject6;
        }
    }
    
    public interface DefaultSerializer<T> {
        JsonElement serialize(final T object, final JsonSerializationContext jsonSerializationContext);
        
        T deserialize(final JsonElement jsonElement, final JsonDeserializationContext jsonDeserializationContext);
    }
}
