package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundRenameItemPacket implements Packet<ServerGamePacketListener> {
    private String name;
    
    public ServerboundRenameItemPacket() {
    }
    
    public ServerboundRenameItemPacket(final String string) {
        this.name = string;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.name = nf.readUtf(32767);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.name);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleRenameItem(this);
    }
    
    public String getName() {
        return this.name;
    }
}
