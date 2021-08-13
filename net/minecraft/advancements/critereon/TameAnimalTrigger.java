package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class TameAnimalTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return TameAnimalTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "entity", ax);
        return new TriggerInstance(b, b2);
    }
    
    public void trigger(final ServerPlayer aah, final Animal azw) {
        final LootContext cys4 = EntityPredicate.createContext(aah, azw);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cys4)));
    }
    
    static {
        ID = new ResourceLocation("tame_animal");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite entity;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final EntityPredicate.Composite b2) {
            super(TameAnimalTrigger.ID, b1);
            this.entity = b2;
        }
        
        public static TriggerInstance tamedAnimal() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
        }
        
        public static TriggerInstance tamedAnimal(final EntityPredicate bg) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(bg));
        }
        
        public boolean matches(final LootContext cys) {
            return this.entity.matches(cys);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("entity", this.entity.toJson(ci));
            return jsonObject3;
        }
    }
}
