package net.minecraft.client.gui.screens;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;

public class InBedChatScreen extends ChatScreen {
    public InBedChatScreen() {
        super("");
    }
    
    @Override
    protected void init() {
        super.init();
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 40, 200, 20, new TranslatableComponent("multiplayer.stopSleeping"), dlg -> this.sendWakeUp()));
    }
    
    @Override
    public void onClose() {
        this.sendWakeUp();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.sendWakeUp();
        }
        else if (integer1 == 257 || integer1 == 335) {
            final String string5 = this.input.getValue().trim();
            if (!string5.isEmpty()) {
                this.sendMessage(string5);
            }
            this.input.setValue("");
            this.minecraft.gui.getChat().resetChatScroll();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private void sendWakeUp() {
        final ClientPacketListener dwm2 = this.minecraft.player.connection;
        dwm2.send(new ServerboundPlayerCommandPacket(this.minecraft.player, ServerboundPlayerCommandPacket.Action.STOP_SLEEPING));
    }
}
