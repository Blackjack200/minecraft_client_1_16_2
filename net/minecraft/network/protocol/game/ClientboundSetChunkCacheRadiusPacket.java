package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetChunkCacheRadiusPacket implements Packet<ClientGamePacketListener> {
    private int radius;
    
    public ClientboundSetChunkCacheRadiusPacket() {
    }
    
    public ClientboundSetChunkCacheRadiusPacket(final int integer) {
        this.radius = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.radius = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.radius);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetChunkCacheRadius(this);
    }
    
    public int getRadius() {
        return this.radius;
    }
}
