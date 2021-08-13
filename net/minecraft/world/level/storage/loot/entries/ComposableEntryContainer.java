package net.minecraft.world.level.storage.loot.entries;

import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;

@FunctionalInterface
interface ComposableEntryContainer {
    public static final ComposableEntryContainer ALWAYS_FALSE = (cys, consumer) -> false;
    public static final ComposableEntryContainer ALWAYS_TRUE = (cys, consumer) -> true;
    
    boolean expand(final LootContext cys, final Consumer<LootPoolEntry> consumer);
    
    default ComposableEntryContainer and(final ComposableEntryContainer czf) {
        Objects.requireNonNull(czf);
        return (cys, consumer) -> this.expand(cys, consumer) && czf.expand(cys, consumer);
    }
    
    default ComposableEntryContainer or(final ComposableEntryContainer czf) {
        Objects.requireNonNull(czf);
        return (cys, consumer) -> this.expand(cys, consumer) || czf.expand(cys, consumer);
    }
}
