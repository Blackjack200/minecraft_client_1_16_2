package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.critereon.LocationPredicate;

public class LocationCheck implements LootItemCondition {
    private final LocationPredicate predicate;
    private final BlockPos offset;
    
    private LocationCheck(final LocationPredicate bw, final BlockPos fx) {
        this.predicate = bw;
        this.offset = fx;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.LOCATION_CHECK;
    }
    
    public boolean test(final LootContext cys) {
        final Vec3 dck3 = cys.<Vec3>getParamOrNull(LootContextParams.ORIGIN);
        return dck3 != null && this.predicate.matches(cys.getLevel(), dck3.x() + this.offset.getX(), dck3.y() + this.offset.getY(), dck3.z() + this.offset.getZ());
    }
    
    public static Builder checkLocation(final LocationPredicate.Builder a) {
        return () -> new LocationCheck(a.build(), BlockPos.ZERO);
    }
    
    public static Builder checkLocation(final LocationPredicate.Builder a, final BlockPos fx) {
        return () -> new LocationCheck(a.build(), fx);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LocationCheck> {
        public void serialize(final JsonObject jsonObject, final LocationCheck dbj, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("predicate", dbj.predicate.serializeToJson());
            if (dbj.offset.getX() != 0) {
                jsonObject.addProperty("offsetX", (Number)dbj.offset.getX());
            }
            if (dbj.offset.getY() != 0) {
                jsonObject.addProperty("offsetY", (Number)dbj.offset.getY());
            }
            if (dbj.offset.getZ() != 0) {
                jsonObject.addProperty("offsetZ", (Number)dbj.offset.getZ());
            }
        }
        
        public LocationCheck deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final LocationPredicate bw4 = LocationPredicate.fromJson(jsonObject.get("predicate"));
            final int integer5 = GsonHelper.getAsInt(jsonObject, "offsetX", 0);
            final int integer6 = GsonHelper.getAsInt(jsonObject, "offsetY", 0);
            final int integer7 = GsonHelper.getAsInt(jsonObject, "offsetZ", 0);
            return new LocationCheck(bw4, new BlockPos(integer5, integer6, integer7), null);
        }
    }
}
