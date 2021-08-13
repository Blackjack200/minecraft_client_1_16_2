package net.minecraft.network.protocol.login;

import net.minecraft.network.PacketListener;
import java.security.PrivateKey;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.security.Key;
import net.minecraft.util.Crypt;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.network.protocol.Packet;

public class ServerboundKeyPacket implements Packet<ServerLoginPacketListener> {
    private byte[] keybytes;
    private byte[] nonce;
    
    public ServerboundKeyPacket() {
        this.keybytes = new byte[0];
        this.nonce = new byte[0];
    }
    
    public ServerboundKeyPacket(final SecretKey secretKey, final PublicKey publicKey, final byte[] arr) {
        this.keybytes = new byte[0];
        this.nonce = new byte[0];
        this.keybytes = Crypt.encryptUsingKey((Key)publicKey, secretKey.getEncoded());
        this.nonce = Crypt.encryptUsingKey((Key)publicKey, arr);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.keybytes = nf.readByteArray();
        this.nonce = nf.readByteArray();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByteArray(this.keybytes);
        nf.writeByteArray(this.nonce);
    }
    
    public void handle(final ServerLoginPacketListener ue) {
        ue.handleKey(this);
    }
    
    public SecretKey getSecretKey(final PrivateKey privateKey) {
        return Crypt.decryptByteToSecretKey(privateKey, this.keybytes);
    }
    
    public byte[] getNonce(final PrivateKey privateKey) {
        if (privateKey == null) {
            return this.nonce;
        }
        return Crypt.decryptUsingKey((Key)privateKey, this.nonce);
    }
}
