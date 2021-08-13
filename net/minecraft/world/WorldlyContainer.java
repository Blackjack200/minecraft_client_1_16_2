package net.minecraft.world;

import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;

public interface WorldlyContainer extends Container {
    int[] getSlotsForFace(final Direction gc);
    
    boolean canPlaceItemThroughFace(final int integer, final ItemStack bly, @Nullable final Direction gc);
    
    boolean canTakeItemThroughFace(final int integer, final ItemStack bly, final Direction gc);
}
