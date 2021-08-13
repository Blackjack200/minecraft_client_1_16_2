package net.minecraft.world;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.network.chat.Component;

public final class SimpleMenuProvider implements MenuProvider {
    private final Component title;
    private final MenuConstructor menuConstructor;
    
    public SimpleMenuProvider(final MenuConstructor bja, final Component nr) {
        this.menuConstructor = bja;
        this.title = nr;
    }
    
    public Component getDisplayName() {
        return this.title;
    }
    
    public AbstractContainerMenu createMenu(final int integer, final Inventory bfs, final Player bft) {
        return this.menuConstructor.createMenu(integer, bfs, bft);
    }
}
