package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class CuredZombieVillagerTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return CuredZombieVillagerTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final EntityPredicate.Composite b2 = EntityPredicate.Composite.fromJson(jsonObject, "zombie", ax);
        final EntityPredicate.Composite b3 = EntityPredicate.Composite.fromJson(jsonObject, "villager", ax);
        return new TriggerInstance(b, b2, b3);
    }
    
    public void trigger(final ServerPlayer aah, final Zombie beg, final Villager bfg) {
        final LootContext cys5 = EntityPredicate.createContext(aah, beg);
        final LootContext cys6 = EntityPredicate.createContext(aah, bfg);
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(cys5, cys6)));
    }
    
    static {
        ID = new ResourceLocation("cured_zombie_villager");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite zombie;
        private final EntityPredicate.Composite villager;
        
        public TriggerInstance(final EntityPredicate.Composite b1, final EntityPredicate.Composite b2, final EntityPredicate.Composite b3) {
            super(CuredZombieVillagerTrigger.ID, b1);
            this.zombie = b2;
            this.villager = b3;
        }
        
        public static TriggerInstance curedZombieVillager() {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY);
        }
        
        public boolean matches(final LootContext cys1, final LootContext cys2) {
            return this.zombie.matches(cys1) && this.villager.matches(cys2);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            jsonObject3.add("zombie", this.zombie.toJson(ci));
            jsonObject3.add("villager", this.villager.toJson(ci));
            return jsonObject3;
        }
    }
}
