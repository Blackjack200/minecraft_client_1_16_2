package net.minecraft.advancements.critereon;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ConsumeItemTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ConsumeItemTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        return new TriggerInstance(b, ItemPredicate.fromJson(jsonObject.get("item")));
    }
    
    public void trigger(final ServerPlayer aah, final ItemStack bly) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bly)));
    }
    
    static {
        ID = new ResourceLocation("consume_item");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        
        public TriggerInstance(final EntityPredicate.Composite b, final ItemPredicate bq) {
            super(ConsumeItemTrigger.ID, b);
            this.item = bq;
        }
        
        public static TriggerInstance usedItem() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY);
        }
        
        public static TriggerInstance usedItem(final ItemLike brt) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, new ItemPredicate(null, brt.asItem(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY));
        }
        
        public boolean matches(final ItemStack bly) {
            return this.item.matches(bly);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("item", this.item.serializeToJson());
            return jsonObject3;
        }
    }
}
