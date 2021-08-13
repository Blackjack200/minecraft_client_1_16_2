package net.minecraft.world.level.storage.loot.entries;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class AlternativesEntry extends CompositeEntryBase {
    AlternativesEntry(final LootPoolEntryContainer[] arr, final LootItemCondition[] arr) {
        super(arr, arr);
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.ALTERNATIVES;
    }
    
    @Override
    protected ComposableEntryContainer compose(final ComposableEntryContainer[] arr) {
        switch (arr.length) {
            case 0: {
                return AlternativesEntry.ALWAYS_FALSE;
            }
            case 1: {
                return arr[0];
            }
            case 2: {
                return arr[0].or(arr[1]);
            }
            default: {
                final int length;
                int i = 0;
                ComposableEntryContainer czf7;
                return (cys, consumer) -> {
                    length = arr.length;
                    while (i < length) {
                        czf7 = arr[i];
                        if (czf7.expand(cys, consumer)) {
                            return true;
                        }
                        else {
                            ++i;
                        }
                    }
                    return false;
                };
            }
        }
    }
    
    @Override
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        for (int integer3 = 0; integer3 < this.children.length - 1; ++integer3) {
            if (ArrayUtils.isEmpty((Object[])this.children[integer3].conditions)) {
                czd.reportProblem("Unreachable entry!");
            }
        }
    }
    
    public static Builder alternatives(final LootPoolEntryContainer.Builder<?>... arr) {
        return new Builder(arr);
    }
    
    public static class Builder extends LootPoolEntryContainer.Builder<Builder> {
        private final List<LootPoolEntryContainer> entries;
        
        public Builder(final LootPoolEntryContainer.Builder<?>... arr) {
            this.entries = (List<LootPoolEntryContainer>)Lists.newArrayList();
            for (final LootPoolEntryContainer.Builder<?> a6 : arr) {
                this.entries.add(a6.build());
            }
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        @Override
        public Builder otherwise(final LootPoolEntryContainer.Builder<?> a) {
            this.entries.add(a.build());
            return this;
        }
        
        @Override
        public LootPoolEntryContainer build() {
            return new AlternativesEntry((LootPoolEntryContainer[])this.entries.toArray((Object[])new LootPoolEntryContainer[0]), this.getConditions());
        }
    }
}
