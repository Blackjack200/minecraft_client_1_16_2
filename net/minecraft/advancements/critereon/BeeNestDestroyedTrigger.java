package net.minecraft.advancements.critereon;

import com.google.gson.JsonSyntaxException;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class BeeNestDestroyedTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return BeeNestDestroyedTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final Block bul5 = deserializeBlock(jsonObject);
        final ItemPredicate bq6 = ItemPredicate.fromJson(jsonObject.get("item"));
        final MinMaxBounds.Ints d7 = MinMaxBounds.Ints.fromJson(jsonObject.get("num_bees_inside"));
        return new TriggerInstance(b, bul5, bq6, d7);
    }
    
    @Nullable
    private static Block deserializeBlock(final JsonObject jsonObject) {
        if (jsonObject.has("block")) {
            final ResourceLocation vk2 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block"));
            return (Block)Registry.BLOCK.getOptional(vk2).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown block type '").append(vk2).append("'").toString()));
        }
        return null;
    }
    
    public void trigger(final ServerPlayer aah, final Block bul, final ItemStack bly, final int integer) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bul, bly, integer)));
    }
    
    static {
        ID = new ResourceLocation("bee_nest_destroyed");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        @Nullable
        private final Block block;
        private final ItemPredicate item;
        private final MinMaxBounds.Ints numBees;
        
        public TriggerInstance(final EntityPredicate.Composite b, @Nullable final Block bul, final ItemPredicate bq, final MinMaxBounds.Ints d) {
            super(BeeNestDestroyedTrigger.ID, b);
            this.block = bul;
            this.item = bq;
            this.numBees = d;
        }
        
        public static TriggerInstance destroyedBeeNest(final Block bul, final ItemPredicate.Builder a, final MinMaxBounds.Ints d) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, bul, a.build(), d);
        }
        
        public boolean matches(final Block bul, final ItemStack bly, final int integer) {
            return (this.block == null || bul == this.block) && this.item.matches(bly) && this.numBees.matches(integer);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            if (this.block != null) {
                jsonObject3.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
            }
            jsonObject3.add("item", this.item.serializeToJson());
            jsonObject3.add("num_bees_inside", this.numBees.serializeToJson());
            return jsonObject3;
        }
    }
}
