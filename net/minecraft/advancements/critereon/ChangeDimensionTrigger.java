package net.minecraft.advancements.critereon;

import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ChangeDimensionTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ChangeDimensionTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ResourceKey<Level> vj5 = jsonObject.has("from") ? ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, new ResourceLocation(GsonHelper.getAsString(jsonObject, "from"))) : null;
        final ResourceKey<Level> vj6 = jsonObject.has("to") ? ResourceKey.<Level>create(Registry.DIMENSION_REGISTRY, new ResourceLocation(GsonHelper.getAsString(jsonObject, "to"))) : null;
        return new TriggerInstance(b, vj5, vj6);
    }
    
    public void trigger(final ServerPlayer aah, final ResourceKey<Level> vj2, final ResourceKey<Level> vj3) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(vj2, vj3)));
    }
    
    static {
        ID = new ResourceLocation("changed_dimension");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        @Nullable
        private final ResourceKey<Level> from;
        @Nullable
        private final ResourceKey<Level> to;
        
        public TriggerInstance(final EntityPredicate.Composite b, @Nullable final ResourceKey<Level> vj2, @Nullable final ResourceKey<Level> vj3) {
            super(ChangeDimensionTrigger.ID, b);
            this.from = vj2;
            this.to = vj3;
        }
        
        public static TriggerInstance changedDimensionTo(final ResourceKey<Level> vj) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, null, vj);
        }
        
        public boolean matches(final ResourceKey<Level> vj1, final ResourceKey<Level> vj2) {
            return (this.from == null || this.from == vj1) && (this.to == null || this.to == vj2);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            if (this.from != null) {
                jsonObject3.addProperty("from", this.from.location().toString());
            }
            if (this.to != null) {
                jsonObject3.addProperty("to", this.to.location().toString());
            }
            return jsonObject3;
        }
    }
}
