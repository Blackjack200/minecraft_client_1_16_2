package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundBlockEntityDataPacket implements Packet<ClientGamePacketListener> {
    private BlockPos pos;
    private int type;
    private CompoundTag tag;
    
    public ClientboundBlockEntityDataPacket() {
    }
    
    public ClientboundBlockEntityDataPacket(final BlockPos fx, final int integer, final CompoundTag md) {
        this.pos = fx;
        this.type = integer;
        this.tag = md;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.type = nf.readUnsignedByte();
        this.tag = nf.readNbt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeByte((byte)this.type);
        nf.writeNbt(this.tag);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleBlockEntityData(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public int getType() {
        return this.type;
    }
    
    public CompoundTag getTag() {
        return this.tag;
    }
}
