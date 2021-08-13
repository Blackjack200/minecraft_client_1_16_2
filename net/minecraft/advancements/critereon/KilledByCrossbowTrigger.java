package net.minecraft.advancements.critereon;

import java.util.Iterator;
import net.minecraft.world.entity.EntityType;
import java.util.Set;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.List;
import java.util.function.Predicate;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class KilledByCrossbowTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return KilledByCrossbowTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final EntityPredicate.Composite[] arr5 = EntityPredicate.Composite.fromJsonArray(jsonObject, "victims", ax);
        final MinMaxBounds.Ints d6 = MinMaxBounds.Ints.fromJson(jsonObject.get("unique_entity_types"));
        return new TriggerInstance(b, arr5, d6);
    }
    
    public void trigger(final ServerPlayer aah, final Collection<Entity> collection) {
        final List<LootContext> list4 = (List<LootContext>)Lists.newArrayList();
        final Set<EntityType<?>> set5 = (Set<EntityType<?>>)Sets.newHashSet();
        for (final Entity apx7 : collection) {
            set5.add(apx7.getType());
            list4.add(EntityPredicate.createContext(aah, apx7));
        }
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches((Collection<LootContext>)list4, set5.size())));
    }
    
    static {
        ID = new ResourceLocation("killed_by_crossbow");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite[] victims;
        private final MinMaxBounds.Ints uniqueEntityTypes;
        
        public TriggerInstance(final EntityPredicate.Composite b, final EntityPredicate.Composite[] arr, final MinMaxBounds.Ints d) {
            super(KilledByCrossbowTrigger.ID, b);
            this.victims = arr;
            this.uniqueEntityTypes = d;
        }
        
        public static TriggerInstance crossbowKilled(final EntityPredicate.Builder... arr) {
            final EntityPredicate.Composite[] arr2 = new EntityPredicate.Composite[arr.length];
            for (int integer3 = 0; integer3 < arr.length; ++integer3) {
                final EntityPredicate.Builder a4 = arr[integer3];
                arr2[integer3] = EntityPredicate.Composite.wrap(a4.build());
            }
            return new TriggerInstance(EntityPredicate.Composite.ANY, arr2, MinMaxBounds.Ints.ANY);
        }
        
        public static TriggerInstance crossbowKilled(final MinMaxBounds.Ints d) {
            final EntityPredicate.Composite[] arr2 = new EntityPredicate.Composite[0];
            return new TriggerInstance(EntityPredicate.Composite.ANY, arr2, d);
        }
        
        public boolean matches(final Collection<LootContext> collection, final int integer) {
            if (this.victims.length > 0) {
                final List<LootContext> list4 = (List<LootContext>)Lists.newArrayList((Iterable)collection);
                for (final EntityPredicate.Composite b8 : this.victims) {
                    boolean boolean9 = false;
                    final Iterator<LootContext> iterator10 = (Iterator<LootContext>)list4.iterator();
                    while (iterator10.hasNext()) {
                        final LootContext cys11 = (LootContext)iterator10.next();
                        if (b8.matches(cys11)) {
                            iterator10.remove();
                            boolean9 = true;
                            break;
                        }
                    }
                    if (!boolean9) {
                        return false;
                    }
                }
            }
            return this.uniqueEntityTypes.matches(integer);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("victims", EntityPredicate.Composite.toJson(this.victims, ci));
            jsonObject3.add("unique_entity_types", this.uniqueEntityTypes.serializeToJson());
            return jsonObject3;
        }
    }
}
