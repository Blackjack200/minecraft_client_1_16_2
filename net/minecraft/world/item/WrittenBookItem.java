package net.minecraft.world.item;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;

public class WrittenBookItem extends Item {
    public WrittenBookItem(final Properties a) {
        super(a);
    }
    
    public static boolean makeSureTagIsValid(@Nullable final CompoundTag md) {
        if (!WritableBookItem.makeSureTagIsValid(md)) {
            return false;
        }
        if (!md.contains("title", 8)) {
            return false;
        }
        final String string2 = md.getString("title");
        return string2.length() <= 32 && md.contains("author", 8);
    }
    
    public static int getGeneration(final ItemStack bly) {
        return bly.getTag().getInt("generation");
    }
    
    public static int getPageCount(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        return (md2 != null) ? md2.getList("pages", 8).size() : 0;
    }
    
    @Override
    public Component getName(final ItemStack bly) {
        if (bly.hasTag()) {
            final CompoundTag md3 = bly.getTag();
            final String string4 = md3.getString("title");
            if (!StringUtil.isNullOrEmpty(string4)) {
                return new TextComponent(string4);
            }
        }
        return super.getName(bly);
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        if (bly.hasTag()) {
            final CompoundTag md6 = bly.getTag();
            final String string7 = md6.getString("author");
            if (!StringUtil.isNullOrEmpty(string7)) {
                list.add(new TranslatableComponent("book.byAuthor", new Object[] { string7 }).withStyle(ChatFormatting.GRAY));
            }
            list.add(new TranslatableComponent(new StringBuilder().append("book.generation.").append(md6.getInt("generation")).toString()).withStyle(ChatFormatting.GRAY));
        }
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (cee5.is(Blocks.LECTERN)) {
            return LecternBlock.tryPlaceBook(bru3, fx4, cee5, bnx.getItemInHand()) ? InteractionResult.sidedSuccess(bru3.isClientSide) : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        bft.openItemGui(bly5, aoq);
        bft.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
    
    public static boolean resolveBookComponents(final ItemStack bly, @Nullable final CommandSourceStack db, @Nullable final Player bft) {
        final CompoundTag md4 = bly.getTag();
        if (md4 == null || md4.getBoolean("resolved")) {
            return false;
        }
        md4.putBoolean("resolved", true);
        if (!makeSureTagIsValid(md4)) {
            return false;
        }
        final ListTag mj5 = md4.getList("pages", 8);
        for (int integer6 = 0; integer6 < mj5.size(); ++integer6) {
            final String string7 = mj5.getString(integer6);
            Component nr8;
            try {
                nr8 = Component.Serializer.fromJsonLenient(string7);
                nr8 = ComponentUtils.updateForEntity(db, nr8, bft, 0);
            }
            catch (Exception exception9) {
                nr8 = new TextComponent(string7);
            }
            mj5.set(integer6, StringTag.valueOf(Component.Serializer.toJson(nr8)));
        }
        md4.put("pages", (Tag)mj5);
        return true;
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
}
