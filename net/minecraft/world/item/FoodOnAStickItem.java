package net.minecraft.world.item;

import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.ItemLike;
import java.util.function.Consumer;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class FoodOnAStickItem<T extends Entity> extends Item {
    private final EntityType<T> canInteractWith;
    private final int consumeItemDamage;
    
    public FoodOnAStickItem(final Properties a, final EntityType<T> aqb, final int integer) {
        super(a);
        this.canInteractWith = aqb;
        this.consumeItemDamage = integer;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        if (bru.isClientSide) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        final Entity apx6 = bft.getVehicle();
        if (bft.isPassenger() && apx6 instanceof ItemSteerable && apx6.getType() == this.canInteractWith) {
            final ItemSteerable aqh7 = (ItemSteerable)apx6;
            if (aqh7.boost()) {
                bly5.<Player>hurtAndBreak(this.consumeItemDamage, bft, (java.util.function.Consumer<Player>)(bft -> bft.broadcastBreakEvent(aoq)));
                if (bly5.isEmpty()) {
                    final ItemStack bly6 = new ItemStack(Items.FISHING_ROD);
                    bly6.setTag(bly5.getTag());
                    return InteractionResultHolder.<ItemStack>success(bly6);
                }
                return InteractionResultHolder.<ItemStack>success(bly5);
            }
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.<ItemStack>pass(bly5);
    }
}
