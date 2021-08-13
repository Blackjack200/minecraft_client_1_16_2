package net.minecraft.advancements.critereon;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class KilledTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private final ResourceLocation id;
    
    public KilledTrigger(final ResourceLocation vk) {
        this.id = vk;
    }
    
    public ResourceLocation getId() {
        return this.id;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        return new TriggerInstance(this.id, b, EntityPredicate.Composite.fromJson(jsonObject, "entity", ax), DamageSourcePredicate.fromJson(jsonObject.get("killing_blow")));
    }
    
    public void trigger(final ServerPlayer aah, final Entity apx, final DamageSource aph) {
        final LootContext cys5 = EntityPredicate.createContext(aah, apx);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(aah, cys5, aph)));
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite entityPredicate;
        private final DamageSourcePredicate killingBlow;
        
        public TriggerInstance(final ResourceLocation vk, final EntityPredicate.Composite b2, final EntityPredicate.Composite b3, final DamageSourcePredicate aw) {
            super(vk, b2);
            this.entityPredicate = b3;
            this.killingBlow = aw;
        }
        
        public static TriggerInstance playerKilledEntity(final EntityPredicate.Builder a) {
            return new TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(a.build()), DamageSourcePredicate.ANY);
        }
        
        public static TriggerInstance playerKilledEntity() {
            return new TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, DamageSourcePredicate.ANY);
        }
        
        public static TriggerInstance playerKilledEntity(final EntityPredicate.Builder a, final DamageSourcePredicate.Builder a) {
            return new TriggerInstance(CriteriaTriggers.PLAYER_KILLED_ENTITY.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(a.build()), a.build());
        }
        
        public static TriggerInstance entityKilledPlayer() {
            return new TriggerInstance(CriteriaTriggers.ENTITY_KILLED_PLAYER.id, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, DamageSourcePredicate.ANY);
        }
        
        public boolean matches(final ServerPlayer aah, final LootContext cys, final DamageSource aph) {
            return this.killingBlow.matches(aah, aph) && this.entityPredicate.matches(cys);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("entity", this.entityPredicate.toJson(ci));
            jsonObject3.add("killing_blow", this.killingBlow.serializeToJson());
            return jsonObject3;
        }
    }
}
