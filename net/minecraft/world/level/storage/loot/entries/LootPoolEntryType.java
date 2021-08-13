package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class LootPoolEntryType extends SerializerType<LootPoolEntryContainer> {
    public LootPoolEntryType(final Serializer<? extends LootPoolEntryContainer> czb) {
        super(czb);
    }
}
