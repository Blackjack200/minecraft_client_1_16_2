package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSelectTradePacket implements Packet<ServerGamePacketListener> {
    private int item;
    
    public ServerboundSelectTradePacket() {
    }
    
    public ServerboundSelectTradePacket(final int integer) {
        this.item = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.item = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.item);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSelectTrade(this);
    }
    
    public int getItem() {
        return this.item;
    }
}
