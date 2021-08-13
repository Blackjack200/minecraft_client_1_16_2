package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetEntityDataPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private List<SynchedEntityData.DataItem<?>> packedItems;
    
    public ClientboundSetEntityDataPacket() {
    }
    
    public ClientboundSetEntityDataPacket(final int integer, final SynchedEntityData uv, final boolean boolean3) {
        this.id = integer;
        if (boolean3) {
            this.packedItems = uv.getAll();
            uv.clearDirty();
        }
        else {
            this.packedItems = uv.packDirty();
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.packedItems = SynchedEntityData.unpack(nf);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        SynchedEntityData.pack(this.packedItems, nf);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetEntityData(this);
    }
    
    public List<SynchedEntityData.DataItem<?>> getUnpackedData() {
        return this.packedItems;
    }
    
    public int getId() {
        return this.id;
    }
}
