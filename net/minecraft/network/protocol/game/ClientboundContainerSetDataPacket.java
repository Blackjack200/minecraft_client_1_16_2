package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundContainerSetDataPacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private int id;
    private int value;
    
    public ClientboundContainerSetDataPacket() {
    }
    
    public ClientboundContainerSetDataPacket(final int integer1, final int integer2, final int integer3) {
        this.containerId = integer1;
        this.id = integer2;
        this.value = integer3;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleContainerSetData(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readUnsignedByte();
        this.id = nf.readShort();
        this.value = nf.readShort();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeShort(this.id);
        nf.writeShort(this.value);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getValue() {
        return this.value;
    }
}
