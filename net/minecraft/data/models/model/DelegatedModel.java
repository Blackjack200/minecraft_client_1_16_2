package net.minecraft.data.models.model;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonElement;
import java.util.function.Supplier;

public class DelegatedModel implements Supplier<JsonElement> {
    private final ResourceLocation parent;
    
    public DelegatedModel(final ResourceLocation vk) {
        this.parent = vk;
    }
    
    public JsonElement get() {
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("parent", this.parent.toString());
        return (JsonElement)jsonObject2;
    }
}
