package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundContainerButtonClickPacket implements Packet<ServerGamePacketListener> {
    private int containerId;
    private int buttonId;
    
    public ServerboundContainerButtonClickPacket() {
    }
    
    public ServerboundContainerButtonClickPacket(final int integer1, final int integer2) {
        this.containerId = integer1;
        this.buttonId = integer2;
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleContainerButtonClick(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readByte();
        this.buttonId = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeByte(this.buttonId);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public int getButtonId() {
        return this.buttonId;
    }
}
