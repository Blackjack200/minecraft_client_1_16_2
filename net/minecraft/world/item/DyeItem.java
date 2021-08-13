package net.minecraft.world.item;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import java.util.Map;

public class DyeItem extends Item {
    private static final Map<DyeColor, DyeItem> ITEM_BY_COLOR;
    private final DyeColor dyeColor;
    
    public DyeItem(final DyeColor bku, final Properties a) {
        super(a);
        this.dyeColor = bku;
        DyeItem.ITEM_BY_COLOR.put(bku, this);
    }
    
    @Override
    public InteractionResult interactLivingEntity(final ItemStack bly, final Player bft, final LivingEntity aqj, final InteractionHand aoq) {
        if (aqj instanceof Sheep) {
            final Sheep bap6 = (Sheep)aqj;
            if (bap6.isAlive() && !bap6.isSheared() && bap6.getColor() != this.dyeColor) {
                if (!bft.level.isClientSide) {
                    bap6.setColor(this.dyeColor);
                    bly.shrink(1);
                }
                return InteractionResult.sidedSuccess(bft.level.isClientSide);
            }
        }
        return InteractionResult.PASS;
    }
    
    public DyeColor getDyeColor() {
        return this.dyeColor;
    }
    
    public static DyeItem byColor(final DyeColor bku) {
        return (DyeItem)DyeItem.ITEM_BY_COLOR.get(bku);
    }
    
    static {
        ITEM_BY_COLOR = (Map)Maps.newEnumMap((Class)DyeColor.class);
    }
}
