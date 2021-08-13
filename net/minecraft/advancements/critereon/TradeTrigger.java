package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class TradeTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return TradeTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "villager", ax);
        final ItemPredicate bq6 = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(b, b2, bq6);
    }
    
    public void trigger(final ServerPlayer aah, final AbstractVillager bfb, final ItemStack bly) {
        final LootContext cys5 = EntityPredicate.createContext(aah, bfb);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cys5, bly)));
    }
    
    static {
        ID = new ResourceLocation("villager_trade");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite villager;
        private final ItemPredicate item;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final EntityPredicate.Composite b2, final ItemPredicate bq) {
            super(TradeTrigger.ID, b1);
            this.villager = b2;
            this.item = bq;
        }
        
        public static TriggerInstance tradedWithVillager() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, ItemPredicate.ANY);
        }
        
        public boolean matches(final LootContext cys, final ItemStack bly) {
            return this.villager.matches(cys) && this.item.matches(bly);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("item", this.item.serializeToJson());
            jsonObject3.add("villager", this.villager.toJson(ci));
            return jsonObject3;
        }
    }
}
