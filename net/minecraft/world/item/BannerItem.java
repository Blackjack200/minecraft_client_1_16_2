package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.network.chat.Component;
import java.util.List;
import org.apache.commons.lang3.Validate;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Block;

public class BannerItem extends StandingAndWallBlockItem {
    public BannerItem(final Block bul1, final Block bul2, final Properties a) {
        super(bul1, bul2, a);
        Validate.isInstanceOf((Class)AbstractBannerBlock.class, bul1);
        Validate.isInstanceOf((Class)AbstractBannerBlock.class, bul2);
    }
    
    public static void appendHoverTextFromBannerBlockEntityTag(final ItemStack bly, final List<Component> list) {
        final CompoundTag md3 = bly.getTagElement("BlockEntityTag");
        if (md3 == null || !md3.contains("Patterns")) {
            return;
        }
        final ListTag mj4 = md3.getList("Patterns", 10);
        for (int integer5 = 0; integer5 < mj4.size() && integer5 < 6; ++integer5) {
            final CompoundTag md4 = mj4.getCompound(integer5);
            final DyeColor bku7 = DyeColor.byId(md4.getInt("Color"));
            final BannerPattern cby8 = BannerPattern.byHash(md4.getString("Pattern"));
            if (cby8 != null) {
                list.add(new TranslatableComponent("block.minecraft.banner." + cby8.getFilename() + '.' + bku7.getName()).withStyle(ChatFormatting.GRAY));
            }
        }
    }
    
    public DyeColor getColor() {
        return ((AbstractBannerBlock)this.getBlock()).getColor();
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        appendHoverTextFromBannerBlockEntityTag(bly, list);
    }
}
