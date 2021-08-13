package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class OutOfMemoryScreen extends Screen {
    public OutOfMemoryScreen() {
        super(new TextComponent("Out of memory!"));
    }
    
    @Override
    protected void init() {
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 4 + 120 + 12, 150, 20, new TranslatableComponent("gui.toTitle"), dlg -> this.minecraft.setScreen(new TitleScreen())));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, this.height / 4 + 120 + 12, 150, 20, new TranslatableComponent("menu.quit"), dlg -> this.minecraft.stop()));
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, this.height / 4 - 60 + 20, 16777215);
        GuiComponent.drawString(dfj, this.font, "Minecraft has run out of memory.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
        GuiComponent.drawString(dfj, this.font, "This could be caused by a bug in the game or by the", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
        GuiComponent.drawString(dfj, this.font, "Java Virtual Machine not being allocated enough", this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
        GuiComponent.drawString(dfj, this.font, "memory.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
        GuiComponent.drawString(dfj, this.font, "To prevent level corruption, the current game has quit.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 54, 10526880);
        GuiComponent.drawString(dfj, this.font, "We've tried to free up enough memory to let you go back to", this.width / 2 - 140, this.height / 4 - 60 + 60 + 63, 10526880);
        GuiComponent.drawString(dfj, this.font, "the main menu and back to playing, but this may not have worked.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 72, 10526880);
        GuiComponent.drawString(dfj, this.font, "Please restart the game if you see this message again.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 81, 10526880);
        super.render(dfj, integer2, integer3, float4);
    }
}
