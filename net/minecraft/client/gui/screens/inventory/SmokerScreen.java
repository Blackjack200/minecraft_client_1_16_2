package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.screens.recipebook.AbstractFurnaceRecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.SmokingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.SmokerMenu;

public class SmokerScreen extends AbstractFurnaceScreen<SmokerMenu> {
    private static final ResourceLocation TEXTURE;
    
    public SmokerScreen(final SmokerMenu bjq, final Inventory bfs, final Component nr) {
        super(bjq, new SmokingRecipeBookComponent(), bfs, nr, SmokerScreen.TEXTURE);
    }
    
    static {
        TEXTURE = new ResourceLocation("textures/gui/container/smoker.png");
    }
}
