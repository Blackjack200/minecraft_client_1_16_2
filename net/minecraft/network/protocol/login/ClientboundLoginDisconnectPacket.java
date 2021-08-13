package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundLoginDisconnectPacket implements Packet<ClientLoginPacketListener> {
    private Component reason;
    
    public ClientboundLoginDisconnectPacket() {
    }
    
    public ClientboundLoginDisconnectPacket(final Component nr) {
        this.reason = nr;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.reason = Component.Serializer.fromJsonLenient(nf.readUtf(262144));
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeComponent(this.reason);
    }
    
    public void handle(final ClientLoginPacketListener ty) {
        ty.handleDisconnect(this);
    }
    
    public Component getReason() {
        return this.reason;
    }
}
