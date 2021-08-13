package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class ConditionReference implements LootItemCondition {
    private static final Logger LOGGER;
    private final ResourceLocation name;
    
    private ConditionReference(final ResourceLocation vk) {
        this.name = vk;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.REFERENCE;
    }
    
    public void validate(final ValidationContext czd) {
        if (czd.hasVisitedCondition(this.name)) {
            czd.reportProblem(new StringBuilder().append("Condition ").append(this.name).append(" is recursively called").toString());
            return;
        }
        super.validate(czd);
        final LootItemCondition dbl3 = czd.resolveCondition(this.name);
        if (dbl3 == null) {
            czd.reportProblem(new StringBuilder().append("Unknown condition table called ").append(this.name).toString());
        }
        else {
            dbl3.validate(czd.enterTable(new StringBuilder().append(".{").append(this.name).append("}").toString(), this.name));
        }
    }
    
    public boolean test(final LootContext cys) {
        final LootItemCondition dbl3 = cys.getCondition(this.name);
        if (cys.addVisitedCondition(dbl3)) {
            try {
                return dbl3.test(cys);
            }
            finally {
                cys.removeVisitedCondition(dbl3);
            }
        }
        ConditionReference.LOGGER.warn("Detected infinite loop in loot tables");
        return false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ConditionReference> {
        public void serialize(final JsonObject jsonObject, final ConditionReference dbd, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("name", dbd.name.toString());
        }
        
        public ConditionReference deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final ResourceLocation vk4 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "name"));
            return new ConditionReference(vk4, null);
        }
    }
}
