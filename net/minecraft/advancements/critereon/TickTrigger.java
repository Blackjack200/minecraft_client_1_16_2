package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class TickTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    public static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return TickTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        return new TriggerInstance(b);
    }
    
    public void trigger(final ServerPlayer aah) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> true));
    }
    
    static {
        ID = new ResourceLocation("tick");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(final EntityPredicate.Composite b) {
            super(TickTrigger.ID, b);
        }
    }
}
