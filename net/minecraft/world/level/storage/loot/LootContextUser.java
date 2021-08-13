package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;

public interface LootContextUser {
    default Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of();
    }
    
    default void validate(final ValidationContext czd) {
        czd.validateUser(this);
    }
}
