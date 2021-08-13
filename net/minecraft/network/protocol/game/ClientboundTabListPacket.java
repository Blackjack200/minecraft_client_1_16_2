package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundTabListPacket implements Packet<ClientGamePacketListener> {
    private Component header;
    private Component footer;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.header = nf.readComponent();
        this.footer = nf.readComponent();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeComponent(this.header);
        nf.writeComponent(this.footer);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleTabListCustomisation(this);
    }
    
    public Component getHeader() {
        return this.header;
    }
    
    public Component getFooter() {
        return this.footer;
    }
}
