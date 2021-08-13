package net.minecraft.world.item;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;

public class ShieldItem extends Item {
    public ShieldItem(final Properties a) {
        super(a);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }
    
    @Override
    public String getDescriptionId(final ItemStack bly) {
        if (bly.getTagElement("BlockEntityTag") != null) {
            return this.getDescriptionId() + '.' + getColor(bly).getName();
        }
        return super.getDescriptionId(bly);
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        BannerItem.appendHoverTextFromBannerBlockEntityTag(bly, list);
    }
    
    @Override
    public UseAnim getUseAnimation(final ItemStack bly) {
        return UseAnim.BLOCK;
    }
    
    @Override
    public int getUseDuration(final ItemStack bly) {
        return 72000;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        bft.startUsingItem(aoq);
        return InteractionResultHolder.<ItemStack>consume(bly5);
    }
    
    @Override
    public boolean isValidRepairItem(final ItemStack bly1, final ItemStack bly2) {
        return ItemTags.PLANKS.contains(bly2.getItem()) || super.isValidRepairItem(bly1, bly2);
    }
    
    public static DyeColor getColor(final ItemStack bly) {
        return DyeColor.byId(bly.getOrCreateTagElement("BlockEntityTag").getInt("Base"));
    }
}
