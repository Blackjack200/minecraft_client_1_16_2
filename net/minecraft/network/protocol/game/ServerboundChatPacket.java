package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundChatPacket implements Packet<ServerGamePacketListener> {
    private String message;
    
    public ServerboundChatPacket() {
    }
    
    public ServerboundChatPacket(String string) {
        if (string.length() > 256) {
            string = string.substring(0, 256);
        }
        this.message = string;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.message = nf.readUtf(256);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.message);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleChat(this);
    }
    
    public String getMessage() {
        return this.message;
    }
}
