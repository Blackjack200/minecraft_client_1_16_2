package net.minecraft.world.item;

import java.util.Collection;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import java.util.Random;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.projectile.Projectile;
import java.util.function.Consumer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.function.Predicate;

public class CrossbowItem extends ProjectileWeaponItem implements Vanishable {
    private boolean startSoundPlayed;
    private boolean midLoadSoundPlayed;
    
    public CrossbowItem(final Properties a) {
        super(a);
        this.startSoundPlayed = false;
        this.midLoadSoundPlayed = false;
    }
    
    @Override
    public Predicate<ItemStack> getSupportedHeldProjectiles() {
        return CrossbowItem.ARROW_OR_FIREWORK;
    }
    
    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return CrossbowItem.ARROW_ONLY;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (isCharged(bly5)) {
            performShooting(bru, bft, aoq, bly5, getShootingPower(bly5), 1.0f);
            setCharged(bly5, false);
            return InteractionResultHolder.<ItemStack>consume(bly5);
        }
        if (!bft.getProjectile(bly5).isEmpty()) {
            if (!isCharged(bly5)) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
                bft.startUsingItem(aoq);
            }
            return InteractionResultHolder.<ItemStack>consume(bly5);
        }
        return InteractionResultHolder.<ItemStack>fail(bly5);
    }
    
    @Override
    public void releaseUsing(final ItemStack bly, final Level bru, final LivingEntity aqj, final int integer) {
        final int integer2 = this.getUseDuration(bly) - integer;
        final float float7 = getPowerForTime(integer2, bly);
        if (float7 >= 1.0f && !isCharged(bly) && tryLoadProjectiles(aqj, bly)) {
            setCharged(bly, true);
            final SoundSource adp8 = (aqj instanceof Player) ? SoundSource.PLAYERS : SoundSource.HOSTILE;
            bru.playSound(null, aqj.getX(), aqj.getY(), aqj.getZ(), SoundEvents.CROSSBOW_LOADING_END, adp8, 1.0f, 1.0f / (CrossbowItem.random.nextFloat() * 0.5f + 1.0f) + 0.2f);
        }
    }
    
    private static boolean tryLoadProjectiles(final LivingEntity aqj, final ItemStack bly) {
        final int integer3 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, bly);
        final int integer4 = (integer3 == 0) ? 1 : 3;
        final boolean boolean5 = aqj instanceof Player && ((Player)aqj).abilities.instabuild;
        ItemStack bly2 = aqj.getProjectile(bly);
        ItemStack bly3 = bly2.copy();
        for (int integer5 = 0; integer5 < integer4; ++integer5) {
            if (integer5 > 0) {
                bly2 = bly3.copy();
            }
            if (bly2.isEmpty() && boolean5) {
                bly2 = new ItemStack(Items.ARROW);
                bly3 = bly2.copy();
            }
            if (!loadProjectile(aqj, bly, bly2, integer5 > 0, boolean5)) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean loadProjectile(final LivingEntity aqj, final ItemStack bly2, final ItemStack bly3, final boolean boolean4, final boolean boolean5) {
        if (bly3.isEmpty()) {
            return false;
        }
        final boolean boolean6 = boolean5 && bly3.getItem() instanceof ArrowItem;
        ItemStack bly4;
        if (!boolean6 && !boolean5 && !boolean4) {
            bly4 = bly3.split(1);
            if (bly3.isEmpty() && aqj instanceof Player) {
                ((Player)aqj).inventory.removeItem(bly3);
            }
        }
        else {
            bly4 = bly3.copy();
        }
        addChargedProjectile(bly2, bly4);
        return true;
    }
    
    public static boolean isCharged(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        return md2 != null && md2.getBoolean("Charged");
    }
    
    public static void setCharged(final ItemStack bly, final boolean boolean2) {
        final CompoundTag md3 = bly.getOrCreateTag();
        md3.putBoolean("Charged", boolean2);
    }
    
    private static void addChargedProjectile(final ItemStack bly1, final ItemStack bly2) {
        final CompoundTag md3 = bly1.getOrCreateTag();
        ListTag mj4;
        if (md3.contains("ChargedProjectiles", 9)) {
            mj4 = md3.getList("ChargedProjectiles", 10);
        }
        else {
            mj4 = new ListTag();
        }
        final CompoundTag md4 = new CompoundTag();
        bly2.save(md4);
        mj4.add(md4);
        md3.put("ChargedProjectiles", (Tag)mj4);
    }
    
    private static List<ItemStack> getChargedProjectiles(final ItemStack bly) {
        final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayList();
        final CompoundTag md3 = bly.getTag();
        if (md3 != null && md3.contains("ChargedProjectiles", 9)) {
            final ListTag mj4 = md3.getList("ChargedProjectiles", 10);
            if (mj4 != null) {
                for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
                    final CompoundTag md4 = mj4.getCompound(integer5);
                    list2.add(ItemStack.of(md4));
                }
            }
        }
        return list2;
    }
    
    private static void clearChargedProjectiles(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        if (md2 != null) {
            final ListTag mj3 = md2.getList("ChargedProjectiles", 9);
            mj3.clear();
            md2.put("ChargedProjectiles", (Tag)mj3);
        }
    }
    
    public static boolean containsChargedProjectile(final ItemStack bly, final Item blu) {
        return getChargedProjectiles(bly).stream().anyMatch(bly -> bly.getItem() == blu);
    }
    
    private static void shootProjectile(final Level bru, final LivingEntity aqj, final InteractionHand aoq, final ItemStack bly4, final ItemStack bly5, final float float6, final boolean boolean7, final float float8, final float float9, final float float10) {
        if (bru.isClientSide) {
            return;
        }
        final boolean boolean8 = bly5.getItem() == Items.FIREWORK_ROCKET;
        Projectile bgj12;
        if (boolean8) {
            bgj12 = new FireworkRocketEntity(bru, bly5, aqj, aqj.getX(), aqj.getEyeY() - 0.15000000596046448, aqj.getZ(), true);
        }
        else {
            bgj12 = getArrow(bru, aqj, bly4, bly5);
            if (boolean7 || float10 != 0.0f) {
                ((AbstractArrow)bgj12).pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
        }
        if (aqj instanceof CrossbowAttackMob) {
            final CrossbowAttackMob bda13 = (CrossbowAttackMob)aqj;
            bda13.shootCrossbowProjectile(bda13.getTarget(), bly4, bgj12, float10);
        }
        else {
            final Vec3 dck13 = aqj.getUpVector(1.0f);
            final Quaternion d14 = new Quaternion(new Vector3f(dck13), float10, true);
            final Vec3 dck14 = aqj.getViewVector(1.0f);
            final Vector3f g16 = new Vector3f(dck14);
            g16.transform(d14);
            bgj12.shoot(g16.x(), g16.y(), g16.z(), float8, float9);
        }
        bly4.<LivingEntity>hurtAndBreak(boolean8 ? 3 : 1, aqj, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(aoq)));
        bru.addFreshEntity(bgj12);
        bru.playSound(null, aqj.getX(), aqj.getY(), aqj.getZ(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0f, float6);
    }
    
    private static AbstractArrow getArrow(final Level bru, final LivingEntity aqj, final ItemStack bly3, final ItemStack bly4) {
        final ArrowItem bjz5 = (ArrowItem)((bly4.getItem() instanceof ArrowItem) ? bly4.getItem() : Items.ARROW);
        final AbstractArrow bfx6 = bjz5.createArrow(bru, bly4, aqj);
        if (aqj instanceof Player) {
            bfx6.setCritArrow(true);
        }
        bfx6.setSoundEvent(SoundEvents.CROSSBOW_HIT);
        bfx6.setShotFromCrossbow(true);
        final int integer7 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, bly3);
        if (integer7 > 0) {
            bfx6.setPierceLevel((byte)integer7);
        }
        return bfx6;
    }
    
    public static void performShooting(final Level bru, final LivingEntity aqj, final InteractionHand aoq, final ItemStack bly, final float float5, final float float6) {
        final List<ItemStack> list7 = getChargedProjectiles(bly);
        final float[] arr8 = getShotPitches(aqj.getRandom());
        for (int integer9 = 0; integer9 < list7.size(); ++integer9) {
            final ItemStack bly2 = (ItemStack)list7.get(integer9);
            final boolean boolean11 = aqj instanceof Player && ((Player)aqj).abilities.instabuild;
            if (!bly2.isEmpty()) {
                if (integer9 == 0) {
                    shootProjectile(bru, aqj, aoq, bly, bly2, arr8[integer9], boolean11, float5, float6, 0.0f);
                }
                else if (integer9 == 1) {
                    shootProjectile(bru, aqj, aoq, bly, bly2, arr8[integer9], boolean11, float5, float6, -10.0f);
                }
                else if (integer9 == 2) {
                    shootProjectile(bru, aqj, aoq, bly, bly2, arr8[integer9], boolean11, float5, float6, 10.0f);
                }
            }
        }
        onCrossbowShot(bru, aqj, bly);
    }
    
    private static float[] getShotPitches(final Random random) {
        final boolean boolean2 = random.nextBoolean();
        return new float[] { 1.0f, getRandomShotPitch(boolean2), getRandomShotPitch(!boolean2) };
    }
    
    private static float getRandomShotPitch(final boolean boolean1) {
        final float float2 = boolean1 ? 0.63f : 0.43f;
        return 1.0f / (CrossbowItem.random.nextFloat() * 0.5f + 1.8f) + float2;
    }
    
    private static void onCrossbowShot(final Level bru, final LivingEntity aqj, final ItemStack bly) {
        if (aqj instanceof ServerPlayer) {
            final ServerPlayer aah4 = (ServerPlayer)aqj;
            if (!bru.isClientSide) {
                CriteriaTriggers.SHOT_CROSSBOW.trigger(aah4, bly);
            }
            aah4.awardStat(Stats.ITEM_USED.get(bly.getItem()));
        }
        clearChargedProjectiles(bly);
    }
    
    @Override
    public void onUseTick(final Level bru, final LivingEntity aqj, final ItemStack bly, final int integer) {
        if (!bru.isClientSide) {
            final int integer2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, bly);
            final SoundEvent adn7 = this.getStartSound(integer2);
            final SoundEvent adn8 = (integer2 == 0) ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            final float float9 = (bly.getUseDuration() - integer) / (float)getChargeDuration(bly);
            if (float9 < 0.2f) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }
            if (float9 >= 0.2f && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                bru.playSound(null, aqj.getX(), aqj.getY(), aqj.getZ(), adn7, SoundSource.PLAYERS, 0.5f, 1.0f);
            }
            if (float9 >= 0.5f && adn8 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                bru.playSound(null, aqj.getX(), aqj.getY(), aqj.getZ(), adn8, SoundSource.PLAYERS, 0.5f, 1.0f);
            }
        }
    }
    
    @Override
    public int getUseDuration(final ItemStack bly) {
        return getChargeDuration(bly) + 3;
    }
    
    public static int getChargeDuration(final ItemStack bly) {
        final int integer2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, bly);
        return (integer2 == 0) ? 25 : (25 - 5 * integer2);
    }
    
    @Override
    public UseAnim getUseAnimation(final ItemStack bly) {
        return UseAnim.CROSSBOW;
    }
    
    private SoundEvent getStartSound(final int integer) {
        switch (integer) {
            case 1: {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_1;
            }
            case 2: {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
            }
            case 3: {
                return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
            }
            default: {
                return SoundEvents.CROSSBOW_LOADING_START;
            }
        }
    }
    
    private static float getPowerForTime(final int integer, final ItemStack bly) {
        float float3 = integer / (float)getChargeDuration(bly);
        if (float3 > 1.0f) {
            float3 = 1.0f;
        }
        return float3;
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        final List<ItemStack> list2 = getChargedProjectiles(bly);
        if (!isCharged(bly) || list2.isEmpty()) {
            return;
        }
        final ItemStack bly2 = (ItemStack)list2.get(0);
        list.add(new TranslatableComponent("item.minecraft.crossbow.projectile").append(" ").append(bly2.getDisplayName()));
        if (bni.isAdvanced() && bly2.getItem() == Items.FIREWORK_ROCKET) {
            final List<Component> list3 = (List<Component>)Lists.newArrayList();
            Items.FIREWORK_ROCKET.appendHoverText(bly2, bru, list3, bni);
            if (!list3.isEmpty()) {
                for (int integer9 = 0; integer9 < list3.size(); ++integer9) {
                    list3.set(integer9, new TextComponent("  ").append((Component)list3.get(integer9)).withStyle(ChatFormatting.GRAY));
                }
                list.addAll((Collection)list3);
            }
        }
    }
    
    private static float getShootingPower(final ItemStack bly) {
        if (bly.getItem() == Items.CROSSBOW && containsChargedProjectile(bly, Items.FIREWORK_ROCKET)) {
            return 1.6f;
        }
        return 3.15f;
    }
    
    @Override
    public int getDefaultProjectileRange() {
        return 8;
    }
}
