package net.minecraft.world.item.crafting;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface RecipeSerializer<T extends Recipe<?>> {
    public static final RecipeSerializer<ShapedRecipe> SHAPED_RECIPE = RecipeSerializer.<ShapedRecipe.Serializer, Recipe>register("crafting_shaped", new ShapedRecipe.Serializer());
    public static final RecipeSerializer<ShapelessRecipe> SHAPELESS_RECIPE = RecipeSerializer.<ShapelessRecipe.Serializer, Recipe>register("crafting_shapeless", new ShapelessRecipe.Serializer());
    public static final SimpleRecipeSerializer<ArmorDyeRecipe> ARMOR_DYE = RecipeSerializer.<SimpleRecipeSerializer<ArmorDyeRecipe>, Recipe>register("crafting_special_armordye", new SimpleRecipeSerializer<ArmorDyeRecipe>((java.util.function.Function<ResourceLocation, ArmorDyeRecipe>)ArmorDyeRecipe::new));
    public static final SimpleRecipeSerializer<BookCloningRecipe> BOOK_CLONING = RecipeSerializer.<SimpleRecipeSerializer<BookCloningRecipe>, Recipe>register("crafting_special_bookcloning", new SimpleRecipeSerializer<BookCloningRecipe>((java.util.function.Function<ResourceLocation, BookCloningRecipe>)BookCloningRecipe::new));
    public static final SimpleRecipeSerializer<MapCloningRecipe> MAP_CLONING = RecipeSerializer.<SimpleRecipeSerializer<MapCloningRecipe>, Recipe>register("crafting_special_mapcloning", new SimpleRecipeSerializer<MapCloningRecipe>((java.util.function.Function<ResourceLocation, MapCloningRecipe>)MapCloningRecipe::new));
    public static final SimpleRecipeSerializer<MapExtendingRecipe> MAP_EXTENDING = RecipeSerializer.<SimpleRecipeSerializer<MapExtendingRecipe>, Recipe>register("crafting_special_mapextending", new SimpleRecipeSerializer<MapExtendingRecipe>((java.util.function.Function<ResourceLocation, MapExtendingRecipe>)MapExtendingRecipe::new));
    public static final SimpleRecipeSerializer<FireworkRocketRecipe> FIREWORK_ROCKET = RecipeSerializer.<SimpleRecipeSerializer<FireworkRocketRecipe>, Recipe>register("crafting_special_firework_rocket", new SimpleRecipeSerializer<FireworkRocketRecipe>((java.util.function.Function<ResourceLocation, FireworkRocketRecipe>)FireworkRocketRecipe::new));
    public static final SimpleRecipeSerializer<FireworkStarRecipe> FIREWORK_STAR = RecipeSerializer.<SimpleRecipeSerializer<FireworkStarRecipe>, Recipe>register("crafting_special_firework_star", new SimpleRecipeSerializer<FireworkStarRecipe>((java.util.function.Function<ResourceLocation, FireworkStarRecipe>)FireworkStarRecipe::new));
    public static final SimpleRecipeSerializer<FireworkStarFadeRecipe> FIREWORK_STAR_FADE = RecipeSerializer.<SimpleRecipeSerializer<FireworkStarFadeRecipe>, Recipe>register("crafting_special_firework_star_fade", new SimpleRecipeSerializer<FireworkStarFadeRecipe>((java.util.function.Function<ResourceLocation, FireworkStarFadeRecipe>)FireworkStarFadeRecipe::new));
    public static final SimpleRecipeSerializer<TippedArrowRecipe> TIPPED_ARROW = RecipeSerializer.<SimpleRecipeSerializer<TippedArrowRecipe>, Recipe>register("crafting_special_tippedarrow", new SimpleRecipeSerializer<TippedArrowRecipe>((java.util.function.Function<ResourceLocation, TippedArrowRecipe>)TippedArrowRecipe::new));
    public static final SimpleRecipeSerializer<BannerDuplicateRecipe> BANNER_DUPLICATE = RecipeSerializer.<SimpleRecipeSerializer<BannerDuplicateRecipe>, Recipe>register("crafting_special_bannerduplicate", new SimpleRecipeSerializer<BannerDuplicateRecipe>((java.util.function.Function<ResourceLocation, BannerDuplicateRecipe>)BannerDuplicateRecipe::new));
    public static final SimpleRecipeSerializer<ShieldDecorationRecipe> SHIELD_DECORATION = RecipeSerializer.<SimpleRecipeSerializer<ShieldDecorationRecipe>, Recipe>register("crafting_special_shielddecoration", new SimpleRecipeSerializer<ShieldDecorationRecipe>((java.util.function.Function<ResourceLocation, ShieldDecorationRecipe>)ShieldDecorationRecipe::new));
    public static final SimpleRecipeSerializer<ShulkerBoxColoring> SHULKER_BOX_COLORING = RecipeSerializer.<SimpleRecipeSerializer<ShulkerBoxColoring>, Recipe>register("crafting_special_shulkerboxcoloring", new SimpleRecipeSerializer<ShulkerBoxColoring>((java.util.function.Function<ResourceLocation, ShulkerBoxColoring>)ShulkerBoxColoring::new));
    public static final SimpleRecipeSerializer<SuspiciousStewRecipe> SUSPICIOUS_STEW = RecipeSerializer.<SimpleRecipeSerializer<SuspiciousStewRecipe>, Recipe>register("crafting_special_suspiciousstew", new SimpleRecipeSerializer<SuspiciousStewRecipe>((java.util.function.Function<ResourceLocation, SuspiciousStewRecipe>)SuspiciousStewRecipe::new));
    public static final SimpleRecipeSerializer<RepairItemRecipe> REPAIR_ITEM = RecipeSerializer.<SimpleRecipeSerializer<RepairItemRecipe>, Recipe>register("crafting_special_repairitem", new SimpleRecipeSerializer<RepairItemRecipe>((java.util.function.Function<ResourceLocation, RepairItemRecipe>)RepairItemRecipe::new));
    public static final SimpleCookingSerializer<SmeltingRecipe> SMELTING_RECIPE = RecipeSerializer.<SimpleCookingSerializer<SmeltingRecipe>, Recipe>register("smelting", new SimpleCookingSerializer<SmeltingRecipe>(SmeltingRecipe::new, 200));
    public static final SimpleCookingSerializer<BlastingRecipe> BLASTING_RECIPE = RecipeSerializer.<SimpleCookingSerializer<BlastingRecipe>, Recipe>register("blasting", new SimpleCookingSerializer<BlastingRecipe>(BlastingRecipe::new, 100));
    public static final SimpleCookingSerializer<SmokingRecipe> SMOKING_RECIPE = RecipeSerializer.<SimpleCookingSerializer<SmokingRecipe>, Recipe>register("smoking", new SimpleCookingSerializer<SmokingRecipe>(SmokingRecipe::new, 100));
    public static final SimpleCookingSerializer<CampfireCookingRecipe> CAMPFIRE_COOKING_RECIPE = RecipeSerializer.<SimpleCookingSerializer<CampfireCookingRecipe>, Recipe>register("campfire_cooking", new SimpleCookingSerializer<CampfireCookingRecipe>(CampfireCookingRecipe::new, 100));
    public static final RecipeSerializer<StonecutterRecipe> STONECUTTER = RecipeSerializer.<SingleItemRecipe.Serializer<StonecutterRecipe>, Recipe>register("stonecutting", new SingleItemRecipe.Serializer<StonecutterRecipe>(StonecutterRecipe::new));
    public static final RecipeSerializer<UpgradeRecipe> SMITHING = RecipeSerializer.<UpgradeRecipe.Serializer, Recipe>register("smithing", new UpgradeRecipe.Serializer());
    
    T fromJson(final ResourceLocation vk, final JsonObject jsonObject);
    
    T fromNetwork(final ResourceLocation vk, final FriendlyByteBuf nf);
    
    void toNetwork(final FriendlyByteBuf nf, final T bon);
    
    default <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(final String string, final S bop) {
        return Registry.<S>register(Registry.RECIPE_SERIALIZER, string, bop);
    }
}
