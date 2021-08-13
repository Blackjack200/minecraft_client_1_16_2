package net.minecraft.world.item.enchantment;

import java.util.Collection;
import net.minecraft.Util;
import net.minecraft.util.WeighedRandom;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import java.util.Random;
import java.util.List;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.apache.commons.lang3.mutable.MutableFloat;
import net.minecraft.world.entity.MobType;
import org.apache.commons.lang3.mutable.MutableInt;
import net.minecraft.world.damagesource.DamageSource;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import com.google.common.collect.Maps;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Items;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

public class EnchantmentHelper {
    public static int getItemEnchantmentLevel(final Enchantment bpp, final ItemStack bly) {
        if (bly.isEmpty()) {
            return 0;
        }
        final ResourceLocation vk3 = Registry.ENCHANTMENT.getKey(bpp);
        final ListTag mj4 = bly.getEnchantmentTags();
        for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
            final CompoundTag md6 = mj4.getCompound(integer5);
            final ResourceLocation vk4 = ResourceLocation.tryParse(md6.getString("id"));
            if (vk4 != null && vk4.equals(vk3)) {
                return Mth.clamp(md6.getInt("lvl"), 0, 255);
            }
        }
        return 0;
    }
    
    public static Map<Enchantment, Integer> getEnchantments(final ItemStack bly) {
        final ListTag mj2 = (bly.getItem() == Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantments(bly) : bly.getEnchantmentTags();
        return deserializeEnchantments(mj2);
    }
    
    public static Map<Enchantment, Integer> deserializeEnchantments(final ListTag mj) {
        final Map<Enchantment, Integer> map2 = (Map<Enchantment, Integer>)Maps.newLinkedHashMap();
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(md4.getString("id"))).ifPresent(bpp -> {
                final Integer n = (Integer)map2.put(bpp, md4.getInt("lvl"));
            });
        }
        return map2;
    }
    
    public static void setEnchantments(final Map<Enchantment, Integer> map, final ItemStack bly) {
        final ListTag mj3 = new ListTag();
        for (final Map.Entry<Enchantment, Integer> entry5 : map.entrySet()) {
            final Enchantment bpp6 = (Enchantment)entry5.getKey();
            if (bpp6 == null) {
                continue;
            }
            final int integer7 = (int)entry5.getValue();
            final CompoundTag md8 = new CompoundTag();
            md8.putString("id", String.valueOf(Registry.ENCHANTMENT.getKey(bpp6)));
            md8.putShort("lvl", (short)integer7);
            mj3.add(md8);
            if (bly.getItem() != Items.ENCHANTED_BOOK) {
                continue;
            }
            EnchantedBookItem.addEnchantment(bly, new EnchantmentInstance(bpp6, integer7));
        }
        if (mj3.isEmpty()) {
            bly.removeTagKey("Enchantments");
        }
        else if (bly.getItem() != Items.ENCHANTED_BOOK) {
            bly.addTagElement("Enchantments", (Tag)mj3);
        }
    }
    
    private static void runIterationOnItem(final EnchantmentVisitor a, final ItemStack bly) {
        if (bly.isEmpty()) {
            return;
        }
        final ListTag mj3 = bly.getEnchantmentTags();
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            final String string5 = mj3.getCompound(integer4).getString("id");
            final int integer5 = mj3.getCompound(integer4).getInt("lvl");
            Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(string5)).ifPresent(bpp -> a.accept(bpp, integer5));
        }
    }
    
    private static void runIterationOnInventory(final EnchantmentVisitor a, final Iterable<ItemStack> iterable) {
        for (final ItemStack bly4 : iterable) {
            runIterationOnItem(a, bly4);
        }
    }
    
    public static int getDamageProtection(final Iterable<ItemStack> iterable, final DamageSource aph) {
        final MutableInt mutableInt3 = new MutableInt();
        runIterationOnInventory((bpp, integer) -> mutableInt3.add(bpp.getDamageProtection(integer, aph)), iterable);
        return mutableInt3.intValue();
    }
    
    public static float getDamageBonus(final ItemStack bly, final MobType aqn) {
        final MutableFloat mutableFloat3 = new MutableFloat();
        runIterationOnItem((bpp, integer) -> mutableFloat3.add(bpp.getDamageBonus(integer, aqn)), bly);
        return mutableFloat3.floatValue();
    }
    
    public static float getSweepingDamageRatio(final LivingEntity aqj) {
        final int integer2 = getEnchantmentLevel(Enchantments.SWEEPING_EDGE, aqj);
        if (integer2 > 0) {
            return SweepingEdgeEnchantment.getSweepingDamageRatio(integer2);
        }
        return 0.0f;
    }
    
    public static void doPostHurtEffects(final LivingEntity aqj, final Entity apx) {
        final EnchantmentVisitor a3 = (bpp, integer) -> bpp.doPostHurt(aqj, apx, integer);
        if (aqj != null) {
            runIterationOnInventory(a3, aqj.getAllSlots());
        }
        if (apx instanceof Player) {
            runIterationOnItem(a3, aqj.getMainHandItem());
        }
    }
    
    public static void doPostDamageEffects(final LivingEntity aqj, final Entity apx) {
        final EnchantmentVisitor a3 = (bpp, integer) -> bpp.doPostAttack(aqj, apx, integer);
        if (aqj != null) {
            runIterationOnInventory(a3, aqj.getAllSlots());
        }
        if (aqj instanceof Player) {
            runIterationOnItem(a3, aqj.getMainHandItem());
        }
    }
    
    public static int getEnchantmentLevel(final Enchantment bpp, final LivingEntity aqj) {
        final Iterable<ItemStack> iterable3 = (Iterable<ItemStack>)bpp.getSlotItems(aqj).values();
        if (iterable3 == null) {
            return 0;
        }
        int integer4 = 0;
        for (final ItemStack bly6 : iterable3) {
            final int integer5 = getItemEnchantmentLevel(bpp, bly6);
            if (integer5 > integer4) {
                integer4 = integer5;
            }
        }
        return integer4;
    }
    
    public static int getKnockbackBonus(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.KNOCKBACK, aqj);
    }
    
    public static int getFireAspect(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.FIRE_ASPECT, aqj);
    }
    
    public static int getRespiration(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.RESPIRATION, aqj);
    }
    
    public static int getDepthStrider(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.DEPTH_STRIDER, aqj);
    }
    
    public static int getBlockEfficiency(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, aqj);
    }
    
    public static int getFishingLuckBonus(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.FISHING_LUCK, bly);
    }
    
    public static int getFishingSpeedBonus(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.FISHING_SPEED, bly);
    }
    
    public static int getMobLooting(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.MOB_LOOTING, aqj);
    }
    
    public static boolean hasAquaAffinity(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.AQUA_AFFINITY, aqj) > 0;
    }
    
    public static boolean hasFrostWalker(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.FROST_WALKER, aqj) > 0;
    }
    
    public static boolean hasSoulSpeed(final LivingEntity aqj) {
        return getEnchantmentLevel(Enchantments.SOUL_SPEED, aqj) > 0;
    }
    
    public static boolean hasBindingCurse(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.BINDING_CURSE, bly) > 0;
    }
    
    public static boolean hasVanishingCurse(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.VANISHING_CURSE, bly) > 0;
    }
    
    public static int getLoyalty(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.LOYALTY, bly);
    }
    
    public static int getRiptide(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.RIPTIDE, bly);
    }
    
    public static boolean hasChanneling(final ItemStack bly) {
        return getItemEnchantmentLevel(Enchantments.CHANNELING, bly) > 0;
    }
    
    @Nullable
    public static Map.Entry<EquipmentSlot, ItemStack> getRandomItemWith(final Enchantment bpp, final LivingEntity aqj) {
        return getRandomItemWith(bpp, aqj, (Predicate<ItemStack>)(bly -> true));
    }
    
    @Nullable
    public static Map.Entry<EquipmentSlot, ItemStack> getRandomItemWith(final Enchantment bpp, final LivingEntity aqj, final Predicate<ItemStack> predicate) {
        final Map<EquipmentSlot, ItemStack> map4 = bpp.getSlotItems(aqj);
        if (map4.isEmpty()) {
            return null;
        }
        final List<Map.Entry<EquipmentSlot, ItemStack>> list5 = (List<Map.Entry<EquipmentSlot, ItemStack>>)Lists.newArrayList();
        for (final Map.Entry<EquipmentSlot, ItemStack> entry7 : map4.entrySet()) {
            final ItemStack bly8 = (ItemStack)entry7.getValue();
            if (!bly8.isEmpty() && getItemEnchantmentLevel(bpp, bly8) > 0 && predicate.test(bly8)) {
                list5.add(entry7);
            }
        }
        return (Map.Entry<EquipmentSlot, ItemStack>)(list5.isEmpty() ? null : ((Map.Entry)list5.get(aqj.getRandom().nextInt(list5.size()))));
    }
    
    public static int getEnchantmentCost(final Random random, final int integer2, int integer3, final ItemStack bly) {
        final Item blu5 = bly.getItem();
        final int integer4 = blu5.getEnchantmentValue();
        if (integer4 <= 0) {
            return 0;
        }
        if (integer3 > 15) {
            integer3 = 15;
        }
        final int integer5 = random.nextInt(8) + 1 + (integer3 >> 1) + random.nextInt(integer3 + 1);
        if (integer2 == 0) {
            return Math.max(integer5 / 3, 1);
        }
        if (integer2 == 1) {
            return integer5 * 2 / 3 + 1;
        }
        return Math.max(integer5, integer3 * 2);
    }
    
    public static ItemStack enchantItem(final Random random, ItemStack bly, final int integer, final boolean boolean4) {
        final List<EnchantmentInstance> list5 = selectEnchantment(random, bly, integer, boolean4);
        final boolean boolean5 = bly.getItem() == Items.BOOK;
        if (boolean5) {
            bly = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (final EnchantmentInstance bps8 : list5) {
            if (boolean5) {
                EnchantedBookItem.addEnchantment(bly, bps8);
            }
            else {
                bly.enchant(bps8.enchantment, bps8.level);
            }
        }
        return bly;
    }
    
    public static List<EnchantmentInstance> selectEnchantment(final Random random, final ItemStack bly, int integer, final boolean boolean4) {
        final List<EnchantmentInstance> list5 = (List<EnchantmentInstance>)Lists.newArrayList();
        final Item blu6 = bly.getItem();
        final int integer2 = blu6.getEnchantmentValue();
        if (integer2 <= 0) {
            return list5;
        }
        integer += 1 + random.nextInt(integer2 / 4 + 1) + random.nextInt(integer2 / 4 + 1);
        final float float8 = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
        integer = Mth.clamp(Math.round(integer + integer * float8), 1, Integer.MAX_VALUE);
        final List<EnchantmentInstance> list6 = getAvailableEnchantmentResults(integer, bly, boolean4);
        if (!list6.isEmpty()) {
            list5.add(WeighedRandom.<EnchantmentInstance>getRandomItem(random, list6));
            while (random.nextInt(50) <= integer) {
                filterCompatibleEnchantments(list6, Util.<EnchantmentInstance>lastOf(list5));
                if (list6.isEmpty()) {
                    break;
                }
                list5.add(WeighedRandom.<EnchantmentInstance>getRandomItem(random, list6));
                integer /= 2;
            }
        }
        return list5;
    }
    
    public static void filterCompatibleEnchantments(final List<EnchantmentInstance> list, final EnchantmentInstance bps) {
        final Iterator<EnchantmentInstance> iterator3 = (Iterator<EnchantmentInstance>)list.iterator();
        while (iterator3.hasNext()) {
            if (!bps.enchantment.isCompatibleWith(((EnchantmentInstance)iterator3.next()).enchantment)) {
                iterator3.remove();
            }
        }
    }
    
    public static boolean isEnchantmentCompatible(final Collection<Enchantment> collection, final Enchantment bpp) {
        for (final Enchantment bpp2 : collection) {
            if (!bpp2.isCompatibleWith(bpp)) {
                return false;
            }
        }
        return true;
    }
    
    public static List<EnchantmentInstance> getAvailableEnchantmentResults(final int integer, final ItemStack bly, final boolean boolean3) {
        final List<EnchantmentInstance> list4 = (List<EnchantmentInstance>)Lists.newArrayList();
        final Item blu5 = bly.getItem();
        final boolean boolean4 = bly.getItem() == Items.BOOK;
        for (final Enchantment bpp8 : Registry.ENCHANTMENT) {
            if (bpp8.isTreasureOnly() && !boolean3) {
                continue;
            }
            if (!bpp8.isDiscoverable()) {
                continue;
            }
            if (!bpp8.category.canEnchant(blu5) && !boolean4) {
                continue;
            }
            for (int integer2 = bpp8.getMaxLevel(); integer2 > bpp8.getMinLevel() - 1; --integer2) {
                if (integer >= bpp8.getMinCost(integer2) && integer <= bpp8.getMaxCost(integer2)) {
                    list4.add(new EnchantmentInstance(bpp8, integer2));
                    break;
                }
            }
        }
        return list4;
    }
    
    @FunctionalInterface
    interface EnchantmentVisitor {
        void accept(final Enchantment bpp, final int integer);
    }
}
