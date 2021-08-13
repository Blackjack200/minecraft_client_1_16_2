package net.minecraft.world.item.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.damagesource.DamageSource;
import com.google.common.collect.Maps;
import net.minecraft.world.item.ItemStack;
import java.util.Map;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Registry;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EquipmentSlot;

public abstract class Enchantment {
    private final EquipmentSlot[] slots;
    private final Rarity rarity;
    public final EnchantmentCategory category;
    @Nullable
    protected String descriptionId;
    
    @Nullable
    public static Enchantment byId(final int integer) {
        return Registry.ENCHANTMENT.byId(integer);
    }
    
    protected Enchantment(final Rarity a, final EnchantmentCategory bpq, final EquipmentSlot[] arr) {
        this.rarity = a;
        this.category = bpq;
        this.slots = arr;
    }
    
    public Map<EquipmentSlot, ItemStack> getSlotItems(final LivingEntity aqj) {
        final Map<EquipmentSlot, ItemStack> map3 = (Map<EquipmentSlot, ItemStack>)Maps.newEnumMap((Class)EquipmentSlot.class);
        for (final EquipmentSlot aqc7 : this.slots) {
            final ItemStack bly8 = aqj.getItemBySlot(aqc7);
            if (!bly8.isEmpty()) {
                map3.put(aqc7, bly8);
            }
        }
        return map3;
    }
    
    public Rarity getRarity() {
        return this.rarity;
    }
    
    public int getMinLevel() {
        return 1;
    }
    
    public int getMaxLevel() {
        return 1;
    }
    
    public int getMinCost(final int integer) {
        return 1 + integer * 10;
    }
    
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 5;
    }
    
    public int getDamageProtection(final int integer, final DamageSource aph) {
        return 0;
    }
    
    public float getDamageBonus(final int integer, final MobType aqn) {
        return 0.0f;
    }
    
    public final boolean isCompatibleWith(final Enchantment bpp) {
        return this.checkCompatibility(bpp) && bpp.checkCompatibility(this);
    }
    
    protected boolean checkCompatibility(final Enchantment bpp) {
        return this != bpp;
    }
    
    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("enchantment", Registry.ENCHANTMENT.getKey(this));
        }
        return this.descriptionId;
    }
    
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }
    
    public Component getFullname(final int integer) {
        final MutableComponent nx3 = new TranslatableComponent(this.getDescriptionId());
        if (this.isCurse()) {
            nx3.withStyle(ChatFormatting.RED);
        }
        else {
            nx3.withStyle(ChatFormatting.GRAY);
        }
        if (integer != 1 || this.getMaxLevel() != 1) {
            nx3.append(" ").append(new TranslatableComponent(new StringBuilder().append("enchantment.level.").append(integer).toString()));
        }
        return nx3;
    }
    
    public boolean canEnchant(final ItemStack bly) {
        return this.category.canEnchant(bly.getItem());
    }
    
    public void doPostAttack(final LivingEntity aqj, final Entity apx, final int integer) {
    }
    
    public void doPostHurt(final LivingEntity aqj, final Entity apx, final int integer) {
    }
    
    public boolean isTreasureOnly() {
        return false;
    }
    
    public boolean isCurse() {
        return false;
    }
    
    public boolean isTradeable() {
        return true;
    }
    
    public boolean isDiscoverable() {
        return true;
    }
    
    public enum Rarity {
        COMMON(10), 
        UNCOMMON(5), 
        RARE(2), 
        VERY_RARE(1);
        
        private final int weight;
        
        private Rarity(final int integer3) {
            this.weight = integer3;
        }
        
        public int getWeight() {
            return this.weight;
        }
    }
}
