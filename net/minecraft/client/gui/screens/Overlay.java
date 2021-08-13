package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.GuiComponent;

public abstract class Overlay extends GuiComponent implements Widget {
    public boolean isPauseScreen() {
        return true;
    }
}
