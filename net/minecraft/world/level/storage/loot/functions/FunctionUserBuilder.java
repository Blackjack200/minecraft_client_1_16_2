package net.minecraft.world.level.storage.loot.functions;

public interface FunctionUserBuilder<T> {
    T apply(final LootItemFunction.Builder a);
    
    T unwrap();
}
