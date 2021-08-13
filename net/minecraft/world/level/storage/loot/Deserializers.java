package net.minecraft.world.level.storage.loot;

import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;

public class Deserializers {
    public static GsonBuilder createConditionSerializer() {
        return new GsonBuilder().registerTypeAdapter((Type)RandomValueBounds.class, new RandomValueBounds.Serializer()).registerTypeAdapter((Type)BinomialDistributionGenerator.class, new BinomialDistributionGenerator.Serializer()).registerTypeAdapter((Type)ConstantIntValue.class, new ConstantIntValue.Serializer()).registerTypeHierarchyAdapter((Class)LootItemCondition.class, LootItemConditions.createGsonAdapter()).registerTypeHierarchyAdapter((Class)LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer());
    }
    
    public static GsonBuilder createFunctionSerializer() {
        return createConditionSerializer().registerTypeAdapter((Type)IntLimiter.class, new IntLimiter.Serializer()).registerTypeHierarchyAdapter((Class)LootPoolEntryContainer.class, LootPoolEntries.createGsonAdapter()).registerTypeHierarchyAdapter((Class)LootItemFunction.class, LootItemFunctions.createGsonAdapter());
    }
    
    public static GsonBuilder createLootTableSerializer() {
        return createFunctionSerializer().registerTypeAdapter((Type)LootPool.class, new LootPool.Serializer()).registerTypeAdapter((Type)LootTable.class, new LootTable.Serializer());
    }
}
