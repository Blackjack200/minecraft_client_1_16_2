package net.minecraft.world.entity.npc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Locale;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.MapItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import com.google.common.collect.Lists;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potion;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.world.item.trading.MerchantOffer;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.item.Item;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Map;

public class VillagerTrades {
    public static final Map<VillagerProfession, Int2ObjectMap<ItemListing[]>> TRADES;
    public static final Int2ObjectMap<ItemListing[]> WANDERING_TRADER_TRADES;
    
    private static Int2ObjectMap<ItemListing[]> toIntMap(final ImmutableMap<Integer, ItemListing[]> immutableMap) {
        return (Int2ObjectMap<ItemListing[]>)new Int2ObjectOpenHashMap((Map)immutableMap);
    }
    
    static {
        TRADES = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(VillagerProfession.FARMER, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.WHEAT, 20, 16, 2), new EmeraldForItems(Items.POTATO, 26, 16, 2), new EmeraldForItems(Items.CARROT, 22, 16, 2), new EmeraldForItems(Items.BEETROOT, 15, 16, 2), new ItemsForEmeralds(Items.BREAD, 1, 6, 16, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Blocks.PUMPKIN, 6, 12, 10), new ItemsForEmeralds(Items.PUMPKIN_PIE, 1, 4, 5), new ItemsForEmeralds(Items.APPLE, 1, 4, 16, 5) }, (Object)3, (Object)new ItemListing[] { new ItemsForEmeralds(Items.COOKIE, 3, 18, 10), new EmeraldForItems(Blocks.MELON, 4, 12, 20) }, (Object)4, (Object)new ItemListing[] { new ItemsForEmeralds(Blocks.CAKE, 1, 1, 12, 15), new SuspisciousStewForEmerald(MobEffects.NIGHT_VISION, 100, 15), new SuspisciousStewForEmerald(MobEffects.JUMP, 160, 15), new SuspisciousStewForEmerald(MobEffects.WEAKNESS, 140, 15), new SuspisciousStewForEmerald(MobEffects.BLINDNESS, 120, 15), new SuspisciousStewForEmerald(MobEffects.POISON, 280, 15), new SuspisciousStewForEmerald(MobEffects.SATURATION, 7, 15) }, (Object)5, (Object)new ItemListing[] { new ItemsForEmeralds(Items.GOLDEN_CARROT, 3, 3, 30), new ItemsForEmeralds(Items.GLISTERING_MELON_SLICE, 4, 3, 30) })));
            hashMap.put(VillagerProfession.FISHERMAN, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.STRING, 20, 16, 2), new EmeraldForItems(Items.COAL, 10, 16, 2), new ItemsAndEmeraldsToItems(Items.COD, 6, Items.COOKED_COD, 6, 16, 1), new ItemsForEmeralds(Items.COD_BUCKET, 3, 1, 16, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.COD, 15, 16, 10), new ItemsAndEmeraldsToItems(Items.SALMON, 6, Items.COOKED_SALMON, 6, 16, 5), new ItemsForEmeralds(Items.CAMPFIRE, 2, 1, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.SALMON, 13, 16, 20), new EnchantedItemForEmeralds(Items.FISHING_ROD, 3, 3, 10, 0.2f) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.TROPICAL_FISH, 6, 12, 30) }, (Object)5, (Object)new ItemListing[] { new EmeraldForItems(Items.PUFFERFISH, 4, 12, 30), new EmeraldsForVillagerTypeItem(1, 12, 30, (Map<VillagerType, Item>)ImmutableMap.builder().put((Object)VillagerType.PLAINS, (Object)Items.OAK_BOAT).put((Object)VillagerType.TAIGA, (Object)Items.SPRUCE_BOAT).put((Object)VillagerType.SNOW, (Object)Items.SPRUCE_BOAT).put((Object)VillagerType.DESERT, (Object)Items.JUNGLE_BOAT).put((Object)VillagerType.JUNGLE, (Object)Items.JUNGLE_BOAT).put((Object)VillagerType.SAVANNA, (Object)Items.ACACIA_BOAT).put((Object)VillagerType.SWAMP, (Object)Items.DARK_OAK_BOAT).build()) })));
            hashMap.put(VillagerProfession.SHEPHERD, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Blocks.WHITE_WOOL, 18, 16, 2), new EmeraldForItems(Blocks.BROWN_WOOL, 18, 16, 2), new EmeraldForItems(Blocks.BLACK_WOOL, 18, 16, 2), new EmeraldForItems(Blocks.GRAY_WOOL, 18, 16, 2), new ItemsForEmeralds(Items.SHEARS, 2, 1, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.WHITE_DYE, 12, 16, 10), new EmeraldForItems(Items.GRAY_DYE, 12, 16, 10), new EmeraldForItems(Items.BLACK_DYE, 12, 16, 10), new EmeraldForItems(Items.LIGHT_BLUE_DYE, 12, 16, 10), new EmeraldForItems(Items.LIME_DYE, 12, 16, 10), new ItemsForEmeralds(Blocks.WHITE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.ORANGE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.MAGENTA_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.YELLOW_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIME_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.PINK_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.CYAN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.PURPLE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BROWN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.GREEN_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.RED_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.BLACK_WOOL, 1, 1, 16, 5), new ItemsForEmeralds(Blocks.WHITE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.ORANGE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.MAGENTA_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.YELLOW_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIME_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.PINK_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.GRAY_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.CYAN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.PURPLE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BROWN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.GREEN_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.RED_CARPET, 1, 4, 16, 5), new ItemsForEmeralds(Blocks.BLACK_CARPET, 1, 4, 16, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.YELLOW_DYE, 12, 16, 20), new EmeraldForItems(Items.LIGHT_GRAY_DYE, 12, 16, 20), new EmeraldForItems(Items.ORANGE_DYE, 12, 16, 20), new EmeraldForItems(Items.RED_DYE, 12, 16, 20), new EmeraldForItems(Items.PINK_DYE, 12, 16, 20), new ItemsForEmeralds(Blocks.WHITE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.YELLOW_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.RED_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BLACK_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BLUE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.BROWN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.CYAN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.GRAY_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.GREEN_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.LIME_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.MAGENTA_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.ORANGE_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.PINK_BED, 3, 1, 12, 10), new ItemsForEmeralds(Blocks.PURPLE_BED, 3, 1, 12, 10) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.BROWN_DYE, 12, 16, 30), new EmeraldForItems(Items.PURPLE_DYE, 12, 16, 30), new EmeraldForItems(Items.BLUE_DYE, 12, 16, 30), new EmeraldForItems(Items.GREEN_DYE, 12, 16, 30), new EmeraldForItems(Items.MAGENTA_DYE, 12, 16, 30), new EmeraldForItems(Items.CYAN_DYE, 12, 16, 30), new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 12, 15), new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 15) }, (Object)5, (Object)new ItemListing[] { new ItemsForEmeralds(Items.PAINTING, 2, 3, 30) })));
            hashMap.put(VillagerProfession.FLETCHER, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.STICK, 32, 16, 2), new ItemsForEmeralds(Items.ARROW, 1, 16, 1), new ItemsAndEmeraldsToItems(Blocks.GRAVEL, 10, Items.FLINT, 10, 12, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.FLINT, 26, 12, 10), new ItemsForEmeralds(Items.BOW, 2, 1, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.STRING, 14, 16, 20), new ItemsForEmeralds(Items.CROSSBOW, 3, 1, 10) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.FEATHER, 24, 16, 30), new EnchantedItemForEmeralds(Items.BOW, 2, 3, 15) }, (Object)5, (Object)new ItemListing[] { new EmeraldForItems(Items.TRIPWIRE_HOOK, 8, 12, 30), new EnchantedItemForEmeralds(Items.CROSSBOW, 3, 3, 15), new TippedArrowForItemsAndEmeralds(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 12, 30) })));
            hashMap.put(VillagerProfession.LIBRARIAN, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.builder().put((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.PAPER, 24, 16, 2), new EnchantBookForEmeralds(1), new ItemsForEmeralds(Blocks.BOOKSHELF, 9, 1, 12, 1) }).put((Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.BOOK, 4, 12, 10), new EnchantBookForEmeralds(5), new ItemsForEmeralds(Items.LANTERN, 1, 1, 5) }).put((Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.INK_SAC, 5, 12, 20), new EnchantBookForEmeralds(10), new ItemsForEmeralds(Items.GLASS, 1, 4, 10) }).put((Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.WRITABLE_BOOK, 2, 12, 30), new EnchantBookForEmeralds(15), new ItemsForEmeralds(Items.CLOCK, 5, 1, 15), new ItemsForEmeralds(Items.COMPASS, 4, 1, 15) }).put((Object)5, (Object)new ItemListing[] { new ItemsForEmeralds(Items.NAME_TAG, 20, 1, 30) }).build()));
            hashMap.put(VillagerProfession.CARTOGRAPHER, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.PAPER, 24, 16, 2), new ItemsForEmeralds(Items.MAP, 7, 1, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.GLASS_PANE, 11, 16, 10), new TreasureMapForEmeralds(13, StructureFeature.OCEAN_MONUMENT, MapDecoration.Type.MONUMENT, 12, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.COMPASS, 1, 12, 20), new TreasureMapForEmeralds(14, StructureFeature.WOODLAND_MANSION, MapDecoration.Type.MANSION, 12, 10) }, (Object)4, (Object)new ItemListing[] { new ItemsForEmeralds(Items.ITEM_FRAME, 7, 1, 15), new ItemsForEmeralds(Items.WHITE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.BLUE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.LIGHT_BLUE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.RED_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.PINK_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.GREEN_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.LIME_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.GRAY_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.BLACK_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.PURPLE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.MAGENTA_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.CYAN_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.BROWN_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.YELLOW_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.ORANGE_BANNER, 3, 1, 15), new ItemsForEmeralds(Items.LIGHT_GRAY_BANNER, 3, 1, 15) }, (Object)5, (Object)new ItemListing[] { new ItemsForEmeralds(Items.GLOBE_BANNER_PATTER, 8, 1, 30) })));
            hashMap.put(VillagerProfession.CLERIC, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.ROTTEN_FLESH, 32, 16, 2), new ItemsForEmeralds(Items.REDSTONE, 1, 2, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.GOLD_INGOT, 3, 12, 10), new ItemsForEmeralds(Items.LAPIS_LAZULI, 1, 1, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.RABBIT_FOOT, 2, 12, 20), new ItemsForEmeralds(Blocks.GLOWSTONE, 4, 1, 12, 10) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.SCUTE, 4, 12, 30), new EmeraldForItems(Items.GLASS_BOTTLE, 9, 12, 30), new ItemsForEmeralds(Items.ENDER_PEARL, 5, 1, 15) }, (Object)5, (Object)new ItemListing[] { new EmeraldForItems(Items.NETHER_WART, 22, 12, 30), new ItemsForEmeralds(Items.EXPERIENCE_BOTTLE, 3, 1, 30) })));
            hashMap.put(VillagerProfession.ARMORER, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack(Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2f), new ItemsForEmeralds(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2f), new ItemsForEmeralds(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2f), new ItemsForEmeralds(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2f) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2f), new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2f), new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2f) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.LAVA_BUCKET, 1, 12, 20), new EmeraldForItems(Items.DIAMOND, 1, 12, 20), new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2f), new ItemsForEmeralds(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2f), new ItemsForEmeralds(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2f) }, (Object)4, (Object)new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2f), new EnchantedItemForEmeralds(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2f) }, (Object)5, (Object)new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_HELMET, 8, 3, 30, 0.2f), new EnchantedItemForEmeralds(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2f) })));
            hashMap.put(VillagerProfession.WEAPONSMITH, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2f), new EnchantedItemForEmeralds(Items.IRON_SWORD, 2, 3, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2f) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.FLINT, 24, 12, 20) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2f) }, (Object)5, (Object)new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_SWORD, 8, 3, 30, 0.2f) })));
            hashMap.put(VillagerProfession.TOOLSMITH, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.COAL, 15, 16, 2), new ItemsForEmeralds(new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2f), new ItemsForEmeralds(new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2f), new ItemsForEmeralds(new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2f), new ItemsForEmeralds(new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2f) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeralds(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2f) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.FLINT, 30, 12, 20), new EnchantedItemForEmeralds(Items.IRON_AXE, 1, 3, 10, 0.2f), new EnchantedItemForEmeralds(Items.IRON_SHOVEL, 2, 3, 10, 0.2f), new EnchantedItemForEmeralds(Items.IRON_PICKAXE, 3, 3, 10, 0.2f), new ItemsForEmeralds(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2f) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeralds(Items.DIAMOND_AXE, 12, 3, 15, 0.2f), new EnchantedItemForEmeralds(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2f) }, (Object)5, (Object)new ItemListing[] { new EnchantedItemForEmeralds(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2f) })));
            hashMap.put(VillagerProfession.BUTCHER, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.CHICKEN, 14, 16, 2), new EmeraldForItems(Items.PORKCHOP, 7, 16, 2), new EmeraldForItems(Items.RABBIT, 4, 16, 2), new ItemsForEmeralds(Items.RABBIT_STEW, 1, 1, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.COAL, 15, 16, 2), new ItemsForEmeralds(Items.COOKED_PORKCHOP, 1, 5, 16, 5), new ItemsForEmeralds(Items.COOKED_CHICKEN, 1, 8, 16, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.MUTTON, 7, 16, 20), new EmeraldForItems(Items.BEEF, 10, 16, 20) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.DRIED_KELP_BLOCK, 10, 12, 30) }, (Object)5, (Object)new ItemListing[] { new EmeraldForItems(Items.SWEET_BERRIES, 10, 12, 30) })));
            hashMap.put(VillagerProfession.LEATHERWORKER, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.LEATHER, 6, 16, 2), new DyedArmorForEmeralds(Items.LEATHER_LEGGINGS, 3), new DyedArmorForEmeralds(Items.LEATHER_CHESTPLATE, 7) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Items.FLINT, 26, 12, 10), new DyedArmorForEmeralds(Items.LEATHER_HELMET, 5, 12, 5), new DyedArmorForEmeralds(Items.LEATHER_BOOTS, 4, 12, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Items.RABBIT_HIDE, 9, 12, 20), new DyedArmorForEmeralds(Items.LEATHER_CHESTPLATE, 7) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.SCUTE, 4, 12, 30), new DyedArmorForEmeralds(Items.LEATHER_HORSE_ARMOR, 6, 12, 15) }, (Object)5, (Object)new ItemListing[] { new ItemsForEmeralds(new ItemStack(Items.SADDLE), 6, 1, 12, 30, 0.2f), new DyedArmorForEmeralds(Items.LEATHER_HELMET, 5, 12, 30) })));
            hashMap.put(VillagerProfession.MASON, toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of((Object)1, (Object)new ItemListing[] { new EmeraldForItems(Items.CLAY_BALL, 10, 16, 2), new ItemsForEmeralds(Items.BRICK, 1, 10, 16, 1) }, (Object)2, (Object)new ItemListing[] { new EmeraldForItems(Blocks.STONE, 20, 16, 10), new ItemsForEmeralds(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5) }, (Object)3, (Object)new ItemListing[] { new EmeraldForItems(Blocks.GRANITE, 16, 16, 20), new EmeraldForItems(Blocks.ANDESITE, 16, 16, 20), new EmeraldForItems(Blocks.DIORITE, 16, 16, 20), new ItemsForEmeralds(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new ItemsForEmeralds(Blocks.POLISHED_GRANITE, 1, 4, 16, 10) }, (Object)4, (Object)new ItemListing[] { new EmeraldForItems(Items.QUARTZ, 12, 12, 30), new ItemsForEmeralds(Blocks.ORANGE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.WHITE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.RED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PINK_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.MAGENTA_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIME_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GREEN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.CYAN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PURPLE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.YELLOW_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BROWN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeralds(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 12, 15) }, (Object)5, (Object)new ItemListing[] { new ItemsForEmeralds(Blocks.QUARTZ_PILLAR, 1, 1, 12, 30), new ItemsForEmeralds(Blocks.QUARTZ_BLOCK, 1, 1, 12, 30) })));
        }));
        WANDERING_TRADER_TRADES = toIntMap((ImmutableMap<Integer, ItemListing[]>)ImmutableMap.of(1, new ItemListing[] { new ItemsForEmeralds(Items.SEA_PICKLE, 2, 1, 5, 1), new ItemsForEmeralds(Items.SLIME_BALL, 4, 1, 5, 1), new ItemsForEmeralds(Items.GLOWSTONE, 2, 1, 5, 1), new ItemsForEmeralds(Items.NAUTILUS_SHELL, 5, 1, 5, 1), new ItemsForEmeralds(Items.FERN, 1, 1, 12, 1), new ItemsForEmeralds(Items.SUGAR_CANE, 1, 1, 8, 1), new ItemsForEmeralds(Items.PUMPKIN, 1, 1, 4, 1), new ItemsForEmeralds(Items.KELP, 3, 1, 12, 1), new ItemsForEmeralds(Items.CACTUS, 3, 1, 8, 1), new ItemsForEmeralds(Items.DANDELION, 1, 1, 12, 1), new ItemsForEmeralds(Items.POPPY, 1, 1, 12, 1), new ItemsForEmeralds(Items.BLUE_ORCHID, 1, 1, 8, 1), new ItemsForEmeralds(Items.ALLIUM, 1, 1, 12, 1), new ItemsForEmeralds(Items.AZURE_BLUET, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.PINK_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.OXEYE_DAISY, 1, 1, 12, 1), new ItemsForEmeralds(Items.CORNFLOWER, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1), new ItemsForEmeralds(Items.WHEAT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.BEETROOT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.PUMPKIN_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.MELON_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.ACACIA_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.BIRCH_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.DARK_OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.JUNGLE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.SPRUCE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.RED_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.WHITE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PINK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLACK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GREEN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.MAGENTA_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.YELLOW_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PURPLE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIME_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.ORANGE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BROWN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.CYAN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.VINE, 1, 1, 12, 1), new ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_MUSHROOM, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_PAD, 1, 2, 5, 1), new ItemsForEmeralds(Items.SAND, 1, 8, 8, 1), new ItemsForEmeralds(Items.RED_SAND, 1, 4, 6, 1) }, 2, new ItemListing[] { new ItemsForEmeralds(Items.TROPICAL_FISH_BUCKET, 5, 1, 4, 1), new ItemsForEmeralds(Items.PUFFERFISH_BUCKET, 5, 1, 4, 1), new ItemsForEmeralds(Items.PACKED_ICE, 3, 1, 6, 1), new ItemsForEmeralds(Items.BLUE_ICE, 6, 1, 6, 1), new ItemsForEmeralds(Items.GUNPOWDER, 1, 1, 8, 1), new ItemsForEmeralds(Items.PODZOL, 3, 3, 6, 1) }));
    }
    
    static class EmeraldForItems implements ItemListing {
        private final Item item;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public EmeraldForItems(final ItemLike brt, final int integer2, final int integer3, final int integer4) {
            this.item = brt.asItem();
            this.cost = integer2;
            this.maxUses = integer3;
            this.villagerXp = integer4;
            this.priceMultiplier = 0.05f;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(this.item, this.cost);
            return new MerchantOffer(bly4, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class EmeraldsForVillagerTypeItem implements ItemListing {
        private final Map<VillagerType, Item> trades;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        
        public EmeraldsForVillagerTypeItem(final int integer1, final int integer2, final int integer3, final Map<VillagerType, Item> map) {
            Registry.VILLAGER_TYPE.stream().filter(bfl -> !map.containsKey(bfl)).findAny().ifPresent(bfl -> {
                throw new IllegalStateException(new StringBuilder().append("Missing trade for villager type: ").append(Registry.VILLAGER_TYPE.getKey(bfl)).toString());
            });
            this.trades = map;
            this.cost = integer1;
            this.maxUses = integer2;
            this.villagerXp = integer3;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            if (apx instanceof VillagerDataHolder) {
                final ItemStack bly4 = new ItemStack((ItemLike)this.trades.get(((VillagerDataHolder)apx).getVillagerData().getType()), this.cost);
                return new MerchantOffer(bly4, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05f);
            }
            return null;
        }
    }
    
    static class ItemsForEmeralds implements ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public ItemsForEmeralds(final Block bul, final int integer2, final int integer3, final int integer4, final int integer5) {
            this(new ItemStack(bul), integer2, integer3, integer4, integer5);
        }
        
        public ItemsForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4) {
            this(new ItemStack(blu), integer2, integer3, 12, integer4);
        }
        
        public ItemsForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4, final int integer5) {
            this(new ItemStack(blu), integer2, integer3, integer4, integer5);
        }
        
        public ItemsForEmeralds(final ItemStack bly, final int integer2, final int integer3, final int integer4, final int integer5) {
            this(bly, integer2, integer3, integer4, integer5, 0.05f);
        }
        
        public ItemsForEmeralds(final ItemStack bly, final int integer2, final int integer3, final int integer4, final int integer5, final float float6) {
            this.itemStack = bly;
            this.emeraldCost = integer2;
            this.numberOfItems = integer3;
            this.maxUses = integer4;
            this.villagerXp = integer5;
            this.priceMultiplier = float6;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class SuspisciousStewForEmerald implements ItemListing {
        final MobEffect effect;
        final int duration;
        final int xp;
        private final float priceMultiplier;
        
        public SuspisciousStewForEmerald(final MobEffect app, final int integer2, final int integer3) {
            this.effect = app;
            this.duration = integer2;
            this.xp = integer3;
            this.priceMultiplier = 0.05f;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.saveMobEffect(bly4, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(Items.EMERALD, 1), bly4, 12, this.xp, this.priceMultiplier);
        }
    }
    
    static class EnchantedItemForEmeralds implements ItemListing {
        private final ItemStack itemStack;
        private final int baseEmeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public EnchantedItemForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4) {
            this(blu, integer2, integer3, integer4, 0.05f);
        }
        
        public EnchantedItemForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4, final float float5) {
            this.itemStack = new ItemStack(blu);
            this.baseEmeraldCost = integer2;
            this.maxUses = integer3;
            this.villagerXp = integer4;
            this.priceMultiplier = float5;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final int integer4 = 5 + random.nextInt(15);
            final ItemStack bly5 = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), integer4, false);
            final int integer5 = Math.min(this.baseEmeraldCost + integer4, 64);
            final ItemStack bly6 = new ItemStack(Items.EMERALD, integer5);
            return new MerchantOffer(bly6, bly5, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class TippedArrowForItemsAndEmeralds implements ItemListing {
        private final ItemStack toItem;
        private final int toCount;
        private final int emeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final Item fromItem;
        private final int fromCount;
        private final float priceMultiplier;
        
        public TippedArrowForItemsAndEmeralds(final Item blu1, final int integer2, final Item blu3, final int integer4, final int integer5, final int integer6, final int integer7) {
            this.toItem = new ItemStack(blu3);
            this.emeraldCost = integer5;
            this.maxUses = integer6;
            this.villagerXp = integer7;
            this.fromItem = blu1;
            this.fromCount = integer2;
            this.toCount = integer4;
            this.priceMultiplier = 0.05f;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(Items.EMERALD, this.emeraldCost);
            final List<Potion> list5 = (List<Potion>)Registry.POTION.stream().filter(bnq -> !bnq.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(bnq)).collect(Collectors.toList());
            final Potion bnq6 = (Potion)list5.get(random.nextInt(list5.size()));
            final ItemStack bly5 = PotionUtils.setPotion(new ItemStack(this.toItem.getItem(), this.toCount), bnq6);
            return new MerchantOffer(bly4, new ItemStack(this.fromItem, this.fromCount), bly5, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class DyedArmorForEmeralds implements ItemListing {
        private final Item item;
        private final int value;
        private final int maxUses;
        private final int villagerXp;
        
        public DyedArmorForEmeralds(final Item blu, final int integer) {
            this(blu, integer, 12, 1);
        }
        
        public DyedArmorForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4) {
            this.item = blu;
            this.value = integer2;
            this.maxUses = integer3;
            this.villagerXp = integer4;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(Items.EMERALD, this.value);
            ItemStack bly5 = new ItemStack(this.item);
            if (this.item instanceof DyeableArmorItem) {
                final List<DyeItem> list6 = (List<DyeItem>)Lists.newArrayList();
                list6.add(getRandomDye(random));
                if (random.nextFloat() > 0.7f) {
                    list6.add(getRandomDye(random));
                }
                if (random.nextFloat() > 0.8f) {
                    list6.add(getRandomDye(random));
                }
                bly5 = DyeableLeatherItem.dyeArmor(bly5, list6);
            }
            return new MerchantOffer(bly4, bly5, this.maxUses, this.villagerXp, 0.2f);
        }
        
        private static DyeItem getRandomDye(final Random random) {
            return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
        }
    }
    
    static class EnchantBookForEmeralds implements ItemListing {
        private final int villagerXp;
        
        public EnchantBookForEmeralds(final int integer) {
            this.villagerXp = integer;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final List<Enchantment> list4 = (List<Enchantment>)Registry.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
            final Enchantment bpp5 = (Enchantment)list4.get(random.nextInt(list4.size()));
            final int integer6 = Mth.nextInt(random, bpp5.getMinLevel(), bpp5.getMaxLevel());
            final ItemStack bly7 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(bpp5, integer6));
            int integer7 = 2 + random.nextInt(5 + integer6 * 10) + 3 * integer6;
            if (bpp5.isTreasureOnly()) {
                integer7 *= 2;
            }
            if (integer7 > 64) {
                integer7 = 64;
            }
            return new MerchantOffer(new ItemStack(Items.EMERALD, integer7), new ItemStack(Items.BOOK), bly7, 12, this.villagerXp, 0.2f);
        }
    }
    
    static class TreasureMapForEmeralds implements ItemListing {
        private final int emeraldCost;
        private final StructureFeature<?> destination;
        private final MapDecoration.Type destinationType;
        private final int maxUses;
        private final int villagerXp;
        
        public TreasureMapForEmeralds(final int integer1, final StructureFeature<?> ckx, final MapDecoration.Type a, final int integer4, final int integer5) {
            this.emeraldCost = integer1;
            this.destination = ckx;
            this.destinationType = a;
            this.maxUses = integer4;
            this.villagerXp = integer5;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            if (!(apx.level instanceof ServerLevel)) {
                return null;
            }
            final ServerLevel aag4 = (ServerLevel)apx.level;
            final BlockPos fx5 = aag4.findNearestMapFeature(this.destination, apx.blockPosition(), 100, true);
            if (fx5 != null) {
                final ItemStack bly6 = MapItem.create(aag4, fx5.getX(), fx5.getZ(), (byte)2, true, true);
                MapItem.renderBiomePreviewMap(aag4, bly6);
                MapItemSavedData.addTargetDecoration(bly6, fx5, "+", this.destinationType);
                bly6.setHoverName(new TranslatableComponent("filled_map." + this.destination.getFeatureName().toLowerCase(Locale.ROOT)));
                return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(Items.COMPASS), bly6, this.maxUses, this.villagerXp, 0.2f);
            }
            return null;
        }
    }
    
    static class ItemsAndEmeraldsToItems implements ItemListing {
        private final ItemStack fromItem;
        private final int fromCount;
        private final int emeraldCost;
        private final ItemStack toItem;
        private final int toCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public ItemsAndEmeraldsToItems(final ItemLike brt, final int integer2, final Item blu, final int integer4, final int integer5, final int integer6) {
            this(brt, integer2, 1, blu, integer4, integer5, integer6);
        }
        
        public ItemsAndEmeraldsToItems(final ItemLike brt, final int integer2, final int integer3, final Item blu, final int integer5, final int integer6, final int integer7) {
            this.fromItem = new ItemStack(brt);
            this.fromCount = integer2;
            this.emeraldCost = integer3;
            this.toItem = new ItemStack(blu);
            this.toCount = integer5;
            this.maxUses = integer6;
            this.villagerXp = integer7;
            this.priceMultiplier = 0.05f;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    public interface ItemListing {
        @Nullable
        MerchantOffer getOffer(final Entity apx, final Random random);
    }
}
