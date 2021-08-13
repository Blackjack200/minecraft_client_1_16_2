package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ItemDurabilityTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ItemDurabilityTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ItemPredicate bq5 = ItemPredicate.fromJson(jsonObject.get("item"));
        final MinMaxBounds.Ints d6 = MinMaxBounds.Ints.fromJson(jsonObject.get("durability"));
        final MinMaxBounds.Ints d7 = MinMaxBounds.Ints.fromJson(jsonObject.get("delta"));
        return new TriggerInstance(b, bq5, d6, d7);
    }
    
    public void trigger(final ServerPlayer aah, final ItemStack bly, final int integer) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bly, integer)));
    }
    
    static {
        ID = new ResourceLocation("item_durability_changed");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        private final MinMaxBounds.Ints durability;
        private final MinMaxBounds.Ints delta;
        
        public TriggerInstance(final EntityPredicate.Composite b, final ItemPredicate bq, final MinMaxBounds.Ints d3, final MinMaxBounds.Ints d4) {
            super(ItemDurabilityTrigger.ID, b);
            this.item = bq;
            this.durability = d3;
            this.delta = d4;
        }
        
        public static TriggerInstance changedDurability(final EntityPredicate.Composite b, final ItemPredicate bq, final MinMaxBounds.Ints d) {
            return new TriggerInstance(b, bq, d, MinMaxBounds.Ints.ANY);
        }
        
        public boolean matches(final ItemStack bly, final int integer) {
            return this.item.matches(bly) && this.durability.matches(bly.getMaxDamage() - integer) && this.delta.matches(bly.getDamageValue() - integer);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("item", this.item.serializeToJson());
            jsonObject3.add("durability", this.durability.serializeToJson());
            jsonObject3.add("delta", this.delta.serializeToJson());
            return jsonObject3;
        }
    }
}
