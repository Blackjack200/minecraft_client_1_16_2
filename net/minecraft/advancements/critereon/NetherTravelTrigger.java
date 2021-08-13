package net.minecraft.advancements.critereon;

import net.minecraft.server.level.ServerLevel;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class NetherTravelTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return NetherTravelTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final LocationPredicate bw5 = LocationPredicate.fromJson(jsonObject.get("entered"));
        final LocationPredicate bw6 = LocationPredicate.fromJson(jsonObject.get("exited"));
        final DistancePredicate ay7 = DistancePredicate.fromJson(jsonObject.get("distance"));
        return new TriggerInstance(b, bw5, bw6, ay7);
    }
    
    public void trigger(final ServerPlayer aah, final Vec3 dck) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah.getLevel(), dck, aah.getX(), aah.getY(), aah.getZ())));
    }
    
    static {
        ID = new ResourceLocation("nether_travel");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final LocationPredicate entered;
        private final LocationPredicate exited;
        private final DistancePredicate distance;
        
        public TriggerInstance(final EntityPredicate.Composite b, final LocationPredicate bw2, final LocationPredicate bw3, final DistancePredicate ay) {
            super(NetherTravelTrigger.ID, b);
            this.entered = bw2;
            this.exited = bw3;
            this.distance = ay;
        }
        
        public static TriggerInstance travelledThroughNether(final DistancePredicate ay) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, LocationPredicate.ANY, LocationPredicate.ANY, ay);
        }
        
        public boolean matches(final ServerLevel aag, final Vec3 dck, final double double3, final double double4, final double double5) {
            return this.entered.matches(aag, dck.x, dck.y, dck.z) && this.exited.matches(aag, double3, double4, double5) && this.distance.matches(dck.x, dck.y, dck.z, double3, double4, double5);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("entered", this.entered.serializeToJson());
            jsonObject3.add("exited", this.exited.serializeToJson());
            jsonObject3.add("distance", this.distance.serializeToJson());
            return jsonObject3;
        }
    }
}
