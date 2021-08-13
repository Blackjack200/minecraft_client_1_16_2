package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundAcceptTeleportationPacket implements Packet<ServerGamePacketListener> {
    private int id;
    
    public ServerboundAcceptTeleportationPacket() {
    }
    
    public ServerboundAcceptTeleportationPacket(final int integer) {
        this.id = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleAcceptTeleportPacket(this);
    }
    
    public int getId() {
        return this.id;
    }
}
