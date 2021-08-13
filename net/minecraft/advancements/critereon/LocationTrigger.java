package net.minecraft.advancements.critereon;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.advancements.CriteriaTriggers;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class LocationTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private final ResourceLocation id;
    
    public LocationTrigger(final ResourceLocation vk) {
        this.id = vk;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "location", jsonObject);
        final LocationPredicate bw6 = LocationPredicate.fromJson((JsonElement)jsonObject2);
        return new TriggerInstance(this.id, b, bw6);
    }
    
    public void trigger(final ServerPlayer aah) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah.getLevel(), aah.getX(), aah.getY(), aah.getZ())));
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final LocationPredicate location;
        
        public TriggerInstance(final ResourceLocation vk, final EntityPredicate.Composite b, final LocationPredicate bw) {
            super(vk, b);
            this.location = bw;
        }
        
        public static TriggerInstance located(final LocationPredicate bw) {
            return new TriggerInstance(CriteriaTriggers.LOCATION.id, EntityPredicate.Composite.ANY, bw);
        }
        
        public static TriggerInstance sleptInBed() {
            return new TriggerInstance(CriteriaTriggers.SLEPT_IN_BED.id, EntityPredicate.Composite.ANY, LocationPredicate.ANY);
        }
        
        public static TriggerInstance raidWon() {
            return new TriggerInstance(CriteriaTriggers.RAID_WIN.id, EntityPredicate.Composite.ANY, LocationPredicate.ANY);
        }
        
        public boolean matches(final ServerLevel aag, final double double2, final double double3, final double double4) {
            return this.location.matches(aag, double2, double3, double4);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("location", this.location.serializeToJson());
            return jsonObject3;
        }
    }
}
