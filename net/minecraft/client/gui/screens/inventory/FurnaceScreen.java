package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.FurnaceMenu;

public class FurnaceScreen extends AbstractFurnaceScreen<FurnaceMenu> {
    private static final ResourceLocation TEXTURE;
    
    public FurnaceScreen(final FurnaceMenu bir, final Inventory bfs, final Component nr) {
        super(bir, new SmeltingRecipeBookComponent(), bfs, nr, FurnaceScreen.TEXTURE);
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
    }
}
