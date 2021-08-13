package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundForgetLevelChunkPacket implements Packet<ClientGamePacketListener> {
    private int x;
    private int z;
    
    public ClientboundForgetLevelChunkPacket() {
    }
    
    public ClientboundForgetLevelChunkPacket(final int integer1, final int integer2) {
        this.x = integer1;
        this.z = integer2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readInt();
        this.z = nf.readInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(this.x);
        nf.writeInt(this.z);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleForgetLevelChunk(this);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getZ() {
        return this.z;
    }
}
