package net.minecraft.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.MoverType;
import net.minecraft.util.Mth;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import java.util.function.Consumer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;

public class TridentItem extends Item implements Vanishable {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    
    public TridentItem(final Properties a) {
        super(a);
        final ImmutableMultimap.Builder<Attribute, AttributeModifier> builder3 = (ImmutableMultimap.Builder<Attribute, AttributeModifier>)ImmutableMultimap.builder();
        builder3.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(TridentItem.BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0, AttributeModifier.Operation.ADDITION));
        builder3.put(Attributes.ATTACK_SPEED, new AttributeModifier(TridentItem.BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.9000000953674316, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = (Multimap<Attribute, AttributeModifier>)builder3.build();
    }
    
    @Override
    public boolean canAttackBlock(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        return !bft.isCreative();
    }
    
    @Override
    public UseAnim getUseAnimation(final ItemStack bly) {
        return UseAnim.SPEAR;
    }
    
    @Override
    public int getUseDuration(final ItemStack bly) {
        return 72000;
    }
    
    @Override
    public void releaseUsing(final ItemStack bly, final Level bru, final LivingEntity aqj, final int integer) {
        if (!(aqj instanceof Player)) {
            return;
        }
        final Player bft6 = (Player)aqj;
        final int integer2 = this.getUseDuration(bly) - integer;
        if (integer2 < 10) {
            return;
        }
        final int integer3 = EnchantmentHelper.getRiptide(bly);
        if (integer3 > 0 && !bft6.isInWaterOrRain()) {
            return;
        }
        if (!bru.isClientSide) {
            bly.<Player>hurtAndBreak(1, bft6, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aqj.getUsedItemHand())));
            if (integer3 == 0) {
                final ThrownTrident bgv9 = new ThrownTrident(bru, bft6, bly);
                bgv9.shootFromRotation(bft6, bft6.xRot, bft6.yRot, 0.0f, 2.5f + integer3 * 0.5f, 1.0f);
                if (bft6.abilities.instabuild) {
                    bgv9.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }
                bru.addFreshEntity(bgv9);
                bru.playSound(null, bgv9, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0f, 1.0f);
                if (!bft6.abilities.instabuild) {
                    bft6.inventory.removeItem(bly);
                }
            }
        }
        bft6.awardStat(Stats.ITEM_USED.get(this));
        if (integer3 > 0) {
            final float float9 = bft6.yRot;
            final float float10 = bft6.xRot;
            float float11 = -Mth.sin(float9 * 0.017453292f) * Mth.cos(float10 * 0.017453292f);
            float float12 = -Mth.sin(float10 * 0.017453292f);
            float float13 = Mth.cos(float9 * 0.017453292f) * Mth.cos(float10 * 0.017453292f);
            final float float14 = Mth.sqrt(float11 * float11 + float12 * float12 + float13 * float13);
            final float float15 = 3.0f * ((1.0f + integer3) / 4.0f);
            float11 *= float15 / float14;
            float12 *= float15 / float14;
            float13 *= float15 / float14;
            bft6.push(float11, float12, float13);
            bft6.startAutoSpinAttack(20);
            if (bft6.isOnGround()) {
                final float float16 = 1.1999999f;
                bft6.move(MoverType.SELF, new Vec3(0.0, 1.1999999284744263, 0.0));
            }
            SoundEvent adn16;
            if (integer3 >= 3) {
                adn16 = SoundEvents.TRIDENT_RIPTIDE_3;
            }
            else if (integer3 == 2) {
                adn16 = SoundEvents.TRIDENT_RIPTIDE_2;
            }
            else {
                adn16 = SoundEvents.TRIDENT_RIPTIDE_1;
            }
            bru.playSound(null, bft6, adn16, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (bly5.getDamageValue() >= bly5.getMaxDamage() - 1) {
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        if (EnchantmentHelper.getRiptide(bly5) > 0 && !bft.isInWaterOrRain()) {
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        bft.startUsingItem(aoq);
        return InteractionResultHolder.<ItemStack>consume(bly5);
    }
    
    @Override
    public boolean hurtEnemy(final ItemStack bly, final LivingEntity aqj2, final LivingEntity aqj3) {
        bly.<LivingEntity>hurtAndBreak(1, aqj3, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        return true;
    }
    
    @Override
    public boolean mineBlock(final ItemStack bly, final Level bru, final BlockState cee, final BlockPos fx, final LivingEntity aqj) {
        if (cee.getDestroySpeed(bru, fx) != 0.0) {
            bly.<LivingEntity>hurtAndBreak(2, aqj, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        }
        return true;
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(final EquipmentSlot aqc) {
        if (aqc == EquipmentSlot.MAINHAND) {
            return this.defaultModifiers;
        }
        return super.getDefaultAttributeModifiers(aqc);
    }
    
    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
