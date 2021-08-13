package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundAnimatePacket implements Packet<ClientGamePacketListener> {
    private int id;
    private int action;
    
    public ClientboundAnimatePacket() {
    }
    
    public ClientboundAnimatePacket(final Entity apx, final int integer) {
        this.id = apx.getId();
        this.action = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.action = nf.readUnsignedByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeByte(this.action);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAnimate(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getAction() {
        return this.action;
    }
}
