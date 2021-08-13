package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class LevitationTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return LevitationTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final DistancePredicate ay5 = DistancePredicate.fromJson(jsonObject.get("distance"));
        final MinMaxBounds.Ints d6 = MinMaxBounds.Ints.fromJson(jsonObject.get("duration"));
        return new TriggerInstance(b, ay5, d6);
    }
    
    public void trigger(final ServerPlayer aah, final Vec3 dck, final int integer) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah, dck, integer)));
    }
    
    static {
        ID = new ResourceLocation("levitation");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final DistancePredicate distance;
        private final MinMaxBounds.Ints duration;
        
        public TriggerInstance(final EntityPredicate.Composite b, final DistancePredicate ay, final MinMaxBounds.Ints d) {
            super(LevitationTrigger.ID, b);
            this.distance = ay;
            this.duration = d;
        }
        
        public static TriggerInstance levitated(final DistancePredicate ay) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, ay, MinMaxBounds.Ints.ANY);
        }
        
        public boolean matches(final ServerPlayer aah, final Vec3 dck, final int integer) {
            return this.distance.matches(dck.x, dck.y, dck.z, aah.getX(), aah.getY(), aah.getZ()) && this.duration.matches(integer);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("distance", this.distance.serializeToJson());
            jsonObject3.add("duration", this.duration.serializeToJson());
            return jsonObject3;
        }
    }
}
