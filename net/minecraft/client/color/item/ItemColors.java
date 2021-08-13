package net.minecraft.client.color.item;

import net.minecraft.world.item.Item;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.MapItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.core.IdMapper;

public class ItemColors {
    private final IdMapper<ItemColor> itemColors;
    
    public ItemColors() {
        this.itemColors = new IdMapper<ItemColor>(32);
    }
    
    public static ItemColors createDefault(final BlockColors dkl) {
        final ItemColors dkp2 = new ItemColors();
        dkp2.register((bly, integer) -> (integer > 0) ? -1 : ((DyeableLeatherItem)bly.getItem()).getColor(bly), Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS, Items.LEATHER_HORSE_ARMOR);
        dkp2.register((bly, integer) -> GrassColor.get(0.5, 1.0), Blocks.TALL_GRASS, Blocks.LARGE_FERN);
        CompoundTag md3;
        int[] arr4;
        int integer2;
        int integer3;
        int integer4;
        final int[] array;
        int length;
        int i = 0;
        int integer5;
        int integer6;
        int integer7;
        int integer8;
        dkp2.register((bly, integer) -> {
            if (integer != 1) {
                return -1;
            }
            else {
                md3 = bly.getTagElement("Explosion");
                arr4 = (int[])((md3 != null && md3.contains("Colors", 11)) ? md3.getIntArray("Colors") : null);
                if (arr4 == null || arr4.length == 0) {
                    return 9079434;
                }
                else if (arr4.length == 1) {
                    return arr4[0];
                }
                else {
                    integer2 = 0;
                    integer3 = 0;
                    integer4 = 0;
                    for (length = array.length; i < length; ++i) {
                        integer5 = array[i];
                        integer2 += (integer5 & 0xFF0000) >> 16;
                        integer3 += (integer5 & 0xFF00) >> 8;
                        integer4 += (integer5 & 0xFF) >> 0;
                    }
                    integer6 = integer2 / arr4.length;
                    integer7 = integer3 / arr4.length;
                    integer8 = integer4 / arr4.length;
                    return integer6 << 16 | integer7 << 8 | integer8;
                }
            }
        }, Items.FIREWORK_STAR);
        dkp2.register((bly, integer) -> (integer > 0) ? -1 : PotionUtils.getColor(bly), Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION);
        for (final SpawnEggItem bmx4 : SpawnEggItem.eggs()) {
            dkp2.register((bly, integer) -> bmx4.getColor(integer), bmx4);
        }
        final BlockState cee4;
        dkp2.register((bly, integer) -> {
            cee4 = ((BlockItem)bly.getItem()).getBlock().defaultBlockState();
            return dkl.getColor(cee4, null, null, integer);
        }, Blocks.GRASS_BLOCK, Blocks.GRASS, Blocks.FERN, Blocks.VINE, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.ACACIA_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.LILY_PAD);
        dkp2.register((bly, integer) -> (integer == 0) ? PotionUtils.getColor(bly) : -1, Items.TIPPED_ARROW);
        dkp2.register((bly, integer) -> (integer == 0) ? -1 : MapItem.getColor(bly), Items.FILLED_MAP);
        return dkp2;
    }
    
    public int getColor(final ItemStack bly, final int integer) {
        final ItemColor dko4 = this.itemColors.byId(Registry.ITEM.getId(bly.getItem()));
        return (dko4 == null) ? -1 : dko4.getColor(bly, integer);
    }
    
    public void register(final ItemColor dko, final ItemLike... arr) {
        for (final ItemLike brt7 : arr) {
            this.itemColors.addMapping(dko, Item.getId(brt7.asItem()));
        }
    }
}
