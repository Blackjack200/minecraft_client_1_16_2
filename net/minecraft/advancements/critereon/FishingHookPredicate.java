package net.minecraft.advancements.critereon;

import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.Entity;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;

public class FishingHookPredicate {
    public static final FishingHookPredicate ANY;
    private boolean inOpenWater;
    
    private FishingHookPredicate(final boolean boolean1) {
        this.inOpenWater = boolean1;
    }
    
    public static FishingHookPredicate inOpenWater(final boolean boolean1) {
        return new FishingHookPredicate(boolean1);
    }
    
    public static FishingHookPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return FishingHookPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "fishing_hook");
        final JsonElement jsonElement2 = jsonObject2.get("in_open_water");
        if (jsonElement2 != null) {
            return new FishingHookPredicate(GsonHelper.convertToBoolean(jsonElement2, "in_open_water"));
        }
        return FishingHookPredicate.ANY;
    }
    
    public JsonElement serializeToJson() {
        if (this == FishingHookPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("in_open_water", (JsonElement)new JsonPrimitive(Boolean.valueOf(this.inOpenWater)));
        return (JsonElement)jsonObject2;
    }
    
    public boolean matches(final Entity apx) {
        if (this == FishingHookPredicate.ANY) {
            return true;
        }
        if (!(apx instanceof FishingHook)) {
            return false;
        }
        final FishingHook bgf3 = (FishingHook)apx;
        return this.inOpenWater == bgf3.isOpenWaterFishing();
    }
    
    static {
        ANY = new FishingHookPredicate(false);
    }
}
