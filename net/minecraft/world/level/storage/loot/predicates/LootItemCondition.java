package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface LootItemCondition extends LootContextUser, Predicate<LootContext> {
    LootItemConditionType getType();
    
    @FunctionalInterface
    public interface Builder {
        LootItemCondition build();
        
        default Builder invert() {
            return InvertedLootItemCondition.invert(this);
        }
        
        default AlternativeLootItemCondition.Builder or(final Builder a) {
            return AlternativeLootItemCondition.alternative(this, a);
        }
    }
}
