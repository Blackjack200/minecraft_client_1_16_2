package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class EnchantedItemTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return EnchantedItemTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ItemPredicate bq5 = ItemPredicate.fromJson(jsonObject.get("item"));
        final MinMaxBounds.Ints d6 = MinMaxBounds.Ints.fromJson(jsonObject.get("levels"));
        return new TriggerInstance(b, bq5, d6);
    }
    
    public void trigger(final ServerPlayer aah, final ItemStack bly, final int integer) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bly, integer)));
    }
    
    static {
        ID = new ResourceLocation("enchanted_item");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        private final MinMaxBounds.Ints levels;
        
        public TriggerInstance(final EntityPredicate.Composite b, final ItemPredicate bq, final MinMaxBounds.Ints d) {
            super(EnchantedItemTrigger.ID, b);
            this.item = bq;
            this.levels = d;
        }
        
        public static TriggerInstance enchantedItem() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, MinMaxBounds.Ints.ANY);
        }
        
        public boolean matches(final ItemStack bly, final int integer) {
            return this.item.matches(bly) && this.levels.matches(integer);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("item", this.item.serializeToJson());
            jsonObject3.add("levels", this.levels.serializeToJson());
            return jsonObject3;
        }
    }
}
