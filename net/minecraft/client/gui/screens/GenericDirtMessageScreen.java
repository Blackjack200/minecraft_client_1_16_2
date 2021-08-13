package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

public class GenericDirtMessageScreen extends Screen {
    public GenericDirtMessageScreen(final Component nr) {
        super(nr);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderDirtBackground(0);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 70, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
}
