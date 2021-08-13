package net.minecraft.world.item.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;

public class ProtectionEnchantment extends Enchantment {
    public final Type type;
    
    public ProtectionEnchantment(final Rarity a, final Type a, final EquipmentSlot... arr) {
        super(a, (a == Type.FALL) ? EnchantmentCategory.ARMOR_FEET : EnchantmentCategory.ARMOR, arr);
        this.type = a;
    }
    
    @Override
    public int getMinCost(final int integer) {
        return this.type.getMinCost() + (integer - 1) * this.type.getLevelCost();
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + this.type.getLevelCost();
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public int getDamageProtection(final int integer, final DamageSource aph) {
        if (aph.isBypassInvul()) {
            return 0;
        }
        if (this.type == Type.ALL) {
            return integer;
        }
        if (this.type == Type.FIRE && aph.isFire()) {
            return integer * 2;
        }
        if (this.type == Type.FALL && aph == DamageSource.FALL) {
            return integer * 3;
        }
        if (this.type == Type.EXPLOSION && aph.isExplosion()) {
            return integer * 2;
        }
        if (this.type == Type.PROJECTILE && aph.isProjectile()) {
            return integer * 2;
        }
        return 0;
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        if (bpp instanceof ProtectionEnchantment) {
            final ProtectionEnchantment bqc3 = (ProtectionEnchantment)bpp;
            return this.type != bqc3.type && (this.type == Type.FALL || bqc3.type == Type.FALL);
        }
        return super.checkCompatibility(bpp);
    }
    
    public static int getFireAfterDampener(final LivingEntity aqj, int integer) {
        final int integer2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, aqj);
        if (integer2 > 0) {
            integer -= Mth.floor(integer * (integer2 * 0.15f));
        }
        return integer;
    }
    
    public static double getExplosionKnockbackAfterDampener(final LivingEntity aqj, double double2) {
        final int integer4 = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, aqj);
        if (integer4 > 0) {
            double2 -= Mth.floor(double2 * (integer4 * 0.15f));
        }
        return double2;
    }
    
    public enum Type {
        ALL("all", 1, 11), 
        FIRE("fire", 10, 8), 
        FALL("fall", 5, 6), 
        EXPLOSION("explosion", 5, 8), 
        PROJECTILE("projectile", 3, 6);
        
        private final String name;
        private final int minCost;
        private final int levelCost;
        
        private Type(final String string3, final int integer4, final int integer5) {
            this.name = string3;
            this.minCost = integer4;
            this.levelCost = integer5;
        }
        
        public int getMinCost() {
            return this.minCost;
        }
        
        public int getLevelCost() {
            return this.levelCost;
        }
    }
}
