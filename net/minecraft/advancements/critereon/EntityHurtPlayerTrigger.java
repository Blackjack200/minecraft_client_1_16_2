package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class EntityHurtPlayerTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return EntityHurtPlayerTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final DamagePredicate av5 = DamagePredicate.fromJson(jsonObject.get("damage"));
        return new TriggerInstance(b, av5);
    }
    
    public void trigger(final ServerPlayer aah, final DamageSource aph, final float float3, final float float4, final boolean boolean5) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah, aph, float3, float4, boolean5)));
    }
    
    static {
        ID = new ResourceLocation("entity_hurt_player");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final DamagePredicate damage;
        
        public TriggerInstance(final EntityPredicate.Composite b, final DamagePredicate av) {
            super(EntityHurtPlayerTrigger.ID, b);
            this.damage = av;
        }
        
        public static TriggerInstance entityHurtPlayer(final DamagePredicate.Builder a) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, a.build());
        }
        
        public boolean matches(final ServerPlayer aah, final DamageSource aph, final float float3, final float float4, final boolean boolean5) {
            return this.damage.matches(aah, aph, float3, float4, boolean5);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("damage", this.damage.serializeToJson());
            return jsonObject3;
        }
    }
}
