package net.minecraft.world.level.storage.loot.predicates;

import java.util.function.Predicate;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;

public class LootItemConditions {
    public static final LootItemConditionType INVERTED;
    public static final LootItemConditionType ALTERNATIVE;
    public static final LootItemConditionType RANDOM_CHANCE;
    public static final LootItemConditionType RANDOM_CHANCE_WITH_LOOTING;
    public static final LootItemConditionType ENTITY_PROPERTIES;
    public static final LootItemConditionType KILLED_BY_PLAYER;
    public static final LootItemConditionType ENTITY_SCORES;
    public static final LootItemConditionType BLOCK_STATE_PROPERTY;
    public static final LootItemConditionType MATCH_TOOL;
    public static final LootItemConditionType TABLE_BONUS;
    public static final LootItemConditionType SURVIVES_EXPLOSION;
    public static final LootItemConditionType DAMAGE_SOURCE_PROPERTIES;
    public static final LootItemConditionType LOCATION_CHECK;
    public static final LootItemConditionType WEATHER_CHECK;
    public static final LootItemConditionType REFERENCE;
    public static final LootItemConditionType TIME_CHECK;
    
    private static LootItemConditionType register(final String string, final Serializer<? extends LootItemCondition> czb) {
        return Registry.<LootItemConditionType, LootItemConditionType>register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(string), new LootItemConditionType(czb));
    }
    
    public static Object createGsonAdapter() {
        return GsonAdapterFactory.<Object, LootItemConditionType>builder(Registry.LOOT_CONDITION_TYPE, "condition", "condition", (java.util.function.Function<Object, LootItemConditionType>)LootItemCondition::getType).build();
    }
    
    public static <T> Predicate<T> andConditions(final Predicate<T>[] arr) {
        switch (arr.length) {
            case 0: {
                return (Predicate<T>)(object -> true);
            }
            case 1: {
                return arr[0];
            }
            case 2: {
                return (Predicate<T>)arr[0].and((Predicate)arr[1]);
            }
            default: {
                return (Predicate<T>)(object -> {
                    for (final Predicate<Object> predicate6 : arr) {
                        if (!predicate6.test(object)) {
                            return false;
                        }
                    }
                    return true;
                });
            }
        }
    }
    
    public static <T> Predicate<T> orConditions(final Predicate<T>[] arr) {
        switch (arr.length) {
            case 0: {
                return (Predicate<T>)(object -> false);
            }
            case 1: {
                return arr[0];
            }
            case 2: {
                return (Predicate<T>)arr[0].or((Predicate)arr[1]);
            }
            default: {
                return (Predicate<T>)(object -> {
                    for (final Predicate<Object> predicate6 : arr) {
                        if (predicate6.test(object)) {
                            return true;
                        }
                    }
                    return false;
                });
            }
        }
    }
    
    static {
        INVERTED = register("inverted", new InvertedLootItemCondition.Serializer());
        ALTERNATIVE = register("alternative", new AlternativeLootItemCondition.Serializer());
        RANDOM_CHANCE = register("random_chance", new LootItemRandomChanceCondition.Serializer());
        RANDOM_CHANCE_WITH_LOOTING = register("random_chance_with_looting", new LootItemRandomChanceWithLootingCondition.Serializer());
        ENTITY_PROPERTIES = register("entity_properties", new LootItemEntityPropertyCondition.Serializer());
        KILLED_BY_PLAYER = register("killed_by_player", new LootItemKilledByPlayerCondition.Serializer());
        ENTITY_SCORES = register("entity_scores", new EntityHasScoreCondition.Serializer());
        BLOCK_STATE_PROPERTY = register("block_state_property", new LootItemBlockStatePropertyCondition.Serializer());
        MATCH_TOOL = register("match_tool", new MatchTool.Serializer());
        TABLE_BONUS = register("table_bonus", new BonusLevelTableCondition.Serializer());
        SURVIVES_EXPLOSION = register("survives_explosion", new ExplosionCondition.Serializer());
        DAMAGE_SOURCE_PROPERTIES = register("damage_source_properties", new DamageSourceCondition.Serializer());
        LOCATION_CHECK = register("location_check", new LocationCheck.Serializer());
        WEATHER_CHECK = register("weather_check", new WeatherCheck.Serializer());
        REFERENCE = register("reference", new ConditionReference.Serializer());
        TIME_CHECK = register("time_check", new TimeCheck.Serializer());
    }
}
