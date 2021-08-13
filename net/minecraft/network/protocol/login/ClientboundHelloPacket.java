package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.util.Crypt;
import net.minecraft.network.FriendlyByteBuf;
import java.security.PublicKey;
import net.minecraft.network.protocol.Packet;

public class ClientboundHelloPacket implements Packet<ClientLoginPacketListener> {
    private String serverId;
    private PublicKey publicKey;
    private byte[] nonce;
    
    public ClientboundHelloPacket() {
    }
    
    public ClientboundHelloPacket(final String string, final PublicKey publicKey, final byte[] arr) {
        this.serverId = string;
        this.publicKey = publicKey;
        this.nonce = arr;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.serverId = nf.readUtf(20);
        this.publicKey = Crypt.byteToPublicKey(nf.readByteArray());
        this.nonce = nf.readByteArray();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.serverId);
        nf.writeByteArray(this.publicKey.getEncoded());
        nf.writeByteArray(this.nonce);
    }
    
    public void handle(final ClientLoginPacketListener ty) {
        ty.handleHello(this);
    }
    
    public String getServerId() {
        return this.serverId;
    }
    
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    
    public byte[] getNonce() {
        return this.nonce;
    }
}
