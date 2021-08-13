package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Function;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;

public class LootPoolEntries {
    public static final LootPoolEntryType EMPTY;
    public static final LootPoolEntryType ITEM;
    public static final LootPoolEntryType REFERENCE;
    public static final LootPoolEntryType DYNAMIC;
    public static final LootPoolEntryType TAG;
    public static final LootPoolEntryType ALTERNATIVES;
    public static final LootPoolEntryType SEQUENCE;
    public static final LootPoolEntryType GROUP;
    
    private static LootPoolEntryType register(final String string, final Serializer<? extends LootPoolEntryContainer> czb) {
        return Registry.<LootPoolEntryType, LootPoolEntryType>register(Registry.LOOT_POOL_ENTRY_TYPE, new ResourceLocation(string), new LootPoolEntryType(czb));
    }
    
    public static Object createGsonAdapter() {
        return GsonAdapterFactory.<Object, LootPoolEntryType>builder(Registry.LOOT_POOL_ENTRY_TYPE, "entry", "type", (java.util.function.Function<Object, LootPoolEntryType>)LootPoolEntryContainer::getType).build();
    }
    
    static {
        EMPTY = register("empty", new EmptyLootItem.Serializer());
        ITEM = register("item", new LootItem.Serializer());
        REFERENCE = register("loot_table", new LootTableReference.Serializer());
        DYNAMIC = register("dynamic", new DynamicLoot.Serializer());
        TAG = register("tag", new TagEntry.Serializer());
        ALTERNATIVES = register("alternatives", CompositeEntryBase.<AlternativesEntry>createSerializer(AlternativesEntry::new));
        SEQUENCE = register("sequence", CompositeEntryBase.<SequentialEntry>createSerializer(SequentialEntry::new));
        GROUP = register("group", CompositeEntryBase.<EntryGroup>createSerializer(EntryGroup::new));
    }
}
