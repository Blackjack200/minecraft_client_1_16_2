package net.minecraft.client.gui.spectator;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.spectator.categories.TeleportToTeamMenuCategory;
import net.minecraft.client.gui.spectator.categories.TeleportToPlayerMenuCategory;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.chat.Component;

public class RootSpectatorMenuCategory implements SpectatorMenuCategory {
    private static final Component PROMPT_TEXT;
    private final List<SpectatorMenuItem> items;
    
    public RootSpectatorMenuCategory() {
        (this.items = (List<SpectatorMenuItem>)Lists.newArrayList()).add(new TeleportToPlayerMenuCategory());
        this.items.add(new TeleportToTeamMenuCategory());
    }
    
    public List<SpectatorMenuItem> getItems() {
        return this.items;
    }
    
    public Component getPrompt() {
        return RootSpectatorMenuCategory.PROMPT_TEXT;
    }
    
    static {
        PROMPT_TEXT = new TranslatableComponent("spectatorMenu.root.prompt");
    }
}
