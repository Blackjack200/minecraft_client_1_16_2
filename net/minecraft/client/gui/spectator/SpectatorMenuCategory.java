package net.minecraft.client.gui.spectator;

import net.minecraft.network.chat.Component;
import java.util.List;

public interface SpectatorMenuCategory {
    List<SpectatorMenuItem> getItems();
    
    Component getPrompt();
}
