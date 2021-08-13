package net.minecraft.world.item.alchemy;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.effect.MobEffectUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import java.util.Map;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.MutableComponent;

public class PotionUtils {
    private static final MutableComponent NO_EFFECT;
    
    public static List<MobEffectInstance> getMobEffects(final ItemStack bly) {
        return getAllEffects(bly.getTag());
    }
    
    public static List<MobEffectInstance> getAllEffects(final Potion bnq, final Collection<MobEffectInstance> collection) {
        final List<MobEffectInstance> list3 = (List<MobEffectInstance>)Lists.newArrayList();
        list3.addAll((Collection)bnq.getEffects());
        list3.addAll((Collection)collection);
        return list3;
    }
    
    public static List<MobEffectInstance> getAllEffects(@Nullable final CompoundTag md) {
        final List<MobEffectInstance> list2 = (List<MobEffectInstance>)Lists.newArrayList();
        list2.addAll((Collection)getPotion(md).getEffects());
        getCustomEffects(md, list2);
        return list2;
    }
    
    public static List<MobEffectInstance> getCustomEffects(final ItemStack bly) {
        return getCustomEffects(bly.getTag());
    }
    
    public static List<MobEffectInstance> getCustomEffects(@Nullable final CompoundTag md) {
        final List<MobEffectInstance> list2 = (List<MobEffectInstance>)Lists.newArrayList();
        getCustomEffects(md, list2);
        return list2;
    }
    
