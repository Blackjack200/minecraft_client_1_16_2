package net.minecraft.network.protocol.status;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundStatusRequestPacket implements Packet<ServerStatusPacketListener> {
    public void read(final FriendlyByteBuf nf) throws IOException {
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
    }
    
    public void handle(final ServerStatusPacketListener uo) {
        uo.handleStatusRequest(this);
    }
}
