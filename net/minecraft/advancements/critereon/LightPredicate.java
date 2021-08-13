package net.minecraft.advancements.critereon;

import net.minecraft.util.GsonHelper;
import javax.annotation.Nullable;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class LightPredicate {
    public static final LightPredicate ANY;
    private final MinMaxBounds.Ints composite;
    
    private LightPredicate(final MinMaxBounds.Ints d) {
        this.composite = d;
    }
    
    public boolean matches(final ServerLevel aag, final BlockPos fx) {
        return this == LightPredicate.ANY || (aag.isLoaded(fx) && this.composite.matches(aag.getMaxLocalRawBrightness(fx)));
    }
    
    public JsonElement serializeToJson() {
        if (this == LightPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("light", this.composite.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    public static LightPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return LightPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "light");
        final MinMaxBounds.Ints d3 = MinMaxBounds.Ints.fromJson(jsonObject2.get("light"));
        return new LightPredicate(d3);
    }
    
    static {
        ANY = new LightPredicate(MinMaxBounds.Ints.ANY);
    }
}
