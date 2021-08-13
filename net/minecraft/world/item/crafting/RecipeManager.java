package net.minecraft.world.item.crafting;

import org.apache.logging.log4j.LogManager;
import com.google.gson.GsonBuilder;
import net.minecraft.Util;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import java.util.Iterator;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.common.collect.Maps;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonElement;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

public class RecipeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON;
    private static final Logger LOGGER;
    private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;
    private boolean hasErrors;
    
    public RecipeManager() {
        super(RecipeManager.GSON, "recipes");
        this.recipes = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>)ImmutableMap.of();
    }
    
    @Override
    protected void apply(final Map<ResourceLocation, JsonElement> map, final ResourceManager acf, final ProfilerFiller ant) {
        this.hasErrors = false;
        final Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> map2 = (Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>>)Maps.newHashMap();
        for (final Map.Entry<ResourceLocation, JsonElement> entry7 : map.entrySet()) {
            final ResourceLocation vk8 = (ResourceLocation)entry7.getKey();
            try {
                final Recipe<?> bon9 = fromJson(vk8, GsonHelper.convertToJsonObject((JsonElement)entry7.getValue(), "top element"));
                ((ImmutableMap.Builder)map2.computeIfAbsent(bon9.getType(), boq -> ImmutableMap.builder())).put(vk8, bon9);
            }
            catch (JsonParseException | IllegalArgumentException ex2) {
                final RuntimeException ex;
                final RuntimeException runtimeException9 = ex;
                RecipeManager.LOGGER.error("Parsing error loading recipe {}", vk8, runtimeException9);
            }
        }
        this.recipes = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>)map2.entrySet().stream().collect(ImmutableMap.toImmutableMap(Map.Entry::getKey, entry -> ((ImmutableMap.Builder)entry.getValue()).build()));
        RecipeManager.LOGGER.info("Loaded {} recipes", map2.size());
    }
    
    public <C extends Container, T extends Recipe<C>> Optional<T> getRecipeFor(final RecipeType<T> boq, final C aok, final Level bru) {
        return (Optional<T>)this.<Container, T>byType(boq).values().stream().flatMap(bon -> Util.toStream((java.util.Optional<?>)boq.<Container>tryMatch(bon, bru, aok))).findFirst();
    }
    
    public <C extends Container, T extends Recipe<C>> List<T> getAllRecipesFor(final RecipeType<T> boq) {
        return (List<T>)this.<Container, T>byType(boq).values().stream().map(bon -> bon).collect(Collectors.toList());
    }
    
    public <C extends Container, T extends Recipe<C>> List<T> getRecipesFor(final RecipeType<T> boq, final C aok, final Level bru) {
        return (List<T>)this.<Container, T>byType(boq).values().stream().flatMap(bon -> Util.toStream((java.util.Optional<?>)boq.<Container>tryMatch(bon, bru, aok))).sorted(Comparator.comparing(bon -> bon.getResultItem().getDescriptionId())).collect(Collectors.toList());
    }
    
    private <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> byType(final RecipeType<T> boq) {
        return (Map<ResourceLocation, Recipe<C>>)this.recipes.getOrDefault(boq, Collections.emptyMap());
    }
    
    public <C extends Container, T extends Recipe<C>> NonNullList<ItemStack> getRemainingItemsFor(final RecipeType<T> boq, final C aok, final Level bru) {
        final Optional<T> optional5 = this.<C, T>getRecipeFor(boq, aok, bru);
        if (optional5.isPresent()) {
            return ((Recipe)optional5.get()).getRemainingItems(aok);
        }
        final NonNullList<ItemStack> gj6 = NonNullList.<ItemStack>withSize(aok.getContainerSize(), ItemStack.EMPTY);
        for (int integer7 = 0; integer7 < gj6.size(); ++integer7) {
            gj6.set(integer7, aok.getItem(integer7));
        }
        return gj6;
    }
    
    public Optional<? extends Recipe<?>> byKey(final ResourceLocation vk) {
        return this.recipes.values().stream().map(map -> (Recipe)map.get(vk)).filter(Objects::nonNull).findFirst();
    }
    
    public Collection<Recipe<?>> getRecipes() {
        return (Collection<Recipe<?>>)this.recipes.values().stream().flatMap(map -> map.values().stream()).collect(Collectors.toSet());
    }
    
    public Stream<ResourceLocation> getRecipeIds() {
        return (Stream<ResourceLocation>)this.recipes.values().stream().flatMap(map -> map.keySet().stream());
    }
    
    public static Recipe<?> fromJson(final ResourceLocation vk, final JsonObject jsonObject) {
        final String string3 = GsonHelper.getAsString(jsonObject, "type");
        return ((RecipeSerializer)Registry.RECIPE_SERIALIZER.getOptional(new ResourceLocation(string3)).orElseThrow(() -> new JsonSyntaxException("Invalid or unsupported recipe type '" + string3 + "'"))).fromJson(vk, jsonObject);
    }
    
    public void replaceRecipes(final Iterable<Recipe<?>> iterable) {
        this.hasErrors = false;
        final Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> map3 = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>)Maps.newHashMap();
        iterable.forEach(bon -> {
            final Map<ResourceLocation, Recipe<?>> map2 = (Map<ResourceLocation, Recipe<?>>)map3.computeIfAbsent(bon.getType(), boq -> Maps.newHashMap());
            final Recipe<?> bon2 = map2.put(bon.getId(), bon);
            if (bon2 != null) {
                throw new IllegalStateException(new StringBuilder().append("Duplicate recipe ignored with ID ").append(bon.getId()).toString());
            }
        });
        this.recipes = (Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>>)ImmutableMap.copyOf((Map)map3);
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        LOGGER = LogManager.getLogger();
    }
}
