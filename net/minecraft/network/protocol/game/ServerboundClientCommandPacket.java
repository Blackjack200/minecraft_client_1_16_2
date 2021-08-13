package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundClientCommandPacket implements Packet<ServerGamePacketListener> {
    private Action action;
    
    public ServerboundClientCommandPacket() {
    }
    
    public ServerboundClientCommandPacket(final Action a) {
        this.action = a;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.action = nf.<Action>readEnum(Action.class);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.action);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleClientCommand(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public enum Action {
        PERFORM_RESPAWN, 
        REQUEST_STATS;
    }
}