    public static void getCustomEffects(@Nullable final CompoundTag md, final List<MobEffectInstance> list) {
        if (md != null && md.contains("CustomPotionEffects", 9)) {
            final ListTag mj3 = md.getList("CustomPotionEffects", 10);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                final CompoundTag md2 = mj3.getCompound(integer4);
                final MobEffectInstance apr6 = MobEffectInstance.load(md2);
                if (apr6 != null) {
                    list.add(apr6);
                }
            }
        }
    }
    
    public static int getColor(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        if (md2 != null && md2.contains("CustomPotionColor", 99)) {
            return md2.getInt("CustomPotionColor");
        }
        return (getPotion(bly) == Potions.EMPTY) ? 16253176 : getColor((Collection<MobEffectInstance>)getMobEffects(bly));
    }
    
    public static int getColor(final Potion bnq) {
        return (bnq == Potions.EMPTY) ? 16253176 : getColor((Collection<MobEffectInstance>)bnq.getEffects());
    }
    
    public static int getColor(final Collection<MobEffectInstance> collection) {
        final int integer2 = 3694022;
        if (collection.isEmpty()) {
            return 3694022;
        }
        float float3 = 0.0f;
        float float4 = 0.0f;
        float float5 = 0.0f;
        int integer3 = 0;
        for (final MobEffectInstance apr8 : collection) {
            if (!apr8.isVisible()) {
                continue;
            }
            final int integer4 = apr8.getEffect().getColor();
            final int integer5 = apr8.getAmplifier() + 1;
            float3 += integer5 * (integer4 >> 16 & 0xFF) / 255.0f;
            float4 += integer5 * (integer4 >> 8 & 0xFF) / 255.0f;
            float5 += integer5 * (integer4 >> 0 & 0xFF) / 255.0f;
            integer3 += integer5;
        }
        if (integer3 == 0) {
            return 0;
        }
        float3 = float3 / integer3 * 255.0f;
        float4 = float4 / integer3 * 255.0f;
        float5 = float5 / integer3 * 255.0f;
        return (int)float3 << 16 | (int)float4 << 8 | (int)float5;
    }
    
    public static Potion getPotion(final ItemStack bly) {
        return getPotion(bly.getTag());
    }
    
    public static Potion getPotion(@Nullable final CompoundTag md) {
        if (md == null) {
            return Potions.EMPTY;
        }
        return Potion.byName(md.getString("Potion"));
    }
    
    public static ItemStack setPotion(final ItemStack bly, final Potion bnq) {
        final ResourceLocation vk3 = Registry.POTION.getKey(bnq);
        if (bnq == Potions.EMPTY) {
            bly.removeTagKey("Potion");
        }
        else {
            bly.getOrCreateTag().putString("Potion", vk3.toString());
        }
        return bly;
    }
    
    public static ItemStack setCustomEffects(final ItemStack bly, final Collection<MobEffectInstance> collection) {
        if (collection.isEmpty()) {
            return bly;
        }
        final CompoundTag md3 = bly.getOrCreateTag();
        final ListTag mj4 = md3.getList("CustomPotionEffects", 9);
        for (final MobEffectInstance apr6 : collection) {
            mj4.add(apr6.save(new CompoundTag()));
        }
        md3.put("CustomPotionEffects", (Tag)mj4);
        return bly;
    }
    
    public static void addPotionTooltip(final ItemStack bly, final List<Component> list, final float float3) {
        final List<MobEffectInstance> list2 = getMobEffects(bly);
        final List<Pair<Attribute, AttributeModifier>> list3 = (List<Pair<Attribute, AttributeModifier>>)Lists.newArrayList();
        if (list2.isEmpty()) {
            list.add(PotionUtils.NO_EFFECT);
        }
        else {
            for (final MobEffectInstance apr7 : list2) {
                MutableComponent nx8 = new TranslatableComponent(apr7.getDescriptionId());
                final MobEffect app9 = apr7.getEffect();
                final Map<Attribute, AttributeModifier> map10 = app9.getAttributeModifiers();
                if (!map10.isEmpty()) {
                    for (final Map.Entry<Attribute, AttributeModifier> entry12 : map10.entrySet()) {
                        final AttributeModifier arg13 = (AttributeModifier)entry12.getValue();
                        final AttributeModifier arg14 = new AttributeModifier(arg13.getName(), app9.getAttributeModifierValue(apr7.getAmplifier(), arg13), arg13.getOperation());
                        list3.add(new Pair(entry12.getKey(), (Object)arg14));
                    }
                }
                if (apr7.getAmplifier() > 0) {
                    nx8 = new TranslatableComponent("potion.withAmplifier", new Object[] { nx8, new TranslatableComponent(new StringBuilder().append("potion.potency.").append(apr7.getAmplifier()).toString()) });
                }
                if (apr7.getDuration() > 20) {
                    nx8 = new TranslatableComponent("potion.withDuration", new Object[] { nx8, MobEffectUtil.formatDuration(apr7, float3) });
                }
                list.add(nx8.withStyle(app9.getCategory().getTooltipFormatting()));
            }
        }
        if (!list3.isEmpty()) {
            list.add(TextComponent.EMPTY);
            list.add(new TranslatableComponent("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            for (final Pair<Attribute, AttributeModifier> pair7 : list3) {
                final AttributeModifier arg15 = (AttributeModifier)pair7.getSecond();
                final double double9 = arg15.getAmount();
                double double10;
                if (arg15.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE || arg15.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    double10 = arg15.getAmount() * 100.0;
                }
                else {
                    double10 = arg15.getAmount();
                }
                if (double9 > 0.0) {
                    list.add(new TranslatableComponent(new StringBuilder().append("attribute.modifier.plus.").append(arg15.getOperation().toValue()).toString(), new Object[] { ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(double10), new TranslatableComponent(((Attribute)pair7.getFirst()).getDescriptionId()) }).withStyle(ChatFormatting.BLUE));
                }
                else {
                    if (double9 >= 0.0) {
                        continue;
                    }
                    double10 *= -1.0;
                    list.add(new TranslatableComponent(new StringBuilder().append("attribute.modifier.take.").append(arg15.getOperation().toValue()).toString(), new Object[] { ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(double10), new TranslatableComponent(((Attribute)pair7.getFirst()).getDescriptionId()) }).withStyle(ChatFormatting.RED));
                }
            }
        }
    }
    
    static {
        NO_EFFECT = new TranslatableComponent("effect.none").withStyle(ChatFormatting.GRAY);
    }
}
