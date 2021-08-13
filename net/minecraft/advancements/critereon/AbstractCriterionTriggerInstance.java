package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.CriterionTriggerInstance;

public abstract class AbstractCriterionTriggerInstance implements CriterionTriggerInstance {
    private final ResourceLocation criterion;
    private final EntityPredicate.Composite player;
    
    public AbstractCriterionTriggerInstance(final ResourceLocation vk, final EntityPredicate.Composite b) {
        this.criterion = vk;
        this.player = b;
    }
    
    public ResourceLocation getCriterion() {
        return this.criterion;
    }
    
    protected EntityPredicate.Composite getPlayerPredicate() {
        return this.player;
    }
    
    public JsonObject serializeToJson(final SerializationContext ci) {
        final JsonObject jsonObject3 = new JsonObject();
        jsonObject3.add("player", this.player.toJson(ci));
        return jsonObject3;
    }
    
    public String toString() {
        return new StringBuilder().append("AbstractCriterionInstance{criterion=").append(this.criterion).append('}').toString();
    }
}
