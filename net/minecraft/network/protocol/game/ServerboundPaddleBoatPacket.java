package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPaddleBoatPacket implements Packet<ServerGamePacketListener> {
    private boolean left;
    private boolean right;
    
    public ServerboundPaddleBoatPacket() {
    }
    
    public ServerboundPaddleBoatPacket(final boolean boolean1, final boolean boolean2) {
        this.left = boolean1;
        this.right = boolean2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.left = nf.readBoolean();
        this.right = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBoolean(this.left);
        nf.writeBoolean(this.right);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePaddleBoat(this);
    }
    
    public boolean getLeft() {
        return this.left;
    }
    
    public boolean getRight() {
        return this.right;
    }
}
