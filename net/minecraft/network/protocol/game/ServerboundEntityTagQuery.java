package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundEntityTagQuery implements Packet<ServerGamePacketListener> {
    private int transactionId;
    private int entityId;
    
    public ServerboundEntityTagQuery() {
    }
    
    public ServerboundEntityTagQuery(final int integer1, final int integer2) {
        this.transactionId = integer1;
        this.entityId = integer2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.transactionId = nf.readVarInt();
        this.entityId = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.transactionId);
        nf.writeVarInt(this.entityId);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleEntityTagQuery(this);
    }
    
    public int getTransactionId() {
        return this.transactionId;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
}
