package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundResourcePackPacket implements Packet<ServerGamePacketListener> {
    private Action action;
    
    public ServerboundResourcePackPacket() {
    }
    
    public ServerboundResourcePackPacket(final Action a) {
        this.action = a;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.action = nf.<Action>readEnum(Action.class);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.action);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleResourcePackResponse(this);
    }
    
    public enum Action {
        SUCCESSFULLY_LOADED, 
        DECLINED, 
        FAILED_DOWNLOAD, 
        ACCEPTED;
    }
}
