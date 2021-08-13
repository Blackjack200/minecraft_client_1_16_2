package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundBlockEntityTagQuery implements Packet<ServerGamePacketListener> {
    private int transactionId;
    private BlockPos pos;
    
    public ServerboundBlockEntityTagQuery() {
    }
    
    public ServerboundBlockEntityTagQuery(final int integer, final BlockPos fx) {
        this.transactionId = integer;
        this.pos = fx;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.transactionId = nf.readVarInt();
        this.pos = nf.readBlockPos();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.transactionId);
        nf.writeBlockPos(this.pos);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleBlockEntityTagQuery(this);
    }
    
    public int getTransactionId() {
        return this.transactionId;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
}
