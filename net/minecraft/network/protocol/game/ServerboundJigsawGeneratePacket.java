package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundJigsawGeneratePacket implements Packet<ServerGamePacketListener> {
    private BlockPos pos;
    private int levels;
    private boolean keepJigsaws;
    
    public ServerboundJigsawGeneratePacket() {
    }
    
    public ServerboundJigsawGeneratePacket(final BlockPos fx, final int integer, final boolean boolean3) {
        this.pos = fx;
        this.levels = integer;
        this.keepJigsaws = boolean3;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.levels = nf.readVarInt();
        this.keepJigsaws = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        nf.writeVarInt(this.levels);
        nf.writeBoolean(this.keepJigsaws);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleJigsawGenerate(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public int levels() {
        return this.levels;
    }
    
    public boolean keepJigsaws() {
        return this.keepJigsaws;
    }
}
