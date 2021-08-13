package net.minecraft.advancements.critereon;

import net.minecraft.server.level.ServerLevel;
import com.google.gson.JsonSyntaxException;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;
import net.minecraft.world.level.block.state.StateDefinition;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class PlacedBlockTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return PlacedBlockTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final Block bul5 = deserializeBlock(jsonObject);
        final StatePropertiesPredicate cm6 = StatePropertiesPredicate.fromJson(jsonObject.get("state"));
        if (bul5 != null) {
            cm6.checkState(bul5.getStateDefinition(), (Consumer<String>)(string -> {
                throw new JsonSyntaxException(new StringBuilder().append("Block ").append(bul5).append(" has no property ").append(string).append(":").toString());
            }));
        }
        final LocationPredicate bw7 = LocationPredicate.fromJson(jsonObject.get("location"));
        final ItemPredicate bq8 = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(b, bul5, cm6, bw7, bq8);
    }
    
    @Nullable
    private static Block deserializeBlock(final JsonObject jsonObject) {
        if (jsonObject.has("block")) {
            final ResourceLocation vk2 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block"));
            return (Block)Registry.BLOCK.getOptional(vk2).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown block type '").append(vk2).append("'").toString()));
        }
        return null;
    }
    
    public void trigger(final ServerPlayer aah, final BlockPos fx, final ItemStack bly) {
        final BlockState cee5 = aah.getLevel().getBlockState(fx);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cee5, fx, aah.getLevel(), bly)));
    }
    
    static {
        ID = new ResourceLocation("placed_block");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final Block block;
        private final StatePropertiesPredicate state;
        private final LocationPredicate location;
        private final ItemPredicate item;
        
        public TriggerInstance(final EntityPredicate.Composite b, @Nullable final Block bul, final StatePropertiesPredicate cm, final LocationPredicate bw, final ItemPredicate bq) {
            super(PlacedBlockTrigger.ID, b);
            this.block = bul;
            this.state = cm;
            this.location = bw;
            this.item = bq;
        }
        
        public static TriggerInstance placedBlock(final Block bul) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, bul, StatePropertiesPredicate.ANY, LocationPredicate.ANY, ItemPredicate.ANY);
        }
        
        public boolean matches(final BlockState cee, final BlockPos fx, final ServerLevel aag, final ItemStack bly) {
            return (this.block == null || cee.is(this.block)) && this.state.matches(cee) && this.location.matches(aag, (float)fx.getX(), (float)fx.getY(), (float)fx.getZ()) && this.item.matches(bly);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            if (this.block != null) {
                jsonObject3.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
            }
            jsonObject3.add("state", this.state.serializeToJson());
            jsonObject3.add("location", this.location.serializeToJson());
            jsonObject3.add("item", this.item.serializeToJson());
            return jsonObject3;
        }
    }
}
