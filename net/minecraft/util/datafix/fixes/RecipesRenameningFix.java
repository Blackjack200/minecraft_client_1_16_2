package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import java.util.function.Function;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;

public class RecipesRenameningFix extends RecipesRenameFix {
    private static final Map<String, String> RECIPES;
    
    public RecipesRenameningFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "Recipes renamening fix", (Function<String, String>)(string -> (String)RecipesRenameningFix.RECIPES.getOrDefault(string, string)));
    }
    
    static {
        RECIPES = (Map)ImmutableMap.builder().put("minecraft:acacia_bark", "minecraft:acacia_wood").put("minecraft:birch_bark", "minecraft:birch_wood").put("minecraft:dark_oak_bark", "minecraft:dark_oak_wood").put("minecraft:jungle_bark", "minecraft:jungle_wood").put("minecraft:oak_bark", "minecraft:oak_wood").put("minecraft:spruce_bark", "minecraft:spruce_wood").build();
    }
}
