package net.minecraft.advancements.critereon;

import com.google.gson.JsonSyntaxException;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;
import net.minecraft.world.level.block.state.StateDefinition;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class SlideDownBlockTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return SlideDownBlockTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final Block bul5 = deserializeBlock(jsonObject);
        final StatePropertiesPredicate cm6 = StatePropertiesPredicate.fromJson(jsonObject.get("state"));
        if (bul5 != null) {
            cm6.checkState(bul5.getStateDefinition(), (Consumer<String>)(string -> {
                throw new JsonSyntaxException(new StringBuilder().append("Block ").append(bul5).append(" has no property ").append(string).toString());
            }));
        }
        return new TriggerInstance(b, bul5, cm6);
    }
    
    @Nullable
    private static Block deserializeBlock(final JsonObject jsonObject) {
        if (jsonObject.has("block")) {
            final ResourceLocation vk2 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block"));
            return (Block)Registry.BLOCK.getOptional(vk2).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown block type '").append(vk2).append("'").toString()));
        }
        return null;
    }
    
    public void trigger(final ServerPlayer aah, final BlockState cee) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cee)));
    }
    
    static {
        ID = new ResourceLocation("slide_down_block");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final Block block;
        private final StatePropertiesPredicate state;
        
        public TriggerInstance(final EntityPredicate.Composite b, @Nullable final Block bul, final StatePropertiesPredicate cm) {
            super(SlideDownBlockTrigger.ID, b);
            this.block = bul;
            this.state = cm;
        }
        
        public static TriggerInstance slidesDownBlock(final Block bul) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, bul, StatePropertiesPredicate.ANY);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            if (this.block != null) {
                jsonObject3.addProperty("block", Registry.BLOCK.getKey(this.block).toString());
            }
            jsonObject3.add("state", this.state.serializeToJson());
            return jsonObject3;
        }
        
        public boolean matches(final BlockState cee) {
            return (this.block == null || cee.is(this.block)) && this.state.matches(cee);
        }
    }
}
