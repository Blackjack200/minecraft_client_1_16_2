package net.minecraft.world.inventory;

import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface MenuConstructor {
    @Nullable
    AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft);
}
