package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.AgableMob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class BredAnimalsTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return BredAnimalsTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "parent", ax);
        final EntityPredicate.Composite b3 = EntityPredicate.Composite.fromJson(jsonObject, "partner", ax);
        final EntityPredicate.Composite b4 = EntityPredicate.Composite.fromJson(jsonObject, "child", ax);
        return new TriggerInstance(b, b2, b3, b4);
    }
    
    public void trigger(final ServerPlayer aah, final Animal azw2, final Animal azw3, @Nullable final AgableMob apv) {
        final LootContext cys6 = EntityPredicate.createContext(aah, azw2);
        final LootContext cys7 = EntityPredicate.createContext(aah, azw3);
        final LootContext cys8 = (apv != null) ? EntityPredicate.createContext(aah, apv) : null;
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cys6, cys7, cys8)));
    }
    
    static {
        ID = new ResourceLocation("bred_animals");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite parent;
        private final EntityPredicate.Composite partner;
        private final EntityPredicate.Composite child;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final EntityPredicate.Composite b2, final EntityPredicate.Composite b3, final EntityPredicate.Composite b4) {
            super(BredAnimalsTrigger.ID, b1);
            this.parent = b2;
            this.partner = b3;
            this.child = b4;
        }
        
        public static TriggerInstance bredAnimals() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
        }
        
        public static TriggerInstance bredAnimals(final EntityPredicate.Builder a) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(a.build()));
        }
        
        public static TriggerInstance bredAnimals(final EntityPredicate bg1, final EntityPredicate bg2, final EntityPredicate bg3) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(bg1), EntityPredicate.Composite.wrap(bg2), EntityPredicate.Composite.wrap(bg3));
        }
        
        public boolean matches(final LootContext cys1, final LootContext cys2, @Nullable final LootContext cys3) {
            return (this.child == EntityPredicate.Composite.ANY || (cys3 != null && this.child.matches(cys3))) && ((this.parent.matches(cys1) && this.partner.matches(cys2)) || (this.parent.matches(cys2) && this.partner.matches(cys1)));
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("parent", this.parent.toJson(ci));
            jsonObject3.add("partner", this.partner.toJson(ci));
            jsonObject3.add("child", this.child.toJson(ci));
            return jsonObject3;
        }
    }
}
