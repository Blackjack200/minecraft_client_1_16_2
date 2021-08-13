package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;

public interface Serializer<T> {
    void serialize(final JsonObject jsonObject, final T object, final JsonSerializationContext jsonSerializationContext);
    
    T deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext);
}
