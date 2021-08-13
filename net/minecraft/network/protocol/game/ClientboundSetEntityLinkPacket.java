package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetEntityLinkPacket implements Packet<ClientGamePacketListener> {
    private int sourceId;
    private int destId;
    
    public ClientboundSetEntityLinkPacket() {
    }
    
    public ClientboundSetEntityLinkPacket(final Entity apx1, @Nullable final Entity apx2) {
        this.sourceId = apx1.getId();
        this.destId = ((apx2 != null) ? apx2.getId() : 0);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.sourceId = nf.readInt();
        this.destId = nf.readInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(this.sourceId);
        nf.writeInt(this.destId);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleEntityLinkPacket(this);
    }
    
    public int getSourceId() {
        return this.sourceId;
    }
    
    public int getDestId() {
        return this.destId;
    }
}
