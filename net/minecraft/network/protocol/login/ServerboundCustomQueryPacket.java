package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundCustomQueryPacket implements Packet<ServerLoginPacketListener> {
    private int transactionId;
    private FriendlyByteBuf data;
    
    public ServerboundCustomQueryPacket() {
    }
    
    public ServerboundCustomQueryPacket(final int integer, @Nullable final FriendlyByteBuf nf) {
        this.transactionId = integer;
        this.data = nf;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.transactionId = nf.readVarInt();
        if (nf.readBoolean()) {
            final int integer3 = nf.readableBytes();
            if (integer3 < 0 || integer3 > 1048576) {
                throw new IOException("Payload may not be larger than 1048576 bytes");
            }
            this.data = new FriendlyByteBuf(nf.readBytes(integer3));
        }
        else {
            this.data = null;
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.transactionId);
        if (this.data != null) {
            nf.writeBoolean(true);
            nf.writeBytes(this.data.copy());
        }
        else {
            nf.writeBoolean(false);
        }
    }
    
    public void handle(final ServerLoginPacketListener ue) {
        ue.handleCustomQueryPacket(this);
    }
}
