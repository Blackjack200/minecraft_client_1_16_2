package net.minecraft.realms;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.events.GuiEventListener;

public class RealmsLabel implements GuiEventListener {
    private final Component text;
    private final int x;
    private final int y;
    private final int color;
    
    public RealmsLabel(final Component nr, final int integer2, final int integer3, final int integer4) {
        this.text = nr;
        this.x = integer2;
        this.y = integer3;
        this.color = integer4;
    }
    
    public void render(final Screen doq, final PoseStack dfj) {
        GuiComponent.drawCenteredString(dfj, Minecraft.getInstance().font, this.text, this.x, this.y, this.color);
    }
    
    public String getText() {
        return this.text.getString();
    }
}
