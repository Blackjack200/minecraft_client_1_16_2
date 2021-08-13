package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundRemoveEntitiesPacket implements Packet<ClientGamePacketListener> {
    private int[] entityIds;
    
    public ClientboundRemoveEntitiesPacket() {
    }
    
    public ClientboundRemoveEntitiesPacket(final int... arr) {
        this.entityIds = arr;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityIds = new int[nf.readVarInt()];
        for (int integer3 = 0; integer3 < this.entityIds.length; ++integer3) {
            this.entityIds[integer3] = nf.readVarInt();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityIds.length);
        for (final int integer6 : this.entityIds) {
            nf.writeVarInt(integer6);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleRemoveEntity(this);
    }
    
    public int[] getEntityIds() {
        return this.entityIds;
    }
}
