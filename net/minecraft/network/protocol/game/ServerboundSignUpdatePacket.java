package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ServerboundSignUpdatePacket implements Packet<ServerGamePacketListener> {
    private BlockPos pos;
    private String[] lines;
    
    public ServerboundSignUpdatePacket() {
    }
    
    public ServerboundSignUpdatePacket(final BlockPos fx, final String string2, final String string3, final String string4, final String string5) {
        this.pos = fx;
        this.lines = new String[] { string2, string3, string4, string5 };
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
        this.lines = new String[4];
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            this.lines[integer3] = nf.readUtf(384);
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
        for (int integer3 = 0; integer3 < 4; ++integer3) {
            nf.writeUtf(this.lines[integer3]);
        }
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleSignUpdate(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public String[] getLines() {
        return this.lines;
    }
}
