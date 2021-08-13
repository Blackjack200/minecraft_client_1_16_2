package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundEntityEventPacket implements Packet<ClientGamePacketListener> {
    private int entityId;
    private byte eventId;
    
    public ClientboundEntityEventPacket() {
    }
    
    public ClientboundEntityEventPacket(final Entity apx, final byte byte2) {
        this.entityId = apx.getId();
        this.eventId = byte2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readInt();
        this.eventId = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(this.entityId);
        nf.writeByte(this.eventId);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleEntityEvent(this);
    }
    
    public Entity getEntity(final Level bru) {
        return bru.getEntity(this.entityId);
    }
    
    public byte getEventId() {
        return this.eventId;
    }
}
