package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ErrorScreen extends Screen {
    private final Component message;
    
    public ErrorScreen(final Component nr1, final Component nr2) {
        super(nr1);
        this.message = nr2;
    }
    
    @Override
    protected void init() {
        super.init();
        this.<Button>addButton(new Button(this.width / 2 - 100, 140, 200, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(null)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.fillGradient(dfj, 0, 0, this.width, this.height, -12574688, -11530224);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 90, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, this.message, this.width / 2, 110, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
