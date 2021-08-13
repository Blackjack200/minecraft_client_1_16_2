package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundOpenSignEditorPacket implements Packet<ClientGamePacketListener> {
    private BlockPos pos;
    
    public ClientboundOpenSignEditorPacket() {
    }
    
    public ClientboundOpenSignEditorPacket(final BlockPos fx) {
        this.pos = fx;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleOpenSignEditor(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
