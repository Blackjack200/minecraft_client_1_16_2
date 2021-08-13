package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class RecipeUnlockedTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return RecipeUnlockedTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ResourceLocation vk5 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "recipe"));
        return new TriggerInstance(b, vk5);
    }
    
    public void trigger(final ServerPlayer aah, final Recipe<?> bon) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bon)));
    }
    
    public static TriggerInstance unlocked(final ResourceLocation vk) {
        return new TriggerInstance(EntityPredicate.Composite.ANY, vk);
    }
    
    static {
        ID = new ResourceLocation("recipe_unlocked");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ResourceLocation recipe;
        
        public TriggerInstance(final EntityPredicate.Composite b, final ResourceLocation vk) {
            super(RecipeUnlockedTrigger.ID, b);
            this.recipe = vk;
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.addProperty("recipe", this.recipe.toString());
            return jsonObject3;
        }
        
        public boolean matches(final Recipe<?> bon) {
            return this.recipe.equals(bon.getId());
        }
    }
}
