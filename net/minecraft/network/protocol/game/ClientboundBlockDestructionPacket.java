package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundBlockDestructionPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private BlockPos pos;
    private int progress;
    
    public ClientboundBlockDestructionPacket() {
    }
    
    public ClientboundBlockDestructionPacket(final int integer1, final BlockPos fx, final int integer3) {
        this.id = integer1;
        this.pos = fx;
        this.progress = integer3;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.pos = nf.readBlockPos();
        this.progress = nf.readUnsignedByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeBlockPos(this.pos);
        nf.writeByte(this.progress);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleBlockDestruction(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public int getProgress() {
        return this.progress;
    }
}
