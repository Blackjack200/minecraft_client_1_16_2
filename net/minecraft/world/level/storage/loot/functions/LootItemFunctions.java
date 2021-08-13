package net.minecraft.world.level.storage.loot.functions;

import java.util.function.Function;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiFunction;

public class LootItemFunctions {
    public static final BiFunction<ItemStack, LootContext, ItemStack> IDENTITY;
    public static final LootItemFunctionType SET_COUNT;
    public static final LootItemFunctionType ENCHANT_WITH_LEVELS;
    public static final LootItemFunctionType ENCHANT_RANDOMLY;
    public static final LootItemFunctionType SET_NBT;
    public static final LootItemFunctionType FURNACE_SMELT;
    public static final LootItemFunctionType LOOTING_ENCHANT;
    public static final LootItemFunctionType SET_DAMAGE;
    public static final LootItemFunctionType SET_ATTRIBUTES;
    public static final LootItemFunctionType SET_NAME;
    public static final LootItemFunctionType EXPLORATION_MAP;
    public static final LootItemFunctionType SET_STEW_EFFECT;
    public static final LootItemFunctionType COPY_NAME;
    public static final LootItemFunctionType SET_CONTENTS;
    public static final LootItemFunctionType LIMIT_COUNT;
    public static final LootItemFunctionType APPLY_BONUS;
    public static final LootItemFunctionType SET_LOOT_TABLE;
    public static final LootItemFunctionType EXPLOSION_DECAY;
    public static final LootItemFunctionType SET_LORE;
    public static final LootItemFunctionType FILL_PLAYER_HEAD;
    public static final LootItemFunctionType COPY_NBT;
    public static final LootItemFunctionType COPY_STATE;
    
    private static LootItemFunctionType register(final String string, final Serializer<? extends LootItemFunction> czb) {
        return Registry.<LootItemFunctionType, LootItemFunctionType>register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(string), new LootItemFunctionType(czb));
    }
    
    public static Object createGsonAdapter() {
        return GsonAdapterFactory.<Object, LootItemFunctionType>builder(Registry.LOOT_FUNCTION_TYPE, "function", "function", (java.util.function.Function<Object, LootItemFunctionType>)LootItemFunction::getType).build();
    }
    
    public static BiFunction<ItemStack, LootContext, ItemStack> compose(final BiFunction<ItemStack, LootContext, ItemStack>[] arr) {
        switch (arr.length) {
            case 0: {
                return LootItemFunctions.IDENTITY;
            }
            case 1: {
                return arr[0];
            }
            case 2: {
                final BiFunction<ItemStack, LootContext, ItemStack> biFunction2 = arr[0];
                final BiFunction<ItemStack, LootContext, ItemStack> biFunction3 = arr[1];
                return (BiFunction<ItemStack, LootContext, ItemStack>)((bly, cys) -> (ItemStack)biFunction3.apply(biFunction2.apply(bly, cys), cys));
            }
            default: {
                return (BiFunction<ItemStack, LootContext, ItemStack>)((bly, cys) -> {
                    for (final BiFunction<ItemStack, LootContext, ItemStack> biFunction7 : arr) {
                        bly = (ItemStack)biFunction7.apply(bly, cys);
                    }
                    return bly;
                });
            }
        }
    }
    
    static {
        IDENTITY = ((bly, cys) -> bly);
        SET_COUNT = register("set_count", new SetItemCountFunction.Serializer());
        ENCHANT_WITH_LEVELS = register("enchant_with_levels", new EnchantWithLevelsFunction.Serializer());
        ENCHANT_RANDOMLY = register("enchant_randomly", new EnchantRandomlyFunction.Serializer());
        SET_NBT = register("set_nbt", new SetNbtFunction.Serializer());
        FURNACE_SMELT = register("furnace_smelt", new SmeltItemFunction.Serializer());
        LOOTING_ENCHANT = register("looting_enchant", new LootingEnchantFunction.Serializer());
        SET_DAMAGE = register("set_damage", new SetItemDamageFunction.Serializer());
        SET_ATTRIBUTES = register("set_attributes", new SetAttributesFunction.Serializer());
        SET_NAME = register("set_name", new SetNameFunction.Serializer());
        EXPLORATION_MAP = register("exploration_map", new ExplorationMapFunction.Serializer());
        SET_STEW_EFFECT = register("set_stew_effect", new SetStewEffectFunction.Serializer());
        COPY_NAME = register("copy_name", new CopyNameFunction.Serializer());
        SET_CONTENTS = register("set_contents", new SetContainerContents.Serializer());
        LIMIT_COUNT = register("limit_count", new LimitCount.Serializer());
        APPLY_BONUS = register("apply_bonus", new ApplyBonusCount.Serializer());
        SET_LOOT_TABLE = register("set_loot_table", new SetContainerLootTable.Serializer());
        EXPLOSION_DECAY = register("explosion_decay", new ApplyExplosionDecay.Serializer());
        SET_LORE = register("set_lore", new SetLoreFunction.Serializer());
        FILL_PLAYER_HEAD = register("fill_player_head", new FillPlayerHead.Serializer());
        COPY_NBT = register("copy_nbt", new CopyNbtFunction.Serializer());
        COPY_STATE = register("copy_state", new CopyBlockState.Serializer());
    }
}
