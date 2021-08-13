package net.minecraft.advancements.critereon;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import net.minecraft.util.Mth;

public class DistancePredicate {
    public static final DistancePredicate ANY;
    private final MinMaxBounds.Floats x;
    private final MinMaxBounds.Floats y;
    private final MinMaxBounds.Floats z;
    private final MinMaxBounds.Floats horizontal;
    private final MinMaxBounds.Floats absolute;
    
    public DistancePredicate(final MinMaxBounds.Floats c1, final MinMaxBounds.Floats c2, final MinMaxBounds.Floats c3, final MinMaxBounds.Floats c4, final MinMaxBounds.Floats c5) {
        this.x = c1;
        this.y = c2;
        this.z = c3;
        this.horizontal = c4;
        this.absolute = c5;
    }
    
    public static DistancePredicate horizontal(final MinMaxBounds.Floats c) {
        return new DistancePredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, c, MinMaxBounds.Floats.ANY);
    }
    
    public static DistancePredicate vertical(final MinMaxBounds.Floats c) {
        return new DistancePredicate(MinMaxBounds.Floats.ANY, c, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY);
    }
    
    public boolean matches(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6) {
        final float float14 = (float)(double1 - double4);
        final float float15 = (float)(double2 - double5);
        final float float16 = (float)(double3 - double6);
        return this.x.matches(Mth.abs(float14)) && this.y.matches(Mth.abs(float15)) && this.z.matches(Mth.abs(float16)) && this.horizontal.matchesSqr(float14 * float14 + float16 * float16) && this.absolute.matchesSqr(float14 * float14 + float15 * float15 + float16 * float16);
    }
    
    public static DistancePredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return DistancePredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "distance");
        final MinMaxBounds.Floats c3 = MinMaxBounds.Floats.fromJson(jsonObject2.get("x"));
        final MinMaxBounds.Floats c4 = MinMaxBounds.Floats.fromJson(jsonObject2.get("y"));
        final MinMaxBounds.Floats c5 = MinMaxBounds.Floats.fromJson(jsonObject2.get("z"));
        final MinMaxBounds.Floats c6 = MinMaxBounds.Floats.fromJson(jsonObject2.get("horizontal"));
        final MinMaxBounds.Floats c7 = MinMaxBounds.Floats.fromJson(jsonObject2.get("absolute"));
        return new DistancePredicate(c3, c4, c5, c6, c7);
    }
    
    public JsonElement serializeToJson() {
        if (this == DistancePredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("x", this.x.serializeToJson());
        jsonObject2.add("y", this.y.serializeToJson());
        jsonObject2.add("z", this.z.serializeToJson());
        jsonObject2.add("horizontal", this.horizontal.serializeToJson());
        jsonObject2.add("absolute", this.absolute.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new DistancePredicate(MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY, MinMaxBounds.Floats.ANY);
    }
}
