package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class PlayerHurtEntityTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return PlayerHurtEntityTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final DamagePredicate av5 = DamagePredicate.fromJson(jsonObject.get("damage"));
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "entity", ax);
        return new TriggerInstance(b, av5, b2);
    }
    
    public void trigger(final ServerPlayer aah, final Entity apx, final DamageSource aph, final float float4, final float float5, final boolean boolean6) {
        final LootContext cys8 = EntityPredicate.createContext(aah, apx);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah, cys8, aph, float4, float5, boolean6)));
    }
    
    static {
        ID = new ResourceLocation("player_hurt_entity");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final DamagePredicate damage;
        private final EntityPredicate.Composite entity;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final DamagePredicate av, final EntityPredicate.Composite b3) {
            super(PlayerHurtEntityTrigger.ID, b1);
            this.damage = av;
            this.entity = b3;
        }
        
        public static TriggerInstance playerHurtEntity(final DamagePredicate.Builder a) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, a.build(), EntityPredicate.Composite.ANY);
        }
        
        public boolean matches(final ServerPlayer aah, final LootContext cys, final DamageSource aph, final float float4, final float float5, final boolean boolean6) {
            return this.damage.matches(aah, aph, float4, float5, boolean6) && this.entity.matches(cys);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("damage", this.damage.serializeToJson());
            jsonObject3.add("entity", this.entity.toJson(ci));
            return jsonObject3;
        }
    }
}
