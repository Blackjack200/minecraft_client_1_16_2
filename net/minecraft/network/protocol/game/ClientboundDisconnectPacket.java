package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundDisconnectPacket implements Packet<ClientGamePacketListener> {
    private Component reason;
    
    public ClientboundDisconnectPacket() {
    }
    
    public ClientboundDisconnectPacket(final Component nr) {
        this.reason = nr;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.reason = nf.readComponent();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeComponent(this.reason);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleDisconnect(this);
    }
    
    public Component getReason() {
        return this.reason;
    }
}
