package net.minecraft.client.gui.screens;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.network.chat.Component;

public class ReceivingLevelScreen extends Screen {
    private static final Component DOWNLOADING_TERRAIN_TEXT;
    
    public ReceivingLevelScreen() {
        super(NarratorChatListener.NO_TITLE);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderDirtBackground(0);
        GuiComponent.drawCenteredString(dfj, this.font, ReceivingLevelScreen.DOWNLOADING_TERRAIN_TEXT, this.width / 2, this.height / 2 - 50, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    static {
        DOWNLOADING_TERRAIN_TEXT = new TranslatableComponent("multiplayer.downloadingTerrain");
    }
}
