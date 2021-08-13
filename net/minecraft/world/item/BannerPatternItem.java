package net.minecraft.world.item;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;

public class BannerPatternItem extends Item {
    private final BannerPattern bannerPattern;
    
    public BannerPatternItem(final BannerPattern cby, final Properties a) {
        super(a);
        this.bannerPattern = cby;
    }
    
    public BannerPattern getBannerPattern() {
        return this.bannerPattern;
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        list.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }
    
    public MutableComponent getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId() + ".desc");
    }
}
