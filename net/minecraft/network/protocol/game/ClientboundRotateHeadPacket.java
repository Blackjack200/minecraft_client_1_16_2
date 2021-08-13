package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundRotateHeadPacket implements Packet<ClientGamePacketListener> {
    private int entityId;
    private byte yHeadRot;
    
    public ClientboundRotateHeadPacket() {
    }
    
    public ClientboundRotateHeadPacket(final Entity apx, final byte byte2) {
        this.entityId = apx.getId();
        this.yHeadRot = byte2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
        this.yHeadRot = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
        nf.writeByte(this.yHeadRot);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleRotateMob(this);
    }
    
    public Entity getEntity(final Level bru) {
        return bru.getEntity(this.entityId);
    }
    
    public byte getYHeadRot() {
        return this.yHeadRot;
    }
}
