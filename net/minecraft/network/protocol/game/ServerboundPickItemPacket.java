package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPickItemPacket implements Packet<ServerGamePacketListener> {
    private int slot;
    
    public ServerboundPickItemPacket() {
    }
    
    public ServerboundPickItemPacket(final int integer) {
        this.slot = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.slot = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.slot);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePickItem(this);
    }
    
    public int getSlot() {
        return this.slot;
    }
}
