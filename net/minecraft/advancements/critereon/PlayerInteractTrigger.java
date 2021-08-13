package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class PlayerInteractTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return PlayerInteractTrigger.ID;
    }
    
    @Override
    protected TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ItemPredicate bq5 = ItemPredicate.fromJson(jsonObject.get("item"));
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "entity", ax);
        return new TriggerInstance(b, bq5, b2);
    }
    
    public void trigger(final ServerPlayer aah, final ItemStack bly, final Entity apx) {
        final LootContext cys5 = EntityPredicate.createContext(aah, apx);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bly, cys5)));
    }
    
    static {
        ID = new ResourceLocation("player_interacted_with_entity");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        private final EntityPredicate.Composite entity;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final ItemPredicate bq, final EntityPredicate.Composite b3) {
            super(PlayerInteractTrigger.ID, b1);
            this.item = bq;
            this.entity = b3;
        }
        
        public static TriggerInstance itemUsedOnEntity(final EntityPredicate.Composite b1, final ItemPredicate.Builder a, final EntityPredicate.Composite b3) {
            return new TriggerInstance(b1, a.build(), b3);
        }
        
        public boolean matches(final ItemStack bly, final LootContext cys) {
            return this.item.matches(bly) && this.entity.matches(cys);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("item", this.item.serializeToJson());
            jsonObject3.add("entity", this.entity.toJson(ci));
            return jsonObject3;
        }
    }
}
