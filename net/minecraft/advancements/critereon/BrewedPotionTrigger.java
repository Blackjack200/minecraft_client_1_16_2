package net.minecraft.advancements.critereon;

import javax.annotation.Nullable;
import com.google.gson.JsonSyntaxException;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.Registry;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class BrewedPotionTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return BrewedPotionTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        Potion bnq5 = null;
        if (jsonObject.has("potion")) {
            final ResourceLocation vk6 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "potion"));
            bnq5 = (Potion)Registry.POTION.getOptional(vk6).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown potion '").append(vk6).append("'").toString()));
        }
        return new TriggerInstance(b, bnq5);
    }
    
    public void trigger(final ServerPlayer aah, final Potion bnq) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bnq)));
    }
    
    static {
        ID = new ResourceLocation("brewed_potion");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final Potion potion;
        
        public TriggerInstance(final EntityPredicate.Composite b, @Nullable final Potion bnq) {
            super(BrewedPotionTrigger.ID, b);
            this.potion = bnq;
        }
        
        public static TriggerInstance brewedPotion() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, null);
        }
        
        public boolean matches(final Potion bnq) {
            return this.potion == null || this.potion == bnq;
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            if (this.potion != null) {
                jsonObject3.addProperty("potion", Registry.POTION.getKey(this.potion).toString());
            }
            return jsonObject3;
        }
    }
}
