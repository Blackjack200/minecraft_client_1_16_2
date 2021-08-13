package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundContainerAckPacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private short uid;
    private boolean accepted;
    
    public ClientboundContainerAckPacket() {
    }
    
    public ClientboundContainerAckPacket(final int integer, final short short2, final boolean boolean3) {
        this.containerId = integer;
        this.uid = short2;
        this.accepted = boolean3;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleContainerAck(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readUnsignedByte();
        this.uid = nf.readShort();
        this.accepted = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeShort(this.uid);
        nf.writeBoolean(this.accepted);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public short getUid() {
        return this.uid;
    }
    
    public boolean isAccepted() {
        return this.accepted;
    }
}
