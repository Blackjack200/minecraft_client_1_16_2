package net.minecraft.client.gui.chat;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ChatType;
import net.minecraft.client.Minecraft;

public class StandardChatListener implements ChatListener {
    private final Minecraft minecraft;
    
    public StandardChatListener(final Minecraft djw) {
        this.minecraft = djw;
    }
    
    public void handle(final ChatType no, final Component nr, final UUID uUID) {
        if (this.minecraft.isBlocked(uUID)) {
            return;
        }
        if (no != ChatType.CHAT) {
            this.minecraft.gui.getChat().addMessage(nr);
        }
        else {
            this.minecraft.gui.getChat().enqueueMessage(nr);
        }
    }
}
