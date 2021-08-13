package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ConstructBeaconTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ConstructBeaconTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final MinMaxBounds.Ints d5 = MinMaxBounds.Ints.fromJson(jsonObject.get("level"));
        return new TriggerInstance(b, d5);
    }
    
    public void trigger(final ServerPlayer aah, final BeaconBlockEntity ccb) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(ccb)));
    }
    
    static {
        ID = new ResourceLocation("construct_beacon");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints level;
        
        public TriggerInstance(final EntityPredicate.Composite b, final MinMaxBounds.Ints d) {
            super(ConstructBeaconTrigger.ID, b);
            this.level = d;
        }
        
        public static TriggerInstance constructedBeacon(final MinMaxBounds.Ints d) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, d);
        }
        
        public boolean matches(final BeaconBlockEntity ccb) {
            return this.level.matches(ccb.getLevels());
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("level", this.level.serializeToJson());
            return jsonObject3;
        }
    }
}
