package net.minecraft.world.item;

import java.util.function.Predicate;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.AbstractArrow;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class BowItem extends ProjectileWeaponItem implements Vanishable {
    public BowItem(final Properties a) {
        super(a);
    }
    
    @Override
    public void releaseUsing(final ItemStack bly, final Level bru, final LivingEntity aqj, final int integer) {
        if (!(aqj instanceof Player)) {
            return;
        }
        final Player bft6 = (Player)aqj;
        final boolean boolean7 = bft6.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bly) > 0;
        ItemStack bly2 = bft6.getProjectile(bly);
        if (bly2.isEmpty() && !boolean7) {
            return;
        }
        if (bly2.isEmpty()) {
            bly2 = new ItemStack(Items.ARROW);
        }
        final int integer2 = this.getUseDuration(bly) - integer;
        final float float10 = getPowerForTime(integer2);
        if (float10 < 0.1) {
            return;
        }
        final boolean boolean8 = boolean7 && bly2.getItem() == Items.ARROW;
        if (!bru.isClientSide) {
            final ArrowItem bjz12 = (ArrowItem)((bly2.getItem() instanceof ArrowItem) ? bly2.getItem() : Items.ARROW);
            final AbstractArrow bfx13 = bjz12.createArrow(bru, bly2, bft6);
            bfx13.shootFromRotation(bft6, bft6.xRot, bft6.yRot, 0.0f, float10 * 3.0f, 1.0f);
            if (float10 == 1.0f) {
                bfx13.setCritArrow(true);
            }
            final int integer3 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bly);
            if (integer3 > 0) {
                bfx13.setBaseDamage(bfx13.getBaseDamage() + integer3 * 0.5 + 0.5);
            }
            final int integer4 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bly);
            if (integer4 > 0) {
                bfx13.setKnockback(integer4);
            }
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bly) > 0) {
                bfx13.setSecondsOnFire(100);
            }
            bly.<Player>hurtAndBreak(1, bft6, (java.util.function.Consumer<Player>)(bft2 -> bft2.broadcastBreakEvent(bft6.getUsedItemHand())));
            if (boolean8 || (bft6.abilities.instabuild && (bly2.getItem() == Items.SPECTRAL_ARROW || bly2.getItem() == Items.TIPPED_ARROW))) {
                bfx13.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            bru.addFreshEntity(bfx13);
        }
        bru.playSound(null, bft6.getX(), bft6.getY(), bft6.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (BowItem.random.nextFloat() * 0.4f + 1.2f) + float10 * 0.5f);
        if (!boolean8 && !bft6.abilities.instabuild) {
            bly2.shrink(1);
            if (bly2.isEmpty()) {
                bft6.inventory.removeItem(bly2);
            }
        }
        bft6.awardStat(Stats.ITEM_USED.get(this));
    }
    
    public static float getPowerForTime(final int integer) {
        float float2 = integer / 20.0f;
        float2 = (float2 * float2 + float2 * 2.0f) / 3.0f;
        if (float2 > 1.0f) {
            float2 = 1.0f;
        }
        return float2;
    }
    
    @Override
    public int getUseDuration(final ItemStack bly) {
        return 72000;
    }
    
    @Override
    public UseAnim getUseAnimation(final ItemStack bly) {
        return UseAnim.BOW;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final boolean boolean6 = !bft.getProjectile(bly5).isEmpty();
        if (bft.abilities.instabuild || boolean6) {
            bft.startUsingItem(aoq);
            return InteractionResultHolder.<ItemStack>consume(bly5);
        }
        return InteractionResultHolder.<ItemStack>fail(bly5);
    }
    
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return BowItem.ARROW_ONLY;
    }
    
    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }
}
