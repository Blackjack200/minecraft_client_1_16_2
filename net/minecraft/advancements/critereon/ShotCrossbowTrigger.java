package net.minecraft.advancements.critereon;

import net.minecraft.world.level.ItemLike;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ShotCrossbowTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ShotCrossbowTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ItemPredicate bq5 = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(b, bq5);
    }
    
    public void trigger(final ServerPlayer aah, final ItemStack bly) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bly)));
    }
    
    static {
        ID = new ResourceLocation("shot_crossbow");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;
        
        public TriggerInstance(final EntityPredicate.Composite b, final ItemPredicate bq) {
            super(ShotCrossbowTrigger.ID, b);
            this.item = bq;
        }
        
        public static TriggerInstance shotCrossbow(final ItemLike brt) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.Builder.item().of(brt).build());
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
