package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ClientboundCustomQueryPacket implements Packet<ClientLoginPacketListener> {
    private int transactionId;
    private ResourceLocation identifier;
    private FriendlyByteBuf data;
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.transactionId = nf.readVarInt();
        this.identifier = nf.readResourceLocation();
        final int integer3 = nf.readableBytes();
        if (integer3 < 0 || integer3 > 1048576) {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
        this.data = new FriendlyByteBuf(nf.readBytes(integer3));
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.transactionId);
        nf.writeResourceLocation(this.identifier);
        nf.writeBytes(this.data.copy());
    }
    
    public void handle(final ClientLoginPacketListener ty) {
        ty.handleCustomQuery(this);
    }
    
    public int getTransactionId() {
        return this.transactionId;
    }
}
