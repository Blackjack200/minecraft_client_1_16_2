package net.minecraft.tags;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.world.item.Item;

public final class ItemTags {
    protected static final StaticTagHelper<Item> HELPER;
    public static final Tag.Named<Item> WOOL;
    public static final Tag.Named<Item> PLANKS;
    public static final Tag.Named<Item> STONE_BRICKS;
    public static final Tag.Named<Item> WOODEN_BUTTONS;
    public static final Tag.Named<Item> BUTTONS;
    public static final Tag.Named<Item> CARPETS;
    public static final Tag.Named<Item> WOODEN_DOORS;
    public static final Tag.Named<Item> WOODEN_STAIRS;
    public static final Tag.Named<Item> WOODEN_SLABS;
    public static final Tag.Named<Item> WOODEN_FENCES;
    public static final Tag.Named<Item> WOODEN_PRESSURE_PLATES;
    public static final Tag.Named<Item> WOODEN_TRAPDOORS;
    public static final Tag.Named<Item> DOORS;
    public static final Tag.Named<Item> SAPLINGS;
    public static final Tag.Named<Item> LOGS_THAT_BURN;
    public static final Tag.Named<Item> LOGS;
    public static final Tag.Named<Item> DARK_OAK_LOGS;
    public static final Tag.Named<Item> OAK_LOGS;
    public static final Tag.Named<Item> BIRCH_LOGS;
    public static final Tag.Named<Item> ACACIA_LOGS;
    public static final Tag.Named<Item> JUNGLE_LOGS;
    public static final Tag.Named<Item> SPRUCE_LOGS;
    public static final Tag.Named<Item> CRIMSON_STEMS;
    public static final Tag.Named<Item> WARPED_STEMS;
    public static final Tag.Named<Item> BANNERS;
    public static final Tag.Named<Item> SAND;
    public static final Tag.Named<Item> STAIRS;
    public static final Tag.Named<Item> SLABS;
    public static final Tag.Named<Item> WALLS;
    public static final Tag.Named<Item> ANVIL;
    public static final Tag.Named<Item> RAILS;
    public static final Tag.Named<Item> LEAVES;
    public static final Tag.Named<Item> TRAPDOORS;
    public static final Tag.Named<Item> SMALL_FLOWERS;
    public static final Tag.Named<Item> BEDS;
    public static final Tag.Named<Item> FENCES;
    public static final Tag.Named<Item> TALL_FLOWERS;
    public static final Tag.Named<Item> FLOWERS;
    public static final Tag.Named<Item> PIGLIN_REPELLENTS;
    public static final Tag.Named<Item> PIGLIN_LOVED;
    public static final Tag.Named<Item> GOLD_ORES;
    public static final Tag.Named<Item> NON_FLAMMABLE_WOOD;
    public static final Tag.Named<Item> SOUL_FIRE_BASE_BLOCKS;
    public static final Tag.Named<Item> BOATS;
    public static final Tag.Named<Item> FISHES;
    public static final Tag.Named<Item> SIGNS;
    public static final Tag.Named<Item> MUSIC_DISCS;
    public static final Tag.Named<Item> CREEPER_DROP_MUSIC_DISCS;
    public static final Tag.Named<Item> COALS;
    public static final Tag.Named<Item> ARROWS;
    public static final Tag.Named<Item> LECTERN_BOOKS;
    public static final Tag.Named<Item> BEACON_PAYMENT_ITEMS;
    public static final Tag.Named<Item> STONE_TOOL_MATERIALS;
    public static final Tag.Named<Item> STONE_CRAFTING_MATERIALS;
    
    private static Tag.Named<Item> bind(final String string) {
        return ItemTags.HELPER.bind(string);
    }
    
    public static TagCollection<Item> getAllTags() {
        return ItemTags.HELPER.getAllTags();
    }
    
    public static List<? extends Tag.Named<Item>> getWrappers() {
        return ItemTags.HELPER.getWrappers();
    }
    
    static {
        HELPER = StaticTags.<Item>create(new ResourceLocation("item"), (java.util.function.Function<TagContainer, TagCollection<Item>>)TagContainer::getItems);
        WOOL = bind("wool");
        PLANKS = bind("planks");
        STONE_BRICKS = bind("stone_bricks");
        WOODEN_BUTTONS = bind("wooden_buttons");
        BUTTONS = bind("buttons");
        CARPETS = bind("carpets");
        WOODEN_DOORS = bind("wooden_doors");
        WOODEN_STAIRS = bind("wooden_stairs");
        WOODEN_SLABS = bind("wooden_slabs");
        WOODEN_FENCES = bind("wooden_fences");
        WOODEN_PRESSURE_PLATES = bind("wooden_pressure_plates");
        WOODEN_TRAPDOORS = bind("wooden_trapdoors");
        DOORS = bind("doors");
        SAPLINGS = bind("saplings");
        LOGS_THAT_BURN = bind("logs_that_burn");
        LOGS = bind("logs");
        DARK_OAK_LOGS = bind("dark_oak_logs");
        OAK_LOGS = bind("oak_logs");
        BIRCH_LOGS = bind("birch_logs");
        ACACIA_LOGS = bind("acacia_logs");
        JUNGLE_LOGS = bind("jungle_logs");
        SPRUCE_LOGS = bind("spruce_logs");
        CRIMSON_STEMS = bind("crimson_stems");
        WARPED_STEMS = bind("warped_stems");
        BANNERS = bind("banners");
        SAND = bind("sand");
        STAIRS = bind("stairs");
        SLABS = bind("slabs");
        WALLS = bind("walls");
        ANVIL = bind("anvil");
        RAILS = bind("rails");
        LEAVES = bind("leaves");
        TRAPDOORS = bind("trapdoors");
        SMALL_FLOWERS = bind("small_flowers");
        BEDS = bind("beds");
        FENCES = bind("fences");
        TALL_FLOWERS = bind("tall_flowers");
        FLOWERS = bind("flowers");
        PIGLIN_REPELLENTS = bind("piglin_repellents");
        PIGLIN_LOVED = bind("piglin_loved");
        GOLD_ORES = bind("gold_ores");
        NON_FLAMMABLE_WOOD = bind("non_flammable_wood");
        SOUL_FIRE_BASE_BLOCKS = bind("soul_fire_base_blocks");
        BOATS = bind("boats");
        FISHES = bind("fishes");
        SIGNS = bind("signs");
        MUSIC_DISCS = bind("music_discs");
        CREEPER_DROP_MUSIC_DISCS = bind("creeper_drop_music_discs");
        COALS = bind("coals");
        ARROWS = bind("arrows");
        LECTERN_BOOKS = bind("lectern_books");
        BEACON_PAYMENT_ITEMS = bind("beacon_payment_items");
        STONE_TOOL_MATERIALS = bind("stone_tool_materials");
        STONE_CRAFTING_MATERIALS = bind("stone_crafting_materials");
    }
}
