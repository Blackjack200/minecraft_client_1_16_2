package net.minecraft.world.item;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;

public class FireworkStarItem extends Item {
    public FireworkStarItem(final Properties a) {
        super(a);
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        final CompoundTag md6 = bly.getTagElement("Explosion");
        if (md6 != null) {
            appendHoverText(md6, list);
        }
    }
    
    public static void appendHoverText(final CompoundTag md, final List<Component> list) {
        final FireworkRocketItem.Shape a3 = FireworkRocketItem.Shape.byId(md.getByte("Type"));
        list.add(new TranslatableComponent("item.minecraft.firework_star.shape." + a3.getName()).withStyle(ChatFormatting.GRAY));
        final int[] arr4 = md.getIntArray("Colors");
        if (arr4.length > 0) {
            list.add(appendColors(new TextComponent("").withStyle(ChatFormatting.GRAY), arr4));
        }
        final int[] arr5 = md.getIntArray("FadeColors");
        if (arr5.length > 0) {
            list.add(appendColors(new TranslatableComponent("item.minecraft.firework_star.fade_to").append(" ").withStyle(ChatFormatting.GRAY), arr5));
        }
        if (md.getBoolean("Trail")) {
            list.add(new TranslatableComponent("item.minecraft.firework_star.trail").withStyle(ChatFormatting.GRAY));
        }
        if (md.getBoolean("Flicker")) {
            list.add(new TranslatableComponent("item.minecraft.firework_star.flicker").withStyle(ChatFormatting.GRAY));
        }
    }
    
    private static Component appendColors(final MutableComponent nx, final int[] arr) {
        for (int integer3 = 0; integer3 < arr.length; ++integer3) {
            if (integer3 > 0) {
                nx.append(", ");
            }
            nx.append(getColorName(arr[integer3]));
        }
        return nx;
    }
    
    private static Component getColorName(final int integer) {
        final DyeColor bku2 = DyeColor.byFireworkColor(integer);
        if (bku2 == null) {
            return new TranslatableComponent("item.minecraft.firework_star.custom_color");
        }
        return new TranslatableComponent("item.minecraft.firework_star." + bku2.getName());
    }
}
