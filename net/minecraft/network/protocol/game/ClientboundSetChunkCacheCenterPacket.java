package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetChunkCacheCenterPacket implements Packet<ClientGamePacketListener> {
    private int x;
    private int z;
    
    public ClientboundSetChunkCacheCenterPacket() {
    }
    
    public ClientboundSetChunkCacheCenterPacket(final int integer1, final int integer2) {
        this.x = integer1;
        this.z = integer2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readVarInt();
        this.z = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.x);
        nf.writeVarInt(this.z);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetChunkCacheCenter(this);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
}
