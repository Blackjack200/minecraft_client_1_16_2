package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.stream.Stream;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class ChanneledLightningTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return ChanneledLightningTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final EntityPredicate.Composite[] arr5 = EntityPredicate.Composite.fromJsonArray(jsonObject, "victims", ax);
        return new TriggerInstance(b, arr5);
    }
    
    public void trigger(final ServerPlayer aah, final Collection<? extends Entity> collection) {
        final List<LootContext> list4 = (List<LootContext>)collection.stream().map(apx -> EntityPredicate.createContext(aah, apx)).collect(Collectors.toList());
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(list4)));
    }
    
    static {
        ID = new ResourceLocation("channeled_lightning");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite[] victims;
        
        public TriggerInstance(final EntityPredicate.Composite b, final EntityPredicate.Composite[] arr) {
            super(ChanneledLightningTrigger.ID, b);
            this.victims = arr;
        }
        
        public static TriggerInstance channeledLightning(final EntityPredicate... arr) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, (EntityPredicate.Composite[])Stream.of((Object[])arr).map(EntityPredicate.Composite::wrap).toArray(EntityPredicate.Composite[]::new));
        }
        
        public boolean matches(final Collection<? extends LootContext> collection) {
            for (final EntityPredicate.Composite b6 : this.victims) {
                boolean boolean7 = false;
                for (final LootContext cys9 : collection) {
                    if (b6.matches(cys9)) {
                        boolean7 = true;
                        break;
                    }
                }
                if (!boolean7) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("victims", EntityPredicate.Composite.toJson(this.victims, ci));
            return jsonObject3;
        }
    }
}
