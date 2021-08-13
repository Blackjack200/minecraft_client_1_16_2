package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.SerializerType;

public class LootItemConditionType extends SerializerType<LootItemCondition> {
    public LootItemConditionType(final Serializer<? extends LootItemCondition> czb) {
        super(czb);
    }
}
