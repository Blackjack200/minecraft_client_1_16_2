package net.minecraft.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.SmithingMenu;

public class SmithingScreen extends ItemCombinerScreen<SmithingMenu> {
    private static final ResourceLocation SMITHING_LOCATION;
    
    public SmithingScreen(final SmithingMenu bjp, final Inventory bfs, final Component nr) {
        super(bjp, bfs, nr, SmithingScreen.SMITHING_LOCATION);
        this.titleLabelX = 60;
        this.titleLabelY = 18;
    }
    
    @Override
    protected void renderLabels(final PoseStack dfj, final int integer2, final int integer3) {
        RenderSystem.disableBlend();
        super.renderLabels(dfj, integer2, integer3);
    }
    
    static {
        SMITHING_LOCATION = new ResourceLocation("textures/gui/container/smithing.png");
    }
}
