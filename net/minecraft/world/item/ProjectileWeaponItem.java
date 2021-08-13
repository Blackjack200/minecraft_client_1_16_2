package net.minecraft.world.item;

import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import java.util.function.Predicate;

public abstract class ProjectileWeaponItem extends Item {
    public static final Predicate<ItemStack> ARROW_ONLY;
    public static final Predicate<ItemStack> ARROW_OR_FIREWORK;
    
    public ProjectileWeaponItem(final Properties a) {
        super(a);
    }
    
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return this.getAllSupportedProjectiles();
    }
    
    public abstract Predicate<ItemStack> getAllSupportedProjectiles();
    
    public static ItemStack getHeldProjectile(final LivingEntity aqj, final Predicate<ItemStack> predicate) {
        if (predicate.test(aqj.getItemInHand(InteractionHand.OFF_HAND))) {
            return aqj.getItemInHand(InteractionHand.OFF_HAND);
        }
        if (predicate.test(aqj.getItemInHand(InteractionHand.MAIN_HAND))) {
            return aqj.getItemInHand(InteractionHand.MAIN_HAND);
        }
        return ItemStack.EMPTY;
    }
    
    @Override
    public int getEnchantmentValue() {
        return 1;
    }
    
    public abstract int getDefaultProjectileRange();
    
    static {
        ARROW_ONLY = (bly -> bly.getItem().is(ItemTags.ARROWS));
        ARROW_OR_FIREWORK = ProjectileWeaponItem.ARROW_ONLY.or(bly -> bly.getItem() == Items.FIREWORK_ROCKET);
    }
}
