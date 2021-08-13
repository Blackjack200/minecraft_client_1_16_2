package net.minecraft.client.gui.chat;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ChatType;
import net.minecraft.client.Minecraft;

public class OverlayChatListener implements ChatListener {
    private final Minecraft minecraft;
    
    public OverlayChatListener(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void handle(final ChatType no, final Component nr, final UUID uUID) {
        if (this.minecraft.isBlocked(uUID)) {
            return;
        }
        this.minecraft.gui.setOverlayMessage(nr, false);
    }
}
