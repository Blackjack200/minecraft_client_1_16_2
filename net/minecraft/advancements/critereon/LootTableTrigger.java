package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class LootTableTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return LootTableTrigger.ID;
    }
    
    @Override
    protected TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ResourceLocation vk5 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "loot_table"));
        return new TriggerInstance(b, vk5);
    }
    
    public void trigger(final ServerPlayer aah, final ResourceLocation vk) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(vk)));
    }
    
    static {
        ID = new ResourceLocation("player_generates_container_loot");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ResourceLocation lootTable;
        
        public TriggerInstance(final EntityPredicate.Composite b, final ResourceLocation vk) {
            super(LootTableTrigger.ID, b);
            this.lootTable = vk;
        }
        
        public static TriggerInstance lootTableUsed(final ResourceLocation vk) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, vk);
        }
        
        public boolean matches(final ResourceLocation vk) {
            return this.lootTable.equals(vk);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.addProperty("loot_table", this.lootTable.toString());
            return jsonObject3;
        }
    }
}
