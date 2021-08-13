package net.minecraft.advancements.critereon;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ItemUsedOnBlockTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ItemUsedOnBlockTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final LocationPredicate bw5 = LocationPredicate.fromJson(jsonObject.get("location"));
        final ItemPredicate bq6 = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(b, bw5, bq6);
    }
    
    public void trigger(final ServerPlayer aah, final BlockPos fx, final ItemStack bly) {
        final BlockState cee5 = aah.getLevel().getBlockState(fx);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cee5, aah.getLevel(), fx, bly)));
    }
    
    static {
        ID = new ResourceLocation("item_used_on_block");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final LocationPredicate location;
        private final ItemPredicate item;
        
        public TriggerInstance(final EntityPredicate.Composite b, final LocationPredicate bw, final ItemPredicate bq) {
            super(ItemUsedOnBlockTrigger.ID, b);
            this.location = bw;
            this.item = bq;
        }
        
        public static TriggerInstance itemUsedOnBlock(final LocationPredicate.Builder a, final ItemPredicate.Builder a) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, a.build(), a.build());
        }
        
        public boolean matches(final BlockState cee, final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
            return this.location.matches(aag, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5) && this.item.matches(bly);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("location", this.location.serializeToJson());
            jsonObject3.add("item", this.item.serializeToJson());
            return jsonObject3;
        }
    }
}
