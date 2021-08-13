package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;

public interface RecipeType<T extends Recipe<?>> {
    public static final RecipeType<CraftingRecipe> CRAFTING = RecipeType.<CraftingRecipe>register("crafting");
    public static final RecipeType<SmeltingRecipe> SMELTING = RecipeType.<SmeltingRecipe>register("smelting");
    public static final RecipeType<BlastingRecipe> BLASTING = RecipeType.<BlastingRecipe>register("blasting");
    public static final RecipeType<SmokingRecipe> SMOKING = RecipeType.<SmokingRecipe>register("smoking");
    public static final RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = RecipeType.<CampfireCookingRecipe>register("campfire_cooking");
    public static final RecipeType<StonecutterRecipe> STONECUTTING = RecipeType.<StonecutterRecipe>register("stonecutting");
    public static final RecipeType<UpgradeRecipe> SMITHING = RecipeType.<UpgradeRecipe>register("smithing");
    
    default <T extends Recipe<?>> RecipeType<T> register(final String string) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(string), new RecipeType<T>() {
            public String toString() {
                return string;
            }
        });
    }
    
    default <C extends Container> Optional<T> tryMatch(final Recipe<C> bon, final Level bru, final C aok) {
        return (Optional<T>)(bon.matches(aok, bru) ? Optional.of(bon) : Optional.empty());
    }
}
