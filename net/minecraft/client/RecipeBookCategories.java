package net.minecraft.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.inventory.RecipeBookType;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import java.util.Map;
import java.util.List;

public enum RecipeBookCategories {
    CRAFTING_SEARCH(new ItemStack[] { new ItemStack(Items.COMPASS) }), 
    CRAFTING_BUILDING_BLOCKS(new ItemStack[] { new ItemStack(Blocks.BRICKS) }), 
    CRAFTING_REDSTONE(new ItemStack[] { new ItemStack(Items.REDSTONE) }), 
    CRAFTING_EQUIPMENT(new ItemStack[] { new ItemStack(Items.IRON_AXE), new ItemStack(Items.GOLDEN_SWORD) }), 
    CRAFTING_MISC(new ItemStack[] { new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.APPLE) }), 
    FURNACE_SEARCH(new ItemStack[] { new ItemStack(Items.COMPASS) }), 
    FURNACE_FOOD(new ItemStack[] { new ItemStack(Items.PORKCHOP) }), 
    FURNACE_BLOCKS(new ItemStack[] { new ItemStack(Blocks.STONE) }), 
    FURNACE_MISC(new ItemStack[] { new ItemStack(Items.LAVA_BUCKET), new ItemStack(Items.EMERALD) }), 
    BLAST_FURNACE_SEARCH(new ItemStack[] { new ItemStack(Items.COMPASS) }), 
    BLAST_FURNACE_BLOCKS(new ItemStack[] { new ItemStack(Blocks.REDSTONE_ORE) }), 
    BLAST_FURNACE_MISC(new ItemStack[] { new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.GOLDEN_LEGGINGS) }), 
    SMOKER_SEARCH(new ItemStack[] { new ItemStack(Items.COMPASS) }), 
    SMOKER_FOOD(new ItemStack[] { new ItemStack(Items.PORKCHOP) }), 
    STONECUTTER(new ItemStack[] { new ItemStack(Items.CHISELED_STONE_BRICKS) }), 
    SMITHING(new ItemStack[] { new ItemStack(Items.NETHERITE_CHESTPLATE) }), 
    CAMPFIRE(new ItemStack[] { new ItemStack(Items.PORKCHOP) }), 
    UNKNOWN(new ItemStack[] { new ItemStack(Items.BARRIER) });
    
    public static final List<RecipeBookCategories> SMOKER_CATEGORIES;
    public static final List<RecipeBookCategories> BLAST_FURNACE_CATEGORIES;
    public static final List<RecipeBookCategories> FURNACE_CATEGORIES;
    public static final List<RecipeBookCategories> CRAFTING_CATEGORIES;
    public static final Map<RecipeBookCategories, List<RecipeBookCategories>> AGGREGATE_CATEGORIES;
    private final List<ItemStack> itemIcons;
    
    private RecipeBookCategories(final ItemStack[] arr) {
        this.itemIcons = (List<ItemStack>)ImmutableList.copyOf((Object[])arr);
    }
    
    public static List<RecipeBookCategories> getCategories(final RecipeBookType bjh) {
        switch (bjh) {
            case CRAFTING: {
                return RecipeBookCategories.CRAFTING_CATEGORIES;
            }
            case FURNACE: {
                return RecipeBookCategories.FURNACE_CATEGORIES;
            }
            case BLAST_FURNACE: {
                return RecipeBookCategories.BLAST_FURNACE_CATEGORIES;
            }
            case SMOKER: {
                return RecipeBookCategories.SMOKER_CATEGORIES;
            }
            default: {
                return (List<RecipeBookCategories>)ImmutableList.of();
            }
        }
    }
    
    public List<ItemStack> getIconItems() {
        return this.itemIcons;
    }
    
    static {
        SMOKER_CATEGORIES = (List)ImmutableList.of(RecipeBookCategories.SMOKER_SEARCH, RecipeBookCategories.SMOKER_FOOD);
        BLAST_FURNACE_CATEGORIES = (List)ImmutableList.of(RecipeBookCategories.BLAST_FURNACE_SEARCH, RecipeBookCategories.BLAST_FURNACE_BLOCKS, RecipeBookCategories.BLAST_FURNACE_MISC);
        FURNACE_CATEGORIES = (List)ImmutableList.of(RecipeBookCategories.FURNACE_SEARCH, RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC);
        CRAFTING_CATEGORIES = (List)ImmutableList.of(RecipeBookCategories.CRAFTING_SEARCH, RecipeBookCategories.CRAFTING_EQUIPMENT, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, RecipeBookCategories.CRAFTING_MISC, RecipeBookCategories.CRAFTING_REDSTONE);
        AGGREGATE_CATEGORIES = (Map)ImmutableMap.of(RecipeBookCategories.CRAFTING_SEARCH, ImmutableList.of((Object)RecipeBookCategories.CRAFTING_EQUIPMENT, (Object)RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, (Object)RecipeBookCategories.CRAFTING_MISC, (Object)RecipeBookCategories.CRAFTING_REDSTONE), RecipeBookCategories.FURNACE_SEARCH, ImmutableList.of((Object)RecipeBookCategories.FURNACE_FOOD, (Object)RecipeBookCategories.FURNACE_BLOCKS, (Object)RecipeBookCategories.FURNACE_MISC), RecipeBookCategories.BLAST_FURNACE_SEARCH, ImmutableList.of((Object)RecipeBookCategories.BLAST_FURNACE_BLOCKS, (Object)RecipeBookCategories.BLAST_FURNACE_MISC), RecipeBookCategories.SMOKER_SEARCH, ImmutableList.of((Object)RecipeBookCategories.SMOKER_FOOD));
    }
}
