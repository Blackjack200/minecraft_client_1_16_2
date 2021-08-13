package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundLevelEventPacket implements Packet<ClientGamePacketListener> {
    private int type;
    private BlockPos pos;
    private int data;
    private boolean globalEvent;
    
    public ClientboundLevelEventPacket() {
    }
    
    public ClientboundLevelEventPacket(final int integer1, final BlockPos fx, final int integer3, final boolean boolean4) {
        this.type = integer1;
        this.pos = fx.immutable();
        this.data = integer3;
        this.globalEvent = boolean4;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.type = nf.readInt();
        this.pos = nf.readBlockPos();
        this.data = nf.readInt();
        this.globalEvent = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeInt(this.type);
        nf.writeBlockPos(this.pos);
        nf.writeInt(this.data);
        nf.writeBoolean(this.globalEvent);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleLevelEvent(this);
    }
    
    public boolean isGlobalEvent() {
        return this.globalEvent;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getData() {
        return this.data;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
