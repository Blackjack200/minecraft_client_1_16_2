package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.BlastingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.BlastFurnaceMenu;

public class BlastFurnaceScreen extends AbstractFurnaceScreen<BlastFurnaceMenu> {
    private static final ResourceLocation TEXTURE;
    
    public BlastFurnaceScreen(final BlastFurnaceMenu bid, final Inventory bfs, final Component nr) {
        super(bid, new BlastingRecipeBookComponent(), bfs, nr, BlastFurnaceScreen.TEXTURE);
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/gui/container/blast_furnace.png");
    }
}
