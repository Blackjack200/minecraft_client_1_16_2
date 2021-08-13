package net.minecraft.advancements.critereon;

import com.google.gson.JsonElement;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.Deserializers;
import com.google.gson.Gson;

public class SerializationContext {
    public static final SerializationContext INSTANCE;
    private final Gson predicateGson;
    
    public SerializationContext() {
        this.predicateGson = Deserializers.createConditionSerializer().create();
    }
    
    public final JsonElement serializeConditions(final LootItemCondition[] arr) {
        return this.predicateGson.toJsonTree(arr);
    }
    
    static {
        INSTANCE = new SerializationContext();
    }
}
