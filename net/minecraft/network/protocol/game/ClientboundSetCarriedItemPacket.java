package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetCarriedItemPacket implements Packet<ClientGamePacketListener> {
    private int slot;
    
    public ClientboundSetCarriedItemPacket() {
    }
    
    public ClientboundSetCarriedItemPacket(final int integer) {
        this.slot = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.slot = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.slot);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetCarriedItem(this);
    }
    
    public int getSlot() {
        return this.slot;
    }
}
