package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class TargetBlockTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return TargetBlockTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final MinMaxBounds.Ints d5 = MinMaxBounds.Ints.fromJson(jsonObject.get("signal_strength"));
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "projectile", ax);
        return new TriggerInstance(b, d5, b2);
    }
    
    public void trigger(final ServerPlayer aah, final Entity apx, final Vec3 dck, final int integer) {
        final LootContext cys6 = EntityPredicate.createContext(aah, apx);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cys6, dck, integer)));
    }
    
    static {
        ID = new ResourceLocation("target_hit");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints signalStrength;
        private final EntityPredicate.Composite projectile;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final MinMaxBounds.Ints d, final EntityPredicate.Composite b3) {
            super(TargetBlockTrigger.ID, b1);
            this.signalStrength = d;
            this.projectile = b3;
        }
        
        public static TriggerInstance targetHit(final MinMaxBounds.Ints d, final EntityPredicate.Composite b) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, d, b);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("signal_strength", this.signalStrength.serializeToJson());
            jsonObject3.add("projectile", this.projectile.toJson(ci));
            return jsonObject3;
        }
        
        public boolean matches(final LootContext cys, final Vec3 dck, final int integer) {
            return this.signalStrength.matches(integer) && this.projectile.matches(cys);
        }
    }
}
