package net.minecraft.advancements.critereon;

import net.minecraft.advancements.CriterionTriggerInstance;
import com.google.gson.JsonObject;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.CriterionTrigger;

public class ImpossibleTrigger implements CriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ImpossibleTrigger.ID;
    }
    
    public void addPlayerListener(final PlayerAdvancements vt, final Listener<TriggerInstance> a) {
    }
    
    public void removePlayerListener(final PlayerAdvancements vt, final Listener<TriggerInstance> a) {
    }
    
    public void removePlayerListeners(final PlayerAdvancements vt) {
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final DeserializationContext ax) {
        return new TriggerInstance();
    }
    
    static {
        ID = new ResourceLocation("impossible");
    }
    
    public static class TriggerInstance implements CriterionTriggerInstance {
        public ResourceLocation getCriterion() {
            return ImpossibleTrigger.ID;
        }
        
        public JsonObject serializeToJson(final SerializationContext ci) {
            return new JsonObject();
        }
    }
}
