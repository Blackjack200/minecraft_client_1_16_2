package net.minecraft.advancements.critereon;

import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class EffectsChangedTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return EffectsChangedTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final MobEffectsPredicate ca5 = MobEffectsPredicate.fromJson(jsonObject.get("effects"));
        return new TriggerInstance(b, ca5);
    }
    
    public void trigger(final ServerPlayer aah) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah)));
    }
    
    static {
        ID = new ResourceLocation("effects_changed");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MobEffectsPredicate effects;
        
        public TriggerInstance(final EntityPredicate.Composite b, final MobEffectsPredicate ca) {
            super(EffectsChangedTrigger.ID, b);
            this.effects = ca;
        }
        
        public static TriggerInstance hasEffects(final MobEffectsPredicate ca) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, ca);
        }
        
        public boolean matches(final ServerPlayer aah) {
            return this.effects.matches(aah);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("effects", this.effects.serializeToJson());
            return jsonObject3;
        }
    }
}
