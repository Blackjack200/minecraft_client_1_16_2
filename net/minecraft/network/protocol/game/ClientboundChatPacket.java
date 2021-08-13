package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.util.UUID;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundChatPacket implements Packet<ClientGamePacketListener> {
    private Component message;
    private ChatType type;
    private UUID sender;
    
    public ClientboundChatPacket() {
    }
    
    public ClientboundChatPacket(final Component nr, final ChatType no, final UUID uUID) {
        this.message = nr;
        this.type = no;
        this.sender = uUID;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.message = nf.readComponent();
        this.type = ChatType.getForIndex(nf.readByte());
        this.sender = nf.readUUID();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeComponent(this.message);
        nf.writeByte(this.type.getIndex());
        nf.writeUUID(this.sender);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleChat(this);
    }
    
    public Component getMessage() {
        return this.message;
    }
    
    public boolean isSystem() {
        return this.type == ChatType.SYSTEM || this.type == ChatType.GAME_INFO;
    }
    
    public ChatType getType() {
        return this.type;
    }
    
    public UUID getSender() {
        return this.sender;
    }
    
    public boolean isSkippable() {
        return true;
    }
}
