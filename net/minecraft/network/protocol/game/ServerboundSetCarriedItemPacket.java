package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetCarriedItemPacket implements Packet<ServerGamePacketListener> {
    private int slot;
    
    public ServerboundSetCarriedItemPacket() {
    }
    
    public ServerboundSetCarriedItemPacket(final int integer) {
        this.slot = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.slot = nf.readShort();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeShort(this.slot);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSetCarriedItem(this);
    }
    
    public int getSlot() {
        return this.slot;
    }
}
