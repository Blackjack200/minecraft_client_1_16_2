package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundResourcePackPacket implements Packet<ClientGamePacketListener> {
    private String url;
    private String hash;
    
    public ClientboundResourcePackPacket() {
    }
    
    public ClientboundResourcePackPacket(final String string1, final String string2) {
        this.url = string1;
        this.hash = string2;
        if (string2.length() > 40) {
            throw new IllegalArgumentException(new StringBuilder().append("Hash is too long (max 40, was ").append(string2.length()).append(")").toString());
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.url = nf.readUtf(32767);
        this.hash = nf.readUtf(40);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.url);
        nf.writeUtf(this.hash);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleResourcePack(this);
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public String getHash() {
        return this.hash;
    }
}
