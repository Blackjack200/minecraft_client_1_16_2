package net.minecraft.world.level.storage.loot.predicates;

public interface ConditionUserBuilder<T> {
    T when(final LootItemCondition.Builder a);
    
    T unwrap();
}
