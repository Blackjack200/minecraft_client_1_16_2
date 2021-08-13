package net.minecraft.advancements.critereon;

import java.util.Iterator;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import java.util.Collection;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class FishingRodHookedTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return FishingRodHookedTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final ItemPredicate bq5 = ItemPredicate.fromJson(jsonObject.get("rod"));
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "entity", ax);
        final ItemPredicate bq6 = ItemPredicate.fromJson(jsonObject.get("item"));
        return new TriggerInstance(b, bq5, b2, bq6);
    }
    
    public void trigger(final ServerPlayer aah, final ItemStack bly, final FishingHook bgf, final Collection<ItemStack> collection) {
        final LootContext cys6 = EntityPredicate.createContext(aah, (bgf.getHookedIn() != null) ? bgf.getHookedIn() : bgf);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bly, cys6, collection)));
    }
    
    static {
        ID = new ResourceLocation("fishing_rod_hooked");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate rod;
        private final EntityPredicate.Composite entity;
        private final ItemPredicate item;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final ItemPredicate bq2, final EntityPredicate.Composite b3, final ItemPredicate bq4) {
            super(FishingRodHookedTrigger.ID, b1);
            this.rod = bq2;
            this.entity = b3;
            this.item = bq4;
        }
        
        public static TriggerInstance fishedItem(final ItemPredicate bq1, final EntityPredicate bg, final ItemPredicate bq3) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, bq1, EntityPredicate.Composite.wrap(bg), bq3);
        }
        
        public boolean matches(final ItemStack bly, final LootContext cys, final Collection<ItemStack> collection) {
            if (!this.rod.matches(bly)) {
                return false;
            }
            if (!this.entity.matches(cys)) {
                return false;
            }
            if (this.item != ItemPredicate.ANY) {
                boolean boolean5 = false;
                final Entity apx6 = cys.<Entity>getParamOrNull(LootContextParams.THIS_ENTITY);
                if (apx6 instanceof ItemEntity) {
                    final ItemEntity bcs7 = (ItemEntity)apx6;
                    if (this.item.matches(bcs7.getItem())) {
                        boolean5 = true;
                    }
                }
                for (final ItemStack bly2 : collection) {
                    if (this.item.matches(bly2)) {
                        boolean5 = true;
                        break;
                    }
                }
                if (!boolean5) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("rod", this.rod.serializeToJson());
            jsonObject3.add("entity", this.entity.toJson(ci));
            jsonObject3.add("item", this.item.serializeToJson());
            return jsonObject3;
        }
    }
}
