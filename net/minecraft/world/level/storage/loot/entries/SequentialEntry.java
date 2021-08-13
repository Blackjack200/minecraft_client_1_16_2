package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SequentialEntry extends CompositeEntryBase {
    SequentialEntry(final LootPoolEntryContainer[] arr, final LootItemCondition[] arr) {
        super(arr, arr);
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.SEQUENCE;
    }
    
    @Override
    protected ComposableEntryContainer compose(final ComposableEntryContainer[] arr) {
        switch (arr.length) {
            case 0: {
                return SequentialEntry.ALWAYS_TRUE;
            }
            case 1: {
                return arr[0];
            }
            case 2: {
                return arr[0].and(arr[1]);
            }
            default: {
                final int length;
                int i = 0;
                ComposableEntryContainer czf7;
                return (cys, consumer) -> {
                    length = arr.length;
                    while (i < length) {
                        czf7 = arr[i];
                        if (!czf7.expand(cys, consumer)) {
                            return false;
                        }
                        else {
                            ++i;
                        }
                    }
                    return true;
                };
            }
        }
    }
}
