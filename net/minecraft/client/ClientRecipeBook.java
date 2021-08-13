package net.minecraft.client;

import org.apache.logging.log4j.LogManager;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import java.util.Collections;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.util.Supplier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.RecipeType;
import java.util.Iterator;
import com.google.common.collect.Table;
import com.google.common.collect.Lists;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import net.minecraft.world.item.crafting.Recipe;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.stats.RecipeBook;

public class ClientRecipeBook extends RecipeBook {
    private static final Logger LOGGER;
    private Map<RecipeBookCategories, List<RecipeCollection>> collectionsByTab;
    private List<RecipeCollection> allCollections;
    
    public ClientRecipeBook() {
        this.collectionsByTab = (Map<RecipeBookCategories, List<RecipeCollection>>)ImmutableMap.of();
        this.allCollections = (List<RecipeCollection>)ImmutableList.of();
    }
    
    public void setupCollections(final Iterable<Recipe<?>> iterable) {
        final Map<RecipeBookCategories, List<List<Recipe<?>>>> map3 = categorizeAndGroupRecipes(iterable);
        final Map<RecipeBookCategories, List<RecipeCollection>> map4 = (Map<RecipeBookCategories, List<RecipeCollection>>)Maps.newHashMap();
        final ImmutableList.Builder<RecipeCollection> builder5 = (ImmutableList.Builder<RecipeCollection>)ImmutableList.builder();
        map3.forEach((dkd, list) -> {
            final List list2 = (List)map4.put(dkd, list.stream().map4(RecipeCollection::new).peek(builder5::add).collect(ImmutableList.toImmutableList()));
        });
        RecipeBookCategories.AGGREGATE_CATEGORIES.forEach((dkd, list) -> {
            final List list2 = (List)map4.put(dkd, list.stream().flatMap(dkd -> ((List)map4.getOrDefault(dkd, ImmutableList.of())).stream()).collect(ImmutableList.toImmutableList()));
        });
        this.collectionsByTab = (Map<RecipeBookCategories, List<RecipeCollection>>)ImmutableMap.copyOf((Map)map4);
        this.allCollections = (List<RecipeCollection>)builder5.build();
    }
    
    private static Map<RecipeBookCategories, List<List<Recipe<?>>>> categorizeAndGroupRecipes(final Iterable<Recipe<?>> iterable) {
        final Map<RecipeBookCategories, List<List<Recipe<?>>>> map2 = (Map<RecipeBookCategories, List<List<Recipe<?>>>>)Maps.newHashMap();
        final Table<RecipeBookCategories, String, List<Recipe<?>>> table3 = (Table<RecipeBookCategories, String, List<Recipe<?>>>)HashBasedTable.create();
        for (final Recipe<?> bon5 : iterable) {
            if (bon5.isSpecial()) {
                continue;
            }
            final RecipeBookCategories dkd6 = getCategory(bon5);
            final String string7 = bon5.getGroup();
            if (string7.isEmpty()) {
                ((List)map2.computeIfAbsent(dkd6, dkd -> Lists.newArrayList())).add(ImmutableList.of((Object)bon5));
            }
            else {
                List<Recipe<?>> list8 = (List<Recipe<?>>)table3.get(dkd6, string7);
                if (list8 == null) {
                    list8 = (List<Recipe<?>>)Lists.newArrayList();
                    table3.put(dkd6, string7, list8);
                    ((List)map2.computeIfAbsent(dkd6, dkd -> Lists.newArrayList())).add(list8);
                }
                list8.add(bon5);
            }
        }
        return map2;
    }
    
    private static RecipeBookCategories getCategory(final Recipe<?> bon) {
        final RecipeType<?> boq2 = bon.getType();
        if (boq2 == RecipeType.CRAFTING) {
            final ItemStack bly3 = bon.getResultItem();
            final CreativeModeTab bkp4 = bly3.getItem().getItemCategory();
            if (bkp4 == CreativeModeTab.TAB_BUILDING_BLOCKS) {
                return RecipeBookCategories.CRAFTING_BUILDING_BLOCKS;
            }
            if (bkp4 == CreativeModeTab.TAB_TOOLS || bkp4 == CreativeModeTab.TAB_COMBAT) {
                return RecipeBookCategories.CRAFTING_EQUIPMENT;
            }
            if (bkp4 == CreativeModeTab.TAB_REDSTONE) {
                return RecipeBookCategories.CRAFTING_REDSTONE;
            }
            return RecipeBookCategories.CRAFTING_MISC;
        }
        else if (boq2 == RecipeType.SMELTING) {
            if (bon.getResultItem().getItem().isEdible()) {
                return RecipeBookCategories.FURNACE_FOOD;
            }
            if (bon.getResultItem().getItem() instanceof BlockItem) {
                return RecipeBookCategories.FURNACE_BLOCKS;
            }
            return RecipeBookCategories.FURNACE_MISC;
        }
        else if (boq2 == RecipeType.BLASTING) {
            if (bon.getResultItem().getItem() instanceof BlockItem) {
                return RecipeBookCategories.BLAST_FURNACE_BLOCKS;
            }
            return RecipeBookCategories.BLAST_FURNACE_MISC;
        }
        else {
            if (boq2 == RecipeType.SMOKING) {
                return RecipeBookCategories.SMOKER_FOOD;
            }
            if (boq2 == RecipeType.STONECUTTING) {
                return RecipeBookCategories.STONECUTTER;
            }
            if (boq2 == RecipeType.CAMPFIRE_COOKING) {
                return RecipeBookCategories.CAMPFIRE;
            }
            if (boq2 == RecipeType.SMITHING) {
                return RecipeBookCategories.SMITHING;
            }
            ClientRecipeBook.LOGGER.warn("Unknown recipe category: {}/{}", new Supplier[] { () -> Registry.RECIPE_TYPE.getKey(bon.getType()), bon::getId });
            return RecipeBookCategories.UNKNOWN;
        }
    }
    
    public List<RecipeCollection> getCollections() {
        return this.allCollections;
    }
    
    public List<RecipeCollection> getCollection(final RecipeBookCategories dkd) {
        return (List<RecipeCollection>)this.collectionsByTab.getOrDefault(dkd, Collections.emptyList());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
