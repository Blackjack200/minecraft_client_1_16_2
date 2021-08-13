package net.minecraft.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;

public class ElytraItem extends Item implements Wearable {
    public ElytraItem(final Properties a) {
        super(a);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }
    
    public static boolean isFlyEnabled(final ItemStack bly) {
        return bly.getDamageValue() < bly.getMaxDamage() - 1;
    }
    
    @Override
    public boolean isValidRepairItem(final ItemStack bly1, final ItemStack bly2) {
        return bly2.getItem() == Items.PHANTOM_MEMBRANE;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final EquipmentSlot aqc6 = Mob.getEquipmentSlotForItem(bly5);
        final ItemStack bly6 = bft.getItemBySlot(aqc6);
        if (bly6.isEmpty()) {
            bft.setItemSlot(aqc6, bly5.copy());
            bly5.setCount(0);
            return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
        }
        return InteractionResultHolder.<ItemStack>fail(bly5);
    }
}
