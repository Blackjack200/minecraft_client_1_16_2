package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EntryGroup extends CompositeEntryBase {
    EntryGroup(final LootPoolEntryContainer[] arr, final LootItemCondition[] arr) {
        super(arr, arr);
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.GROUP;
    }
    
    @Override
    protected ComposableEntryContainer compose(final ComposableEntryContainer[] arr) {
        switch (arr.length) {
            case 0: {
                return EntryGroup.ALWAYS_TRUE;
            }
            case 1: {
                return arr[0];
            }
            case 2: {
                final ComposableEntryContainer czf3 = arr[0];
                final ComposableEntryContainer czf4 = arr[1];
                final ComposableEntryContainer composableEntryContainer;
                final ComposableEntryContainer composableEntryContainer2;
                return (cys, consumer) -> {
                    composableEntryContainer.expand(cys, consumer);
                    composableEntryContainer2.expand(cys, consumer);
                    return true;
                };
            }
            default: {
                int length;
                int i = 0;
                ComposableEntryContainer czf5;
                return (cys, consumer) -> {
                    for (length = arr.length; i < length; ++i) {
                        czf5 = arr[i];
                        czf5.expand(cys, consumer);
                    }
                    return true;
                };
            }
        }
    }
}
