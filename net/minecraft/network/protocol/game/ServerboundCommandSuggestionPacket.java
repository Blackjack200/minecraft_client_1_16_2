package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundCommandSuggestionPacket implements Packet<ServerGamePacketListener> {
    private int id;
    private String command;
    
    public ServerboundCommandSuggestionPacket() {
    }
    
    public ServerboundCommandSuggestionPacket(final int integer, final String string) {
        this.id = integer;
        this.command = string;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.command = nf.readUtf(32500);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeUtf(this.command, 32500);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleCustomCommandSuggestions(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getCommand() {
        return this.command;
    }
}
