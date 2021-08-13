package net.minecraft.world.item;

import java.util.Iterator;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.ItemLike;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class EnchantedBookItem extends Item {
    public EnchantedBookItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
    
    @Override
    public boolean isEnchantable(final ItemStack bly) {
        return false;
    }
    
    public static ListTag getEnchantments(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        if (md2 != null) {
            return md2.getList("StoredEnchantments", 10);
        }
        return new ListTag();
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        super.appendHoverText(bly, bru, list, bni);
        ItemStack.appendEnchantmentNames(list, getEnchantments(bly));
    }
    
    public static void addEnchantment(final ItemStack bly, final EnchantmentInstance bps) {
        final ListTag mj3 = getEnchantments(bly);
        boolean boolean4 = true;
        final ResourceLocation vk5 = Registry.ENCHANTMENT.getKey(bps.enchantment);
        for (int integer6 = 0; integer6 < mj3.size(); ++integer6) {
            final CompoundTag md7 = mj3.getCompound(integer6);
            final ResourceLocation vk6 = ResourceLocation.tryParse(md7.getString("id"));
            if (vk6 != null && vk6.equals(vk5)) {
                if (md7.getInt("lvl") < bps.level) {
                    md7.putShort("lvl", (short)bps.level);
                }
                boolean4 = false;
                break;
            }
        }
        if (boolean4) {
            final CompoundTag md8 = new CompoundTag();
            md8.putString("id", String.valueOf(vk5));
            md8.putShort("lvl", (short)bps.level);
            mj3.add(md8);
        }
        bly.getOrCreateTag().put("StoredEnchantments", (Tag)mj3);
    }
    
    public static ItemStack createForEnchantment(final EnchantmentInstance bps) {
        final ItemStack bly2 = new ItemStack(Items.ENCHANTED_BOOK);
        addEnchantment(bly2, bps);
        return bly2;
    }
    
    @Override
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        if (bkp == CreativeModeTab.TAB_SEARCH) {
            for (final Enchantment bpp5 : Registry.ENCHANTMENT) {
                if (bpp5.category != null) {
                    for (int integer6 = bpp5.getMinLevel(); integer6 <= bpp5.getMaxLevel(); ++integer6) {
                        gj.add(createForEnchantment(new EnchantmentInstance(bpp5, integer6)));
                    }
                }
            }
        }
        else if (bkp.getEnchantmentCategories().length != 0) {
            for (final Enchantment bpp5 : Registry.ENCHANTMENT) {
                if (bkp.hasEnchantmentCategory(bpp5.category)) {
                    gj.add(createForEnchantment(new EnchantmentInstance(bpp5, bpp5.getMaxLevel())));
                }
            }
        }
    }
}
