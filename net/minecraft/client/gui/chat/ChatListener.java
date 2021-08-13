package net.minecraft.client.gui.chat;

import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ChatType;

public interface ChatListener {
    void handle(final ChatType no, final Component nr, final UUID uUID);
}
