package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class UsedEnderEyeTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return UsedEnderEyeTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final MinMaxBounds.Floats c5 = MinMaxBounds.Floats.fromJson(jsonObject.get("distance"));
        return new TriggerInstance(b, c5);
    }
    
    public void trigger(final ServerPlayer aah, final BlockPos fx) {
        final double double4 = aah.getX() - fx.getX();
        final double double5 = aah.getZ() - fx.getZ();
        final double double6 = double4 * double4 + double5 * double5;
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(double6)));
    }
    
    static {
        ID = new ResourceLocation("used_ender_eye");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Floats level;
        
        public TriggerInstance(final EntityPredicate.Composite b, final MinMaxBounds.Floats c) {
            super(UsedEnderEyeTrigger.ID, b);
            this.level = c;
        }
        
        public boolean matches(final double double1) {
            return this.level.matchesSqr(double1);
        }
    }
}
